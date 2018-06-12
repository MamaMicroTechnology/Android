package com.example.reube.menu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reube.menu.model.ResObj;
import com.example.reube.menu.remote.ApiUtils;
import com.example.reube.menu.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_activity extends Activity {
    EditText email;
    EditText password;
    Button login;
    UserService userService;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
        Boolean check_user_status = prefs.getBoolean("USER_LOGGED_IN", false);
        if(check_user_status.equals(true)){
            Intent intent = new Intent(login_activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.login);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        userService = ApiUtils.getUserService();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = email.getText().toString();
                String userpassword = password.getText().toString();
                if(validateLogin(useremail,userpassword)){
                    doLogin(useremail,userpassword);
                }
            }
        });



    }




    private boolean validateLogin(String email, String password){
        if(email == null || email.trim().length() == 0){
            Toast.makeText(this,"Pleas fill in email id",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this,"Pleas enter your password",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void doLogin(String email, String password){
        Call<ResObj> call = userService.login(email,password);
        call.enqueue(new Callback<ResObj>() {
            @Override
            public void onResponse(Call<ResObj> call, Response<ResObj> response) {
//                Toast.makeText(login_activity.this,""+response.toString(),Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()){
                    ResObj resObj = response.body();
                    if(resObj.getMessage().equals("true")){
                        Intent intent = new Intent(login_activity.this, MainActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("userid", resObj.getUserId());
                        extras.putString("userName", resObj.getUserName());
//                        intent.putExtra("userid", resObj.getUserId());
//                        intent.putExtra("userName",resObj.getUserName());
                        intent.putExtras(extras);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("USER_LOGGED_IN", true);
                        editor.putString("USER_NAME", resObj.getUserName());
                        editor.putString("USER_ID", resObj.getUserId());
                        editor.apply();
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(login_activity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(login_activity.this,"Error! Please try again later",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResObj> call, Throwable t) {
                Toast.makeText(login_activity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if request is cancelled, the result arrays are empty.
                } else {
//    permission denied
                }
                return;
            }
        }
    }
}
