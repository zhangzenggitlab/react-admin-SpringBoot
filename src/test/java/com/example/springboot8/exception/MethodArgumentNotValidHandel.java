package com.example.springboot8.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Component
public class MethodArgumentNotValidHandel {
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public JSONObject MethodArgumentNotValidHandler(HttpServletRequest request,
                                                    MethodArgumentNotValidException exception) throws Exception
    {
        JSONObject result=new JSONObject();
        result.put("code","fail");
        JSONObject errorMsg = new JSONObject();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errorMsg.put(error.getField(),error.getDefaultMessage());
        }
        result.put("msg",errorMsg);
        return result;
    }
}
