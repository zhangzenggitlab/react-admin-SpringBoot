package com.example.springboot8.exception;

import com.example.springboot8.common.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 处理异常
@ControllerAdvice
public class GlobalException {

    // 验证参数必填异常，如 NotBlack
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public void handleHttpException(HttpServletRequest req, MethodArgumentNotValidException exception, HttpServletResponse response) throws IOException {

        BindingResult bindingResult = exception.getBindingResult();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");

        if (bindingResult.hasErrors()) {
            ResponseResult result = new ResponseResult(400, null, exception.getBindingResult().getFieldError().getDefaultMessage());
            ObjectMapper mapper = new ObjectMapper();
            // 将Java对象转换为JSON字符串
            String jsonString = mapper.writeValueAsString(result);
            response.getWriter().write(jsonString);
        }


    }

}
