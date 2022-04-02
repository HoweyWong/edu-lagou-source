package com.lagou.edu.boss.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author : chenrg
 * @create 2020/7/15 13:58
 **/
@ApiModel("资源分类表单信息")
@Data
@NoArgsConstructor
@ToString
public class ResourceCategoryForm implements Serializable {

    @ApiModelProperty(value = "资源分类ID，新建时为空")
    private Integer id;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("排序")
    private int sort;
}
