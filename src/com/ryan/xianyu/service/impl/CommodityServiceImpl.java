package com.ryan.xianyu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.dao.CommodityDao;
import com.ryan.xianyu.dao.DealDao;
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

    @Autowired
    private DealDao dealDao;

    @Override
    public JSONObject getCommodityById(Integer commodityId, Integer pageSize) {
        Commodity commodity = commodityDao.getCommodityById(commodityId);
        if (commodity == null) {
            return Util.constructResponse(0, "获取商品详情失败！", "");
        }
        User user = userDao.selectById(commodity.getPublisher());
        CommodityVo commodityVo = new CommodityVo();

        commodityVo.setPublisherIsAdmin(user.isAdmin() ? 1 : 0);
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
        commodityVo.setBrowse(commodity.getBrowse());
        commodityVo.setPublisherId(user.getId());

        commodityDao.addBrowse(commodityId);

        Integer total = postDao.countReply(commodityId);

        JSONObject jsonObject = (JSONObject) JSON.toJSON(commodityVo);
        jsonObject.put("replyPages", total / pageSize + 1);
        return Util.constructResponse(1, "获取商品详情成功", jsonObject);
    }

    @Override
    public JSONObject publishCommodity(Commodity commodity) {
        Integer userId = commodity.getPublisher();
        User user = userDao.selectById(userId);
        if (user == null) {
            return Util.constructResponse(0, "用户不存在", "");
        }
        Integer s = commodityDao.publishCommodity(commodity);
        if (s <= 0) {
            return Util.constructResponse(0, "发布失败！", "");
        }
        logger.error("last insert id:{}------", commodity.getId());
        try {
            Util.saveImages(commodity);
        } catch (Exception e) {
            logger.error("保存图片时失败出现异常", e);
        }
        Integer i = commodityDao.insertImages(commodity);
        if (i <= 0) {
            return Util.constructResponse(0, "发布失败！", "");
        }
        JSONObject data = new JSONObject();
        data.put("id", commodity.getId());
        return Util.constructResponse(1, "发布成功！", data);
    }

    @Override
    public JSONObject searchCommodity(String search, String institute, String classification, PageInfo pageInfo) {
        logger.error("参数：pageSize:{},pageStart:{},search:{},institute:{},classification:{}",
                pageInfo.getPageSize(), pageInfo.getStart(), search, institute, classification);

        List classList = this.convertString2IntList(classification);
        List instituteList = this.convertString2IntList(institute);

        List<Commodity> l = commodityDao.searchCommodity(search, classList, instituteList, pageInfo);
        for (Commodity commodity : l) {
            try {
                commodity.setImages(Util.readImages(commodity.getImages()));
            } catch (Exception e) {
                logger.error("读取图片异常", e);
            }
        }
        logger.error("----------- l长度:{} -----------", l.size());
        return Util.constructResponse(1, "搜索成功", JSONArray.toJSON(l));
    }

    @Override
    public JSONObject getSearchPages(String search, String institute, String classification, Integer pageSize) {

        List classList = this.convertString2IntList(classification);
        List instituteList = this.convertString2IntList(institute);

        List<Commodity> l = commodityDao.searchCommodity(search, classList, instituteList, null);
        if (l == null || l.size() == 0) {
            return Util.constructResponse(0, "失败", "");
        }
        JSONObject jsonObject = new JSONObject();
        int pages = l.size() / pageSize;
        if (pages * pageSize != l.size()) {
            pages++;
        }
        jsonObject.put("pages", pages);
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
        if (commodityList.size() == 0) {
            return new ArrayList<CommodityVo>();
        }

        List<CommodityVo> res = convertCommodityList2VoList(commodityList);

        List commodityIds = new ArrayList();
        for (CommodityVo re : res) {
            commodityIds.add(re.getId());
        }
        if (commodityIds.size() == 0) {
            return res;
        }
        //各个帖子的回复总数
        Map cId2Rc = postDao.selectReplyByIds(commodityIds);

        if (cId2Rc == null || cId2Rc.size() == 0) {
            for (CommodityVo re : res) {
                re.setReply(0);
            }
        } else {
            for (CommodityVo re : res) {
                if (cId2Rc.containsKey(re.getId())) {
                    re.setReply(((Long) (((Map) cId2Rc.get(re.getId())).get("count(*)"))).intValue());
                } else {
                    re.setReply(0);
                }
            }
        }


        return res;
    }

    @Override
    public JSONObject purchaseCommodity(Integer purchaserId, Integer commodityId) {
        Commodity commodity = commodityDao.getCommodityById(commodityId);
        if (commodity == null || commodity.getStatus() != 0) {
            return Util.constructResponse(0, "商品不可购买！", "");
        }
        if (commodity.getPublisher() == purchaserId) {
            return Util.constructResponse(0, "不能购买自己的商品", "");
        }
        User user = userDao.selectById(purchaserId);
        if (user == null) {
            return Util.constructResponse(0, "用户id错误,无法购买", "");
        }
        Integer i = dealDao.insertDeal(purchaserId, commodityId);
        if (i <= 0) {
            return Util.constructResponse(0, "购买失败！", "");
        }
        Integer s = commodityDao.updateCommodityStatus(commodityId, 1);
        if (s <= 0) {
            dealDao.deleteDeal(purchaserId, commodityId);
        }
        return Util.constructResponse(1, "够买成功", "");
    }

    @Override
    public JSONObject modifyCommodity(Commodity commodity) {
        if (!validateUserAndCommodity(commodity.getId(), commodity.getPublisher())) {
            return Util.constructResponse(0, "非本人修改或修改人非管理员,修改失败!", "");
        }
        Integer commodityId = commodity.getId();
        Commodity commodity1 = commodityDao.getCommodityById(commodityId);
        commodity.setPublisher(commodity1.getPublisher());

        Util.deleteImages(commodity.getPublisher(), commodityId);

        try {
            Util.saveImages(commodity);
        } catch (Exception e) {
            logger.error("保存图片异常", e);
            return Util.constructResponse(0, "更新图片失败！", "");
        }

        Integer res = commodityDao.modifyCommodity(commodity);
        if (res < 0) {
            return Util.constructResponse(0, "修改商品信息失败！", "");
        }

        return Util.constructResponse(1, "修改成功！", "");
    }

    @Override
    public JSONObject deleteCommodity(Integer commodityId, Integer userId) {
        if (!validateUserAndCommodity(commodityId, userId)) {
            return Util.constructResponse(0, "您无权限删除此商品或商品不存在！", "");
        }
        Commodity commodity = commodityDao.getCommodityById(commodityId);
        Integer res = commodityDao.deleteCommodity(commodityId);
        if (res <= 0) {
            return Util.constructResponse(0, "删除失败！", "");
        }
        Util.deleteImages(commodity.getPublisher(), commodity.getId());
        return Util.constructResponse(1, "删除成功", "");
    }

    @Override
    public JSONObject getCommodityClassPages(Integer classificationId, Integer pageSize) {
        Integer res = commodityDao.countCommodityByClassification(classificationId);
        if (res <= 0) {
            return Util.constructResponse(0, "该分类下没有商品哦", "");
        }
        JSONObject data = new JSONObject();
        int pages = res / pageSize;
        if (pages * pageSize != res) {
            pages++;
        }
        data.put("pages", pages);
        return Util.constructResponse(1, "获取分类商品页数成功", data);
    }

    @Override
    public JSONObject getCommodities(Integer classificationId, PageInfo pageInfo) {
        List<Commodity> s = commodityDao.getCommoditiesByPage(classificationId, pageInfo);
        if (s == null || s.size() == 0) {
            return Util.constructResponse(1, "该分类下尚无商品", "");
        }
        List<CommodityVo> res = convertCommodityList2VoList(s);
        return Util.constructResponse(1, "获取成功", JSON.toJSON(res));
    }

    // TODO: 2018/4/16 验证一下
    private List<CommodityVo> convertCommodityList2VoList(List<Commodity> s){
        List<Integer> idList = new ArrayList<>();
        for (Commodity commodity : s) {
            idList.add(commodity.getPublisher());
        }

        List<User> ls = userDao.selectByIds(idList);
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

    private boolean validateUserAndCommodity(Integer commodityId, Integer userId) {
        Commodity commodity = commodityDao.getCommodityById(commodityId);
        if (commodity == null) {
            return false;
        }
        User user = userDao.selectById(userId);
        if (user == null || !user.getId().equals(commodity.getPublisher())) {
            if (user.isAdmin()) {
                return true;
            }
            return false;
        }
        return true;
    }

    private CommodityVo convertCommodity2Vo(Commodity commodity,Map id2Name) {
        CommodityVo commodityVo = new CommodityVo();
        commodityVo.setId(commodity.getId());
        commodityVo.setTitle(commodity.getTitle());
        commodityVo.setContact(commodity.getContact());
        commodityVo.setDescription(commodity.getDescription());
        try {
            commodityVo.setImages(Util.readImages(commodity.getImages()));
        } catch (Exception e) {
            logger.error("获取商品图片失败,商品id:{}", commodity.getId(), e);
        }
        commodityVo.setPrice(commodity.getPrice());
        commodityVo.setStatus(commodity.getStatus());
        commodityVo.setTime(commodity.getTime());
        commodityVo.setPublisherName((String) id2Name.get(commodity.getPublisher()));
        commodityVo.setPublisherId(commodity.getPublisher());
        commodityVo.setBrowse(commodity.getBrowse());
        return commodityVo;
    }

    private List convertString2IntList(String str) {
        if (Util.isEmpty(str)) {
            return null;
        }
        String[] strings = str.split(",");
        List integers = new ArrayList<Integer>();
        for (String s : strings) {
            integers.add(Integer.parseInt(s));
        }
        return integers;
    }

}
