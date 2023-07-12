package ru.dingo3.streamingmusicbmbf.providers.models;
import lombok.Data;
@Data
public class BaseTrack {
    Integer id;
    String title;
    String artist;
    Boolean syncing;
}
