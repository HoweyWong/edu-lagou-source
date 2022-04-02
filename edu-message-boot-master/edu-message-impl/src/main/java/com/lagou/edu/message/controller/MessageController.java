package com.lagou.edu.message.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lagou.edu.common.page.DataGrid;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.message.api.dto.MessageDTO;
import com.lagou.edu.message.api.dto.MessageQueryDTO;
import com.lagou.edu.message.service.IMessageService;

/**
 * @author: ma wei long
 * @date:   2020年6月28日 下午1:46:09
 */
@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
    private IMessageService messageService;
    
	/**
	 * @author: ma wei long
	 * @date:   2020年6月28日 下午1:47:56   
	*/
	@PostMapping("/getMessageList")
    public ResponseDTO<DataGrid<MessageDTO>> getCourseOrderByOrderNo(@RequestBody MessageQueryDTO messageQueryDTO) {
        return ResponseDTO.success(messageService.getMessageByUserId(messageQueryDTO));
    }
    
    /**
	 * @author: ma wei long
	 * @date:   2020年6月29日 上午11:13:18   
	*/
	@PostMapping("/updateReadStatus")
	ResponseDTO<Boolean> updateReadStatus(@RequestParam("userId") Integer userId){
		return ResponseDTO.success(messageService.updateReadStatus(userId));
	}
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月13日 下午8:01:32   
	*/
	@GetMapping("/getUnReadMessageFlag")
	ResponseDTO<Boolean> getUnReadMessageFlag(@RequestParam("userId") Integer userId){
		return ResponseDTO.success(messageService.getUnReadMessageFlag(userId));
	}
}
