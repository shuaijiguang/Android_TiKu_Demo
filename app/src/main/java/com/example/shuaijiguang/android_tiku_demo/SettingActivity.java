package com.example.shuaijiguang.android_tiku_demo;

import android.os.Bundle;


public class SettingActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		this.setContentView(R.layout.activity_setting);
		
		initActionBar(getString(R.string.menu_setting));
		

	}

}
