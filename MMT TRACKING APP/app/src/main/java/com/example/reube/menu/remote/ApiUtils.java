package com.example.reube.menu.remote;

public class ApiUtils {
    public static final String BASE_URL = "http://mamahome360.com/webapp/api/";
//    public static final String BASE_URL = "http://10.156.109.166:8000/api/";
    public static UserService getUserService(){
        return com.example.reube.menu.remote.RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}
