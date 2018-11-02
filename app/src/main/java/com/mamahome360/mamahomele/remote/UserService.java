package com.mamahome360.mamahomele.remote;

import com.mamahome360.mamahomele.model.ResObj;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    // requesiting login authetication and get the LE details
    @GET("tracklogin")
    Call<ResObj> login(@Query("username") String username, @Query("password") String password,@Query("applogintime") String applogintime);

    @GET("gettime")
    Call<ResponseBody> gettime();
    //add current location into the db
    @POST("addLocation")
    @FormUrlEncoded
    Call<ResObj> addLocation(@Field("user_id") String user_id,
                             @Field("lat_long") String lat_long,
                             @Field("time") String time,
                             @Field("date") String date,
                             @Field("kms")String kms);
    //update location into the db
    @POST("updateLocation")
    @FormUrlEncoded
    Call<ResObj> updateLocation(@Field("user_id") String user_id,
                                @Field("lat_long") String lat_long,
                                @Field("time") String time,
                                @Field("kms") String kms,
                                @Field("date") String date);
    //Field Login for LE
    @POST("recordtime")
    @FormUrlEncoded
    Call<ResObj> recordtime(@Field("user_id") String user_id,
                             @Field("logindate") String login_date,
                             @Field("logintime") String login_time,
                             @Field("remark")String remark,
                            @Field("latitude")String latitude,
                            @Field("longitude")String longitude,
                              @Field("address")String address
    );

    //Sending status of Fake Gps
    @GET("fakegps")
    Call<ResObj> fakegps(@Query("user_id") String user_id,@Query("date") String date,@Query("time") String time,@Query("fakegps") String fakegps);
    //Field logout for LE
    @POST("flogout")
    @FormUrlEncoded
    Call<ResObj>flogout(
            @Field("user_id") String user_id,
            @Field("logouttime") String logout_time
    );
    Call tracklogout (@Query("userid") String userid,@Query("tracklogout") String tracklogout);

}
