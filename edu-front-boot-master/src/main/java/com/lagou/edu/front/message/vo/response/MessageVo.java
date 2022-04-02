package com.lagou.edu.front.message.vo.response;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年6月28日 下午2:08:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "消息通知对象")
public class MessageVo implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -8778057100628664495L;
	@ApiModelProperty(value = "id")
	private Integer id;
	@ApiModelProperty(value = "用户id")
    private Integer userId;
	@ApiModelProperty(value = "课程id")
    private Integer courseId;
	@ApiModelProperty(value = "课程名称")
    private Integer courseName;
	@ApiModelProperty(value = "课时id")
    private Integer courseLessonId;
	@ApiModelProperty(value = "课时主题")
    private String theme;
	@ApiModelProperty(value = "是否读取  0未读 1已读")
    private Integer hasRead;
	@ApiModelProperty(value = "备注")
    private String remark;
	@ApiModelProperty(value = "创建时间")
    private Date createTime;
	@ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
