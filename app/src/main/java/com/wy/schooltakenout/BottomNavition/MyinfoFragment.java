package com.wy.schooltakenout.BottomNavition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wy.schooltakenout.R;

public class MyinfoFragment extends Fragment {
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
        return view;
    }
}
