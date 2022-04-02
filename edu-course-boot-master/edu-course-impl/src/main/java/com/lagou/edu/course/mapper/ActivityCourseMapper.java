package com.lagou.edu.course.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lagou.edu.course.entity.po.ActivityCourse;

/**
 * @author: ma wei long
 * @date:   2020年7月6日 下午9:30:14
*/
public interface ActivityCourseMapper extends BaseMapper<ActivityCourse> {
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月8日 上午11:13:23   
	*/
	@Update(" update activity_course set stock = stock - #{num} where id = #{id} and stock >= #{num} ")
	int updateStock(@Param("id") Integer id,@Param("num") Integer num);
}
