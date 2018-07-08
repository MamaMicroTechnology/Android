package com.mamahome.application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EnquiriesAdapter extends RecyclerView.Adapter<EnquiriesAdapter.EnquiriesViewHolder> {

    private Context context;
    private ArrayList<Enquiry> enquiries;

    public EnquiriesAdapter(Context context, ArrayList<Enquiry> enquiries) {
        this.context = context;
        this.enquiries = enquiries;
    }

    @NonNull
    @Override
    public EnquiriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.enquiry, parent, false);
        return new EnquiriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnquiriesViewHolder holder, int position) {
        holder.tv_project_id.setText(enquiries.get(position).getProject_id());
        holder.tv_notes.setText(enquiries.get(position).getNotes());
        holder.tv_mainCategory.setText(enquiries.get(position).getMain_category());
        holder.tv_subCategory.setText(enquiries.get(position).getSub_category());
        holder.tv_brand.setText(enquiries.get(position).getBrand());
        holder.tv_quantity.setText(enquiries.get(position).getQuantity());
        holder.tv_enq_status.setText(enquiries.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return enquiries.size();
    }

    public class EnquiriesViewHolder extends RecyclerView.ViewHolder{

        TextView tv_project_id, tv_mainCategory, tv_subCategory, tv_brand, tv_quantity, tv_enq_status, tv_notes;

        public EnquiriesViewHolder(View view) {
            super(view);

            tv_project_id = view.findViewById(R.id.tv_project_id);
            tv_notes = view.findViewById(R.id.tv_notes);
            tv_mainCategory = view.findViewById(R.id.tv_main_category);
            tv_subCategory = view.findViewById(R.id.tv_sub_category);
            tv_brand = view.findViewById(R.id.tv_Brand);
            tv_quantity = view.findViewById(R.id.tv_quantity);
            tv_enq_status = view.findViewById(R.id.tv_enq_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Toast.makeText(context, "Project Name" + enquiries.get(pos).getNotes(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
