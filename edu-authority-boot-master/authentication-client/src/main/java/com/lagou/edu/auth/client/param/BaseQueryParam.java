package com.lagou.edu.auth.client.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询条件基类
 *
 * @author : chenrg
 * @create 2020/7/13 18:19
 **/
@Data
@ToString
public class BaseQueryParam implements Serializable {

    @ApiModelProperty("主键ID")
    private Integer id;

    /**
     * 创建时间起始
     */
    @ApiModelProperty("按创建时间查询-起始时间")
    private Date startCreateTime;

    /**
     * 创建时间结束
     */
    @ApiModelProperty("按创建时间查询-结束时间")
    private Date endCreateTime;

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private Integer current = 1;

    /**
     * 每页大小
     */
    @ApiModelProperty("每页数据量")
    private Integer size = 10;

}
