package com.mamahome.application;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    View view;

    public OrdersFragment() {
        // Required empty public constructor
    }
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int tab_count = 2;
    FloatingActionButton fbt_addOrder;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs_orders);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_orders);

        viewPager.setAdapter(new OrdersTabAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        fbt_addOrder = (FloatingActionButton) view.findViewById(R.id.fbt_addOrder);
        fbt_addOrder.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_add_white));

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Orders");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getFragmentManager().popBackStack("BS_HOME", 0);
                    return true;
                }
                return false;
            }
        });
    }

}
