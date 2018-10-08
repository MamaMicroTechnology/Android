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
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder> {

    private Context context;
    private ArrayList<Project> projects;
    private Project project;

    public ProjectsAdapter(Context context, ArrayList<Project> projects) {

        this.context = context;
        this.projects = projects;
    }

    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.project, parent, false);
        return new ProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectsViewHolder holder, int position) {

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


        ProjectsViewHolder(View view) {
            super(view);
            projectCardView = view.findViewById(R.id.projectsCardview);
            iv_project = view.findViewById(R.id.iv_project);
            tv_projectTitle = view.findViewById(R.id.tv_projectTitle);
            tv_projectAddress = view.findViewById(R.id.tv_projectAddress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        //Toast.makeText(context, "Project Name" + projects.get(pos).getProject_name(), Toast.LENGTH_LONG).show();
                        openProjectDetails(projects.get(pos).getProject_id(),projects.get(pos).getProject_name(), projects.get(pos).getAddress(),
                                projects.get(pos).getProject_status(), projects.get(pos).getRoad_name(),
                                projects.get(pos).getRoad_width(), projects.get(pos).getConstruction_type(),
                                projects.get(pos).getInterested_in_rmc(), projects.get(pos).getInterested_in_loan(),
                                projects.get(pos).getInterested_in_doorsandwindows(), projects.get(pos).getPlotsize(),
                                projects.get(pos).getBasement(), projects.get(pos).getGround(), projects.get(pos).getProject_size(),
                                projects.get(pos).getBudgetType(), projects.get(pos).getBudget(), "RoomTypes",
                                projects.get(pos).getLatitude(), projects.get(pos).getLongitude(), projects.get(pos).getImage(),
                                projects.get(pos).getMunicipality_approval(), projects.get(pos).getLength(), projects.get(pos).getBreadth());

                    }
                }
            });
        }

        private void openProjectDetails(String Project_id, String projectName, String address, String projectStatus, String roadName,
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
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                    R.anim.slide_out_left, R.anim.slide_in_left,
                    R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.home_container, viewProjectFragment);
            fragmentTransaction.addToBackStack("BS_VIEWPROJECT");
            fragmentTransaction.commit();



        }
    }

    public void setSearchFilter(ArrayList<Project> searchedProjects){
        projects = new ArrayList<>();
        projects.addAll(searchedProjects);
        notifyDataSetChanged();
    }
}
