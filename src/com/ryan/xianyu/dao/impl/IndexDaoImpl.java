package com.ryan.xianyu.dao.impl;

import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.IndexDao;
import com.ryan.xianyu.domain.Classification;
import com.ryan.xianyu.domain.Notice;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Override
    public Integer addInstitute(String name) {
        return SQLFactory.getSession().insert("index.addInstitute", name);
    }

    @Override
    public Integer deleteInstitute(Integer instituteId) {
        return SQLFactory.getSession().delete("index.deleteInstitute", instituteId);
    }

    @Override
    public Integer renameInstitute(Integer instituteId, String newName) {
        Map params = new HashMap();
        params.put("instituteId", instituteId);
        params.put("newName", newName);
        return SQLFactory.getSession().update("index.renameInstitute", params);
    }

    @Override
    public Integer addClassification(Classification classification) {
        return SQLFactory.getSession().insert("index.addClassification", classification);
    }

    @Override
    public Integer deleteClassification(Integer classificationId) {
        return SQLFactory.getSession().delete("index.deleteClassification", classificationId);
    }

    @Override
    public Integer renameClassification(Integer classificationId, String newName) {
        Map params = new HashMap();
        params.put("classificationId", classificationId);
        params.put("newName", newName);
        return SQLFactory.getSession().update("index.renameClassification", params);
    }
}
