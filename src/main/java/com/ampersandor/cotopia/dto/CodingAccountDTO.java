package com.ampersandor.cotopia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.ampersandor.cotopia.entity.CodingAccount;
import com.ampersandor.cotopia.entity.User;
public class CodingAccountDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private String platform;
        private String url;
        private String platformId;

        public CodingAccount toEntity(User user) {
            return CodingAccount.builder()
                    .platform(this.platform)
                    .url(this.url)
                    .platformId(this.platformId)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private Long id;
        private String platform;
        private String url;
        private String platformId;

        public CodingAccount toEntity(User user) {
            return CodingAccount.builder()
                    .id(this.id)
                    .platform(this.platform)
                    .url(this.url)
                    .platformId(this.platformId)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String platform;
        private String url;
        private String platformId;

        public static Response from(CodingAccount account) {
            return Response.builder()
                    .id(account.getId())
                    .platform(account.getPlatform())
                    .url(account.getUrl())
                    .platformId(account.getPlatformId())
                    .build();
        }
    }
}


