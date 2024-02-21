package com.example.springboot8.controller;

import com.example.springboot8.common.BaseController;
import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.Role;
import com.example.springboot8.service.imp.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleServiceImpl roleService;

    @PreAuthorize("hasAnyAuthority('admin','role/select')")
    @GetMapping("/list")
    public ResponseResult list(@RequestParam Map<String, Object> map) {

        int page = 1;
        int pageSize = 10;

        if (!map.get("pageSize").equals("") && !Objects.isNull(map.get("pageSize"))) {
            pageSize = Integer.parseInt((String) map.get("pageSize"));
        }

        if (!map.get("page").equals("") && !Objects.isNull(map.get("page"))) {
            page = Integer.parseInt((String) map.get("page"));
        }

        return roleService.list(page, pageSize);
    }

    @PreAuthorize("hasAnyAuthority('admin','user','role/select')")
    @GetMapping("/allTree")
    public ResponseResult allTree() {
        return roleService.allTree();
    }
    @PreAuthorize("hasAnyAuthority('admin','role/add')")
    @PostMapping("/add")
    public ResponseResult add(@Validated @RequestBody Role role) {
        return roleService.add(role);
    }

    @PreAuthorize("hasAnyAuthority('admin','role/delete')")
    @DeleteMapping("/delRoleById")
    public ResponseResult delRoleById(@RequestBody Role role) {
        return roleService.delRoleById(role.getId());
    }

    @PreAuthorize("hasAnyAuthority('admin','role/update')")
    @PutMapping("/updateRoleById")
    public ResponseResult updateRoleById(@RequestBody Role role) {
        return roleService.updateRoleById(role);
    }

}
