package com.mamahome.application;

public class AddEnquiryRequest {

    private String project_id;
    private String A_contact;
    private String quantity;
    private String brand;
    private String main_category;
    private String sub_category;
    private String notes;
    private String generated_by;
    private String requirement_date;
    private String location;

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
}
