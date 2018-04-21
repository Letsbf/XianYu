package com.ryan.xianyu.service;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.domain.Classification;
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

    JSONObject getBoughtPages(Integer userId, Integer pageSize);

    JSONObject addAdmin(Integer adminId, Integer userId);

    JSONObject getReply2Me(Integer userId, PageInfo pageInfo);

    JSONObject timeShopping(Long start, Long end, Integer userId);

    JSONObject sendPrivateMessage(Integer fromId, Integer toId, String message);

    JSONObject myPrivateMessage(Integer userId);

    JSONObject deletePrivateMessage(Integer messageId, Integer userId);

    JSONObject addInstitute(String instituteName, Integer userId);

    JSONObject renameInstitute(Integer instituteId, String newName, Integer userId);

    JSONObject deleteInstitute(Integer instituteId, Integer userId);

    JSONObject addClassification(Classification classification, Integer userId);

    JSONObject renameClassification(Integer classificationId,String newName,Integer userId);

    JSONObject deleteClassification(Integer classificationId, Integer userId);

    JSONObject deleteNotice(Integer noticeId, Integer userId);
}
