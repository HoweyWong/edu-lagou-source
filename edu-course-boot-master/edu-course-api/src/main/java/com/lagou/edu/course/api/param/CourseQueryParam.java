package com.lagou.edu.course.api.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseQueryParam implements Serializable {
     Integer currentPage;
     Integer pageSize;
     String courseName;
     Integer status;
}
