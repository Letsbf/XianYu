package com.ryan.xianyu.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/index")
public class IndexController {
    // TODO: 2018/4/5 search

    @Autowired
    private IndexService indexService;

    @PostMapping("/institute")
    @ResponseBody
    public JSONObject getInstitute() {
        return indexService.getInstitute();
    }


    @PostMapping("/notice")
    @ResponseBody
    public JSONObject getNotice(@RequestParam("id") Integer id) {
        return indexService.getNotice(id);
    }


    @PostMapping("/classification")
    @ResponseBody
    public JSONObject getClassification(){
        return indexService.getClassification();
    }




}
