package com.lagou.edu.boss.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/13 15:01
 **/
@ApiModel("分配用户角色表单")
@Data
@NoArgsConstructor
@ToString
public class AllocateUserRoleForm implements Serializable {

    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private Integer userId;

    @NotEmpty(message = "分配角色ID列表不能为空")
    @ApiModelProperty("角色ID列表")
    private List<Integer> roleIdList;

}
