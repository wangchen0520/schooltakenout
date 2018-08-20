package com.wy.schooltakenout.BottomNavition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wy.schooltakenout.Adapter.StoreAdapter;
import com.wy.schooltakenout.Data.Store;
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
        storeList = new ArrayList<>();
        List<String> storeTags = new ArrayList<>();
        storeTags.add("不好吃");
        storeTags.add("冷饮");

        storeList.clear();
        for(int i=0; i<10; i++) {
            Store store = new Store("食堂", R.drawable.ic_store_img, storeTags);
            storeList.add(store);
        }

        GridLayoutManager storeLayoutManager=new GridLayoutManager(getContext(),1);
        storeView.setLayoutManager(storeLayoutManager);
        storeView.setAdapter(new StoreAdapter(storeList));
    }
}
