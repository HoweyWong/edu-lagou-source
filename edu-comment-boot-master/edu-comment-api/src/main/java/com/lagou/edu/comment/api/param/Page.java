package com.lagou.edu.comment.api.param;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // 传递的参数或是配置的值
    private int currentPage; // 当前页
    private int pageSize; // 每页显示的数量

    // 查询数据库
    private List<T> recordList; // 本页的数据列表
    private int recordCount; // 总记录数
    // 计算
    private int pageCount; // 总页数
    private int beginPageIndex; // 页码列表的开始索引
    private int endPageIndex; // 页码列表的结束索引

    private int lastPage;//上一页
    private int nextPage;//下一页

    /**
     * 只需要接受必要的4个参数，会自动的计算出其他3个属性的值
     *
     * @param currentPage
     * @param pageSize
     * @param recordList
     * @param recordCount
     */

    public Page(int currentPage, int pageSize, List<T> recordList, int recordCount) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.recordList = recordList;
        this.recordCount = recordCount;
        // 计算pageCount
        pageCount = (recordCount + pageSize - 1) / pageSize;

        // 计算beginPageIndex与endPageIndex
        // 一、总页数小于等于10页，就全部显示
        if (pageCount <= 10) {
            beginPageIndex = 1;
            endPageIndex = pageCount;
        }
        // 二、总页数大于10页
        else {
            // 显示当前页附近的共10个页码（前4页 + 当前页 + 后5页）
            beginPageIndex = currentPage - 4; // 3
            endPageIndex = currentPage + 5; // 12
            // 如果前面不足4个页码，就显示前10页
            if (beginPageIndex < 1) {
                beginPageIndex = 1;
                endPageIndex = 10;
            }
            // 如果后面不足5个页码，就显示后10页
            if (endPageIndex > pageCount) {
                endPageIndex = pageCount;
                beginPageIndex = pageCount - 10 + 1; // 显示时是包含两个边界值的
            }
        }
        if (currentPage > 1) {
            lastPage = currentPage - 1;
        }
        if (currentPage < pageCount) {
            nextPage = currentPage + 1;
        }
    }

    public Page() {
        super();
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getBeginPageIndex() {
        return beginPageIndex;
    }

    public void setBeginPageIndex(int beginPageIndex) {
        this.beginPageIndex = beginPageIndex;
    }

    public int getEndPageIndex() {
        return endPageIndex;
    }

    public void setEndPageIndex(int endPageIndex) {
        this.endPageIndex = endPageIndex;
    }


    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    @Override
    public String toString() {
        return "Page [currentPage=" + currentPage + ", pageSize=" + pageSize
            + ", recordList=" + recordList + ", recordCount=" + recordCount
            + ", pageCount=" + pageCount + ", beginPageIndex="
            + beginPageIndex + ", endPageIndex=" + endPageIndex + "]";
    }

}
