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

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {

    private Context context;
    private ArrayList<Brand> brands;
    private Brand brand;
    HomeNewFragment homeNewFragment;
    String categoryID;
    RecyclerView rv_brand;
    int row_index = -1;

    public BrandAdapter(Context context, ArrayList<Brand> brands, HomeNewFragment homeNewFragment, String categoryID, RecyclerView rv_brand) {
        this.context = context;
        this.brands = brands;
        this.homeNewFragment = homeNewFragment;
        this.categoryID = categoryID;
        this.rv_brand = rv_brand;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product, parent, false);
        return new BrandAdapter.BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, final int position) {
        brand = brands.get(position);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.oswald);
        holder.tv_productTitle.setTypeface(typeface);
        holder.tv_productTitle.setText(brand.getBrand());

        if(brand.getBrandimage() != null) {
            GlideApp.with(context).load("http://mamamicrotechnology.com/clients/MH/webapp/public/brands/"
                    + brand.getBrandimage().trim())
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
                homeNewFragment.getSubCatgories(categoryID, brands.get(position).getId());
            }
        });
        if(row_index==position){
            //holder.productsCardview.setCardBackgroundColor(Color.parseColor("#388e3c"));
            holder.ll_productTitle.setBackgroundColor(Color.parseColor("#388e3c"));
            holder.tv_productTitle.setTextColor(Color.parseColor("#ffffff"));

            rv_brand.scrollToPosition(row_index);
        }
        else{
            //holder.productsCardview.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.ll_productTitle.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tv_productTitle.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    public class BrandViewHolder extends RecyclerView.ViewHolder {

        CardView productsCardview;

        ImageView iv_product;

        TextView tv_productTitle;

        LinearLayout ll_productTitle;


        public BrandViewHolder(View view) {
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
