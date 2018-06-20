package com.mamahome.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    EditText et_Name, et_Email, et_Phone, et_Password;
    RadioGroup rg_userType;
    RadioButton radioButton;
    Button btn_signUp;
    String ROOT_URL = "http://mamahome360.com";
    String Name, Email, Phone, Password, UserType;
    APIKeys APIKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        et_Name = (EditText)findViewById(R.id.et_name);
        et_Email = (EditText) findViewById(R.id.et_email);
        et_Phone = (EditText) findViewById(R.id.et_phoneNumber);
        et_Password = (EditText) findViewById(R.id.et_password);
        rg_userType = (RadioGroup) findViewById(R.id.rg_usertype);

        btn_signUp = (Button) findViewById(R.id.btn_signup);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = et_Name.getText().toString();
                Email = et_Email.getText().toString();
                Phone = et_Phone.getText().toString();
                Password = et_Password.getText().toString();
                checkUserType();


                if (TextUtils.isEmpty(Name)) {
                    et_Name.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Phone)) {
                    et_Phone.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Email)) {
                    et_Email.setError(getString(R.string.cannot_empty));
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    et_Password.setError(getString(R.string.cannot_empty));
                    return;
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ROOT_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                APIKeys = retrofit.create(APIKeys.class);
                AddUser();

                //insertUser();
            }
        });

    }

    public void AddUser(){
        SignUpRequest signUpRequest = new SignUpRequest();

        signUpRequest.setName(Name);
        signUpRequest.setEmail(Email);
        signUpRequest.setNumber(Phone);
        signUpRequest.setPassword(Password);
        signUpRequest.setCategory(UserType);

        Call<SignUpResponse> signUpResponseCall = APIKeys.getSignInAccess(signUpRequest);

        signUpResponseCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                int statusCode = response.code();

                SignUpResponse signUpResponse = response.body();
                String APIresponse = response.body().getMessage();

                if (APIresponse.equals("This email/phone number has already been used."))
                {
                    Toast.makeText(getApplicationContext(), "Response: " +APIresponse, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "on Success " + statusCode , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }



            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkUserType(){
        int radiobutton = rg_userType.getCheckedRadioButtonId();
        radioButton = findViewById(radiobutton);
        UserType = radioButton.getText().toString();
    }
}
