package com.ryan.xianyu.dao;

import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.domain.Post;

import java.util.List;

public interface PostDao {


    List<Post> selectReply(Integer commodityId, PageInfo pageInfo);

    Integer insertReply(Integer replier, String text, Integer replyPostId, Integer commodityId);

    Integer deleteReply(Integer postId);

    Integer countReply(Integer commodityId);
}
