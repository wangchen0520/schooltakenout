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
import com.google.gson.Gson;
import com.wy.schooltakenout.Data.Orders;
import com.wy.schooltakenout.Data.User;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private Context context;
    private List<Orders> ordersList;

    public OrderAdapter(List<Orders> ordersList){
        this.ordersList = ordersList;
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
        Orders orders = ordersList.get(position);

        // 读出美食图片
        String filename = orders.getGoodsID()+".jpg";
        String url = IOTool.pictureIp+"resources/food/images/"+filename;
        String path = this.context.getFileStreamPath("food_"+filename).getPath();
        File file = new File(path);
        IOTool.savePicture(url, path);

        // 根据userID得到用户信息
        int userID = orders.getUserID();
        url = IOTool.ip+"read/user/info.do";
        List<String> list = new ArrayList<>();
        list.add("userID="+userID);
        IOTool.upAndDown(url, list);
        JSONObject json = IOTool.getData();
        Gson gson = new Gson();
        User user = gson.fromJson(json.toString(), User.class);

        Glide.with(context).load(file).into(holder.storeImage);
        holder.clientName.setText(user.getName());
        holder.clientPhone.setText(user.getPhone());
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }
}
