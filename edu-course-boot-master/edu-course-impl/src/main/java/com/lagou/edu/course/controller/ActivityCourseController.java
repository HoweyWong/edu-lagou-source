package com.lagou.edu.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.util.ConvertUtils;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.course.api.dto.ActivityCourseDTO;
import com.lagou.edu.course.service.IActivityCourseService;

import lombok.extern.slf4j.Slf4j;


/**
 * @author: ma wei long
 * @date:   2020年7月6日 下午9:33:25
*/
@Slf4j
@RestController
@RequestMapping("/activityCourse")
public class ActivityCourseController {

    @Autowired
    private IActivityCourseService activityCourseService;
    
    /**
     * @author: ma wei long
     * @date:   2020年7月7日 下午8:05:21   
    */
    @PostMapping("/saveActivityCourse")
    public ResponseDTO<?> saveActivityCourse(@RequestBody ActivityCourseDTO reqDTO) {
    	log.info("saveActivityCourse - reqDTO:{}",JSON.toJSONString(reqDTO));
    	activityCourseService.saveActivityCourse(reqDTO);
        return ResponseDTO.success();
    }
    
    /**
     * @author: ma wei long
     * @date:   2020年7月7日 下午8:59:06   
    */
    @PostMapping("/updateActivityCourseStatus")
    public ResponseDTO<?> updateActivityCourseStatus(@RequestBody ActivityCourseDTO reqDTO) {
    	log.info("updateActivityCourseStatus - reqVo:{}",JSON.toJSONString(reqDTO));
    	ValidateUtils.isTrue(activityCourseService.updateActivityCourseStatus(reqDTO), "更新状态失败");
        return ResponseDTO.success();
    }
    
    
    /**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午9:30:01   
	*/
	@GetMapping("/getById")
    ResponseDTO<ActivityCourseDTO> getById(@RequestParam("id") Integer id){
    	log.info("getById - id:{}",id);
    	ValidateUtils.notNullParam(id);
		return ResponseDTO.success(ConvertUtils.convert(activityCourseService.getById(id), ActivityCourseDTO.class));
	}
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午9:30:01   
	*/
	@GetMapping("/getByCourseId")
    ResponseDTO<ActivityCourseDTO> getByCourseId(@RequestParam("courseId") Integer courseId){
		log.info("getByCourseId - courseId:{}",courseId);
    	ValidateUtils.notNullParam(courseId);
		return ResponseDTO.success(activityCourseService.getByCourseId(courseId));
	}
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月8日 上午11:33:07   
	*/
	@PostMapping("/updateActivityCourseStock")
	ResponseDTO<?> updateActivityCourseStock(@RequestParam("courseId")Integer courseId,@RequestParam("orderNo")String orderNo){
		activityCourseService.updateActivityCourseStock(courseId,orderNo);
		return ResponseDTO.success();
	}
}
