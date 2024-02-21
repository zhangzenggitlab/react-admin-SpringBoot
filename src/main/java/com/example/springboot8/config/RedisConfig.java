package com.example.springboot8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    // 连接redis方式
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {

        // 单机远程redis服务器配置
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(5000);


        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
               .usePooling()
               .poolConfig(jedisPoolConfig)
               .build();

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,jedisClientConfiguration);
        return jedisConnectionFactory;
    }

    // 配置redis模板
    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer(Object.class));
        return redisTemplate;
    }


}
