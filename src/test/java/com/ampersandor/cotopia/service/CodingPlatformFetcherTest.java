package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.util.StatResponse;
import com.ampersandor.cotopia.util.CodingPlatformFetcher;
import com.ampersandor.cotopia.util.LeetcodeFetcher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class CodingPlatformFetcherTest {
    RestTemplate restTemplate;
    CodingPlatformFetcher codingPlatformFetcher;

    @BeforeEach
    public void beforeEach(){
        restTemplate = new RestTemplate();
        codingPlatformFetcher = new LeetcodeFetcher(restTemplate);

    }

    @Test
    void connectionTest(){
        StatResponse res = codingPlatformFetcher.fetchStat("ampersandor");
        Assertions.assertThat(res.status()).isEqualTo("success");

        StatResponse res2 = codingPlatformFetcher.fetchStat("ampersandordsldkfsdfsd");
        Assertions.assertThat(res2.status()).isEqualTo("error");

    }

}

