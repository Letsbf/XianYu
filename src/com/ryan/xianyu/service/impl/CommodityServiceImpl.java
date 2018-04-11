package com.ryan.xianyu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
        commodityVo.setContact(commodity.getContact());
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
        commodityVo.setPublisherName(user.getUsername());

        Integer total = postDao.countReply(commodityId);

        JSONObject jsonObject = (JSONObject) JSON.toJSON(commodityVo);
        jsonObject.put("pages", total / pageSize + 1);
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
    public JSONObject searchCommodity(String search, String institute, String classification, PageInfo pageInfo) {
        // TODO: 2018/4/10 SQL 注入
        logger.error("参数：pageSize:{},pageStart:{},search:{},institute:{},classification:{}",
                pageInfo.getPageSize(), pageInfo.getStart(), search, institute, classification);
        List<Commodity> l = commodityDao.searchCommodity(search, classification, institute, pageInfo);
        logger.error("----------- l长度:{} -----------", l.size());
        return Util.constructResponse(0, "失败", JSONArray.toJSON(l));
    }

    @Override
    public JSONObject getSearchPages(String search, String institute, String classification, Integer pageSize) {
        List<Commodity> l = commodityDao.searchCommodity(search, classification, institute, null);
        if (l == null || l.size() == 0) {
            return Util.constructResponse(0, "失败", "");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pages", l.size() / pageSize + 1);
        return Util.constructResponse(1, "获取分页数成功！", jsonObject);
    }

    @Override
    public Integer getPagesByUserId(Integer userId, Integer pageSize) {
        Integer i = commodityDao.getCommodityCountByUserId(userId);
        logger.error("该用户:{}共发布了:{}个商品", userId, i);
        if (i == null || i == 0) {
            return 0;
        }
        return i / pageSize + 1;
    }

    /**
     * 分页获取"已发布的商品"
     * @param userId 用户ID
     * @param pageInfo 分页信息
     * @return list
     */
    @Override
    public List<CommodityVo> getCommoditiesByUserId(Integer userId, PageInfo pageInfo) {
        List<Commodity> commodityList = commodityDao.getCommoditiesByUserId(userId, pageInfo);
        if (commodityList == null) {
            return null;
        }

        List<CommodityVo> res = convertCommodityList2VoList(commodityList);

        StringBuilder sb = new StringBuilder("");
        for (CommodityVo re : res) {
            sb.append(re.getId() + ",");
        }
        if (sb.length() == 0) {
            return res;
        }
        sb.deleteCharAt(sb.length() - 1);
        String commodityIds = sb.toString();
        Map cId2Rc = postDao.selectReplyByIds(commodityIds);

        logger.error("cId2Rc:{}", cId2Rc);

        for (CommodityVo re : res) {
            re.setReply(((Long) (((Map) cId2Rc.get(re.getId())).get("count(*)"))).intValue());
        }
        return res;
    }

    @Override
    public JSONObject getCommodities(Integer classificationId, PageInfo pageInfo) {
        List<Commodity> s = commodityDao.getCommoditiesByPage(classificationId, pageInfo);
        if (s == null) {
            return Util.constructResponse(0, "获取该分类下商品失败！", "");
        }
        List<CommodityVo> res = convertCommodityList2VoList(s);
        return Util.constructResponse(1, "获取成功", JSON.toJSON(res));
    }

    private List<CommodityVo> convertCommodityList2VoList(List<Commodity> s){
        StringBuilder sb = new StringBuilder();
        for (Commodity commodity : s) {
            sb.append(commodity.getPublisher()).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String publisherIds = sb.toString();

        List<User> ls = userDao.selectByIds(publisherIds);
        Map id2Name = new HashMap<Integer, String>();
        for (User l : ls) {
            id2Name.put(l.getId(), l.getUsername());
        }

        List res = new ArrayList<CommodityVo>();
        for (Commodity commodity : s) {
            CommodityVo commodityVo = convertCommodity2Vo(commodity, id2Name);
            res.add(commodityVo);
        }
        return res;
    }

    private CommodityVo convertCommodity2Vo(Commodity commodity,Map id2Name) {
        CommodityVo commodityVo = new CommodityVo();
        commodityVo.setId(commodity.getId());
        commodityVo.setTitle(commodity.getTitle());
        commodityVo.setContact(commodity.getContact());
        commodityVo.setDescription(commodity.getDescription());
        commodityVo.setImages(commodity.getImages());
        commodityVo.setPrice(commodity.getPrice());
        commodityVo.setStatus(commodity.getStatus());
        commodityVo.setTime(commodity.getTime());
        commodityVo.setPublisherName((String) id2Name.get(commodity.getId()));
        commodityVo.setPublisherId(commodity.getPublisher());
        commodityVo.setBrowse(commodity.getBrowse());
        return commodityVo;
    }
}
