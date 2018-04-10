package com.ryan.xianyu.dao;

import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.domain.Commodity;

import java.util.List;

public interface CommodityDao {

    List getCommoditiesByPage(Integer id, PageInfo pageInfo);

    Commodity getCommodityById(Integer id);

    Integer publishCommodity(Commodity commodity);

    Integer insertImages(Commodity commodity);

    List searchCommodity(String search, String classification, String instituteId, PageInfo pageInfo);

    Integer getCommodityCountByUserId(Integer userId);

    List<Commodity> getCommoditiesByUserId(Integer userId, PageInfo pageInfo);
}
