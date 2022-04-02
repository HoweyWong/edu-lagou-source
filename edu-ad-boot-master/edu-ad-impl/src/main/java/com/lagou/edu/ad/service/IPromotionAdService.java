package com.lagou.edu.ad.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.ad.entity.PromotionAd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leo
 * @since 2020-06-18
 */
public interface IPromotionAdService extends IService<PromotionAd> {

    List<PromotionAd> getByPromotionSpaceId(Integer promotionSpaceId);

}
