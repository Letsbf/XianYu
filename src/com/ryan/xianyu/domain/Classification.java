package com.ryan.xianyu.domain;

public class Classification {

    private Integer id;
    private String name;
    private Integer refer;

    public Classification() {
    }

    public Classification(Integer id, String name, Integer refer) {
        this.id = id;
        this.name = name;
        this.refer = refer;
    }

    public Classification(String name, Integer refer) {
        this.name = name;
        this.refer = refer;
    }

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
}
