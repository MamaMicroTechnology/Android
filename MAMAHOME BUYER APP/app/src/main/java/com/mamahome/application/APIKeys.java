package com.mamahome.application;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIKeys {
    @POST("/webapp/api/getregister")
    Call<SignUpResponse> getSignInAccess(@Body SignUpRequest signUpRequest);

    @POST("/webapp/api/addProject")
    Call<AddProjectResponse> Addproject(@Body AddProjectRequest addProjectRequest);

    @GET("/webapp/api/login")
    Call<LoginResponse> getLoginAccess (@Query("email") String Email, @Query("password") String Password);
}
