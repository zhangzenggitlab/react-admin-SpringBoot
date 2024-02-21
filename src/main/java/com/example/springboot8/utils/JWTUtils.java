package com.example.springboot8.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springboot8.exception.SystemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;


@Configuration
public class JWTUtils {

    @Value("${jwt.secret}")
    public String secret;

    // 验证
    public boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            return true;
        } catch (Exception exception) {
            System.out.println("重新登陆" + exception);
            throw new SystemException(401, "", null);
            //    return false;
        }
    }


    public String getJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256(this.secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("data").asString();
    }


    // 生成jwt
    public String createJWT(Object o) {

        int expire = 1000 * 60 * 60 * 24;
        String secret = this.secret;

        Date date = new Date(System.currentTimeMillis() + expire);
        Algorithm algorithm = Algorithm.HMAC256(secret);

        // 附带username信息
        return JWT.create()
                .withClaim("data", String.valueOf(o))
                .withExpiresAt(date)
                .sign(algorithm);

    }
}
