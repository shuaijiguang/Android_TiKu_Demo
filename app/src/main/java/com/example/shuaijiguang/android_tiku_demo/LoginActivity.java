package com.example.shuaijiguang.android_tiku_demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import json.LoginResponse;
import tools.AsyncHttpHelper;
import tools.GsonHelper;

public class LoginActivity extends BaseActivity {

    private EditText et_username,et_pwd;

    /**登录按钮*/
    private Button btn_login;

    private TextView tv_forget_pwd,tv_register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){

       // setTitle(getString(R.string.btn_login));

        setTitle("登陆");

        et_username = (EditText)this.findViewById(R.id.et_username);
        et_pwd = (EditText)this.findViewById(R.id.et_pwd);

    //登录

        btn_login = (Button)this.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doLogin();

            }
        });

    //忘记密码

        tv_forget_pwd = (TextView)this.findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, null));
            }
        });

    //注册

        tv_register = (TextView)this.findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisteActivity.class));
            }
        });
    }


 //登陆   存储用户信息

    private void doLogin(){

        RequestParams params = new RequestParams();
        params.put("username", et_username.getText().toString());
        params.put("password", et_pwd.getText().toString());

        AsyncHttpHelper.post(AsyncHttpHelper.URL_LOGIN, params, new AsyncHttpHelper.MyResponseHandler(this) {
            @Override
            public void success(String content) {

                LoginResponse res = GsonHelper.getGson().fromJson(content, LoginResponse.class);

                if(res.success){

                    //登录成功，把User信息保存起来
                    ctx.user = res.user;

                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();

                    //设置成自动登录


                    ctx.setAutoLogin(true);
                    ctx.setUsername(res.user.username);
                    ctx.setPassword(res.user.password);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();



                }else{
                    Toast.makeText(LoginActivity.this, res.reason, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
