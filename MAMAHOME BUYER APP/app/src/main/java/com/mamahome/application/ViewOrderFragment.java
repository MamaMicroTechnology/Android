package com.mamahome.application;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewOrderFragment extends Fragment {


    View view;

    public ViewOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_order, container, false);
        ButterKnife.bind(this, view);



        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("View Order");
        return view;
    }

}
