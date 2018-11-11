package com.wy.schooltakenout.LoginPage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        if(strings[0].equals("已登录")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userID", Integer.parseInt(strings[1]));
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        }
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
                    list.add("phone_"+phone);
                    list.add("passWord_"+password);
                    // 发送往服务器
                    String result = IOTool.upAndDown(url, list);
                    // 对服务器返回内容进行处理
                    if(result == null) {
                        Toast.makeText(LoginActivity.this, "服务器连接失败", Toast.LENGTH_LONG).show();
                    } else if(result.equals("0")) {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        // 保存登录状态
                        IOTool.save("已登录_"+result, "login", LoginActivity.this);
                        // result即为userID，将其传入MainActivity
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userID", Integer.parseInt(result));
                        startActivity(intent);
                    }
                } else
                    Toast.makeText(LoginActivity.this, "请填写完整", Toast.LENGTH_LONG).show();
                break;
            case R.id.button_login_store:
                // 得到url
                url = IOTool.ip + "read/seller/login.do";
                // 将数据封装
                String sellerID = userPhoneEdit.getText().toString();
                String sellerPassword = passwordEdit.getText().toString();
                List<String> list = new ArrayList<>();
                list.add("sellerID_"+sellerID);
                list.add("passWord_"+sellerPassword);
                // 发送往服务器
                String result = IOTool.upAndDown(url, list);
                // 对服务器返回内容进行处理
                if(result == null) {
                    Toast.makeText(LoginActivity.this, "服务器连接失败", Toast.LENGTH_LONG).show();
                } else if(result.equals("0")) {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    // result即为sellerID，将其传入StorePageActivity
                    intent = new Intent(LoginActivity.this, StorePageActivity.class);
                    intent.putExtra("sellerID", result);
                    startActivity(intent);
                }
                break;
            case R.id.button_signin:
                intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
                break;
        }
    }
}
