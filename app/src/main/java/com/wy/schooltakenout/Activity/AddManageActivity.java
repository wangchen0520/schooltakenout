package com.wy.schooltakenout.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wy.schooltakenout.Adapter.AddressAdapter;
import com.wy.schooltakenout.Data.Address;
import com.wy.schooltakenout.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddManageActivity extends AppCompatActivity {

    private String TAG=this.getClass().getName();

    //地址列表和适配器
    private List<Address> addressList=new ArrayList<>();
    private AddressAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.address_list)
    RecyclerView addList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manage);
        initView();
    }

    private void initView(){
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initAddresses();
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        addList.setLayoutManager(layoutManager);
        adapter=new AddressAdapter(addressList);
        addList.setAdapter(adapter);
    }

    //初始化地址列表数据
    private void initAddresses(){
        addressList.clear();
        Address address=new Address("汪浩","17796203147","四川大学江安校区文科楼三区B1025");
        for(int i=0;i<5;i++){
            addressList.add(address);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.address_manage_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:finish();break;
            case R.id.add:
                Log.d(TAG,"添加新地址");
                break;
            default:
        }
        return true;
    }
}
