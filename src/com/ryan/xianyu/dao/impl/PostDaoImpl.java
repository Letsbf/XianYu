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
        sqlSession.close();
        return ls;
    }

    @Override
    public Map selectReplyByIds(List commodityIds) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map l = sqlSession.selectMap("post.selectReplyByIds", commodityIds,"commodity_id");
        sqlSession.close();
        return l;
    }

    @Override
    public Integer countUnread(String commodityIds) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.selectOne("post.countUnread", commodityIds);
        sqlSession.close();
        return i;
    }

    @Override
    public Post selectPostById(Integer postId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Post post = sqlSession.selectOne("post.selectPostById", postId);
        sqlSession.close();
        return post;
    }

    @Override
    public List<Post> selectReplyByCommodityIds(List commodityIds,PageInfo pageInfo) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("commodityIds", commodityIds);
        params.put("pageInfo", pageInfo);
        List postList = sqlSession.selectList("post.selectReplyByCommodityIds", params);
        sqlSession.close();
        return postList;
    }

//    @Override
//    public List<Post> selectReplyByUserId(Integer userId) {
//        SqlSession sqlSession = SQLFactory.getSession();
//        List l = sqlSession.selectList("post.selectReplyByUserId", userId);
//        return l;
//    }

    @Override
    public List<Post> selectReply(Integer commodityId, PageInfo pageInfo) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("commodityId", commodityId);
        params.put("pageInfo", pageInfo);
        List<Post> ls = sqlSession.selectList("post.selectReply", params);
        sqlSession.close();
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
        sqlSession.close();
        return i;
    }

    @Override
    public Integer deleteReply(Integer postId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.delete("post.deleteReply", postId);
        sqlSession.close();
        return i;
    }
}
