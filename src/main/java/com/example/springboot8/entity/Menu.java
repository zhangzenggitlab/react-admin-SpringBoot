package com.example.springboot8.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
public class Menu {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String path;

    @NotBlank(message = "请填写菜单名称")
    private String name;

    private String permission;

    private String icon;

    private Integer hideMenu;

    @NotBlank(message = "请填写菜单类型")
    private Integer type;

    private Timestamp createTime;                             // 创建时间

    private Timestamp updateTime;                             // 更新时间

    private Integer parentId;

    @NotBlank(message = "请填写是否启用")
    private Integer status;

    @TableLogic(value = "1",delval = "2")                     // 1 未删除 2 删除
    @TableField(select = false)
    private Integer isDelete;

    private Integer sort;
}
