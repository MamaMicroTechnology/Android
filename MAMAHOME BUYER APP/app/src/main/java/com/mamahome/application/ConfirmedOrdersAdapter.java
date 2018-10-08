package com.mamahome.application;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ConfirmedOrdersAdapter extends RecyclerView.Adapter<ConfirmedOrdersAdapter.ConfrimedOrdersViewHolder> {

    private Context context;
    private ArrayList<Order> orders;
    private Order order;

    public ConfirmedOrdersAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ConfirmedOrdersAdapter.ConfrimedOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.order, parent, false);
        return new ConfrimedOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmedOrdersAdapter.ConfrimedOrdersViewHolder holder, int position) {

        order = orders.get(position);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.oswald);

        holder.tv_ProjectID.setText("Project ID: "+order.getProjectId());
        holder.tv_EnquiryID.setText("Requirement ID: "+order.getReqId());
        holder.tv_Order.setText("This Order Is For: "+ order.getQuantity()+" "+order.getSubCategory()+" "
                +order.getBrand()+" "
                +order.getMainCategory()+"\n Status: "
                +order.getStatus()+"\n Payment Status: "
                +order.getPaymentStatus()+"\n Delivery Status: "
                +order.getDeliveryStatus());

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ConfrimedOrdersViewHolder extends RecyclerView.ViewHolder {

        CardView ordersCardView;

        TextView tv_ProjectID, tv_EnquiryID, tv_Order;

        public ConfrimedOrdersViewHolder(View view) {
            super(view);
            ordersCardView = view.findViewById(R.id.ordersCardView);
            tv_ProjectID = view.findViewById(R.id.tv_ProjectID);
            tv_EnquiryID = view.findViewById(R.id.tv_EnquiryID);
            tv_Order = view.findViewById(R.id.tv_Order);
        }
    }
}
