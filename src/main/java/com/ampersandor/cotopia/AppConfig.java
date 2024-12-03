package com.ampersandor.cotopia;

import com.ampersandor.cotopia.repository.*;
import com.ampersandor.cotopia.service.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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
        return new JpaStatRepository(em);
    }

    @Bean
    public StatDataFetcher statDataFetcher(){
        return new StatDataFetcherImpl(new RestTemplate());
    }

    @Bean
    public StatScheduler statScheduler() { return new StatScheduler(statService(), memberService());}

    @Bean
    public MemberLikeServiceImpl memberLikeService() { return new MemberLikeServiceImpl(redisTemplate(), memberLikeRepository());}

    @Bean
    public MemberLikeRepository memberLikeRepository() {
        return new JpaMemberLikeRepository();
    }

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

}
