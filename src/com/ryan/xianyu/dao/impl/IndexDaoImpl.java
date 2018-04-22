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
    public Classification selectClassificationById(Integer classificationId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Classification classification = sqlSession.selectOne("index.selectClassificationById", classificationId);
        sqlSession.close();
        return classification;
    }

    @Override
    public Map<Integer,String> getInstitute() {
        SqlSession sqlSession = SQLFactory.getSession();
        Map s = sqlSession.selectMap("index.selectInstitute", "id");
        sqlSession.close();
        return s;
    }

    @Override
    public List<Notice> getNotices() {
        SqlSession sqlSession = SQLFactory.getSession();
        List s = sqlSession.selectList("index.selectNotices");
        sqlSession.close();
        return s;
    }

    @Override
    public Notice getNotice(int id) {
        SqlSession sqlSession = SQLFactory.getSession();
        Notice notice = sqlSession.selectOne("index.selectNotice", id);
        sqlSession.close();
        return notice;
    }

    @Override
    public List<Classification> getClassification() {
        SqlSession sqlSession = SQLFactory.getSession();
        List s = sqlSession.selectList("index.selectClassification");
        sqlSession.close();
        return s;
    }

    @Override
    public Integer addInstitute(String name) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.insert("index.addInstitute", name);
        sqlSession.close();
        return i;
    }

    @Override
    public Integer deleteInstitute(Integer instituteId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.delete("index.deleteInstitute", instituteId);
        sqlSession.close();
        return i;
    }

    @Override
    public Integer renameInstitute(Integer instituteId, String newName) {
        Map params = new HashMap();
        params.put("instituteId", instituteId);
        params.put("newName", newName);
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.update("index.renameInstitute", params);
        sqlSession.close();
        return i;
    }

    @Override
    public Integer addClassification(Classification classification) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.insert("index.addClassification", classification);
        sqlSession.close();
        return i;
    }

    @Override
    public Integer deleteClassification(Integer classificationId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.delete("index.deleteClassification", classificationId);
        sqlSession.close();
        return i;
    }

    @Override
    public Integer renameClassification(Integer classificationId, String newName) {
        Map params = new HashMap();
        params.put("classificationId", classificationId);
        params.put("newName", newName);
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.update("index.renameClassification", params);
        sqlSession.close();
        return i;
    }
}
