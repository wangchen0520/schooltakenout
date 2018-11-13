package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wy.schooltakenout.Data.Goods;
import com.wy.schooltakenout.Data.OrderFood;
import com.wy.schooltakenout.Data.OrderItem;
import com.wy.schooltakenout.Data.Orders;
import com.wy.schooltakenout.Data.User;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;
import com.wy.schooltakenout.Tool.TestPrinter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<Object> orderList;
    private final int ITEM_ORDER = 1, ITEM_FOOD = 2;

    public OrderAdapter(List<Orders> ordersList, int sellerID){
        // 一些变量
        String url;
        JSONArray json;
        Type type;
        Gson gson = new Gson();

        // 从服务器获取商家美食列表
        url = IOTool.ip+"read/good/list.do";
        List<String> list = new ArrayList<>();
        list.add("sellerID="+sellerID);
        IOTool.upAndDown(url, list);
        json = IOTool.getDateArray();
        // 解析美食列表
        type = new TypeToken<List<Goods>>(){}.getType();
        List<Goods> goodsList = gson.fromJson(json.toString(), type);

        // 找出点过菜的商家，将购物车信息按顺序排列为List<Object>
        orderList = new ArrayList<>();
        List<OrderFood> orderedFood = new ArrayList<>();

        // 遍历美食列表，找到订单中美食
        for(int i=0; i<ordersList.size(); i++) {
            Orders orders = ordersList.get(i);

            // 遍历美食列表
            orderedFood.clear();
            OrderItem orderItem = new OrderItem(orders.getUserID(), orders.getTime());

            String[] goodsIDs = orders.getGoodsID().split("_");
            String[] goodsNums = orders.getGoodsNum().split("_");
            for(int k=0; k<goodsIDs.length; k++) {
                for(int j = 0; j< goodsList.size(); j++) {
                    Goods goods = goodsList.get(j);
                    if(Integer.parseInt(goodsIDs[k]) == goods.getGoodsID()) {
                        OrderFood orderFood = new OrderFood(
                                Integer.parseInt(goodsIDs[k]),
                                goods.getName(),
                                goods.getPrice(),
                                Integer.parseInt(goodsNums[k]));
                        orderedFood.add(orderFood);
                    }
                }
            }
            orderList.add(orderItem);
            orderList.addAll(orderedFood);
        }
    }

    //用于连接Order列表的项中的各个View，区分不同的数据类型
    //自定义ViewHolder
    static class ViewHolderOrder extends RecyclerView.ViewHolder {
        TextView clientName;
        TextView clientPhone;
        TextView orderTime;

        private ViewHolderOrder(View view){
            super(view);

            clientName=view.findViewById(R.id.client_name);
            clientPhone=view.findViewById(R.id.client_phone);
            orderTime=view.findViewById(R.id.order_time);
        }
    }

    static class ViewHolderFood extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView foodNum;

        private ViewHolderFood(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_img);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodNum = itemView.findViewById(R.id.food_num);
        }
    }

    //初始时调用，连接layout，不同的部分使用不同布局
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view;
        switch (viewType) {
            case ITEM_ORDER:
                view = LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
                return new ViewHolderOrder(view);
            case ITEM_FOOD:
                view = LayoutInflater.from(context).inflate(R.layout.order_food,parent,false);
                return new ViewHolderFood(view);
            default:
                return null;
        }
    }

    //用于生成Order列表中各个项
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        //官方建议不直接使用position，因为可能会变
        position = holder.getAdapterPosition();

        //按照ViewHolder的类型进行View的生成
        if(holder instanceof ViewHolderOrder) {
            final ViewHolderOrder viewHolderOrder = (ViewHolderOrder) holder;
            final OrderItem orderItem = (OrderItem) orderList.get(position);

            // 根据userID得到用户信息
            int userID = orderItem.getUserID();
            String url = IOTool.ip+"read/user/info.do";
            List<String> list = new ArrayList<>();
            list.add("userID="+userID);
            IOTool.upAndDown(url, list);
            JSONObject json = IOTool.getData();
            Gson gson = new Gson();
            User user = gson.fromJson(json.toString(), User.class);

            viewHolderOrder.clientName.setText(user.getName());
            viewHolderOrder.clientPhone.setText(user.getPhone());
            viewHolderOrder.orderTime.setText(orderItem.getTime());
        } else if(holder instanceof ViewHolderFood) {
            final ViewHolderFood viewHolderFood = (ViewHolderFood) holder;
            OrderFood orderFood = (OrderFood) orderList.get(position);

            // 读出美食图片
            String filename = orderFood.getGoodsID()+".jpg";
            String url = IOTool.pictureIp+"resources/food/images/"+filename;
            String path = this.context.getFileStreamPath("food_"+filename).getPath();
            File file = new File(path);
            IOTool.savePicture(url, path);

            Glide.with(context).load(file).into(viewHolderFood.foodImage);
            viewHolderFood.foodName.setText(orderFood.getGoodsName());
            String foodPrice = new DecimalFormat("0.00").format(orderFood.getGoodsPrice());
            viewHolderFood.foodPrice.setText(foodPrice);
            viewHolderFood.foodNum.setText(orderFood.getGoodsNum()+"");
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    //得到部件的类型码
    @Override
    public int getItemViewType(int position) {
        int viewType = 0;

        if (orderList.get(position) instanceof OrderItem) {
            viewType = ITEM_ORDER;
        } else if (orderList.get(position) instanceof OrderFood) {
            viewType = ITEM_FOOD;
        }

        return viewType;
    }
}
