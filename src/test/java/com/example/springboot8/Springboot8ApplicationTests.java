package com.example.springboot8;

import com.example.springboot8.entity.User;
import com.example.springboot8.mapper.UserMapper;
import com.example.springboot8.utils.EncryptionUtils;
import com.example.springboot8.utils.JWTUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class Springboot8ApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EncryptionUtils encryptionUtils;

    @Test
    void contextLoads() {


    }

    @Test
    public void TestBCryptPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin@123"));
        //  System.out.println(passwordEncoder.matches("123456", "$2a$10$hZpUmOMJWWM7Hi7AhksRU.UoNbCaK9uS25Y8TTohRSUFjXsRyP2P."));
    }

    @Test
    public void TestJWT() {

        // 加密
        String token = jwtUtils.createJWT(1);
        System.out.println("token:" + token);

        // 验证
        System.out.println("验证:" + jwtUtils.verify(token));

        // 解密
        System.out.println("解密:" + jwtUtils.getJWT(token));
    }

    @Test
    public void TestRedis() {
        User user = new User();

//        user.setAge(10);
        try {
            //    redisTemplate.opsForValue().set(12,user, TimeUnit.SECONDS);
            System.out.println(redisTemplate.opsForValue().get(12));
        } catch (Exception e) {
            System.out.println("超时");
            System.out.println(e);
        }

        // redisTemplate.opsForValue().set("redisKey","user");
        //      System.out.println(redisTemplate.opsForValue().get("redisKey"));
    }


    @Test
    public void TestUser() {
        BoundHashOperations ops = redisTemplate.boundHashOps("token" + "1");
        System.out.println(ops.get(1));
    }

    @Test
    public void TestEncryptionUtils() throws Exception {

        String zz = "";
        System.out.println("zz:" + zz.isEmpty());
        System.out.println("zz2:" + (zz != ""));
        String id = encryptionUtils.encrypt("123456");
        System.out.println("加密:" + id);

        System.out.println("解密：" + encryptionUtils.decrypt(id));
    }
}
