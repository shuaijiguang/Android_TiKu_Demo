package com.example.shuaijiguang.android_tiku_demo;

import java.lang.reflect.Method;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import tools.AppContext;


/**
 * 基类Activity
 * @author qiujy
 *
 */
public class BaseActivity extends FragmentActivity {

	public AppContext ctx;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		ctx = (AppContext)this.getApplicationContext();
	}
	
	public void initActionBar(String title) {

		setTitle(title);
		
		ActionBar mActionBar = this.getActionBar();

		try {

			Method setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", int.class);

			setHomeAsUpIndicator.invoke(mActionBar, R.drawable.ic_arrow_back_white_24dp);

		} catch (Exception e) {

			Log.e("QuestionListActivity", "setActionBarUpIndicator error", e);

		}

		mActionBar.setDisplayHomeAsUpEnabled(true);

		mActionBar.setHomeButtonEnabled(true);
	}
}
