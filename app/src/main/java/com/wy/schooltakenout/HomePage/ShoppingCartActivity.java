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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wy.schooltakenout.Adapter.CartAdapter;
import com.wy.schooltakenout.Data.Goods;
import com.wy.schooltakenout.Data.Seller;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    private int userID;
    private int sellerNum;
    private int[][] chosenFood;
    private CartAdapter cartAdapter;

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

        //用上一页面传来的数据提取点菜的信息
        Intent intent = getIntent();
        sellerNum = intent.getIntExtra("sellerNum", 0);
        userID = intent.getIntExtra("userID", 0);
        chosenFood = new int[sellerNum][30];
        for(int i=0; i<sellerNum; i++) {
            for(int j=0; j<30; j++) {
                chosenFood[i][j] = intent.getIntArrayExtra("chosenFood"+i)[j];
            }
        }

        //必要，但是不知道有什么用
        GridLayoutManager foodLayoutManager=new GridLayoutManager(this,1);
        cartList.setLayoutManager(foodLayoutManager);
        //为cartList添加适配器
        cartAdapter = new CartAdapter(chosenFood);
        //为适配器添加点击事件
        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, Seller thisSeller) {
                //进行页面跳转并传递商店数据和购买过的数据
                Intent intent = new Intent(ShoppingCartActivity.this, StoreActivity.class);
                intent.putExtra("sellerID", thisSeller.getSellerID());
                intent.putExtra("userID", userID);
                intent.putExtra("sellerPosition", thisSeller.getSellerPosition());
                //将所有购物车的数据全发过去，防止数据丢失
                intent.putExtra("sellerNum", sellerNum);
                for(int i=0; i<sellerNum; i++) {
                    intent.putExtra("chosenFood"+i, chosenFood[i]);
                }
                viewPosition = position;
                sellerID = thisSeller.getSellerID();
                startActivityForResult(intent, position);
            }

            @Override
            public void onClickDelete(final int position, final Seller thisSeller) {
                new AlertDialog.Builder(ShoppingCartActivity.this)
                        .setTitle("删除这个商店的美食")
                        .setMessage("是否确定")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 获取该商店的美食列表
                                String url = IOTool.ip+"read/good/list.do";
                                List<String> list = new ArrayList<>();
                                list.add("sellerID="+thisSeller.getSellerID());
                                IOTool.upAndDown(url, list);
                                JSONArray json = IOTool.getDateArray();
                                Type type = new TypeToken<List<Goods>>(){}.getType();
                                Gson gson = new Gson();
                                List<Goods> goodsList = gson.fromJson(json.toString(), type);

                                for(int i = 0; i< goodsList.size(); i++) {
                                    chosenFood[thisSeller.getSellerPosition()][i] = 0;
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
        for(int i=0; i<sellerNum; i++) {
            intent.putExtra("chosenFood"+i, chosenFood[i]);
        }
        setResult(resultCode, intent);
        finish();
    }

    //返回的数据，viewPosition为点击的商店所在位置，同时作为请求码
    private int viewPosition;
    private int sellerID;
    @Override
    public void onActivityResult(int position, int resultCode, Intent data) {
        if(position==viewPosition&&(resultCode==StoreActivity.resultCode)){
            //data是上一个Activity调用setResult方法时传递过来的Intent
            for(int i=0; i<sellerNum; i++) {
                for(int j=0; j<30; j++) {
                    chosenFood[i][j] = data.getIntArrayExtra("chosenFood"+i)[j];
                }
            }
            //对改变的美食在页面上进行刷新
            int sellerPosition = data.getIntExtra("sellerPosition", 0);
            cartAdapter.changeFood(position, chosenFood[sellerPosition]);
        }
    }

    //覆写按下物理回退键的事件
    @Override
    public void onBackPressed() {
        back();
    }
}
