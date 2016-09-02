package tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.AsyncTask;




public class CacheHelper {
	private static final String TAG = "CacheHelper";
	
	private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb

	private static DiskLruCache sCache;

	public synchronized static DiskLruCache getDiskLruCache(Context ctx) {
		if(null == sCache){
			try {
				sCache =  DiskLruCache.open(new File(AppContext.CACHE_DIR_FILE),
						SystemHelper.getAppVersionCode(ctx), 1, MAX_SIZE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sCache;
	}

	public static DiskLruCache.Editor editor(String url, String txt) {
		DiskLruCache.Editor mEditor = null;
		String key = MD5Encoder.encode(url);
		try {
			mEditor = sCache.edit(key);
			if (mEditor != null) {
				OutputStream outputStream = mEditor.newOutputStream(0);
				if (IOHelper.stringToStream(txt, outputStream)) {
					mEditor.commit();
				} else {
					mEditor.abort();
				}
			}
			sCache.flush();
			LogHelper.i(TAG, "成功将" + url + "对应的内容写入缓存！");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return mEditor;
	}

	/** 
     * 将缓存记录同步到journal文件中。 
     */  
    public static void fluchCache() {  
        if (sCache != null) {  
            try {  
            	sCache.flush();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	
	/**
	 * 从缓存中获取缓存的文件字符串
	 * @param
	 * @param url
	 * @return
	 */
	public static String getTxtFromSnapshot(String url) {
		String txt = null;
		if(url == null){
			return null;
		}
		String key = MD5Encoder.encode(url);
		DiskLruCache.Snapshot snapShot = null;
		InputStream is = null;
		try {
			snapShot = sCache.get(key);
			if (snapShot != null) {
				is = snapShot.getInputStream(0);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int i;
				while ((i = is.read()) != -1) {
					baos.write(i);
				}
				LogHelper.i(TAG, "成功将" + url + "对应的内容从缓存中读取！");
				txt = baos.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return txt;
	}

	
	public static void remove(Context ctx, String url){
		String key = MD5Encoder.encode(url);  
		try {
			sCache.remove(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将所有的缓存数据全部删除
	 */
	@SuppressWarnings("static-access")
	public static void removeAll(){
		try {
			sCache.deleteContents(new File(AppContext.CACHE_DIR_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class clearTask extends AsyncTask<Integer, Integer, Boolean> {

		@SuppressWarnings("static-access")
		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				sCache.deleteContents(new File(AppContext.CACHE_DIR_FILE));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			//cl.clearOk(result);
		}
	}
	
	
	
	interface clear {
		public void clearOk(boolean isOk);
	}
	
	/**
	 * 将DiskLruCache关闭掉,关闭掉了之后就不能再进行任何操作
	 */
	public static void close(){
		try {
			sCache.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取缓存大小
	 */
	public static String getCacheSize(){
		LogHelper.i("CacheSize", sCache.size()+"");
		if((sCache.size() / 1024) > 0) {
			return (sCache.size() / 1024) + "kb";
		} else {
			return "0kb";
		}
	}
}
