package com.ryan.xianyu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.domain.Commodity;
import com.ryan.xianyu.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @PostMapping("/class")
    @ResponseBody
    public JSONObject getCommdities(@RequestParam("classificationId") Integer classificationId,
                                    @RequestParam("start") Integer start,
                                    @RequestParam("pageSize") Integer pageSize,
                                    @RequestParam("total") Integer total) {
        if (classificationId < 0) {
            return Util.constructResponse(0, "分类id不正确", "");
        }
        PageInfo pageInfo = new PageInfo(start, pageSize, total);
        return commodityService.getCommodities(classificationId, pageInfo);
    }

    @PostMapping("/detail")
    @ResponseBody
    public JSONObject getCommodityById(@RequestParam("id") Integer commodityId, @RequestParam("pageSize") Integer pageSize) {
        if (commodityId <= 0) {
            return Util.constructResponse(0, "id不正确", "");
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        return commodityService.getCommodityById(commodityId, pageSize);
    }

    /**
     * 发布一个商品
     * @param title 标题
     * @param contact 联系方式
     * @param description 描述
     * @param images 图片 base64编码 英文分号隔开
     * @param classification 分类对应的id
     * @param price 价格 数字
     * @param publisherId 发布人id 数字
     * @return json
     */
    @PostMapping("/publish")
    @ResponseBody
    public JSONObject publishCommodity(@RequestParam("title") String title, @RequestParam("contact") String contact,
                                       @RequestParam("description") String description, @RequestParam("images") String images,
                                       @RequestParam("classification") Integer classification, @RequestParam("price") Float price,
                                       @RequestParam("publisherId") Integer publisherId) {
        if (Util.isEmpty(title)) {
            return Util.constructResponse(0, "标题不能为空", "");
        }
        if (Util.isEmpty(contact)) {
            return Util.constructResponse(0, "联系方式不能为空", "");
        }
        if (Util.isEmpty(description)) {
            return Util.constructResponse(0, "描述信息不能为空", "");
        }
        if (Util.isEmpty(images)) {
            return Util.constructResponse(0, "至少要上传一张图片", 0);
        }
        if (classification == null || classification <= 0) {
            return Util.constructResponse(0, "商品类别不能为空", "");
        }
        if (price == null || price <= 0) {
            return Util.constructResponse(0, "商品价格不能为空", "");
        }
        if (publisherId == null || publisherId <= 0) {
            return Util.constructResponse(0, "用户状态错误", "");
        }
        Commodity commodity = new Commodity(title, price, classification, publisherId, description, images, contact);
        return commodityService.publishCommodity(commodity);
    }

    @PostMapping("/modify")
    @ResponseBody
    public JSONObject modifyCommodity(@RequestParam("commodityId") Integer commodityId,
                                      @RequestParam("title") String title, @RequestParam("contact") String contact,
                                      @RequestParam("description") String description, @RequestParam("images") String images,
                                      @RequestParam("classification") Integer classification, @RequestParam("price") Float price,
                                      @RequestParam("publisherId") Integer publisherId) {
        if (commodityId == null || commodityId <= 0) {
            return Util.constructResponse(0, "商品ID不能为空", "");
        }
        if (Util.isEmpty(title)) {
            return Util.constructResponse(0, "标题不能为空", "");
        }
        if (Util.isEmpty(contact)) {
            return Util.constructResponse(0, "联系方式不能为空", "");
        }
        if (Util.isEmpty(description)) {
            return Util.constructResponse(0, "描述信息不能为空", "");
        }
        if (Util.isEmpty(images)) {
            return Util.constructResponse(0, "至少要上传一张图片", 0);
        }
        if (classification == null || classification <= 0) {
            return Util.constructResponse(0, "商品类别不能为空", "");
        }
        if (price == null || price <= 0) {
            return Util.constructResponse(0, "商品价格不能为空", "");
        }
        if (publisherId == null || publisherId <= 0) {
            return Util.constructResponse(0, "用户状态错误", "");
        }
        Commodity commodity = new Commodity(commodityId, title, price, classification, publisherId, description, images, contact);
        return commodityService.modifyCommodity(commodity);
    }

    /**
     * 购买商品
     * @param purchaserId 购买人ID
     * @param commodityId 商品ID
     * @return json
     */
    @PostMapping("/purchase")
    @ResponseBody
    public JSONObject buyCommodity(@RequestParam("purchaserId") Integer purchaserId,
                                   @RequestParam("commodityId") Integer commodityId) {
        //先检查商品状态
        if (purchaserId <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }
        if (commodityId <= 0) {
            return Util.constructResponse(0, "商品id不正确", "");
        }
        return commodityService.purchaseCommodity(purchaserId, commodityId);

    }


}
