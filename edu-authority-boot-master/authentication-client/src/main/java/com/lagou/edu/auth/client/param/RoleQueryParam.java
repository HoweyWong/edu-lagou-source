package com.lagou.edu.auth.client.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author chenrg
 * @date 2020年7月7日18:23:05
 */
@ApiModel("角色查询表单")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleQueryParam extends BaseQueryParam {

    /**
     * 角色编码
     */
    @ApiModelProperty("角色编码")
    private String code;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String name;


}
