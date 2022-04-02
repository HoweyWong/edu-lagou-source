package com.lagou.edu.message.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.lagou.edu.common.page.DataGrid;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.result.ResultCode;
import com.lagou.edu.common.util.ConvertUtils;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.course.api.CourseRemoteService;
import com.lagou.edu.course.api.LessonRemoteService;
import com.lagou.edu.course.api.dto.CourseDTO;
import com.lagou.edu.course.api.dto.LessonDTO;
import com.lagou.edu.course.api.enums.CourseLessonStatus;
import com.lagou.edu.message.api.dto.MessageDTO;
import com.lagou.edu.message.api.dto.MessageQueryDTO;
import com.lagou.edu.message.entity.Message;
import com.lagou.edu.message.mapper.MessageMapper;
import com.lagou.edu.message.server.PushServer;
import com.lagou.edu.message.service.IMessageService;
import com.lagou.edu.order.api.UserCourseOrderRemoteService;
import com.lagou.edu.order.api.dto.UserCourseOrderDTO;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月28日 下午1:45:03
 */
@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
	
	@Autowired
	private LessonRemoteService lessonRemoteService;
	@Autowired
	private UserCourseOrderRemoteService userCourseOrderRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	/**
	 * @author: ma wei long
	 * @date:   2020年6月28日 下午1:50:55   
	*/
	@Override
	public DataGrid<MessageDTO> getMessageByUserId(MessageQueryDTO messageQueryDTO) {
		log.info("getMessageByUserId - messageQueryDTO:{}",JSON.toJSONString(messageQueryDTO));
		ValidateUtils.notNullParam(messageQueryDTO.getUserId());
		ValidateUtils.isTrue(messageQueryDTO.getUserId() > 0, "用户id参数错误");
		
		QueryWrapper<Message> wrapper = new QueryWrapper<Message>();
        wrapper.eq("user_id", messageQueryDTO.getUserId()).orderByDesc("id");

  	    IPage<Message> page = new Page<Message>(messageQueryDTO.getPage(),messageQueryDTO.getRows());
  	    
        IPage<Message> pageDate = page(page, wrapper);
        
        DataGrid<MessageDTO> dataGrid = new DataGrid<>();
        dataGrid.setRows(ConvertUtils.convertList(pageDate.getRecords(), MessageDTO.class));
        dataGrid.setTotal(pageDate.getTotal());
        
		return dataGrid;
	}

	/**
	 * @author: ma wei long
	 * @date:   2020年6月29日 上午11:14:49   
	*/
	@Override
	public Boolean updateReadStatus(Integer userId) {
		log.info("updateReadStatus - userId:{}",userId);
		ValidateUtils.notNullParam(userId);
		ValidateUtils.isTrue(userId > 0, "用户id参数错误");
		
		Message updateMessage = new Message();
		updateMessage.setHasRead(1);
		return update(updateMessage, new QueryWrapper<Message>().eq("user_id", userId));
	}

	/**
	 * @author: ma wei long
	 * @date:   2020年6月30日 上午10:28:06   
	*/
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<Integer> saveMessage(Integer lessonId) {
		log.info("saveMessage - lessonId:{}",lessonId);
		ValidateUtils.notNullParam(lessonId);
		ValidateUtils.isTrue(lessonId > 0, "课时id参数错误");
		
		LessonDTO lessonDB = lessonRemoteService.getById(lessonId);
		ValidateUtils.isTrue(null != lessonDB, StringUtils.join("课时信息查询为空-lessonID:",lessonId));
		
		if(!lessonDB.getStatus().equals(CourseLessonStatus.RELEASE.getCode())) {
			log.warn("saveMessage - 课时id:{} 状态为：{} 不能发送消息",lessonId,CourseLessonStatus.valueOf(lessonDB.getStatus()));
			return Lists.newArrayList();
		}
		
		ResponseDTO<List<UserCourseOrderDTO>> resp = userCourseOrderRemoteService.getOrderListByCourseId(lessonDB.getCourseId());
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(), resp.getMessage());
		
		List<UserCourseOrderDTO> userCourseOrderList = resp.getContent();
		
		if(Collections.isEmpty(userCourseOrderList)) {
			log.warn("saveMessage - lessonId:{} courseId:{} 没有查到支付成功的商品订单",lessonId,lessonDB.getCourseId());
			return Lists.newArrayList();
		}
		
		CourseDTO courseDTO = courseRemoteService.getCourseById(lessonDB.getCourseId(), userCourseOrderList.get(0).getUserId());
		ValidateUtils.notNull(courseDTO, ResultCode.ALERT_ERROR.getState(), StringUtils.join("课程信息查询为空-courseId:",lessonDB.getCourseId()));
		
		List<Message> saveMessageList = new ArrayList<Message>();
		List<Integer> res = new ArrayList<Integer>();
		Message saveMessage = null;
		for(UserCourseOrderDTO userCourseOrder : userCourseOrderList) {
			saveMessage = new Message();
			saveMessage.setCourseId(userCourseOrder.getCourseId());
			saveMessage.setCourseLessonId(lessonId);
			saveMessage.setCourseName(courseDTO.getCourseName());
			saveMessage.setCreateTime(new Date());
			saveMessage.setTheme(lessonDB.getTheme());
			saveMessage.setUpdateTime(saveMessage.getCreateTime());
			saveMessage.setUserId(userCourseOrder.getUserId());
			saveMessageList.add(saveMessage);
			res.add(userCourseOrder.getUserId());
		}
		boolean saveRes = saveBatch(saveMessageList);
		ValidateUtils.isTrue(saveRes, "批量保存消息通知异常");
		
		return res;
	}

	/**
	 * @author: ma wei long
	 * @date:   2020年6月30日 下午4:57:36   
	*/
	@Override
	public void sendMessage(com.lagou.edu.message.api.dto.Message message) {
		if(null == message) {
			return;
		}
		PushServer.pushServer.push(message);
	}

	/**
	 * @author: ma wei long
	 * @date:   2020年7月13日 下午8:02:32   
	*/
	@Override
	public Boolean getUnReadMessageFlag(Integer userId) {
		return count(new QueryWrapper<Message>().eq("user_id", userId).eq("has_read", 0)) > 0;
	}
}
