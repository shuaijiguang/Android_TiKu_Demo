
package adapter;

import android.content.Context;

import java.util.List;


public abstract class EnhancedQuickAdapter<T> extends QuickAdapter<T> {


    public EnhancedQuickAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }


    public EnhancedQuickAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected final void convert(BaseAdapterHelper helper, T item) {
        boolean itemChanged = helper.associatedObject == null || !helper.associatedObject.equals(item);
        helper.associatedObject = item;
        convert(helper, item, itemChanged);
    }


    protected abstract void convert(BaseAdapterHelper helper, T item, boolean itemChanged);
}
