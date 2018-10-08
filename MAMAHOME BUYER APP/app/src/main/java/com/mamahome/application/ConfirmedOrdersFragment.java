package com.mamahome.application;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class ConfirmedOrdersFragment extends Fragment {

    View view;
    RecyclerView rv_confirmedOrders;
    ArrayList<Order> Orders;
    public static final String ROOT_URL = "https://mamamicrotechnology.com/clients/MH/";
    String User_ID;
    SwipeRefreshLayout swipeLayout;
    APIKeys APIKeys;
    ConfirmedOrdersAdapter confirmedOrdersAdapter;
    ProgressDialog progressDialog;
    int[] type = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};


    public ConfirmedOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_confirmed_orders, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Orders = new ArrayList<>();
        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
        User_ID = prefs.getString("USER_ID", null);

        rv_confirmedOrders = view.findViewById(R.id.rv_confirmedOrders);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //swipeLayout.setRefreshing(false);
            }
        });

        if(isNetWorkAvailable(type)){
            showProgressDialog();
            getOrders();
        }
        else{
            ShowAlert();
        }

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_fall_down);
        rv_confirmedOrders.setLayoutAnimation(controller);
        rv_confirmedOrders.setLayoutManager(staggeredGridLayoutManager);



        return view;
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    private void getOrders(){

        if(!swipeLayout.isRefreshing())
        {
            swipeLayout.setRefreshing(true);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProjectsFragment.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(selfSigningClientBuilder.createClient(getContext()))
                .build();

        APIKeys = retrofit.create(APIKeys.class);
        Call<ConfirmedOrders> orderCall = APIKeys.ConfirmedOrders(User_ID);

        orderCall.enqueue(new Callback<ConfirmedOrders>() {
            @Override
            public void onResponse(Call<ConfirmedOrders> call, Response<ConfirmedOrders> response) {
                ConfirmedOrders confirmedOrders = response.body();
                Orders = new ArrayList<>(Arrays.asList(confirmedOrders.getConfirmedOrders()));
                confirmedOrdersAdapter = new ConfirmedOrdersAdapter(getContext(), Orders);
                rv_confirmedOrders.setAdapter(confirmedOrdersAdapter);
                swipeLayout.setRefreshing(false);
                progressDialog.cancel();

            }

            @Override
            public void onFailure(Call<ConfirmedOrders> call, Throwable t) {
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
                            getOrders();
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
