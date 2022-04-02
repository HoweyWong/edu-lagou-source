package com.lagou.edu.front.course.service;

import com.lagou.edu.front.course.model.response.CourseSectionListResult;

public interface SectionService {
    CourseSectionListResult getSectionInfoByCourseId(Integer userId, Integer courseId);
}
