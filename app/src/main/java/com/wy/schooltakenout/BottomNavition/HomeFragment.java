package com.wy.schooltakenout.BottomNavition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wy.schooltakenout.Adapter.StoreAdapter;
import com.wy.schooltakenout.Data.Seller;
import com.wy.schooltakenout.Data.User;
import com.wy.schooltakenout.HomePage.SearchActivity;
import com.wy.schooltakenout.HomePage.ShoppingCartActivity;
import com.wy.schooltakenout.HomePage.StoreActivity;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private User user;
    private int[][] shoppingFood;
    private int storeNum;
    //测试数据
//    private int storeNum = 10;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    // 需要传递参数
    public static HomeFragment newInstance(String userID) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("userID", userID);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        // 一些变量
        String url;
        String json;
        Gson gson = new Gson();

        // 获取布局中的组件
        final EditText searchView = view.findViewById(R.id.edit_search);
        ImageButton searchButton = view.findViewById(R.id.imageButton_search);
        RecyclerView storeView = view.findViewById(R.id.store_view);
        FloatingActionButton shoppingCart = view.findViewById(R.id.shopping_cart);

        //一开始不打开软键盘
        if(this.getActivity() != null) {
            this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        // 获取传入的UserID
        String userID = getArguments().getString("userID");
        // 获取User信息
        url = IOTool.ip+"user/info.do";
        List<String> userInfoList = new ArrayList<>();
        userInfoList.add("userID_"+userID);
        json = IOTool.upAndDown(url, userInfoList);
        // 存储User信息
        user = gson.fromJson(json, User.class);

        // 从服务器获取商家列表
        url = IOTool.ip+"user/info.do";
        json = IOTool.upAndDown(url, null);
        // 解析商家列表
        List<Seller> sellerList = new ArrayList<>();
        sellerList.clear();
        Type type = new TypeToken<List<Seller>>(){}.getType();
        sellerList = gson.fromJson(json, type);
        storeNum = sellerList.size();

//        Seller store;
//        //测试数据
//        List<String> storeTags = new ArrayList<>();
//        storeTags.add("不好吃");
//        storeTags.add("冷饮");
//
//        for(int i=0; i<storeNum; i++) {
//            //测试数据
//            store = new Seller(i, "食堂"+i, R.drawable.ic_store_img, storeTags, i+1, 2.00);
//
//            sellerList.add(store);
//        }

        //初始化购物车
        shoppingFood = new int[storeNum][100];

        //美食搜索部分
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchView.getText().toString();
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("searchString", searchText);
                intent.putExtra("storeNum", storeNum);
                for(int i=0; i<storeNum; i++) {
                    intent.putExtra("chosenFood"+i, shoppingFood[i]);
                }
                startActivityForResult(intent, requestCode);
            }
        });

        //必要，但是不知道有什么用
        GridLayoutManager storeLayoutManager=new GridLayoutManager(getActivity(),1);
        storeView.setLayoutManager(storeLayoutManager);
        //设置适配器和点击监听
        StoreAdapter storeAdapter = new StoreAdapter(sellerList);
        storeAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, Seller thisSeller) {
                //进行页面跳转并传递商店数据和购买过的数据
                Intent intent = new Intent(getActivity(), StoreActivity.class);
                intent.putExtra("storeNo", thisSeller.getSellerID());
//                intent.putExtra("name", thisSeller.getStoreName());
//                intent.putExtra("img",  thisSeller.getStoreImg());
//                intent.putExtra("tags", (ArrayList<String>) thisSeller.getStoreTags());
//                intent.putExtra("storeFoodNum", thisSeller.getStoreFoodNum());
//                intent.putExtra("storeFee", thisSeller.getStoreFee());
                //将所有购物车的数据全发过去，防止数据丢失
                intent.putExtra("storeNum", storeNum);
                for(int i=0; i<storeNum; i++) {
                    intent.putExtra("chosenFood"+i, shoppingFood[i]);
                }
                startActivityForResult(intent, requestCode);
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
                startActivityForResult(intent, requestCode);
            }
        });
    }

    //返回的数据
    private static int requestCode = 100;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==HomeFragment.requestCode){
            //data是上一个Activity调用setResult方法时传递过来的Intent
            for(int i=0; i<storeNum; i++) {
                //这里应该用i获得该Store，来获得storeFoodNum
                //测试数据
                int storeFoodNum = i+1;

                for(int j=0; j<storeFoodNum; j++) {
                    shoppingFood[i][j] = data.getIntArrayExtra("chosenFood"+i)[j];
                }
            }
        }
    }
}