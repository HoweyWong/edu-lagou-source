package com.lagou.edu.boss.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 角色信息
 *
 * @author : chenrg
 * @create 2020/7/10 17:15
 **/
@ApiModel("角色表单信息")
@Data
@NoArgsConstructor
@ToString
public class RoleForm implements Serializable {

    @ApiModelProperty(value = "角色ID，新建时为空")
    private Integer id;

    @NotBlank(message = "角色编码不能为空")
    @ApiModelProperty(value = "角色编码")
    private String code;

    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;


}
