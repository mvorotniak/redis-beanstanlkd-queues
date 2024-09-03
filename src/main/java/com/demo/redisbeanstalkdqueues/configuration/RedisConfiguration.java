package com.demo.redisbeanstalkdqueues.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisConnectionFactory redisRdbConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public RedisConnectionFactory redisAofConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6380);
    }

    @Bean(name = "redisTemplateRdb")
    @Primary
    public RedisTemplate<String, String> redisTemplateRdb() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisRdbConnectionFactory());
        return redisTemplate;
    }

    @Bean(name = "redisTemplateAof")
    public RedisTemplate<String, String> redisTemplateAof() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisAofConnectionFactory());
        return redisTemplate;
    }
}
