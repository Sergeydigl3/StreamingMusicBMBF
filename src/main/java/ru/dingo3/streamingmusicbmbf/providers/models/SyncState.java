package ru.dingo3.streamingmusicbmbf.providers.models;

public enum SyncState {
    NOT_DOWNLOADED,
    DOWNLOADING,
    DOWNLOADED,
    CONVERSION,
    CONVERTED,
    SYNCING,
    SYNCED
}
