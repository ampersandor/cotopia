package com.ampersandor.cotopia.util;

public interface CodingPlatformFetcher {
    StatResponse fetchStat(String username);
    String getPlatform();
}

