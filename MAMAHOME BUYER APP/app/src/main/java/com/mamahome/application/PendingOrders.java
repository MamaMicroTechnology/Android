package com.mamahome.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingOrders {

    @SerializedName("order")
    @Expose
    private Order[] PendingOrders;


    public Order[] getPendingOrders() {
        return PendingOrders;
    }

    public void setPendingOrders(Order[] pendingOrders) {
        PendingOrders = pendingOrders;
    }
}
