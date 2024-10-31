package com.ampersandor.leettrack.service;

import com.ampersandor.leettrack.model.StatResponse;

public interface StatDataFetcher {
    StatResponse fetchStat(String username);
}
