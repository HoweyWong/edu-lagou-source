package com.lagou.edu.pay.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lagou.edu.order.api.enums.StatusTypeEnum;

/**
 * @author: ma wei long
 * @date:   2020年6月22日 上午12:27:56
*/
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PayOrderRecord {
	StatusTypeEnum type();
}