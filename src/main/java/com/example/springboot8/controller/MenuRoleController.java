package com.example.springboot8.controller;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.service.imp.MenuRoleServiceImpl;
import com.example.springboot8.service.imp.UserRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/menuRole")
public class MenuRoleController {

    @Autowired
    private MenuRoleServiceImpl menuRoleService;

    @PreAuthorize("hasAnyAuthority('admin','role/add')")
    @RequestMapping("/add")
    public ResponseResult add(@RequestBody Map<String, Object> map){
        List list = new ArrayList<>();

        if (map.get("menuIds") instanceof ArrayList) {
            list = (ArrayList) map.get("menuIds");
        }

        return menuRoleService.add((int)map.get("roleId"), list);
    }

    @GetMapping("/menuRoleByRoleId")
    public ResponseResult  menuRoleByRoleId(int roleId){
        return menuRoleService.menuRoleByRoleId(roleId);
    }
}
