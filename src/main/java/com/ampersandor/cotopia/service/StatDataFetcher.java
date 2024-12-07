package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.dto.request.StatResponse;

public interface StatDataFetcher {
    StatResponse fetchStat(String username);
}
