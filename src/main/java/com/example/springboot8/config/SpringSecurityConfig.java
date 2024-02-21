package com.example.springboot8.config;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.constants.HttpCode;
import com.example.springboot8.exception.SpringSecurityAuthenticationException;
import com.example.springboot8.filter.JwtAuthenticationTokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

// spring security 配置
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)                        // 开启方法级别的权限控制
//@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    // 重写密码验证方式（默认数据库明文，重写为加密验证）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 重写登录验证
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 拦截规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().sessionManagement()
                .disable().authorizeRequests()
                .antMatchers("/user/login").permitAll()                                  //设置哪些路径可以直接访问，不需要认证
                .anyRequest().authenticated()                                                       // 除上以外的所有请求都需要认证
                //开启异常处理
                .and().exceptionHandling()
                //处理认证异常
                .authenticationEntryPoint(new SpringSecurityAuthenticationException())
                //处理授权异常
                .accessDeniedHandler((request, response, e) -> {
                    ResponseResult result = new ResponseResult(HttpCode.CODE_FORBIDDEN, HttpCode.constants.get(HttpCode.CODE_FORBIDDEN),null);

                    response.setContentType("application/json");
//                  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setCharacterEncoding("utf-8");

                    ObjectMapper mapper = new ObjectMapper();
                    // 将Java对象转换为JSON字符串
                    String jsonString = mapper.writeValueAsString(result);
                    response.getWriter().write(jsonString);

                });

        // 添加过滤器(某些不需要拦截的接口需要在过滤器里面额外放行)
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 允许跨域
        http.cors();
    }

}
