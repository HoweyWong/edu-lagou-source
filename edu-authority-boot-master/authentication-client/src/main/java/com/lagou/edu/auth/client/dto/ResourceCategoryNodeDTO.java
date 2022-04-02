package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/15 9:51
 **/
@ApiModel("资源分类节点信息，按分类展示")
@Data
@NoArgsConstructor
@ToString
public class ResourceCategoryNodeDTO extends ResourceCategoryDTO {

    @ApiModelProperty("资源列表")
    private List<ResourceDTO> resourceList;

}
