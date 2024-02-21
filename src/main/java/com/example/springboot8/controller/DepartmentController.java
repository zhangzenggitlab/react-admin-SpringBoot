package com.example.springboot8.controller;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.Department;
import com.example.springboot8.service.imp.DepartmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("department")
public class DepartmentController {
    @Autowired
    private DepartmentServiceImpl departmentService;

    @PreAuthorize("hasAnyAuthority('admin','department/select')")
    @GetMapping("/list")
    public ResponseResult list(@RequestParam Map<String, Object> map) {

        int page = 1;
        int pageSize = 10;
        int status = 0;
        String name = "";

        if (!Objects.isNull(map.get("pageSize")) && !map.get("pageSize").equals("")) {
            pageSize = Integer.parseInt((String) map.get("pageSize"));
        }

        if (!Objects.isNull(map.get("page")) && !map.get("page").equals("")) {
            page = Integer.parseInt((String) map.get("page"));
        }

        if (!Objects.isNull(map.get("name")) && !map.get("name").equals("")) {
            name = (String) map.get("name");
        }

        if (!Objects.isNull(map.get("status"))) {
            status = Integer.parseInt((String) map.get("status"));
        }

        return departmentService.list(page, pageSize, name, status);
    }

    @PreAuthorize("hasAnyAuthority('admin','department/update')")
    @PutMapping("/updateDepartmentById")
    public ResponseResult updateDepartmentById(@RequestBody Department department) {
        return departmentService.updateDepartmentById(department);
    }

    @PreAuthorize("hasAnyAuthority('admin','user','department/select')")
    @GetMapping("/all")
    public ResponseResult all() {
        return departmentService.all();
    }

    @PreAuthorize("hasAnyAuthority('admin','department/add')")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody Department department) {
        return departmentService.add(department);
    }

    @PreAuthorize("hasAnyAuthority('admin','department/delete')")
    @DeleteMapping("/delDepartmentById")
    public ResponseResult delDepartmentById(@RequestBody Department department) {
        return departmentService.delById(department.getId());
    }

}
