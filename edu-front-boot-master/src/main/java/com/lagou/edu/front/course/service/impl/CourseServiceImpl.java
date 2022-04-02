package com.lagou.edu.front.course.service.impl;

import com.lagou.edu.course.api.CourseRemoteService;
import com.lagou.edu.course.api.dto.CourseDTO;
import com.lagou.edu.front.course.model.response.AllCoursePurchasedRecordRespVo;
import com.lagou.edu.front.course.model.response.CoursePurchasedRecordRespVo;
import com.lagou.edu.front.course.model.response.PurchasedRecordForAppResult;
import com.lagou.edu.front.course.service.CourseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Author:   mkp
 * Date:     2020/6/28 16:29
 * Description:
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRemoteService courseRemoteService;
    @Override
    public  List<CoursePurchasedRecordRespVo> getAllCoursePurchasedRecord(Integer userId) {
        PurchasedRecordForAppResult record = new PurchasedRecordForAppResult();
        if (userId == null) {
            return Collections.emptyList();
        }
        List<AllCoursePurchasedRecordRespVo> allCoursePurchasedRecord = new LinkedList<>();
        List<CourseDTO> courseDtoList = this.courseRemoteService.getPurchasedCourse(userId);
        if(CollectionUtils.isEmpty(courseDtoList)){
            record.setAllCoursePurchasedRecord(allCoursePurchasedRecord);
            return Collections.emptyList();
        }
        List<CoursePurchasedRecordRespVo> courseList = new LinkedList<>();
        setCoursePurchasedRecord(courseDtoList,courseList);

        return courseList;
    }

    /**
     * 根据不同的分栏类型组长信息
     * @param courseList
     * @param allCoursePurchasedRecord
     */
    private void combinePurchasedCommonCourse(
                                              List<CoursePurchasedRecordRespVo> courseList,
                                              List<AllCoursePurchasedRecordRespVo> allCoursePurchasedRecord) {

        if (CollectionUtils.isEmpty(courseList)) {
            return;
        }

        AllCoursePurchasedRecordRespVo purchasedRecord = new AllCoursePurchasedRecordRespVo();
        purchasedRecord.setCourseRecordList(courseList);
        allCoursePurchasedRecord.add(purchasedRecord);
    }
    /**
     * 获取购买的专栏课程信息列表
     * @param courseDtoList
     * @param courseList 专栏课列表
     */
    private void setCoursePurchasedRecord(List<CourseDTO> courseDtoList,
                                          List<CoursePurchasedRecordRespVo> courseList) {

        for (CourseDTO course : courseDtoList) {

            CoursePurchasedRecordRespVo purchasedRecord = new CoursePurchasedRecordRespVo();
            purchasedRecord.setId(course.getId());
            purchasedRecord.setName(course.getCourseName());
            purchasedRecord.setImage(course.getCourseListImg());
            Integer lessonUpdateCount = (course.getLessonUpdateCount() == null || course.getLessonUpdateCount() < 0) ? 0 : course.getLessonUpdateCount();
            purchasedRecord.setLessonUpdateNum(lessonUpdateCount);
            if (lessonUpdateCount > 0 && lessonUpdateCount <= 99) {
                purchasedRecord.setUpdateTips(lessonUpdateCount + "更新");
            } else if (lessonUpdateCount > 99) {
                purchasedRecord.setUpdateTips("99+更新");
            }


            //设置课程更新进度
            String previewFirstField = course.getPreviewFirstField();
            String previewSecondField = course.getPreviewSecondField();
            purchasedRecord.setPreviewFirstField(previewFirstField);
            purchasedRecord.setPreviewSecondField(previewSecondField);
            purchasedRecord.setLastLearnLessonName(StringUtils.isBlank(course.getLastLearnLessonName()) ? "未开始学习" : course.getLastLearnLessonName());
            courseList.add(purchasedRecord);

        }
    }
}
