package com.mamahome.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnquiriesList {

    @SerializedName("EnqDetails")
    @Expose
    private Enquiry[] enquiries;

    public Enquiry[] getEnquiries() {
        return enquiries;
    }

    public void setEnquiries(Enquiry[] enquiries) {
        this.enquiries = enquiries;
    }
}
