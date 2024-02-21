package com.example.springboot8.service;

import com.example.springboot8.common.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface TxCosService {
     ResponseResult upload(MultipartFile file);
}
