package com.ryan.xianyu.vo;

import java.util.List;

public class ClassificationVo {

    private Integer id;
    private String name;
    private Integer refer;
    private List images;
    private List commodityIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRefer() {
        return refer;
    }

    public void setRefer(Integer refer) {
        this.refer = refer;
    }

    public List getImages() {
        return images;
    }

    public void setImages(List images) {
        this.images = images;
    }

    public List getCommodityIds() {
        return commodityIds;
    }

    public void setCommodityIds(List commodityIds) {
        this.commodityIds = commodityIds;
    }
}
