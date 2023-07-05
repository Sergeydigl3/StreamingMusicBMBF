package ru.dingo3.streamingmusicbmbf.models;

import lombok.Data;

@Data
public class DownloadHashResponse {
    private String host;
    private String path;
    private String ts;
    private int region;
    private String s;
}
