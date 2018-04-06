package com.ryan.xianyu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.dao.CommodityDao;
import com.ryan.xianyu.dao.UserDao;
import com.ryan.xianyu.domain.Commodity;
import com.ryan.xianyu.domain.User;
import com.ryan.xianyu.service.CommodityService;
import com.ryan.xianyu.vo.CommodityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    private CommodityDao commodityDao;

    @Autowired
    private UserDao userDao;

    @Override
    public JSONObject getCommodityById(Integer id) {
        Commodity commodity = commodityDao.getCommodityById(id);
        if (commodity == null) {
            return Util.constructResponse(0, "获取商品详情失败！", "");
        }
        User user = userDao.selectById(commodity.getPublisher());
        CommodityVo commodityVo = new CommodityVo();

        commodityVo.setTitle(commodity.getTitle());
        commodityVo.setContacter(commodity.getContacter());
        commodityVo.setDescription(commodity.getDescription());
        commodityVo.setImages(commodity.getImages());
        commodityVo.setPrice(commodity.getPrice());
        commodityVo.setStatus(commodity.getStatus());
        commodityVo.setTime(commodity.getTime());
        commodityVo.setPublisher(user.getUsername());

        return Util.constructResponse(1, "获取商品详情成功", JSON.toJSON(commodityVo));
    }

    @Override
    public JSONObject getCommodities(Integer classificationId, PageInfo pageInfo) {
        List<Commodity> s = commodityDao.getCommoditiesByPage(classificationId, pageInfo);
        if (s == null) {
            return Util.constructResponse(0, "获取该分类下商品失败！", "");
        }
        StringBuilder sb = new StringBuilder();
        for (Commodity commodity : s) {
            sb.append(commodity.getPublisher()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        String publisherIds = sb.toString();

        List<User> ls = userDao.selectByIds(publisherIds);
        Map id2Name = new HashMap<Integer, String>();
        for (User l : ls) {
            id2Name.put(l.getId(), l.getUsername());
        }

        List res = new ArrayList<CommodityVo>();
        for (Commodity commodity : s) {
            CommodityVo commodityVo = new CommodityVo();
            commodityVo.setTitle(commodity.getTitle());
            commodityVo.setContacter(commodity.getContacter());
            commodityVo.setDescription(commodity.getDescription());
            commodityVo.setImages(commodity.getImages());
            commodityVo.setPrice(commodity.getPrice());
            commodityVo.setStatus(commodity.getStatus());
            commodityVo.setTime(commodity.getTime());
            commodityVo.setPublisher((String) id2Name.get(commodity.getId()));
            res.add(commodityVo);
        }
        return Util.constructResponse(1, "获取成功", JSON.toJSON(res));
    }
}
