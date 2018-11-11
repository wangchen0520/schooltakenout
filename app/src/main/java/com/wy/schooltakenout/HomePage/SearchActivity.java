package com.wy.schooltakenout.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wy.schooltakenout.Adapter.SearchAdapter;
import com.wy.schooltakenout.Data.Seller;
import com.wy.schooltakenout.R;

public class SearchActivity extends AppCompatActivity {
    private int userID;
    private int sellerNum;
    private int[][] chosenFood;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        init();
    }

    private void init() {
        //获取传来的数据
        Intent intent = getIntent();
        String searchString = intent.getStringExtra("searchString");
        userID = intent.getIntExtra("userID", 0);
        sellerNum = intent.getIntExtra("sellerNum", 0);
        chosenFood = new int[sellerNum][30];
        for(int i=0; i<sellerNum; i++) {
            for(int j=0; j<30; j++) {
                chosenFood[i][j] = intent.getIntArrayExtra("chosenFood"+i)[j];
            }
        }

        //获取布局中的组件
        Toolbar searchToolbar = findViewById(R.id.search_toolbar);
        RecyclerView searchList = findViewById(R.id.search_list);

        //Toolbar上更改标题并添加回退按钮
        searchToolbar.setTitle("搜索内容："+searchString);
        setSupportActionBar(searchToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            searchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        }

        //必要，但是不知道有什么用
        GridLayoutManager foodLayoutManager=new GridLayoutManager(this,1);
        searchList.setLayoutManager(foodLayoutManager);
        //设置适配器，并设置点击事件
        SearchAdapter searchAdapter = new SearchAdapter(searchString);
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, Seller thisSeller) {
                //进行页面跳转并传递商店数据和购买过的数据
                Intent intent = new Intent(SearchActivity.this, StoreActivity.class);
                intent.putExtra("sellerID", thisSeller.getSellerID());
                intent.putExtra("userID", userID);
                intent.putExtra("sellerPosition", thisSeller.getSellerPosition());
                //将所有购物车的数据全发过去，防止数据丢失
                intent.putExtra("sellerNum", sellerNum);
                for(int i=0; i<sellerNum; i++) {
                    intent.putExtra("chosenFood"+i, chosenFood[i]);
                }
                viewPosition = position;
                startActivityForResult(intent, position);
            }
        });
        searchList.setAdapter(searchAdapter);
    }

    //进行回传数据的请求码
    public static int resultCode = 300;
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
    @Override
    public void onActivityResult(int position, int resultCode, Intent data) {
        if(position==viewPosition&&(resultCode==StoreActivity.resultCode)){
            //data是上一个Activity调用setResult方法时传递过来的Intent
            //应该用sellerID来找到生成store，得到storeFoodNum
            for(int i=0; i<sellerNum; i++) {
                for(int j=0; j<30; j++) {
                    chosenFood[i][j] = data.getIntArrayExtra("chosenFood"+i)[j];
                }
            }
        }
        back();
    }

    //覆写按下物理回退键的事件
    @Override
    public void onBackPressed() {
        back();
    }
}
