package com.wy.schooltakenout.LoginPage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wy.schooltakenout.MainActivity;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.StorePage.StorePageActivity;
import com.wy.schooltakenout.Tool.IOTool;
import com.wy.schooltakenout.Tool.TestPrinter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userPhoneEdit;
    EditText passwordEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        userPhoneEdit = findViewById(R.id.edit_username);
        passwordEdit = findViewById(R.id.edit_password);
        Button loginButton = findViewById(R.id.button_login);
        Button loginStoreButton = findViewById(R.id.button_login_store);
        Button signinButton = findViewById(R.id.button_signin);

        loginButton.setOnClickListener(this);
        loginStoreButton.setOnClickListener(this);
        signinButton.setOnClickListener(this);

        // 判断是否登录过
        String login = IOTool.read("login", this);
        String[] strings = login.split("_");
//        if(strings[0].equals("已登录")) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("userID", Integer.parseInt(strings[1]));
//            startActivity(intent);
//            this.overridePendingTransition(0, 0);
//        }
    }

    @Override
    public void onClick(View view) {
        String url;
        Intent intent;

        switch (view.getId()) {
            case R.id.button_login:
                // 获取登录信息
                String phone = userPhoneEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                // 判断登录信息完整性
                if(!phone.equals("") && !password.equals("")) {
                    // 得到url
                    url = IOTool.ip + "read/user/login.do";
                    // 将数据封装
                    List<String> list = new ArrayList<>();
                    list.add("phone="+phone);
                    list.add("passWord="+password);
                    // 发送往服务器
                    IOTool.upAndDown(url, list);
                    JSONObject json = IOTool.getData();
                    int status = IOTool.getStatus();
                    // 对服务器返回内容进行处理

                    if(status == 0) {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    } else if(status == 1) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        // 将userID从result中解析出来
                        int userID = 0;
                        try {
                            userID = json.getInt("userID");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 保存登录状态
                        IOTool.save("已登录_"+userID, "login", LoginActivity.this);

                        // 将userID传入MainActivity
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                    }
                } else
                    Toast.makeText(LoginActivity.this, "请填写完整", Toast.LENGTH_LONG).show();
                break;
            case R.id.button_login_store:
                // 获取登录信息
                String sellerID = userPhoneEdit.getText().toString();
                String sellerPassword = passwordEdit.getText().toString();
                // 判断登录信息完整性
                if(!sellerID.equals("") && !sellerPassword.equals("")) {
                    // 得到url
                    url = IOTool.ip + "read/seller/login.do";
                    // 将数据封装
                    List<String> list = new ArrayList<>();
                    list.add("sellerID="+sellerID);
                    list.add("passWord="+sellerPassword);
                    // 发送往服务器
                    IOTool.upAndDown(url, list);
                    JSONObject json = IOTool.getData();
                    int status = IOTool.getStatus();
                    // 对服务器返回内容进行处理
                    if (status == 0) {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    } else if (status == 1) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        intent = new Intent(LoginActivity.this, StorePageActivity.class);
                        intent.putExtra("sellerID", Integer.parseInt(sellerID));
                        startActivity(intent);
                    }
                }
                break;
            case R.id.button_signin:
                intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
                break;
        }
    }
}
