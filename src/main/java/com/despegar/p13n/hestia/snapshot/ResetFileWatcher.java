package com.despegar.p13n.hestia.snapshot;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.library.snapshot.backup.BackupFileManager;

public class ResetFileWatcher
    implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetFileWatcher.class);
    private static final int WATCH_FRECUENCY = 5000; // in millis


    private final UpdateControlQuerableSnapshot.SnapshotDownladTrigger trigger;
    private final String[] whatchedSnapshots;
    private BackupFileManager backupFileManager;


    public ResetFileWatcher(UpdateControlQuerableSnapshot.SnapshotDownladTrigger trigger,
        BackupFileManager backupFileManager, String... snapshotAliases) {
        this.trigger = trigger;
        this.backupFileManager = backupFileManager;
        this.whatchedSnapshots = snapshotAliases;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (String watchedSnapshot : this.whatchedSnapshots) {
                    File resetFile = this.backupFileManager.resetFile(watchedSnapshot);
                    if (resetFile.exists()) {
                        LOGGER.info("Resetting {} snapshot. Triggering full download", watchedSnapshot);
                        this.trigger.triggerDownload();
                    }
                    resetFile.delete();
                }

                Thread.sleep(WATCH_FRECUENCY);
            } catch (Exception e) {
                LOGGER.error("Error in ResetFileWatcher", e);
            }
        }
    }

}
