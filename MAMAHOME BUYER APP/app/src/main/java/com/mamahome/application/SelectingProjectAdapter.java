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
import android.widget.Toast;

import java.util.ArrayList;

public class SelectingProjectAdapter extends RecyclerView.Adapter<SelectingProjectAdapter.ProjectsViewHolder> {
    private Context context;
    private ArrayList<Project> projects;
    private Bundle bundle;
    private Project project;

    public SelectingProjectAdapter(Context context, ArrayList<Project> projects, Bundle bundle) {
        this.context = context;
        this.projects = projects;
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.projects_in_enquiries, parent, false);
        return new SelectingProjectAdapter.ProjectsViewHolder(view);
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
        CardView projectCardView;

        //@BindView(R.id.iv_project)
//        ImageView iv_project;
        ImageView iv_project;

        //@BindView(R.id.tv_projectTitle)
        TextView tv_projectTitle, tv_projectAddress;
        RecyclerView recyclerViewEnquiries;

        public ProjectsViewHolder(View view) {
            super(view);
            projectCardView = view.findViewById(R.id.projectsCardview);
            iv_project = view.findViewById(R.id.iv_project);
            tv_projectTitle = view.findViewById(R.id.tv_projectTitle);
            tv_projectAddress = view.findViewById(R.id.tv_projectAddress);
            recyclerViewEnquiries = view.findViewById(R.id.rv_enquiries);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = getAdapterPosition();
                    String Project_id = projects.get(pos).getProject_id();
                    CustomEnquiryFragment customEnquiryFragment = new CustomEnquiryFragment();
                    bundle.putString("Project_id", Project_id);
                    customEnquiryFragment.setArguments(bundle);
                    Toast.makeText(context, "Project ID - "+Project_id, Toast.LENGTH_SHORT).show();
                    FragmentTransaction fragmentTransaction = ((HomeActivity)context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left, R.anim.slide_in_left,
                            R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.home_container, customEnquiryFragment);
                    fragmentTransaction.addToBackStack("BS_CUSTOM_ENQUIRY");
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
