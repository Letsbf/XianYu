package com.ryan.xianyu.dao.impl;


import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.UserDao;
import com.ryan.xianyu.domain.User;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserDaoImpl implements UserDao {

    @Override
    public User selectByUserName(String username) {
        SqlSession sqlSession = SQLFactory.getSession();
        User user = sqlSession.selectOne("user.selectByUserName", username);
        return user;
    }

    @Override
    public Integer insertNewUser(User user) {
        SqlSession sqlSession = SQLFactory.getSession();
        int i = sqlSession.insert("user.insertNewUser", user);
        return i;
    }

    @Override
    public User selectById(Integer id) {
        SqlSession sqlSession = SQLFactory.getSession();
        User user = sqlSession.selectOne("user.selectById", id);
        return user;
    }

    @Override
    public List<User> selectByIds(List ids) {
        SqlSession sqlSession = SQLFactory.getSession();
        List<User> s = sqlSession.selectList("user.selectByIds", ids);
        return s;
    }

    @Override
    public Integer updateUser(User user) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer s = sqlSession.update("user.updateUser", user);
        return s;
    }

    @Override
    public Integer deleteUser(Integer userId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer s = sqlSession.delete("user.deleteUser", userId);
        return s;
    }

    @Override
    public Integer updatePw(Integer userId, String oldPw, String newPw) {
        SqlSession sqlSession = SQLFactory.getSession();
        Map params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("oldPw", oldPw);
        params.put("newPw", newPw);
        Integer s = sqlSession.update("user.updatePw", params);
        return s;
    }

    @Override
    public Integer totalUsers() {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer s = sqlSession.selectOne("user.totalUsers");
        return s;
    }

    @Override
    public List<User> selectAllByPage(PageInfo pageInfo) {
        SqlSession sqlSession = SQLFactory.getSession();
        List<User> l = sqlSession.selectList("user.selectAllByPage", pageInfo);
        return l;
    }

    @Override
    public List<User> searchUsers(String search) {
        SqlSession sqlSession = SQLFactory.getSession();
        List<User> l = sqlSession.selectList("user.searchUsers", search);
        return l;
    }

    @Override
    public Integer setUser2Admin(Integer userId) {
        SqlSession sqlSession = SQLFactory.getSession();
        Integer i = sqlSession.update("user.setUser2Admin", userId);
        return i;
    }


}
