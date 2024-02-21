package com.example.springboot8.controller;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.constants.HttpCode;
import com.example.springboot8.service.imp.TxCosServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/txCos")
public class TxCosController {
    @Autowired
    private TxCosServiceImpl txCosService;

    @PostMapping("/upload")
    public ResponseResult upload(@RequestBody MultipartFile file) {
        if(Objects.isNull(file)){
            return  new ResponseResult(HttpCode.CODE_FAIL,HttpCode.constants.get(HttpCode.CODE_FAIL),null);
        }
        return txCosService.upload(file);
    }

    @PostMapping("/uploadModelZip")
    public ResponseResult uploadModelZip(@RequestBody MultipartFile file) throws IOException {
        return txCosService.uploadModelZip(file);
    }



}
