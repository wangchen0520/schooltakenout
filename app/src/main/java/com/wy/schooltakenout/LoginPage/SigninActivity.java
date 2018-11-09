package com.wy.schooltakenout.LoginPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wy.schooltakenout.R;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usernameEdit;
    EditText passwordEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        init();
    }

    private void init() {
        usernameEdit = findViewById(R.id.edit_username);
        passwordEdit = findViewById(R.id.edit_password);
        Button signinButton = findViewById(R.id.button_signin);

        signinButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_signin:
                String userPhone = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(!userPhone.equals("") && !password.equals("")) {
                    Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "请完善信息", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
