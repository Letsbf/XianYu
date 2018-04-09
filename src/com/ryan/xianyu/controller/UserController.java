package com.ryan.xianyu.controller;


import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.domain.User;
import com.ryan.xianyu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/detail")
    @ResponseBody
    public JSONObject detail(@RequestParam("userId") Integer userId) {

        if (userId <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }
        return userService.detail(userId);
    }


    @GetMapping("/update")
    @ResponseBody
    public JSONObject update(@RequestBody User user) {
        // TODO: 2018/4/6 上传头像
        if (user.getId() == null || user.getId() <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }

        return userService.update(user);
    }





}

