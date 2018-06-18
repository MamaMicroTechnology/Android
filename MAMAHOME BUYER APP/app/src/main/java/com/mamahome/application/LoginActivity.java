package com.mamahome.application;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button btn_signup;
    Button btn_login;
    EditText et_username;
    EditText et_password;
    String emailPhone, password;
    String ROOT_URL = "http://mamahome360.com";
    APIKeys APIKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        et_username = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validations
                emailPhone = et_username.getText().toString();
                password = et_password.getText().toString();

                if (TextUtils.isEmpty(emailPhone)) {
                    et_username.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    et_password.setError(getString(R.string.cannot_empty));
                    return;
                }

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ROOT_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                APIKeys = retrofit.create(APIKeys.class);
                doLogin();
            }
        });

    }

    private void doLogin(){
        Call<LoginResponse> loginResponseCall = APIKeys.getLoginAccess(emailPhone, password);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body().getMessage().equals("false")){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Wrong Credentials")
                            .setMessage("Please check you credentials and try again!")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setCancelable(false)
                            .show();
                    Toast.makeText(getApplicationContext(), "Wrong Credentials, Try Again", Toast.LENGTH_LONG).show();
                }
                else if(response.body().getMessage().equals("true")){
                    String UserID = response.body().getUserid();
                    String UserName = response.body().getUserName();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("USER_LOGGED_IN", true);
                    editor.putString("USER_ID", UserID);
                    editor.putString("USER_NAME", UserName);
                    editor.apply();
                    Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(home);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();
            }
        });

    }


}
