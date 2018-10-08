package com.mamahome.application;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EnquiriesAdapter extends RecyclerView.Adapter<EnquiriesAdapter.EnquiriesViewHolder> {

    private Context context;
    private ArrayList<Enquiry> enquiries;
    String Req_date;

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
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr = enquiries.get(position).getRequirement_date();
        Date date = null;
        if(inputDateStr != null) {
            try {
                date = inputFormat.parse(inputDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String Req_date = outputFormat.format(date);
            holder.tv_notes.setText("Requirement Date: "+Req_date);
        }

        holder.tv_project_id.setText("Project ID: "+enquiries.get(position).getProject_id());
        holder.tv_mainCategory.setText("Category: "+enquiries.get(position).getMain_category());
        holder.tv_subCategory.setText("Product: "+enquiries.get(position).getSub_category());
        holder.tv_brand.setText("Brand: "+enquiries.get(position).getBrand());
        holder.tv_quantity.setText("Quantity: "+enquiries.get(position).getQuantity());
        holder.tv_enq_status.setText("Status: "+enquiries.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return enquiries.size();
    }

    public class EnquiriesViewHolder extends RecyclerView.ViewHolder{

        TextView tv_project_id, tv_mainCategory, tv_subCategory, tv_brand, tv_quantity, tv_enq_status, tv_notes;

        ImageView iv_edit;

        public EnquiriesViewHolder(View view) {
            super(view);

            tv_project_id = view.findViewById(R.id.tv_project_id);
            tv_notes = view.findViewById(R.id.tv_notes);
            tv_mainCategory = view.findViewById(R.id.tv_main_category);
            tv_subCategory = view.findViewById(R.id.tv_sub_category);
            tv_brand = view.findViewById(R.id.tv_Brand);
            tv_quantity = view.findViewById(R.id.tv_quantity);
            tv_enq_status = view.findViewById(R.id.tv_enq_status);
            iv_edit = view.findViewById(R.id.iv_edit);

            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                        String inputDateStr = enquiries.get(pos).getRequirement_date();
                        if(inputDateStr != null){
                            Date date = null;
                            try {
                                date = inputFormat.parse(inputDateStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Req_date = outputFormat.format(date);
                        }
                        openEnquiryDetails(enquiries.get(pos).getId(), enquiries.get(pos).getProject_id(),
                                enquiries.get(pos).getMain_category(), enquiries.get(pos).getBrand(),
                                enquiries.get(pos).getSub_category(), enquiries.get(pos).getRequirement_date(),
                                enquiries.get(pos).getNotes(), enquiries.get(pos).getA_contact(),
                                enquiries.get(pos).getLocation(), enquiries.get(pos).getQuantity());
                    }
                        Toast.makeText(context, "Enquiry ID: " + enquiries.get(pos).getId(), Toast.LENGTH_LONG).show();
                }
            });
            /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        openEnquiryDetails(enquiries.get(pos).getId(), enquiries.get(pos).getProject_id(),
                                enquiries.get(pos).getMain_category(), enquiries.get(pos).getBrand(),
                                enquiries.get(pos).getSub_category(), enquiries.get(pos).getRequirement_date(),
                                enquiries.get(pos).getNotes(), enquiries.get(pos).getA_contact(),
                                enquiries.get(pos).getLocation(), enquiries.get(pos).getQuantity());
                    }
                    return false;
                }
            });*/
        }
    }

    /*$enquiry->project_id = $request->project_id;
    $enquiry->main_category = $request->main_category;
    $enquiry->brand = $request->brand;
    $enquiry->sub_category = $request->sub_category;
    $enquiry->requirement_date = $request->requirement_date;
    $enquiry->notes = $request->notes;
    $enquiry->A_contact = $request->A_contact;*/

    private void openEnquiryDetails(String id, String Project_id, String main_category, String brand,
                                    String sub_category, String requirement_date, String notes, String A_contact,
                                    String Location, String Quantity){

        UpdateEnquiryFragment updateEnquiryFragment = new UpdateEnquiryFragment();

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("projectId", Project_id);
        bundle.putString("main_category", main_category);
        bundle.putString("brand", brand);
        bundle.putString("sub_category", sub_category);
        bundle.putString("requirement_date", requirement_date);
        bundle.putString("notes", notes);
        bundle.putString("A_contact", A_contact);
        bundle.putString("location", Location);
        bundle.putString("quantity", Quantity);

        updateEnquiryFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = ((HomeActivity)context).getSupportFragmentManager().beginTransaction();
        /*fragmentTransaction.setCustomAnimations(
                R.anim.right_in, R.anim.right_out,
                R.anim.left_in, R.anim.left_out);*/
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left, R.anim.slide_in_left,
                R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.home_container, updateEnquiryFragment);
        fragmentTransaction.addToBackStack("BS_UPDATEENQUIRY");
        fragmentTransaction.commit();



    }
}
