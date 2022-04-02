package com.lagou.edu.course.job;

import com.lagou.edu.course.service.ICourseService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 课程自动上架功能
 *
 * @author : chenrg
 * @create 2020/7/9 21:16
 **/
@Component
public class CourseAutoOnlineJob {

    @Autowired
    private ICourseService courseService;

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("courseAutoOnlineJobHandler")
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("Execute course auto online.");
        courseService.courseAutoOnline();
        return ReturnT.SUCCESS;
    }
}
