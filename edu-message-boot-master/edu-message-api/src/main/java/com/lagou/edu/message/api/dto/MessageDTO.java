package com.lagou.edu.message.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: ma wei long
 * @date:   2020年6月28日 下午1:48:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageDTO implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -4762698220574370693L;
	
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
