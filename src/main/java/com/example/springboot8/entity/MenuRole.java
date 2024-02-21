package com.example.springboot8.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 菜单绑定角色
 */
@Data
public class MenuRole {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer menuId;

    private Integer roleId;

    private Timestamp createTime;

    private Timestamp updateTime;

    @TableLogic(value = "1", delval = "2")           // 1 未删除 2 删除
    private Integer isDelete;
}
