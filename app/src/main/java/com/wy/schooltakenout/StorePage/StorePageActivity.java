package com.wy.schooltakenout.StorePage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorePageActivity extends AppCompatActivity {
    //本商店的数据
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
        Intent intent = getIntent();

        // 获取布局中的构件
        ImageView imageView = findViewById(R.id.store_img);
        TextView nameView = findViewById(R.id.store_name);
        RecyclerView orderView = findViewById(R.id.store_orders);

        // 获取传输过来的商店数据
        int sellerID = intent.getIntExtra("sellerID", 0);
        url = IOTool.ip+"seller/info.do";
        list = new ArrayList<>();
        list.add("sellerID_"+sellerID);
        json = IOTool.upAndDown(url, list);
        thisSeller = gson.fromJson(json, Seller.class);

        // 获取商店的订单数据
        url = IOTool.ip+"orders/list.do";
        list = new ArrayList<>();
        list.add("sellerID_"+sellerID);
        json = IOTool.upAndDown(url, list);
        Type type = new TypeToken<List<Orders>>(){}.getType();
        final List<Orders> ordersList = gson.fromJson(json, type);


        // 读出商家头像
        String filename = thisSeller.getSellerID()+".jpg";
        String path = this.getFilesDir().getAbsolutePath();
        File file = new File(path+"store_"+filename);
        if(!file.exists()) {
            // 向服务器请求商家头像并存储
            url = IOTool.ip+"resources/seller/head/"+filename;
            String result = IOTool.upAndDown(url, null);
            IOTool.save(result, "store_"+filename, this);
        }
        // 使用传输的数据进行构件的初始化赋值
        imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        nameView.setText(thisSeller.getName());

        //必要，但是不知道有什么用
        GridLayoutManager orderLayoutManager=new GridLayoutManager(this,1);
        orderView.setLayoutManager(orderLayoutManager);
        //设置适配器
        OrderAdapter orderAdapter = new OrderAdapter(ordersList);
        orderView.setAdapter(orderAdapter);
    }
}
