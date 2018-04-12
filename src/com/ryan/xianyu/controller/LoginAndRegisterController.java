package com.ryan.xianyu.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.domain.User;
import com.ryan.xianyu.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginAndRegisterController {

    private static Logger logger = LoggerFactory.getLogger(LoginAndRegisterController.class);

    @Autowired
    private UserService userService;


    // TODO: 2018/4/11 读消息的请求
    @GetMapping("/login")
    @ResponseBody
    public JSONObject login(@RequestParam("username") String username, @RequestParam("password") String password,
                            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (Util.isEmpty(username) || Util.isEmpty(password)) {
            return Util.constructResponse(0, "账号密码不能为空！", "");
        }

        if (username.length() > 20 || password.length() > 16) {
            return Util.constructResponse(0, "账号密码不正确!", "");
        }

        return userService.login(username, password, httpServletRequest, httpServletResponse);
    }

    @PostMapping("/register")
    @ResponseBody
    public JSONObject register(@RequestBody User user) {
        if (Util.isEmpty(user.getUsername()) || user.getUsername().length() > 20) {
            return Util.constructResponse(0, "用户名长度不正确！", "");
        }

        if (Util.isEmpty(user.getPassword()) || user.getPassword().length() > 16) {
            return Util.constructResponse(0, "密码长度不正确！", "");
        }

        if (Util.isEmpty(user.getName()) || user.getName().length() > 16) {
            return Util.constructResponse(0, "姓名长度不正确！", "");
        }

        if (Util.isEmpty(user.getStuId()) || user.getStuId().length() > 20) {
            return Util.constructResponse(0, "学号长度不正确！", "");
        }

        if (Util.isPhoneNum(user.getPhone())) {
            return Util.constructResponse(0, "手机号不正确！", "");
        }

        if (user.getInstituteId() <= 0) {
            return Util.constructResponse(0, "学院不正确！", "");
        }

        if (Util.isEmpty(user.getEmail())) {
            user.setEmail("");
        }

        return userService.register(user);
    }

    // TODO: 2018/4/5 存入全局的登录校验中
    @GetMapping("/logout")
    @ResponseBody
    public JSONObject logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null || session.isNew() || session.getAttribute("user") != null) {
            logger.error("session为空，未登录状态!");
            return Util.constructResponse(1, "身份过期，已登出！", "");
        }

        session.invalidate();

        return Util.constructResponse(1, "退出成功！", "");
    }


    public static void main(String[] args) {
        System.out.println("汉字".length());
    }
}
