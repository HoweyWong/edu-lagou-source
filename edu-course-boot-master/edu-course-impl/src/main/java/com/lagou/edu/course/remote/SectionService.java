package com.lagou.edu.course.remote;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.course.api.SectionRemoteService;
import com.lagou.edu.course.api.dto.LessonDTO;
import com.lagou.edu.course.api.dto.MediaDTO;
import com.lagou.edu.course.api.dto.SectionDTO;
import com.lagou.edu.course.api.enums.CourseLessonStatus;
import com.lagou.edu.course.api.enums.SectionStatus;
import com.lagou.edu.course.entity.po.Lesson;
import com.lagou.edu.course.entity.po.Media;
import com.lagou.edu.course.entity.po.Section;
import com.lagou.edu.course.service.ILessonService;
import com.lagou.edu.course.service.IMediaService;
import com.lagou.edu.course.service.ISectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Author:   mkp
 * Date:     2020/6/21 10:47
 * Description:课程章节操作
 */
@Slf4j
@Service
public class SectionService {
    @Autowired
    private ISectionService sectionService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private MediaService mediaService;

    public boolean saveOrUpdateSection(SectionDTO sectionDTO) {
        Section section = new Section();
        BeanUtils.copyProperties(sectionDTO,section);
        if(sectionDTO.getId() == null){
            section.setCreateTime(LocalDateTime.now());
        }
        section.setUpdateTime(LocalDateTime.now());
        log.info("保存更新章节信息：{}", JSON.toJSONString(section));
        return  sectionService.saveOrUpdate(section);
    }

   
    public List<SectionDTO> getSectionAndLesson(Integer courseId) {
        QueryWrapper<Section> query = new QueryWrapper<>();
        query.eq("course_id", courseId);
        query.orderByAsc("order_num");
        List<Section> sections = sectionService.list(query);
        if(CollectionUtils.isEmpty(sections)){
            log.info("获取章节信息为空直接返回");
            return Collections.emptyList();
        }
        List<SectionDTO> sectionDTOS = sections.stream().map(section -> {
            SectionDTO sectionDTO = new SectionDTO();
            BeanUtils.copyProperties(section, sectionDTO);
            Integer sectionId = section.getId();
            List<Lesson> lessons = lessonService.getBySectionId(sectionId);
            if (CollectionUtils.isEmpty(lessons)) {
                return sectionDTO;
            }
            List<LessonDTO> lessonDTOs = lessons.stream().map(lesson -> {
                LessonDTO lessonDTO = new LessonDTO();
                BeanUtil.copyProperties(lesson, lessonDTO);
                Integer lessonId = lesson.getId();
                MediaDTO mediaDTO = mediaService.getByLessonId(lessonId);
                if(mediaDTO != null){
                    lessonDTO.setDurationNum(mediaDTO.getDurationNum());
                    lessonDTO.setDuration(mediaDTO.getDuration());
                }

                return lessonDTO;
            }).collect(Collectors.toList());
            sectionDTO.setLessonDTOS(lessonDTOs);
            return sectionDTO;
        }).collect(Collectors.toList());


        return sectionDTOS;
    }

   
    public SectionDTO getBySectionId(Integer sectionId) {
        Section section = sectionService.getById(sectionId);
        if(section == null){
            return null;
        }
        SectionDTO sectionDTO = new SectionDTO();
        BeanUtils.copyProperties(section,sectionDTO);
        return sectionDTO;
    }
}
