package com.mamahome.application;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProjectsInEnquiriesAdapter extends RecyclerView.Adapter<ProjectsInEnquiriesAdapter.ProjectsViewHolder>{

    private Context context;
    private ArrayList<Project> projects;
    private Project project;
    private EnquiriesFragment enquiriesFragment;
    private ArrayList<Enquiry> enquiries;


    public ProjectsInEnquiriesAdapter(Context context, ArrayList<Project> projects, EnquiriesFragment enquiriesFragment) {
        this.context = context;
        this.projects = projects;
        this.enquiriesFragment = enquiriesFragment;
    }

    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.projects_in_enquiries, parent, false);
        return new ProjectsInEnquiriesAdapter.ProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsViewHolder holder, int position) {
        project = projects.get(position);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.oswald);
        if (!TextUtils.isEmpty(project.getImage())) {

            //holder.iv_project.setImageDrawable(null);
            GlideApp.with(context).load("http://mamamicrotechnology.com/clients/MH/webapp/public/projectImages/"
                    + project.getImage().trim())
                    .centerCrop()
                    .into(holder.iv_project);
            holder.tv_projectTitle.setTypeface(typeface);
            holder.tv_projectTitle.setText(project.getProject_name());
            holder.tv_projectAddress.setText("Located at: "+project.getAddress());
        } else {
            holder.tv_projectTitle.setTypeface(typeface);
            holder.tv_projectTitle.setText(project.getProject_name());
            holder.tv_projectAddress.setText("Located at: "+project.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ProjectsViewHolder extends RecyclerView.ViewHolder {
        //@BindView(R.id.projectsCardview)
        CardView projectCardView;

        //@BindView(R.id.iv_project)
//        ImageView iv_project;
        ImageView iv_project;

        //@BindView(R.id.tv_projectTitle)
        TextView tv_projectTitle, tv_projectAddress;
        RecyclerView recyclerViewEnquiries;

        LinearLayout ll_newEnquiry;


        ProjectsViewHolder(View view) {
            super(view);
            projectCardView = view.findViewById(R.id.projectsCardview);
            iv_project = view.findViewById(R.id.iv_project);
            tv_projectTitle = view.findViewById(R.id.tv_projectTitle);
            tv_projectAddress = view.findViewById(R.id.tv_projectAddress);
            recyclerViewEnquiries = view.findViewById(R.id.rv_enquiries);
            ll_newEnquiry = view.findViewById(R.id.ll_newEnquiry);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Toast.makeText(context, "Project ID"  + projects.get(pos).getProject_id(), Toast.LENGTH_LONG).show();
                    openExistingEnquiries(projects.get(pos).getProject_id());
                    /*enquiriesFragment.getEnquiries(projects.get(pos).getProject_id());
                    SharedPreferences pref = context.getSharedPreferences("SP_ENQUIRY_DATA", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("PROJECT_SELECTED_ENQUIRY", projects.get(pos).getProject_id());
                    editor.apply();
                    enquiriesFragment.AllowNewEnquiry();*/
                    /*if (pos != RecyclerView.NO_POSITION) {
                        Toast.makeText(context, "Project Name" + projects.get(pos).getProject_name(), Toast.LENGTH_LONG).show();
                        openProjectDetails(projects.get(pos).getProject_id(), projects.get(pos).getProject_name(), projects.get(pos).getAddress(),
                                projects.get(pos).getProject_status(), projects.get(pos).getRoad_name(),
                                projects.get(pos).getRoad_width(), projects.get(pos).getConstruction_type(),
                                projects.get(pos).getInterested_in_rmc(), projects.get(pos).getInterested_in_loan(),
                                projects.get(pos).getInterested_in_doorsandwindows(), projects.get(pos).getPlotsize(),
                                projects.get(pos).getBasement(), projects.get(pos).getGround(), projects.get(pos).getProject_size(),
                                projects.get(pos).getBudgetType(), projects.get(pos).getBudget(), "RoomTypes",
                                projects.get(pos).getLatitude(), projects.get(pos).getLongitude(), projects.get(pos).getImage(),
                                projects.get(pos).getMunicipality_approval(), projects.get(pos).getLength(), projects.get(pos).getBreadth());
                    }*/
                        /*Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ProjectsFragment.ROOT_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .client(selfSigningClientBuilder.createClient((HomeActivity)context))
                                .build();
                        APIKeys apiKeys = retrofit.create(APIKeys.class);
                        Toast.makeText(context, "Project ID" + projects.get(pos).getProject_id(), Toast.LENGTH_LONG).show();
                        getEnquiries(projects.get(pos).getProject_id(), recyclerViewEnquiries, apiKeys);*/

                }
            });
        }
    }

    private void openExistingEnquiries(String Project_ID){
        ExistingEnquiriesFragment existingEnquiriesFragment = new ExistingEnquiriesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("project_id", Project_ID);
        existingEnquiriesFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = ((HomeActivity)context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left, R.anim.slide_in_left,
                R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.home_container, existingEnquiriesFragment);
        fragmentTransaction.addToBackStack("BS_VIEWEXISTINGENQUIRIES");
        fragmentTransaction.commit();
    }

        /*private void openProjectDetails(String Project_id, String projectName, String address, String projectStatus, String roadName,
                                        String roadWidth, String constructionType, String RMC, String Loans,
                                        String UPVC, String plotSize, String basementCount, String floorsCount,
                                        String projectSize, String budgetType,
                                        String budget, String RoomsCount, String Latitude, String Longitude,
                                        String projectImage, String govApproval, String length, String breadth){

            ViewProjectFragment viewProjectFragment = new ViewProjectFragment();

            Bundle bundle = new Bundle();
            bundle.putString("projectId", Project_id);
            bundle.putString("projectName", projectName);
            bundle.putString("address", address);
            bundle.putString("projectStatus", projectStatus);
            bundle.putString("roadName", roadName);
            bundle.putString("roadWidth", roadWidth);
            bundle.putString("constructionType", constructionType);
            bundle.putString("RMC", RMC);
            bundle.putString("Loans", Loans);
            bundle.putString("UPVC", UPVC);
            bundle.putString("plotSize", plotSize);
            bundle.putString("basementCount", basementCount);
            bundle.putString("floorsCount", floorsCount);
            bundle.putString("projectSize", projectSize);
            bundle.putString("budgetType", budgetType);
            bundle.putString("budget", budget);
            bundle.putString("RoomsCount", RoomsCount);
            bundle.putString("Latitude", Latitude);
            bundle.putString("Longitude", Longitude);
            bundle.putString("projectImage", projectImage);
            bundle.putString("govApproval", govApproval);
            bundle.putString("Length", length);
            bundle.putString("Breadth", breadth);

            viewProjectFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = ((HomeActivity)context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_container, viewProjectFragment);
            fragmentTransaction.addToBackStack("BS_VIEWPROJECT");
            fragmentTransaction.commit();



        }*/

    /*private void getEnquiries(String Project_ID, final RecyclerView recyclerViewEnquiries, APIKeys apiKeys){
        *//*StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerViewEnquiries.setLayoutManager(staggeredGridLayoutManager);*//*
        Call<EnquiriesList> enquiriesListCall = apiKeys.viewEnquiries("13213");


        enquiriesListCall.enqueue(new Callback<EnquiriesList>() {
            @Override
            public void onResponse(Call<EnquiriesList> call, Response<EnquiriesList> response) {
                //Toast.makeText(getContext(), "Success" + response.body().getProjects(), Toast.LENGTH_LONG).show();

                EnquiriesList enquiriesList = response.body();
                enquiries = new ArrayList<>(Arrays.asList(enquiriesList.getEnquiries()));

                EnquiriesAdapter enquiriesAdapter = new EnquiriesAdapter(((HomeActivity)context), enquiries);
                recyclerViewEnquiries.setAdapter(enquiriesAdapter);
                //recyclerViewProjects.setAdapter(new ProjectsAdapter(getContext(), projectsList.getProjects()));
            }

            @Override
            public void onFailure(Call<EnquiriesList> call, Throwable t) {
                Log.d("faliure message", t.getMessage());
                Toast.makeText(((HomeActivity)context), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();

            }
        });
    }*/


}
