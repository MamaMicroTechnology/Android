package com.example.reube.menu.model;

public class ResObj {
    public String getMessage() {
        return message;
    }
    public void setMessgae(String messsgae) {
        this.message = message;
    }

    public String getUserId() {
        return userid;
    }
    public void setUserId(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
    private String message;
    private String userid;
}
