package com.wy.schooltakenout.BottomNavition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wy.schooltakenout.R;

public class AppreciateFragment extends Fragment {
    public static AppreciateFragment newInstance() {
        AppreciateFragment fragment = new AppreciateFragment();
        return fragment;
    }

    public AppreciateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appreciate_fragment, container, false);
        return view;
    }
}
