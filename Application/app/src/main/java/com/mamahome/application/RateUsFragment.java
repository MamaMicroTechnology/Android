package com.mamahome.application;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


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


    public RateUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rate_us, container, false);
        setRetainInstance(true);

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
                }
                else{
                    Toast.makeText(getContext(), "Please Choose Your Rating First!", Toast.LENGTH_LONG).show();
                }

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
                    getFragmentManager().popBackStack("BS_HOME", 0);
                    return true;
                }
                return false;
            }
        });
    }

}
