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
    private PrefManager prefManager;
    Animation toTop;
    Animation fadeout;
    Animation growbig;
    Animation growbig1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        iv_logo = (ImageView) findViewById(R.id.iv_logoMama);
        iv_building = (ImageView) findViewById(R.id.iv_building);
        cl_splashscreeen = (ConstraintLayout) findViewById(R.id.activity_launcher_splash_screen);

        toTop = AnimationUtils.loadAnimation(getBaseContext(), R.anim.totop);
        fadeout = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
        growbig = AnimationUtils.loadAnimation(getBaseContext(), R.anim.growbig);
        growbig1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.growbig);
        doAnimation();
    }

    private void launchWelcomeScreen() {
        startActivity(new Intent(SplashScreenActivity.this, WelcomeActivity.class));
        finish();
    }

    private void doAnimation(){
        iv_logo.startAnimation(growbig);

        growbig.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iv_building.startAnimation(growbig1);
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

                prefManager = new PrefManager(SplashScreenActivity.this);
                //prefManager.setFirstTimeLaunch(true);
                if (prefManager.isFirstTimeLaunch()) {
                    launchWelcomeScreen();
                    //finish();
                }
                else{
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


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        /*toTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //iv_building.startAnimation(growbig1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/

    }

}
