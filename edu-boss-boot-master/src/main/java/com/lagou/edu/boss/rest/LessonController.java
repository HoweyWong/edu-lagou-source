package com.lagou.edu.boss.rest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lagou.edu.boss.entity.form.LessonForm;
import com.lagou.edu.boss.entity.vo.LessonVo;
import com.lagou.edu.boss.service.ILessonService;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.course.api.LessonRemoteService;
import com.lagou.edu.course.api.dto.LessonDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Author:   mkp
 * Date:     2020/6/21 17:09
 * Description: 获取课时信息
 */
@Api(tags = "课时内容", produces = "application/json")
@Slf4j
@RestController
@RequestMapping("/course/lesson")
public class LessonController {
	
    @Autowired
    private LessonRemoteService lessonRemoteService;
    @Autowired
    private ILessonService lessionService;
    
    /**
     * 保存或者更新课程
     * @param lessonForm
     * @return
     */
    @ApiOperation(value = "保存或更新课时")
    @PostMapping(value = "/saveOrUpdate")
    Result saveOrUpdate(@RequestBody LessonForm lessonForm){
        LessonDTO lessonDTO = new LessonDTO();
        BeanUtils.copyProperties(lessonForm,lessonDTO);
        return Result.success(lessionService.saveOrUpdate(lessonDTO));
    }
    /**
     * 通过lessonId获取课时
     * @param lessonId
     * @return
     */
    @ApiOperation(value = "通过Id获取课时")
    @ResponseBody
    @GetMapping(value = "/getById")
    Result getById(@RequestParam("lessonId") Integer lessonId){
        LessonDTO lessonDTO = lessonRemoteService.getById(lessonId);
        LessonVo lessonVo = new LessonVo();
        BeanUtils.copyProperties(lessonDTO,lessonVo);
        return Result.success(lessonVo);
    }
}
