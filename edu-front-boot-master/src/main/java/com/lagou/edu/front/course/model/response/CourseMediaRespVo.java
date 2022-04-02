package com.lagou.edu.front.course.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CourseMediaRespVo {
    @ApiModelProperty("课程媒体ID")
    private Integer id;
    @ApiModelProperty("媒体渠道")
    private Integer channel;
    @ApiModelProperty("媒体类型")
    private Integer mediaType;
    @ApiModelProperty("音频封面图")
    private String coverImageUrl;
    @ApiModelProperty("格式化后的音视频时长")
    private String duration;
    @ApiModelProperty("音视频时长")
    private Integer durationNum;
    @ApiModelProperty("音视频fileID")
    private String fileId;
    @ApiModelProperty("音频或视频fileUrl")
    private String fileUrl;
    @ApiModelProperty("媒体文件对应的EDK")
    private String fileEdk;
    @ApiModelProperty("媒体文件大小")
    private Double fileSize;
    @ApiModelProperty("是否免费")
    private boolean isFree;
}
