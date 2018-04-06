package com.ryan.xianyu.dao;

import com.ryan.xianyu.domain.Classification;
import com.ryan.xianyu.domain.Notice;

import java.util.List;
import java.util.Map;

public interface IndexDao {


    Map<Integer, String> getInstitute();


    List<Notice> getNotices();

    Notice getNotice(int id);

    List<Classification> getClassification();

}
