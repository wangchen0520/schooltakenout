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
import com.wy.schooltakenout.HomePage.SearchActivity;
import com.wy.schooltakenout.HomePage.ShoppingCartActivity;
import com.wy.schooltakenout.HomePage.StoreActivity;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;
import com.wy.schooltakenout.Tool.TestPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private int userID;
    private int[][] shoppingFood;
    private int sellerNum;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    // 需要传递参数
    public static HomeFragment newInstance(int userID) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("userID", userID);
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
        JSONArray json;
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
        userID = getArguments().getInt("userID");

        // 从服务器获取商家列表
        url = IOTool.ip+"read/seller/list.do";
        IOTool.upAndDown(url, null);
        json = IOTool.getDateArray();

        // 解析商家列表
        Type type = new TypeToken<List<Seller>>(){}.getType();
        List<Seller> sellerList = gson.fromJson(json.toString(), type);
        sellerNum = sellerList.size();
        for(int i=0; i<sellerList.size(); i++) {
            sellerList.get(i).setSellerPosition(i);
        }

        //初始化购物车
        shoppingFood = new int[sellerNum][100];

        //美食搜索部分
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchView.getText().toString();
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("searchString", searchText);
                intent.putExtra("userID", userID);
                intent.putExtra("sellerNum", sellerNum);
                for(int i=0; i<sellerNum; i++) {
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
            public void onClick(Seller thisSeller) {
                //进行页面跳转并传递商店数据和购买过的数据
                Intent intent = new Intent(getActivity(), StoreActivity.class);
                intent.putExtra("sellerID", thisSeller.getSellerID());
                intent.putExtra("sellerPosition", thisSeller.getSellerPosition());
                intent.putExtra("userID", userID);
                //将所有购物车的数据全发过去，防止数据丢失
                intent.putExtra("sellerNum", sellerNum);
                for(int i=0; i<sellerNum; i++) {
                    intent.putExtra("chosenFood"+i, shoppingFood[i]);
                }
                startActivityForResult(intent, requestCode);
            }
        });
        storeView.setAdapter(storeAdapter);

        // 购物车的点击事件
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("sellerNum", sellerNum);
                for(int i=0; i<sellerNum; i++) {
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
            for(int i=0; i<sellerNum; i++) {
                for(int j=0; j<30; j++) {
                    shoppingFood[i][j] = data.getIntArrayExtra("chosenFood"+i)[j];
                }
            }
        }
    }
}