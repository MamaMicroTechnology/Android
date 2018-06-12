package com.mamahome.application;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEnquiryFragment extends Fragment {

    View view;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    public AddEnquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_enquiry, container, false);

        /*drawerLayout = (DrawerLayout) view.findViewById(R.id.navDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) view.findViewById(R.id.design_navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_id:
                        AddEnquiryFragment addEnquiryFragment = (AddEnquiryFragment) getFragmentManager().findFragmentByTag("ADD_ENQUIRY_FRAGMENT");
                        if(addEnquiryFragment != null && addEnquiryFragment.isVisible()) {

                        }
                        break;
                        }

                        }
            return true;
        });*/


        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Make New Enquiry");
        return view;
    }

    /*@Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left, R.anim.slide_in_left,
                            R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.home_container, new EnquiriesFragment(), "ENQUIRIES_FRAGMENT");
                    fragmentTransaction.addToBackStack("BS_ENQUIRIES");
                    fragmentTransaction.commit();
                }
                return false;
            }
        });
    }*/
}
