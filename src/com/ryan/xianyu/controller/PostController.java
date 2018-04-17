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
     * 分页获取商品当前的回复
     *
     * @param commodityId 商品id
     * @param pageSize    分页大小
     * @param pageStart   页面开始
     * @return json
     */
    @PostMapping("/detail")
    @ResponseBody
    public JSONObject getReply(@RequestParam("commodityId") Integer commodityId,
                               @RequestParam("pageSize") Integer pageSize,
                               @RequestParam("pageStart") Integer pageStart) {
        if (commodityId <= 0) {
            return Util.constructResponse(0, "商品ID错误", "");
        }
        if (pageSize < 0) {
            pageSize = 10;
        }
        if (pageStart < 0) {
            pageStart = 0;
        }
        PageInfo pageInfo = new PageInfo(pageStart, pageSize, -1);
        return postService.getReply(commodityId, pageInfo);
    }

    /**
     * 回复帖子
     * @param replier 回复人ID
     * @param text 回复内容
     * @param replyPostId 被回复的回帖ID 直接回复设为0
     * @param commodityId 商品ID
     * @return json
     */
    @PostMapping("/reply")
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
     * 删除回复
     * @param postId 回复ID
     * @param userId 用户ID
     * @return json
     */
    @PostMapping("/delete")
    @ResponseBody
    public JSONObject delete(@RequestParam("postId") Integer postId,
                             @RequestParam("userId") Integer userId) {
        if (postId <= 0) {
            return Util.constructResponse(0, "删除失败略略略", "");
        }

        return postService.deleteReply(postId, userId);
    }


}
