package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wy.schooltakenout.Data.Food;
import com.wy.schooltakenout.R;

import java.text.DecimalFormat;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private Context context;
    private List<Food> foodList;

    //用于连接Store列表的项中的各个View
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView foodView;
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView foodNum;
        ImageButton foodAdd;
        ImageButton foodReduce;

        private ViewHolder(View view){
            super(view);

            foodView = (CardView) view;
            foodImage = view.findViewById(R.id.food_img);
            foodName = view.findViewById(R.id.food_name);
            foodPrice = view.findViewById(R.id.food_price);
            foodNum = view.findViewById(R.id.food_num);
            foodAdd = view.findViewById(R.id.food_add);
            foodReduce = view.findViewById(R.id.food_reduce);
        }
    }

    public FoodAdapter(List<Food> foodList){
        this.foodList=foodList;
    }

    //初始时调用，连接layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.food_item,parent,false);
        return new ViewHolder(view);
    }

    //用于生成Store列表中各个项
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //官方建议不直接使用position，因为可能会变
        position = holder.getAdapterPosition();
        Food food = foodList.get(position);

        holder.foodName.setText(food.getFoodName());
        holder.foodPrice.setText(new DecimalFormat("0.00").format(food.getFoodPrice()));
        if(food.getFoodNum() != 0) {
            holder.foodNum.setText(new DecimalFormat("0").format(food.getFoodNum()));
        } else {
            holder.foodNum.setText("");
        }
        Glide.with(context).load(food.getFoodImg()).into(holder.foodImage);

        //设置点击响应
        if(onItemClickListener!= null){
//            holder.itemView.setOnClickListener( new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition(), foodList.get(holder.getAdapterPosition()));
//                }
//            });
            holder.foodAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClickAdd(holder.itemView, holder.getAdapterPosition(), foodList.get(holder.getAdapterPosition()));
                }
            });
            holder.foodReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClickReduce(holder.itemView, holder.getAdapterPosition(), foodList.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    //设置点击响应
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
//        //点击进入美食详情界面
//        void onItemClick(View view, int position, Food thisFood);
        //点击添加美食进行购买
        void onClickAdd(View view, int position, Food thisFood);
        //点击减少美食
        void onClickReduce(View view, int position, Food thisFood);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
}
