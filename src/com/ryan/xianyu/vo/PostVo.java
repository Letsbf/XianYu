package com.ryan.xianyu.vo;

public class PostVo {

    private Integer id;
    private String text;
    private Integer replyPostId;
    private String replier;
    private String replierAvatar;
    private Integer replierIsAdmin;
    private Integer replierId;
    private Long time;

    public Integer getReplierId() {
        return replierId;
    }

    public void setReplierId(Integer replierId) {
        this.replierId = replierId;
    }

    public String getReplierAvatar() {
        return replierAvatar;
    }

    public void setReplierAvatar(String replierAvatar) {
        this.replierAvatar = replierAvatar;
    }

    public Integer getReplierIsAdmin() {
        return replierIsAdmin;
    }

    public void setReplierIsAdmin(Integer replierIsAdmin) {
        this.replierIsAdmin = replierIsAdmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getReplyPostId() {
        return replyPostId;
    }

    public void setReplyPostId(Integer replyPostId) {
        this.replyPostId = replyPostId;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
