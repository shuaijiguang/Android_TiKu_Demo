package com.example.shuaijiguang.android_tiku_demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import tools.AsyncHttpHelper;
import tools.GsonHelper;
import tools.LogHelper;
import tools.LoginResponse;
import tools.SystemHelper;


public class WelcomeActivity extends BaseActivity {


    @SuppressLint("InflateParams")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


     //关联布局   需要让整个页面实现 动画效果 和 跳转 功能  所以先要得到一个视图view，对视图进行操作
        LayoutInflater inflater = LayoutInflater.from(this);
        View root = inflater.inflate(R.layout.activity_welcome, null);

        setContentView(root);

     //渐变展示启动屏    加载XML文件的方法

        Animation aa = AnimationUtils.loadAnimation(this, R.anim.alpha);

        root.startAnimation(aa);


    //直接代码实现法
//        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
//
//        aa.setDuration(3000);
//
//        root.startAnimation(aa);

        aa.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {

            //在动画结束时执行跳转页面操作

                doJump();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationStart(Animation animation) {}


        });
    }

//    @Override
//    protected void onRestart() {
//
//        super.onRestart();
//
//        doJump();
//    }


    private void doJump(){

        //如果是第一次运行程序，就需要先启动引导页

        boolean first = this.ctx.sysSharedPreferences.getBoolean("first", true);

        LogHelper.d("WelcomeActivity", "WelcomeActivity-->first==" + first);

        if(first){

            startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));

            this.ctx.sysSharedPreferences

                    .edit()
                    .putBoolean("first", false)
                    .commit();

        }else{

            //FIXME 检查网络
            if(-1 == SystemHelper.getNetWorkType(WelcomeActivity.this)){
                //先弹出对话框提示
                //使用隐式Intent打开网络设置程序

                Toast.makeText(WelcomeActivity.this, "网络不可用，请检查！", Toast.LENGTH_LONG).show();
                return ;
            }

            //判断是否要自动登录

            if(ctx.isAutoLogin()){

             //执行自动登录
                autoLogin();

            }else{

                //跳转到登录界面
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                WelcomeActivity.this.finish();
            }
        }
    }

    @Override //禁用按钮
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    private void autoLogin(){

        String username = ctx.getUsername();
        String password = ctx.getPassword();

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){

            RequestParams params = new RequestParams();
            params.put("username", username);
            params.put("password", password);

            AsyncHttpHelper.post(AsyncHttpHelper.URL_LOGIN, params,
                    new AsyncHttpHelper.NoProgressResponseHandler(WelcomeActivity.this){
                        @Override
                        public void success(String content) {

                            LoginResponse res = GsonHelper.getGson().fromJson(content, LoginResponse.class);

                            if(res.success){
                                //登录成功，把User信息保存起来
                                ctx.user = res.user;

                                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

                            }else{

                                 startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));

                            }

                            WelcomeActivity.this.finish();
                        }
                    });
        }else{

            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        }

    }
}
