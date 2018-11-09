package com.wy.schooltakenout.LoginPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wy.schooltakenout.Data.Store;
import com.wy.schooltakenout.MainActivity;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.StorePage.StorePageActivity;
import com.wy.schooltakenout.Tool.IOTool;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userPhoneEdit;
    EditText passwordEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        if(login.equals("已登录")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        //测试数据
        List<String> storeTags = new ArrayList<>();
        storeTags.add("不好吃");
        storeTags.add("冷饮");
        Store thisStore = new Store(1, "食堂"+1, R.drawable.ic_store_img, storeTags, 2, 2.00);
        String url;
        String json;

        switch (view.getId()) {
            case R.id.button_login:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                String username = userPhoneEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(username.equals("123") && password.equals("123")) {
                    url = "http://47.107.145.127:8081/resources/head/1.jpg";
                    json = IOTool.upAndDown(url);
                    if(json.equals("")) {
                        Toast.makeText(this, "请求失败", Toast.LENGTH_LONG).show();
                    } else {
                        IOTool.save("已登录", "login", this);
                        Toast.makeText(this, json, Toast.LENGTH_LONG).show();
                    }
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.button_login_store:
                intent = new Intent(LoginActivity.this, StorePageActivity.class);
                String storename = userPhoneEdit.getText().toString();
                String storePassword = passwordEdit.getText().toString();
                if(storename.equals("123") && storePassword.equals("123")) {
                    // 测试数据
                    intent.putExtra("storeNo", thisStore.getStoreID());
                    intent.putExtra("name", thisStore.getStoreName());
                    intent.putExtra("img",  thisStore.getStoreImg());
                    intent.putExtra("tags", (ArrayList<String>) thisStore.getStoreTags());
                    intent.putExtra("storeFoodNum", thisStore.getStoreFoodNum());
                    intent.putExtra("storeFee", thisStore.getStoreFee());
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.button_signin:
                intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
                break;
        }
    }
}
