package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class CartAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Object> chosenList;
    private final int ITEM_STORE = 1, ITEM_FOOD = 2;

    public CartAdapter(int[][] chosenFood, int storeNum) {
        //找出点过菜的商家，将购物车信息按顺序排列为List<Object>
        chosenList = new ArrayList<>();
        //测试数据
        List<String> storeTags = new ArrayList<>();
        storeTags.add("不好吃");
        storeTags.add("冷饮");
        List<Food> storeFood = new ArrayList<>();
        int flag;
        for(int i=0; i<storeNum; i++) {
            flag = 0;
            Store store;
            store = new Store(i, "食堂"+i, R.drawable.ic_store_img, storeTags, i+1, 2.00);
            storeFood.clear();
            for(int j=0; j<store.getStoreFoodNum(); j++) {
                if(chosenFood[i][j] != 0) {
                    flag = 1;
                    Food food = new Food(j, "泡椒风爪"+j, "食堂"+i, R.drawable.ic_food, 5.60, chosenFood[i][j]);
                    storeFood.add(food);
                }
            }
            if(flag == 1) {
                chosenList.add(store);
                chosenList.addAll(storeFood);
            }
        }
    }

    //用于连接Store列表的项中的各个View，区分不同的数据类型
    //自定义ViewHolder
    static class ViewHolderStore extends RecyclerView.ViewHolder {
        RelativeLayout itemView;
        ImageView storeImage;
        TextView storeName;
        ImageButton storeDelete;

        private ViewHolderStore(View itemView) {
            super(itemView);
            this.itemView = (RelativeLayout) itemView;
            storeImage = itemView.findViewById(R.id.store_img);
            storeName = itemView.findViewById(R.id.store_name);
            storeDelete = itemView.findViewById(R.id.store_delete);
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
            case ITEM_STORE:
                view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_store,parent,false);
                return new ViewHolderStore(view);
            case ITEM_FOOD:
                view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_food,parent,false);
                return new ViewHolderFood(view);
            default:
                return null;
        }
    }

    //用于生成Store列表中各个项
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        //官方建议不直接使用position，因为可能会变
        position = holder.getAdapterPosition();

        //按照ViewHolder的类型进行View的生成
        if(holder instanceof ViewHolderStore) {
            final ViewHolderStore viewHolderStore = (ViewHolderStore) holder;
            final Store store = (Store) chosenList.get(position);

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
                viewHolderStore.storeDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClickDelete(holder.getAdapterPosition(), store);
                    }
                });
            }
        } else if(holder instanceof ViewHolderFood) {
            final ViewHolderFood viewHolderFood = (ViewHolderFood) holder;
            Food food = (Food) chosenList.get(position);

            Glide.with(context).load(food.getFoodImg()).into(viewHolderFood.foodImage);
            viewHolderFood.foodName.setText(food.getFoodName());
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

    //设置点击响应
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position, Store thisStore);
        //position为所删除的商店View所在位置
        void onClickDelete(int position, Store thisStore);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }

    //动态删除商家，position为删除的商家View位置
    public void deleteStore(int position) {
        do {
            chosenList.remove(position);
            if(chosenList.size() == position)
                break;
        } while (!(chosenList.get(position) instanceof Store));
        //保险起见，再刷新一遍
        notifyDataSetChanged();
    }

    //动态改变商家中选择的美食变化，position为添加的商家View位置
    public void changeFood(int position, int storeNo, int[] changedFoods) {
        Store store;
        //测试数据
        List<String> storeTags = new ArrayList<>();
        storeTags.add("不好吃");
        storeTags.add("冷饮");
        store = new Store(storeNo, "食堂" + storeNo, R.drawable.ic_store_img, storeTags, storeNo+1, 2.00);

        //先删除
        deleteStore(position);
        //再判断是否需要添加
        int flag = 0;
        List<Object> changedList = new ArrayList<>();

        for(int i=0; i<store.getStoreFoodNum(); i++) {
            if (changedFoods[i] != 0) {
                flag = 1;
                changedList.add(store);
                break;
            }
        }
        if(flag == 1) {
            for(int i=0; i<store.getStoreFoodNum(); i++) {
                if(changedFoods[i] != 0) {
                    Food food;
                    //测试数据
                    food = new Food(i, "泡椒风爪"+i, "食堂"+i, R.drawable.ic_food, 5.60, changedFoods[i]);

                    changedList.add(food);
                }
            }
            chosenList.addAll(position, changedList);
            //保险起见，再刷新一遍
            notifyDataSetChanged();
        }
    }
}
