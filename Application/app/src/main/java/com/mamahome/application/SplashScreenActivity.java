package com.mamahome.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView iv_logo;
    ImageView iv_building;
    ConstraintLayout cl_splashscreeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        iv_logo = (ImageView) findViewById(R.id.iv_logoMama);
        iv_building = (ImageView) findViewById(R.id.iv_building);
        cl_splashscreeen = (ConstraintLayout) findViewById(R.id.activity_launcher_splash_screen);

        final Animation toTop = AnimationUtils.loadAnimation(getBaseContext(), R.anim.totop);
        final Animation fadeout = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
        final Animation growbig = AnimationUtils.loadAnimation(getBaseContext(), R.anim.growbig);
        final Animation growbig1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.growbig);

        iv_logo.startAnimation(growbig);

        growbig.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iv_building.startAnimation(toTop);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cl_splashscreeen.startAnimation(fadeout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences prefs = getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
                Boolean check_user_status = prefs.getBoolean("USER_LOGGED_IN", false);
                cl_splashscreeen.setVisibility(View.GONE);
                if(check_user_status.equals(true)){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (check_user_status.equals(false)){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        toTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iv_building.startAnimation(growbig1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
