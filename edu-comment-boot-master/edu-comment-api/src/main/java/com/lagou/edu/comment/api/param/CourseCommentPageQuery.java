package com.lagou.edu.comment.api.param;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bobbao
 * @description
 * @date 2019-08-13 11:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCommentPageQuery extends PageQuery {
    /**
     *    0: 待审核
     *    1: 审核通过
     *    2: 审核不通过
     *    3: 已删除
     */
    private Integer status;

    private Boolean replied;

    /**
     *     0:无标记
     *     1:无需回复
     *     2:讲师回复
     *     3:编辑回复
     *     4:运营回复
     */
    private Integer markBelong;

    private Integer courseId;

    private Integer sectionId;

    private Integer lessonId;

}
