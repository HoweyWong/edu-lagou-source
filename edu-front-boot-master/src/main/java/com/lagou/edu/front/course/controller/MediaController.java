package com.lagou.edu.front.course.controller;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.result.ResultCode;
import com.lagou.edu.course.api.MediaRemoteService;
import com.lagou.edu.course.api.dto.MediaDTO;
import com.lagou.edu.course.api.dto.VideoPlayDto;
import com.lagou.edu.front.common.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "课程媒体接口")
@Slf4j
@RestController
@RequestMapping("/course/media")
public class MediaController {

    @Autowired
    private MediaRemoteService mediaRemoteService;

    @ApiOperation("通过课时ID获取媒体信息")
    @GetMapping("getByLessonId")
    public ResponseDTO getByLessonId(@RequestParam("lessonId") Integer lessonId) throws Exception {
        MediaDTO mediaDTO = mediaRemoteService.getByLessonId(lessonId);
        if (mediaDTO == null) {
            return ResponseDTO.response(ResultCode.PARAM_ERROR);
        }
        return ResponseDTO.success(mediaDTO);
    }

    @ApiOperation("获取阿里播放key")
    @GetMapping("alikey")
    public byte[] aliKey(HttpServletRequest request, String code, String vid) {
        Integer userId = UserManager.getUserId();
        log.info("尝试获取阿里播放key:code:{}, vid:{},appId:{},UserId:{}", code, vid,userId);
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(vid)) {
            return null;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            log.warn("检测到用户尚未进行登录处理，无法进行鉴权成功！code = {}, vid = {}", code, vid);
            return null;
        }
        byte[] dkBytes = null;
        try {
            dkBytes = mediaRemoteService.getCourseMediaDKByFileId(vid, code, userId);
            log.info("获取返回结果 dkBytes:{}", JSON.toJSONString(dkBytes));
        } catch (Exception e) {
            log.error("获取视频鉴权失败！vid:{},code:{}", vid, code, e);
        }
        return dkBytes;
    }

    @ApiOperation("根据fileId获取阿里云对应的视频播放信息")
    @GetMapping("videoPlayInfo")
    public ResponseDTO videoPlayInfo(Integer lessonId) {
        try {
            Integer userId = UserManager.getUserId();
            log.info("t根据fileId获取阿里云对应的视频播放信息 userId:{} lessonId:{}",userId,lessonId);
            VideoPlayDto videoPlayDto = mediaRemoteService.getVideoPlayInfo(lessonId, userId);
            return ResponseDTO.success(videoPlayDto);
        } catch (Exception e) {
            log.error("根据fileId获取阿里云对应的视频播放信息失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR);
        }
    }

}