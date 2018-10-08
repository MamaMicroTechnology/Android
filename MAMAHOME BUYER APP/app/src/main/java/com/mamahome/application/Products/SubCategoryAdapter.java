package com.mamahome.application.Products;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mamahome.application.GlideApp;
import com.mamahome.application.HomeNewFragment;
import com.mamahome.application.R;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {

    private Context context;
    private ArrayList<SubCategory> subCategories;
    private SubCategory subCategory;
    HomeNewFragment homeNewFragment;
    RecyclerView rv_Sub;
    int row_index = -1;
    String categoryname, brand;

    public SubCategoryAdapter(Context context, ArrayList<SubCategory> subCategories, HomeNewFragment homeNewFragment, RecyclerView rv_SubString,
                              String categoryname, String brand) {
        this.context = context;
        this.subCategories = subCategories;
        this.homeNewFragment = homeNewFragment;
        this.rv_Sub = rv_Sub;
        this.categoryname = categoryname;
        this.brand = brand;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product, parent, false);
        return new SubCategoryAdapter.SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, final int position) {
        subCategory = subCategories.get(position);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.oswald);
        holder.tv_productTitle.setTypeface(typeface);
        holder.tv_productTitle.setText(subCategory.getSub_cat_name());
        if(subCategory.getSubimage() != null) {
            GlideApp.with(context).load("http://mamamicrotechnology.com/clients/MH/webapp/public/subcat/"
                    + subCategory.getSubimage().trim())
                    .into(holder.iv_product);
        }
        else{
            holder.iv_product.setImageDrawable(context.getResources().getDrawable(R.drawable.color));
        }

        holder.productsCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                //homeNewFragment.DisplayPopUp(categoryname, brand, subCategories.get(position).getSub_cat_name());
                //Toast.makeText(context, "Name : "+ subCategories.get(position).getSub_cat_name(), Toast.LENGTH_SHORT).show();
            }
        });
        if(row_index==position){
            //holder.productsCardview.setCardBackgroundColor(Color.parseColor("#388e3c"));
            holder.ll_productTitle.setBackgroundColor(Color.parseColor("#388e3c"));
            holder.tv_productTitle.setTextColor(Color.parseColor("#ffffff"));
//            rv_Sub.scrollToPosition(row_index);
            homeNewFragment.DisplayPopUp(categoryname, brand,
                    subCategories.get(position).getSub_cat_name(), subCategories.get(position).getSubimage(), row_index);
        }
        else{
            //holder.productsCardview.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.ll_productTitle.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tv_productTitle.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        CardView productsCardview;

        ImageView iv_product;

        TextView tv_productTitle;

        LinearLayout ll_productTitle;

        public SubCategoryViewHolder(View view) {
            super(view);

            productsCardview = view.findViewById(R.id.productsCardview);
            iv_product = view.findViewById(R.id.iv_product);
            tv_productTitle = view.findViewById(R.id.tv_productTitle);
            ll_productTitle = view.findViewById(R.id.ll_productTitle);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                }
            });*/
        }
    }
}
