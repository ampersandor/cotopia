package com.ampersandor.leettrack;

import com.ampersandor.leettrack.repository.*;
import com.ampersandor.leettrack.service.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private final EntityManager em;

    @Autowired
    public AppConfig(EntityManager em){
        this.em = em;
    }

    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository(), statService());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new JpaMemberRepository(em);
    }

    @Bean
    public StatService statService(){
        return new StatServiceImpl(statRepository(), statDataFetcher());
    }

    @Bean
    public StatRepository statRepository(){
        return new MemoryStatRepository();
    }

    @Bean
    public StatDataFetcher statDataFetcher(){
        return new StatDataFetcherImpl(new RestTemplate());
    }



}
