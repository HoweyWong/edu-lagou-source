package com.lagou.edu.course.remote;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.course.api.dto.TeacherDTO;
import com.lagou.edu.course.entity.po.Teacher;
import com.lagou.edu.course.service.ITeacherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:   mkp
 * Date:     2020/7/6 19:13
 * Description:
 */
@Slf4j
@Service
public class TeacherService  {
    @Autowired
    private ITeacherService teacherService;
    public TeacherDTO getByCourseId(Integer courseId) {
        log.info("通过课程ID获取老师信息 courseId:{}",courseId);
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("is_del",Boolean.FALSE);
        List<Teacher> teachers = teacherService.list(queryWrapper);
        if(CollectionUtils.isEmpty(teachers)){
            return null;
        }
        Teacher teacher = teachers.get(0);
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtil.copyProperties(teacherDTO,teacher);
        return teacherDTO;
    }
}
