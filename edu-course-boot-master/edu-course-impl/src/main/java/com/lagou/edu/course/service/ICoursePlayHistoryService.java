package com.lagou.edu.course.service;

import com.lagou.edu.course.api.dto.CoursePlayHistoryDTO;
import com.lagou.edu.course.entity.po.CoursePlayHistory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leo
 * @since 2020-06-19
 */
public interface ICoursePlayHistoryService extends IService<CoursePlayHistory> {

    CoursePlayHistory getByUserIdAndCourseId(Integer userId, Integer courseId);

    void saveCourseHistoryNode(CoursePlayHistoryDTO playHistoryDTO);
}
