package com.ryan.xianyu.dao;

public interface NoticeDao {

    Integer publishNotice(String title, String text, Integer publisherId);

}
