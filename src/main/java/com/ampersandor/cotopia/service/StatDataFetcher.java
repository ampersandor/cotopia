package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.model.StatResponse;

public interface StatDataFetcher {
    StatResponse fetchStat(String username);
}
