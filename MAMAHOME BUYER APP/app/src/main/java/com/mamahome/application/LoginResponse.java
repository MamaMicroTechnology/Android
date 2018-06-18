package com.mamahome.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("userName")
    @Expose
    private String userName;

    public String getMessage() {
        return message;
    }

    public String getUserid() {
        return userid;
    }

    public String getUserName() {
        return userName;
    }
}
