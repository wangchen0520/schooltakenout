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
import com.wy.schooltakenout.Data.Goods;
import com.wy.schooltakenout.Tool.IOTool;
import com.wy.schooltakenout.Tool.OrderView.OrderView;
import com.wy.schooltakenout.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import rx.functions.Action2;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private Context context;
    private List<Goods> goodsList;

    //用于连接Food列表的项中的各个View
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView foodView;
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        OrderView orderView;

        private ViewHolder(View view){
            super(view);

            foodView = (CardView) view;
            foodImage = view.findViewById(R.id.food_img);
            foodName = view.findViewById(R.id.food_name);
            foodPrice = view.findViewById(R.id.food_price);
            orderView = view.findViewById(R.id.order_food);
        }
    }

    public FoodAdapter(List<Goods> goodsList){
        this.goodsList = goodsList;
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
        Goods goods = goodsList.get(position);

        holder.foodName.setText(goods.getName());
        holder.foodPrice.setText(new DecimalFormat("0.00").format(goods.getPrice()));

        // 读出美食图片
        String filename = goods.getGoodsID()+".jpg";
        String url = IOTool.pictureIp+"resources/food/images/"+filename;
        String path = this.context.getFileStreamPath("food_"+filename).getPath();
        File file = new File(path);
        IOTool.savePicture(url, path);

        Glide.with(context).load(file).into(holder.foodImage);

        //设置数量初始状态
        if(goods.getNum() != 0) {
            holder.orderView.setState(5, goods.getNum());
        }
        //设置点击响应
        holder.orderView.setCallback(new Action2<Integer, Integer>() {
            @Override
            public void call(Integer integer, Integer integer2) {
                onItemClickListener.onClickButtom(
                        holder.getAdapterPosition(),
                        integer2,
                        holder.orderView,
                        goodsList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    //设置点击响应
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClickButtom(int position, int variable, OrderView orderView, Goods thisGoods);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
}
