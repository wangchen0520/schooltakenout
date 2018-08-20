package com.wy.schooltakenout.BottomNavition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wy.schooltakenout.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyinfoFragment extends Fragment {
    @BindView(R.id.personal_info)
    LinearLayout mPersonalInfo;
    @BindView(R.id.item_1)
    LinearLayout mItem1;
    public static MyinfoFragment newInstance() {
        MyinfoFragment fragment = new MyinfoFragment();
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
        ButterKnife.bind(view);
        return view;
    }
}