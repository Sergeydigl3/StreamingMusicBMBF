package ru.dingo3.streamingmusicbmbf.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistResponse {
    private InvocationInfo invocationInfo;
    private PlaylistResult result;

    @Data
    public static class InvocationInfo {
        @JsonProperty("exec-duration-millis")
        private int executionDurationMillis;
        private String hostname;
        @JsonProperty("req-id")
        private String requestId;
        @JsonProperty("app-name")
        private String appName;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlaylistResult {
        private Owner owner;
        private String playlistUuid;
        private boolean available;
        private int uid;
        private int kind;
        private String title;
        private int revision;
        private int snapshot;
        private int trackCount;
        private String visibility;
        private boolean collective;
        private String created;
        private String modified;
        private boolean isBanner;
        private boolean isPremiere;
        private int durationMs;
        private Cover cover;
        private String ogImage;
        private ArrayList<Track> tracks;
        private List<Object> tags;
        private int likesCount;
    }

    @Data
    public static class Owner {
        private int uid;
        private String login;
        private String name;
        private String sex;
        private boolean verified;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cover {
        private String type;
        private String dir;
        private String version;
        private String uri;
        private boolean custom;
    }

    @Data
    public static class Track {
        private int id;
        private TrackDetails track;
        private String timestamp;
        private int originalIndex;
        private boolean recent;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TrackDetails {
        private String id;
        private String realId;
        private String title;
        private String trackSource;
        private Major major;
        private boolean available;
        private boolean availableForPremiumUsers;
        private boolean availableFullWithoutPermission;
        private List<String> disclaimers;
        private List<String> availableForOptions;
        private int durationMs;
        private String storageDir;
        private int fileSize;
        private R128 r128;
        private Fade fade;
        private int previewDurationMs;
        private List<Artist> artists;
        private List<Album> albums;
        private String coverUri;
        private String ogImage;
        private boolean lyricsAvailable;
        private LyricsInfo lyricsInfo;
        private String type;
        private boolean rememberPosition;
        private String backgroundVideoUri;
        private String trackSharingFlag;
        private String playerId;
    }

    @Data
    public static class Major {
        private int id;
        private String name;
    }

    @Data
    public static class R128 {
        private double i;
        private double tp;
    }

    @Data
    public static class Fade {
        private double inStart;
        private double inStop;
        private double outStart;
        private double outStop;
    }

    @Data
    public static class Artist {
        private int id;
        private String name;
        private boolean various;
        private boolean composer;
        private Cover cover;
        private List<Object> genres;
        private List<Object> disclaimers;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Album {
        private int id;
        private String title;
        private String metaType;
        private String version;
        private String contentWarning;
        private int year;
        private String releaseDate;
        private String coverUri;
        private String ogImage;
        private String genre;
        private int trackCount;
        private boolean recent;
        private boolean veryImportant;
        private List<Artist> artists;
        private List<Label> labels;
        private boolean available;
        private boolean availableForPremiumUsers;
        private List<Object> disclaimers;
        private List<String> availableForOptions;
        private boolean availableForMobile;
        private boolean availablePartially;
        private List<Integer> bests;
        private TrackPosition trackPosition;
    }

    @Data
    public static class Label {
        private int id;
        private String name;
    }

    @Data
    public static class TrackPosition {
        private int volume;
        private int index;
    }

    @Data
    public static class LyricsInfo {
        private boolean hasAvailableSyncLyrics;
        private boolean hasAvailableTextLyrics;
    }
}