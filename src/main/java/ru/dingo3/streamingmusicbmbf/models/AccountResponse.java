package ru.dingo3.streamingmusicbmbf.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private InvocationInfo invocationInfo;
    private Result result;

    @Data
    public static class InvocationInfo {
        private String hostname;
        @JsonProperty("req-id")
        private String requestId;
        @JsonProperty("exec-duration-millis")
        private String executionDurationMillis;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private AccountInfo account;

        @Data
        public static class AccountInfo {
            private String now;
            private int uid;
            private String login;
            private int region;
            private String fullName;
            private String secondName;
            private String firstName;
            private String displayName;
            private boolean serviceAvailable;
            private boolean hostedUser;
            @JsonProperty("passport-phones")
            private List<PassportPhone> passportPhones;
            private String registeredAt;
            private boolean child;
            private boolean nonOwnerFamilyMember;
        }

        @Data
        public static class PassportPhone {
            private String phone;
        }
    }
}
