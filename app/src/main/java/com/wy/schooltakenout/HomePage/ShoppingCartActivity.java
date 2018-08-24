package com.wy.schooltakenout.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wy.schooltakenout.Adapter.CartAdapter;
import com.wy.schooltakenout.R;

public class ShoppingCartActivity extends AppCompatActivity {
    private int storeNum;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_activity);
        init();
    }

    private void init() {
        //获取布局中的控件
        Toolbar toolbar = findViewById(R.id.cart_toolbar);
        RecyclerView cartList = findViewById(R.id.cart_list);

        //在Toolbar上添加回退按钮
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        }

        //用上一页面传来的数据提取Store的信息
        intent = getIntent();
        storeNum = intent.getIntExtra("storeNum", 0);
        int[][] chosenFood = new int[storeNum][100];
        for(int i=0; i<storeNum; i++) {
            for(int j=0; j<100; j++) {
                chosenFood[i][j] = intent.getIntArrayExtra("chosenFood"+i)[j];
            }
        }

        //必要，但是不知道有什么用
        GridLayoutManager foodLayoutManager=new GridLayoutManager(this,1);
        cartList.setLayoutManager(foodLayoutManager);
        //为cartList添加适配器
        CartAdapter cartAdapter = new CartAdapter(chosenFood, storeNum);
        cartList.setAdapter(cartAdapter);
    }

    //进行回传数据的请求码，同时表示本商店美食数量
    public static int resultCode = 100;
    private void back() {
        Intent intent = new Intent();
        for(int i=0; i<storeNum; i++) {
            intent.putExtra("chosenFood"+i, this.intent.getIntArrayExtra("chosenFood"+i));
        }
        setResult(resultCode, intent);
        finish();
    }
}
