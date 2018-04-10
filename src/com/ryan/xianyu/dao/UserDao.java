package com.ryan.xianyu.dao;


import com.ryan.xianyu.domain.User;

import java.util.List;

public interface UserDao {

    User selectByUserName(String username);

    Integer insertNewUser(User user);

    User selectById(Integer id);

    List<User> selectByIds(String ids);

    Integer updateUser(User user);

    Integer deleteUser(Integer userId);
}
