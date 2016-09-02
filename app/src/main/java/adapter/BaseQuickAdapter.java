
package adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public abstract class BaseQuickAdapter<T, H extends BaseAdapterHelper> extends BaseAdapter {

    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();

    protected final Context context;

    protected final int layoutResId;

    protected final List<T> data;

    protected boolean displayIndeterminateProgress = false;


    public BaseQuickAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }


    public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        int extra = displayIndeterminateProgress ? 1 : 0;
        return data.size() + extra;
    }

    @Override
    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position >= data.size() ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            final H helper = getAdapterHelper(position, convertView, parent);
            T item = getItem(position);
            helper.setAssociatedObject(item);
            convert(helper, item);
            return helper.getView();
        }

        return createIndeterminateProgressView(convertView, parent);
    }

    private View createIndeterminateProgressView(View convertView, ViewGroup parent) {
        if (convertView == null) {
        	
        	//TODO
            LinearLayout container = new LinearLayout(context);
            container.setOrientation(LinearLayout.HORIZONTAL);
            //container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            container.setGravity(Gravity.CENTER);
            
            //FrameLayout container = new FrameLayout(context);
            //container.setForegroundGravity(Gravity.CENTER);
            ProgressBar progress = new ProgressBar(context, null, android.R.attr.progressBarStyle);
            container.addView(progress);
            
            TextView tv = new TextView(context);
            tv.setText("数据加载中...");
            container.addView(tv);
            
            convertView = container;
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return position < data.size();
    }

    public void add(T elem) {
        data.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    /** Clear data list */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void showIndeterminateProgress(boolean display) {
        if (display == displayIndeterminateProgress) return;
        displayIndeterminateProgress = display;
        notifyDataSetChanged();
    }


    protected abstract void convert(H helper, T item);

    protected abstract H getAdapterHelper(int position, View convertView, ViewGroup parent);

}
