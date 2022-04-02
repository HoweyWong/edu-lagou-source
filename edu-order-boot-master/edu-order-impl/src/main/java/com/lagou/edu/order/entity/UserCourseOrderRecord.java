package com.lagou.edu.order.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description:(商品订单日志表)   
 * @author: ma wei long
 * @date:   2020年6月18日 下午5:02:29
*/
@Data
@EqualsAndHashCode(callSuper = false)
public class UserCourseOrderRecord implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -7570167955274135869L;
	
	/**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String orderNo;//订单号 唯一
    private String fromStatus;//原订单状态
    private String toStatus;//新订单状态
    private String remark;//备注
    private Date createTime;//创建时间
    private String createUser;//创建人
    private Date updateTime;//更新时间
    private String updateUser;//更新人
}
