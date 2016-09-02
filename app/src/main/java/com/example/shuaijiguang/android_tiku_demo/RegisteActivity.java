package com.example.shuaijiguang.android_tiku_demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.loopj.android.http.RequestParams;


import tools.AsyncHttpHelper;
import tools.GsonHelper;
import tools.Response;

/**
 * 注册界面
 */
public class RegisteActivity extends BaseActivity{

	private EditText mEditText;
	private EditText mEditText2;
	private EditText mEditText3;
	private EditText mEditText4;

	/**登录按钮*/
	private Button mButton1;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_register);
		
		initView();
	}
	
	private void initView(){

		setTitle(getString(R.string.tv_register));
		
		mEditText = (EditText)this.findViewById(R.id.et_username);
		mEditText2 = (EditText)this.findViewById(R.id.et_pwd);
		mEditText3 = (EditText)this.findViewById(R.id.et_nickname);
		mEditText4 = (EditText)this.findViewById(R.id.et_phone);
		
		mButton1 = (Button)this.findViewById(R.id.reg_ok);
		mButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doRegiste();
			}
		});
	}
	
	private void doRegiste(){
		RequestParams params = new RequestParams();
		params.put("username", mEditText.getText().toString());
		params.put("password", mEditText2.getText().toString());
		params.put("nickname", mEditText3.getText().toString());
		params.put("telephone", mEditText4.getText().toString());
		
		AsyncHttpHelper.post(AsyncHttpHelper.URL_REGISTE, params,
		new AsyncHttpHelper.MyResponseHandler(this) {
			@Override
			public void success(String content) {
				Response res = GsonHelper.getGson().fromJson(content, Response.class);
				if(res.success){
					RegisteActivity.this.finish();
				}else{
					Toast.makeText(RegisteActivity.this, res.reason, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
