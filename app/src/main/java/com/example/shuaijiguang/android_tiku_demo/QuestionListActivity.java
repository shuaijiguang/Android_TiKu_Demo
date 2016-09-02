package com.example.shuaijiguang.android_tiku_demo;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import adapter.BaseAdapterHelper;
import adapter.QuickAdapter;
import pojo.Page;
import pojo.Question;
import tools.AsyncHttpHelper;
import tools.GsonHelper;
import tools.LogHelper;

/**
 * 试题列表界面
 */
@SuppressLint("InflateParams")
public class QuestionListActivity extends BaseActivity {
	private static final String TAG = "QuestionListActivity";
	ActionBar mActionBar;
	
	//PullDownView mPullDownView;
	SwipeRefreshLayout mSwipeRefreshLayout;
	ListView mListView;
	LinearLayout listview_foot;
	ProgressBar listview_foot_progress;
	TextView listview_foot_more;
	
	private QuickAdapter<Question> adapter;
	
	private int cate_id;
	/**当前页号*/
	private int page = 1;
	/**总记录数*/
	private long totalElements = 0; 
	/**总页数*/
	private int totalPages = 1;
	/**是否有下一页*/
	private boolean hasMore = false;
	
	
	private String [] types;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activtiy_quest_list);

		types = getResources().getStringArray(R.array.question_type);
		cate_id = this.getIntent().getIntExtra("cate_id", 0);
		
		initActionBar(this.getIntent().getStringExtra("cate_name"));
		
		initSwipeRefreshLayout();
		
		initListView();
		
		mSwipeRefreshLayout.setRefreshing(true);
		loadRemoteData();
	}
	
	private void initSwipeRefreshLayout(){
		mSwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				LogHelper.d(TAG, "开始刷新~~~");
				
				page = 1;

				loadRemoteData();
			}
		});
	}
	
	private void loadRemoteData(){
		if(page == 1){

			adapter.clear();
		}
		
		RequestParams params = new RequestParams();

		params.put("catalogId", cate_id);
		params.put("page", page);
		
		AsyncHttpHelper.get(AsyncHttpHelper.URL_QUESTION_LIST, params,
		new AsyncHttpHelper.MyResponseHandler(this) {
			@Override
			public void success(String content) {
				mSwipeRefreshLayout.setRefreshing(false); //关闭刷新条
				
				Page<Question> pager = GsonHelper.getGson().fromJson(content,
						new TypeToken<Page<Question>>(){}.getType());
				
				totalPages = pager.totalPages;
				totalElements = pager.totalElements;
				
				if(null != pager.content){
					adapter.addAll(pager.content);
				}
				
				//数据加载完成后，判断是否还有下一页
				if(page < totalPages){
					hasMore = true;
					listview_foot_progress.setVisibility(View.VISIBLE);
					listview_foot_more.setText(getString(R.string.doing_update));
					listview_foot_more.setVisibility(View.VISIBLE);
				}else{
					hasMore = false;
					listview_foot_progress.setVisibility(View.GONE);
					listview_foot_more.setText(getString(R.string.load_all));
					listview_foot_more.setVisibility(View.VISIBLE);
				}
				
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	
	private void initListView(){
		mListView = (ListView)findViewById(R.id.lv);
		
		//添加FooterView
		listview_foot = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.listview_footer, null);
		listview_foot_progress = (ProgressBar)listview_foot.findViewById(R.id.listview_foot_progress);
		listview_foot_more = (TextView)listview_foot.findViewById(R.id.listview_foot_more);
		mListView.addFooterView(listview_foot);
		mListView.setFooterDividersEnabled(false);
		
		adapter = new QuickAdapter<Question>(this, R.layout.listview_questlist_item) {
			@Override
			protected void convert(BaseAdapterHelper helper, Question item) {
				helper.setText(R.id.tv_title, item.content);
				
				String pubTime = DateUtils.formatDateTime(QuestionListActivity.this, 
						item.pubTime.getTime(),
						DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE);
				helper.setText(R.id.tv_pubTime, pubTime);
				
				helper.setText(R.id.tv_title, item.content);
				helper.setText(R.id.tv_type, types[item.typeid - 1]);
			}
		};
		//adapter = new QuestListAdapter();
		mListView.setAdapter(adapter);
		
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState
						&& view.getLastVisiblePosition() == view.getCount() - 1){
					if(hasMore && QuestionListActivity.this.page < QuestionListActivity.this.totalPages) {
						QuestionListActivity.this.page++;
						loadRemoteData();
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(QuestionListActivity.this.ctx,
						QuestionDetailActivity.class);
				
				intent.putExtra("id", adapter.getItem(position).id);
				intent.putExtra("totalElements", totalElements);
				intent.putExtra("position", position);
				
				startActivity(intent);
			}
		});
	}
}
