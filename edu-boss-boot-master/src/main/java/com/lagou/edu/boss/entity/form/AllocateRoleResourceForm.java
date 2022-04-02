package com.lagou.edu.boss.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/13 15:08
 **/
@ApiModel("给角色分配资源")
@Data
@NoArgsConstructor
@ToString
public class AllocateRoleResourceForm implements Serializable {

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("分配的资源ID列表")
    private List<Integer> resourceIdList;

}
