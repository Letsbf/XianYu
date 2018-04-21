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
    public List getCommoditiesByPage(Integer classificationId, PageInfo pageInfo) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("classificationId", classificationId);
        params.put("pageInfo", pageInfo);
        List s = sqlSession.selectList("commodity.selectByPage", params);
        sqlSession.close();
        return s;
    }

    @Override
    public Commodity getCommodityById(Integer id) {
        SqlSession sqlSession = SQLFactory.getSession();
        Commodity commodity = sqlSession.selectOne("commodity.selectById", id);
        sqlSession.close();
        return commodity;
    }

    @Override
    public Integer publishCommodity(Commodity commodity) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.insert("commodity.insertCommodity", commodity);
        sqlSession.close();
        return i;
    }

    @Override
    public Integer insertImages(Commodity commodity) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.update("commodity.insertImages", commodity);
        sqlSession.close();
        return i;
    }

    @Override
    public List searchCommodity(String search, List classification, List instituteId, PageInfo pageInfo) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("classification", classification);
        params.put("instituteId", instituteId);
        params.put("pageInfo", pageInfo);
        List<Commodity> l = sqlSession.selectList("commodity.searchCommodity", params);
        sqlSession.close();
        return l;
    }

    @Override
    public Integer getCommodityCountByUserId(Integer userId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.selectOne("commodity.getCommodityCountByUserId", userId);
        sqlSession.close();
        return i;
    }

    @Override
    public List<Commodity> getCommoditiesByUserId(Integer userId, PageInfo pageInfo) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("pageInfo", pageInfo);
        List<Commodity> l = sqlSession.selectList("commodity.getCommoditiesByUserId", params);
        sqlSession.close();
        return l;
    }

    @Override
    public Integer updateCommodityStatus(Integer commodityId, Integer status) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("commodityId", commodityId);
        params.put("status", status);
        Integer i = sqlSession.update("commodity.updateCommodityStatus", params);
        sqlSession.close();
        return i;
    }

    @Override
    public Integer addBrowse(Integer commodityId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.update("commodity.addBrowse", commodityId);
        sqlSession.close();
        return i;
    }

    @Override
    public Integer modifyCommodity(Commodity commodity) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.update("commodity.modifyCommodity", commodity);
        sqlSession.close();
        return i;
    }

    @Override
    public Integer deleteCommodity(Integer commodityId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.delete("commodity.deleteCommodity", commodityId);
        sqlSession.close();
        return i;
    }

    @Override
    public List<Commodity> getCommoditiesByIds(List commodityList) {
        SqlSession sqlSession = SQLFactory.getSession();
        List<Commodity> l = sqlSession.selectList("commodity.getCommoditiesByIds", commodityList);
        sqlSession.close();
        return l;
    }

    @Override
    public List<Commodity> getCommoditiesByIdList(List commodityIdList) {
        SqlSession sqlSession = SQLFactory.getSession();
        List<Commodity> l = sqlSession.selectList("commodity.getCommoditiesByIdList", commodityIdList);
        sqlSession.close();
        return l;
    }

    @Override
    public Integer countCommodityByClassification(Integer classificationId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.selectOne("commodity.countCommodityByClassification", classificationId);
        sqlSession.close();
        return i;
    }

}
