package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 页面路由、接口资源
 *
 * @author : chenrg
 * @create 2020/7/8 17:10
 **/

@ApiModel("资源信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResourceDTO extends AuthorityBaseDTO {


    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("资源分类ID")
    private Integer categoryId;

    @ApiModelProperty("资源URL")
    private String url;

    @ApiModelProperty("资源描述")
    private String description;

    @ApiModelProperty("是否选中")
    private boolean selected;

}
