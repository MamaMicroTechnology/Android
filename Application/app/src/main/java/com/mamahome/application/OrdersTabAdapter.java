package com.mamahome.application;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.mamahome.application.OrdersFragment.tab_count;

public class OrdersTabAdapter extends FragmentPagerAdapter {

    public OrdersTabAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ConfirmedOrdersFragment();
            case 1:
                return new PendingOrdersFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tab_count;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Confirmed";
            case 1:
                return "Pending";
        }

        return null;
    }
}
