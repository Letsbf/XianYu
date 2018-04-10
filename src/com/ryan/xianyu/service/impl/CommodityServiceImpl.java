package com.ryan.xianyu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.dao.CommodityDao;
import com.ryan.xianyu.dao.PostDao;
import com.ryan.xianyu.dao.UserDao;
import com.ryan.xianyu.domain.Commodity;
import com.ryan.xianyu.domain.User;
import com.ryan.xianyu.service.CommodityService;
import com.ryan.xianyu.vo.CommodityVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommodityServiceImpl implements CommodityService {

    private static Logger logger = LoggerFactory.getLogger(CommodityServiceImpl.class);

    @Autowired
    private CommodityDao commodityDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Override
    public JSONObject getCommodityById(Integer commodityId, Integer pageSize) {
        Commodity commodity = commodityDao.getCommodityById(commodityId);
        if (commodity == null) {
            return Util.constructResponse(0, "获取商品详情失败！", "");
        }
        User user = userDao.selectById(commodity.getPublisher());
        CommodityVo commodityVo = new CommodityVo();

        commodityVo.setTitle(commodity.getTitle());
        commodityVo.setContacter(commodity.getContacter());
        commodityVo.setDescription(commodity.getDescription());
        try {
            commodityVo.setImages(Util.readImages(commodity.getImages()));
        } catch (Exception e) {
            logger.error("读取图片失败！", e);
            commodityVo.setImages("");
        }
        commodityVo.setPrice(commodity.getPrice());
        commodityVo.setStatus(commodity.getStatus());
        commodityVo.setTime(commodity.getTime());
        commodityVo.setPublisher(user.getUsername());

        Integer total = postDao.countReply(commodityId);

        JSONObject jsonObject = (JSONObject) JSON.toJSON(commodityVo);
        jsonObject.put("pages", total / pageSize);
        return Util.constructResponse(1, "获取商品详情成功", jsonObject);
    }

    @Override
    public JSONObject publishCommodity(Commodity commodity) {
        // TODO: 2018/4/8 发布人id校验
        String images = commodity.getImages();
        Integer id = commodityDao.publishCommodity(commodity);
        if (id <= 0) {
            return Util.constructResponse(0, "发布失败！", "");
        }
        commodity.setId(id);
        try {
            Util.saveImages(commodity);
        } catch (Exception e) {
            logger.error("保存图片时失败出现异常", e);
        }
        Integer i = commodityDao.insertImages(commodity);
        if (i <= 0) {
            return Util.constructResponse(0, "发布失败！", "");
        }
        return Util.constructResponse(1, "发布成功！", "");
    }

    @Override
    public JSONObject searchCommodity(String search, String institute, String classification) {
        // TODO: 2018/4/10 SQL 注入
        // TODO: 2018/4/10 分页
//        if (Util.isEmpty(search)) {
//            search = "";
//        }
//        if (Util.isEmpty(institute)) {
//            if (Util.isEmpty(classification)) {
//
//            } else {
//
//            }
//        }
        List<Commodity> l = commodityDao.searchCommodity(search, classification, institute);
        logger.error("----------- l长度:{} -----------", l.size());
        // TODO: 2018/4/10 搜索结果相关度排序
        return Util.constructResponse(0, "失败", "");
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
