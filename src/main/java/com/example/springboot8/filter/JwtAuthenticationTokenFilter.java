package com.example.springboot8.filter;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.controller.LoginUser;
import com.example.springboot8.exception.SystemException;
import com.example.springboot8.utils.JWTUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// spring security 过滤器,验证jwt过期处理
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("/user/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取请求头部的token
        String token = request.getHeader("token");

        // 没有token,直接放行
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 有token, 去验证,解析token
        boolean bol = jwtUtil.verify(token);

        // 验证失败
        if (!bol) {
            throw new SystemException(401, "token失效，验证失败!", null);
        }

        // 解析成功,存入securityContextHolder(否则后续验证无法通过)
        // 角色权限
        // 1. redis获取用户信息

        int redisKey = Integer.parseInt(jwtUtil.getJWT(token));

        BoundHashOperations ops = redisTemplate.boundHashOps("token" + redisKey);

        // 查看redis是否有该用户信息(注销等情况就需要重新登录)
        if (ops.get(redisKey) == null) {
            ResponseResult result = new ResponseResult(401, null, "认证失败,请重新登录");

            response.setContentType("application/json");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);        // 设置状态吗
            response.setCharacterEncoding("utf-8");
            // 构建JSON格式的响应
            // 创建ObjectMapper对象
            ObjectMapper mapper = new ObjectMapper();
            // 将Java对象转换为JSON字符串
            String jsonString = mapper.writeValueAsString(result);
            response.getWriter().write(jsonString);

            return;
        }


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);     // 对象转换没有set方法的会报错
        LoginUser loginUser = objectMapper.convertValue(ops.get(redisKey), LoginUser.class);

        UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(token, null, loginUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(token1);
        filterChain.doFilter(request, response);

    }
}
