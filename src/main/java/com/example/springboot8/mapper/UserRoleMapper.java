package com.example.springboot8.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot8.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper  extends BaseMapper<UserRole> {
    UserRole selectOne(int id);
}
