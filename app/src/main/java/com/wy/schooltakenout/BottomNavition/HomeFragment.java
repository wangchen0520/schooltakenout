package com.wy.schooltakenout.BottomNavition;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wy.schooltakenout.Adapter.StoreAdapter;
import com.wy.schooltakenout.Data.Store;
import com.wy.schooltakenout.HomePage.StoreActivity;
import com.wy.schooltakenout.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
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

    private RecyclerView storeView;
    private List<Store> storeList;

    private void init(View view) {
        storeView = view.findViewById(R.id.store_view);

        //添加一些商店数据用于测试
        storeList = new ArrayList<>();
        List<String> storeTags = new ArrayList<>();
        storeTags.add("不好吃");
        storeTags.add("冷饮");
        storeList.clear();
        for(int i=0; i<10; i++) {
            Store store = new Store("食堂"+i, R.drawable.ic_store_img, storeTags);
            storeList.add(store);
        }

        //必要，但是不知道有什么用
        GridLayoutManager storeLayoutManager=new GridLayoutManager(getActivity(),1);
        storeView.setLayoutManager(storeLayoutManager);
        //设置适配器和点击监听
        StoreAdapter storeAdapter = new StoreAdapter(storeList);
        storeAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Store thisStore) {
                //进行页面跳转并传递商店数据
                Intent intent = new Intent(getActivity(), StoreActivity.class);
                Store store = storeList.get(position);
                intent.putExtra("name", store.getStoreName());
                intent.putExtra("img",  store.getStoreImg());
                intent.putExtra("tags", (ArrayList<String>) store.getStoreTags());
                startActivity(intent);
            }
        });
        storeView.setAdapter(storeAdapter);
    }
}
