package com.mamahome.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("req_id")
    @Expose
    private Object reqId;
    @SerializedName("main_category")
    @Expose
    private String mainCategory;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("sub_category")
    @Expose
    private String subCategory;
    @SerializedName("material_spec")
    @Expose
    private String materialSpec;
    @SerializedName("referral_image1")
    @Expose
    private String referralImage1;
    @SerializedName("referral_image2")
    @Expose
    private String referralImage2;
    @SerializedName("requirement_date")
    @Expose
    private String requirementDate;
    @SerializedName("measurement_unit")
    @Expose
    private String measurementUnit;
    @SerializedName("unit_price")
    @Expose
    private String unitPrice;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Truck")
    @Expose
    private Object truck;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("delivery_status")
    @Expose
    private String deliveryStatus;
    @SerializedName("dispatch_status")
    @Expose
    private String dispatchStatus;
    @SerializedName("generated_by")
    @Expose
    private String generatedBy;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("delivery_boy")
    @Expose
    private Object deliveryBoy;
    @SerializedName("delivered_on")
    @Expose
    private Object deliveredOn;
    @SerializedName("payment_mode")
    @Expose
    private Object paymentMode;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Object getReqId() {
        return reqId;
    }

    public void setReqId(Object reqId) {
        this.reqId = reqId;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getMaterialSpec() {
        return materialSpec;
    }

    public void setMaterialSpec(String materialSpec) {
        this.materialSpec = materialSpec;
    }

    public String getReferralImage1() {
        return referralImage1;
    }

    public void setReferralImage1(String referralImage1) {
        this.referralImage1 = referralImage1;
    }

    public String getReferralImage2() {
        return referralImage2;
    }

    public void setReferralImage2(String referralImage2) {
        this.referralImage2 = referralImage2;
    }

    public String getRequirementDate() {
        return requirementDate;
    }

    public void setRequirementDate(String requirementDate) {
        this.requirementDate = requirementDate;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getTruck() {
        return truck;
    }

    public void setTruck(Object truck) {
        this.truck = truck;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Object getDeliveryBoy() {
        return deliveryBoy;
    }

    public void setDeliveryBoy(Object deliveryBoy) {
        this.deliveryBoy = deliveryBoy;
    }

    public Object getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(Object deliveredOn) {
        this.deliveredOn = deliveredOn;
    }

    public Object getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Object paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}