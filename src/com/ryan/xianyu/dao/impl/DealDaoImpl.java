package com.ryan.xianyu.dao.impl;

import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.DealDao;
import com.ryan.xianyu.domain.Deal;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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

    @Override
    public List<Deal> getDealsByUserId(Integer userId) {
        SqlSession sqlSession = SQLFactory.getSession();
        List<Deal> dealList = sqlSession.selectList("deal.getDealsByUserId", userId);
        return dealList;
    }

    @Override
    public List<Deal> getDealsByUserIdByPage(Integer userId, PageInfo pageInfo) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("pageInfo", pageInfo);
        List<Deal> dealList = sqlSession.selectList("deal.getDealsByUserIdByPage", params);
        return dealList;
    }

    @Override
    public List<Deal> getDealsByTime(Long start, Long end, Integer userId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("start", start);
        params.put("end", end);
        params.put("userId", userId);
        List<Deal> dealList = sqlSession.selectList("deal.getDealsByTime", params);
        return dealList;
    }

}

