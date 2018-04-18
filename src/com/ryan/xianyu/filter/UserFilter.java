package com.ryan.xianyu.filter;

import com.ryan.xianyu.common.Util;
import com.ryan.xianyu.dao.UserDao;
import com.ryan.xianyu.dao.impl.UserDaoImpl;
import com.ryan.xianyu.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(UserFilter.class);

    //本项目中最不优雅的一行代码
    @Autowired
    private UserDao userDao = new UserDaoImpl();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        HttpSession session = request.getSession();
//        String username = (String) session.getAttribute("user");
//        if (Util.isEmpty(username)) {
//            logger.error("未登录");
//            response.sendRedirect("/login");
//            return;
//        }

//        String uriStr = request.getRequestURI();
//        logger.error("-----UserFilter uri:{}----", uriStr);
//        String[] uris = uriStr.split("/");
//        for (String uri : uris) {
//            if (uri.contains("admin")) {
//                String userIdStr = request.getParameter("userId");
//                if (Util.isEmpty(userIdStr)) {
//                    return;
//                }
//                try {
//                    Integer userId = Integer.parseInt(userIdStr);
//                    User user = userDao.selectById(userId);
//                    if (user == null || !user.isAdmin()) {
//                        logger.error("用户不存在或用户非管理员！");
//                        return;
//                    }
//                } catch (Exception e) {
//                    logger.error("验证用户信息异常,userIdStr:{}", userIdStr, e);
//                    return;
//                }
//                filterChain.doFilter(request, servletResponse);
//            }
//        }

        filterChain.doFilter(request, servletResponse);
    }
}
