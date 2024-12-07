package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.dto.request.StatResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class StatDataFetcherTest {
    RestTemplate restTemplate;
    StatDataFetcher statDataFetcher;

    @BeforeEach
    public void beforeEach(){
        restTemplate = new RestTemplate();
        statDataFetcher = new StatDataFetcherImpl(restTemplate);

    }

    @Test
    void connectionTest(){
        StatResponse res = statDataFetcher.fetchStat("ampersandor");
        Assertions.assertThat(res.status()).isEqualTo("success");

        StatResponse res2 = statDataFetcher.fetchStat("ampersandordsldkfsdfsd");
        Assertions.assertThat(res2.status()).isEqualTo("error");

    }

}

