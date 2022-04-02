package com.lagou.edu.authority.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author : chenrg
 * @create 2020/7/14 19:39
 **/
@Data
@NoArgsConstructor
@ToString
@TableName("resource_category")
public class ResourceCategory extends AuthorityBasePO {

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private int sort;
}
