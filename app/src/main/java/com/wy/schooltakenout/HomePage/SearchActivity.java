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
import com.wy.schooltakenout.Data.Store;
import com.wy.schooltakenout.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private int storeNum;
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
        storeNum = intent.getIntExtra("storeNum", 0);
        chosenFood = new int[storeNum][100];
        for(int i=0; i<storeNum; i++) {
            //测试数据
            for(int j=0; j<i+1; j++) {

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
        SearchAdapter searchAdapter = new SearchAdapter(searchString, storeNum, chosenFood);
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, Store thisStore) {
                //进行页面跳转并传递商店数据和购买过的数据
                Intent intent = new Intent(SearchActivity.this, StoreActivity.class);
                intent.putExtra("storeNo", thisStore.getStoreID());
                intent.putExtra("name", thisStore.getStoreName());
                intent.putExtra("img",  thisStore.getStoreImg());
                intent.putExtra("tags", (ArrayList<String>) thisStore.getStoreTags());
                intent.putExtra("storeFoodNum", thisStore.getStoreFoodNum());
                intent.putExtra("storeFee", thisStore.getStoreFee());
                //将所有购物车的数据全发过去，防止数据丢失
                intent.putExtra("storeNum", storeNum);
                for(int i=0; i<storeNum; i++) {
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
        for(int i=0; i<storeNum; i++) {
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
            //应该用storeNo来找到生成store，得到storeFoodNum
            int storeFoodNum;

            for(int i=0; i<storeNum; i++) {
                //测试数据
                storeFoodNum = i+1;

                for(int j=0; j<storeFoodNum; j++) {
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
