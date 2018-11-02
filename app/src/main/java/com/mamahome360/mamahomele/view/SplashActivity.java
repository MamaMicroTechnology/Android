package com.mamahome360.mamahomele.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mamahome360.mamahomele.R;
import com.mamahome360.mamahomele.utils.Location_ForeGround_Service;

public class SplashActivity extends AppCompatActivity {
ImageView im_logo;
    ConstraintLayout cl_splashscreeen;
    Animation toTop;
    Animation fadeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        im_logo  = findViewById(R.id.logo);
        cl_splashscreeen = findViewById(R.id.activity_launcher_splash_screen);
        fadeout = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);

        cl_splashscreeen.setAnimation(fadeout);

        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cl_splashscreeen.setVisibility(View.GONE);
                SharedPreferences prefs = getSharedPreferences("MH_LE_DATA", MODE_PRIVATE);
                Boolean check_user_status = prefs.getBoolean("USER_LOGGED_IN", false);
                if(check_user_status.equals(true)){
                    startService(new Intent(getApplicationContext(), Location_ForeGround_Service.class));
                    Intent intent = new Intent(SplashActivity.this,MapActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent in = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(in);
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
