package com.mamahome.application;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEnquiryFragment extends Fragment {

    View view;

    EditText et_projectName, et_contactNumber, et_location, et_remarks;

    String ProjectName, ContactNumber, Location, Remarks, MainCategory,
            SubCategory, Brand, RequirementDate, UserID, ProjectID;

    FragmentTransaction fragmentTransaction;

    public AddEnquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_enquiry, container, false);




        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Make New Enquiry");
        return view;
    }

}
