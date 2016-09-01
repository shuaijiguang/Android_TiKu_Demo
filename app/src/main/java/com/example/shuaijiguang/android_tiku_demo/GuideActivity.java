package com.example.shuaijiguang.android_tiku_demo;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 引导页
 *
 */
@SuppressLint("InflateParams")
public class GuideActivity extends Activity {

	private ImageView iv_1,iv_2,iv_3;

	private ViewPager vp;

	private List<View> pageView = new ArrayList<View>();

	private PagerAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_guide);

		initView();

	}

	private void initView() {


		iv_1 = (ImageView) findViewById(R.id.iv_dot_1);
		iv_2 = (ImageView) findViewById(R.id.iv_dot_2);
		iv_3 = (ImageView) findViewById(R.id.iv_dot_3);

		vp = (ViewPager) findViewById(R.id.vp);

		adapter = new MyAdapter();
		vp.setAdapter(adapter);

		initData();
		adapter.notifyDataSetChanged();
		vp.setPageMargin(2);
		vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {

				GuideActivity.this.iv_1.setImageResource(R.drawable.page_indicator_focused);
				iv_2.setImageResource(R.drawable.page_indicator_focused);
				iv_3.setImageResource(R.drawable.page_indicator_focused);

				if(arg0 == 0) {

					iv_1.setImageResource(R.drawable.page_indicator_unfocused);

				}

				if(arg0 == 1) {

					iv_2.setImageResource(R.drawable.page_indicator_unfocused);

				}

				if(arg0 == 2) {

					iv_3.setImageResource(R.drawable.page_indicator_unfocused);

				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	@Override //禁用按钮
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	private void initData() {

	//关联导航页面的三个布局

		LayoutInflater inflater = LayoutInflater.from(this);
		
		View view1 = inflater.inflate(R.layout.view_guide, null);
		
		View view2 = inflater.inflate(R.layout.view_guide, null);

		view2.setBackgroundResource(R.drawable.guide_2);
		
		View view3 = inflater.inflate(R.layout.view_guide_last, null);

		TextView tv_1 = (TextView) view3.findViewById(R.id.tv);  //获取  开始使用

		tv_1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				GuideActivity.this.finish();

			}

		});

		pageView.add(view1);
		pageView.add(view2);
		pageView.add(view3);

	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return pageView == null ? 0 : pageView.size();

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;

		}

		@Override
		public void destroyItem(View container, int position, Object object) {

			((ViewPager) container).removeView(pageView.get(position));

		}

		@Override
		public Object instantiateItem(View container, int position) {

			((ViewPager) container).addView(pageView.get(position));

			return pageView.get(position);

		}

	}
}
