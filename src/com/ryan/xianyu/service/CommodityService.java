package com.ryan.xianyu.service;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.domain.Commodity;

public interface CommodityService {

    JSONObject getCommodities(Integer classificationId, PageInfo pageInfo);

    JSONObject getCommodityById(Integer id, Integer pageSize);

    JSONObject publishCommodity(Commodity commodity);

    JSONObject searchCommodity(String search, String institute, String classification);
}
