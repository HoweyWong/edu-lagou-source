package com.lagou.edu.comment.api.param;

import java.io.Serializable;

public class PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageNo = 1;
    private Integer pageSize = 15;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            this.pageSize = 15;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getLimitStart() {
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        return (pageNo - 1) * pageSize;
    }

}
