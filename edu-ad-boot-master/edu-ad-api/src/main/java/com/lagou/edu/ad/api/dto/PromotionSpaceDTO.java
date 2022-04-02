package com.lagou.edu.ad.api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class PromotionSpaceDTO {

    private static final long serialVersionUID = 1L;

    private Integer id;


    /**
     * 名称
     */
    private String name;

    /**
     * 广告位key
     */
    private String spaceKey;



    private Date createTime;

    private Date updateTime;

    private Integer isDel;


    /**
     *
     */
    List<PromotionAdDTO> adDTOList;

}
