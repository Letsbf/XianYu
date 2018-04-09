package com.ryan.xianyu.dao.impl;

import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.CommodityDao;
import com.ryan.xianyu.domain.Commodity;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommodityDaoImpl implements CommodityDao {


    @Override
    public List getCommoditiesByPage(Integer classficationId, PageInfo pageInfo) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("classficationId", classficationId);
        params.put("pageInfo", pageInfo);
        List s = sqlSession.selectList("commodity.selectByPage", params);
        return s;
    }

    @Override
    public Commodity getCommodityById(Integer id) {
        SqlSession sqlSession = SQLFactory.getSession();
        Commodity commodity = sqlSession.selectOne("commodity.selectById", id);
        return commodity;
    }

    @Override
    public Integer publishCommodity(Commodity commodity) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.insert("commodity.insertCommodity", commodity);
        return i;
    }

    @Override
    public Integer insertImages(Commodity commodity) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.update("commodity.insertImages", commodity);
        return i;
    }

}
