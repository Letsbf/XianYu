package com.ryan.xianyu.dao;

import com.ryan.xianyu.domain.Notice;

import java.util.List;

public interface NoticeDao {

    Integer publishNotice(String title, String text, Integer publisherId);

    List<Notice> getSixNotice();

    Integer deleteNotice(Integer noticeId);
}

