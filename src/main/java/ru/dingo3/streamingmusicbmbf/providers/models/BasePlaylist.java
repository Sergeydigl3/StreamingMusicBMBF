package ru.dingo3.streamingmusicbmbf.providers.models;

import lombok.Data;

@Data
public class BasePlaylist {
    String id;
    String image;
    String title;
    Integer musicCount;
    Boolean syncing;
}
