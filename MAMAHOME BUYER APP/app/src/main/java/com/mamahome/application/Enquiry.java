package com.mamahome.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Enquiry {

    @SerializedName("project_id")
    @Expose
    private String project_id;
    @SerializedName("A_contact")
    @Expose
    private String A_contact;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("main_category")
    @Expose
    private String main_category;
    @SerializedName("sub_category")
    @Expose
    private String sub_category;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("generated_by")
    @Expose
    private String generated_by;
    @SerializedName("requirement_date")
    @Expose
    private String requirement_date;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("status")
    @Expose
    private String status;

    public Enquiry(String project_id, String a_contact, String quantity, String brand, String main_category, String sub_category, String notes, String generated_by, String requirement_date, String location, String status) {
        this.project_id = project_id;
        this.A_contact = a_contact;
        this.quantity = quantity;
        this.brand = brand;
        this.main_category = main_category;
        this.sub_category = sub_category;
        this.notes = notes;
        this.generated_by = generated_by;
        this.requirement_date = requirement_date;
        this.location = location;
        this.status = status;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getA_contact() {
        return A_contact;
    }

    public void setA_contact(String a_contact) {
        A_contact = a_contact;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getGenerated_by() {
        return generated_by;
    }

    public void setGenerated_by(String generated_by) {
        this.generated_by = generated_by;
    }

    public String getRequirement_date() {
        return requirement_date;
    }

    public void setRequirement_date(String requirement_date) {
        this.requirement_date = requirement_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
