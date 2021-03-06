package com.lagou.edu.course.remote;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.course.api.dto.MediaDTO;
import com.lagou.edu.course.api.dto.VideoPlayDto;
import com.lagou.edu.course.config.AliyunConfig;
import com.lagou.edu.course.entity.po.Lesson;
import com.lagou.edu.course.entity.po.Media;
import com.lagou.edu.course.service.ILessonService;
import com.lagou.edu.course.service.IMediaService;
import com.lagou.edu.order.api.UserCourseOrderRemoteService;
import com.netflix.client.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class MediaService {

    @Autowired
    private IMediaService mediaService;
    @Autowired
    private ILessonService lessonService;

    @Autowired
    private UserCourseOrderRemoteService userCourseOrderRemoteService;
    @Autowired
    private AliyunConfig aliyunConfig;
    private DefaultAcsClient aliClient = null;


    
    public MediaDTO getByLessonId(Integer lessonId) {
        QueryWrapper<Media> mediaQueryWrapper = new QueryWrapper<>();
        mediaQueryWrapper.eq("lesson_id",lessonId);
        mediaQueryWrapper.eq("is_del", Boolean.FALSE);
        List<Media> mediaList = this.mediaService.list(mediaQueryWrapper);
        if (CollectionUtils.isEmpty(mediaList)) {
            return null;
        }
        MediaDTO mediaDTO = new MediaDTO();
        BeanUtil.copyProperties(mediaList.get(0),mediaDTO);
        return mediaDTO;
    }

    
    public byte[] getCourseMediaDKByFileId(String fileId, String edk, Integer userId) {
        QueryWrapper<Media> query = new QueryWrapper<Media>().eq("file_id", fileId).eq("is_del", Boolean.FALSE);
        List<Media> courseMediaList = mediaService.list(query);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(courseMediaList)) {
            log.info("fileId:{}??????????????????????????????", fileId);
            return null;
        }

        List<Integer> courseIdList = new ArrayList<>(courseMediaList.size());
        List<Integer> lessonIdList = new ArrayList<>(courseMediaList.size());
        for (Media courseMedia : courseMediaList) {
            courseIdList.add(courseMedia.getCourseId());
            lessonIdList.add(courseMedia.getLessonId());
        }


        List<Lesson> courseLessonList = lessonService.listByIds(lessonIdList);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(courseLessonList)) {
            log.info("lessonIdList:{}????????????????????????????????????", lessonIdList);
            return null;
        }

        Media courseMedia = courseMediaList.get(0);
        String mediaEdk = courseMedia.getFileEdk();
        if (!Objects.equals(mediaEdk, edk)) {
            log.info("???????????????EDK????????????fileId:{},fromEDK:{}", fileId, edk);
            return null;
        }

        String mediaDk = courseMedia.getFileDk();
        if (StringUtils.isBlank(mediaDk)) {
            log.info("fileId:{}?????????????????????DK?????????", fileId);
            return null;
        }

        byte[] dkBytes = Base64.getDecoder().decode(mediaDk);

        //???????????????????????????????????????????????????
        for (Lesson lesson : courseLessonList) {
            //???????????????????????????????????????
            if (lesson.getIsFree() != null && lesson.getIsFree()) {
                log.info("lessonId:{}???????????????,fileId:{},mediaDk:{},dkBytes:{} ", lesson.getId(), fileId, mediaDk,dkBytes);
                return dkBytes;
            }
        }

        //?????????ID???null???????????????????????????????????????null
        if (userId == null) {
            log.info("???????????? ?????????????????? ????????????");
            return null;
        }
        ResponseDTO<Integer> responseDTO = userCourseOrderRemoteService.countUserCourseOrderByCoursIds(userId, courseIdList);
        if(!responseDTO.isSuccess()){
            log.info("???????????????????????? responseDTO???{}",JSON.toJSONString(responseDTO));
            return null;
        }
        Integer orderCount = responseDTO.getContent();
        if (orderCount <= 0) {
            log.info("????????????????????????,courseIdList:{}, fileId:{}???userId:{}", courseIdList, fileId, userId);
            return null;
        }

        return dkBytes;
    }

    public void updateOrSaveMedia(MediaDTO mediaDTO) {
        Media media = new Media();
        BeanUtils.copyProperties(mediaDTO,media);
        Integer lessonId = mediaDTO.getLessonId();
        Lesson lesson = lessonService.getById(lessonId);
        if(lesson == null){
            log.error("???????????????lesson?????? mediaDTO:{}", JSON.toJSONString(mediaDTO));
            return;
        }
        lesson.setDuration(media.getDurationNum()/60);
        lessonService.saveOrUpdate(lesson);
        Integer courseId = lesson.getCourseId();
        Integer sectionId = lesson.getSectionId();
        media.setCourseId(courseId);
        media.setSectionId(sectionId);
        QueryWrapper<Media> query = new QueryWrapper<Media>();
        query.eq("lesson_id", lessonId);
        query.eq("course_id", courseId);
        query.eq("section_id", sectionId);
        query.eq("is_del", Boolean.FALSE);
        List<Media> medias = mediaService.list(query);
        if(!CollectionUtils.isEmpty(medias)){
            Integer id = medias.get(0).getId();
            media.setId(id);
        }else{
            media.setCreateTime(LocalDateTime.now());
        }
        media.setUpdateTime(LocalDateTime.now());
        mediaService.saveOrUpdate(media);
    }

    public VideoPlayDto getVideoPlayInfo(Integer lessonId, Integer userId){
        MediaDTO mediaDTO = getByLessonId(lessonId);
        if (mediaDTO == null) {
            log.info("??????????????????????????????,lessonId={},userId={}", lessonId, userId);
            return null;
        }
        //??????????????????????????????????????????
        String fileId = mediaDTO.getFileId();

        VideoPlayDto aliYunVideoPlayInfo = new VideoPlayDto();
        try {
            String playAuth = getAliVideoPlayAuth(fileId);
            aliYunVideoPlayInfo.setPlayAuth(playAuth);
        } catch (com.aliyuncs.exceptions.ClientException e) {
            log.info("???????????????????????????PlayAuth?????????",e);
            return  null;
        }

        aliYunVideoPlayInfo.setFileId(fileId);
        Lesson lesson = lessonService.getById(lessonId);


        if (lesson.getIsFree()) {
            log.info("???????????????????????????,lessonId={},userId={}", lessonId, userId);
            return aliYunVideoPlayInfo;
        }

        if (userId == null) {
            log.info("??????ID??????,lessonId={},userId={}", lessonId, userId);
            return null;
        }

        Integer courseId = lesson.getCourseId();
        if (courseId == null) {
            log.info("?????????????????????????????????ID??????, fileId:{}???userId:{}", fileId, userId);
            return null;
        }
        ResponseDTO<Integer> responseDTO = userCourseOrderRemoteService.countUserCourseOrderByCoursIds(userId, Arrays.asList(courseId));
        if(responseDTO.isSuccess() && responseDTO.getContent().compareTo(0) > 0 ){
            return aliYunVideoPlayInfo;
        }

        return null;
    }

    /**
     * ????????????auth
     *
     * @param vid
     * @return
     * @throws ClientException
     */
    public String getAliVideoPlayAuth(String vid) throws com.aliyuncs.exceptions.ClientException {
        GetVideoPlayAuthRequest playAuthRequest = new GetVideoPlayAuthRequest();
        playAuthRequest.setVideoId(vid);
        GetVideoPlayAuthResponse acsResponse = getAliVodClient().getAcsResponse(playAuthRequest);
        return acsResponse.getPlayAuth();
    }
    protected DefaultAcsClient getAliVodClient() {
        if (aliClient != null) {
            return aliClient;
        }
        // ????????????????????????
        String regionId = "cn-beijing";
        DefaultProfile profile = DefaultProfile.getProfile(regionId, aliyunConfig.getAccessKeyId(), aliyunConfig.getAccessKeySecret());
        aliClient = new DefaultAcsClient(profile);
        return aliClient;
    }
}
