package com.lagou.edu.front.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.course.api.CoursePlayHistoryRemoteService;
import com.lagou.edu.course.api.CourseRemoteService;
import com.lagou.edu.course.api.SectionRemoteService;
import com.lagou.edu.course.api.dto.CourseDTO;
import com.lagou.edu.course.api.dto.LessonDTO;
import com.lagou.edu.course.api.dto.MediaDTO;
import com.lagou.edu.course.api.dto.SectionDTO;
import com.lagou.edu.front.course.model.response.CourseLessonRespVo;
import com.lagou.edu.front.course.model.response.CourseMediaRespVo;
import com.lagou.edu.front.course.model.response.CourseSectionListResult;
import com.lagou.edu.front.course.model.response.CourseSectionRespVo;
import com.lagou.edu.front.course.service.SectionService;
import com.lagou.edu.order.api.UserCourseOrderRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author:   mkp
 * Date:     2020/6/24 10:39
 * Description: 章节操作
 */
@Slf4j
@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private UserCourseOrderRemoteService userCourseOrderRemoteService;
    @Autowired
    private CoursePlayHistoryRemoteService coursePlayHistoryRemoteService;
    @Autowired
    private SectionRemoteService sectionRemoteService;
    @Override
    public CourseSectionListResult getSectionInfoByCourseId(Integer userId, Integer courseId) {
        CourseSectionListResult result = new CourseSectionListResult();
        if (courseId == null) {
            return result;
        }

        CourseDTO course = courseRemoteService.getCourseById(courseId,userId);
        if(course == null){
            return result;
        }


        //设置课程名称
        result.setCourseName(course.getCourseName());
        // 设置课程封面图
        result.setCoverImage(course.getCourseListImg());

        //保存用户已学的课时ID
        Set hasLearnedLessonIds = new HashSet();
        if(userId!=null){
            result.setHasBuy(checkHasBuy(userId, courseId));
            // 获取某一课已经学习课时
            List lessonIds = coursePlayHistoryRemoteService.hasStudyLessons(userId,courseId);
            if(!CollectionUtils.isEmpty(lessonIds)){
                hasLearnedLessonIds.addAll(lessonIds);
            }
        }
        List<SectionDTO> sectionDTOs = sectionRemoteService.getSectionAndLesson(courseId);
        if (CollectionUtils.isEmpty(sectionDTOs)) {
            return result;
        }

        List<CourseSectionRespVo> courseSectionRespVos = new LinkedList<>();

        //拷贝课程章节信息
        for (SectionDTO sectionDTO : sectionDTOs) {
            CourseSectionRespVo sectionRespVo = new CourseSectionRespVo();
            BeanUtils.copyProperties(sectionDTO, sectionRespVo);
            List<LessonDTO> lessonDTOS = sectionDTO.getLessonDTOS();
            List<CourseLessonRespVo> courseLessonRespVos = copyCourseLessonRespVos(lessonDTOS,result.isHasBuy(),hasLearnedLessonIds);
            //章节下没有课时信息时，不进行展示
            if (courseLessonRespVos == null){
                continue;
            }

            sectionRespVo.setCourseLessons(courseLessonRespVos);
            courseSectionRespVos.add(sectionRespVo);
        }
        result.setCourseSectionList(courseSectionRespVos);
        return result;
    }

    private List<CourseLessonRespVo> copyCourseLessonRespVos(List<LessonDTO> lessonDTOS, boolean hasBuy, Set hasLearnedLessonIds) {
        if (CollectionUtils.isEmpty(lessonDTOS)) {
            return null;
        }
        List<CourseLessonRespVo> courseLessonRespVos = new LinkedList<>();
        for (LessonDTO lessonDTO : lessonDTOS) {
            CourseLessonRespVo lessonRespVo = copySingleCourseLessonRespVo(lessonDTO);
            //购买的课程，设置课时设置为可见
            if(hasBuy){
                lessonRespVo.setCanPlay(hasBuy);
            }
            courseLessonRespVos.add(lessonRespVo);
            lessonRespVo.setHasLearned(hasLearnedLessonIds.contains(lessonDTO.getId()));
        }
        return courseLessonRespVos;
    }
    /**
     * 拷贝CourseLesson信息
     * @param lessonDTO
     * @return
     */
    private CourseLessonRespVo copySingleCourseLessonRespVo(LessonDTO lessonDTO) {
        CourseLessonRespVo lessonRespVo = new CourseLessonRespVo();
        BeanUtils.copyProperties(lessonDTO, lessonRespVo);
        if(lessonDTO.getIsFree()){
            lessonRespVo.setCanPlay(true);
        }
        //复制视频信息
        MediaDTO videoMediaDTO = lessonDTO.getMediaDTO();
        if (videoMediaDTO != null) {
            CourseMediaRespVo videoMediaRespVo = new CourseMediaRespVo();
            BeanUtils.copyProperties(videoMediaDTO, videoMediaRespVo);
            supplementDurationOfVideo(videoMediaRespVo);
            lessonRespVo.setVideoMediaDTO(videoMediaRespVo);
            lessonRespVo.setHasVideo(true);
        }

        return lessonRespVo;
    }

    private void supplementDurationOfVideo(CourseMediaRespVo videoMediaDTO){
        String duration = videoMediaDTO.getDuration();
        Integer durationNum = videoMediaDTO.getDurationNum();
        if(durationNum == null && StringUtils.isNotBlank(duration)){
            durationNum = getDurationNum(duration, durationNum);
            videoMediaDTO.setDurationNum(durationNum);
        }

    }
    private Integer getDurationNum(String duration, Integer durationNum) {
        String[] times = duration.split(":");
        if (times.length == 3) {
            durationNum = Integer.parseInt(times[0]) * 60 * 60 + Integer.parseInt(times[1]) * 60 + Integer.parseInt(times[2]);
        }
        if (times.length == 2) {
            durationNum = Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
        }
        if (times.length == 1) {
            durationNum = Integer.parseInt(times[0]);
        }
        return durationNum;
    }
    private boolean checkHasBuy(Integer userId, Integer courseId) {
        ResponseDTO<Integer> responseDTO = userCourseOrderRemoteService.countUserCourseOrderByCoursIds(userId, Arrays.asList(courseId));
        log.info("判断用户是否购买 userId:{}  responseDTO:{}",userId, JSON.toJSONString(responseDTO));
        if(responseDTO.isSuccess() && responseDTO.getContent() > 0){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
