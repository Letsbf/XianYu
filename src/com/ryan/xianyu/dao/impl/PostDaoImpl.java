package com.ryan.xianyu.dao.impl;

import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.PostDao;
import com.ryan.xianyu.domain.Post;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostDaoImpl implements PostDao {

    @Override
    public Integer countReply(Integer commodityId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer ls = sqlSession.selectOne("post.countReply", commodityId);
        return ls;
    }

    @Override
    public Map selectReplyByIds(String commodityIds) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map l = sqlSession.selectMap("post.selectReplyByIds", commodityIds,"commodity_id");
        return l;
    }

    @Override
    public Integer countUnread(String commodityIds) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.selectOne("post.countUnread", commodityIds);
        return i;
    }

    @Override
    public List<Post> selectReply(Integer commodityId, PageInfo pageInfo) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("commodityId", commodityId);
        params.put("pageInfo", pageInfo);
        List<Post> ls = sqlSession.selectList("post.selectReply", params);
        return ls;
    }


    @Override
    public Integer insertReply(Integer replier, String text, Integer replyPostId, Integer commodityId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("replier", replier);
        params.put("text", text);
        params.put("replyPostId", replyPostId);
        params.put("commodityId", commodityId);
        Integer i = sqlSession.insert("post.insertReply", params);
        return i;
    }

    @Override
    public Integer deleteReply(Integer postId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.delete("post.deleteReply", postId);
        return i;
    }
}
