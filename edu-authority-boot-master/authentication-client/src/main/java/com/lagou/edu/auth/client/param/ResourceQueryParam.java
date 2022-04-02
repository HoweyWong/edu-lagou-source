package com.lagou.edu.auth.client.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author : chenrg
 * @create 2020/7/14 18:50
 **/
@ApiModel("资源查询表单")
@Data
@NoArgsConstructor
@ToString
public class ResourceQueryParam extends BaseQueryParam {

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("资源路径")
    private String url;

    @ApiModelProperty("资源分类ID")
    private Integer categoryId;

}
