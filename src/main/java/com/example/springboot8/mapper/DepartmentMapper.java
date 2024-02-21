package com.example.springboot8.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot8.entity.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
