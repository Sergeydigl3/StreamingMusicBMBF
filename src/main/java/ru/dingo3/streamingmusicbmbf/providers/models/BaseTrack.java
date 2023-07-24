package ru.dingo3.streamingmusicbmbf.providers.models;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseTrack implements Serializable {
    String id;
    String title;
    String artist;
    SyncState syncState = SyncState.NOT_DOWNLOADED;

    String localPath = "";

    String mapPath = "";

    Boolean nowSyncing = false;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseTrack) {
            return ((BaseTrack) obj).getId().equals(id);
        }
        return false;
    }
}

