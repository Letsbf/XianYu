package com.ryan.xianyu.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.Util;
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
    public JSONObject getCommodityById(@RequestParam("id") Integer id) {
        if (id <= 0) {
            return Util.constructResponse(0, "id不正确", "");
        }
        return commodityService.getCommodityById(id);
    }

}
