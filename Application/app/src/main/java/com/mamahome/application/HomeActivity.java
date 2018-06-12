package com.mamahome.application;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = (DrawerLayout) findViewById(R.id.navDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left, R.anim.slide_in_left,
                R.anim.slide_out_right);
        fragmentTransaction.add(R.id.home_container, new HomeFragment(), "HOME_FRAGMENT");
        fragmentTransaction.addToBackStack("BS_HOME");
        fragmentTransaction.commit();

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.design_navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_id:
                        HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
                        if(homeFragment != null && homeFragment.isVisible() == false) {
                            drawerLayout.closeDrawers();
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                                    R.anim.slide_out_left, R.anim.slide_in_left,
                                    R.anim.slide_out_right);
                            fragmentTransaction.replace(R.id.home_container, new HomeFragment(), "HOME_FRAGMENT");
                            fragmentTransaction.addToBackStack(null);
                            item.setChecked(true);
                            fragmentTransaction.commit();
                            break;
                        }
                        else {
                            drawerLayout.closeDrawers();
                            break;
                        }

                    case R.id.projects_id:
                        drawerLayout.closeDrawers();
                        /*ProjectsFragment projectsFragment = (ProjectsFragment) getSupportFragmentManager().findFragmentByTag("PROJECTS_FRAGMENT");
                        if(projectsFragment != null && projectsFragment.isVisible() == false) {*/
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left, R.anim.slide_in_left,
                                R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.home_container, new ProjectsFragment(), "PROJECTS_FRAGMENT");
                        fragmentTransaction.addToBackStack("BS_PROJECTS");
                        item.setChecked(true);
                        fragmentTransaction.commit();
                        break;
                        /*}
                        else {
                            drawerLayout.closeDrawers();
                            break;
                        }*/

                    case R.id.enquiries_id:
                        drawerLayout.closeDrawers();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left, R.anim.slide_in_left,
                                R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.home_container, new EnquiriesFragment(), "ENQUIRIES_FRAGMENT");
                        fragmentTransaction.addToBackStack("BS_ENQUIRIES");
                        item.setChecked(true);
                        fragmentTransaction.commit();
                        break;

                    case R.id.orders_id:
                        drawerLayout.closeDrawers();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left, R.anim.slide_in_left,
                                R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.home_container, new OrdersFragment(), "ORDERS_FRAGMENT");
                        fragmentTransaction.addToBackStack("BS_ORDERS");
                        item.setChecked(true);
                        fragmentTransaction.commit();
                        break;

                    case R.id.customersupport_id:
                        drawerLayout.closeDrawers();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left, R.anim.slide_in_left,
                                R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.home_container, new CustomerSupportFragment(), "CUSTOMER_SUPPORT_FRAGMENT");
                        fragmentTransaction.addToBackStack("BS_CUSTOMERSUPPORT");
                        item.setChecked(true);
                        fragmentTransaction.commit();
                        break;

                    case R.id.logout_id:
                        drawerLayout.closeDrawers();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("USER_LOGGED_IN", false);
                        editor.apply();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.rateus_id:
                        drawerLayout.closeDrawers();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left, R.anim.slide_in_left,
                                R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.home_container, new RateUsFragment(), "RATE_US_FRAGMENT");
                        fragmentTransaction.addToBackStack("BS_RATEUS");
                        item.setChecked(true);
                        fragmentTransaction.commit();
                        break;

                }
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

    @Override
    public void onBackPressed() {

        HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
        if(homeFragment != null && homeFragment.isVisible()){
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("EXIT MAMAHOME APP")
                    .setMessage("Are you sure?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setCancelable(false)
                    .show();
        }
        else {
            super.onBackPressed();
        }
    }



}
