package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 资源分类
 *
 * @author : chenrg
 * @create 2020/7/14 17:41
 **/
@ApiModel("资源分类信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResourceCategoryDTO extends AuthorityBaseDTO implements Comparable<ResourceCategoryDTO> {

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("排序")
    private int sort;

    @ApiModelProperty("是否被选中")
    private boolean selected;

    @Override
    public int compareTo(ResourceCategoryDTO o) {
        return this.sort - o.sort;
    }
}
