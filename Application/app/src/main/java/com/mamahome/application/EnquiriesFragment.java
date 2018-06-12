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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnquiriesFragment extends Fragment {

    View view;
    FloatingActionButton fabt_addenquiry;
    TextView tv_addnewenquiry;
    FragmentTransaction fragmentTransaction;

    public EnquiriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_enquiries, container, false);
        setRetainInstance(true);

        fabt_addenquiry = (FloatingActionButton) view.findViewById(R.id.fabt_addenquiry);
        fabt_addenquiry.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_add_white));

        //anim = AnimationUtils.loadAnimation(getContext(), R.anim.tobottom);

        tv_addnewenquiry = (TextView) view.findViewById(R.id.tv_addnewproject);

        fabt_addenquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tv_addnewenquiry.setVisibility(View.VISIBLE);
                //tv_addnewenquiry.startAnimation(anim);
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.home_container, new AddEnquiryFragment(), "ADD_ENQUIRY_FRAGMENT");
                fragmentTransaction.addToBackStack("BS_ADD_ENQUIRY");
                fragmentTransaction.commit();
            }
        });

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Enquiries");

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
                if( event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    getFragmentManager().popBackStack("BS_HOME", 0);
                    return true;
                }
                return false;
            }
        });
    }

}
