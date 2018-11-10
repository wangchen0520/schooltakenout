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
import com.wy.schooltakenout.Data.Goods;
import com.wy.schooltakenout.Data.Seller;
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

        Seller seller;
        List<Goods> storeGoods = new ArrayList<>();
        int flag;
        //测试数据
        List<String> storeTags = new ArrayList<>();
        storeTags.add("不好吃");
        storeTags.add("冷饮");
        for(int i=0; i<storeNum; i++) {
            flag = 0;
            seller = new Seller(i, "食堂" + i, R.drawable.ic_store_img, storeTags, i+1, 2.00);
            if(seller.getStoreName().contains(searchString))
                flag = 1;
            storeGoods.clear();
            for(int j = 0; j< seller.getStoreFoodNum(); j++) {
                Goods goods = new Goods(j, "泡椒风爪"+j, "食堂"+i, R.drawable.ic_food, 5.60, chosenFood[i][j]);
                if(goods.getFoodName().contains(searchString)) {
                    flag = 1;
                    storeGoods.add(goods);
                }
            }
            if(flag == 1) {
                searchList.add(seller);
                searchList.addAll(storeGoods);
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
            final Seller seller = (Seller) searchList.get(position);

            Glide.with(context).load(seller.getStoreImg()).into(viewHolderStore.storeImage);
            viewHolderStore.storeName.setText(seller.getStoreName());

            //设置点击响应
            if(onItemClickListener!= null){
                viewHolderStore.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClick(holder.getAdapterPosition(), seller);
                    }
                });
            }
        } else if(holder instanceof ViewHolderFood) {
            final ViewHolderFood viewHolderFood = (ViewHolderFood) holder;
            Goods goods = (Goods) searchList.get(position);

            Glide.with(context).load(goods.getFoodImg()).into(viewHolderFood.foodImage);
            viewHolderFood.foodName.setText(goods.getFoodName());
            String foodPrice = new DecimalFormat("0.00").format(goods.getFoodPrice());
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

        if (searchList.get(position) instanceof Seller) {
            viewType = ITEM_STORE;
        } else if (searchList.get(position) instanceof Goods) {
            viewType = ITEM_FOOD;
        }

        return viewType;
    }

    //设置点击响应
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position, Seller thisSeller);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
}
