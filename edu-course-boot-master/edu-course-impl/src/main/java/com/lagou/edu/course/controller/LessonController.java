package com.lagou.edu.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.course.api.dto.LessonDTO;
import com.lagou.edu.course.api.dto.MediaDTO;
import com.lagou.edu.course.entity.po.Lesson;
import com.lagou.edu.course.entity.po.Media;
import com.lagou.edu.course.remote.LessonService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程节内容 前端控制器
 * </p>
 *
 * @author leo
 * @since 2020-06-17
 */
@RestController
@RequestMapping("/course/lesson")
public class LessonController {
    @Autowired
    private LessonService lessonService;


    @GetMapping(value = "/getByIds")
    Map<Integer,String> getByIds(@RequestParam("lessonIds") List<Integer> lessonIds){

       return lessonService.getByIds(lessonIds);
    }
    /**
     * 保存或者更新课程
     * @param lessonDTO
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    boolean saveOrUpdate(@RequestBody LessonDTO lessonDTO){
        return lessonService.saveOrUpdate(lessonDTO);
    }
    /**
     * 通过lessonId获取课时
     * @param lessonId
     * @return
     */
    @GetMapping(value = "/getById")
    LessonDTO getById(@RequestParam("lessonId") Integer lessonId){
        return lessonService.getById(lessonId);
    }

}
