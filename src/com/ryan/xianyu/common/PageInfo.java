package com.ryan.xianyu.common;

public class PageInfo {

    private Integer start;
    private Integer pageSize;
    private Integer total;

    public PageInfo(Integer start, Integer pageSize, Integer total) {
        this.start = start;
        this.pageSize = pageSize;
        this.total = total;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
