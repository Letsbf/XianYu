package com.ryan.xianyu.dao;

public interface DealDao {

    Integer insertDeal(Integer purchaserId, Integer commodityId);

    Integer deleteDeal(Integer purchaserId, Integer commodityId);
}
