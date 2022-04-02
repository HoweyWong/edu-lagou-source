package com.lagou.edu.course.controller;


import com.lagou.edu.course.api.dto.MediaDTO;
import com.lagou.edu.course.api.dto.VideoPlayDto;
import com.lagou.edu.course.remote.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leo
 * @since 2020-06-17
 */
@RestController
@RequestMapping("/course/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("/getByLessonId")
    public MediaDTO getByLessonId(@RequestParam("lessonId") Integer lessonId) {
        return mediaService.getByLessonId(lessonId);
    }

    /**
     * 更新或者保存媒体
     */
    @PostMapping(value = "/updateOrSaveMedia")
    void updateOrSaveMedia(@RequestBody MediaDTO mediaDTO){
         mediaService.updateOrSaveMedia(mediaDTO);
    }

    /**
     * 获取是媒体播放信息
     */
    @GetMapping("/getVideoPlayInfo")
    VideoPlayDto getVideoPlayInfo(Integer lessonId, Integer userId){
        return mediaService.getVideoPlayInfo(lessonId,userId);
    }

    @GetMapping("/alikey")
    byte[] getCourseMediaDKByFileId(@RequestParam("fileId") String fileId,
                                    @RequestParam("edk")  String edk,
                                    @RequestParam("userId")  Integer userId){
        return mediaService.getCourseMediaDKByFileId(fileId,edk,userId);
    }

}
