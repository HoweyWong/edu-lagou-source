package com.lagou.edu.comment.api.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * Author:   mkp
 * Date:     2020/7/7 11:00
 * Description: 评论查询参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCommentParam  implements Serializable    {
    private Integer userId;
    private Integer courseId;
    private int pageNum;
    private int pageSize;
}
