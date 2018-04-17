package com.ryan.xianyu.dao;


import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.domain.User;
import sun.jvm.hotspot.debugger.Page;

import java.util.List;

public interface UserDao {

    User selectByUserName(String username);

    Integer insertNewUser(User user);

    User selectById(Integer id);

    List<User> selectByIds(List ids);

    Integer updateUser(User user);

    Integer deleteUser(Integer userId);

    Integer updatePw(Integer userId, String oldPw, String newPw);

    Integer totalUsers();

    List<User> selectAllByPage(PageInfo pageInfo);

    List<User> searchUsers(String search);

    Integer setUser2Admin(Integer userId);

}
