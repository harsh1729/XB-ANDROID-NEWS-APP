package com.xercesblue.newspaperapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Globals {

	public static final int APP_TRUE = 1;
	public static final int APP_FALSE = 0;
	
	/* To be changed with each client */
	public static final int APP_ID= 1;
	public static final int CLIENT_ID= 1;
	public static final String APP_NAME = "XB NEWS";
	public static final String AD_INMOBI_PROPERTY_ID = "";
	public static  String GCM_REG_ID = "";
	public static final String VSERV_BILLBOARD_ZONE_ID = "";
	public static final String VSERV_BANNER_ZONE_ID = "";
	public static final int FINAL_NEWS_LIMIT_FIRST_CALL = 10;
	public static final int FINAL_NEWS_LIMIT_REFRESH = 5;
	/* To be changed with each client   */
	
	public static final String DEFAULT_APP_SERVER_PATH= "http://www.newstest2.tk/newsci/client_requests/";//"http://xercesblue.in/newsentry/";
	//public static final String DEFAULT_APP_NEWS_IMAGES_PATH= "uploads/";
	//public static final String DEFAULT_APP_CAT_IMAGES_PATH= "CategoryUploads/";
	public static final String PREFIX_HOME_IMAGES="small_";
	public static final String DEFAULT_NEWS_HEADING = "#9#";
	public static final String DEFAULT_APP_WEBSITE = "www.xercesblue.in";
	
	public static final int ADD_TYPE_INMOBI = 1;
	public static final int ADD_TYPE_VSERV= 2;
	public static final int VOLLEY_TIMEOUT_MILLISECS = 10000;
	public static final String CALLTYPE_NEW = "new";
	public static final String CALLTYPE_OLD = "old";
	public static final String CALLTYPE_FRESH = "fresh";

	
	public static final String TEXT_CONNECTION_ERROR_HEADING = "Error in Connection";
	public static final String TEXT_CONNECTION_ERROR_DETAIL_TOAST ="Please check your internet connection and try again.";
	public static final String TEXT_CONNECTION_ERROR_DETAIL_DIALOG_MAIN_SCREEN ="Please check your internet connection and try again.\n\nLoading news from your previous session.";
	public static final String TEXT_NO_INTERNET_HEADING = "No Internet Connectivity";
	public static final String TEXT_NO_INTERNET_DETAIL_TOAST= "No Internet Connectivity, please connect to a network.";
	public static final String TEXT_NO_INTERNET_DETAIL_DIALOG_MAIN_SCREEN= "Please connect to a network, and click on 'Refresh' button from top options menu to load current news.  \n\nLoading news from your previous session.";
	
	//public static final String NEWS_ITEM = "item";
	//public static final String NEWS_ITEM_NEXT = "itemNext";
	//public static final String NEWS_ITEM_PREV = "itemPrev";
	
	public static final String OPTION_SAVED_NEWS = "Saved News";
	public static final String OPTION_SAVE = "Save News";
	public static final String OPTION_CALENDER = "Date Wise";
	public static final String OPTION_SETTINGS = "Settings";
	public static final String OPTION_REFRESH = "Refresh All";
	public static final String OPTION_SHARE = "Share";
	
	static String getURL_CatNewsFirstCall(){
		//http://www.newstest2.tk/newsci/client_requests/news/mob_get_cat_news
		//return DEFAULT_APP_SERVER_PATH+"getAppConfig.php?CatVersion="+catVersionId+"&AppConfigVersion=0"+appVersionId;
		
		Log.i("HARSH", DEFAULT_APP_SERVER_PATH+"news/mob_get_cat_news");
		return DEFAULT_APP_SERVER_PATH+"news/mob_get_cat_news";//?CatVersion="+catVersionId+"&AppConfigVersion=0"+appVersionId;
	}
	
	static Map<String, String> getParams_CatNewsFirstCall(int catVersionId,int appVersionId){
		HashMap<String, String> mParams = new HashMap<String, String>();
			        mParams.put("clientid", CLIENT_ID+"");
			        mParams.put("catversion", catVersionId+"");
			        mParams.put("appconfigversion", appVersionId+"");
			        
			        
			        return mParams;
	}
	static String getURL_NewsByCategory(){ //int catId , int lastNewsId, String callType
		//http://www.newstest2.tk/newsci/client_requests/news/mob_get_news_by_category
		//return  DEFAULT_APP_SERVER_PATH+"getnewsbycategory.php?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		Log.i("HARSH", DEFAULT_APP_SERVER_PATH+"news/mob_get_news_by_category");//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType);
		return  DEFAULT_APP_SERVER_PATH+"news/mob_get_news_by_category";//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		//Calltype : fresh,new,old
	}

	static Map<String, String> getParams_NewsByCategory(int catId,String callType,int lastNewsId,int limit){
		
		
		HashMap<String, String> mParams = new HashMap<String, String>();
			        mParams.put("catid", catId+"");
			        mParams.put("calltype", callType);
			        mParams.put("lastnewsid", lastNewsId+"");
			        mParams.put("limit", limit+"");
			        Log.i("DARSH", "getParams_NewsByCategory --->" + mParams);
			        
			        return mParams;
	}
static Map<String, String> getParams_NewsByCategoryDateWise(int catId,String callType,int lastNewsId,int limit,String startDate,String endDate ){
		
		
		HashMap<String, String> mParams = new HashMap<String, String>();
			        mParams.put("catid", catId+"");
			        mParams.put("calltype", callType);
			        mParams.put("lastnewsid", lastNewsId+"");
			        mParams.put("limit", limit+"");
			        mParams.put("startdate", startDate+"");
			        mParams.put("enddate", endDate+"");
			        Log.i("DARSH", "getParams_NewsByCategory --->" + mParams);
			        
			        return mParams;
	}
	
	static String getURL_NewsDetail(){ //int catId , int lastNewsId, String callType
		//http://www.newstest2.tk/newsci/client_requests/news/mob_get_news_by_category
		//return  DEFAULT_APP_SERVER_PATH+"getnewsbycategory.php?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		Log.i("HARSH", DEFAULT_APP_SERVER_PATH+"news/mob_get_news_detail");//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType);
		return  DEFAULT_APP_SERVER_PATH+"news/mob_get_news_detail";//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		//Calltype : fresh,new,old
	}

	static Map<String, String> getParams_NewsDetail(int newsId){	
		HashMap<String, String> mParams = new HashMap<String, String>();
			        mParams.put("newsid", newsId+"");			        
			        Log.i("DARSH", "getParams_NewsDetail --->" + mParams);
			        
			        return mParams;
	}
	static String getURLPushNotification(String gcmId,int appId){
		//DEFAULT_APP_SERVER_PATH
		return  "http://xercesblue.in/newsentry/pushNotification/registerPushNotificationUser.php?gcmId="+gcmId+"&AppId="+appId;
	}
	
	@SuppressLint("NewApi")
	static public Point getScreenSize(Activity currentActivity){
		Display display = currentActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();


		if (android.os.Build.VERSION.SDK_INT >= 13) 
		{
			display.getSize(size);
		} 
		else 
		{
			 size.x = display.getWidth();
			 size.y = display.getHeight();
		}
		
		return size;
	}
	
	

	static public Point getAppButtonSize(Activity context ){
		
		int screenWidth = Globals.getScreenSize(context).x;
		
		Point size = new Point();
		
		size.x = 4*screenWidth/10;
		size.y = size.x/3;
		
		return size;
	}
	static public Boolean getBoolFromInt(int val){
		if(val > 0)
			return true;
		return false;
	}
	static public int getIntFromBool(Boolean val){
		if(val)
			return 1;
		
		return 0;
	}
	
	static public Bitmap scaleToWidth(Bitmap bitmap,int scaledWidth) {
		if (bitmap != null) {

			int bitmapHeight = bitmap.getHeight(); 
			int bitmapWidth = bitmap.getWidth(); 

			// scale According to WIDTH
			int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;

			try {

				bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	
	static public int getButtonsPadding(int btnSize){
		
		return btnSize/10;
	}
	
	static public int getAppFontSize_Normal(Activity context){
		
		Object_AppConfig obj = new Object_AppConfig(context);
		return (int) (context.getResources().getDimension(R.dimen.font_lbl_normal)/ context.getResources().getDisplayMetrics().density * obj.getFontFactor());
	}
	static public int getAppFontSize_Large(Activity context){
		
		Object_AppConfig obj = new Object_AppConfig(context);
		return (int) (context.getResources().getDimension(R.dimen.font_lbl_large)/ context.getResources().getDisplayMetrics().density * obj.getFontFactor());
	}
	
	/*
	static public int getAppFontSize_Small(Activity context){
		
		return (getScreenSize(context).x/120 + 10);
	}
	
	*/

	static public void showAlertDialogError(final Activity activity){
		
		showAlertDialogError(activity,"Please try again.");
		
	}
	
	static public void showAlertDialogError(final Activity activity , String msg){
	
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				dialog.cancel();
				activity.finish();
			}
		};
		
		Globals.showAlertDialog("Error",msg ,activity,"OK", listener,null,null,false);
	}
	
static public void showAlertDialogOneButton(String title,String msg,Context context,String positiveButtonText,DialogInterface.OnClickListener listnerPositive,Boolean isCancelable){
		
		Globals.showAlertDialog(title,msg ,context,positiveButtonText, listnerPositive,null,null,isCancelable);
	}

	static public void showAlertDialog(String title,String msg,Context context,String positiveButtonText,DialogInterface.OnClickListener listnerPositive,String negativeButtonText ,DialogInterface.OnClickListener listnerNegative,Boolean isCancelable){
		
		AlertDialog alertDialog = new AlertDialog.Builder(
				context ,AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setCancelable(isCancelable);
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,positiveButtonText,listnerPositive);
		
		if(negativeButtonText!= null && !negativeButtonText.equals("")){
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,negativeButtonText,listnerNegative);
		}
		alertDialog.show();
		
	}

	
	
	
	public static int getHeightofListView(ListView listView) {

	    ListAdapter mAdapter = listView.getAdapter();

	    int listviewElementsheight = 0;
	    int items = mAdapter.getCount(); 


	    for (int i = 0; i < items; i++) {

	        View childView = mAdapter.getView(i, null, listView);
	        childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        listviewElementsheight+= childView.getMeasuredHeight();
	        }


	        return listviewElementsheight;

	    }
	
	public static String convertInputStreamToString(InputStream is)
	{
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try 
		{
			while((line = rd.readLine()) != null)
			{
				total.append(line);
			}
		} 
		catch (IOException e) 
		{
			//Toast.makeText(this,"Stream Exception", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return total.toString();
	}
	/*
	public Bitmap writeTextOnDrawable(Bitmap bm, String text , int color) {

	    Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);

	    Paint paint = new Paint();
	    paint.setStyle(Style.FILL);
	    paint.setColor(color);
	    paint.setTypeface(tf);
	    paint.setTextAlign(Align.CENTER);
	   // paint.setTextSize(convertToPixels(currentActivity, 11));
	    paint.setTextSize(currentActivity.getResources().getDimension(R.dimen.txt_app_buttons_container_fontsize_big));
	    Rect textRect = new Rect();
	    paint.getTextBounds(text, 0, text.length(), textRect);

	    Canvas canvas = new Canvas(bm);

	    //If the text is bigger than the canvas , reduce the font size
	    if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
	        paint.setTextSize(currentActivity.getResources().getDimension(R.dimen.txt_app_buttons_container_fontsize_small));        //Scaling needs to be used for different dpi's

	    //Calculate the positions
	    int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

	    //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
	    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;  

	    canvas.drawText(text, xPos, yPos, paint);

	    return new BitmapDrawable(currentActivity.getResources(), bm).getBitmap();
	}



	public  int convertToPixels(Context context, int nDP)
	{
	    final float conversionScale = context.getResources().getDisplayMetrics().density;

	    return (int) ((nDP * conversionScale) + 0.5f) ;

	}
	*/
	
	public static byte [] bitmapToByteArray(Bitmap bmp){
		if(bmp == null)
			return null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		
		return byteArray;
	}
	
	public static Bitmap  byteArrayToBitmap(byte [] bytes){
		if(bytes == null)
			return null;
		Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		
		return bmp;
	}
	public static RotateAnimation getRotationAnimation() {

		RotateAnimation rotation = new RotateAnimation(0f, 359f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotation.setDuration(500);
		rotation.setInterpolator(new LinearInterpolator());
		rotation.setRepeatMode(Animation.RESTART);
		rotation.setRepeatCount(Animation.INFINITE);

		return rotation;
	}
	
	

	public static ProgressDialog showLoadingDialog(ProgressDialog mDialog, Activity act , Boolean cancelable) {
		
		if(mDialog == null){
		  mDialog = new ProgressDialog(act,
		  ProgressDialog.THEME_HOLO_LIGHT);
		  mDialog.setTitle("Loading");
		  mDialog.setMessage("Please wait for a moment...");
		  mDialog.setCancelable(cancelable); 
		  mDialog.setProgressDrawable(null);
		  mDialog.show();
		}else if(!mDialog.isShowing()) {
			mDialog.show();
		}
		 
		return mDialog;
	}

	public static void hideLoadingDialog(ProgressDialog mDialog) {

		 if (mDialog != null) {
			 mDialog.dismiss(); 
		 }
		 

	}
}
