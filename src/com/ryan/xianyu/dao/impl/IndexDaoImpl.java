package com.ryan.xianyu.dao.impl;

import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.IndexDao;
import com.ryan.xianyu.domain.Classification;
import com.ryan.xianyu.domain.Notice;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IndexDaoImpl implements IndexDao {

    @Override
    public Map<Integer,String> getInstitute() {
        SqlSession sqlSession = SQLFactory.getSession();
        Map s = sqlSession.selectMap("index.selectInstitute", "id");
        return s;
    }

    @Override
    public List<Notice> getNotices() {

        SqlSession sqlSession = SQLFactory.getSession();
        List s = sqlSession.selectList("index.selectNotices");
        return s;
    }

    @Override
    public Notice getNotice(int id) {
        SqlSession sqlSession = SQLFactory.getSession();
        Notice notice = sqlSession.selectOne("index.selectNotice", id);
        return notice;
    }

    @Override
    public List<Classification> getClassification() {
        SqlSession sqlSession = SQLFactory.getSession();
        List s = sqlSession.selectList("index.selectClassification");
        return s;
    }
}
