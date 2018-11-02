package com.wy.schooltakenout.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.wy.schooltakenout.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifyInfoActivity extends AppCompatActivity {

    private String TAG=this.getClass().getName();

    //toolbar
    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    //修改头像
    @BindView(R.id.modify_headimage)
    RelativeLayout modifyImg;

    //修改昵称
    @BindView(R.id.name)
    EditText editName;

    //修改手机号
    @BindView(R.id.phone_num)
    EditText editPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        initView();
    }

    private void initView(){
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_info_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:finish();break;
            case R.id.complete:
                Log.d(TAG,"complete");
                break;
            default:
        }
        return true;
    }
}
