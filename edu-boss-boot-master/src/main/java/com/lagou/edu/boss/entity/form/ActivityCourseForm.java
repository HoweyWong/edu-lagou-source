package com.lagou.edu.boss.entity.form;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年7月7日 下午7:36:43
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("活动课程对象")
public class ActivityCourseForm implements Serializable {
	
    /**
	*/
	private static final long serialVersionUID = 8356180608016701680L;
	
	@ApiModelProperty("主键ID")
    private Integer id;
	@ApiModelProperty("课程ID")
    private Integer courseId;
	@ApiModelProperty("活动开始时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date beginTime;
	@ApiModelProperty("活动结束时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date endTime;
	@ApiModelProperty("活动价格")
	private Double amount;
	@ApiModelProperty("库存值")
	private Integer stock;
	@ApiModelProperty("状态 0未上架 10已上架")
	private Integer status;
	@ApiModelProperty("备注")
	private String remark;
}


