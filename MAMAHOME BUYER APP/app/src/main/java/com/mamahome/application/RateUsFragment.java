package com.mamahome.application;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RateUsFragment extends Fragment {

    View view;
    FragmentTransaction fragmentTransaction;
    ConstraintLayout cl_rateus;
    RelativeLayout rl_rateus;
    RatingBar rb_rateus;
    Button bt_rate;
    Animation anim;


    public RateUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rate_us, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);

        cl_rateus = (ConstraintLayout) view.findViewById(R.id.cl_rateus);
        rl_rateus = (RelativeLayout) view.findViewById(R.id.rl_rateus);
        rb_rateus = (RatingBar) view.findViewById(R.id.rb_rateus);
        bt_rate = (Button) view.findViewById(R.id.bt_rate);

        bt_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float rating = rb_rateus.getRating();
                if(rating >= .5){
                    cl_rateus.setVisibility(View.GONE);
                    rl_rateus.setVisibility(View.VISIBLE);
                    rl_rateus.setAnimation(anim);
                }
                else{
                    Toast.makeText(getContext(), "Please Choose Your Rating First!", Toast.LENGTH_LONG).show();
                }

            }
        });

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((HomeActivity)getActivity()).MarkHomeItemSelected(0);
                getFragmentManager().popBackStack("BS_HOME", 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Rate Us");

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
                    ((HomeActivity)getActivity()).MarkHomeItemSelected(0);
                    getFragmentManager().popBackStack("BS_HOME", 0);
                    return true;
                }
                return false;
            }
        });
    }

}
