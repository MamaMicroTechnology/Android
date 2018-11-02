package com.mamahome360.mamahomele.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mamahome360.mamahomele.R;
import com.mamahome360.mamahomele.model.ResObj;
import com.mamahome360.mamahomele.remote.LoginApiUtils;
import com.mamahome360.mamahomele.remote.UserService;
import com.mamahome360.mamahomele.utils.Location_ForeGround_Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  LoginActivity extends AppCompatActivity {
    @BindView(R.id.btn_login)
    Button bt_Login;
    @BindView(R.id.input_email)
    EditText et_email;
    @BindView(R.id.input_password)
    EditText et_password;
    String username;
    String password;
    String login_time;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fabric.with(this, new Crashlytics());

       // bt_Login = findViewById(R.id.btn_login);
        ButterKnife.bind(this);
        bt_Login.setEnabled(false);
        if (!runTimePermissions()){
            bt_Login.setEnabled(true);
        };
        userService = LoginApiUtils.getUserService(getApplicationContext());


        }
@OnClick(R.id.btn_login)
       public void enable_button() {

                /*DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                databaseHelper.addUserLocation("MH1234", "HELP", "12-08-2018");*/
               //Crashlytics.getInstance().crash(); // Force a crash

                username = et_email.getText().toString();
                password = et_password.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                login_time =  mdformat.format(calendar.getTime());
               int[] type ={ConnectivityManager.TYPE_MOBILE,ConnectivityManager.TYPE_WIFI};
               if(isNetWorkAvailable(type)){
                   if(isLocationEnabled(getApplicationContext())) {


                       if (validateLogin(username, password)) {
                           doLogin(username, password,login_time);

                       }
                   }
                   else{
                      // Toast.makeText(getApplicationContext(),"Turn On Gps",Toast.LENGTH_LONG).show();
                       AlertDialog.Builder builder = new AlertDialog.Builder(this);
                       builder.setTitle("Enable GPS");  // GPS not found
                       builder.setMessage("The app needs GPS to be enabled  "); // Want to enable?
                       builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialogInterface, int i) {
                               startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                           }
                       });

                       builder.setCancelable(false);
                       builder.create().show();
                   }

               }
               else{
                   Toast.makeText(getApplicationContext(),"Internet Is Not Working",Toast.LENGTH_LONG).show();
               }

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

    private void doLogin(String email, String password,String login_time){

        Call<ResObj> call = userService.login(email,password,login_time);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("MH_LE");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ResObj>() {
            @Override
            public void onResponse(Call<ResObj> call, Response<ResObj> response) {
//                Toast.makeText(login_activity.this,""+response.toString(),Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()){
                    ResObj resObj = response.body();
                    assert resObj != null;
                    if(Objects.requireNonNull(resObj).getMessage().equals("true")){
                     //   Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                        startService(new Intent(getApplicationContext(), Location_ForeGround_Service.class));
                        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MH_LE_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("USER_LOGGED_IN", true);
                        editor.putString("USER_ID", resObj.getUserid());
                        editor.putString("USER_NAME", resObj.getUserName());
                        editor.putString("WARD_ASSIGNED", resObj.getWardAssigned());
                        editor.putString("LAT_LON", resObj.getLatlon());
                        editor.putString("MODE",resObj.getMode());
                        editor.putBoolean("CHECK_SERVICE",true);
                        editor.apply();
                        startActivity(intent);
//                        Calendar calendar = Calendar.getInstance();
//                        Date date1 = calendar.getTime();
//                        Toast.makeText(getApplicationContext(),  date1.toString(),Toast.LENGTH_LONG).show();
                        Crashlytics.log("LoginActivity:"+resObj.getUserid());
                        finish();
                        progressDoalog.dismiss();
                    }else{
                        progressDoalog.dismiss();
                       Toast.makeText(LoginActivity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDoalog.dismiss();
                    Toast.makeText(LoginActivity.this,"Error! Please try again later",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResObj> call, Throwable t) {
                Toast.makeText(LoginActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
            }
        });
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

    private boolean isNetWorkAvailable(int[] type){
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            for(int typeNetwork:type){
                assert cm != null;
                NetworkInfo networkInfo = cm.getNetworkInfo(typeNetwork);
                if(networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        catch (Exception e){
            return false;
        }
        return false;
    }

    @SuppressLint("ObsoleteSdkInt")
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
               bt_Login.setEnabled(true);
            }
            else{
                runTimePermissions();
            }
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("LoginActivity:","Start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("LoginActivity:","Stop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("LoginActivity:","Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("LoginActivity:","Resume");
    }
}
