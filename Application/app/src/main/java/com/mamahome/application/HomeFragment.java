package com.mamahome.application;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView tv_homepage_text1;
    View view;
    FragmentTransaction fragmentTransaction;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setRetainInstance(true);

        tv_homepage_text1 = (TextView) view.findViewById(R.id.tv_homepage_text1);
        String text = "<font color='#f58220'>Link</font> " +
                "<font color='#9c9c9c'> Buyers, Sellers and Professionals </font> <br>" +
                "<font color='#ffffff'> 0000000l</font>" +
                "<font color='#069241'>in the Construction Industry</font>";
        tv_homepage_text1.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        // Inflate the layout for this fragment
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Home");
        return view;
    }




}
