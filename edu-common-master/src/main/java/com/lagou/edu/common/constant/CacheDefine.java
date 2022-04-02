package com.lagou.edu.common.constant;


/**
 * @Description:(缓存key结构定义)   
 * @author: ma wei long
 * @date:   2020年7月7日 下午8:26:59
 */
public interface CacheDefine {
	
    String PREFIX = "edu";
    String SP = ":";
    String DOT = ".";
    String U = "_";
    
    interface ActivityCourse {
        static String getKey(Integer activityCourseId) {
            return PREFIX + SP + "activityCourse" + SP + activityCourseId;
        }
        static String getStockKey(Integer activityCourseId) {
            return PREFIX + SP + "activityCourse" + SP + "stock" + SP + activityCourseId;
        }
     }
}