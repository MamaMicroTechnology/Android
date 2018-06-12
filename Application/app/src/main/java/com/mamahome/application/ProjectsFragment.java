package com.mamahome.application;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectsFragment extends Fragment {

    View view;
    FragmentTransaction fragmentTransaction;
    FloatingActionButton fabt_addproject;
    TextView tv_addnewproject;
    Animation anim;


    public ProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_projects, container, false);
        setRetainInstance(true);

        fabt_addproject = (FloatingActionButton) view.findViewById(R.id.fabt_addproject);
        fabt_addproject.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_add_white));

        //anim = AnimationUtils.loadAnimation(getContext(), R.anim.tobottom);

        tv_addnewproject = (TextView) view.findViewById(R.id.tv_addnewproject);

        fabt_addproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.home_container, new AddProjectFragment(), "ADD_PROJECT_FRAGMENT");
                fragmentTransaction.addToBackStack("BS_ADD_PROJECT");
                fragmentTransaction.commit();
                //tv_addnewproject.setVisibility(View.VISIBLE);
                //tv_addnewproject.startAnimation(anim);
            }
        });

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Projects");


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
                if( event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getFragmentManager().popBackStack("BS_HOME", 0);
                    return true;
                }
                return false;
            }
        });
    }

}
