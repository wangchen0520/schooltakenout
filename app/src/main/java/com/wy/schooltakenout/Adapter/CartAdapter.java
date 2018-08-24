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
import com.wy.schooltakenout.Data.Food;
import com.wy.schooltakenout.Data.Store;
import com.wy.schooltakenout.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Object> chosenList;
    private final int ITEM_STORE = 1, ITEM_FOOD = 2;

    public CartAdapter(int[][] chosenFood, int storeNum) {
        //找出点过菜的商家，将购物车信息按顺序排列为List<Object>
        chosenList = new ArrayList<>();
        double foodPrice = 5.60;
        List<Food> storeFood = new ArrayList<>();
        int flag;
        for(int i=0; i<storeNum; i++) {
            flag = 0;
            storeFood.clear();
            for(int j=0; j<100; j++) {
                if(chosenFood[i][j] != 0) {
                    flag = 1;
                    storeFood.add(new Food("泡椒风爪"+j, "食堂"+i, R.drawable.ic_food, foodPrice, chosenFood[i][j]));
                }
            }
            if(flag == 1) {
                chosenList.add(new Store("食堂"+i, R.drawable.ic_store_img, null));
                chosenList.addAll(storeFood);
            }
        }
    }

    //用于连接Store列表的项中的各个View，区分不同的数据类型
    //自定义ViewHolder
    static class ViewHolderStore extends RecyclerView.ViewHolder {
        ImageView storeImage;
        TextView storeName;

        public ViewHolderStore(View itemView) {
            super(itemView);
            storeImage = itemView.findViewById(R.id.store_img);
            storeName = itemView.findViewById(R.id.store_name);
        }
    }

    static class ViewHolderFood extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView foodNum;

        public ViewHolderFood(View itemView) {
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
            case ITEM_STORE:
                view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_item,parent,false);
                return new ViewHolderStore(view);
            case ITEM_FOOD:
                view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_food,parent,false);
                return new ViewHolderFood(view);
        }
        return null;
    }

    //用于生成Store列表中各个项
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //官方建议不直接使用position，因为可能会变
        position = holder.getAdapterPosition();

        if(holder instanceof ViewHolderStore) {
            final ViewHolderStore viewHolderStore = (ViewHolderStore) holder;
            Store store = (Store) chosenList.get(position);

            Glide.with(context).load(store.getStoreImg()).into(viewHolderStore.storeImage);
            viewHolderStore.storeName.setText(store.getStoreName());
        } else if(holder instanceof ViewHolderFood) {
            final ViewHolderFood viewHolderFood = (ViewHolderFood) holder;
            Food food = (Food) chosenList.get(position);

            Glide.with(context).load(food.getFoodImg()).into(viewHolderFood.foodImage);
            viewHolderFood.foodName.setText(food.getFoodName()+"");
            String foodPrice = new DecimalFormat("0.00").format(food.getFoodPrice());
            viewHolderFood.foodPrice.setText(foodPrice);
            viewHolderFood.foodNum.setText(food.getFoodNum()+"");
        }
    }

    @Override
    public int getItemCount() {
        return chosenList.size();
    }

    //得到部件的类型码
    @Override
    public int getItemViewType(int position) {
        int viewType = 0;

        if (chosenList.get(position) instanceof Store) {
            viewType = ITEM_STORE;
        } else if (chosenList.get(position) instanceof Food) {
            viewType = ITEM_FOOD;
        }

        return viewType;
    }
}
