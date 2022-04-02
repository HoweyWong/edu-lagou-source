package com.lagou.edu.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.course.api.dto.LessonDTO;
import com.lagou.edu.course.api.dto.MediaDTO;
import com.lagou.edu.course.api.enums.CourseLessonStatus;
import com.lagou.edu.course.entity.po.Lesson;
import com.lagou.edu.course.entity.po.Media;
import com.lagou.edu.course.mapper.LessonMapper;
import com.lagou.edu.course.mapper.MediaMapper;
import com.lagou.edu.course.remote.MediaService;
import com.lagou.edu.course.service.ILessonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.edu.course.service.IMediaService;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程节内容 服务实现类
 * </p>
 *
 * @author leo
 * @since 2020-06-17
 */
@Service
public class LessonServiceImpl extends ServiceImpl<LessonMapper, Lesson> implements ILessonService {
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private IMediaService mediaService;
    @Override
    public Integer getReleaseCourse(Integer courseId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("course_id",courseId);
       
        queryWrapper.eq("status", CourseLessonStatus.RELEASE.getCode());
        queryWrapper.eq("is_del",Boolean.FALSE);
         return lessonMapper.selectCount(queryWrapper);
    }

    @Override
    public List<Lesson> getBySectionId(Integer sectionId) {
        QueryWrapper<Lesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("section_id",sectionId);
        queryWrapper.eq("is_del",Boolean.FALSE);
        queryWrapper.orderByAsc("order_num");
        List<Lesson> lessons = lessonMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(lessons)){
            return Collections.emptyList();
        }
        return lessons;
    }
}
