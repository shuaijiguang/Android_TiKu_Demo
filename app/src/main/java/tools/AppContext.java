package tools;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;


import com.example.shuaijiguang.android_tiku_demo.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import pojo.User;


 // 本应用程序的应下用上文

public class AppContext extends Application {
	private static final String CACHE_DIR_NAME = "/qbank";
	private static final String TAG = "AppContext";
	
	/**缓存目录全路径名*/
	public static final String CACHE_DIR;
	/**文件缓存目录全路径名*/
	public static final String CACHE_DIR_FILE;
	/**图片缓存目录全路径名*/
	public static final String CACHE_DIR_IMAGE;
	
	/**图像加载器的显示参数 */
	public DisplayImageOptions options;
	/**登录后的用户信息*/
	public User user;
	/**系统默认的共享首选项*/
	public SharedPreferences sysSharedPreferences;
	
	private boolean autoLogin;
	private boolean showImg;
	private String username;
	private String password;
	
	static {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
				&& !Environment.isExternalStorageRemovable()) {
			
			CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + CACHE_DIR_NAME;
		} else {
			CACHE_DIR = Environment.getRootDirectory().getAbsolutePath() + CACHE_DIR_NAME;
		}
		CACHE_DIR_IMAGE = CACHE_DIR + "/pic";
		CACHE_DIR_FILE = CACHE_DIR + "/tmp";
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		
		createCacheDir();
		
		initSystemSetting();
		
		initImageLoader(this);
	}
	
	public void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getCacheDirectory(context);
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)

		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 2) // default
		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))

		        .diskCache(new UnlimitedDiscCache(cacheDir)) // default

		        .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // default HashCodeFileNameGenerator
		        .imageDownloader(new BaseImageDownloader(context)) // default
		        //.imageDecoder(new BaseImageDecoder()) // default
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
		        .writeDebugLogs()
		        .build();
		
		ImageLoader.getInstance().init(config);
		
		
		options = new DisplayImageOptions.Builder()
	        .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
	        .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
	        .showImageOnFail(R.drawable.ic_error) // resource or drawable
	        .resetViewBeforeLoading(false)  // default false
	        .delayBeforeLoading(0)
	        .cacheInMemory(true) // default false
	        .cacheOnDisk(true) // default false

	        .considerExifParams(false) // default
	        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
	        .bitmapConfig(Bitmap.Config.ARGB_8888) // default

	        .displayer(new SimpleBitmapDisplayer()) // default
	        .handler(new Handler()) // default
	        .build();
	}
	
	
	public boolean isAutoLogin(){
		autoLogin = sysSharedPreferences.getBoolean("pref_key_auto_login", false);
		return autoLogin;
	}
	
	public void setAutoLogin(boolean autoLogin){
		this.autoLogin = autoLogin;
		sysSharedPreferences.edit()
			.putBoolean("pref_key_auto_login", autoLogin)
			.commit(); //同步提交
	}
	
	public boolean isShowImg(){
		showImg = sysSharedPreferences.getBoolean("pref_key_download_img", true);
		return showImg;
	}
	
	public void setShowImg(boolean showImg){
		this.showImg = showImg;
		
		sysSharedPreferences.edit()
			.putBoolean("pref_key_download_img", showImg)
			.commit(); 
	}
	
	public String getUsername() {
		username = sysSharedPreferences.getString("username", null);
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		
		sysSharedPreferences.edit()
			.putString("username", username)
			.commit(); 
	}

	public String getPassword() {
		password = sysSharedPreferences.getString("password", null);
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		sysSharedPreferences.edit()
			.putString("password", password)
			.commit(); 
	}

	/**初始化系统设置*/
	private void initSystemSetting(){
		sysSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		autoLogin = isAutoLogin();
		setAutoLogin(autoLogin);
		
		showImg = isShowImg();
		setShowImg(showImg);
	}
	
	/**
	 * 创建缓存目录
	 */
	private void createCacheDir() {
		File f = new File(CACHE_DIR);
		if (f.exists()) {

			LogHelper.d("AppContext", "SD卡缓存目录:已存在!");

		} else {

			if (f.mkdirs()) {

				LogHelper.d("AppContext", "SD卡缓存目录:" + f.getAbsolutePath() + "已创建!");

			} else {

				LogHelper.d("AppContext", "SD卡缓存目录:创建失败!");

			}
		}

		File ff = new File(CACHE_DIR_FILE);
		if (ff.exists()) {

			LogHelper.d(TAG, "SD卡文件缓存目录：已存在!");

		} else {

			if (ff.mkdirs()) {

				LogHelper.d(TAG, "SD卡文件卡缓存目录:" + ff.getAbsolutePath() + "已创建!");

			} else {

				LogHelper.d(TAG, "SD卡文件缓存目录:创建失败!");

			}
		}
		
		File fff = new File(CACHE_DIR_IMAGE);
		if (fff.exists()) {
			LogHelper.d(TAG, "SD卡图片缓存目录：已存在!");
		} else {
			if (fff.mkdirs()) {
				LogHelper
						.d(TAG, "SD卡图片卡缓存目录:" + fff.getAbsolutePath() + "已创建!");
			} else {
				LogHelper.d(TAG, "SD卡图片缓存目录:创建失败!");
			}
		}
	}
}
