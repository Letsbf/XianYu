package com.ryan.xianyu.dao.impl;

import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.NoticeDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NoticeDaoImpl implements NoticeDao{

    @Override
    public Integer publishNotice(String title, String text, Integer publisherId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("title", title);
        params.put("text", text);
        params.put("publisherId", publisherId);
        Integer i = sqlSession.insert("notice.insertNotice", params);
        return i;
    }
}
