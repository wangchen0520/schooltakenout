package com.wy.schooltakenout.HomePage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wy.schooltakenout.Adapter.FoodAdapter;
import com.wy.schooltakenout.Data.Goods;
import com.wy.schooltakenout.Data.Orders;
import com.wy.schooltakenout.Data.Seller;
import com.wy.schooltakenout.Tool.IOTool;
import com.wy.schooltakenout.Tool.OrderView.OrderView;
import com.wy.schooltakenout.Tool.Pay.PayResult;
import com.wy.schooltakenout.Tool.Pay.util.OrderInfoUtil2_0;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.TestPrinter;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {
    //进行点餐所需的数据
    private Seller thisSeller;
    private int sellerPosition;
    private int sellerNum;
    private int[] chosenNum;
    private double totalPrice;
    private List<Goods> goodsList;
    private int userID;

    //支付需要的数据
    private static final int SDK_PAY_FLAG = 1001;
    private String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+rKL2ruz7D3npGdt9gSrmUsXi3EJkD7eP0Elm0vaRtMWIKMuX+PCUxvXiBuSB34azdacjB2qffJHLegGTOaKHupN1WlCAU1RZtvBMg25HQ/SLPgV+1cvejhjiXDdx3L3q3X6+vuOdccYJ3WAxAB9j95TrpDwxTMKMVdbbXN+CPNNV4Dhc5L5+rKP+D3sbxS98SafJAxTw4fNvOlLivNKKDlqXrYOJCyBn3dXnlAbP+uVPd3cvEzWXxxsKctpol1WG3mVa1x/XLDjsckFYN74WfPORQwrg0waYBY+n1o30C6QxFu6IAD9yYzfD92h4/f1oA7khoEtz2wqZ4cLKqo4zAgMBAAECggEBAKCDMAS+KR0IYDZM4Cr9C6Is2A0sIm+dk4drbf8zZQIvzYHb5dgfZle4TdFiwAZcp0Z3qqWgXq+36/wAz0ai5CGifPPzfYUpoP6NNCkVE0bG/Bwjr/if/ilyMZyMyNf/XZlTsJjelTo7Rt2TEKDvW32KNEFSVqGh3rzInkC4JCe7nBwABVfCnoh3y+K0lHrUrMGxeKPKOlH02PhVsik/aNHpyWXs2r8j1pi807RfpoxmooHmrCxRPF74QsDAB8gX8N0Gekuj82KCCR0Rhnm8jp/SiTSU7/yljaAPhWC8rRvaTOAzPR6H9Zofb/xEUffH42PvR08tkEAXx29Zzt4V5UECgYEA6VZMj+wZL9OLl0SUXbjR8llqYjr2T2XNuglbYj+u7D3EEjEHOH6bKbNpB/WfwaHyZtYJ3+PBi7GPCkHje3AYr/JnKOFqGtXRGqVrN0X1cDXgRDkKfX5xhcTxjwHEp9ET4LmsWVJk7axpjH4ky2Hp+bap7MWAlIkeeTgLoyZYY1ECgYEA0TGR2fF8mSZ5M+BRN7CfLcaDMwG8Q3yfvdVHb2ADvxwovyLFgmE+WhOqqBgUwbQ9rQNZFxlwhvBljNR2kj6NxPiYWhQJSZz7U+KerIv8PEesw1t1h/wrKklaAdqdhf03xsI5pCIkBOmikmly4pmUdYbtS5eUU1I7RoVrx0Y7kEMCgYAWfbABi2NeHcjturmGUyNBdebwMvi87HYpAW/ih3rn4Ig+rKUih+mS2m08TCuq8V45FVZ8Uyq7uVCtT26K3mIyy22wHhIpJgTyyZENEdw3hanpSYo+UGrG1xXlj+kRt/Bvj9YSAgQieKTv2tTXGcgAvt+gJ+Vd692ZF5LflfmdEQKBgEeJJvIbdVs939aQj9V2bYiOyNDJeCKv3ERjSbFZaQ6SR5DNCDOEo3jUHtiqm3Z991VRvsZ6aZOLY36cLQvnnj1MMXUkQuTMbNmLtm0U1aWbcQXBIdjlr7EfMktOAe4O4GL0iheJoT2Y04Iz0xiQIkdqY30lEZDInwKHn4qehsHXAoGAEpNq7hry9FdYOtAnOi004X2EgiatedGhRnxks1gMX843BL7NfYgMJojvUfOE6vOo4JhyP23m9xSDRRNdYNfdpi67bSyODlKPVG1T0duNL69269umYL/ZBYhkESG12wHXsNXFy+ntpL38qLF4jXzNE0g5E7kiF6RJ8ChXYoOMMYM=";
    public static final String APPID = "2016092000557984";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    //进行初始化操作
    private void init() {
        String url;
        List<String> list;
        Gson gson = new Gson();
        chosenNum = new int[100];
        totalPrice = 0.00;
        Intent intent = getIntent();

        // 根据传输过来的sellerID获取商店信息
        int sellerID = intent.getIntExtra("sellerID", 0);
        url = IOTool.ip+"read/seller/info.do";
        list = new ArrayList<>();
        list.add("sellerID="+sellerID);
        IOTool.upAndDown(url, list);
        JSONObject jsonObject = IOTool.getData();
        thisSeller = gson.fromJson(jsonObject.toString(), Seller.class);

        sellerNum = intent.getIntExtra("sellerNum", 0);
        sellerPosition = intent.getIntExtra("sellerPosition", 0);
        thisSeller.setSellerPosition(sellerPosition);
        userID = intent.getIntExtra("userID", 0);
        chosenNum = intent.getIntArrayExtra("chosenFood"+sellerPosition);

        // 获取商店的美食列表
        url = IOTool.ip+"read/good/list.do";
        IOTool.upAndDown(url, list);
        JSONArray jsonArray = IOTool.getDateArray();
        Type type = new TypeToken<List<Goods>>(){}.getType();
        goodsList = gson.fromJson(jsonArray.toString(), type);
        for(int i=0; i<goodsList.size(); i++) {
            goodsList.get(i).setNum(chosenNum[i]);
        }

        //获取布局中的构件
        ImageView imageView = findViewById(R.id.store_img);
        TextView nameView = findViewById(R.id.store_name);
        RecyclerView foodView = findViewById(R.id.store_foods);
        final TextView totalPriceView = findViewById(R.id.buy_total_price);
        Button buyButton = findViewById(R.id.buy);
        Toolbar toolbar = findViewById(R.id.store_toolbar);
        //将Toolbar上标题改为商店名并添加回退按钮，实现回退功能
        toolbar.setTitle(thisSeller.getName());
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

        // 读出商家头像
        String filename = thisSeller.getSellerID()+".jpg";
        url = IOTool.pictureIp+"resources/seller/head/"+filename;
        String path = this.getFileStreamPath("store_"+filename).getPath();
        File file = new File(path);
        IOTool.savePicture(url, path);

        // 使用传输的数据进行构件的初始化赋值
        imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        nameView.setText(thisSeller.getName());

        //使用传过来的数据计算已选美食的总价格
        for(int i = 0; i< goodsList.size(); i++) {
            totalPrice += chosenNum[i] * goodsList.get(i).getPrice();
        }
        totalPriceView.setText(new DecimalFormat("0.00").format(totalPrice));

        //添加美食数据
        //必要，但是不知道有什么用
        GridLayoutManager foodLayoutManager=new GridLayoutManager(this,1);
        foodView.setLayoutManager(foodLayoutManager);

        //设置适配器和点击监听
        final FoodAdapter foodAdapter = new FoodAdapter(goodsList);
        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onClickButtom(int position, int variable, OrderView orderView, Goods thisGoods) {
                //加入或移出购物车
                if(variable == 1) {
                    chosenNum[position]++;
                    //计算并显示总费用
                    totalPrice += thisGoods.getPrice();
                    totalPriceView.setText(new DecimalFormat("0.00").format(Math.abs(totalPrice)));
                } else if(variable == -1) {
                    chosenNum[position]--;
                    //计算并显示总费用
                    totalPrice -= thisGoods.getPrice();
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
                    //调用支付宝接口进行支付
                    pay(totalPrice);
                }
            }
        });
    }

    //进行支付的方法
    private void pay(double totalPrice) {
        //秘钥验证的类型 true:RSA2 false:RSA
        boolean rsa = false;
        //构造支付订单参数列表
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, totalPrice, rsa);
        //构造支付订单参数信息
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        //对支付参数信息进行签名
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE, rsa);
        //订单信息
        final String orderInfo = orderParam + "&" + sign;
        //异步处理
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //新建任务
                PayTask alipay = new PayTask(StoreActivity.this);
                //获取支付结果
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //同步获取结果
                    String resultInfo = payResult.getResult();
                    Log.i("Pay", "Pay:" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(StoreActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        // 得到购买的商品数量，用于数组的初始化
                        int num = 0;
                        for(int i = 0; i< goodsList.size(); i++) {
                            if(chosenNum[i] != 0) {
                                num++;
                            }
                        }
                        // 计算总价、得到foodID和foodNum数组
                        int[] foodIDs = new int[num];
                        int[] foodNums = new int[num];
                        int j = 0;
                        for(int i = 0; i< goodsList.size(); i++) {
                            if(chosenNum[i] != 0) {
                                foodIDs[j] = goodsList.get(i).getGoodsID();
                                foodNums[j] = chosenNum[i];
                                j++;
                                // 提交成功后清空选择的项
                                chosenNum[i] = 0;
                            }
                        }
                        // 将foodID和foodNum数组转化为字符串
                        StringBuilder IDsb = new StringBuilder();
                        StringBuilder Numsb = new StringBuilder();
                        for(int i=0; i<num; i++) {
                            IDsb.append(foodIDs[i]);
                            Numsb.append(foodNums[i]);
                            if(i < num-1) {
                                IDsb.append("_");
                                Numsb.append("_");
                            }
                        }
                        String foodIDString = IDsb.toString();
                        String foodNumString = Numsb.toString();
                        // 得到时间
                        Date currentTime = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateString = formatter.format(currentTime);
                        // 生成订单的json数据并上传
                        List<String> jsonList = new ArrayList<>();
                        jsonList.add("sellerID="+thisSeller.getSellerID());
                        jsonList.add("userID="+userID);
                        jsonList.add("totalPrice="+totalPrice);
                        jsonList.add("goodsID="+foodIDString);
                        jsonList.add("goodsNum="+foodNumString);
                        jsonList.add("time="+dateString);
                        IOTool.upAndDown(IOTool.ip+"write/orders/add.do", jsonList);
                        int status = IOTool.getStatus();
                        TestPrinter.print(status+"");
                        back();
                    } else {
                        Toast.makeText(StoreActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    //进行回传数据的请求码
    public static int resultCode = 100;
    private void back() {
        Intent intent = new Intent();
        for(int i=0; i<sellerNum; i++) {
            int[] tempChosen = this.getIntent().getIntArrayExtra("chosenFood"+i);
                intent.putExtra("chosenFood"+i, tempChosen);
        }
        intent.putExtra("sellerPosition", sellerPosition);
        setResult(resultCode, intent);
        finish();
    }

    //覆写按下物理回退键的事件
    @Override
    public void onBackPressed() {
        back();
    }
}
