package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户是否拥有角色信息
 *
 * @author : chenrg
 * @create 2020/7/13 19:57
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("用户-角色信息，列出所有角色及用户是否拥有该角色")
public class UserRoleDTO implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty("角色ID")
    private Integer id;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String name;

    /**
     * 是否有权限
     */
    @ApiModelProperty("是否有权限")
    private boolean hasPermission;
}
