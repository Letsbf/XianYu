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
import com.ryan.xianyu.vo.PrivateMessageVo;
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

    @Autowired
    private IndexDao indexDao;


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
        try {
            data.put("avatar", Util.readAvatar(user.getAvatar()));
        } catch (Exception e) {
            logger.error("读取头像出现异常", e);
        }
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
            user.setAvatar(Util.readAvatar(user.getAvatar()));
        } catch (Exception e) {
            logger.error("获取头像失败，用户ID:{}", userId, e);
            user.setAvatar("");
        }

        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        try {
            userVo.setAvatar(user.getAvatar());
        } catch (Exception e) {
            logger.error("读取头像失败,用户id:{}", userId, e);
        }
        userVo.setAdmin(user.isAdmin());
        userVo.setEmail(user.getEmail());
        userVo.setName(user.getName());
        userVo.setPhone(user.getPhone());
        userVo.setTime(user.getTime());
        userVo.setStuID(user.getStuId());
        userVo.setUsername(user.getUsername());
        userVo.setInstituteId(user.getInstituteId());


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
            if (!Util.isEmpty(user.getAvatar())) {
                Util.saveAvatar(user);
            }
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
        int pages = i / pageSize;
        if (pages * pageSize != i) {
            pages++;
        }
        jsonObject.put("pages", pages);
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
        List<User> users = userDao.searchUsers(search);
        if (users == null || users.size() == 0) {
            return Util.constructResponse(0, "未搜索到用户", "");
        }
        for (User user : users) {
            try {
                user.setAvatar(Util.readAvatar(user.getAvatar()));
            } catch (Exception e) {
                logger.error("读取头像失败", e);
            }

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
    public JSONObject getBoughtPages(Integer userId, Integer pageSize) {
        List<Deal> dealList = dealDao.getDealsByUserId(userId);
        if (dealList == null) {
            return Util.constructResponse(0, "获取已购买物品失败", "");
        }
        JSONObject data = new JSONObject();
        int pages = dealList.size() / pageSize;
        if (pages * pageSize != dealList.size()) {
            pages++;
        }
        data.put("pages", pages);
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
            int pagesr = posts.size() / pageInfo.getPageSize();
            if (pagesr * pageInfo.getPageSize() != posts.size()) {
                pagesr++;
            }
            pages.put("pages", pagesr);

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
        List<Deal> dealList = dealDao.getDealsByTime(start, end, userId);
        if (dealList == null || dealList.size() == 0) {
            JSONArray ja = new JSONArray();
            JSONObject data = new JSONObject();
            data.put("name", "尚未购买");
            data.put("value", 1);
            ja.add(data);
            return Util.constructResponse(1, "您并没买过东西", ja);
        }

        List<Integer> commodityIdList = new ArrayList<>();
        for (Deal deal : dealList) {
            commodityIdList.add(deal.getCommodityId());
        }
        List<Commodity> commodityList = commodityDao.getCommoditiesByIdList(commodityIdList);
        List<Classification> classificationList = indexDao.getClassification();
        Map classificationId2NameMap = new HashMap<Integer, String>();
        for (Classification classification : classificationList) {
            classificationId2NameMap.put(classification.getId(), classification.getName());
        }
        Map classId2CountMap = new HashMap<String, Integer>();
        for (Commodity commodity : commodityList) {
            classId2CountMap.put(classificationId2NameMap.get(commodity.getClassification()),
                    1 + (Integer) classId2CountMap.getOrDefault(commodity.getClassification(), 0));
        }

        JSONArray ja = new JSONArray();
        for (Object o : classId2CountMap.keySet()) {
            JSONObject data = new JSONObject();
            data.put("name", o);
            data.put("value", classId2CountMap.get(o));
            ja.add(data);
        }

        return Util.constructResponse(1, "获取购物分类详情成功！", ja);
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

        List userIdList = new ArrayList();
        for (PrivateMessage privateMessage : privateMessageList) {
            userIdList.add(privateMessage.getFromId());
        }
        List<User> users = userDao.selectByIds(userIdList);
        Map userId2UserName = new HashMap();
        for (User user : users) {
            userId2UserName.put(user.getId(), user);
        }

        List pml = new ArrayList<PrivateMessageVo>();
        for (PrivateMessage privateMessage : privateMessageList) {
            PrivateMessageVo pmVo = new PrivateMessageVo();
            pmVo.setId(privateMessage.getId());
            pmVo.setTime(privateMessage.getTime());
            pmVo.setFromId(privateMessage.getFromId());

            try {
                pmVo.setFromUserAvatar(Util.readAvatar(((User) userId2UserName.get(privateMessage.getFromId())).getAvatar()));
            } catch (Exception e) {
                logger.error("读取头像失败", e);
            }

            pmVo.setStatus(privateMessage.getStatus());
            pmVo.setMessage(privateMessage.getMessage());
            pmVo.setFromUserName(((User) userId2UserName.get(privateMessage.getFromId())).getUsername());
            pml.add(pmVo);
        }

        return Util.constructResponse(1, "获取私信成功", pml);
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

    @Override
    public JSONObject addInstitute(String instituteName, Integer userId) {
        User user = userDao.selectById(userId);
        if (user == null || !user.isAdmin()) {
            return Util.constructResponse(0, "用户不存在或不具备权限", "");
        }
        Integer i = indexDao.addInstitute(instituteName);
        if (i <= 0) {
            return Util.constructResponse(0, "添加学院失败", "");
        }
        JSONObject data = new JSONObject();
        data.put("" + i, instituteName);
        return Util.constructResponse(1, "添加成功", data);
    }

    @Override
    public JSONObject deleteInstitute(Integer instituteId, Integer userId) {
        User user = userDao.selectById(userId);
        if (user == null || !user.isAdmin()) {
            return Util.constructResponse(0, "用户不存在或不具备权限", "");
        }
        Integer i = indexDao.deleteInstitute(instituteId);
        if (i <= 0) {
            return Util.constructResponse(0, "删除失败", "");
        }
        return Util.constructResponse(1, "删除成功", "");
    }

    @Override
    public JSONObject renameInstitute(Integer instituteId, String newName, Integer userId) {
        User user = userDao.selectById(userId);
        if (user == null || !user.isAdmin()) {
            return Util.constructResponse(0, "用户不存在或不具备权限", "");
        }
        Integer i = indexDao.renameInstitute(instituteId, newName);
        if (i <= 0) {
            return Util.constructResponse(0, "修改学院名失败！", "");
        }

        return Util.constructResponse(1, "修改成功！", "");
    }

    @Override
    public JSONObject addClassification(Classification classification, Integer userId) {
        User user = userDao.selectById(userId);
        if (user == null || !user.isAdmin()) {
            return Util.constructResponse(0, "用户不存在或不具备权限", "");
        }
        Integer i = indexDao.addClassification(classification);
        if (i <= 0) {
            return Util.constructResponse(0, "添加分类失败", "");
        }
        return Util.constructResponse(1, "添加分类成功", classification);
    }

    @Override
    public JSONObject renameClassification(Integer classificationId, String newName, Integer userId) {
        User user = userDao.selectById(userId);
        if (user == null || !user.isAdmin()) {
            return Util.constructResponse(0, "用户不存在或不具备权限", "");
        }
        Integer i = indexDao.renameClassification(classificationId, newName);

        if (i <= 0) {
            return Util.constructResponse(0, "修改分类失败！", "");
        }

        return Util.constructResponse(1, "修改成功！", "");
    }

    @Override
    public JSONObject deleteClassification(Integer classificationId, Integer userId) {
        User user = userDao.selectById(userId);
        if (user == null || !user.isAdmin()) {
            return Util.constructResponse(0, "用户不存在或不具备权限", "");
        }
        Integer i = indexDao.deleteClassification(classificationId);
        if (i <= 0) {
            return Util.constructResponse(0, "删除分类失败", "");
        }
        return Util.constructResponse(1, "删除成功", "");
    }

    @Override
    public JSONObject deleteNotice(Integer noticeId, Integer userId) {
        User user = userDao.selectById(userId);
        if (user == null || !user.isAdmin()) {
            return Util.constructResponse(0, "用户不存在或不具备权限", "");
        }
        Integer i = noticeDao.deleteNotice(noticeId);
        if (i <= 0) {
            return Util.constructResponse(0, "删除公告失败", "");
        }
        return Util.constructResponse(1, "删除成功", "");
    }
}
