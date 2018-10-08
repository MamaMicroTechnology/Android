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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnquiriesFragment extends Fragment {

    View view;
    FloatingActionButton fabt_addenquiry;
    TextView tv_addnewenquiry;
    FragmentTransaction fragmentTransaction;
    RecyclerView recyclerViewEnquiries, recyclerViewProjects;
    APIKeys APIKeys;
    String ROOT_URL = "https://mamahome360.com";
    String User_ID;
    LinearLayout ll_newEnquiry;
    ImageView iv_noEnquiries;
    private ArrayList<Enquiry> enquiries;
    private EnquiriesAdapter enquiriesAdapter;
    private ArrayList<Project> project;
    private ProjectsInEnquiriesAdapter projectsAdapter;
    int[] type = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
    ProgressDialog progressDialog;
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

        ll_newEnquiry = view.findViewById(R.id.ll_newEnquiry);
        ll_newEnquiry.setVisibility(View.GONE);
        iv_noEnquiries = view.findViewById(R.id.iv_noEnquiries);
        project = new ArrayList<>();

        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
        User_ID = prefs.getString("USER_ID", null);
        recyclerViewProjects = view.findViewById(R.id.rv_projectsforEnquiry);

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

        LinearLayoutManager staggeredGridLayoutManagerProjects = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewProjects.setLayoutManager(staggeredGridLayoutManagerProjects);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProjectsFragment.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(selfSigningClientBuilder.createClient(getContext()))
                .build();
        APIKeys = retrofit.create(APIKeys.class);

        if(isNetWorkAvailable(type)){
            showProgressDialog();
            getProjects();
        }
        else{
            ShowAlert();
        }



        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Projects For Enquiries");

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

                EnquiriesList enquiriesList = response.body();
                enquiries = new ArrayList<>(Arrays.asList(enquiriesList.getEnquiries()));
                enquiriesAdapter = new EnquiriesAdapter(getContext(), enquiries);
                enquiriesAdapter.notifyDataSetChanged();
                recyclerViewEnquiries.setAdapter(enquiriesAdapter);

                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                recyclerViewEnquiries.setLayoutManager(staggeredGridLayoutManager);
                progressDialog.cancel();

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

    public void AllowNewEnquiry(){
        ll_newEnquiry.setVisibility(View.VISIBLE);
    }

    private void getProjects(){

        /*if(!swipeLayout.isRefreshing())
        {
            swipeLayout.setRefreshing(true);
        }*/
        Call<ProjectsList> projectsListCall = APIKeys.viewProject(User_ID);


        projectsListCall.enqueue(new Callback<ProjectsList>() {
            @Override
            public void onResponse(Call<ProjectsList> call, Response<ProjectsList> response) {
                //Toast.makeText(getContext(), "Success" + response.body().getProjects(), Toast.LENGTH_LONG).show();

                ProjectsList projectsList = response.body();
                project = new ArrayList<>(Arrays.asList(projectsList.getProjects()));
                projectsAdapter = new ProjectsInEnquiriesAdapter(getContext(), project, EnquiriesFragment.this);
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_fall_down);
                recyclerViewProjects.setLayoutAnimation(controller);
                recyclerViewProjects.setAdapter(projectsAdapter);
                progressDialog.cancel();
                //swipeLayout.setRefreshing(false);
                //recyclerViewProjects.setAdapter(new ProjectsAdapter(getContext(), projectsList.getProjects()));
            }

            @Override
            public void onFailure(Call<ProjectsList> call, Throwable t) {
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
                    ((HomeActivity)getActivity()).MarkHomeItemSelected(0);
                    getFragmentManager().popBackStack("BS_HOME", 0);
                    return true;
                }
                return false;
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
                            getProjects();
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
