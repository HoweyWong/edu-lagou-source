package com.lagou.edu.course.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年7月8日 上午11:00:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCourseUpdateStockDTO implements Serializable {


	/**
	 */
	private static final long serialVersionUID = -7676716381184085300L;

	/**
	 * id
	*/
	private Integer id;
}
