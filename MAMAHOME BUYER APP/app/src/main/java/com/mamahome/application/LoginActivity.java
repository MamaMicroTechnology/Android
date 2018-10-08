package com.mamahome.application;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    TextView btn_signup;
    Button btn_login;
    EditText et_username;
    EditText et_password;
    String emailPhone, password;
    String ROOT_URL = "https://mamahome360.com";
    APIKeys APIKeys;
    ImageView iv_logoMama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);





        iv_logoMama = findViewById(R.id.iv_logoMama);
        et_username = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        Drawable myIcon = getResources().getDrawable( R.drawable.mama_vec_logo );
        iv_logoMama.setImageDrawable(myIcon);

        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setText(Html.fromHtml("Don't Have An Account? <b>Click Here</b> To Sign Up"));
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        btn_login = findViewById(R.id.btn_login);
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

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ProjectsFragment.ROOT_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(selfSigningClientBuilder.createClient(getApplicationContext()))
                        .build();
                APIKeys = retrofit.create(APIKeys.class);
                doLogin();
            }
        });

        runTimePermissions();

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
                    String phoneNumber = response.body().getPhoneNumber();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("USER_LOGGED_IN", true);
                    editor.putString("USER_ID", UserID);
                    editor.putString("USER_NAME", UserName);
                    editor.putString("USER_NUMBER", phoneNumber);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                //btn_login.setEnabled(true);
            }
            else{
                runTimePermissions();
            }
        }
    }

    private boolean runTimePermissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest
                .permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                    .ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }


}
