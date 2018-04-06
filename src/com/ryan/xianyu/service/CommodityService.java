package com.ryan.xianyu.service;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;

public interface CommodityService {

    JSONObject getCommodities(Integer classificationId, PageInfo pageInfo);

    JSONObject getCommodityById(Integer id);
}
