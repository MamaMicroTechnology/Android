package com.mamahome.application;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExistingEnquiriesFragment extends Fragment {

    View view;
    FloatingActionButton fabt_addenquiry;
    TextView tv_addnewenquiry;
    FragmentTransaction fragmentTransaction;
    RecyclerView recyclerViewEnquiries;
    APIKeys APIKeys;
    String ROOT_URL = "https://mamahome360.com";
    String User_ID;
    SwipeRefreshLayout swipeLayout;
    ImageView iv_noEnquiries;
    private ArrayList<Enquiry> enquiries;
    private EnquiriesAdapter enquiriesAdapter;
    String Project_ID;
    int[] type = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
    ProgressDialog progressDialog;


    public ExistingEnquiriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_existing_enquiries, container, false);

        final Bundle bundle = this.getArguments();
        Project_ID = bundle.getString("project_id");

        iv_noEnquiries = view.findViewById(R.id.iv_noEnquiries);
        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
        User_ID = prefs.getString("USER_ID", null);
        recyclerViewEnquiries = view.findViewById(R.id.rv_enquiries);

        fabt_addenquiry = (FloatingActionButton) view.findViewById(R.id.fabt_addenquiry);
        fabt_addenquiry.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_add_white));
        swipeLayout = view.findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isNetWorkAvailable(type)){
                    getEnquiries(Project_ID);
                }
                else{
                    ShowAlert();
                }
                //swipeLayout.setRefreshing(false);
            }
        });

        fabt_addenquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tv_addnewenquiry.setVisibility(View.VISIBLE);
                //tv_addnewenquiry.startAnimation(anim);
                AddEnquiryFragment addEnquiryFragment = new AddEnquiryFragment();
               /* Bundle bundle1 = new Bundle();
                bundle1.putString("project_id", Project_ID);*/
                addEnquiryFragment.setArguments(bundle);
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.home_container, addEnquiryFragment, "ADD_ENQUIRY_FRAGMENT");
                fragmentTransaction.addToBackStack("BS_ADD_ENQUIRY");
                fragmentTransaction.commit();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProjectsFragment.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(selfSigningClientBuilder.createClient(getContext()))
                .build();
        APIKeys = retrofit.create(APIKeys.class);

        if(isNetWorkAvailable(type)){
            showProgressDialog();
            getEnquiries(Project_ID);
        }
        else{
            ShowAlert();
        }

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Enquiries For "+Project_ID);



        return view;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void getEnquiries(String Project_ID){
        Call<EnquiriesList> enquiriesListCall = APIKeys.viewEnquiries(Project_ID);

        enquiriesListCall.enqueue(new Callback<EnquiriesList>() {
            @Override
            public void onResponse(Call<EnquiriesList> call, Response<EnquiriesList> response) {


                //Toast.makeText(getContext(), "Success" + response.body().getProjects(), Toast.LENGTH_LONG).show();

                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                recyclerViewEnquiries.setLayoutManager(staggeredGridLayoutManager);
                EnquiriesList enquiriesList = response.body();
                enquiries = new ArrayList<>(Arrays.asList(enquiriesList.getEnquiries()));
                enquiriesAdapter = new EnquiriesAdapter(getContext(), enquiries);
                enquiriesAdapter.notifyDataSetChanged();
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_fall_down);
                recyclerViewEnquiries.setLayoutAnimation(controller);
                recyclerViewEnquiries.setAdapter(enquiriesAdapter);
                progressDialog.cancel();

                if(swipeLayout.isRefreshing()){
                    swipeLayout.setRefreshing(false);
                }


                if(enquiriesAdapter.getItemCount() == 0){
                    iv_noEnquiries.setVisibility(View.VISIBLE);
                    recyclerViewEnquiries.setVisibility(View.GONE);
                }
                else{
                    iv_noEnquiries.setVisibility(View.GONE);
                    recyclerViewEnquiries.setVisibility(View.VISIBLE);
                }
                //recyclerViewProjects.setAdapter(new ProjectsAdapter(getContext(), projectsList.getProjects()));
            }

            @Override
            public void onFailure(Call<EnquiriesList> call, Throwable t) {
                Log.d("faliure message", t.getMessage());
                Toast.makeText(getContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();

            }
        });
    }

    private void ShowAlert(){
        new AlertDialog.Builder(getContext())
                .setTitle("Oops.. No Internet Connection")
                .setMessage("Please Connect To The Internet To Use Our Services! \nThank You.")
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetWorkAvailable(type)){
                            showProgressDialog();
                            getEnquiries(Project_ID);
                        }
                        else{
                            ShowAlert();
                        }
                    }
                })
                .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Aborted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private boolean isNetWorkAvailable(int[] type){
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            for(int typeNetwork:type){
                assert cm != null;
                NetworkInfo networkInfo = cm.getNetworkInfo(typeNetwork);
                if(networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        catch (Exception e){
            return false;
        }
        return false;
    }

}
