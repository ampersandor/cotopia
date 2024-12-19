package com.ampersandor.cotopia.util;

import java.util.Collections;

import org.springframework.stereotype.Component;
@Component("acmicpc")
public class AcmicpcFetcher implements CodingPlatformFetcher {
    
    @Override
    public StatResponse fetchStat(String username) {
        return new StatResponse("200", "temp", 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0,
                0, 0, Collections.emptyMap());
    }

    @Override
    public String getPlatform() {  // 플랫폼 식별자 추가
        return "acmicpc";
    }
}
