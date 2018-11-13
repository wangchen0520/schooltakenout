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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wy.schooltakenout.Data.Goods;
import com.wy.schooltakenout.Data.Seller;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;

import org.json.JSONArray;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Object> chosenList;
    private final int ITEM_STORE = 1, ITEM_FOOD = 2;

    public CartAdapter(int[][] chosenFood) {
        // 一些变量
        String url;
        JSONArray json;
        Type type;
        Gson gson = new Gson();

        // 找出点过菜的商家，将购物车信息按顺序排列为List<Object>
        chosenList = new ArrayList<>();
        List<Goods> storeGoods = new ArrayList<>();

        // 从服务器获取商家列表
        url = IOTool.ip+"read/seller/list.do";
        IOTool.upAndDown(url, null);
        json = IOTool.getDateArray();

        // 解析商家列表
        type = new TypeToken<List<Seller>>(){}.getType();
        List<Seller> sellerList = gson.fromJson(json.toString(), type);
        for(int i=0; i<sellerList.size(); i++) {
            sellerList.get(i).setSellerPosition(i);
        }

        // 遍历商家列表，找到数量大于0的美食
        int flag;
        for(int i=0; i<sellerList.size(); i++) {
            // flag表示这个商店或商店的美食被选择了
            flag = 0;

            Seller seller = sellerList.get(i);
            // 获取该商店的美食列表
            url = IOTool.ip+"read/good/list.do";
            List<String> list = new ArrayList<>();
            list.add("sellerID="+seller.getSellerID());
            IOTool.upAndDown(url, list);
            json = IOTool.getDateArray();
            type = new TypeToken<List<Goods>>(){}.getType();
            List<Goods> goodsList = gson.fromJson(json.toString(), type);

            // 遍历美食列表
            storeGoods.clear();
            for(int j = 0; j< goodsList.size(); j++) {
                if(chosenFood[i][j] != 0) {
                    flag = 1;
                    Goods goods = goodsList.get(j);
                    // 因为服务器传来的没有这个数据，自己添加
                    goods.setNum(chosenFood[i][j]);
                    storeGoods.add(goods);
                }
            }
            if(flag == 1) {
                chosenList.add(seller);
                chosenList.addAll(storeGoods);
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
            final Seller seller = (Seller) chosenList.get(position);

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
                viewHolderStore.storeDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClickDelete(holder.getAdapterPosition(), seller);
                    }
                });
            }
        } else if(holder instanceof ViewHolderFood) {
            final ViewHolderFood viewHolderFood = (ViewHolderFood) holder;
            Goods goods = (Goods) chosenList.get(position);

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
            viewHolderFood.foodNum.setText(goods.getNum()+"");
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

        if (chosenList.get(position) instanceof Seller) {
            viewType = ITEM_STORE;
        } else if (chosenList.get(position) instanceof Goods) {
            viewType = ITEM_FOOD;
        }

        return viewType;
    }

    //设置点击响应
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position, Seller thisSeller);
        //position为所删除的商店View所在位置
        void onClickDelete(int position, Seller thisSeller);
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
        } while (!(chosenList.get(position) instanceof Seller));
        //保险起见，再刷新一遍
        notifyDataSetChanged();
    }

    //动态改变商家中选择的美食变化，position为添加的商家View位置
    public void changeFood(int position, int[] changedFoods) {
        Seller seller = (Seller) chosenList.get(position);

        // 获取该商店的美食列表
        String url = IOTool.ip+"read/good/list.do";
        List<String> list = new ArrayList<>();
        list.add("sellerID="+seller.getSellerID());
        IOTool.upAndDown(url, list);
        JSONArray json = IOTool.getDateArray();
        Type type = new TypeToken<List<Goods>>(){}.getType();
        Gson gson = new Gson();
        List<Goods> goodsList = gson.fromJson(json.toString(), type);

        // 先删除
        deleteStore(position);
        // 再判断是否需要添加
        int flag = 0;
        List<Object> changedList = new ArrayList<>();

        for(int i = 0; i< goodsList.size(); i++) {
            if (changedFoods[i] != 0) {
                flag = 1;
                changedList.add(seller);
                break;
            }
        }
        if(flag == 1) {
            for(int i = 0; i< goodsList.size(); i++) {
                if(changedFoods[i] != 0) {
                    Goods goods = goodsList.get(i);
                    goods.setNum(changedFoods[i]);
                    changedList.add(goods);
                }
            }
            chosenList.addAll(position, changedList);
            //保险起见，再刷新一遍
            notifyDataSetChanged();
        }
    }
}
