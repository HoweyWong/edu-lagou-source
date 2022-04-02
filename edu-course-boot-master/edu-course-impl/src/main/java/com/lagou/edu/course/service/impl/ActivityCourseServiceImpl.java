package com.lagou.edu.course.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.edu.common.constant.CacheDefine;
import com.lagou.edu.common.date.DateUtil;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.util.ConvertUtils;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.course.api.dto.ActivityCourseDTO;
import com.lagou.edu.course.api.enums.ActivityCourseStatus;
import com.lagou.edu.course.entity.po.ActivityCourse;
import com.lagou.edu.course.mapper.ActivityCourseMapper;
import com.lagou.edu.course.service.IActivityCourseService;
import com.lagou.edu.order.api.UserCourseOrderRemoteService;
import com.lagou.edu.order.api.dto.UserCourseOrderDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年7月6日 下午9:32:00
 */
@Slf4j
@Service
public class ActivityCourseServiceImpl extends ServiceImpl<ActivityCourseMapper, ActivityCourse> implements IActivityCourseService {

	
	@Autowired
    private RedisTemplate<String,String> redisTemplate;
	@Autowired
	private ActivityCourseMapper activityCourseMapper;
	@Autowired
	private UserCourseOrderRemoteService userCourseOrderRemoteService;
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午7:57:29   
	*/
	@Override
	public void saveActivityCourse(ActivityCourseDTO reqDTO) {
		log.info("saveActivityCourse - reqDTO:{}",JSON.toJSONString(reqDTO));
		checkParam(reqDTO);
		ActivityCourse activityCourse = ConvertUtils.convert(reqDTO, ActivityCourse.class);
		activityCourse.setCreateTime(new Date());
		activityCourse.setCreateUser("auto");//TODO 记得取当前登录用户
		activityCourse.setUpdateTime(activityCourse.getCreateTime());
		activityCourse.setUpdateUser("auto");//TODO 记得取当前登录用户
		
		save(activityCourse);
	}

	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月7日 下午7:57:29   
	*/
	@Override
	public boolean updateActivityCourseStatus(ActivityCourseDTO reqDTO) {
		log.info("updateActivityCourseStatus - reqDTO:{}",JSON.toJSONString(reqDTO));
		ValidateUtils.notNullParam(reqDTO);
		ValidateUtils.notNullParam(reqDTO.getId());
		ValidateUtils.isTrue(reqDTO.getId() > 0, "活动课程id必须大于零");
		ValidateUtils.notNullParam(ActivityCourseStatus.parse(reqDTO.getStatus()));
		
		ActivityCourse activityCourseDB = getById(reqDTO.getId());
		ValidateUtils.notNullParam(activityCourseDB);
		
		if(activityCourseDB.getStatus().equals(reqDTO.getStatus())) {
			return true;
		}
		
		activityCourseDB.setStatus(reqDTO.getStatus());
		boolean res = updateById(activityCourseDB);
		ValidateUtils.isTrue(res, "更新状态失败");
		
		redisTemplate.opsForValue().set(CacheDefine.ActivityCourse.getKey(activityCourseDB.getCourseId()), JSON.toJSONString(ConvertUtils.convert(activityCourseDB, ActivityCourseDTO.class)), DateUtil.getSecond(new Date(), activityCourseDB.getEndTime()), TimeUnit.SECONDS);
		redisTemplate.opsForValue().set(CacheDefine.ActivityCourse.getStockKey(activityCourseDB.getCourseId()), activityCourseDB.getStock().toString(), DateUtil.getSecond(new Date(), activityCourseDB.getEndTime()), TimeUnit.SECONDS);

		return res;
	}

	/**
	 * @author: ma wei long
	 * @date:   2020年7月8日 上午10:31:41   
	*/
	@Override
	public ActivityCourseDTO getByCourseId(Integer courseId) {
		log.info("getByCourseId - courseId:{}",courseId);
		ValidateUtils.notNullParam(courseId);
		ValidateUtils.isTrue(courseId > 0, "课程id必须大于零");
		ActivityCourse activityCourseDB = getOne(new QueryWrapper<ActivityCourse>().eq("course_id", courseId));
//		ValidateUtils.notNullParam(activityCourseDB);
		if(null == activityCourseDB) {
			return null;
		}
		return ConvertUtils.convert(activityCourseDB, ActivityCourseDTO.class);
	}


	/**
	 * @author: ma wei long
	 * @date:   2020年7月8日 上午11:37:32   
	*/
	@Override
	public void updateActivityCourseStock(Integer courseId,String orderNo) {
		log.info("updateActivityCourseStock - courseId；{} orderNo:{}",courseId,orderNo);
		ValidateUtils.notNullParam(orderNo);
		ResponseDTO<UserCourseOrderDTO> resp = userCourseOrderRemoteService.getCourseOrderByOrderNo(orderNo);
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(), resp.getMessage());
		if(resp.getContent().getActivityCourseId() == 0) {
			return;
		}
		ActivityCourseDTO activityCourseDTO = getByCourseId(courseId);
		int res = activityCourseMapper.updateStock(activityCourseDTO.getId(),-1);
		ValidateUtils.isTrue(res == 1, "updateStock is fail id:" + activityCourseDTO.getId());
	}

	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月9日 下午6:56:25   
	*/
	@Override
	public void saveOrUpdateActivityCourse(ActivityCourseDTO reqDTO) {
		log.info("saveOrUpdateActivityCourse - reqDTO:{}",JSON.toJSONString(reqDTO));
		checkParam(reqDTO);
		ActivityCourse activityCourse = ConvertUtils.convert(reqDTO, ActivityCourse.class);
		activityCourse.setUpdateTime(activityCourse.getCreateTime());
		activityCourse.setUpdateUser("auto");//TODO 记得取当前登录用户
		
		ActivityCourseDTO activityCourseDTODB = getByCourseId(reqDTO.getCourseId());
		if(null == activityCourseDTODB) {
			activityCourse.setCreateTime(new Date());
			activityCourse.setCreateUser("auto");//TODO 记得取当前登录用户
			save(activityCourse);
		}else {
			activityCourse.setId(activityCourseDTODB.getId());
			ValidateUtils.isTrue(updateById(activityCourse), "活动课程更新失败，id:"+activityCourse.getId());
		}
		redisTemplate.opsForValue().set(CacheDefine.ActivityCourse.getKey(activityCourse.getCourseId()), JSON.toJSONString(ConvertUtils.convert(activityCourse, ActivityCourseDTO.class)), DateUtil.getSecond(new Date(), activityCourse.getEndTime()), TimeUnit.SECONDS);
		redisTemplate.opsForValue().set(CacheDefine.ActivityCourse.getStockKey(activityCourse.getCourseId()), activityCourse.getStock().toString(), DateUtil.getSecond(new Date(), activityCourse.getEndTime()), TimeUnit.SECONDS);
	}
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月9日 下午7:00:01   
	*/
	private void checkParam(ActivityCourseDTO reqDTO) {
		ValidateUtils.notNullParam(reqDTO);
		ValidateUtils.notNullParam(reqDTO.getAmount());
		ValidateUtils.isTrue(reqDTO.getAmount() > 0, "价格必须大于零");
		ValidateUtils.notNullParam(reqDTO.getCourseId());
		ValidateUtils.isTrue(reqDTO.getCourseId() > 0, "课程必须大于零");
		ValidateUtils.notNullParam(reqDTO.getStock());
		ValidateUtils.isTrue(reqDTO.getStock() > 0, "库存必须大于零");
		ValidateUtils.notNullParam(reqDTO.getBeginTime());
		ValidateUtils.notNullParam(reqDTO.getEndTime());
		ValidateUtils.isTrue(DateUtil.isBefore(reqDTO.getEndTime(), reqDTO.getBeginTime()), "结束时间必须大于开始时间");
	}
}
