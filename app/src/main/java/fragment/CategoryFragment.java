package fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.shuaijiguang.android_tiku_demo.QuestionListActivity;
import com.example.shuaijiguang.android_tiku_demo.R;
import com.google.gson.reflect.TypeToken;

import adapter.QuickAdapter;
import pojo.Catelog;
import tools.AsyncHttpHelper;
import adapter.BaseAdapterHelper;
import tools.GsonHelper;
import tools.LogHelper;



 //类别列表界面

public class CategoryFragment extends BaseFragment {
	private static final String TAG = "CategoryFragment";
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private GridView mGridView;
	
	private QuickAdapter<Catelog> adapter;
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogHelper.d(TAG, "onCreateView()-------------");
		
		View root = inflater.inflate(R.layout.fragment_quest_category, container, false);
		
		initSwipe(root);
		initGridView(root);

		mSwipeRefreshLayout.setRefreshing(true);
		loadRemoteData();
		
		return root;
	}
	
	@Override 
    public void setUserVisibleHint(boolean isVisibleToUser) {
    	super.setUserVisibleHint(isVisibleToUser);
    	
    	if(getUserVisibleHint()){

    	}else{
    		
    	}
    }
	
	private void initSwipe(View root){
		mSwipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				LogHelper.d(TAG, "开始刷新~~~");
				
				loadRemoteData();
			}
		});
	}
	
	private void initGridView(View root){
		mGridView = (GridView)root.findViewById(R.id.grid_view);
		
		adapter = new QuickAdapter<Catelog>(this.getActivity(), R.layout.gridview_item) {
			@Override
			protected void convert(BaseAdapterHelper helper, Catelog item) {
				helper.setText(R.id.tv_cate_title, item.name);
				helper.setImageUrl(R.id.iv_cate_icon, 
						AsyncHttpHelper.URL_IMG_BASE + "/" + item.icon);
			}
		};
		//adapter = new MyAdapter();
		mGridView.setAdapter(adapter);
		
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CategoryFragment.this.ctx, QuestionListActivity.class);
				intent.putExtra("cate_id", adapter.getItem(position).id);
				intent.putExtra("cate_name", adapter.getItem(position).name);

				startActivity(intent);
			}
		});
	}
	
	private void loadRemoteData(){
		AsyncHttpHelper.get(AsyncHttpHelper.URL_CATALOG_LIST,
				new AsyncHttpHelper.MyResponseHandler(this.getActivity()) {
			@Override
			public void success(String content) {
				ArrayList<Catelog> list = GsonHelper.getGson().fromJson(content,
						new TypeToken<ArrayList<Catelog>>(){}.getType());
				
				mSwipeRefreshLayout.setRefreshing(false);
				
				adapter.addAll(list);
				adapter.notifyDataSetChanged();
			}
		});
	}
	

}
