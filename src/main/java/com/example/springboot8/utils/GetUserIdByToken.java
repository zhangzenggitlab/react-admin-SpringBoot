package com.example.springboot8.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class GetUserIdByToken {
    @Autowired
    private JWTUtils jwtUtil;
    public Integer getUserIdByToken() {

        // 获取请求头部的token
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = attributes.getRequest().getHeader("token");

        // 1. redis获取用户信息
        int userId = Integer.parseInt(jwtUtil.getJWT(token));

        return userId;
    }
}
