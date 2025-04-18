// package com.jobconnect.message.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.StringRedisSerializer;

// @Configuration
// public class RedisConfig {

//     @Value("${redis.host}")
//     private String redisHost;

//     @Value("${redis.port}")
//     private int redisPort;

//     @Bean
//     public RedisConnectionFactory redisConnectionFactory() {
//         JedisConnectionFactory factory = new JedisConnectionFactory();
//         factory.setHostName(redisHost); 
//         factory.setPort(redisPort); 
//         return factory;
//     }

//     @Bean
//     public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//         RedisTemplate<String, Object> template = new RedisTemplate<>();
//         template.setConnectionFactory(redisConnectionFactory);
//         template.setKeySerializer(new StringRedisSerializer());
//         template.setValueSerializer(new StringRedisSerializer());
//         return template;
//     }
// }