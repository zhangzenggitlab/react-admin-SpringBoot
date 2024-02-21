package com.example.springboot8.controller;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.User;
import com.example.springboot8.service.imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserServiceImp userServiceImp;

    @PreAuthorize("hasAnyAuthority('admin','user','user/select')")
    @PostMapping("/userList")
    public ResponseResult userList(@RequestBody Map<String,Object> params){
        return userServiceImp.userList(params);
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return userServiceImp.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
       return userServiceImp.logout();
    }

    @PreAuthorize("hasAnyAuthority('admin','user/select')")
    @GetMapping("/getUserInfo")
    public ResponseResult getUserInfo(){
        return userServiceImp.getUserInfo();
    }

    @PreAuthorize("hasAnyAuthority('admin','user/edit')")
    @PostMapping("/editUser")
    public ResponseResult editUser(@RequestBody User user){
        return userServiceImp.editUser(user);
    }

    @PreAuthorize("hasAnyAuthority('admin','user/add')")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody User user){
        return userServiceImp.add(user);
    }

    @PreAuthorize("hasAnyAuthority('admin','user/update')")
    @PutMapping("/updatePassword")
    public ResponseResult updatePassword(@RequestBody Map<String,String> map){
        return userServiceImp.updatePassword(map.get("oldPassword"),map.get("newPassword"));
    }

    @PreAuthorize("hasAnyAuthority('admin','user/delete')")
    @PostMapping("/deleteById")
    public ResponseResult deleteById(@RequestBody User user){
        return userServiceImp.deleteById(user.getId());
    }

    @PreAuthorize("hasAnyAuthority('admin','user/edit')")
    @PostMapping("/editUserById")
    public ResponseResult editUserById(@RequestBody User user){
        return userServiceImp.editUserById(user);
    }

}
