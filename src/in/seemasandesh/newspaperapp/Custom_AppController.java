package in.seemasandesh.newspaperapp;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


public class Custom_AppController extends Application {

	 private RequestQueue requestQueue;
	 public static final boolean DEVELOPER_MODE = false;
	 private static Custom_AppController appControllerContext;
	 
	 @Override
	 public void onCreate() 
	 { 
	  super.onCreate();
	  System.out.println("AppController onCreate Called!");
	  appControllerContext = this;
	  
	  if (DEVELOPER_MODE
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}

		initImageLoader(getApplicationContext());
	 }
	 
	 public static synchronized Custom_AppController getInstance()
	 {
		 return appControllerContext;
	 }
	 
	 public RequestQueue getRequestQueue()
	 {
	  if(requestQueue == null)
	  {
	   requestQueue = Volley.newRequestQueue(appControllerContext); 
	  }
	  return requestQueue;
	 }
	 
	 public <T> void addToRequestQueue(Request<T> req)
	 {
		 req.setRetryPolicy(new DefaultRetryPolicy(
				 	Globals.VOLLEY_TIMEOUT_MILLISECS, 
	                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
	                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		 getRequestQueue().add(req);
	 }
	 public void cancelPendingRequest(Object tag)
	 {
	  if(requestQueue != null)
	  {
	   requestQueue.cancelAll(tag);
	  }
	 }
	 
	 public static void initImageLoader(Context context) {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.no_image_large)
					.showImageOnFail(R.drawable.no_image_large).resetViewBeforeLoading()
					.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new FadeInBitmapDisplayer(300)).build();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context).threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator())
					.defaultDisplayImageOptions(options)
					.tasksProcessingOrder(QueueProcessingType.LIFO).build();

			ImageLoader.getInstance().init(config);
		}
}
