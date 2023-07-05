package ru.dingo3.streamingmusicbmbf.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlaylistsResponse {
    private InvocationInfo invocationInfo;
    private List<Playlist> result;

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
    public static class Playlist {
        private String playlistUuid;
        private String description;
        private String descriptionFormatted;
        private boolean available;
        private boolean collective;
        private Cover cover;
        private String created;
        private String modified;
        private String backgroundColor;
        private String textColor;
        private int durationMs;
        private boolean isBanner;
        private boolean isPremiere;
        private int kind;
        private String ogImage;
        private Owner owner;
        private List<Object> prerolls;
        private int revision;
        private int snapshot;
        private List<Tag> tags;
        private String title;
        private int trackCount;
        private int uid;
        private String visibility;
        private int likesCount;
        private List<Track> tracks;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Cover {
            private boolean custom;
            private String dir;
            private String type;
            private List<String> itemsUri;
            private String uri;
            private String version;
            private String error;
        }

        @Data
        public static class Owner {
            private String login;
            private String name;
            private String sex;
            private int uid;
            private boolean verified;
        }

        @Data
        public static class Tag {
            private String id;
            private String value;
        }

        @Data
        public static class Track {
            private int id;
            private int playCount;
            private boolean recent;
            private String timestamp;
            private TrackDetails track;

            @Data
            public static class TrackDetails {
                private List<Album> albums;
                private List<Artist> artists;
                private boolean available;
                private boolean availableForPremiumUsers;
                private boolean availableFullWithoutPermission;
                private String coverUri;
                private int durationMs;
                private int fileSize;
                private String id;
                private boolean lyricsAvailable;
                private Major major;
                private Normalization normalization;
                private String ogImage;
                private int previewDurationMs;
                private String realId;
                private boolean rememberPosition;
                private String storageDir;
                private String title;
                private String type;

                @Data
                public static class Album {
                    private int id;
                    private String error;
                    private String title;
                    private String type;
                    private String metaType;
                    private int year;
                    private String releaseDate;
                    private String coverUri;
                    private String ogImage;
                    private String genre;
                    private List<Object> buy;
                    private int trackCount;
                    private boolean recent;
                    private boolean veryImportant;
                    private List<Artist> artists;
                    private List<Label> labels;
                    private boolean available;
                    private boolean availableForPremiumUsers;
                    private boolean availableForMobile;
                    private boolean availablePartially;
                    private List<Integer> bests;
                    private List<Object> prerolls;
                    private List<List<Object>> volumes;

                    @Data
                    public static class Label {
                        private int id;
                        private String name;
                    }
                }

                @Data
                public static class Artist {
                    private boolean composer;
                    private Cover cover;
                    private List<Object> decomposed;
                    private List<Object> genres;
                    private String id;
                    private String name;
                    private boolean various;
                    private List<Object> popularTracks;
                    private boolean ticketsAvailable;
                    private List<String> regions;
                }

                @Data
                public static class Major {
                    private int id;
                    private String name;
                }

                @Data
                public static class Normalization {
                    private int gain;
                    private int peak;
                }
            }
        }
    }
}