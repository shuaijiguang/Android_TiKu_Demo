
package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;

import tools.AppContext;


public class BaseAdapterHelper {


    private final SparseArray<View> views;

    private final Context context;

    private int position;

    private View convertView;


    Object associatedObject;

    protected BaseAdapterHelper(Context context, ViewGroup parent, int layoutId, int position) {
        this.context = context;
        this.position = position;
        this.views = new SparseArray<View>();
        convertView = LayoutInflater.from(context) //
                .inflate(layoutId, parent, false);
        convertView.setTag(this);
    }


    public static BaseAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId) {
        return get(context, convertView, parent, layoutId, -1);
    }

    /** This method is package private and should only be used by QuickAdapter. */
    static BaseAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new BaseAdapterHelper(context, parent, layoutId, position);
        }

        // Retrieve the existing helper and update its position
        BaseAdapterHelper existingHelper = (BaseAdapterHelper) convertView.getTag();
        existingHelper.position = position;
        return existingHelper;
    }


    public <T extends View> T getView(int viewId) {
        return retrieveView(viewId);
    }

    public BaseAdapterHelper setText(int viewId, String value) {
        TextView view = retrieveView(viewId);
        view.setText(value);
        return this;
    }

    public BaseAdapterHelper setImageResource(int viewId, int imageResId) {
        ImageView view = retrieveView(viewId);
        view.setImageResource(imageResId);
        return this;
    }


    public BaseAdapterHelper setBackgroundColor(int viewId, int color) {
        View view = retrieveView(viewId);
        view.setBackgroundColor(color);
        return this;
    }


    public BaseAdapterHelper setBackgroundRes(int viewId, int backgroundRes) {
        View view = retrieveView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }


    public BaseAdapterHelper setTextColor(int viewId, int textColor) {
        TextView view = retrieveView(viewId);
        view.setTextColor(textColor);
        return this;
    }


    public BaseAdapterHelper setTextColorRes(int viewId, int textColorRes) {
        TextView view = retrieveView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
        return this;
    }


    public BaseAdapterHelper setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = retrieveView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public BaseAdapterHelper setImageUrl(int viewId, String imageUrl) {
        ImageView view = retrieveView(viewId);
        
        //TODO: 把默认图像加载器更换为universal-image-loader-1.9.3.jar
        ImageLoader.getInstance().displayImage(imageUrl, view,
        		((AppContext)context.getApplicationContext()).options);
        
        //Picasso.with(context).load(imageUrl).into(view);
        return this;
    }


    public BaseAdapterHelper setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = retrieveView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }


    public BaseAdapterHelper setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            retrieveView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            retrieveView(viewId).startAnimation(alpha);
        }
        return this;
    }


    public BaseAdapterHelper setVisible(int viewId, boolean visible) {
        View view = retrieveView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }


    public BaseAdapterHelper linkify(int viewId) {
        TextView view = retrieveView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /** Apply the typeface to the given viewId, and enable subpixel rendering. */
    public BaseAdapterHelper setTypeface(int viewId, Typeface typeface) {
        TextView view = retrieveView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /** Apply the typeface to all the given viewIds, and enable subpixel rendering. */
    public BaseAdapterHelper setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = retrieveView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }


    public BaseAdapterHelper setProgress(int viewId, int progress) {
        ProgressBar view = retrieveView(viewId);
        view.setProgress(progress);
        return this;
    }


    public BaseAdapterHelper setProgress(int viewId, int progress, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }


    public BaseAdapterHelper setMax(int viewId, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        return this;
    }

    public BaseAdapterHelper setRating(int viewId, float rating) {
        RatingBar view = retrieveView(viewId);
        view.setRating(rating);
        return this;
    }


    public BaseAdapterHelper setRating(int viewId, float rating, int max) {
        RatingBar view = retrieveView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }


    public BaseAdapterHelper setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnClickListener(listener);
        return this;
    }


    public BaseAdapterHelper setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = retrieveView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public BaseAdapterHelper setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }


    public BaseAdapterHelper setTag(int viewId, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(tag);
        return this;
    }


    public BaseAdapterHelper setTag(int viewId, int key, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(key, tag);
        return this;
    }


    public BaseAdapterHelper setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) retrieveView(viewId);
        view.setChecked(checked);
        return this;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public BaseAdapterHelper setAdapter(int viewId, Adapter adapter) {
		AdapterView view = retrieveView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    /** Retrieve the convertView */
    public View getView() {
        return convertView;
    }


    public int getPosition() {
        if (position == -1)
            throw new IllegalStateException("Use BaseAdapterHelper constructor " +
                    "with position if you need to retrieve the position.");
        return position;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /** Retrieves the last converted object on this view. */
    public Object getAssociatedObject() {
        return associatedObject;
    }

    /** Should be called during convert */
    public void setAssociatedObject(Object associatedObject) {
        this.associatedObject = associatedObject;
    }
}
