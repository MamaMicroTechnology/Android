package com.mamahome.application;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
public class ProjectsFragment extends Fragment implements SearchView.OnQueryTextListener {

    View view;
    FragmentTransaction fragmentTransaction;
    FloatingActionButton fabt_addproject;
    TextView tv_addnewproject;
    Animation anim;
    RecyclerView recyclerViewProjects;
    APIKeys APIKeys;
    String ROOT_URL = "http://mamahome360.com";
    String User_ID;
    private ArrayList<Project> project;
    private ProjectsAdapter projectsAdapter;



    public ProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_projects, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
        User_ID = prefs.getString("USER_ID", null);

        getProjects();

        fabt_addproject = (FloatingActionButton) view.findViewById(R.id.fabt_addproject);
        fabt_addproject.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_add_white));
        recyclerViewProjects = view.findViewById(R.id.rv_projects);

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

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewProjects.setLayoutManager(staggeredGridLayoutManager);


        return view;
    }

    private void getProjects(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIKeys = retrofit.create(APIKeys.class);
        Call<ProjectsList> projectsListCall = APIKeys.viewProject(User_ID);

        projectsListCall.enqueue(new Callback<ProjectsList>() {
            @Override
            public void onResponse(Call<ProjectsList> call, Response<ProjectsList> response) {
                //Toast.makeText(getContext(), "Success" + response.body().getProjects(), Toast.LENGTH_LONG).show();

                ProjectsList projectsList = response.body();
                project = new ArrayList<>(Arrays.asList(projectsList.getProjects()));
                projectsAdapter = new ProjectsAdapter(getContext(), project);
                recyclerViewProjects.setAdapter(projectsAdapter);
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
                if( event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getFragmentManager().popBackStack("BS_HOME", 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Project> searchedProjects = new ArrayList<>();
        //for (Project project : project){
        for (int i = 0; i<project.size(); i++){
            String name = project.get(i).getProject_name().toLowerCase();
            if (name.contains(newText)){
                searchedProjects.add(project.get(i));
            }
        }
        projectsAdapter.setSearchFilter(searchedProjects);
        return true;
    }
}
