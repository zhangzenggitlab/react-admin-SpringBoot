package com.example.springboot8.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.sql.Timestamp;

// 用户绑定角色表
@Data
public class UserRole {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer roleId;

    private Timestamp createTime;

    private Timestamp updateTime;

    @TableLogic(value = "1", delval = "2")           // 1 未删除 2 删除
    private Integer isDelete;

}
