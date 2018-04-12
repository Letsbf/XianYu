package com.ryan.xianyu.common;


import com.alibaba.fastjson.JSONObject;
import com.ryan.xianyu.domain.Commodity;
import com.ryan.xianyu.domain.User;
import sun.misc.BASE64Encoder;
import sun.misc.OSEnvironment;

import java.io.*;
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


    public static String saveAvatar(User user) throws Exception{
        String path = System.getProperty("user.dir");
        String image = user.getAvatar();
        if (isEmpty(image)) {
            String avatar = path + "/default/avatar.jpg";
            user.setAvatar(avatar);
            return avatar;
        }
        String avatar = path + "/" + user.getId() + "/avatar";
        save(avatar, image);
        user.setAvatar(avatar);
        return avatar;
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
            String dirPath = path + "/" + commodity.getPublisher();
            File file = new File(dirPath);
            if (!file.isDirectory()) {
                file.mkdir();
            }
            String opath = dirPath + "/" + id + "_" + i;
            save(opath, s);
            sb.append(opath + ";");
        }
        sb.deleteCharAt(sb.length() - 1);
        commodity.setImages(sb.toString());
        return commodity.getImages();
    }

    public static void save(String opath, String image) throws Exception{
        File f = new File(opath);
        System.out.println(opath);

        if (!f.exists()) {
            f.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(f);
        outputStream.write(image.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public static String readImages(String imagePath) throws Exception {
        if (imagePath == null) {
            return "";
        }
        String[] imagePaths = imagePath.split(";");
        StringBuilder sb = new StringBuilder("");
        for (String path : imagePaths) {
            File file = new File(path);
            if (!file.exists()) {
                continue;
            }
            InputStream inputStream = new FileInputStream(file);
            byte[] content = new byte[new Long(file.length()).intValue()];
            inputStream.read(content);
            sb.append(new String(content) + ";");
        }
        System.out.println(sb.toString());
        if (sb.length() > 0) {
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
        return "";
    }


    public static void main(String[] args) {
        System.out.println(isPhoneNum("17611173306"));

//        try {
//            Commodity commodity = new Commodity();
//            commodity.setId(123);
//            commodity.setPublisher(456);
//            commodity.setImages(new String(Base64.getEncoder().encode(new String("eedskjdhaksdkashdjkashjkdahskjdhaksjdhkasj").getBytes())));
//            saveImages(commodity);
//            readImages(commodity.getImages());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        User user = new User();
//        user.setAvatar("blablablablabl");
//        user.setId(456);
//
//        try {
//            saveAvatar(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
