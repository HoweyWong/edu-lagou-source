package com.lagou.edu.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.edu.course.entity.po.Lesson;

import java.util.List;

/**
 * <p>
 * 课程节内容 服务类
 * </p>
 *
 * @author leo
 * @since 2020-06-17
 */
public interface ILessonService extends IService<Lesson> {

    Integer getReleaseCourse(Integer courseId);

    List<Lesson> getBySectionId(Integer sectionId);
}
