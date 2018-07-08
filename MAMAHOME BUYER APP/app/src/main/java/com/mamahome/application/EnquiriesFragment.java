package com.mamahome.application;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnquiriesFragment extends Fragment {

    View view;
    FloatingActionButton fabt_addenquiry;
    TextView tv_addnewenquiry;
    FragmentTransaction fragmentTransaction;
    RecyclerView recyclerViewEnquiries;
    APIKeys APIKeys;
    String ROOT_URL = "http://mamahome360.com";
    String User_ID;
    private ArrayList<Enquiry> enquiries;
    private EnquiriesAdapter enquiriesAdapter;

    public EnquiriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_enquiries, container, false);
        ButterKnife.bind(this, view);
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

        recyclerViewEnquiries = view.findViewById(R.id.rv_enquiries);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewEnquiries.setLayoutManager(staggeredGridLayoutManager);
        getEnquiries();

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Enquiries");

        return view;
    }

    private void getEnquiries(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIKeys = retrofit.create(APIKeys.class);
        Call<EnquiriesList> enquiriesListCall = APIKeys.viewEnquiries("13213");

        enquiriesListCall.enqueue(new Callback<EnquiriesList>() {
            @Override
            public void onResponse(Call<EnquiriesList> call, Response<EnquiriesList> response) {
                //Toast.makeText(getContext(), "Success" + response.body().getProjects(), Toast.LENGTH_LONG).show();

                EnquiriesList enquiriesList = response.body();
                enquiries = new ArrayList<>(Arrays.asList(enquiriesList.getEnquiries()));
                enquiriesAdapter = new EnquiriesAdapter(getContext(), enquiries);
                recyclerViewEnquiries.setAdapter(enquiriesAdapter);
                //recyclerViewProjects.setAdapter(new ProjectsAdapter(getContext(), projectsList.getProjects()));
            }

            @Override
            public void onFailure(Call<EnquiriesList> call, Throwable t) {
                Log.d("faliure message", t.getMessage());
                Toast.makeText(getContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();

            }
        });
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
