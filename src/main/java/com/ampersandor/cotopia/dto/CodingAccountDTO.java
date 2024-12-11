package com.ampersandor.cotopia.dto;

import com.ampersandor.cotopia.entity.CodingAccount;

public class CodingAccountDTO {
    private Long id;
    private String platform;
    private String url;
    private String platformId;

    public CodingAccountDTO() {
    }

    public CodingAccountDTO(Long id, String platform, String url, String platformId) {
        this.id = id;
        this.platform = platform;
        this.url = url;
        this.platformId = platformId;
    }

    public Long getId() { return id; }
    public String getPlatform() { return platform; }
    public String getUrl() { return url; }
    public String getPlatformId() { return platformId; }

    public static CodingAccountDTO from(CodingAccount account) {
        return new CodingAccountDTO(
            account.getId(),
            account.getPlatform(),
            account.getUrl(),
            account.getPlatformId()
        );
    }
} 