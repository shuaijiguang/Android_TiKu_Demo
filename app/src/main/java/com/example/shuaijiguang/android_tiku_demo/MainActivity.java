package com.example.shuaijiguang.android_tiku_demo;

import java.util.ArrayList;

import android.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import circleimageview.CircleImageView;
import entity.MenuListViewItem;
import fragment.CategoryFragment;
import fragment.FavoriteFragment;
import view.ActionBarDrawerToggle;
import view.DrawerArrowDrawable;


///////登陆后 主界面  九宫格（gridview）实现分类练习   侧滑菜单


public class MainActivity extends BaseActivity {
	
	DrawerLayout mDrawerLayout;
	
	/**Fragment的容器*/
	FrameLayout container;
	
	/**侧滑菜单的布局文件*/
	LinearLayout ll;
	
	/**侧滑菜单中的ListView*/
	ListView mDrawerList;
	ArrayList<MenuListViewItem> items = new ArrayList<MenuListViewItem>();
	MyAdapter adapter;
	
	CircleImageView mCircleImageView;
	TextView mTextViewNickName;

	/**侧滑菜单中的设置*/
	TextView mTextViewSetting;

	/**侧滑菜单中的退出*/
	TextView mTextViewExit;
	
	DrawerArrowDrawable drawerArrow;

	ActionBarDrawerToggle mDrawerToggle;

	/**侧滑菜单是否打开*/
	boolean openOrClose;
	
	long temp;
	
	FragmentManager fm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);

		fm = getSupportFragmentManager();
		
		initListViewData();
		initSlidingMenu();
		
		initFragment();
	}
	
	private void initFragment(){

		setTitle(getString(R.string.menu_exercise));

		changeFragment(new CategoryFragment());

	}
	
	private void initSlidingMenu(){
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//设置阴影
		mDrawerLayout.setDrawerShadow(R.drawable.right_shadow, GravityCompat.START);
		
		ll = (LinearLayout) findViewById(R.id.ll);
		mDrawerList = (ListView) findViewById(R.id.navdrawer);
		adapter = new MyAdapter();
		mDrawerList.setAdapter(adapter);
	//	mDrawerList.setItemChecked(0, true);//默认选择第一项
		
		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					changeFragment(new CategoryFragment());
					setTitle(getString(R.string.menu_exercise));
					break;
				case 1:
					startActivity(new Intent(MainActivity.this, SearchActivity.class));
					break;
				case 2:
					
					setTitle(getString(R.string.menu_achievement));
					break;
				case 3:
					changeFragment(new FavoriteFragment());
					setTitle(getString(R.string.menu_fav));
					break;
				}
				mDrawerLayout.closeDrawers();
				openOrClose = false;
			}
		});
		
		mCircleImageView = (CircleImageView)findViewById(R.id.iv_head_img);
		mTextViewNickName = (TextView)findViewById(R.id.tv_nickname);
		mTextViewNickName.setText("shuai");

		mTextViewSetting = (TextView)findViewById(R.id.tv_setting);
		mTextViewSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivity(new Intent(MainActivity.this, SettingActivity.class));

				mDrawerLayout.closeDrawers();
				openOrClose = false;

			}
		});
		
		mTextViewExit = (TextView)findViewById(R.id.tv_exit);
		mTextViewExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.finish();
			}
		});

		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, 0, 0) {


			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
				openOrClose = false;
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
				openOrClose = true;
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();
	}
	
	private void initListViewData() {
		items.add(new MenuListViewItem(R.drawable.home_nav_icon01, getString(R.string.menu_exercise)));
		items.add(new MenuListViewItem(R.drawable.home_nav_icon03,  getString(R.string.menu_find)));
		items.add(new MenuListViewItem(R.drawable.home_nav_icon04,  getString(R.string.menu_achievement)));
		items.add(new MenuListViewItem(R.drawable.home_nav_icon02,  getString(R.string.menu_fav)));
	}
	
	private void changeFragment(Fragment f) {
		FragmentTransaction ft = fm.beginTransaction();
		
		ft.replace(R.id.container, f);
		
		ft.commit();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(ll)) {
				mDrawerLayout.closeDrawer(ll);
				openOrClose = false;
			} else {
				mDrawerLayout.openDrawer(ll);
				openOrClose = true;
			}
		}else if(item.getItemId() == R.id.action_search){
			//Toast.makeText(this, "打开搜索界面~~~", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(MainActivity.this, SearchActivity.class));
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(openOrClose){
				mDrawerLayout.closeDrawers();
			}else{
				long curr = System.currentTimeMillis();
				if(curr - temp < 3000){
					MainActivity.this.finish();
				}else{
					temp = curr;
					Toast.makeText(this, getString(R.string.exit_hint), Toast.LENGTH_SHORT).show();
				}
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private class MyAdapter extends BaseAdapter {
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listview_menu_item, parent, false);
				vh = new ViewHolder();
				vh.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
				vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder)convertView.getTag();
			}
			
			MenuListViewItem item = items.get(position);
			vh.iv_pic.setImageResource(item.pic);
			vh.tv_title.setText(item.title);
			
			return convertView;
		}
		
		private class ViewHolder{
			ImageView iv_pic;
			TextView tv_title;
		}
	}
}
