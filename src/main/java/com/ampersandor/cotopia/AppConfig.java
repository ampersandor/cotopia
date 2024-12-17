package com.ampersandor.cotopia;

import com.ampersandor.cotopia.repository.*;
import com.ampersandor.cotopia.service.*;
import com.ampersandor.cotopia.util.CodingPlatformFetcher;
import com.ampersandor.cotopia.util.LeetcodeFetcher;
import jakarta.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private final EntityManager em;
    private final Map<String, CodingPlatformFetcher> fetchers;

    @Autowired
    public AppConfig(EntityManager em, Map<String, CodingPlatformFetcher> fetchers){
        this.em = em;
        this.fetchers = new HashMap<String, CodingPlatformFetcher>(Map.of("leetcode", new LeetcodeFetcher(new RestTemplate())));
    }

    @Bean
    public UserRepository userRepository(){
        return new JpaUserRepository(em);
    }

    @Bean
    public StatService statService(){
        return new StatServiceImpl(statRepository());
    }

    @Bean
    public StatRepository statRepository(){
        return new JpaStatRepository(em);
    }

    @Bean
    public StatScheduler statScheduler() { return new StatScheduler(codingAccountRepository(), statService(), fetchers);}

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CodingAccountService codingAccountService() { return new CodingAccountServiceImpl(codingAccountRepository());}

    @Bean
    public CodingAccountRepository codingAccountRepository(){
        return new JpaCodingAccountRepository(em);
    }


    @Bean
    public FoodRepository foodRepository() {
        return new JpaFoodRepository(em);
    }

    @Bean
    public TeamService teamService() { return new TeamServiceImpl(teamRepository(), userRepository());}

    @Bean
    public TeamRepository teamRepository() { return new JpaTeamRepository(em);}

    
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.username}")
    private String redisUsername;

    @Value("${spring.redis.password}")
    private String redisPassword;

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
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        configuration.setPassword(redisPassword);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(10))
            .build();

        return new LettuceConnectionFactory(configuration, clientConfig);
    }

}
