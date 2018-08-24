package com.wy.schooltakenout.BottomNavition;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.wy.schooltakenout.Adapter.StoreAdapter;
import com.wy.schooltakenout.Data.Store;
import com.wy.schooltakenout.HomePage.ShoppingCartActivity;
import com.wy.schooltakenout.HomePage.StoreActivity;
import com.wy.schooltakenout.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        init(view);
        return view;
    }

    private List<Store> storeList;
    private int storeNum = 10;
    private int[][] shoppingFood;
    //商店编号，也作为请求码
    public static int storeNo = 0;

    private void init(View view) {
        //获取布局中的组件
        EditText searchView = view.findViewById(R.id.edit_search);
        RecyclerView storeView = view.findViewById(R.id.store_view);
        FloatingActionButton shoppingCart = view.findViewById(R.id.shopping_cart);

        //一开始不打开软键盘
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //添加一些商店数据用于测试
        storeList = new ArrayList<>();
        List<String> storeTags = new ArrayList<>();
        storeTags.add("不好吃");
        storeTags.add("冷饮");
        storeList.clear();
        for(int i=0; i<storeNum; i++) {
            Store store = new Store("食堂"+i, R.drawable.ic_store_img, storeTags);
            storeList.add(store);
        }

        //初始化购物车
        shoppingFood = new int[storeNum][100];

        //必要，但是不知道有什么用
        GridLayoutManager storeLayoutManager=new GridLayoutManager(getActivity(),1);
        storeView.setLayoutManager(storeLayoutManager);
        //设置适配器和点击监听
        StoreAdapter storeAdapter = new StoreAdapter(storeList);
        storeAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Store thisStore) {
                //以该商店的编号作为请求码
                storeNo = position;
                //进行页面跳转并传递商店数据和购买过的数据
                Intent intent = new Intent(getActivity(), StoreActivity.class);
                Store store = storeList.get(position);
                intent.putExtra("name", store.getStoreName());
                intent.putExtra("img",  store.getStoreImg());
                intent.putExtra("tags", (ArrayList<String>) store.getStoreTags());
                //将所有购物车的数据全发过去，防止数据丢失
                intent.putExtra("storeNum", storeNum);
                for(int i=0; i<storeNum; i++) {
                    intent.putExtra("chosenFood"+i, shoppingFood[i]);
                }
                startActivityForResult(intent, storeNo);
            }
        });
        storeView.setAdapter(storeAdapter);

        //添加购物车的点击事件
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
                intent.putExtra("storeNum", storeNum);
                for(int i=0; i<storeNum; i++) {
                    intent.putExtra("chosenFood"+i, shoppingFood[i]);
                }
                int requestCode = storeNo;
                startActivityForResult(intent, requestCode);
            }
        });
    }

    //返回的数据
    @Override
    public void onActivityResult(int storeNo, int foodNum, Intent data) {
        if(storeNo==HomeFragment.storeNo&&(foodNum==StoreActivity.foodNum||foodNum==ShoppingCartActivity.resultCode)){
            //data是上一个Activity调用setResult方法时传递过来的Intent
            for(int i=0; i<storeNum; i++) {
                for(int j=0; j<100; j++) {
                    shoppingFood[i][j] = data.getIntArrayExtra("chosenFood"+i)[j];
                }
            }
        }
    }
}