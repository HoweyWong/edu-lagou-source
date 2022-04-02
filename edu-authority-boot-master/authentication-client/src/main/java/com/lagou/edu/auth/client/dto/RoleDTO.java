package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色
 *
 * @author : chenrg
 * @create 2020/7/9 11:10
 **/
@ApiModel("角色信息")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends AuthorityBaseDTO {

    @ApiModelProperty("角色编码")
    private String code;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色描述")
    private String description;

}
