package com.despegar.p13n.hestia.snapshot;

import java.io.Serializable;
import java.lang.annotation.Inherited;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.despegar.framework.media.manager.MediaManager;
import com.despegar.framework.resource.Reloadable;
import com.despegar.framework.spring.snapshot.Cronable;
import com.despegar.library.version.Artifact;
import com.despegar.library.version.MavenVersionExtractor;

/**
 * Simplified Snapshot abstraction. :)
 * 
 * @author ejoncas
 *
 * @param <T> generic type extending {@link Serializable} that is contained in the snapshot.
 */
public abstract class AbstractAsyncSnapshot<T extends Serializable>
    implements Reloadable, ApplicationContextAware, InitializingBean, Cronable {

    protected enum SnapshotState {
        UNLOADED, LOADING, LOADED
    }

    protected SnapshotState snapshotState = SnapshotState.UNLOADED;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext applicationContext;

    private MediaManager<String, T> mediaManager;

    protected volatile T snapshot;

    private String cronExpression;

    private ExecutorService executors = Executors.newSingleThreadExecutor();

    /**
     * Implement here the access to the data source and 
     * @return an instance of T user data.
     */
    protected abstract T getData();

    /**
     * @return the snapshot identifier.
     */
    public abstract String getIdentifier();

    /**
     * Tries to load the data using the configured {@link MediaManager}, if there are no data found, 
     * it triggers the user provided (abstract) {@link #getData()} method. 
     */
    private void retrieveData() {
        this.snapshotState = SnapshotState.LOADING;
        this.LOGGER.info("Executing retrieveData for " + this.getIdentifier() + " snapshot.");

        String identifier = this.getFullIdentifier();
        boolean mediaExists = this.mediaManager.find(identifier);
        try {
            T data = null;
            if (mediaExists) {
                try {
                    data = this.mediaManager.get(identifier);
                    this.snapshotState = SnapshotState.LOADED;
                } catch (Exception e) {
                    this.LOGGER.error("Unknown error -- trying to reload", e);
                    this.mediaManager.remove(identifier);
                    data = null;
                }
            }
            if (data == null) {
                this.LOGGER.info("Loading data async. Snapshot will be null until getData call finish");
                this.executors.execute(new GetDataAsync());
            } else {
                this.snapshot = data;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.LOGGER.info("Done!");
    }

    private class GetDataAsync
        implements Runnable {
        @Override
        public void run() {
            AbstractAsyncSnapshot.this.snapshot = AbstractAsyncSnapshot.this.getData();
            AbstractAsyncSnapshot.this.mediaManager.put(AbstractAsyncSnapshot.this.getFullIdentifier(),
                AbstractAsyncSnapshot.this.snapshot);
            AbstractAsyncSnapshot.this.snapshotState = SnapshotState.LOADED;
        }
    }

    /**
     * Reload method is used 
     */
    public void reload() {
        this.LOGGER.debug("Executing reload for " + this.getIdentifier() + " snapshot...");

        this.snapshotState = SnapshotState.LOADING;
        String identifier = this.getFullIdentifier();
        T data = this.getData();

        try {
            if (data != null) {
                this.snapshot = data;
                boolean result = true;

                if (this.mediaManager.find(identifier)) {
                    result = this.mediaManager.remove(identifier);
                }

                if (!result) {
                    this.LOGGER.warn("Problem deleting file while reloading data");
                } else {
                    this.mediaManager.put(identifier, this.snapshot);
                    this.LOGGER.info("Succesfully regenerated file for data. [" + identifier + "]");
                }
            } else {
                this.LOGGER.debug("Snapshot getData method returned null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.snapshotState = SnapshotState.LOADED;
        }
    }

    private String getFullIdentifier() {
        String version = this.findOutVersion();
        return this.getIdentifier() + (version != null ? "-" + version : "");
    }

    protected String findOutVersion() {
        if (this.applicationContext != null) {
            Artifact artifact = MavenVersionExtractor.findArtifact();
            if (artifact != null) {
                this.LOGGER.debug("Using [" + artifact.version + "] as version for [" + artifact.groupId + ":"
                    + artifact.artifactId + "].");
                return artifact.version;
            } else {
                this.LOGGER.warn("No default artifact was found, assuming no version.");
            }
        } else {
            this.LOGGER.warn("No valid applicationContext was supplied, assuming no version.");
        }

        return null;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() throws Exception {
        this.retrieveData();
    }

    @Required
    public void setMediaManager(MediaManager<String, T> mediaManager) {
        this.mediaManager = mediaManager;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * 
     * {@link Inherited}
     */
    public String getCronExpression() {
        return this.cronExpression;
    }

}
