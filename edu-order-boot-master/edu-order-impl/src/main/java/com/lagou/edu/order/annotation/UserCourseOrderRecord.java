package com.lagou.edu.order.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lagou.edu.order.api.enums.StatusTypeEnum;

/**
 * @author: ma wei long
 * @date:   2020年6月21日 下午11:58:32
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserCourseOrderRecord {
	StatusTypeEnum type();
}