package com.ryan.xianyu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by tong on 2018/3/30.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/test1")
    public String test1() {
        return "test";
    }

    @RequestMapping("/test2")
    public ModelAndView test2() {
        ModelAndView modelAndView = new ModelAndView("test");
        modelAndView.addObject("msg", "来自test2返回的消息");
        return modelAndView;
    }

    @RequestMapping(value = "/test3",method = RequestMethod.POST)
    @ResponseBody
    public String test3() {
        return "来自test3返回的消息";
    }

}
