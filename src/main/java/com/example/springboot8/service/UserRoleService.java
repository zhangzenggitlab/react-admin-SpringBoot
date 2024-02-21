package com.example.springboot8.service;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.UserRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserRoleService {
    ResponseResult add(int userId, List roleIds);
}
