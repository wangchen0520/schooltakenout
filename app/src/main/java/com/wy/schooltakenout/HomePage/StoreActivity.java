package com.wy.schooltakenout.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wy.schooltakenout.R;

import java.util.List;

public class StoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);
        init();
    }

    //进行初始化操作
    private void init() {
        Intent intent = getIntent();

        //获取传输过来的商店数据
        String storeName = intent.getStringExtra("name");
        int storeImg = intent.getIntExtra("img", 0);
        List<String> storeTags = intent.getStringArrayListExtra("tags");
        //获取布局中的构件
        ImageView imageView = findViewById(R.id.store_img);
        TextView nameView = findViewById(R.id.store_name);
        LinearLayout tagsLayout = findViewById(R.id.store_tags);
        //使用传输的数据进行构件的赋值
        imageView.setImageResource(storeImg);
        nameView.setText(storeName);
        for(String storeTag: storeTags) {
            TextView tagView = new TextView(this);
            tagView.setWidth(140);
            tagView.setHeight(70);
            tagView.setText(storeTag);
            tagView.setTextSize(10);
            tagView.setGravity(Gravity.CENTER);
            tagView.setBackground(this.getResources().getDrawable(R.drawable.ic_tag));
            tagsLayout.addView(tagView);
        }
    }
}
