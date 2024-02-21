package com.example.springboot8.service;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.User;

import java.util.Map;

public interface UserService {

    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult userList(Map<String, Object> params);

    ResponseResult getUserInfo();

    ResponseResult editUser(User user);

    ResponseResult add(User user);

    ResponseResult updatePassword(String oldPassword, String newPassword);

    ResponseResult deleteById(int id);

    ResponseResult editUserById(User user);
}
