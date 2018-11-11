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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wy.schooltakenout.Data.Goods;
import com.wy.schooltakenout.Data.Seller;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Object> searchList;
    private final int ITEM_STORE = 1, ITEM_FOOD = 2;

    public SearchAdapter(String searchString) {
        searchList = new ArrayList<>();

        // 从服务器获取商家列表
        String url = IOTool.ip+"read/seller/list.do";
        IOTool.upAndDown(url, null);
        JSONArray json = IOTool.getDateArray();
        // 解析商家列表
        Type type = new TypeToken<List<Seller>>(){}.getType();
        Gson gson = new Gson();
        List<Seller> sellerList = gson.fromJson(json.toString(), type);
        for(int i=0; i<sellerList.size(); i++) {
            sellerList.get(i).setSellerPosition(i);
        }

        int flag;
        for(int i=0; i<sellerList.size(); i++) {
            flag = 0;
            Seller seller = sellerList.get(i);
            if(seller.getName().contains(searchString))
                flag = 1;
            // 获取该商店的美食列表
            url = IOTool.ip+"read/good/list.do";
            List<String> list = new ArrayList<>();
            list.add("sellerID="+seller.getSellerID());
            IOTool.upAndDown(url, list);
            json = IOTool.getDateArray();
            type = new TypeToken<List<Goods>>(){}.getType();
            List<Goods> goodsList = gson.fromJson(json.toString(), type);

            List<Goods> foundGoods = new ArrayList<>();

            for(int j = 0; j< goodsList.size(); j++) {
                Goods goods = goodsList.get(j);
                if(goods.getName().contains(searchString)) {
                    flag = 1;
                    foundGoods.add(goods);
                }
            }
            if(flag == 1) {
                searchList.add(seller);
                searchList.addAll(foundGoods);
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

            // 读出商家头像
            String filename = seller.getSellerID()+".jpg";
            String url = IOTool.pictureIp+"resources/seller/head/"+filename;
            String path = this.context.getFileStreamPath("store_"+filename).getPath();
            File file = new File(path);
            IOTool.savePicture(url, path);

            Glide.with(context).load(file).into(viewHolderStore.storeImage);
            viewHolderStore.storeName.setText(seller.getName());

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

            // 读出美食图片
            String filename = goods.getGoodsID()+".jpg";
            String url = IOTool.pictureIp+"resources/food/images/"+filename;
            String path = this.context.getFileStreamPath("food_"+filename).getPath();
            File file = new File(path);
            IOTool.savePicture(url, path);

            Glide.with(context).load(file).into(viewHolderFood.foodImage);
            viewHolderFood.foodName.setText(goods.getName());
            String foodPrice = new DecimalFormat("0.00").format(goods.getPrice());
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
