package com.wy.schooltakenout.HomePage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wy.schooltakenout.Adapter.CartAdapter;
import com.wy.schooltakenout.Data.Store;
import com.wy.schooltakenout.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    private int storeNum;
    private int[][] chosenFood;
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
        chosenFood = new int[storeNum][100];
        for(int i=0; i<storeNum; i++) {
            for(int j=0; j<100; j++) {
                chosenFood[i][j] = intent.getIntArrayExtra("chosenFood"+i)[j];
            }
        }

        //必要，但是不知道有什么用
        GridLayoutManager foodLayoutManager=new GridLayoutManager(this,1);
        cartList.setLayoutManager(foodLayoutManager);
        //为cartList添加适配器
        final CartAdapter cartAdapter = new CartAdapter(chosenFood, storeNum);
        //为适配器添加点击事件
        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onClick(Store thisStore) {
                //进行页面跳转并传递商店数据和购买过的数据
                Intent intent = new Intent(ShoppingCartActivity.this, StoreActivity.class);
                intent.putExtra("name", thisStore.getStoreName());
                intent.putExtra("img",  thisStore.getStoreImg());
                intent.putExtra("tags", (ArrayList<String>) thisStore.getStoreTags());
                intent.putExtra("storeNo", thisStore.getStoreID());
                //将所有购物车的数据全发过去，防止数据丢失
                intent.putExtra("storeNum", storeNum);
                for(int i=0; i<storeNum; i++) {
                    intent.putExtra("chosenFood"+i, chosenFood[i]);
                }
                startActivityForResult(intent, thisStore.getStoreID());
            }

            @Override
            public void onClickDelete(final int position, final Store thisStore) {
                new AlertDialog.Builder(ShoppingCartActivity.this)
                        .setTitle("删除这个商店的美食")
                        .setMessage("是否确定")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i=0; i<100; i++) {
                                    chosenFood[thisStore.getStoreID()][i] = 0;
                                }
                                cartAdapter.deleteStore(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        cartList.setAdapter(cartAdapter);
    }

    //进行回传数据的请求码
    public static int resultCode = 200;
    private void back() {
        Intent intent = new Intent();
        for(int i=0; i<storeNum; i++) {
            intent.putExtra("chosenFood"+i, chosenFood[i]);
        }
        setResult(resultCode, intent);
        finish();
    }
}
