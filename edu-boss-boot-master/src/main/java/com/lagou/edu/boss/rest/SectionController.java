package com.lagou.edu.boss.rest;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.boss.entity.form.SectionForm;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.course.api.SectionRemoteService;
import com.lagou.edu.course.api.dto.SectionDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author:   mkp
 * Date:     2020/6/21 17:24
 * Description: 章节内容
 */
@Api(tags = "章节内容", produces = "application/json")
@Slf4j
@RestController
@RequestMapping("/course/section")
public class SectionController {
    @Autowired
    private SectionRemoteService sectionRemoteService;

    /**
     * 更新保存章节课程
     * @param sectionForm
     * @return
     */
    @PostMapping(value = "/saveOrUpdateSection",consumes = "application/json")
    Result saveOrUpdateSection(@RequestBody SectionForm sectionForm){
        log.info("保存章节信息 sectionForm:{}", JSON.toJSONString(sectionForm));
        try {
            SectionDTO sectionDTO = new SectionDTO();
            BeanUtils.copyProperties(sectionForm,sectionDTO);
            this.sectionRemoteService.saveOrUpdateSection(sectionDTO);
            return Result.success();
        }catch (Exception e){
            log.error("保存课程信息失败:",e);
            return Result.fail(e.getMessage());
        }
    }



    /**
     * 通过课程Id获取章节和课时
     * @param courseId
     * @return
     */
    @GetMapping(value = "/getSectionAndLesson")
    Result  getSectionAndLesson(@RequestParam("courseId") Integer courseId){
        log.info("通过课程Id获取章节和课时 courseId:{}",courseId);
        try {
            List<SectionDTO> sectionAndLesson = this.sectionRemoteService.getSectionAndLesson(courseId);
            return Result.success(sectionAndLesson);
        }catch (Exception e){
            log.error("通过课程Id获取章节和课时:",e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取章节信息
     * @param sectionId
     * @return
     */
    @GetMapping(value = "/getBySectionId")
    Result getBySectionId(@RequestParam("sectionId") Integer sectionId){
        log.info("获取章节信息 sectionId:{}",sectionId);
        try {
            SectionDTO sectionDTO = this.sectionRemoteService.getBySectionId(sectionId);
            return Result.success(sectionDTO);
        }catch (Exception e){
            log.error("通过课程Id获取章节和课时:",e);
            return Result.fail(e.getMessage());
        }
    }
}
