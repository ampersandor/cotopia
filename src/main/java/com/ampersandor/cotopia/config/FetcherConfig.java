package com.ampersandor.cotopia.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.ampersandor.cotopia.util.CodingPlatformFetcher;
import java.util.Map;

@Configuration
public class FetcherConfig {
    @Bean
    public Map<String, CodingPlatformFetcher> codingPlatformFetchers(
        @Qualifier("leetcodeFetcher") CodingPlatformFetcher leetcodeFetcher,
        @Qualifier("acmicpcFetcher") CodingPlatformFetcher acmicpcFetcher
    ) {
        return Map.of(
            "leetcode", leetcodeFetcher,
            "acmicpc", acmicpcFetcher
        );
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
