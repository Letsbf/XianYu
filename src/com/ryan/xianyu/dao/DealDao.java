package com.ryan.xianyu.dao;

import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.domain.Deal;

import java.util.List;

public interface DealDao {

    Integer insertDeal(Integer purchaserId, Integer commodityId);

    Integer deleteDeal(Integer purchaserId, Integer commodityId);

    List<Deal> getDealsByUserId(Integer userId);

    List<Deal> getDealsByUserIdByPage(Integer userId, PageInfo pageInfo);
}
