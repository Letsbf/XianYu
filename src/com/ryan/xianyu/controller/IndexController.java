package com.ryan.xianyu.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.service.CommodityService;
import com.ryan.xianyu.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return indexService.getInstitute();
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
                             @RequestParam("classification") String classification) {
//        if (Util.isEmpty(search)) {
//            search = "";
//        }
//        if (Util.isEmpty(institute)) {
//            institute = "";
//        }
//        if (Util.isEmpty(classification)) {
//            classification = "";
//        }
        return commodityService.searchCommodity(search, institute, classification);
    }

    public static void main(String[] args) {
        String[] s = "".split(";");
        System.out.println("te" + s[0] + "st");
    }



}
