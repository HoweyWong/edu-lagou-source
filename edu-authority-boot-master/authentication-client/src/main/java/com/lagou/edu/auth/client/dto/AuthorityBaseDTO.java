package com.lagou.edu.auth.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限模块DTO基类，共有属性
 *
 * @author : chenrg
 * @create 2020/7/10 17:54
 **/
@Data
@EqualsAndHashCode(of = "id")
public class AuthorityBaseDTO implements Serializable {

    private static final String DEFAULT_USER_NAME = "system";

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Integer id;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createdBy;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updatedBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createdTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updatedTime;

}
