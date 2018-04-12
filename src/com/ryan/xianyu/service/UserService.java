package com.ryan.xianyu.service;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.domain.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface UserService {

    JSONObject login(String username, String password, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    JSONObject register(User user);

    JSONObject detail(Integer userId);

    JSONObject update(User user);

    JSONObject deleteUser(Integer userId);

    JSONObject modifyPw(Integer userId, String oldPw, String newPw);

    JSONObject totalUsers(Integer pageSize);

    JSONObject obtainAllUsers(PageInfo pageInfo);

    JSONObject searchUsers(String search);

    JSONObject publishNotice(String title, String text, Integer userId);

}
