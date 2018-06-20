package com.mamahome.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProjectResponse {
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }
}
