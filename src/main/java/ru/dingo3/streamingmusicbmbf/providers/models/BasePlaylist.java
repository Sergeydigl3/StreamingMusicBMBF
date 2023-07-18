package ru.dingo3.streamingmusicbmbf.providers.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasePlaylist implements Serializable {
    String id;
    String image;
    String title;
    Integer musicCount;
    Boolean sync = false;

    Boolean nowSyncing = false;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BasePlaylist) {
            return ((BasePlaylist) obj).getId().equals(id);
        }
        return false;
    }
}
