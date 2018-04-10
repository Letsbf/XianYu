package com.ryan.xianyu.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.service.CommodityService;
import com.ryan.xianyu.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private CommodityService commodityService;

    @GetMapping("/institute")
    @ResponseBody
    public JSONObject getInstitute() {
        Map m = indexService.getInstitute();
        if (m == null) {
            return Util.constructResponse(0, "获取学院信息失败！", "");
        }
        return Util.constructResponse(1, "获取学院信息成功！", m);
    }


    @PostMapping("/notice")
    @ResponseBody
    public JSONObject getNotice(@RequestParam("id") Integer id) {
        return indexService.getNotice(id);
    }


    @GetMapping("/classification")
    @ResponseBody
    public JSONObject getClassification(){
        return indexService.getClassification();
    }


    @GetMapping("/search")
    @ResponseBody
    public JSONObject search(@RequestParam("search") String search,
                             @RequestParam("institute") String institute,
                             @RequestParam("classification") String classification,
                             @RequestParam("pageStart") Integer pageStart,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("total") Integer total) {
//        if (Util.isEmpty(search)) {
//            search = "";
//        }
//        if (Util.isEmpty(institute)) {
//            institute = "";
//        }
//        if (Util.isEmpty(classification)) {
//            classification = "";
//        }
        if (pageStart < 0) {
            pageStart = 0;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        PageInfo pageInfo = new PageInfo(pageStart, pageSize, total);
        return commodityService.searchCommodity(search, institute, classification, pageInfo);
    }

    @GetMapping("/getSearchPages")
    @ResponseBody
    public JSONObject getSearchPages(@RequestParam("search") String search,
                                     @RequestParam("institute") String institute,
                                     @RequestParam("classification") String classification,
                                     @RequestParam("pageSize") Integer pageSize) {
        return commodityService.getSearchPages(search, institute, classification, pageSize);
    }

    public static void main(String[] args) {
        String[] s = "".split(";");
        System.out.println("te" + s[0] + "st");
    }



}
