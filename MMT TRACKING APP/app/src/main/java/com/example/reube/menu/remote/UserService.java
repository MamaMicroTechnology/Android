package com.example.reube.menu.remote;

import com.example.reube.menu.model.ResObj;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {
    @GET("login/{username}/{password}")
    Call<ResObj> login(@Path("username") String username, @Path("password") String password);

    @GET("saveLocation/{userid}/{latitude}/{longitude}")
    Call<ResObj> saveLocation(@Path("userid") String userid, @Path("latitude") String latitude, @Path("longitude") String longitude);
}
