package com.ryan.xianyu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.dao.IndexDao;
import com.ryan.xianyu.dao.UserDao;
import com.ryan.xianyu.domain.User;
import com.ryan.xianyu.service.IndexService;
import com.ryan.xianyu.service.UserService;
import com.ryan.xianyu.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.*;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private IndexService indexService;

    @Override
    public JSONObject login(String username, String password, HttpServletRequest request, HttpServletResponse response) {

        User user = userDao.selectByUserName(username);
        if (user == null || !user.getPassword().equals(password)) {
            return Util.constructResponse(0, "账号不存在或密码不正确", "");
        }

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60 * 60);
        session.setAttribute("user", user.getUsername());

        JSONObject data = new JSONObject();
        data.put("sessionId", session.getId());
        data.put("avatar", user.getAvatar());
        data.put("admin", user.isAdmin() ? "1" : "0");
        data.put("username", user.getUsername());
        data.put("id", user.getId());
        data.put("sessionId", session.getId());

        logger.info("用户 {} 登陆成功", user.getUsername());

        return Util.constructResponse(1, "登录成功", data.toJSONString());
    }


    @Override
    public JSONObject register(User user) {
        User ue = userDao.selectByUserName(user.getUsername());
        if (ue != null && ue.getUsername().equals(user.getUsername())) {
            return Util.constructResponse(0, "账号已存在！", "");
        }

        int res = userDao.insertNewUser(user);
        if (res > 0) {
            JSONObject data = new JSONObject();
            data.put("username", user.getUsername());
            logger.info("用户 {} 注册成功", user.getUsername());
            return Util.constructResponse(1, "注册成功！", data.toJSONString());
        }

        return Util.constructResponse(0, "注册失败！", "");
    }

    /**
     * 根据用户id获取用户属性
     * @param userId 用户id
     * @return json
     */
    @Override
    public JSONObject detail(Integer userId) {
        User user = userDao.selectById(userId);

        if (user == null) {
            logger.error("获取用户信息失败,userId:{}", userId);
            return Util.constructResponse(0, "获取用户个人信息失败！", "");
        }

        try {
            user.setAvatar(Util.readImages(user.getAvatar()));
        } catch (Exception e) {
            logger.error("获取头像失败，用户ID:{}", userId, e);
            user.setAvatar("");
        }

        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setAvatar(user.getAvatar());
        userVo.setAdmin(user.isAdmin());
        userVo.setEmail(user.getEmail());
        userVo.setName(user.getName());
        userVo.setPhone(user.getPhone());
        userVo.setTime(user.getTime());
        userVo.setStuID(user.getStuId());
        userVo.setUsername(user.getUsername());

        if (user.getSex() == null) {
            userVo.setSex("未知");
        } else {
            userVo.setSex(user.getSex() == 1 ? "男" : "女");
        }

        Map l = indexService.getInstitute();
        if (l != null && l.containsKey(user.getInstituteId())) {
            userVo.setInstitute((String) l.get(user.getInstituteId()));
        } else {
            logger.error("学院map:{},用户学院ID:{}", l, user.getInstituteId());
            userVo.setInstitute("某学院");
        }

        return Util.constructResponse(1, "获取用户个人信息成功！", JSON.toJSON(userVo));
    }

    @Override
    public JSONObject update(User user) {
        try {
            Util.saveAvatar(user);
        } catch (Exception e) {
            logger.error("保存头像失败!userId:{}", user.getId(), e);
            return Util.constructResponse(0, "更新失败！", "");
        }
        Integer i = userDao.updateUser(user);
        if (i > 0) {
            return Util.constructResponse(1, "更新成功！", "");
        }
        return Util.constructResponse(0, "更新失败！", "");
    }

    @Override
    public JSONObject deleteUser(Integer userId) {
        Integer i = userDao.deleteUser(userId);
        if (i > 0) {
            return Util.constructResponse(1, "删除成功！", "");
        }
        return Util.constructResponse(0, "删除失败！", "");
    }

}
