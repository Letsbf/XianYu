package com.ryan.xianyu.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.domain.User;
import com.ryan.xianyu.service.CommodityService;
import com.ryan.xianyu.service.UserService;
import com.ryan.xianyu.vo.CommodityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommodityService commodityService;

    /**
     * 根据用户id获取用户属性
     * @param userId 用户id
     * @return json
     */
    @PostMapping("/detail")
    @ResponseBody
    public JSONObject detail(@RequestParam("userId") Integer userId) {

        if (userId <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }
        return userService.detail(userId);
    }

    /**
     * 用户中心界面获取"已发布的商品"分页数
     * @param userId 用户id
     * @param pageSize 分页大小
     * @return json
     */
    @PostMapping("/commodityPages")
    @ResponseBody
    public JSONObject getCommodityPages(@RequestParam("userId") Integer userId,
                               @RequestParam("pageSize") Integer pageSize) {
        if (userId <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        Integer pages = commodityService.getPagesByUserId(userId, pageSize);
        if (pages == null || pages <= 0) {
            return Util.constructResponse(0, "获取分页失败", "");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pages", pages);
        return Util.constructResponse(1, "获取分页成功", jsonObject);
    }

    /**
     * 分页获取"已发布的商品"
     * @param userId 用户ID
     * @param pageSize 分页大小
     * @param pageStart 页面开始位置
     * @return json
     */
    @PostMapping("/myPublish")
    @ResponseBody
    public JSONObject myPublish(@RequestParam("userId") Integer userId,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam("pageStart") Integer pageStart) {

        if (userId <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        if (pageStart < 0) {
            pageStart = 0;
        }
        PageInfo pageInfo = new PageInfo(pageStart, pageSize, -1);
        List<CommodityVo> l = commodityService.getCommoditiesByUserId(userId, pageInfo);
        if (l == null) {
            return Util.constructResponse(0, "获取已发布商品失败", "");
        }
        return Util.constructResponse(1, "获取已发布商品成功", JSONArray.toJSON(l));
    }

    /**
     * 更新账户密码
     * @param userId 用户id
     * @param oldPw 旧密码
     * @param newPw 新密码
     * @return json
     */
    @PostMapping("/modifyPw")
    @ResponseBody
    public JSONObject modifyPw(@RequestParam("userId") Integer userId,
                               @RequestParam("oldPw") String oldPw,
                               @RequestParam("newPw") String newPw) {
        if (userId <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }
        if (Util.isEmpty(oldPw) || Util.isEmpty(newPw)) {
            return Util.constructResponse(0, "输入错误，请检查后重试！", "");
        }
        return userService.modifyPw(userId, oldPw, newPw);
    }

    @PostMapping("/update")
    @ResponseBody
    public JSONObject update(@RequestBody User user) {
        if (user.getId() == null || user.getId() <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }

        return userService.update(user);
    }

    /**
     * 获取用户的分页数
     * @param pageSize 分页大小
     * @return json
     */
    @PostMapping("/admin/totalUsers")
    @ResponseBody
    public JSONObject totalUsers(Integer pageSize) {
        return userService.totalUsers(pageSize);
    }


    /**
     * 分页获取用户信息
     * @param pageStart 偏移
     * @param pageSize 页面大小
     * @return json
     */
    @PostMapping("/admin/obtainAllUserByPage")
    @ResponseBody
    public JSONObject obtainAllUserByPage(@RequestParam("pageStart") Integer pageStart,
                                          @RequestParam("pageSize") Integer pageSize) {
        if (pageSize <= 0) {
            pageSize = 10;
        }
        if (pageStart < 0) {
            pageStart = 0;
        }
        PageInfo pageInfo = new PageInfo(pageStart, pageSize, -1);
        return userService.obtainAllUsers(pageInfo);
    }

    /**
     * 管理员删除用户
     * @param userId 用户id
     * @return json
     */
    @PostMapping("/admin/deleteUser")
    @ResponseBody
    public JSONObject deleteUser(@RequestParam("userId") Integer userId) {
        if (userId == null || userId <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }

        return userService.deleteUser(userId);
    }

    /**
     * 支持id、username、name、stuId搜索用户
     * @param search 搜索内容
     * @return json
     */
    @PostMapping("/admin/searchUser")
    @ResponseBody
    public JSONObject searchUser(@RequestParam("search") String search) {
        if (Util.isEmpty(search)) {
            return Util.constructResponse(0, "搜索内容不能为空！", "");
        }
        return userService.searchUsers(search);
    }

    /**
     * 发布新闻和公告
     * @return json
     */
    @PostMapping("/admin/publishNotice")
    @ResponseBody
    public JSONObject publishNotice(@RequestParam("title") String title,
                                    @RequestParam("text") String text,
                                    @RequestParam("userId") Integer userId) {
        if (Util.isEmpty(title)) {
            return Util.constructResponse(0, "文章标题不能为空", "");
        }
        if (Util.isEmpty(text)) {
            return Util.constructResponse(0, "文章正文不能为空", "");
        }
        if (userId == null || userId <= 0) {
            return Util.constructResponse(0, "用户id不正确", "");
        }
        return userService.publishNotice(title, text, userId);

    }


}

