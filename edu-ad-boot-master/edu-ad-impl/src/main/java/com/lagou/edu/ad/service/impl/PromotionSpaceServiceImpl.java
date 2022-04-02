package com.lagou.edu.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.ad.entity.PromotionSpace;
import com.lagou.edu.ad.mapper.PromotionSpaceMapper;
import com.lagou.edu.ad.service.IPromotionSpaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leo
 * @since 2020-06-18
 */
@Service
public class PromotionSpaceServiceImpl extends ServiceImpl<PromotionSpaceMapper, PromotionSpace> implements IPromotionSpaceService {

    public PromotionSpace getBySpaceKey(String spaceKey) {

        QueryWrapper<PromotionSpace> queryWrapper = new QueryWrapper();
        //根据spaceKey获取PromotionSpace
        queryWrapper.eq("spaceKey",spaceKey);
        return this.getBaseMapper().selectOne(queryWrapper);
    }
}
