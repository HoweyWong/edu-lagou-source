package com.lagou.edu.message.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: ma wei long
 * @date:   2020年6月28日 下午1:39:38
*/
@Data
@EqualsAndHashCode(callSuper = false)
public class Message implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -2579402214938306380L;
	/**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;//用户id
    private Integer courseId;//课程id
    private String courseName;//课程名称
    private Integer courseLessonId;//课时id
    private String theme;//课时主题
    private Integer hasRead;//是否读取  0未读 1已读
    private Integer isDel;//是否删除 0未删除 1删除
    private String remark;//备注
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
}
