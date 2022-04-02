package com.lagou.edu.course.remote;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.course.api.dto.LessonDTO;
import com.lagou.edu.course.entity.po.Lesson;
import com.lagou.edu.course.service.ILessonService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author:   mkp
 * Date:     2020/6/21 11:17
 * Description:
 */
@Service
public class LessonService {
    @Autowired
    private ILessonService lessonService;
    public boolean saveOrUpdate(LessonDTO lessonDTO) {
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(lessonDTO,lesson);
        if(lesson.getId() == null){
            lesson.setCreateTime(LocalDateTime.now());
        }
        lesson.setUpdateTime(LocalDateTime.now());
        return lessonService.saveOrUpdate(lesson);
    }

    public LessonDTO getById(Integer lessonId) {
        Lesson lesson = lessonService.getById(lessonId);
        if(lesson == null){
            return null;
        }
        LessonDTO lessonDTO = new LessonDTO();
        BeanUtils.copyProperties(lesson,lessonDTO);
        return lessonDTO;
    }

    public Map<Integer, String> getByIds(List<Integer> lessonIds) {

        QueryWrapper<Lesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",lessonIds);
        List<Lesson> lessons = lessonService.list(queryWrapper);
        if(CollectionUtils.isEmpty(lessons)){
            return Collections.EMPTY_MAP;
        }
        return lessons.stream().collect(Collectors.toMap(Lesson::getCourseId,Lesson::getTheme));
    }
}
