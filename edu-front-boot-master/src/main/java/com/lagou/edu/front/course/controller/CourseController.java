package com.lagou.edu.front.course.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.result.ResultCode;
import com.lagou.edu.course.api.CoursePlayHistoryRemoteService;
import com.lagou.edu.course.api.CourseRemoteService;
import com.lagou.edu.course.api.dto.CourseDTO;
import com.lagou.edu.course.api.dto.PageResultDTO;
import com.lagou.edu.course.api.param.CourseQueryParam;
import com.lagou.edu.front.common.UserManager;
import com.lagou.edu.front.course.model.response.CoursePurchasedRecordRespVo;
import com.lagou.edu.front.course.model.response.CourseResp;
import com.lagou.edu.front.course.service.CourseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "课程接口", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping("/course/")
public class CourseController {

    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private CoursePlayHistoryRemoteService coursePlayHistoryRemoteService;
    @Autowired
    private CourseService courseService;


    @ApiOperation(value = "获取选课内容")
    @RequestMapping(value = "getAllCourse",method=RequestMethod.GET)
    public ResponseDTO<List<CourseResp>> getAllCourse(){
        try {
            Integer userId = UserManager.getUserId();
            List<CourseDTO> allCourses = this.courseRemoteService.getAllCourses(userId);
            List<CourseResp> courseResps = allCourses.stream()
                    .map(courseDTO -> {
                        CourseResp courseResp = new CourseResp();
                        BeanUtils.copyProperties(courseDTO, courseResp);
                        return courseResp;
                    })
                    .collect(Collectors.toList());
            return ResponseDTO.response(ResultCode.SUCCESS.getState(), null, courseResps);
        } catch (Exception e) {
            log.error("获取选课失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败", null);
        }
    }

    @ApiOperation(value = "获取课程详情")
    @RequestMapping(value = "getCourseById", method = RequestMethod.GET)
    public ResponseDTO getCourseById(@RequestParam("courseId") Integer courseId) {
        try {
            Integer userId = UserManager.getUserId();
            CourseDTO courseDTO = this.courseRemoteService.getCourseById(courseId,userId);
            if (courseDTO == null) {
                return ResponseDTO.response(ResultCode.SUCCESS, null);
            }
            CourseResp courseResp = new CourseResp();
            BeanUtils.copyProperties(courseDTO, courseResp);
            return ResponseDTO.response(ResultCode.SUCCESS, courseResp);
        } catch (Exception e) {
            log.error("获取课程详情失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR);

        }
    }

    @ApiOperation(value = "获取已购课程")
    @RequestMapping(value = "getPurchaseCourse",method=RequestMethod.GET)
    public ResponseDTO getPurchaseCourse(){

        try {
            Integer userId = UserManager.getUserId();
            if(userId == null){
                log.info("[获取已购课程] 用户ID为空，获取数据为空");
                return ResponseDTO.response(ResultCode.SUCCESS);
            }
            log.info("获取已购课程 userId:{}" ,userId);
            List<CoursePurchasedRecordRespVo>  coursePurchasedRecordRespVos= courseService.getAllCoursePurchasedRecord(userId);
            return ResponseDTO.response(ResultCode.SUCCESS.getState(), null, coursePurchasedRecordRespVos);
        } catch (Exception e) {
            log.error("获取已购课程:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败", null);
        }
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("getQueryCourses")
    public ResponseDTO getQueryCourses(@RequestBody CourseQueryParam courseQueryParam) {
        PageResultDTO<CourseDTO> pageResultDTO = this.courseRemoteService.getQueryCourses(courseQueryParam);
        return ResponseDTO.response(1, null, pageResultDTO);
    }

}