package com.ryan.xianyu.common;


import com.alibaba.fastjson.JSONObject;

public class Util {


    public static boolean isEmpty(String str){
        return "".equals(str);
    }

    public static JSONObject constructResponse(int flag, String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flag", flag);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;
    }

    public static boolean isPhoneNum(String phone) {
        return isEmpty(phone) ? false : phone.matches("^\\d{11}$");
    }

}
