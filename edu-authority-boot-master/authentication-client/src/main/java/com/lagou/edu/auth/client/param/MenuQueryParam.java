package com.lagou.edu.auth.client.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel("菜单查询表单")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuQueryParam extends BaseQueryParam {

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("是否显示")
    private Boolean shown;

    @ApiModelProperty("是否要求查看下级菜单，在查询下级菜单列表时传true")
    private boolean querySubLevel;
}
