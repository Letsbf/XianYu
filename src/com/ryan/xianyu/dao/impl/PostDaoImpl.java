package com.ryan.xianyu.dao.impl;

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
    public List<Post> selectReply(Integer commodityId) {
        SqlSession sqlSession = SQLFactory.getSession();
        List<Post> ls = sqlSession.selectList("post.selectReply", commodityId);
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
