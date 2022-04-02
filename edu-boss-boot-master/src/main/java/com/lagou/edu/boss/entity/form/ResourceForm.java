package com.lagou.edu.boss.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 资源信息
 *
 * @author : chenrg
 * @create 2020/7/10 18:54
 **/
@ApiModel("资源表单信息")
@Data
@NoArgsConstructor
@ToString
public class ResourceForm implements Serializable {

    @ApiModelProperty(value = "资源ID，新建时为空")
    private Integer id;

    @NotBlank(message = "资源名称不能为空")
    @ApiModelProperty(value = "资源名称")
    private String name;

    @NotBlank(message = "资源分类ID不能为空")
    @ApiModelProperty(value = "资源分类ID")
    private Integer categoryId;

    @NotBlank(message = "资源路径不能为空")
    @ApiModelProperty(value = "资源路径")
    private String url;

    @ApiModelProperty(value = "资源描述")
    private String description;
}
