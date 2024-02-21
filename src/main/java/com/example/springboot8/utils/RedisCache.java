package com.example.springboot8.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RedisCache {

    @Autowired
    private RedisTemplate redisTemplate;

    public void set(Object key, Object value, TimeUnit timeUnit) {
   //  BoundGeoOperations old =  redisTemplate.boundValueOps(key);

    }

    public Object get(Object key) {
        return redisTemplate.opsForValue().get(key);
    }
}
