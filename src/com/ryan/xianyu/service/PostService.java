package com.ryan.xianyu.service;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;

public interface PostService {

    JSONObject getReply(Integer commodityId, PageInfo pageInfo);

    JSONObject reply(Integer replier, String text, Integer replyPostId, Integer commodityId);

    JSONObject deleteReply(Integer postId);
}
