// package com.ampersandor.cotopia.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
// import org.springframework.data.redis.serializer.StringRedisSerializer;

// @Configuration
// public class RedisConfig {
    
//     @Value("${spring.redis.host}")
//     private String redisHost;
    
//     @Value("${spring.redis.port}")
//     private int redisPort;
    
//     @Value("${spring.redis.password}")
//     private String redisPassword;

//     @Bean
//     public RedisConnectionFactory redisConnectionFactory() {
//         LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
//         lettuceConnectionFactory.setHostName(redisHost);
//         lettuceConnectionFactory.setPort(redisPort);
//         lettuceConnectionFactory.setPassword(redisPassword);
//         lettuceConnectionFactory.setUseSsl(true);
//         return lettuceConnectionFactory;
//     }

//     @Bean
//     public RedisTemplate<String, Object> redisTemplate() {
//         RedisTemplate<String, Object> template = new RedisTemplate<>();
//         template.setConnectionFactory(redisConnectionFactory());
//         template.setKeySerializer(new StringRedisSerializer());
//         template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//         template.setHashKeySerializer(new StringRedisSerializer());
//         template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//         return template;
//     }
// }
