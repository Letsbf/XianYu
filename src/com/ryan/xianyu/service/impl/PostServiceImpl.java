package com.ryan.xianyu.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.dao.PostDao;
import com.ryan.xianyu.dao.UserDao;
import com.ryan.xianyu.domain.Post;
import com.ryan.xianyu.domain.User;
import com.ryan.xianyu.service.PostService;
import com.ryan.xianyu.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserDao userDao;

    @Override
    public JSONObject getReply(Integer commodityId, PageInfo pageInfo) {
        List<Post> res = postDao.selectReply(commodityId, pageInfo);
        if (res == null) {
            return Util.constructResponse(0, "获取帖子回复失败，也许没有回复", "");
        }

        StringBuilder sb = new StringBuilder();
        for (Post re : res) {
            sb.append(re.getReplier());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        Map id2Name = new HashMap<Integer, String>();
        List<User> l = userDao.selectByIds(sb.toString());
        for (User user : l) {
            id2Name.put(user.getId(), user.getUsername());
        }

        List r = new ArrayList<PostVo>();
        for (Post re : res) {
            PostVo postVo = new PostVo();
            postVo.setText(re.getText());
            postVo.setTime(re.getTime());
            postVo.setReplyPostId(re.getReplyPostId());
            postVo.setId(re.getId());

            postVo.setReplier((String) id2Name.get(re.getReplier()));
            r.add(postVo);
        }
        return Util.constructResponse(1, "获取帖子回复成功", JSONArray.toJSON(r));
    }

    @Override
    public JSONObject reply(Integer replier, String text, Integer replyPostId, Integer commodityId) {
        Integer r = postDao.insertReply(replier, text, replyPostId, commodityId);
        if (r <= 0) {
            return Util.constructResponse(0, "回复失败！请稍后尝试", "");
        }
        return Util.constructResponse(1, "回复成功！", "");
    }


    @Override
    public JSONObject deleteReply(Integer postId) {
        Integer r = postDao.deleteReply(postId);
        if (r <= 0) {
            return Util.constructResponse(0, "删除失败略略", "");
        }
        return Util.constructResponse(1, "删除成功", "");
    }
}
