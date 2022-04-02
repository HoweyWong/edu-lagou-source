package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 用户权限
 *
 * @author : chenrg
 * @create 2020/7/8 15:32
 **/
@ApiModel("用户菜单、资源权限")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PermissionDTO implements Serializable {

    /**
     * 菜单列表
     */
    @ApiModelProperty("菜单列表")
    private List<MenuNodeDTO> menuList;

    /**
     * 资源（包含页面路由、接口等）
     */
    @ApiModelProperty("资源列表")
    private List<ResourceDTO> resourceList;

}
