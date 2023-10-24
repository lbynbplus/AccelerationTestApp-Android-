package com.example.accelerationtestapp.;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private TextView register;
    private EditText etUsername;
    private EditText etPassword;
    private TextView login;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化SharedPreferences
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);

        //初始化控件
        register = findViewById(R.id.register);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        login = findViewById(R.id.login);

        //设置点击事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到注册页面
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //Login event
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "The user name or password cannot be empty!", Toast.LENGTH_SHORT).show();
                }else{
                    String name = sharedPreferences.getString("username","");
                    String pwd = sharedPreferences.getString("password","");

                    //判断用户名和密码是否正确
                    if (username.equals(username) && password.equals(pwd)){

                        //登陆成功，转跳主页面，后面可以根据需要的页面调整
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }
}