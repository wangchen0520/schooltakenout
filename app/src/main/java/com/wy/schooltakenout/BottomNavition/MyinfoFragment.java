package com.wy.schooltakenout.BottomNavition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wy.schooltakenout.Activity.AboutUsActivity;
import com.wy.schooltakenout.Activity.AddManageActivity;
import com.wy.schooltakenout.Activity.CollectionActivity;
import com.wy.schooltakenout.Activity.CommentActivity;
import com.wy.schooltakenout.Activity.ModifyInfoActivity;
import com.wy.schooltakenout.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyinfoFragment extends Fragment {
    private int userID;
    private String TAG=this.getClass().getName();

    //个人信息
    @BindView(R.id.personal_info)
    LinearLayout personalInfo;
    //收藏
    @BindView(R.id.collection)
    LinearLayout collection;
    //评论
    @BindView(R.id.comment)
    LinearLayout comment;
    //地址管理
    @BindView(R.id.address_management)
    LinearLayout addressManagement;
    //关于我们
    @BindView(R.id.about_us)
    LinearLayout aboutUs;

    public static MyinfoFragment newInstance() {
        MyinfoFragment fragment = new MyinfoFragment();
        return fragment;
    }

    // 需要传递参数
    public static MyinfoFragment newInstance(int userID) {
        MyinfoFragment fragment = new MyinfoFragment();
        Bundle args = new Bundle();
        args.putInt("userID", userID);
        fragment.setArguments(args);
        return fragment;
    }

    public MyinfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myinfo_fragment, container, false);
        initView(view);
        return view;
    }

    public void initView(View view){
        ButterKnife.bind(this,view);
        MyListener lis=new MyListener();
        personalInfo.setOnClickListener(lis);
        collection.setOnClickListener(lis);
        comment.setOnClickListener(lis);
        addressManagement.setOnClickListener(lis);
        aboutUs.setOnClickListener(lis);

        // 得到传来的userID
        userID = getArguments().getInt("userID");
    }

    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.personal_info:
                    Log.d(TAG,"修改个人信息");
                    Intent intent=new Intent(getContext(), ModifyInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.collection:
                    Log.d(TAG,"跳转收藏界面");
                    Intent col=new Intent(getContext(), CollectionActivity.class);
                    startActivity(col);
                    break;
                case R.id.address_management:
                    Log.d(TAG,"跳转地址管理界面");
                    Intent add=new Intent(getContext(), AddManageActivity.class);
                    startActivity(add);
                    break;
                case R.id.about_us:
                    Log.d(TAG,"关于我们");
                    Intent abo=new Intent(getContext(), AboutUsActivity.class);
                    startActivity(abo);
                    break;
                case R.id.comment:
                    Log.d(TAG,"跳转评论界面");
                    Intent com=new Intent(getContext(), CommentActivity.class);
                    startActivity(com);
                default:
            }
        }
    }
}