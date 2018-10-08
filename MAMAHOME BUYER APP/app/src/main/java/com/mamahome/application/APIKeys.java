package com.mamahome.application;

import com.mamahome.application.Products.ProductList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIKeys {

    @POST("webapp/api/getregister")
    Call<SignUpResponse> getSignInAccess(@Body SignUpRequest signUpRequest);

    @POST("webapp/api/addProject")
    Call<AddProjectResponse> Addproject(@Body AddProjectRequest addProjectRequest);

    @POST("webapp/api/updateProject")
    Call<AddProjectResponse> Updateproject(@Body AddProjectRequest addProjectRequest);

    @POST("webapp/api/addenquiry")
    Call<AddEnquiryResponse> Addenquiry(@Body AddEnquiryRequest addEnquiryRequest);

    @POST("webapp/api/updateEnquiry")
    Call<AddEnquiryResponse> updateEnquiry(@Body AddEnquiryRequest addEnquiryRequest);

    @GET("webapp/api/buyerlogin")
    Call<LoginResponse> getLoginAccess (@Query("email") String Email, @Query("password") String Password);

    @GET("webapp/api/getproject")
    Call<ProjectsList> viewProject (@Query("user_id") String User_ID);

    @GET("webapp/api/confirmorders")
    Call<ConfirmedOrders> ConfirmedOrders (@Query("userid") String User_ID);

    @GET("webapp/api/pending")
    Call<PendingOrders> PendingOrders (@Query("userid") String User_ID);

    @GET("webapp/api/getenq")
    Call<EnquiriesList> viewEnquiries (@Query("project_id") String project_id);

    @GET("webapp/api/brand")
    Call<ProductList> PRODUCT_LIST_CALL();

    @GET("webapp/api/banner")
    Call<BannerList> BANNER_LIST_CALL();
}
