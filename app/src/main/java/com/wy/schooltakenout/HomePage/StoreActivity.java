package com.wy.schooltakenout.HomePage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.wy.schooltakenout.Adapter.FoodAdapter;
import com.wy.schooltakenout.Data.Food;
import com.wy.schooltakenout.Data.Store;
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
    private Intent intent;
    private Store thisStore;
    private int storeNum;
    private int[] chosenNum;
    private double totalPrice;

    //进行初始化操作
    private void init() {
        chosenNum = new int[100];
        totalPrice = 0.00;
        intent = getIntent();

        //获取传输过来的商店数据
        String storeName = intent.getStringExtra("name");
        int storeImg = intent.getIntExtra("img", 0);
        List<String> storeTags = intent.getStringArrayListExtra("tags");
        int storeFoodNum = intent.getIntExtra("storeFoodNum", 0);
        int storeNo = intent.getIntExtra("storeNo", 0);
        double storeFee = intent.getDoubleExtra("storeFee", 0.00);
        thisStore = new Store(storeNo, storeName, storeImg, storeTags, storeFoodNum, storeFee);

        storeNum = intent.getIntExtra("storeNum", 0);
        chosenNum = intent.getIntArrayExtra("chosenFood"+storeNo);

        //添加商店的美食数据
        final List<Food> foodList = new ArrayList<>();
        for(int i=0; i<storeFoodNum; i++) {
            Food food;
            //测试数据
            food = new Food(i, "泡椒风爪"+i, storeName, R.drawable.ic_food, 5.60, chosenNum[i]);

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
        Toolbar toolbar = findViewById(R.id.store_toolbar);
        //将Toolbar上标题改为商店名并添加回退按钮，实现回退功能
        toolbar.setTitle(storeName);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        }

        //使用传输的数据进行构件的初始化赋值
        imageView.setImageResource(storeImg);
        nameView.setText(storeName);
        //使用传过来的数据计算已选美食的总价格
        for(int i=0; i<storeFoodNum; i++) {
            totalPrice += chosenNum[i] * foodList.get(i).getFoodPrice();
        }
        totalPriceView.setText(new DecimalFormat("0.00").format(totalPrice));

        String feeString = "配送费"+(new DecimalFormat("0.00").format(thisStore.getStoreFee()));
        feeView.setText(feeString);
        //获取屏幕dpi，使标签可以正常显示（pixel会受分辨率影响，需要转化为dp）
        DisplayMetrics metric = getResources().getDisplayMetrics();
        double ddpi = metric.densityDpi / 160.0;
        //添加tag栏
        for(String storeTag: storeTags) {
            TextView tagView = new TextView(this);
            tagView.setText(storeTag);
            tagView.setTextSize(16);
            tagView.setGravity(Gravity.CENTER);
            tagView.setBackground(this.getResources().getDrawable(R.drawable.ic_tag));
            tagsLayout.addView(tagView);
            tagView.getLayoutParams().width = (int) (80 * ddpi);
            tagView.getLayoutParams().height = (int) (40 * ddpi);
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
            public void onClickAdd(int position, Food thisFood) {
                chosenNum[position]++;
                thisFood.setFoodNum(chosenNum[position]);
                foodAdapter.notifyDataSetChanged();
                //计算并显示总费用
                totalPrice += thisFood.getFoodPrice();
                totalPriceView.setText(new DecimalFormat("0.00").format(Math.abs(totalPrice)));
            }

            @Override
            public void onClickReduce(int position, Food thisFood) {
                if(chosenNum[position]>0) {
                    chosenNum[position]--;
                    thisFood.setFoodNum(chosenNum[position]);
                    foodAdapter.notifyDataSetChanged();
                    //计算并显示总费用
                    totalPrice -= thisFood.getFoodPrice();
                    totalPriceView.setText(new DecimalFormat("0.00").format(Math.abs(totalPrice)));
                }
            }
        });
        foodView.setAdapter(foodAdapter);

        //进行提交
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalPrice - 0 < 0.001) {
                    Toast.makeText(StoreActivity.this, "(#`O′)还什么都没买呢！", Toast.LENGTH_SHORT).show();
                } else {
                    String totalPriceString = new DecimalFormat("0.00").format(totalPrice+thisStore.getStoreFee());
                    Toast.makeText(StoreActivity.this, "总价格为"+totalPriceString+"，提交成功", Toast.LENGTH_SHORT).show();
                    //提交成功后清空选择的项
                    //测试数据
                    for(int i=0; i<thisStore.getStoreFoodNum(); i++) {
                        chosenNum[i] = 0;
                    }
                    back();
                }
            }
        });
    }

    //进行回传数据的请求码
    public static int resultCode = 100;
    private void back() {
        Intent intent = new Intent();
        for(int i=0; i<storeNum; i++) {
            intent.putExtra("chosenFood"+i, this.intent.getIntArrayExtra("chosenFood"+i));
        }
        setResult(resultCode, intent);
        finish();
    }

    //覆写按下物理回退键的事件
    @Override
    public void onBackPressed() {
        back();
    }
}
