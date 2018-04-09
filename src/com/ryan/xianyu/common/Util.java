package com.ryan.xianyu.common;


import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.domain.Commodity;
import sun.misc.BASE64Encoder;
import sun.misc.OSEnvironment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

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


    public static String saveImages(Commodity commodity) throws Exception{
        String path = System.getProperty("user.dir");

        String images = commodity.getImages();
        Integer id = commodity.getId();
        StringBuilder sb = new StringBuilder();

        String[] imagesBase64 = images.split(";");
        int i = 0;
        for (String s : imagesBase64) {
            i++;
            String image = s;
            String opath = path + "/" + id + "_" + i;
            File f = new File(opath);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(f);
            outputStream.write(image.getBytes());
            outputStream.flush();
            outputStream.close();
            sb.append(opath + ";");
        }
        sb.deleteCharAt(sb.length() - 1);
        commodity.setImages(sb.toString());
        return commodity.getImages();
    }

    public static void main(String[] args) {
        Commodity commodity = new Commodity();
        commodity.setId(123);
        commodity.setPublisher(456);
        commodity.setImages(new String(Base64.getEncoder().encode(new String("tedskjdhaksdkashdjkashjkdahskjdhaksjdhkasj").getBytes())));
        try {
            saveImages(commodity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
