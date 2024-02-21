package com.example.springboot8.exception;

import com.example.springboot8.common.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 重写spring security 认证异常(没有登录等情况)
@Component
public class SpringSecurityAuthenticationException implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException {
        ResponseResult result = new ResponseResult(401, null, "认证失败,请重新登录");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");

        ObjectMapper mapper = new ObjectMapper();
        // 将Java对象转换为JSON字符串
        String jsonString = mapper.writeValueAsString(result);
        response.getWriter().write(jsonString);

    }

}
