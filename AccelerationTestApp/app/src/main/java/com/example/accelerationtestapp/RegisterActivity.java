package com.example.accelerationtestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText etUsername;
    private EditText etPassword;
    private TextView register;
    //private SharedPreferences msharedPreferences;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化SharedPreferences
        //msharedPreferences = getSharedPreferences("user",MODE_PRIVATE);

        // 初始化DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        //初始化控件
        mToolbar = findViewById(R.id.toolbar);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        register = findViewById(R.id.register);

        //返回点击事件
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        //注册点击事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "The user name and password cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!databaseHelper.checkUser(username)) {
                        databaseHelper.addUser(username, password);
                        Toast.makeText(RegisterActivity.this, "Registered successfully! Please log in", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }
}