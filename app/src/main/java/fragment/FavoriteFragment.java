package fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shuaijiguang.android_tiku_demo.QuestionDetailActivity;
import com.example.shuaijiguang.android_tiku_demo.R;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import adapter.BaseAdapterHelper;
import adapter.QuickAdapter;
import pojo.Page;
import pojo.Question;
import tools.AsyncHttpHelper;
import tools.GsonHelper;


public class FavoriteFragment extends BaseFragment {

	SwipeRefreshLayout mSwipeRefreshLayout;
	ListView mListView;
	LinearLayout listview_foot;
	ProgressBar listview_foot_progress;
	TextView listview_foot_more;
	
	private QuickAdapter<Question> adapter;
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.activtiy_quest_list, container, false);
		
		types = getResources().getStringArray(R.array.question_type);
		
		initSwipeRefreshLayout(root);
		
		initListView(root);
		
		mSwipeRefreshLayout.setRefreshing(true);
		loadRemoteData();
		
		return root;
	}
	
	private void initSwipeRefreshLayout(View root){
		mSwipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				page = 1;
				loadRemoteData();
			}
		});
	}
	
	@SuppressLint("InflateParams")
	private void initListView(View root){
		mListView = (ListView)root.findViewById(R.id.lv);
		
		//添加FooterView
		listview_foot = (LinearLayout)LayoutInflater.from(this.getActivity()).inflate(R.layout.listview_footer, null);
		listview_foot_progress = (ProgressBar)listview_foot.findViewById(R.id.listview_foot_progress);
		listview_foot_more = (TextView)listview_foot.findViewById(R.id.listview_foot_more);
		mListView.addFooterView(listview_foot);
		mListView.setFooterDividersEnabled(false);
		
		adapter = new QuickAdapter<Question>(this.getActivity(), R.layout.listview_questlist_item) {
			@Override
			protected void convert(BaseAdapterHelper helper, Question item) {
				helper.setText(R.id.tv_title, item.content);
				
				String pubTime = DateUtils.formatDateTime(FavoriteFragment.this.getActivity(), 
						item.pubTime.getTime(),
						DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE);
				helper.setText(R.id.tv_pubTime, pubTime);
				
				helper.setText(R.id.tv_title, item.content);
				helper.setText(R.id.tv_type, types[item.typeid - 1]);
			}
		};
		mListView.setAdapter(adapter);
		
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState
						&& view.getLastVisiblePosition() == view.getCount() - 1){
					if(hasMore && FavoriteFragment.this.page < FavoriteFragment.this.totalPages) {
						FavoriteFragment.this.page++;
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
				
				Intent intent = new Intent(FavoriteFragment.this.ctx,
						QuestionDetailActivity.class);
				
				intent.putExtra("id", adapter.getItem(position).id);
				intent.putExtra("totalElements", totalElements);
				intent.putExtra("position", position);
				
				startActivity(intent);
			}
		});
	}
	
	private void loadRemoteData(){
		if(page == 1){
			adapter.clear();
		}
		
		RequestParams params = new RequestParams();
		params.put("userId", ctx.user.id);
		params.put("page", page);
		
		AsyncHttpHelper.get(AsyncHttpHelper.URL_FAVORITE_LIST, params,
		new AsyncHttpHelper.MyResponseHandler(this.getActivity()) {
			@Override
			public void success(String content) {
				mSwipeRefreshLayout.setRefreshing(false); //关闭刷新条
				
				Page<Question> pager = GsonHelper.getGson().fromJson(content,
						new TypeToken<Page<Question>>(){}.getType());
				
				totalPages = pager.totalPages;
				totalElements = pager.totalElements;
				
				adapter.addAll(pager.content);
				
				
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
}
