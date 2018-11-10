package com.wy.schooltakenout.StorePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wy.schooltakenout.Adapter.OrderAdapter;
import com.wy.schooltakenout.Data.Orders;
import com.wy.schooltakenout.Data.Seller;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorePageActivity extends AppCompatActivity {
    //本商店的数据
    private Intent intent;
    private Seller thisSeller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storepage);
        init();
    }

    private void init() {
        String url;
        List<String> list;
        String json;
        Gson gson = new Gson();
        intent = getIntent();

        //获取布局中的构件
        ImageView imageView = findViewById(R.id.store_img);
        TextView nameView = findViewById(R.id.store_name);
        RecyclerView orderView = findViewById(R.id.store_orders);

        //获取传输过来的商店数据
        int storeID = intent.getIntExtra("storeID", 0);
//        String storeName = intent.getStringExtra("name");
//        int storeImg = intent.getIntExtra("img", 0);
//        List<String> storeTags = intent.getStringArrayListExtra("tags");
//        int storeFoodNum = intent.getIntExtra("storeFoodNum", 0);
//        double storeFee = intent.getDoubleExtra("storeFee", 0.00);
//        thisSeller = new Seller(storeNo, storeName, storeImg, storeTags, storeFoodNum, storeFee);
        url = IOTool.ip+"seller/info.do";
        list = new ArrayList<>();
        list.add("sellerID_"+storeID);
        json = IOTool.upAndDown(url, list);
        thisSeller = gson.fromJson(json, Seller.class);

        //添加商店的美食数据
//        final List<Orders> ordersList = new ArrayList<>();
//        for(int i=0; i<thisSeller.getStoreFoodNum(); i++) {
//            Orders order;
//            //测试数据
//            order = new Orders(i, "泡椒风爪"+i, R.drawable.ic_food, "用户", "15200000000");
//
//            ordersList.add(order);
//        }
        url = IOTool.ip+"orders/list.do";
        list = new ArrayList<>();
        list.add("sellerID_"+storeID);
        json = IOTool.upAndDown(url, list);
        Type type = new TypeToken<List<Orders>>(){}.getType();
        final List<Orders> ordersList = gson.fromJson(json, type);

        //使用传输的数据进行构件的初始化赋值
        imageView.setImageResource(thisSeller.getStoreImg());
        nameView.setText(thisSeller.getStoreName());

        //必要，但是不知道有什么用
        GridLayoutManager orderLayoutManager=new GridLayoutManager(this,1);
        orderView.setLayoutManager(orderLayoutManager);
        //设置适配器
        OrderAdapter orderAdapter = new OrderAdapter(ordersList);
        orderView.setAdapter(orderAdapter);
    }
}
