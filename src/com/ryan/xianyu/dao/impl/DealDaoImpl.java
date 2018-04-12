package com.ryan.xianyu.dao.impl;

import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.DealDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DealDaoImpl implements DealDao {

    @Override
    public Integer insertDeal(Integer purchaserId, Integer commodityId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("purchaserId", purchaserId);
        params.put("commodityId", commodityId);
        Integer i = sqlSession.insert("deal.insertDeal", params);
        return i;
    }

    @Override
    public Integer deleteDeal(Integer purchaserId, Integer commodityId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("purchaserId", purchaserId);
        params.put("commodityId", commodityId);
        Integer i = sqlSession.insert("deal.deleteDeal", params);
        return i;
    }
}

