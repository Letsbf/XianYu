package com.ryan.xianyu.dao;

import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.domain.Commodity;

import java.util.List;

public interface CommodityDao {

    //分类
    List getCommoditiesByPage(Integer id, PageInfo pageInfo);

    Commodity getCommodityById(Integer id);

    Integer publishCommodity(Commodity commodity);

    Integer insertImages(Commodity commodity);

    List searchCommodity(String search, List classification, List instituteId, PageInfo pageInfo);

    Integer getCommodityCountByUserId(Integer userId);

    List<Commodity> getCommoditiesByUserId(Integer userId, PageInfo pageInfo);

    Integer updateCommodityStatus(Integer commodityId, Integer status);

    Integer addBrowse(Integer commodityId);

    Integer modifyCommodity(Commodity commodity);

    Integer deleteCommodity(Integer commodityId);

    List<Commodity> getCommoditiesByIds(String commodityStr);

    List<Commodity> getCommoditiesByIdList(List commodityIdList);

    Integer countCommodityByClassification(Integer classificationId);


}
