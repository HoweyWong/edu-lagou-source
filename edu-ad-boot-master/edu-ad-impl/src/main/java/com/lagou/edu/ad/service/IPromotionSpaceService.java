package com.lagou.edu.ad.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.ad.entity.PromotionSpace;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leo
 * @since 2020-06-18
 */
public interface IPromotionSpaceService extends IService<PromotionSpace> {

    PromotionSpace getBySpaceKey(String spaceKey);

}
