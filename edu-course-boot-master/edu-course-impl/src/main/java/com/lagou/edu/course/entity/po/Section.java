package com.lagou.edu.course.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author leo
 * @since 2020-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("course_section")
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 章节名
     */
    private String sectionName;

    /**
     * 章节描述
     */
    private String description;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Boolean isDe;

    /**
     * 排序字段
     */
    private Integer orderNum;

    /**
     * 状态，0:隐藏；1：待更新；2：已发布
     */
    private Integer status;

}
