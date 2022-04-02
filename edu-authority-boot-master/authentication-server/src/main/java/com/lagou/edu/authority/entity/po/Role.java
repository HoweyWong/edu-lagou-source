package com.lagou.edu.authority.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("roles")
public class Role extends AuthorityBasePO {

    private String code;

    private String name;

    private String description;

    @TableField(exist = false)
    private Set<Integer> resourceIds;
}
