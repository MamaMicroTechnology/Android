package com.mamahome.application;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProjectFragment extends Fragment {

    View view;

    ImageView iv_projectImage,iv_edit, iv_location;

    Glide glide;
    TextView tv_projectName, tv_address, tv_projectStatus, tv_roadName, tv_roadWidth, tv_constructionType,
             tv_RMC, tv_Loans, tv_UPVC, tv_plotSize, tv_basementCount, tv_floorsCount, tv_total_floor_count,
             tv_projectSize, tv_budgetType, tv_budget, tv_RoomsCount, tv_location_latlng, tv_moreInfo;

    LinearLayout ll_moreInfo;

    Animation hide, show;

    String projectId, projectName, address, projectStatus, roadName, roadWidth, constructionType,
           RMC, Loans, UPVC, plotSize, basementCount, floorsCount, total_floor_count,
           projectSize, budgetType, budget, RoomsCount, Latitude, Longitude, projectImage,
           govApproval, length, breadth;

    public ViewProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_project, container, false);
        ButterKnife.bind(this, view);

        tv_moreInfo = view.findViewById(R.id.tv_moreInfo);
        ll_moreInfo = view.findViewById(R.id.ll_moreInfo);
        iv_location = view.findViewById(R.id.iv_location);
        iv_edit = view.findViewById(R.id.iv_edit);
        iv_projectImage = view.findViewById(R.id.iv_projectImage);
        tv_projectName = view.findViewById(R.id.tv_projectName);
        tv_address = view.findViewById(R.id.tv_address);
        tv_projectStatus = view.findViewById(R.id.tv_projectStatus);
        tv_roadName = view.findViewById(R.id.tv_roadName);
        tv_roadWidth = view.findViewById(R.id.tv_roadWidth);
        tv_constructionType = view.findViewById(R.id.tv_constructionType);
        tv_RMC = view.findViewById(R.id.tv_RMC);
        tv_Loans = view.findViewById(R.id.tv_Loans);
        tv_UPVC = view.findViewById(R.id.tv_UPVC);
        tv_plotSize = view.findViewById(R.id.tv_plotSize);
        tv_basementCount = view.findViewById(R.id.tv_basementCount);
        tv_floorsCount = view.findViewById(R.id.tv_floorsCount);
        tv_total_floor_count = view.findViewById(R.id.tv_total_floor_count);
        tv_projectSize = view.findViewById(R.id.tv_projectSize);
        tv_budgetType = view.findViewById(R.id.tv_budgetType);
        tv_budget = view.findViewById(R.id.tv_budget);
        tv_RoomsCount = view.findViewById(R.id.tv_RoomsCount);
        tv_location_latlng = view.findViewById(R.id.tv_location_latlng);

        final Bundle bundle = this.getArguments();
        projectId = bundle.getString("projectId");
        projectName = bundle.getString("projectName");
        address = bundle.getString("address");
        projectStatus = bundle.getString("projectStatus");
        roadName = bundle.getString("roadName");
        roadWidth = bundle.getString("roadWidth");
        constructionType = bundle.getString("constructionType");
        RMC = bundle.getString("RMC");
        Loans = bundle.getString("Loans");
        UPVC = bundle.getString("UPVC");
        plotSize = bundle.getString("plotSize");
        basementCount = bundle.getString("basementCount");
        floorsCount = bundle.getString("floorsCount");
        projectSize = bundle.getString("projectSize");
        budgetType = bundle.getString("budgetType");
        budget = bundle.getString("budget");
        RoomsCount = bundle.getString("RoomsCount");
        Latitude = bundle.getString("Latitude");
        Longitude = bundle.getString("Longitude");
        projectImage = bundle.getString("projectImage");
        govApproval = bundle.getString("govApproval");
        length = bundle.getString("Length");
        breadth = bundle.getString("Breadth");
        if(basementCount != null && floorsCount != null) {
            int i = Integer.parseInt(basementCount) + Integer.parseInt(floorsCount) + 1;
            total_floor_count = String.valueOf(i);
        }

        hide = AnimationUtils.loadAnimation(getContext(), R.anim.layout_hide);
        show = AnimationUtils.loadAnimation(getContext(), R.anim.layout_show);



        putValues();

        tv_moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_moreInfo.getVisibility() == View.GONE){
                    tv_moreInfo.setText("Show Less");
                    ll_moreInfo.startAnimation(show);
                    ll_moreInfo.setVisibility(View.VISIBLE);
                }
                else{
                    tv_moreInfo.setText("Show More");
                    ll_moreInfo.startAnimation(hide);
                    hide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ll_moreInfo.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProjectFragment updateProjectFragment = new UpdateProjectFragment();
                updateProjectFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.home_container, updateProjectFragment);
                fragmentTransaction.addToBackStack("BS_UPDATE_PROJECT");
                fragmentTransaction.commit();
            }
        });


        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("View Project");
        return view;
    }

    public void AnimLisitner(){

    }


    private void putValues(){

        tv_projectName.setText(projectName);
        tv_address.setText(address);
        tv_projectStatus.setText("Status: "+projectStatus);
        tv_roadName.setText("Road Name: "+roadName);
        tv_roadWidth.setText("Road Width: "+roadWidth);
        tv_constructionType.setText("Construction Type: "+constructionType);
        tv_RMC.setText("Intrested in RMC: "+RMC);
        tv_Loans.setText("Intrested in Loan: "+Loans);
        tv_UPVC.setText("Intrested in UPVC: "+UPVC);
        tv_plotSize.setText("Length("+length+") * Breadth("+breadth+") = Plot Size: "+ plotSize);
        tv_basementCount.setText("Basement Floor Count: "+basementCount);
        tv_floorsCount.setText("Upper Floor Count: " + floorsCount);
        tv_projectSize.setText("Project Size: "+ projectSize);
        tv_total_floor_count.setText("Total Floor Count: "+total_floor_count);
        tv_budgetType.setText("Budget Type: "+budgetType);
        tv_budget.setText("Budget Amount: Rs."+budget+".00");
        tv_RoomsCount.setText("House Types: "+RoomsCount);
        tv_location_latlng.setText("Location: \n"+ Latitude+","+Longitude +"\n"+govApproval);
                //Latitude+Longitude);

        if(!TextUtils.isEmpty(Latitude) && !TextUtils.isEmpty(Longitude)) {
            String Lat = Latitude.substring(0, 9);
            String Long = Longitude.substring(0,9);
            String Location_URL = "http://maps.google.com/maps/api/staticmap?center=" + Lat + "," + Long + "&zoom=16&size=400x400&sensor=false&markers=color:red|" +
                    Lat + "," + Long;

            //+ "&key=AIzaSyB-id7em4GdsqAgxAUDBMRVAt_ApWOv-zk"
            GlideApp.with(getActivity()).load(Location_URL.trim()).centerCrop().into(iv_location);
            //GlideApp.with(getActivity()).load("http://maps.google.com/maps/api/staticmap?center=12.9755703,77.6101547&zoom=16&size=400x400&sensor=false&markers=color:red|12.9755703,77.6101547").into(iv_location);
        }


        if (!TextUtils.isEmpty(projectImage)) {
            GlideApp.with(getActivity()).load("http://mamamicrotechnology.com/clients/MH/webapp/public/projectImages/"
                    + projectImage).into(iv_projectImage);
        }
    }

}
