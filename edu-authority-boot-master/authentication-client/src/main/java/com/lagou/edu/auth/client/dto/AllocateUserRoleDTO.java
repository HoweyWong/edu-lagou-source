package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 给用户分配角色请求DTO
 *
 * @author : chenrg
 * @create 2020/7/13 15:08
 **/
@ApiModel("给用户分配角色请求表单")
@Data
@NoArgsConstructor
@ToString
public class AllocateUserRoleDTO extends AuthorityBaseDTO {

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("分配的角色id列表")
    private List<Integer> roleIdList;

}
