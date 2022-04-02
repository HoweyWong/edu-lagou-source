package com.lagou.edu.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author leo
 * @since 2020-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PromotionAdResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    private int state;

    private  T content;


    public PromotionAdResult() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PromotionAdResult{" +
                "message='" + message + '\'' +
                ", state=" + state +
                ", content=" + content +
                '}';
    }
}
