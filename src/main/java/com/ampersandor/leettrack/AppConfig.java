package com.ampersandor.leettrack;

import com.ampersandor.leettrack.repository.MemberRepository;
import com.ampersandor.leettrack.repository.MemoryMemberRepository;
import com.ampersandor.leettrack.repository.MemoryStatRepository;
import com.ampersandor.leettrack.repository.StatRepository;
import com.ampersandor.leettrack.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository(), statService());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
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
