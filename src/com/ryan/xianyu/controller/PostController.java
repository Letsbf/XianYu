package com.ryan.xianyu.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
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


    /**
     * 获取帖子当前回复
     *
     * @param commodityId
     * @return
     */
    @PostMapping("/detail")
    @ResponseBody
    public JSONObject getReply(@RequestParam("commodityId") Integer commodityId,
                               @RequestParam("pageSize") Integer pageSize,
                               @RequestParam("pageStart") Integer pageStart,
                               @RequestParam("total") Integer total) {
        if (commodityId <= 0) {
            return Util.constructResponse(0, "商品ID错误", "");
        }
        if (pageSize < 0) {
            pageSize = 10;
        }
        if (pageStart < 0) {
            pageStart = 0;
        }
        PageInfo pageInfo = new PageInfo(pageStart, pageSize, total);
        return postService.getReply(commodityId, pageInfo);
    }

    /**
     * 回复帖子
     * @param replier
     * @param text
     * @param replyPostId
     * @param commodityId
     * @return
     */
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

    /**
     * 删除当前回复
     * @param postId
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public JSONObject delete(@RequestParam("postId") Integer postId) {
        // TODO: 2018/4/6 校验管理员身份 禁止删除他人回复
        if (postId <= 0) {
            return Util.constructResponse(0, "删除失败略略略", "");
        }

        return postService.deleteReply(postId);
    }


}
