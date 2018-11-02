package com.mamahome.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerList {

    @SerializedName("banner")
    @Expose
    private Banner[] banner;

    public Banner[] getBanner() {
        return banner;
    }

    public void setBanner(Banner[] banner) {
        this.banner = banner;
    }
}
