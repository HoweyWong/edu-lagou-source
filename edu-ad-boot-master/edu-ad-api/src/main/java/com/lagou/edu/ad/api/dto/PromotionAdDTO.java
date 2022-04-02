package com.lagou.edu.ad.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
public class PromotionAdDTO {


    public static final Integer STATUS_ON = 1;   //上架
    public static final Integer STATUS_OFF = 0;   //上架


    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 广告名
     */
    private String name;

    /**
     * 广告位id
     */
    private Integer spaceId;


    /**
     * 精确搜索关键词
     */
    private String keyword;



    /**
     * 静态广告的内容
     */
    private String htmlContent;

    /**
     * 文字一
     */
    private String text;



    /**
     * 图片一
     */
    private String img;



    /**
     * 链接一
     */
    private String link;


    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss",timezone = "GMT+8")
    private Date endTime;



    private Date createTime;

    private Date updateTime;



    private Integer status;



    /**
     * 优先级
     */
    private Integer priority;



    /**
     * 格式化开始时间为yyyy-MM-dd HH:mm:ss字符串
     */
    private String startTimeFormatString;
    public String getStartTimeFormatString(){
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(this.startTime);
    }

    /**
     * 格式化结束时间为yyyy-MM-dd HH:mm:ss字符串
     */
    private String endTimeFormatString;
    public String getEndTimeFormatString(){
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(this.endTime);
    }
}
