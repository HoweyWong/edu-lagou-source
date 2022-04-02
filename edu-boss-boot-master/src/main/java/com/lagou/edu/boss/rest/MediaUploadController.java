package com.lagou.edu.boss.rest;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.lagou.edu.boss.service.AliyunService;
import com.lagou.edu.boss.service.AliyunTask;
import com.lagou.edu.boss.service.impl.AliYunVideoService;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.course.api.LessonRemoteService;
import com.lagou.edu.course.api.MediaRemoteService;
import com.lagou.edu.course.api.dto.LessonDTO;
import com.lagou.edu.course.api.dto.MediaDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author:   mkp
 * Date:     2020/6/24 17:45
 * Description: 阿里视频上传
 */
@Slf4j
@RestController
@Api(tags = "阿里上传", description = "专栏媒体上传")
@RequestMapping("/course/upload")
public class MediaUploadController {
    @Autowired
    private AliyunService aliyunService;
    @Autowired
    private MediaRemoteService  mediaRemoteService;
    @Autowired
    private LessonRemoteService lessonRemoteService;
    @Autowired
    private AliYunVideoService aliYunVideoService;
    @ApiOperation(value = "获取阿里云图片上传凭证")
    @ResponseBody
    @RequestMapping(value = "/aliyunImagUploadAddressAdnAuth.json", method = RequestMethod.GET)
    public Result generateAliyunImagUploadAddressAdnAuth() {
        try {
            final CreateUploadImageResponse createUploadImageResponse = aliYunVideoService.generateAliyunImagUploadAddressAdnAuth();
            return Result.success(createUploadImageResponse);
        } catch (Exception e) {
            log.error("获取阿里云图片上传凭证失败", e);
            return Result.fail("获取阿里云图片上传凭证失败");
        }
    }

    @ApiOperation(value = "获取阿里云视频上传凭证")
    @ResponseBody
    @RequestMapping(value = "/aliyunVideoUploadAddressAdnAuth.json", method = RequestMethod.GET)
    public Result aliyunVideoUploadAddressAdnAuth(String fileName, String imageUrl) {
        try {
            final CreateUploadVideoResponse createUploadVideoResponse = aliYunVideoService.generateVideoUploadAddressAndAuth(fileName, imageUrl);
            return Result.success(createUploadVideoResponse);
        } catch (ClientException e) {
            log.error("获取阿里云视频上传凭证失败", e);
            return Result.fail("获取阿里云视频上传凭证失败");
        }
    }

    @ApiOperation(value = "刷新阿里云视频上传凭证")
    @ResponseBody
    @RequestMapping(value = "/refreshAliyunVideoUploadAddressAdnAuth.json", method = RequestMethod.GET)
    public Result refreshAliyunVideoUploadAddressAdnAuth(@RequestParam String videoId) {
        try {
            final RefreshUploadVideoResponse refreshUploadVideoResponse = aliYunVideoService.refreshUploadVideo(videoId);
            return Result.success(refreshUploadVideoResponse);
        } catch (Exception e) {
            log.error("获取阿里云视频上传凭证失败", e);
            return Result.fail("刷新阿里云视频上传凭证");
        }
    }
    @ApiOperation(value = "阿里云转码请求")
    @ResponseBody
    @RequestMapping(value = "/aliyunTransCode.json", method = RequestMethod.POST)
    public Result aliyunTransCode(@RequestBody MediaDTO courseMediaDTO) {
        try {
            log.info("转码课程信息 courseMediaDTO：{}", JSON.toJSONString(courseMediaDTO));
            AliyunTask aliyunTask = new AliyunTask();
            Integer lessonId = courseMediaDTO.getLessonId();
            aliyunTask.setFileId(courseMediaDTO.getFileId());
            aliyunTask.setTaskId(lessonId.toString());
            aliyunTask.setFileName(courseMediaDTO.getFileName());
            LessonDTO lessonDTO = lessonRemoteService.getById(lessonId);
            courseMediaDTO.setCourseId(lessonDTO.getCourseId());
            courseMediaDTO.setSectionId(lessonDTO.getSectionId());
            aliyunService.processTranscode(aliyunTask, (rAliyunTask -> {
                BeanUtils.copyProperties(rAliyunTask, courseMediaDTO);
                log.info("保存信息 rAliyunTask:{}",JSON.toJSONString(rAliyunTask));
                courseMediaDTO.setDuration(rAliyunTask.getDuration());
                courseMediaDTO.setDurationNum(rAliyunTask.getDurationNum());
                mediaRemoteService.updateOrSaveMedia(courseMediaDTO);
                return null;
            }));
            return Result.success(true);
        } catch (ClientException e) {
            log.error("获取阿里云视频上传凭证失败", e);
            return Result.fail("获取失败");
        }
    }

    @ApiOperation(value = "阿里云转码进度")
    @ResponseBody
    @RequestMapping(value = "/aliyunTransCodePercent.json", method = RequestMethod.GET)
    public Result aliyunTransCodePercent(Integer lessonId) {
        try {
            String taskId =  "aliyun:transCode:" + lessonId;
            final double percent = aliyunService.processTranscodePercent(taskId);
            return Result.success(percent);
        } catch (Exception e) {
            log.error("获取阿里云视频上传凭证失败", e);
            return Result.fail("获取失败");
        }
    }
    @ApiOperation(value = "获取媒体信息")
    @ResponseBody
    @RequestMapping(value = "/getMediaByLessonId.json", method = RequestMethod.GET)
    public Result getMediaByLessonId(Integer lessonId) {
        try {
            MediaDTO mediaDTO = mediaRemoteService.getByLessonId(lessonId);
            return Result.success(mediaDTO);
        } catch (Exception e) {
            log.error("获取阿里云视频上传凭证失败", e);
            return Result.fail("获取失败");
        }
    }

}
