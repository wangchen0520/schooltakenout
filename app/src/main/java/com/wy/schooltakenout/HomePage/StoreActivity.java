package com.wy.schooltakenout.HomePage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wy.schooltakenout.Adapter.FoodAdapter;
import com.wy.schooltakenout.Data.Food;
import com.wy.schooltakenout.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    //进行点餐所需的数据
    int[] num;
    double totalPrice;

    //进行初始化操作
    private void init() {
        num = new int[100];
        totalPrice = 0.0;
        Intent intent = getIntent();

        //获取传输过来的商店数据
        String storeName = intent.getStringExtra("name");
        int storeImg = intent.getIntExtra("img", 0);
        List<String> storeTags = intent.getStringArrayListExtra("tags");
        //添加一些美食数据用于测试
        final List<Food> foodList = new ArrayList<>();
        String foodName = "泡椒风爪";
        int foodImg = R.drawable.ic_food;
        final double foodPrice = 5.60;
        for(int i=0; i<5; i++) {
            Food food = new Food(foodName+i, storeName, foodImg, foodPrice, 0);
            foodList.add(food);
        }
        //获取布局中的构件
        ImageView imageView = findViewById(R.id.store_img);
        TextView nameView = findViewById(R.id.store_name);
        LinearLayout tagsLayout = findViewById(R.id.store_tags);
        RecyclerView foodView = findViewById(R.id.store_foods);
        final TextView totalPriceView = findViewById(R.id.buy_total_price);
        final TextView feeView = findViewById(R.id.buy_fee);
        ImageButton buyButton = findViewById(R.id.buy);
        //使用传输的数据进行构件的初始化赋值
        imageView.setImageResource(storeImg);
        nameView.setText(storeName);
        totalPriceView.setText(new DecimalFormat("0.00").format(0.00));
        String fee = new DecimalFormat("0.00").format(2.00);
        feeView.setText("配送费"+fee);
        //添加tag栏
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
        //添加美食数据
        //必要，但是不知道有什么用
        GridLayoutManager foodLayoutManager=new GridLayoutManager(this,1);
        foodView.setLayoutManager(foodLayoutManager);
        //设置适配器和点击监听
        final FoodAdapter foodAdapter = new FoodAdapter(foodList);
        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, Food thisFood) {
//                //进行页面跳转并传递美食数据
//                Intent intent = new Intent(StoreActivity.this, StoreActivity.class);
//                Food food = foodList.get(position);
//                intent.putExtra("name", food.getFoodName());
//                intent.putExtra("img",  food.getFoodImg());
//                intent.putExtra("storeName", food.getStoreName());
//                intent.putExtra("price", food.getFoodPrice());
//                startActivity(intent);
//                //测试
//                Toast.makeText(StoreActivity.this, "测试", Toast.LENGTH_SHORT).show();
//            }

            @Override
            public void onClickAdd(View view, int position, Food thisFood) {
                num[position]++;
                Toast.makeText(StoreActivity.this, "买了一个"+thisFood.getFoodName(), Toast.LENGTH_SHORT).show();
                thisFood.setFoodNum(num[position]);
                foodAdapter.notifyDataSetChanged();
                //计算并显示总费用
                totalPrice += thisFood.getFoodPrice();
                totalPriceView.setText(new DecimalFormat("0.00").format(totalPrice));
            }

            @Override
            public void onClickReduce(View view, int position, Food thisFood) {
                if(num[position]>0) {
                    num[position]--;
                    Toast.makeText(StoreActivity.this, "减了一个"+thisFood.getFoodName(), Toast.LENGTH_SHORT).show();
                    thisFood.setFoodNum(num[position]);
                    foodAdapter.notifyDataSetChanged();
                    //计算并显示总费用
                    totalPrice -= thisFood.getFoodPrice();
                    totalPriceView.setText(new DecimalFormat("0.00").format(totalPrice));
                }
            }
        });
        foodView.setAdapter(foodAdapter);

        //进行提交
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalPriceString = new DecimalFormat("0.00").format(totalPrice+2.00);
                Toast.makeText(StoreActivity.this, "总价格为"+totalPriceString+"，提交成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
