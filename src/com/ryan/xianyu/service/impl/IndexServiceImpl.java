package com.ryan.xianyu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.dao.IndexDao;
import com.ryan.xianyu.dao.UserDao;
import com.ryan.xianyu.domain.Notice;
import com.ryan.xianyu.domain.User;
import com.ryan.xianyu.service.IndexService;
import com.ryan.xianyu.vo.NoticeVo;
import com.sun.tools.corba.se.idl.constExpr.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class IndexServiceImpl implements IndexService{

    private static Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Autowired
    private IndexDao indexDao;

    @Autowired
    private UserDao userDao;

    @Override
    public JSONObject getInstitute() {
        Map ins = indexDao.getInstitute();
        if (ins == null) {
            return Util.constructResponse(0, "获取学院信息失败", "");
        }
        return Util.constructResponse(1, "获取成功", new JSONObject(ins));
    }


    @Override
    public JSONObject getNotice(int id) {
        if (id <= 0) {
            List s = indexDao.getNotices();
            if (s == null) {
                return Util.constructResponse(0, "获取公告失败", "");
            }
            return Util.constructResponse(1, "获取公告成功", JSONArray.toJSON(s));
        }
        Notice notice = indexDao.getNotice(id);
        if (notice == null) {
            return Util.constructResponse(0, "获取公告失败", "");
        }
        User user = userDao.selectById(Integer.parseInt(notice.getPublisher()));

        NoticeVo n = new NoticeVo();
        n.setTitle(notice.getTitle());
        n.setPublishTime(notice.getPublishTime());
        n.setText(notice.getText());
        n.setPublisherName(user.getUsername());
        return Util.constructResponse(1, "获取公告成功", JSON.toJSON(n));
    }


    @Override
    public JSONObject getClassification() {
        List l = indexDao.getClassification();
        if (l == null) {
            return Util.constructResponse(0, "获取分类失败", "");
        }
        return Util.constructResponse(0, "获取分类成功", JSONArray.toJSON(l));
    }
}
