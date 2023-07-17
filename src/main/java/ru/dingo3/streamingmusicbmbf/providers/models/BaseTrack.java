package ru.dingo3.streamingmusicbmbf.providers.models;
import lombok.Data;
@Data
public class BaseTrack {
    Integer id;
    String title;
    String artist;
    SyncState syncState = SyncState.NOT_DOWNLOADED;
}

public enum SyncState {
    NOT_DOWNLOADED,
    DOWNLOADING,
    DOWNLOADED,
    CONVERSION,
    CONVERTED,
    SYNCING,
    SYNCED
}