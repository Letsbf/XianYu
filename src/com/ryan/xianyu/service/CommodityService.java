package com.ryan.xianyu.service;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.domain.Commodity;
import com.ryan.xianyu.vo.CommodityVo;

import java.util.List;

public interface CommodityService {

    JSONObject getCommodities(Integer classificationId, PageInfo pageInfo);

    JSONObject getCommodityById(Integer commodityId, Integer pageSize);

    JSONObject publishCommodity(Commodity commodity);

    JSONObject searchCommodity(String search, String institute, String classification, PageInfo pageInfo);

    JSONObject getSearchPages(String search, String institute, String classification, Integer pageSize);

    Integer getPagesByUserId(Integer userId, Integer pageSize);

    /**
     * 分页获取"已发布的商品"
     * @param userId 用户ID
     * @param pageInfo 分页信息
     * @return list
     */
    List<CommodityVo> getCommoditiesByUserId(Integer userId, PageInfo pageInfo);

    JSONObject purchaseCommodity(Integer purchaserId, Integer commodityId);
}
