package com.mamahome.application;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProjectFragment extends Fragment {

    View view;

    ImageView iv_projectImage;

    TextView tv_projectName, tv_address, tv_projectStatus, tv_roadName, tv_roadWidth, tv_constructionType,
             tv_RMC, tv_Loans, tv_UPVC, tv_plotSize, tv_basementCount, tv_floorsCount, tv_total_floor_count,
             tv_projectSize, tv_budgetType, tv_budget, tv_RoomsCount, tv_location_latlng;

    String projectName, address, projectStatus, roadName, roadWidth, constructionType,
           RMC, Loans, UPVC, plotSize, basementCount, floorsCount, total_floor_count,
           projectSize, budgetType, budget, RoomsCount, Latitude, Longitude, projectImage, govApproval;

    public ViewProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_project, container, false);
        ButterKnife.bind(this, view);

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

        Bundle bundle = this.getArguments();
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
        total_floor_count = "10+20=30";

        putValues();


        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("View Project");
        return view;
    }


    private void putValues(){

        tv_projectName.setText(projectName);
        tv_address.setText(address);
        tv_projectStatus.setText("Status: "+projectStatus);
        tv_roadName.setText("Road Name: "+roadName);
        tv_roadWidth.setText("Road Width"+roadWidth);
        tv_constructionType.setText("Construction Type"+constructionType);
        tv_RMC.setText("Intrested in RMC: "+RMC);
        tv_Loans.setText("Intrested in Loan: "+Loans);
        tv_UPVC.setText("Intrested in UPVC: "+UPVC);
        tv_plotSize.setText("Plot Size: "+plotSize);
        tv_basementCount.setText("Basement Floor Count: "+basementCount);
        tv_total_floor_count.setText("Floor Count: "+total_floor_count);
        tv_budgetType.setText("Budget Type: "+budgetType);
        tv_budget.setText("Budget Amount: "+budget);
        tv_RoomsCount.setText("House Types: "+RoomsCount);
        tv_location_latlng.setText("Lat & Long: "+Latitude+Longitude);

        if (!TextUtils.isEmpty(projectImage)) {
            Glide.with(getActivity()).load("http://mamahome360.com/webapp/public/projectImages/"
                    + projectImage).into(iv_projectImage);
        }
    }

}
