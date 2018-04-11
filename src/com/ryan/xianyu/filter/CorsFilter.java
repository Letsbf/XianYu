package com.ryan.xianyu.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CorsFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");

        httpServletResponse.addHeader("Access-Control-Allow-Origin", "http://localhost:8081");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");

        filterChain.doFilter(servletRequest, httpServletResponse);
    }

}