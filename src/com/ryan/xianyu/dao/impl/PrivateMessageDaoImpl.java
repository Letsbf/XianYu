package com.ryan.xianyu.dao.impl;

import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.PrivateMessageDao;
import com.ryan.xianyu.domain.PrivateMessage;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrivateMessageDaoImpl implements PrivateMessageDao {

    @Override
    public Integer insertMessage(Integer fromId, Integer toId, String message) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("fromId", fromId);
        params.put("toId", toId);
        params.put("message", message);
        Integer i = sqlSession.insert("privateMessage.insertMessage", params);
        return i;
    }

    @Override
    public List<PrivateMessage> getPrivateMessageByToId(Integer userId) {
        SqlSession sqlSession = SQLFactory.getSession();
        List<PrivateMessage> l = sqlSession.selectList("privateMessage.getPrivateMessageByToId", userId);
        return l;
    }

    @Override
    public Integer updateMessage2Read(Integer userId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer res = sqlSession.update("privateMessage.updateMessage2Read", userId);
        return res;
    }

    @Override
    public PrivateMessage getPrivateMessageById(Integer messageId) {
        SqlSession sqlSession = SQLFactory.getSession();
        PrivateMessage privateMessage = sqlSession.selectOne("privateMessage.getPrivateMessageById", messageId);
        return privateMessage;
    }

    @Override
    public Integer deleteById(Integer messageId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.delete("privateMessage.deleteById", messageId);
        return i;
    }

}
