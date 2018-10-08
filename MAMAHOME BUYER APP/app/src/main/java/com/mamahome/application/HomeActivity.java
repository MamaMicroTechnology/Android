package com.mamahome.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    FragmentTransaction fragmentTransaction;
    NavigationView nv_home;
    TextView tv_name;
    String Home = "HOME", Projects = "PROJECTS", Enquiry = "ENQUIRY", Customer_Support = "CUSTOMER_SUPPORT",
    Orders = "ORDERS", Rate_Us = "RATE_US", HomeR = "HOME_REPLACE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //setup of Navigation Drawer

        drawerLayout = (DrawerLayout) findViewById(R.id.navDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);


        //FragmentManager fragmentManager =
        doFragmentTransactions(Home);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        SharedPreferences prefs = getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
        String userName = prefs.getString("USER_NAME", null);
        String phoneNumber = prefs.getString("USER_NUMBER", null);

        nv_home = findViewById(R.id.nv_home);
        View header = nv_home.getHeaderView(0);
        tv_name = header.findViewById(R.id.tv_name);
        tv_name.setText("Hi, "+ userName);
        //tv_name.setText("Hi, "+ userName + "\n" + phoneNumber);

        nv_home.setCheckedItem(R.id.home_id);

        nv_home.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_id:
                        HomeNewFragment homeFragment = (HomeNewFragment) getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
                        if(homeFragment != null && !homeFragment.isVisible()) {
                            drawerLayout.closeDrawers();
                            item.setChecked(true);
                            doFragmentTransactions(HomeR);
                            break;
                        }
                        else {
                            drawerLayout.closeDrawers();
                            break;
                        }

                    case R.id.projects_id:
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        doFragmentTransactions(Projects);
                        break;

                    case R.id.enquiries_id:
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        doFragmentTransactions(Enquiry);
                        break;

                    case R.id.orders_id:
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        doFragmentTransactions(Orders);
                        break;

                    case R.id.customersupport_id:
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        doFragmentTransactions(Customer_Support);
                        break;

                    case R.id.logout_id:
                        drawerLayout.closeDrawers();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("USER_LOGGED_IN", false);
                        editor.putString("USER_ID", "");
                        editor.putString("USER_NAME", "");
                        editor.putString("USER_NUMBER", "");
                        editor.apply();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.rateus_id:
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        doFragmentTransactions(Rate_Us);
                        break;

                }
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable toolbar = getResources().getDrawable(R.drawable.toolbar_background);
        getSupportActionBar().setBackgroundDrawable(toolbar);
        getSupportActionBar().setElevation(20);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void doFragmentTransactions(String Fragment){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left, R.anim.slide_in_left,
                R.anim.slide_out_right);
        if(Fragment.equals(Home)){
            fragmentTransaction.replace(R.id.home_container, new HomeNewFragment(), "HOME_FRAGMENT");
            fragmentTransaction.addToBackStack("BS_HOME");
        }
        else if(Fragment.equals(Projects)){
            fragmentTransaction.replace(R.id.home_container, new ProjectsFragment(), "PROJECTS_FRAGMENT");
            fragmentTransaction.addToBackStack("BS_PROJECTS");
        }
        else if(Fragment.equals(Enquiry)){
            fragmentTransaction.replace(R.id.home_container, new EnquiriesFragment(), "ENQUIRIES_FRAGMENT");
            fragmentTransaction.addToBackStack("BS_ENQUIRIES");
        }
        else if(Fragment.equals(Orders)){
            fragmentTransaction.replace(R.id.home_container, new OrdersFragment(), "ORDERS_FRAGMENT");
            fragmentTransaction.addToBackStack("BS_ORDERS");
        }
        else if(Fragment.equals(Customer_Support)){
            fragmentTransaction.replace(R.id.home_container, new CustomerSupportFragment(), "CUSTOMER_SUPPORT_FRAGMENT");
            fragmentTransaction.addToBackStack("BS_CUSTOMERSUPPORT");
        }
        else if(Fragment.equals(Rate_Us)){
            fragmentTransaction.replace(R.id.home_container, new RateUsFragment(), "RATE_US_FRAGMENT");
            fragmentTransaction.addToBackStack("BS_RATEUS");
        }
        else if(Fragment.equals(HomeR)){
            fragmentTransaction.replace(R.id.home_container, new HomeNewFragment(), "HOME_FRAGMENT");
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void MarkHomeItemSelected(int id){
        nv_home.getMenu().getItem(id).setChecked(true);
        /*nv_home.post(new Runnable() {
            @Override
            public void run() {
                nv_home.setCheckedItem(R.id.home_id);
            }
        });*/

    }

    /*@Override
    public void onBackPressed() {
        HomeNewFragment homeFragment = (HomeNewFragment)getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
        if(homeFragment == null && homeFragment.isVisible()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do You Want To Exit?")
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
        else{
            super.onBackPressed();
        }
    }*/
}
