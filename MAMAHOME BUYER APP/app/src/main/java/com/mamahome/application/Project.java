package com.mamahome.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Project {

    @SerializedName("project_id")
    @Expose
    private String project_id;

    @SerializedName("project_name")
    @Expose
    private String project_name;
    @SerializedName("road_name")
    @Expose
    private String road_name;
    @SerializedName("road_width")
    @Expose
    private String road_width;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("construction_type")
    @Expose
    private String construction_type;
    @SerializedName("interested_in_rmc")
    @Expose
    private String interested_in_rmc;
    @SerializedName("interested_in_loan")
    @Expose
    private String interested_in_loan;
    @SerializedName("interested_in_doorsandwindows")
    @Expose
    private String interested_in_doorsandwindows;
    @SerializedName("municipality_approval")
    @Expose
    private String municipality_approval;
    @SerializedName("project_status")
    @Expose
    private String project_status;
    @SerializedName("plotsize")
    @Expose
    private String plotsize;
    @SerializedName("project_type")
    @Expose
    private String project_type;
    @SerializedName("project_size")
    @Expose
    private String project_size;
    @SerializedName("budgetType")
    @Expose
    private String budgetType;
    @SerializedName("budget")
    @Expose
    private String budget;
    @SerializedName("basement")
    @Expose
    private String basement;
    @SerializedName("ground")
    @Expose
    private String ground;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("breadth")
    @Expose
    private String breadth;


    public Project(String project_name, String road_name, String road_width, String address, String construction_type, String interested_in_rmc, String interested_in_loan, String interested_in_doorsandwindows, String municipality_approval, String project_status, String plotsize, String project_type, String project_size, String budgetType, String budget, String basement, String ground, String latitude, String longitude, String image, String user_id) {
        this.project_name = project_name;
        this.road_name = road_name;
        this.road_width = road_width;
        this.address = address;
        this.construction_type = construction_type;
        this.interested_in_rmc = interested_in_rmc;
        this.interested_in_loan = interested_in_loan;
        this.interested_in_doorsandwindows = interested_in_doorsandwindows;
        this.municipality_approval = municipality_approval;
        this.project_status = project_status;
        this.plotsize = plotsize;
        this.project_type = project_type;
        this.project_size = project_size;
        this.budgetType = budgetType;
        this.budget = budget;
        this.basement = basement;
        this.ground = ground;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.user_id = user_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getRoad_name() {
        return road_name;
    }

    public void setRoad_name(String road_name) {
        this.road_name = road_name;
    }

    public String getRoad_width() {
        return road_width;
    }

    public void setRoad_width(String road_width) {
        this.road_width = road_width;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConstruction_type() {
        return construction_type;
    }

    public void setConstruction_type(String construction_type) {
        this.construction_type = construction_type;
    }

    public String getInterested_in_rmc() {
        return interested_in_rmc;
    }

    public void setInterested_in_rmc(String interested_in_rmc) {
        this.interested_in_rmc = interested_in_rmc;
    }

    public String getInterested_in_loan() {
        return interested_in_loan;
    }

    public void setInterested_in_loan(String interested_in_loan) {
        this.interested_in_loan = interested_in_loan;
    }

    public String getInterested_in_doorsandwindows() {
        return interested_in_doorsandwindows;
    }

    public void setInterested_in_doorsandwindows(String interested_in_doorsandwindows) {
        this.interested_in_doorsandwindows = interested_in_doorsandwindows;
    }

    public String getMunicipality_approval() {
        return municipality_approval;
    }

    public void setMunicipality_approval(String municipality_approval) {
        this.municipality_approval = municipality_approval;
    }

    public String getProject_status() {
        return project_status;
    }

    public void setProject_status(String project_status) {
        this.project_status = project_status;
    }

    public String getPlotsize() {
        return plotsize;
    }

    public void setPlotsize(String plotsize) {
        this.plotsize = plotsize;
    }

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public String getProject_size() {
        return project_size;
    }

    public void setProject_size(String project_size) {
        this.project_size = project_size;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBasement() {
        return basement;
    }

    public void setBasement(String basement) {
        this.basement = basement;
    }

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getBreadth() {
        return breadth;
    }

    public void setBreadth(String breadth) {
        this.breadth = breadth;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
}
