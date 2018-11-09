package com.wy.schooltakenout.StorePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wy.schooltakenout.Adapter.OrderAdapter;
import com.wy.schooltakenout.Data.Order;
import com.wy.schooltakenout.Data.Store;
import com.wy.schooltakenout.R;

import java.util.ArrayList;
import java.util.List;

public class StorePageActivity extends AppCompatActivity {
    //本商店的数据
    private Intent intent;
    private Store thisStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storepage);
        init();
    }

    private void init() {
        intent = getIntent();

        //获取布局中的构件
        ImageView imageView = findViewById(R.id.store_img);
        TextView nameView = findViewById(R.id.store_name);
        RecyclerView orderView = findViewById(R.id.store_orders);

        //获取传输过来的商店数据
        String storeName = intent.getStringExtra("name");
        int storeImg = intent.getIntExtra("img", 0);
        List<String> storeTags = intent.getStringArrayListExtra("tags");
        int storeFoodNum = intent.getIntExtra("storeFoodNum", 0);
        int storeNo = intent.getIntExtra("storeNo", 0);
        double storeFee = intent.getDoubleExtra("storeFee", 0.00);
        thisStore = new Store(storeNo, storeName, storeImg, storeTags, storeFoodNum, storeFee);

        //添加商店的美食数据
        final List<Order> orderList = new ArrayList<>();
        for(int i=0; i<storeFoodNum; i++) {
            Order order;
            //测试数据
            order = new Order(i, "泡椒风爪"+i, R.drawable.ic_food, "用户", "15200000000");

            orderList.add(order);
        }

        //使用传输的数据进行构件的初始化赋值
        imageView.setImageResource(storeImg);
        nameView.setText(storeName);

        //必要，但是不知道有什么用
        GridLayoutManager orderLayoutManager=new GridLayoutManager(this,1);
        orderView.setLayoutManager(orderLayoutManager);
        //设置适配器
        OrderAdapter orderAdapter = new OrderAdapter(orderList);
        orderView.setAdapter(orderAdapter);
    }
}
