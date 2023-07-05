package ru.dingo3.streamingmusicbmbf.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadResponse {
    private InvocationInfo invocationInfo;
    private List<DownloadInfo> result;

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
    public static class DownloadInfo {
        private String codec;
        private boolean gain;
        private boolean preview;
        private String downloadInfoUrl;
        private boolean direct;
        private int bitrateInKbps;
    }
}
