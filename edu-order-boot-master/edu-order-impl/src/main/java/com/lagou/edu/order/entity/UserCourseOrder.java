package com.lagou.edu.order.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description:(商品订单表)   
 * @author: ma wei long
 * @date:   2020年6月18日 下午4:47:50
*/
@Data
@TableName("user_course_order")
@EqualsAndHashCode(callSuper = false)
public class UserCourseOrder implements Serializable {

    /**
	 */
	private static final long serialVersionUID = 2924775151267715094L;

	/**
     * id
     */
//  @TableId(value = "id", type = IdType.AUTO)
	@Id
    private Long id;
    private String orderNo;//订单号 唯一
    private Integer userId;//用户id
    private Integer courseId;//课程id，根据订单中的课程类型来选择
    private Integer sourceType;//订单来源类型: 1 用户下单购买 2 后台添加专栏
    private Integer status;//当前状态: 0已创建 10已支付 20已完成 30已取消 40已过期 
    private Integer isDel;//是否删除
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private Integer activityCourseId;//活动课程id

}
