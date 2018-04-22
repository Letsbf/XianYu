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

    Integer addInstitute(String name);

    Integer deleteInstitute(Integer instituteId);

    Integer renameInstitute(Integer instituteId, String newName);

    Integer addClassification(Classification classification);

    Integer deleteClassification(Integer classificationId);

    Integer renameClassification(Integer classification, String newName);

    Classification selectClassificationById(Integer classificationId);

}
