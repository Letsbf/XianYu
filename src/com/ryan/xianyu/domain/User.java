package com.ryan.xianyu.domain;

public class User {

    //表id
    private Integer id;

    private String username;

    private String password;

    private String name;

    private String phone;

    //学院
    private Integer instituteId;

    //学号
    private String stuId;

    private String email;

    //头像
    private String avatar;

    //管理员 1是 0不是
    private boolean admin;

    private Long time;

    public User() {
    }

    public User(Integer id, String username, String name, String phone, Integer instituteId, String stuId, String email, String avatar) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.instituteId = instituteId;
        this.stuId = stuId;
        this.email = email;
        this.avatar = avatar;
    }

    public User(String username, String password, String name, String phone, Integer instituteId, String stuId, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.instituteId = instituteId;
        this.stuId = stuId;
        this.email = email;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Integer instituteId) {
        this.instituteId = instituteId;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
