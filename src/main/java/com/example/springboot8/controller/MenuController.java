package com.example.springboot8.controller;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.Menu;
import com.example.springboot8.service.imp.MenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private MenuServiceImpl menuService;

    @PreAuthorize("hasAnyAuthority('admin','menu/select')")
    @GetMapping("/list")
    public ResponseResult list(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "pageSize", required = false, defaultValue = "1") int pageSize, @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "status", required = false, defaultValue = "") String status) {
        return menuService.list(page, pageSize, name,status);
    }

    @PreAuthorize("hasAnyAuthority('admin','menu/select')")
    @PutMapping(value = "/menuById")
    public ResponseResult menuById(@RequestBody Menu menu) {
        return menuService.menuById(menu);
    }

    @PreAuthorize("hasAnyAuthority('admin','menu/delete')")
    @PostMapping(value = "/menuById")
    public ResponseResult delMenuById(@RequestBody Menu menu) {
        return menuService.delMenuById(menu.getId());
    }

    @PreAuthorize("hasAnyAuthority('admin','menu/add')")
    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody Menu menu) {
        return menuService.add(menu);
    }

    @PreAuthorize("hasAnyAuthority('admin','menu/update')")
    @PutMapping(value = "/updateMenuById")
    public ResponseResult updateMenuById(@RequestBody Menu menu) {
        return menuService.updateMenuById(menu);
    }

    @GetMapping("/all")
    public ResponseResult all() {
        return menuService.all();
    }

    @GetMapping("/menuRouter")
    public ResponseResult menuRouter() {
        return menuService.menuRouter();
    }
}
