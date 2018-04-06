package com.ryan.xianyu.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/post")
public class PostController {


    @Autowired
    private PostService postService;


    @PostMapping("/detail")
    @ResponseBody
    public JSONObject getReply(@RequestParam("commodityId") Integer commodityId) {
        if (commodityId <= 0) {
            return Util.constructResponse(0, "商品ID错误", "");
        }
        return postService.getReply(commodityId);

    }

    @GetMapping("/reply")
    @ResponseBody
    public JSONObject reply(@RequestParam("replier") Integer replier,
                            @RequestParam("text") String text,
                            @RequestParam("replyPostId") Integer replyPostId,
                            @RequestParam("commodityId") Integer commodityId) {
        if (Util.isEmpty(text)) {
            return Util.constructResponse(0, "回复不能为空!", "");
        }
        if (replier <= 0 || replyPostId < 0 || commodityId <= 0) {
            return Util.constructResponse(0, "出现异常blabla", "");
        }
        return postService.reply(replier, text, replyPostId, commodityId);
    }

    @GetMapping("/delete")
    @ResponseBody
    public JSONObject delete(@RequestParam("postId") Integer postId) {
        if (postId <= 0) {
            return Util.constructResponse(0, "删除失败略略略", "");
        }

        return postService.deleteReply(postId);
    }


}
