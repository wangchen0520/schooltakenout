package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wy.schooltakenout.Data.Food;
import com.wy.schooltakenout.Data.Store;
import com.wy.schooltakenout.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Object> searchList;
    private final int ITEM_STORE = 1, ITEM_FOOD = 2;

    public SearchAdapter(String searchString, int storeNum, int[][] chosenFood) {
        searchList = new ArrayList<>();

        Store store;
        List<Food> storeFood = new ArrayList<>();
        int flag;
        //测试数据
        List<String> storeTags = new ArrayList<>();
        storeTags.add("不好吃");
        storeTags.add("冷饮");
        for(int i=0; i<storeNum; i++) {
            flag = 0;
            store = new Store(i, "食堂" + i, R.drawable.ic_store_img, storeTags, i+1, 2.00);
            if(store.getStoreName().contains(searchString))
                flag = 1;
            storeFood.clear();
            for(int j=0; j<store.getStoreFoodNum(); j++) {
                Food food = new Food(j, "泡椒风爪"+j, "食堂"+i, R.drawable.ic_food, 5.60, chosenFood[i][j]);
                if(food.getFoodName().contains(searchString)) {
                    flag = 1;
                    storeFood.add(food);
                }
            }
            if(flag == 1) {
                searchList.add(store);
                searchList.addAll(storeFood);
            }
        }
    }

    //自定义ViewHolder
    static class ViewHolderStore extends RecyclerView.ViewHolder {
        RelativeLayout itemView;
        ImageView storeImage;
        TextView storeName;

        private ViewHolderStore(View itemView) {
            super(itemView);
            this.itemView = (RelativeLayout) itemView;
            storeImage = itemView.findViewById(R.id.store_img);
            storeName = itemView.findViewById(R.id.store_name);
        }
    }

    static class ViewHolderFood extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;

        private ViewHolderFood(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_img);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view;
        switch (viewType) {
            case ITEM_STORE:
                view = LayoutInflater.from(context).inflate(R.layout.search_store,parent,false);
                return new ViewHolderStore(view);
            case ITEM_FOOD:
                view = LayoutInflater.from(context).inflate(R.layout.search_food,parent,false);
                return new ViewHolderFood(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        //官方建议不直接使用position，因为可能会变
        position = holder.getAdapterPosition();

        //按照ViewHolder的类型进行View的生成
        if(holder instanceof ViewHolderStore) {
            final ViewHolderStore viewHolderStore = (ViewHolderStore) holder;
            final Store store = (Store) searchList.get(position);

            Glide.with(context).load(store.getStoreImg()).into(viewHolderStore.storeImage);
            viewHolderStore.storeName.setText(store.getStoreName());

            //设置点击响应
            if(onItemClickListener!= null){
                viewHolderStore.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClick(holder.getAdapterPosition(), store);
                    }
                });
            }
        } else if(holder instanceof ViewHolderFood) {
            final ViewHolderFood viewHolderFood = (ViewHolderFood) holder;
            Food food = (Food) searchList.get(position);

            Glide.with(context).load(food.getFoodImg()).into(viewHolderFood.foodImage);
            viewHolderFood.foodName.setText(food.getFoodName());
            String foodPrice = new DecimalFormat("0.00").format(food.getFoodPrice());
            viewHolderFood.foodPrice.setText(foodPrice);
        }
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    //得到部件的类型码
    @Override
    public int getItemViewType(int position) {
        int viewType = 0;

        if (searchList.get(position) instanceof Store) {
            viewType = ITEM_STORE;
        } else if (searchList.get(position) instanceof Food) {
            viewType = ITEM_FOOD;
        }

        return viewType;
    }

    //设置点击响应
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position, Store thisStore);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
}
