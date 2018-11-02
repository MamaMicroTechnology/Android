package com.mamahome.application.Products;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<Category> categories;
    private Category category;
    HomeNewFragment homeNewFragment;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    RecyclerView rv_categories;
    int row_index = -1;

    public CategoryAdapter(Context context, ArrayList<Category> categories, HomeNewFragment homeNewFragment, StaggeredGridLayoutManager staggeredGridLayoutManager, RecyclerView rv_categories) {
        this.context = context;
        this.categories = categories;
        this.homeNewFragment = homeNewFragment;
        this.staggeredGridLayoutManager = staggeredGridLayoutManager;
        this.rv_categories = rv_categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product, parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        category = categories.get(position);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.oswald);
        holder.tv_productTitle.setTypeface(typeface);
        holder.tv_productTitle.setText(category.getCategory_name());

        if(category.getCatimage() != null) {
            GlideApp.with(context).load("http://mamamicrotechnology.com/clients/MH/webapp/public/category/"
                    + category.getCatimage().trim())
                    .into(holder.iv_product);
        }
        else{
            holder.iv_product.setImageDrawable(context.getResources().getDrawable(R.drawable.color));
        }

        holder.productsCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                notifyDataSetChanged();
                homeNewFragment.getBrands(categories.get(position).getId());
            }
        });
        if(row_index==position){
            //holder.productsCardview.setCardBackgroundColor(Color.parseColor("#388e3c"));
            holder.ll_productTitle.setBackgroundColor(Color.parseColor("#388e3c"));
            holder.tv_productTitle.setTextColor(Color.parseColor("#ffffff"));

            //staggeredGridLayoutManager.scrollToPositionWithOffset(row_index, 0);

            //rv_categories.smoothScrollToPosition(row_index);
            rv_categories.scrollToPosition(row_index);
            //holder.iv_tick.setVisibility(View.VISIBLE);
        }
        else{
            //holder.productsCardview.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.ll_productTitle.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tv_productTitle.setTextColor(Color.parseColor("#000000"));
            //holder.iv_tick.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        CardView productsCardview;

        ImageView iv_product, iv_tick;

        TextView tv_productTitle;

        LinearLayout ll_productTitle;

        public CategoryViewHolder(View view) {
            super(view);

            productsCardview = view.findViewById(R.id.productsCardview);
            iv_product = view.findViewById(R.id.iv_product);
            iv_tick = view.findViewById(R.id.iv_tick);
            tv_productTitle = view.findViewById(R.id.tv_productTitle);
            ll_productTitle = view.findViewById(R.id.ll_productTitle);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/
        }
    }
}
