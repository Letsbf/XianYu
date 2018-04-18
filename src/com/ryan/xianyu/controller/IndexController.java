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


    /**
     * 获取学院信息
     * @return json,key学院的id,value学院中文名
     */
    @GetMapping("/institute")
    @ResponseBody
    public JSONObject getInstitute() {
        Map m = indexService.getInstitute();
        if (m == null) {
            return Util.constructResponse(0, "获取学院信息失败！", "");
        }
        return Util.constructResponse(1, "获取学院信息成功！", m);
    }

    /**
     * 获取至多6条公告
     * @return json
     */
    @GetMapping("/sixNoticeTitle")
    @ResponseBody
    public JSONObject sixNoticeTitle() {
        return indexService.getSixNotice();
    }

    /**
     * 获取公告详情
     * @param id 公告id
     * @return json
     */
    @PostMapping("/notice")
    @ResponseBody
    public JSONObject getNotice(@RequestParam("id") Integer id) {
        return indexService.getNotice(id);
    }


    /**
     * 获取分类列表
     * @return json
     */
    @GetMapping("/classification")
    @ResponseBody
    public JSONObject getClassification(){
        return indexService.getClassification();
    }


    /**
     * 首页搜索框
     * @param search 搜索内容
     * @param institute 学院
     * @param classification 分类
     * @param pageStart 页面开始
     * @param pageSize 页面大小
     * @return json
     */
    @PostMapping("/search")
    @ResponseBody
    public JSONObject search(@RequestParam("search") String search,
                             @RequestParam("institute") String institute,
                             @RequestParam("classification") String classification,
                             @RequestParam("pageStart") Integer pageStart,
                             @RequestParam("pageSize") Integer pageSize) {
        if (pageStart < 0) {
            pageStart = 0;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        PageInfo pageInfo = new PageInfo(pageStart, pageSize, -1);
        return commodityService.searchCommodity(search, institute, classification, pageInfo);
    }

    /**
     * 搜索获取搜索结果页数
     * @param search 搜索内容
     * @param institute 学院
     * @param classification 分类
     * @param pageSize 页面大小
     * @return json
     */
    @PostMapping("/getSearchPages")
    @ResponseBody
    public JSONObject getSearchPages(@RequestParam("search") String search,
                                     @RequestParam("institute") String institute,
                                     @RequestParam("classification") String classification,
                                     @RequestParam("pageSize") Integer pageSize) {
        return commodityService.getSearchPages(search, institute, classification, pageSize);
    }

}
