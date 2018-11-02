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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
public class SelectProjectFragment extends Fragment {

    View view;
    FloatingActionButton fabt_addproject;
    TextView tv_addnewproject;
    FragmentTransaction fragmentTransaction;
    RecyclerView recyclerViewProjects;
    APIKeys APIKeys;
    String ROOT_URL = "https://mamahome360.com";
    String User_ID;
    private ArrayList<Project> project;
    private SelectingProjectAdapter projectsAdapter;
    int[] type = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
    ProgressDialog progressDialog;
    Bundle bundle;

    public SelectProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_select_project, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);

        bundle = this.getArguments();
        String Category = bundle.getString("Category");
        String Brand = bundle.getString("Brand");
        String Sub_Cat = bundle.getString("Sub_Cat");


        project = new ArrayList<>();

        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
        User_ID = prefs.getString("USER_ID", null);
        recyclerViewProjects = view.findViewById(R.id.rv_projectsforEnquiry);

        fabt_addproject = view.findViewById(R.id.fabt_addproject);
        fabt_addproject.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_add_white));

        //anim = AnimationUtils.loadAnimation(getContext(), R.anim.tobottom);

        tv_addnewproject = view.findViewById(R.id.tv_addnewproject);
        fabt_addproject.setOnClickListener(new View.OnClickListener() {
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



        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Select Project");

        return view;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
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
                projectsAdapter = new SelectingProjectAdapter(getContext(), project, bundle);
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
