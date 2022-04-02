package com.lagou.edu.course.controller;


import com.lagou.edu.course.api.dto.SectionDTO;
import com.lagou.edu.course.remote.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leo
 * @since 2020-06-17
 */
@RestController
@RequestMapping("/course/section")
public class SectionController {
    @Autowired
    private SectionService sectionService;
    /**
     * 保存课程
     * @param sectionDTO
     * @return
     */
    @PostMapping(value = "/saveOrUpdateSection")
    boolean saveOrUpdateSection(@RequestBody SectionDTO sectionDTO){
       return sectionService.saveOrUpdateSection(sectionDTO);
    }



    /**
     * 通过课程Id获取章节和课时
     * @param courseId
     * @return
     */
    @GetMapping(value = "/getSectionAndLesson")
    List<SectionDTO> getSectionAndLesson(@RequestParam("courseId") Integer courseId){
        return sectionService.getSectionAndLesson(courseId);
    }


    /**
     * 获取章节信息
     * @param sectionId
     * @return
     */
    @GetMapping(value = "/getBySectionId")
    SectionDTO getBySectionId(@RequestParam("sectionId") Integer sectionId){
        return sectionService.getBySectionId(sectionId);
    }
}
