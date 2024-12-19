package com.ampersandor.cotopia.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.ampersandor.cotopia.util.CodingPlatformFetcher;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Configuration
public class FetcherConfig {
    @Bean
    public Map<String, CodingPlatformFetcher> codingPlatformFetchers(
        List<CodingPlatformFetcher> fetchers
    ) {
        return fetchers.stream()
            .collect(Collectors.toMap(
                CodingPlatformFetcher::getPlatform,
                fetcher -> fetcher
            ));
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
