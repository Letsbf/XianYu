package com.ryan.xianyu.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface IndexService {

    Map getInstitute();

    JSONObject getNotice(int id);

    JSONObject getClassification();

}
