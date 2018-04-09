package com.ryan.xianyu.controller;

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
                                    @RequestParam("start")Integer start,
                                    @RequestParam("pageSize") Integer pageSize,
                                    @RequestParam("total") Integer total) {
        if (classificationId < 0) {
            return Util.constructResponse(0, "分类id不正确", "");
        }
        PageInfo pageInfo = new PageInfo(start, pageSize, total);
        return commodityService.getCommodities(classificationId, pageInfo);
    }

    @GetMapping("/detail")
    @ResponseBody
    public JSONObject getCommodityById(@RequestParam("id") Integer id, @RequestParam("pageSize") Integer pageSize) {
        if (id <= 0) {
            return Util.constructResponse(0, "id不正确", "");
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        return commodityService.getCommodityById(id, pageSize);
    }

    @GetMapping("/publish")
    @ResponseBody
    public JSONObject publishCommodity(@RequestBody Commodity commodity) {
        if (Util.isEmpty(commodity.getTitle())) {
            return Util.constructResponse(0, "标题不能为空", "");
        }
        if (Util.isEmpty(commodity.getContacter())) {
            return Util.constructResponse(0, "联系方式不能为空", "");
        }
        if (Util.isEmpty(commodity.getDescription())) {
            return Util.constructResponse(0, "描述信息不能为空", "");
        }
        if (Util.isEmpty(commodity.getImages())) {
            return Util.constructResponse(0, "至少要上传一张图片", 0);
        }
        if (commodity.getClassification() == null || commodity.getClassification() <= 0) {
            return Util.constructResponse(0, "商品类别不能为空", "");
        }
        if (commodity.getPrice() == null || commodity.getPrice() <= 0) {
            return Util.constructResponse(0, "商品价格不能为空", "");
        }
        if (commodity.getPublisher() == null || commodity.getPublisher() <= 0) {
            return Util.constructResponse(0, "用户状态错误", "");
        }
        return commodityService.publishCommodity(commodity);

    }

}
