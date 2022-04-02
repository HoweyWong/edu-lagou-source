package com.lagou.edu.course.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.course.api.dto.ActivityCourseDTO;

/**
 * @author: ma wei long
 * @date:   2020年7月6日 下午9:34:56
*/
@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/activityCourse")
public interface ActivityCourseRemoteService {
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午7:52:24   
	*/
	@PostMapping("/saveActivityCourse")
	ResponseDTO<?> saveActivityCourse(@RequestBody ActivityCourseDTO reqDTO);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午9:01:41   
	*/
	@PostMapping("/updateActivityCourseStatus")
    ResponseDTO<?> updateActivityCourseStatus(@RequestBody ActivityCourseDTO reqDTO);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午9:30:01   
	*/
	@GetMapping("/getById")
    ResponseDTO<ActivityCourseDTO> getById(@RequestParam("id") Integer id);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午9:30:01   
	*/
	@GetMapping("/getByCourseId")
    ResponseDTO<ActivityCourseDTO> getByCourseId(@RequestParam("courseId") Integer courseId);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月8日 上午11:33:07   
	*/
	@PostMapping("/updateActivityCourseStock")
	ResponseDTO<?> updateActivityCourseStock(@RequestParam("courseId")Integer courseId,@RequestParam("orderNo")String orderNo);
}
