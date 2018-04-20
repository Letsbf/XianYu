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
import javafx.geometry.Pos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    private static Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserDao userDao;

    @Override
    public JSONObject getReply(Integer commodityId, PageInfo pageInfo) {
        List<Post> res = postDao.selectReply(commodityId, pageInfo);
        if (res == null || res.size() == 0) {
            return Util.constructResponse(0, "获取帖子回复失败，也许没有回复", "");
        }

        List<Integer> idList = new ArrayList<>();
        for (Post re : res) {
            idList.add(re.getReplier());
        }

        Map id2Name = new HashMap<Integer, String>();
        Map id2Admin = new HashMap<Integer, Integer>();
        Map id2User = new HashMap<Integer, User>();
        List<User> l = userDao.selectByIds(idList);
        logger.error("userList:{}", l);

        for (User user : l) {
            id2Name.put(user.getId(), user.getUsername());
            id2Admin.put(user.getId(), user.isAdmin() ? 1 : 0);
            try {
                user.setAvatar(Util.readImages(user.getAvatar()));
            } catch (Exception e) {
                logger.error("加载头像失败", e);
            }
            id2User.put(user.getId(), user);
        }

        List r = new ArrayList<PostVo>();
        for (Post re : res) {
            PostVo postVo = new PostVo();
            postVo.setText(re.getText());
            postVo.setTime(re.getTime());
            postVo.setReplierId(re.getReplier());
            postVo.setReplyPostId(re.getReplyPostId());
            postVo.setId(re.getId());
            postVo.setReplierIsAdmin((Integer) id2Admin.get(re.getReplier()));
            postVo.setReplierAvatar(((User) id2User.get(re.getReplier())).getAvatar());

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
    public JSONObject deleteReply(Integer postId, Integer userId) {
        User user = userDao.selectById(userId);
        if (user == null) {
            return Util.constructResponse(0, "用户不存在", "");
        }
        Post post = postDao.selectPostById(postId);
        if (!post.getReplier().equals(userId) && !user.isAdmin()) {
            return Util.constructResponse(0, "出现异常", "");
        }
        Integer r = postDao.deleteReply(postId);
        if (r <= 0) {
            return Util.constructResponse(0, "删除失败略略", "");
        }
        return Util.constructResponse(1, "删除成功", "");
    }
}
