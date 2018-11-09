package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wy.schooltakenout.Data.Order;
import com.wy.schooltakenout.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private Context context;
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList){
        this.orderList = orderList;
    }

    //用于连接Store列表的项中的各个View
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView itemView;
        ImageView storeImage;
        TextView clientName;
        TextView clientPhone;

        private ViewHolder(View view){
            super(view);

            itemView=(CardView) view;
            storeImage=view.findViewById(R.id.store_img);
            clientName=view.findViewById(R.id.client_name);
            clientPhone=view.findViewById(R.id.client_phone);
        }
    }

    //初始时调用，连接layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new ViewHolder(view);
    }

    //用于生成Order列表中各个项
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //官方建议不直接使用position，因为可能会变
        position = holder.getAdapterPosition();
        Order order = orderList.get(position);

        Glide.with(context).load(order.getFoodImage()).into(holder.storeImage);
        holder.clientName.setText(order.getClientName());
        holder.clientPhone.setText(order.getClientPhone());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
