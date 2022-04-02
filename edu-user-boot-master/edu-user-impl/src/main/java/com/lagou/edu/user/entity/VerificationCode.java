package com.lagou.edu.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author felix
 * @since 2020-06-28
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_phone_verification_code")
public class VerificationCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 验证码是否校验过
     */
    @TableField("isCheck")
    private Boolean isCheck;

    /**
     * 校验次数
     */
    private Integer checkTimes;


}
