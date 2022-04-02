package com.lagou.edu.boss.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 菜单信息
 *
 * @author : chenrg
 * @create 2020/7/13 13:35
 **/
@ApiModel("菜单表单信息")
@Data
@NoArgsConstructor
@ToString
public class MenuForm implements Serializable {

    @ApiModelProperty(value = "菜单ID，更新时须带上传给后台")
    private Integer id;

    @NotBlank(message = "菜单父id不能为空(上级菜单ID,无上级菜单赋值为-1)")
    @ApiModelProperty(value = "菜单父id")
    private Integer parentId;

    @NotBlank(message = "菜单名称不能为空")
    @ApiModelProperty(value = "菜单名称")
    private String name;

    @NotBlank(message = "菜单路径不能为空")
    @ApiModelProperty(value = "菜单路径")
    private String href;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "菜单序号")
    private int orderNum;

    @ApiModelProperty(value = "菜单描述")
    private String description;

    @ApiModelProperty(value = "是否显示")
    private boolean shown;
}
