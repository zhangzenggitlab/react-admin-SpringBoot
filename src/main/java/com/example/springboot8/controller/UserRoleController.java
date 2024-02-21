package com.example.springboot8.controller;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.constants.HttpCode;
import com.example.springboot8.service.imp.UserRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户绑定角色表
 */

@RestController
@CrossOrigin
@RequestMapping("userRole")
public class UserRoleController {

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @PreAuthorize("hasAnyAuthority('admin','user','role')")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody Map<String, Object> map) {
        List list = new ArrayList<>();

        if (map.get("roleIds") instanceof ArrayList) {
            list = (ArrayList) map.get("roleIds");
        }

        return userRoleService.add((int)map.get("userId"), list);
    }
}
