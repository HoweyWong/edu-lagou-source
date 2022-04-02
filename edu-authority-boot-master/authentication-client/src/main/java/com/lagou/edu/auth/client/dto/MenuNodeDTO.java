package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/14 15:46
 **/
@ApiModel("菜单节点，包括子菜单列表")
@Data
@NoArgsConstructor
@ToString
public class MenuNodeDTO extends MenuDTO {

    @ApiModelProperty("是否被选中，用于编辑菜单时标记所选中上级菜单")
    private boolean selected;

    private List<MenuNodeDTO> subMenuList;
}
