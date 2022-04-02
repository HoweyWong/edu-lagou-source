package com.lagou.edu.course.service;

import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.course.api.dto.ActivityCourseDTO;
import com.lagou.edu.course.entity.po.ActivityCourse;

/**
 * @author: ma wei long
 * @date:   2020年7月6日 下午9:31:23
 */
public interface IActivityCourseService extends IService<ActivityCourse> {
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午7:57:29   
	*/
	void saveActivityCourse(ActivityCourseDTO reqDTO);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月9日 下午6:56:25   
	*/
	void saveOrUpdateActivityCourse(ActivityCourseDTO reqDTO);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午7:57:29   
	*/
	boolean updateActivityCourseStatus(ActivityCourseDTO reqDTO);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月8日 上午10:31:41   
	*/
	ActivityCourseDTO getByCourseId(Integer courseId);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月8日 上午11:37:32   
	*/
    void updateActivityCourseStock(Integer courseId,String orderNo);
}
