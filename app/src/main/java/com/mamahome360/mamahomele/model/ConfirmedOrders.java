package com.mamahome.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmedOrders {

    @SerializedName("order")
    @Expose
    private Order[] ConfirmedOrders;

    public Order[] getConfirmedOrders() {
        return ConfirmedOrders;
    }

    public void setConfirmedOrders(Order[] confirmedOrders) {
        ConfirmedOrders = confirmedOrders;
    }
}
