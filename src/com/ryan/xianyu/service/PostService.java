package com.ryan.xianyu.service;

import com.alibaba.fastjson.JSONObject;

public interface PostService {

    JSONObject getReply(Integer commodityId);

    JSONObject reply(Integer replier, String text, Integer replyPostId, Integer commodityId);

    JSONObject deleteReply(Integer postId);
}
