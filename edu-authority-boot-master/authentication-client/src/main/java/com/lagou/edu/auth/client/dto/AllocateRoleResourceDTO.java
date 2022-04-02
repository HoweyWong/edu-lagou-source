package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/13 15:08
 **/
@ApiModel("给角色分配菜单")
@Data
@NoArgsConstructor
@ToString
public class AllocateRoleResourceDTO extends AuthorityBaseDTO {

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("分配的资源ID列表")
    private List<Integer> resourceIdList;

}
