package com.lagou.edu.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author: ma wei long
 * @date:   2020年6月30日 下午8:33:57
*/
@ApiModel("分页基础类")
public class BasePageVO implements Serializable {


    /**
	 */
	private static final long serialVersionUID = 5407112309847105354L;

	/**
     * 页数
     */
    @ApiModelProperty("页数")
    private int page = 1;

    /**
     * 开始记录数
     */
    @ApiModelProperty("开始记录数")
    private int from;

    /**
     * 查询条数
     */
    @ApiModelProperty("查询条数 默认10")
    private int rows = 10;

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:构造方法
     * </p>
     */
    public BasePageVO() {
        super();
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:构造方法
     * </p>
     *
     * @param page
     * @param from
     * @param rows
     */
    public BasePageVO(int page, int from, int rows) {
        this.page = page;
        this.from = from;
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getFrom() {
        from = (page - 1) * rows;
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
