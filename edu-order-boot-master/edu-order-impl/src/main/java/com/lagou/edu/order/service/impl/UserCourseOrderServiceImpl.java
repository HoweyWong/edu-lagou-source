package com.lagou.edu.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dangdang.ddframe.rdb.sharding.keygen.KeyGenerator;
import com.google.common.collect.Lists;
import com.lagou.edu.common.constant.CacheDefine;
import com.lagou.edu.common.constant.MQConstant;
import com.lagou.edu.common.mq.RocketMqService;
import com.lagou.edu.common.mq.dto.BaseMqDTO;
import com.lagou.edu.common.util.ConvertUtils;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.course.api.CourseRemoteService;
import com.lagou.edu.course.api.dto.ActivityCourseDTO;
import com.lagou.edu.course.api.dto.ActivityCourseUpdateStockDTO;
import com.lagou.edu.course.api.dto.CourseDTO;
import com.lagou.edu.course.api.enums.CourseStatus;
import com.lagou.edu.order.annotation.UserCourseOrderRecord;
import com.lagou.edu.order.api.dto.CreateShopGoodsOrderReqDTO;
import com.lagou.edu.order.api.dto.UserCourseOrderDTO;
import com.lagou.edu.order.api.dto.UserCourseOrderResDTO;
import com.lagou.edu.order.api.enums.StatusTypeEnum;
import com.lagou.edu.order.api.enums.UserCourseOrderSourceType;
import com.lagou.edu.order.api.enums.UserCourseOrderStatus;
import com.lagou.edu.order.entity.UserCourseOrder;
import com.lagou.edu.order.mapper.UserCourseOrderMapper;
import com.lagou.edu.order.service.IUserCourseOrderService;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date: 2020年6月17日 上午11:47:09
 */
@Slf4j
@Service
public class UserCourseOrderServiceImpl extends ServiceImpl<UserCourseOrderMapper, UserCourseOrder>
    implements IUserCourseOrderService {

    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private IUserCourseOrderService userCourseOrderService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RocketMqService rocketMqService;
    @Autowired
    private KeyGenerator keygenerator;

    /**
     * @Description: (创建商品订单)
     * @author: ma wei long
     * @date: 2020年6月19日 上午10:43:26
     */
    @Override
    public UserCourseOrderResDTO saveOrder(CreateShopGoodsOrderReqDTO reqDTO) {
        // 校验商品信息
        CourseDTO courseDTO = courseRemoteService.getCourseById(reqDTO.getGoodsId(), reqDTO.getUserId());
        log.info("saveOrder - courseRemoteService.getCourseById - goodsId:{} courseDTO:{}", reqDTO.getGoodsId(),
            JSON.toJSONString(courseDTO));
        ValidateUtils.isFalse(null == courseDTO, "课程信息为空");
        ValidateUtils.isTrue(courseDTO.getStatus().equals(CourseStatus.PUTAWAY.getCode()), "课程状态错误");
        UserCourseOrder userCourseOrder = checkSuccessBuyGoods(reqDTO.getGoodsId(), reqDTO.getUserId());
        ValidateUtils.isTrue(null == userCourseOrder, "已成功购买过该课程");
        userCourseOrder = checkCreateBuyGoods(reqDTO.getGoodsId(), reqDTO.getUserId());
        if (null != userCourseOrder) {
            // 已经下单还有效
            return new UserCourseOrderResDTO(userCourseOrder.getOrderNo());
        }
        // 创建商品订单
        UserCourseOrder saveOrder =
            buildUserCourseOrder(reqDTO.getGoodsId(), reqDTO.getUserId(), reqDTO.getSourceType());
        String activityCourseStr =
            redisTemplate.opsForValue().get(CacheDefine.ActivityCourse.getKey(reqDTO.getGoodsId()));
        ActivityCourseDTO activityCourseCache = null;
        if (StringUtils.isNotBlank(activityCourseStr)) {
            activityCourseCache = JSON.parseObject(activityCourseStr, ActivityCourseDTO.class);
            Long cacheRes = redisTemplate.opsForValue()
                .increment(CacheDefine.ActivityCourse.getStockKey(activityCourseCache.getCourseId()), -1L);
            log.info("saveOrder - increment - activityCourseId:{} courseId:{} cacheRes:{}", activityCourseCache.getId(),
                reqDTO.getGoodsId(), cacheRes);
            if (cacheRes >= 0) {
                saveOrder.setActivityCourseId(activityCourseCache.getId());
            } else {
                redisTemplate.opsForValue()
                    .increment(CacheDefine.ActivityCourse.getStockKey(activityCourseCache.getId()), 1L);
            }
        }
        try {
            userCourseOrderService.saveOrder(saveOrder);
        } catch (Exception e) {
            log.error("saveOrder - reqDTO:{} err", JSON.toJSONString(reqDTO), e);
            // 异常还原库存
            if (saveOrder.getActivityCourseId() != null) {
                redisTemplate.opsForValue()
                    .increment(CacheDefine.ActivityCourse.getStockKey(activityCourseCache.getId()), 1L);
            }
            ValidateUtils.isTrue(false, "课程订单处理失败");
        }
        // 发送MQ
        if (saveOrder.getActivityCourseId() != null) {
            rocketMqService.convertAndSend(MQConstant.Topic.ACTIVITY_COURSE_STOCK,
                new BaseMqDTO<ActivityCourseUpdateStockDTO>(
                    new ActivityCourseUpdateStockDTO(saveOrder.getActivityCourseId()), UUID.randomUUID().toString()));
        }
        return new UserCourseOrderResDTO(saveOrder.getOrderNo());
    }

    /**
     * @Description: (查询用户成功状态订单)
     * @author: ma wei long
     * @date: 2020年6月19日 上午10:42:20
     */
    UserCourseOrder checkSuccessBuyGoods(Integer goodId, Integer userId) {
        ValidateUtils.notNullParam(userId);
        ValidateUtils.isTrue(userId > 0, "用户id错误");
        ValidateUtils.notNullParam(goodId);
        ValidateUtils.isTrue(goodId > 0, "课程id错误");
        return getOne(new QueryWrapper<UserCourseOrder>().eq("course_id", goodId).eq("user_id", userId).eq("status",
            UserCourseOrderStatus.SUCCESS.getCode()));
    }

    /**
     * @Description: (查询用户新建状态订单)
     * @author: ma wei long
     * @date: 2020年6月19日 上午10:42:20
     */
    UserCourseOrder checkCreateBuyGoods(Integer goodId, Integer userId) {
        ValidateUtils.notNullParam(userId);
        ValidateUtils.isTrue(userId > 0, "用户id错误");
        ValidateUtils.notNullParam(goodId);
        ValidateUtils.isTrue(goodId > 0, "课程id错误");
        return getOne(new QueryWrapper<UserCourseOrder>().eq("course_id", goodId).eq("user_id", userId).eq("status",
            UserCourseOrderStatus.CREATE.getCode()));
    }

    /**
     * @Description: (构建商品订单信息)
     * @author: ma wei long
     * @date: 2020年6月19日 上午10:41:52
     */
    UserCourseOrder buildUserCourseOrder(Integer goodId, Integer userId, UserCourseOrderSourceType sourceType) {
        UserCourseOrder saveUserCourseOrder = new UserCourseOrder();
        saveUserCourseOrder.setId(Long.parseLong(keygenerator.generateKey().toString()));
        saveUserCourseOrder.setCourseId(goodId);
        saveUserCourseOrder.setCreateTime(new Date());
        saveUserCourseOrder.setOrderNo(keygenerator.generateKey().toString());
        saveUserCourseOrder.setSourceType(sourceType.getCode());
        saveUserCourseOrder.setUpdateTime(saveUserCourseOrder.getCreateTime());
        saveUserCourseOrder.setUserId(userId);
        return saveUserCourseOrder;
    }

    /**
     * @Description: (根据订单号获取订单信息)
     * @author: ma wei long
     * @date: 2020年6月19日 上午11:31:59
     */
    @Override
    public UserCourseOrderDTO getCourseOrderByOrderNo(String orderNo) {
        ValidateUtils.notNullParam(orderNo);
        UserCourseOrder userCourseOrderDB = getOne(new QueryWrapper<UserCourseOrder>().eq("order_no", orderNo));
        ValidateUtils.isTrue(null != userCourseOrderDB, "商品订单信息查询为空");
        return ConvertUtils.convert(userCourseOrderDB, UserCourseOrderDTO.class);
    }

    /**
     * @Description: (更新商品订单状态)
     * @author: ma wei long
     * @date: 2020年6月21日 下午11:15:18
     */
    @Override
    @UserCourseOrderRecord(type = StatusTypeEnum.UPDATE)
    public void updateOrderStatus(String orderNo, Integer status) {
        ValidateUtils.notNullParam(orderNo);
        ValidateUtils.notNullParam(status);

        UserCourseOrder uerCourseOrderDB = getOne(new QueryWrapper<UserCourseOrder>().eq("order_no", orderNo));
        ValidateUtils.isTrue(null != uerCourseOrderDB, "商品订单信息查询为空");

        if (uerCourseOrderDB.getStatus().equals(status)) {
            return;
        }
        if (uerCourseOrderDB.getStatus().equals(UserCourseOrderStatus.SUCCESS.getCode())) {
            return;
        }
        UserCourseOrder updateUerCourseOrder = new UserCourseOrder();
        updateUerCourseOrder.setId(uerCourseOrderDB.getId());
        updateUerCourseOrder.setStatus(status);

        ValidateUtils.isTrue(updateById(updateUerCourseOrder), "更新商品订单信息查询失败");
    }

    /**
     * @Description: (保存订单信息)
     * @author: ma wei long
     * @date: 2020年6月21日 下午11:44:42
     */
    @Override
    @UserCourseOrderRecord(type = StatusTypeEnum.INSERT)
    public void saveOrder(UserCourseOrder order) {
        save(order);
    }

    /**
     * @Description: (根据用户id获取订单列表)
     * @author: ma wei long
     * @date: 2020年6月22日 下午8:27:22
     */
    @Override
    public List<UserCourseOrderDTO> getUserCourseOrderByUserId(Integer userId) {
        ValidateUtils.notNullParam(userId);
        ValidateUtils.isTrue(userId > 0, "用户id错误");
        List<UserCourseOrder> userCourseOrderList = list(new QueryWrapper<UserCourseOrder>().eq("user_id", userId)
            .eq("status", UserCourseOrderStatus.SUCCESS.getCode()).orderByDesc("id"));
        if (CollectionUtils.isEmpty(userCourseOrderList)) {
            return Lists.newArrayList();
        }
        return ConvertUtils.convertList(userCourseOrderList, UserCourseOrderDTO.class);
    }

    /**
     * @Description: (根据用户&课程id统计订单数量)
     * @author: ma wei long
     * @date: 2020年6月23日 下午1:16:18
     */
    @Override
    public Integer countUserCourseOrderByCoursIds(Integer userId, List<Integer> coursIds) {
        ValidateUtils.notNullParam(userId);
        ValidateUtils.isTrue(userId > 0, "用户id错误");
        return count(new QueryWrapper<UserCourseOrder>().eq("user_id", userId).in("course_id", coursIds).eq("status",
            UserCourseOrderStatus.SUCCESS.getCode()));
    }

    /**
     * @Description: (根据课程id统计支付成功订单数量)
     * @author: ma wei long
     * @date: 2020年6月29日 上午11:28:05
     */
    @Override
    public Integer countUserCourseOrderByCourseId(Integer coursId) {
        ValidateUtils.notNullParam(coursId);
        ValidateUtils.isTrue(coursId > 0, "课程id错误");
        return count(new QueryWrapper<UserCourseOrder>().eq("course_id", coursId).eq("status",
            UserCourseOrderStatus.SUCCESS.getCode()));
    }

    /**
     * @Description: (根据课程id查询支付成功订单集合)
     * @author: ma wei long
     * @date: 2020年6月30日 上午10:40:47
     */
    @Override
    public List<UserCourseOrderDTO> getOrderListByCourseId(Integer coursId) {
        ValidateUtils.notNullParam(coursId);
        ValidateUtils.isTrue(coursId > 0, "课程id错误");

        List<UserCourseOrder> userCourseOrderList = list(new QueryWrapper<UserCourseOrder>().eq("course_id", coursId)
            .eq("status", UserCourseOrderStatus.SUCCESS.getCode()));
        if (Collections.isEmpty(userCourseOrderList)) {
            return Lists.newArrayList();
        }

        return ConvertUtils.convertList(userCourseOrderList, UserCourseOrderDTO.class);
    }
}
