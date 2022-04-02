package com.lagou.edu.authority.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("menu")
public class Menu extends AuthorityBasePO {

    private Integer parentId;
    private String name;
    private String href;
    private String icon;
    private int orderNum;
    private String description;
    /**
     * 是否展示
     */
    private boolean shown;

    /**
     * 菜单层级，方便展示
     */
    private int level;
}
