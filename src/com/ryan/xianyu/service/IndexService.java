package com.ryan.xianyu.service;

import com.alibaba.fastjson.JSONObject;

public interface IndexService {

    JSONObject getInstitute();

    JSONObject getNotice(int id);

    JSONObject getClassification();

}
