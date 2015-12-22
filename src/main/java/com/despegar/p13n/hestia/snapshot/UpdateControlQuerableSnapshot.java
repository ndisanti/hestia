package com.despegar.p13n.hestia.snapshot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.library.rest.HttpResponse;
import com.despegar.library.rest.HttpStatus;
import com.despegar.library.rest.RestConnector;
import com.despegar.library.snapshot.QuereableSnapshot;
import com.despegar.library.snapshot.backup.BackupFileManager;
import com.despegar.library.snapshot.backup.SnapshotBackup;
import com.despegar.library.snapshot.backup.SnapshotBackupManager;
import com.despegar.library.snapshot.index.Index;
import com.despegar.library.snapshot.index.IndexDefinition;
import com.despegar.library.snapshot.index.query.Query;
import com.despegar.library.snapshot.news.SnapshotNews;
import com.despegar.library.snapshot.news.SnapshotNewsDeserializer;
import com.despegar.library.snapshot.thread.DaemonThreadFactory;
import com.despegar.library.snapshot.utils.FileNameExtractor;
import com.despegar.library.snapshot.utils.RestConnectorFactory;
import com.despegar.library.snapshot.utils.VersionUtils;


/**
 * 
 * Copied from {@link QuereableSnapshot} to override behaviour of updates, making it
 * possible to cancel updates. This is useful in map reduces which use snapshots and
 * loose their connections when querying for new snapshot
 * 
 * @author mmendez
 *
 * @param <T>
 */
public class UpdateControlQuerableSnapshot<T> {
    private static final String DEFAULT_SNAPSHOT_DIR = new File(System.getProperty("java.io.tmpdir"), "snapshots")
        .getAbsolutePath();

    private static final String X_SNAPSHOT_VERSION = "X-Snapshot-Version";

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateControlQuerableSnapshot.class);

    private static final int _1_HOUR = 3600;

    private ScheduledExecutorService updaterExecutor = Executors.newScheduledThreadPool(1, DaemonThreadFactory.instance);
    private ExecutorService resetExecutor = Executors.newSingleThreadExecutor(DaemonThreadFactory.instance);

    // Snapshot State
    protected Index<T> index;

    private String lastModified;

    private String snapshotCurrentVersion;

    private int maxAge = -1;

    private long nextUpdate = -1; // millis from epoch to perform next snapshot update


    // Next fields must be configured

    private RestConnector restConnector;

    private IndexDefinition<T> indexDefinition;

    private SnapshotNewsDeserializer<T> snapshotNewsDeserializer;

    private SnapshotBackupManager<T> snapshotBackupManager;

    private List<String> supportedVersions;

    private String path;

    BackupFileManager backupFileManager;

    // Alias to reset snapshot (optional)
    private final String[] aliases;

    private final boolean update;


    public UpdateControlQuerableSnapshot(IndexDefinition<T> indexDef, String authority, String path, boolean update,
        String... aliases) {
        this(indexDef, "http", authority, path, update, aliases);
    }

    public UpdateControlQuerableSnapshot(IndexDefinition<T> indexDef, String protocol, String authority, String path,
        boolean update, String... aliases) {
        this(RestConnectorFactory.build(protocol, authority), indexDef, path, update, aliases);
    }

    public UpdateControlQuerableSnapshot(RestConnector restConnector, IndexDefinition<T> indexDef, String path,
        boolean update, String... aliases) {
        this(restConnector, DEFAULT_SNAPSHOT_DIR, indexDef, path, update, aliases);
    }


    // SnapshotDir deberia ir entre path y aliases, pero en ese lugar colisiona con "String... aliases", las llamadas
    // hay que hacerlas explicitas
    // con QuereableSnapshot(restConnector, IndexDefinition<T> indexDef, String path, snapshotDir, null) y esto rompe
    // compatibilidad con los que
    // venian usando la version QuereableSnapshot(RestConnector restConnector, IndexDefinition<T> indexDef, String path,
    // String... aliases)
    public UpdateControlQuerableSnapshot(RestConnector restConnector, String snapshotDir, IndexDefinition<T> indexDef,
        String path, boolean update, String... aliases) {
        LOGGER.info("Update for path {} is " + (update ? "ENABLED" : "DISABLED"), path);

        this.update = update;
        this.restConnector = restConnector;

        this.path = path;
        this.supportedVersions = new ArrayList<String>();

        this.backupFileManager = BackupFileManager.createBackupFileManager(snapshotDir);

        String filename = FileNameExtractor.extractFilenameFromPath(this.path);

        this.indexDefinition = indexDef;
        this.snapshotBackupManager = new SnapshotBackupManager<T>(filename, this.indexDefinition, this.backupFileManager);
        this.snapshotNewsDeserializer = new SnapshotNewsDeserializer<T>(indexDef);
        this.aliases = aliases;

        this.index = new Index(indexDef);
    }

    /**
     * Modified to enable optional update of snapshot
     * @throws IOException
     */
    public void init() throws IOException {
        // check if there is a snapshot in file system
        boolean recovered = this.recoverBackup();

        if (!recovered) {
            // download snapshot from remote service
            this.download();
        }

        if (this.update) {
            this.updaterExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (UpdateControlQuerableSnapshot.this.nextUpdate < System.currentTimeMillis()) {
                            LOGGER.debug("{}: Executing update because it was scheduled to be performed at {} ",
                                UpdateControlQuerableSnapshot.this.path,
                                new Date(UpdateControlQuerableSnapshot.this.nextUpdate));
                            UpdateControlQuerableSnapshot.this.nextUpdate = -1;
                            UpdateControlQuerableSnapshot.this.update();
                        }
                    } catch (Exception e) {
                        // TODO: Better logging
                        LOGGER.error("{}: Error while updating", UpdateControlQuerableSnapshot.this.path, e);
                        if (UpdateControlQuerableSnapshot.this.maxAge > 0) {
                            LOGGER.debug("{}: Trying next update in {} seconds", UpdateControlQuerableSnapshot.this.path,
                                UpdateControlQuerableSnapshot.this.maxAge);
                            UpdateControlQuerableSnapshot.this
                                .triggerNextUpdateIn(UpdateControlQuerableSnapshot.this.maxAge);
                        } else {
                            LOGGER.debug("{}: Trying next update in 300 seconds", UpdateControlQuerableSnapshot.this.path);
                            UpdateControlQuerableSnapshot.this.triggerNextUpdateIn(300);
                        }
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);
        }

        String[] names = this.aliases != null ? new String[this.aliases.length + 1] : new String[1];
        names[0] = this.path;
        if (this.aliases != null) {
            System.arraycopy(this.aliases, 0, names, 1, this.aliases.length);
        }
        this.resetExecutor.execute(new ResetFileWatcher(new SnapshotDownladTrigger(), this.backupFileManager, names));
    }

    public T get(Query q) {
        Collection<T> items = this.index.getItems(q);
        if (items.size() > 1) {
            throw new RuntimeException("More than one item");
        }
        return items.size() == 0 ? null : items.iterator().next();
    }

    public Collection<T> getMulti(Query q) {
        return this.index.getItems(q);
    }

    public Collection<T> getAll() {
        return this.index.getItems().values();
    }

    public void addSupportedVersion(String version) {
        this.supportedVersions.add(version);
    }

    public String getLastModified() {
        return this.lastModified;
    }

    // ***************
    // private methods
    // ***************

    /*
     * ask the service for update since "lastUpdate"
     */
    private void update() throws IOException {
        LOGGER.debug("{}: Updating snapshot...", this.path);

        HttpResponse httpResponse = this.restConnector.get(this.path).accept("application/json")
            .withHeader(X_SNAPSHOT_VERSION, this.snapshotCurrentVersion).ifModifiedSince(this.lastModified).execute();

        if (httpResponse.getStatus() == HttpStatus.PARTIAL_CONTENT) {
            LOGGER.debug("{}: Partial content returned. Merging actual items with new ones", this.path);
            this.readPartialContent(httpResponse);

            // triger next update
            this.triggerNextUpdateIn(this.maxAge);

            this.afterUpdate();

            LOGGER.debug("{}: QuereableSnapshot updated successfully. Version {}", this.path, this.snapshotCurrentVersion);
        } else if (httpResponse.getStatus() == HttpStatus.OK) {
            LOGGER.debug("{}: Complete content returned. Replacing actual items with new ones", this.path);
            this.readFullContent(httpResponse);

            // triger next update
            this.triggerNextUpdateIn(this.maxAge);

            this.afterUpdate();

            LOGGER.debug("{}: QuereableSnapshot downloaded successfully. Version {}", this.path,
                this.snapshotCurrentVersion);
        } else if (httpResponse.getStatus() == HttpStatus.RESET_CONTENT) {
            // hash is no loger valid, we keep old version
            String version = httpResponse.getHeaders().get(X_SNAPSHOT_VERSION);
            LOGGER.error("{}: QuereableSnapshot version {} is no longer valid. New version is {}", this.path,
                this.snapshotCurrentVersion, version);

            LOGGER.error("{}: Updates will no longer be requested. Trying to download last supported version", this.path);

            this.triggerDownloadInBackground();

        } else if (httpResponse.getStatus() == HttpStatus.NOT_MODIFIED) {
            // we are up to date :)
            LOGGER.debug("{}: QuereableSnapshot update not necessary. NOT MODIFIED was returned.", this.path);

            // trigger next update
            this.maxAge = this.getMaxAge(httpResponse);
            this.triggerNextUpdateIn(this.maxAge);

        } else {
            // everything else is not known, so we failed :(
            LOGGER.error("{}: Unknown status {} in snapshot {}", this.path, httpResponse.getStatus(), this.path);

            // triger next update
            this.maxAge = this.getMaxAge(httpResponse);
            this.triggerNextUpdateIn(this.maxAge);
        }
    }

    /**
    * This method is call after a successfull update (partial or total . Default implementation doesn't do nothing.
    * Usefull to post-process snapshots when it is updated in subclasses
    */
    protected void afterUpdate() {
    }

    /**
    * Download the complete snapshot
    *
    * @throws IOException
    */
    private void download() throws IOException {
        LOGGER.debug("{}: Download snapshot...", this.path);

        HttpResponse httpResponse = this.restConnector.get(this.path).accept("application/json")
            .withHeader(X_SNAPSHOT_VERSION, VersionUtils.lastVersion(this.supportedVersions)).execute();

        if (httpResponse.getStatus() == HttpStatus.OK) {
            LOGGER.debug("{}: Complete content returned. Replacing actual items with new ones", this.path);

            this.readFullContent(httpResponse);
            this.triggerNextUpdateIn(this.maxAge);
        } else if (httpResponse.getStatus() == HttpStatus.RESET_CONTENT) {
            // version is no longer valid, we keep old version
            String version = httpResponse.getHeaders().get(X_SNAPSHOT_VERSION);
            LOGGER.error("{}: QuereableSnapshot version {} is no longer valid. New version is {}", this.path,
                this.snapshotCurrentVersion, version);

            LOGGER.error("{}: Updates will no longer be requested.", this.path);
            throw new RuntimeException("Could not download snapshot " + this.path + ". Unsupported version: " + version);

        } else {
            throw new RuntimeException("Could not download snapshot" + this.path + ". " + httpResponse.getStatus() + " - "
                + httpResponse.getBody());
        }
        LOGGER.debug("{}: QuereableSnapshot download successfuly. Version {}", this.path, this.snapshotCurrentVersion);
    }

    // ***************
    // Helper methods
    // ***************

    private void swap(Index<T> index, int maxAge, String lastModified, String snapshotVersion) {
        this.index = index;
        this.maxAge = maxAge;
        this.lastModified = lastModified;
        this.snapshotCurrentVersion = snapshotVersion;
    }

    /*
     * Use only when starting app
     * 
     * @return true iff backup could be recovered
     */
    private boolean recoverBackup() {
        LOGGER.debug("{}: Recovering backup for endpoint {}", this.path, this.path);

        SnapshotBackup<T> backup;
        if (this.supportedVersions.isEmpty()) {
            backup = this.snapshotBackupManager.readNewestBackup();
        } else {
            backup = this.snapshotBackupManager.readNewestBackupFromSupportedVersions(this.supportedVersions);
        }

        if (backup != null) {
            Index<T> index = new Index<T>(this.indexDefinition);
            index.indexAllItems(backup.getItems());
            this.swap(index, backup.getMaxAge(), backup.getLastModified(), backup.getSnapshotVersion());

            LOGGER.debug("{}: Backup for endpoint recovered. Version {} Last update {}", this.path,
                backup.getSnapshotVersion(), new Date(backup.getLastUpdate()));

            long nextUpdateInMillis = Math.max(0, backup.getLastUpdate() + this.maxAge * 1000 - System.currentTimeMillis());

            this.triggerNextUpdateIn((int) nextUpdateInMillis / 1000);
            return true;
        } else {
            LOGGER.debug("{}: Couldn't get backup for endpoint.", this.path);
            return false;
        }
    }

    private void readFullContent(HttpResponse httpResponse) throws IOException {
        // service sent only updates, we should merge updates and replace old
        // cache
        String version = httpResponse.getHeaders().get(X_SNAPSHOT_VERSION);

        if (this.isVersionSupported(version)) {
            long lastUpdate = System.currentTimeMillis();
            String lastModified = httpResponse.getHeaders().getLastModified();

            byte[] body = httpResponse.getBodyAsByteArray();
            SnapshotNews<T> news = this.snapshotNewsDeserializer.deserializeFull(new ByteArrayInputStream(body));
            Index<T> newIndex = new Index<T>(this.indexDefinition);
            newIndex.indexAllItems(news.getItemsToAdd());

            int maxAge = this.getMaxAge(httpResponse);
            this.snapshotBackupManager.writeBackupInBackground(version, new ByteArrayInputStream(body), lastUpdate,
                lastModified, maxAge);

            this.swap(newIndex, maxAge, lastModified, version);
        } else {
            throw new RuntimeException("Version unsupported " + version + " for snapshot " + this.path);
        }
    }

    private void readPartialContent(HttpResponse httpResponse) throws IOException {
        // service sent only updates, we should merge updates and replace old
        // cache
        String version = httpResponse.getHeaders().get(X_SNAPSHOT_VERSION);
        version = version != null ? version : "";

        if (this.isVersionSupported(version)) {

            if (!version.equalsIgnoreCase(this.snapshotCurrentVersion)) {
                LOGGER.warn("{}: QuereableSnapshot didn't return the asked version. Version asked {} ; version returned {}",
                    this.path, this.snapshotCurrentVersion, version);
            }

            byte[] body = httpResponse.getBodyAsByteArray();
            SnapshotNews<T> news = this.snapshotNewsDeserializer.deserializePartial(new ByteArrayInputStream(body));
            Index<T> mergedIndex = this.index.merge(news);


            long lastUpdate = System.currentTimeMillis();
            String lastModified = httpResponse.getHeaders().getLastModified();

            int maxAge = this.getMaxAge(httpResponse);
            this.snapshotBackupManager.updateBackupInBackground(version, new ByteArrayInputStream(body), lastUpdate,
                lastModified, maxAge);
            this.swap(mergedIndex, maxAge, lastModified, version);
        } else {
            throw new RuntimeException(
                String.format("%s: QuereableSnapshot returned an usupported version %s. Supported versions: %s", this.path,
                    version, this.supportedVersions));
        }
    }

    private int getMaxAge(HttpResponse httpResponse) {
        // TODO what if service doesn't send max-age ?
        String cacheControl = httpResponse.getHeaders().get("Cache-Control");
        if (cacheControl != null && cacheControl.trim().length() > 0) {
            String[] cacheControlHeaders = cacheControl.split(",");
            for (String cch : cacheControlHeaders) {
                String[] kv = cch.split("=");
                if (kv.length == 2) {
                    String k = kv[0];
                    String v = kv[1];
                    if (k.equalsIgnoreCase("max-age")) {
                        LOGGER.debug("Cache-Control: {} --- max-age: {}", cacheControl, v);
                        return Integer.parseInt(v);
                    }
                }
            }
        }
        LOGGER.warn("{}: QuereableSnapshot didn't send MAX-AGE header. Triggering next update in 1 hour.", this.path);
        return _1_HOUR;
    }

    private void triggerNextUpdateIn(int nextUpdateInSeconds) {
        LOGGER.debug("{}: Triggering next update in {} seconds.", this.path, nextUpdateInSeconds);
        this.nextUpdate = System.currentTimeMillis() + nextUpdateInSeconds * 1000;
    }

    private void downloadWithRetries() {
        boolean successful = false;
        int retries = 0;
        while (!successful && retries < 3) {
            try {
                UpdateControlQuerableSnapshot.this.download();
                successful = true;
            } catch (Exception e) {
                // TODO: Better logging
                LOGGER.error("{} Error while downloading in background. Retries {}", UpdateControlQuerableSnapshot.this.path,
                    retries, e);
                if (retries++ >= 3) {
                    LOGGER.error("{}: Max retries reached. Won't try to download again.",
                        UpdateControlQuerableSnapshot.this.path);
                    break;
                } else {
                    LOGGER.debug("{}: Will try to download again. Retries: {}", UpdateControlQuerableSnapshot.this.path,
                        retries);
                }
            }
        }
    }

    private void triggerDownloadInBackground() {
        LOGGER.debug("{}: Triggering download", this.path);
        this.updaterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                UpdateControlQuerableSnapshot.this.downloadWithRetries();
            }
        });
    }

    private boolean isVersionSupported(String version) {
        return this.supportedVersions.isEmpty() || this.supportedVersions.contains(version);
    }

    public boolean isUpdateEnabled() {
        return this.update;
    }

    // *************************
    // SETTERS for optional configuration
    // *************************

    public void setSnapshotReader(SnapshotNewsDeserializer<T> snapshotNewsDeserializer) {
        this.snapshotNewsDeserializer = snapshotNewsDeserializer;
    }

    public void setSnapshotBackupManager(SnapshotBackupManager<T> snapshotBackupManager) {
        this.snapshotBackupManager = snapshotBackupManager;
    }

    public void shutdown() {
        this.resetExecutor.shutdown();
        this.updaterExecutor.shutdown();
        this.restConnector.shutdown();
    }


    public class SnapshotDownladTrigger {
        public void triggerDownload() {
            UpdateControlQuerableSnapshot.this.downloadWithRetries();
        }
    }
}
