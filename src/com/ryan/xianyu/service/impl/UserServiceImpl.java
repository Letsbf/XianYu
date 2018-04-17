package com.ryan.xianyu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.common.PageInfo;
import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.dao.*;
import com.ryan.xianyu.domain.*;
import com.ryan.xianyu.service.IndexService;
import com.ryan.xianyu.service.UserService;
import com.ryan.xianyu.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private IndexService indexService;

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private CommodityDao commodityDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private DealDao dealDao;

    @Autowired
    private PrivateMessageDao privateMessageDao;


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
        // TODO: 2018/4/17 未返回头像base64
        data.put("avatar", user.getAvatar());
        data.put("admin", user.isAdmin() ? "1" : "0");
        data.put("username", user.getUsername());
        data.put("id", user.getId());
        data.put("sessionId", session.getId());

        Integer userId = user.getId();
        List<Commodity> l = commodityDao.getCommoditiesByUserId(userId, null);
        StringBuilder sb = new StringBuilder();
        for (Commodity commodity : l) {
            sb.append(commodity.getId() + ",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        logger.warn("userId:{},commodityIds:{}", userId, sb.toString());
        Integer unread = postDao.countUnread(sb.toString());
        if (unread == null) {
            unread = 0;
        }
        data.put("unread", unread);

        logger.info("用户 {} 登陆成功", user.getUsername());

        return Util.constructResponse(1, "登录成功", data);
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
            return Util.constructResponse(1, "注册成功！", data);
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
        User u1 = userDao.selectById(user.getId());
        if (u1 == null) {
            return Util.constructResponse(0, "用户不存在", "");
        }
        User u2 = userDao.selectByUserName(user.getUsername());
        if (!u2.getId().equals(user.getId())) {
            return Util.constructResponse(0, "用户名已存在", "");
        }
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

    @Override
    public JSONObject modifyPw(Integer userId, String oldPw, String newPw) {
        Integer res = userDao.updatePw(userId, oldPw, newPw);

        if (res <= 0) {
            return Util.constructResponse(0, "更新账户密码失败！", "");
        }

        return Util.constructResponse(1, "更新成功！", "");
    }

    @Override
    public JSONObject totalUsers(Integer pageSize) {
        Integer i = userDao.totalUsers();
        if (i == null) {
            return Util.constructResponse(0, "获取用户总数失败！", "");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pages", i / pageSize + 1);
        return Util.constructResponse(1, "获取用户总数成功", jsonObject);
    }

    @Override
    public JSONObject obtainAllUsers(PageInfo pageInfo) {
        List l = userDao.selectAllByPage(pageInfo);
        if (l == null || l.size() == 0) {
            return Util.constructResponse(0, "获取用户列表失败", "");
        }
        return Util.constructResponse(1, "获取用户信息成功", JSONArray.toJSON(l));
    }

    @Override
    public JSONObject searchUsers(String search) {
        // TODO: 2018/4/12 支持分页、高级模糊搜索
        List<User> users = userDao.searchUsers(search);
        if (users == null || users.size() == 0) {
            return Util.constructResponse(0, "未搜索到用户", "");
        }
        return Util.constructResponse(1, "搜索成功！", JSONArray.toJSON(users));
    }

    @Override
    public JSONObject publishNotice(String title, String text, Integer userId) {
        // TODO: 2018/4/12 indexmapper和indexDao中notice代码迁移至noticeMapper\Dao
        Integer i = noticeDao.publishNotice(title, text, userId);
        if (i <= 0) {
            return Util.constructResponse(0, "发布公告失败！", "");
        }
        return Util.constructResponse(1, "发布公告成功！", "");
    }

    @Override
    public JSONObject bought(Integer userId, PageInfo pageInfo) {
        logger.error("userId:{},pageInfo:{}", userId, pageInfo);
        List<Deal> dealList = dealDao.getDealsByUserIdByPage(userId, pageInfo);
        if (dealList == null || dealList.size() == 0) {
            return Util.constructResponse(0, "获取已购买列表失败", "");
        }
        StringBuilder commodityIdsStr = new StringBuilder("");
        for (Deal deal : dealList) {
            commodityIdsStr.append(deal.getCommodityId() + ",");
        }
        if (commodityIdsStr.length() > 0) {
            commodityIdsStr.deleteCharAt(commodityIdsStr.length() - 1);
        }
        List<Commodity> l = commodityDao.getCommoditiesByIds(commodityIdsStr.toString());
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < l.size(); i++) {
            JSONObject data = new JSONObject();
            data.put("id", l.get(i).getId());
            data.put("title", l.get(i).getTitle());
            data.put("price", l.get(i).getPrice());
            data.put("description", l.get(i).getDescription());
            jsonArray.add(data);
        }
        return Util.constructResponse(1, "分页获取已购买商品成功", jsonArray);
    }

    @Override
    public JSONObject getBoughtPages(Integer userId, Integer pageSize) {
        List<Deal> dealList = dealDao.getDealsByUserId(userId);
        if (dealList == null) {
            return Util.constructResponse(0, "获取已购买物品失败", "");
        }
        JSONObject data = new JSONObject();
        data.put("pages", dealList.size() / pageSize + 1);
        return Util.constructResponse(1, "获取页数成功", data);
    }

    @Override
    public JSONObject addAdmin(Integer adminId, Integer userId) {
        User admin = userDao.selectById(adminId);
        if (admin == null || !admin.isAdmin()) {
            return Util.constructResponse(0, "您还不是管理员呢，没有这个权利哦", "");
        }
        User user = userDao.selectById(userId);
        if (user == null || user.isAdmin()) {
            return Util.constructResponse(0, "他不存在或者已经是管理员了呢", "");
        }
        Integer i = userDao.setUser2Admin(userId);
        if (i <= 0) {
            return Util.constructResponse(0, "设置管理员失败！", "");
        }
        return Util.constructResponse(1, "设置管理员成功！", "");
    }

    @Override
    public JSONObject getReply2Me(Integer userId, PageInfo pageInfo) {
        //获取该用户发布的所有商品的所有回复、及商品id-title的map
        List<Commodity> commodityList = commodityDao.getCommoditiesByUserId(userId, null);
        if (commodityList == null || commodityList.size() == 0) {
            return Util.constructResponse(0, "您还未发布商品", "");
        }
        List<Integer> commodityIds = new ArrayList<>();
        Map<Integer, String> commodityId2TitleMap = new HashMap<>();
        for (Commodity commodity : commodityList) {
            commodityIds.add(commodity.getId());
            commodityId2TitleMap.put(commodity.getId(), commodity.getTitle());
        }
        List<Post> posts = postDao.selectReplyByCommodityIds(commodityIds,pageInfo);

        if (posts == null || posts.size() == 0) {
            return Util.constructResponse(0, "没有回复哦", "");
        }

        //获取所有回复的用户的名称
        List<Integer> userIds = new ArrayList<>();
        for (Post post : posts) {
            userIds.add(post.getReplier());
        }
        List<User> users = userDao.selectByIds(userIds);
        Map<Integer, String> userId2NameMap = new HashMap<>();
        for (User user : users) {
            userId2NameMap.put(user.getId(), user.getUsername());
        }

        //组装数据
        JSONArray finalData = new JSONArray();
        JSONArray dataArray = new JSONArray();
        JSONObject pages = new JSONObject();
        if (pageInfo.getStart() == 0) {
            pages.put("pages", posts.size() / pageInfo.getPageSize() + 1);
            finalData.add(pages);
        }

        for (Post post : posts) {
            JSONObject data = new JSONObject();
            data.put("postId", post.getId());
            data.put("commodityId", post.getCommodityId());
            data.put("commodityTitle", commodityId2TitleMap.get(post.getCommodityId()));
            data.put("replierId", post.getReplier());
            data.put("replierUserName", userId2NameMap.get(post.getReplier()));
            data.put("unread", post.getStatus());
            dataArray.add(data);
        }
        finalData.add(dataArray);
        return Util.constructResponse(1, "获取回复成功", finalData);
    }

    @Override
    public JSONObject timeShopping(Long start, Long end, Integer userId) {


        return null;
    }

    @Override
    public JSONObject sendPrivateMessage(Integer fromId, Integer toId, String message) {
        Integer res = privateMessageDao.insertMessage(fromId, toId, message);
        if (res <= 0) {
            return Util.constructResponse(0, "发送失败！", "");
        }
        return Util.constructResponse(1, "发送成功！", "");
    }

    @Override
    public JSONObject myPrivateMessage(Integer userId) {
        List<PrivateMessage> privateMessageList = privateMessageDao.getPrivateMessageByToId(userId);
        if (privateMessageList == null || privateMessageList.size() == 0) {
            return Util.constructResponse(1, "没有私信", "");
        }
        privateMessageDao.updateMessage2Read(userId);
        return Util.constructResponse(1, "获取私信成功", privateMessageList);
    }

    @Override
    public JSONObject deletePrivateMessage(Integer messageId, Integer userId) {
        PrivateMessage privateMessage = privateMessageDao.getPrivateMessageById(messageId);
        if (privateMessage == null || !privateMessage.getToId().equals(userId)) {
            return Util.constructResponse(0, "不是给你的消息哦", "");
        }
        Integer i = privateMessageDao.deleteById(messageId);
        if (i <= 0) {
            return Util.constructResponse(0, "删除失败", "");
        }
        return Util.constructResponse(1, "删除成功", "");
    }

}
