package com.lagou.edu.authority.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@TableName("resource")
public class Resource extends AuthorityBasePO {
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源分类ID
     */
    private Integer categoryId;
    /**
     * 资源url，用于匹配访问路径。能匹配上表明有权限
     */
    private String url;
    /**
     * 资源描述
     */
    private String description;
}
