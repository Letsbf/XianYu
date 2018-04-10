package com.ryan.xianyu.dao.impl;


import com.ryan.xianyu.common.SQLFactory;
import com.ryan.xianyu.dao.UserDao;
import com.ryan.xianyu.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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
    public List<User> selectByIds(String ids) {
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
}
