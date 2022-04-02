package com.lagou.edu.common.constant;

/**
 * @Description:(mq常量配置信息)   
 * @author: ma wei long
 * @date:   2020年6月27日 下午12:27:19
 */
public interface MQConstant {
	
	 /**
	  * @Description:(mq topic)   
	  * @author: ma wei long
	  * @date:   2020年6月27日 下午12:27:12
	  */
     interface Topic {
    	 String CANCEL_PAY_ORDER = "cancel_pay_order";
    	 String LESSON_STATUS_RELEASE = "lesson_status_release";
    	 String LESSON_RELEASE_SEND_MESSAGE = "lesson_release_send_message";
    	 String ACTIVITY_COURSE_STOCK = "activity_course_stock";
     }
     
     /**
      * @Description:(延时级别)   
      * @author: ma wei long
      * @date:   2020年6月27日 下午1:05:16
     */
     interface DelayLevel {
    	 int level_1 = 1;//延时1秒
    	 int level_2 = 2;//延时5秒
    	 int level_3 = 3;//延时10秒
    	 int level_4 = 4;//延时30秒
    	 int level_5 = 5;//延时1分钟
    	 int level_6 = 6;//延时2分钟
    	 int level_7 = 7;//延时3分钟
    	 int level_8 = 8;//延时4分钟
    	 int level_9 = 9;//延时5分钟
    	 int level_10 = 10;//延时6分钟
    	 int level_11 = 11;//延时7分钟
    	 int level_12 = 12;//延时8分钟
    	 int level_13 = 13;//延时9分钟
    	 int level_14 = 14;//延时10分钟
    	 int level_15 = 15;//延时20分钟
    	 int level_16 = 16;//延时30分钟
    	 int level_17 = 17;//延时1小时
    	 int level_18 = 18;//延时2小时
     }
}
