package in.seemasandesh.newspaperapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Custom_AdapterNewsList extends  ArrayAdapter<Interface_ListItem> {

	LayoutInflater myInflater;
	Activity context;
	ArrayList<Interface_ListItem> listData;
	
	public Custom_AdapterNewsList(Activity context, int resource,
			ArrayList<Interface_ListItem> listData) {
		super(context, resource, listData);
		
		this.context = context;
		myInflater = LayoutInflater.from(this.context);
		this.listData = listData;
	}


	@Override
	public View getView(final int position, View convertView,
			ViewGroup parentViewGroup) {
		
		if(listData != null && listData.size() > position){
			Interface_ListItem interNews = listData.get(position);

			if(interNews.getClass() == Object_ListItem_NewsCategory.class ){
				
				Object_ListItem_NewsCategory objCat = (Object_ListItem_NewsCategory)interNews;
				convertView =  createNewHeading(convertView,objCat);
				
			}else if(interNews.getClass() == Object_ListItem_MainNews.class){
				
				Object_ListItem_MainNews objNews = (Object_ListItem_MainNews)interNews;
				
				switch (objNews.getNewsType()) {
				case Object_ListItem_MainNews.NEWS_TYPE_NORMAL:
					convertView = createNewsRow(convertView , objNews);
					break;
				case Object_ListItem_MainNews.NEWS_TYPE_CAT_TOP:
					convertView =  createTopNewsRow(convertView,objNews);
					break;
				case Object_ListItem_MainNews.NEWS_TYPE_BREAKING:
					convertView =  createBreakingNewsRow(convertView,objNews);
					break;

				default:
					convertView = createNewsRow(convertView , objNews);
					break;
				}
				
			}
		}
		return convertView;
	}

	private View createNewHeading(View convertView,Object_ListItem_NewsCategory objCat){
    	//if (convertView == null)
			convertView = myInflater.inflate(R.layout.row_item_news_category, null);
    	
    	if(objCat == null)
			return convertView;
    	
    	TextView tv = (TextView) convertView.findViewById(R.id.txtCatHeading);
    	tv.setText(objCat.getCatName());
    	tv.setTextSize(Globals.getAppFontSize_Large(context));
    	
    	Object_AppConfig objConfig = new Object_AppConfig(context);
    	
    	int currentTheme = objConfig.getTheme();
    	if(currentTheme == Custom_ThemeUtil.THEME_GREEN_VALUE || currentTheme == Custom_ThemeUtil.THEME_YELLOW_VALUE)
    		tv.setTextColor(context.getResources().getColor(R.color.app_black));
    	else
    		tv.setTextColor(context.getResources().getColor(R.color.app_white));
    	
    	tv.setBackgroundResource(Custom_ThemeUtil.getThemeColorId(context));
    	
    	return convertView;
    }
	
	private View createNewsRow( View convertView,Object_ListItem_MainNews objNews){
		//if (convertView == null)
			convertView = myInflater.inflate(R.layout.row_item_news_list, null);
		
		
		if(objNews == null)
			return convertView;
		
		//Set News Heading
		TextView tv = (TextView) convertView.findViewById(R.id.txt_news_item_heading);
		
		tv.setText(objNews.getHeadingSpan());
		tv.setTextSize(Globals.getAppFontSize_Normal(context));
		
		Point point = Globals.getScreenSize(context);
		//int screenWidth = point.x;
		int screenHeight = point.y;
		//Set News Image
		int imgMargin = 6;
		int imgWidthHeight = (int)screenHeight/6;
		ImageView iv = (ImageView) convertView.findViewById(R.id.img_news_item);
		//Object_AppConfig objAppConfig = new Object_AppConfig(context);
		
		String imgURL =  objNews.getImagePath();//objAppConfig.getNewsImagesFullPath() +Globals.PREFIX_HOME_IMAGES+
		Log.i("HARSH", "imgURL :"+imgURL);
		
		LinearLayout.LayoutParams lpImg = (LayoutParams) iv.getLayoutParams();
				//new LinearLayout.LayoutParams(imgWidthHeight,imgWidthHeight);
		lpImg.setMargins(imgMargin, 0, 0, 0);
		
		lpImg.height = imgWidthHeight;
		lpImg.width = imgWidthHeight;
		
		iv.setLayoutParams(lpImg);
		
		
		
		if(!imgURL.isEmpty())
			Globals.loadImageIntoImageView(iv, imgURL, context, R.drawable.loading_image_small, R.drawable.no_image_small);
		else
			iv.setImageResource(R.drawable.no_image_small);
		
		TextView txtDate = (TextView) convertView
				.findViewById(R.id.txt_news_item_date);
		txtDate.setText(getFormatedDateTime(objNews.getDate()));
		
		return convertView;
	}
    
	private View createTopNewsRow( View convertView,Object_ListItem_MainNews objNews){
		//if (convertView == null)
			convertView = myInflater.inflate(R.layout.row_item_news_cat_top, null);
		
		if(objNews == null)
			return convertView;
		
		//Set News Heading
		TextView tv = (TextView) convertView.findViewById(R.id.txt_news_item_heading);
		
		tv.setText(objNews.getHeadingSpan());
		tv.setTextSize(Globals.getAppFontSize_Normal(context));
		
		Point point = Globals.getScreenSize(context);
		int imgHeight = (int)point.y/3;
		
		final View Container = convertView.findViewById(R.id.rlytContainerNewsItem);
		LinearLayout.LayoutParams lp = (LayoutParams) Container.getLayoutParams();
		lp.height = imgHeight;
		Container.setLayoutParams(lp);
		
		//Object_AppConfig objAppConfig = new Object_AppConfig(context);
		//objAppConfig.getNewsImagesFullPath() +
		String imgURL =  objNews.getImagePath();
		
		
		ImageView iv = (ImageView) convertView.findViewById(R.id.img_news_item);
		if(!imgURL.isEmpty())
			Globals.loadImageIntoImageView(iv, imgURL, context, R.drawable.loading_image_large, R.drawable.no_image_large);
		else
			iv.setImageResource(R.drawable.no_image_large);
		
		
		return convertView;
	}
	private View createBreakingNewsRow( View convertView,Object_ListItem_MainNews objNews){
		//if (convertView == null)
			convertView = myInflater.inflate(R.layout.row_item_news_cat_top, null);
		
		if(objNews == null)
			return convertView;
		
		//Set News Heading
		TextView tv = (TextView) convertView.findViewById(R.id.txt_news_item_heading);
		
		tv.setText(objNews.getHeadingSpan());
		tv.setTextSize(Globals.getAppFontSize_Normal(context));
		
		Point point = Globals.getScreenSize(context);
		int imgHeight = (int)point.y/3;
		
		final View Container = convertView.findViewById(R.id.rlytContainerNewsItem);
		LinearLayout.LayoutParams lp = (LayoutParams) Container.getLayoutParams();
		lp.height = imgHeight;
		Container.setLayoutParams(lp);
		
		//Object_AppConfig objAppConfig = new Object_AppConfig(context);
		//objAppConfig.getNewsImagesFullPath() +
		String imgURL =  objNews.getImagePath();
		
		
		ImageView iv = (ImageView) convertView.findViewById(R.id.img_news_item);
		if(!imgURL.isEmpty())
			Globals.loadImageIntoImageView(iv, imgURL, context, R.drawable.loading_image_large, R.drawable.no_image_large);
		else
			iv.setImageResource(R.drawable.no_image_large);
		
		
		return convertView;
	}
	
    private String getFormatedDateTime(String dateString){
    	
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy",Locale.ENGLISH);
        
        SimpleDateFormat currentdateFormat = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy",Locale.ENGLISH);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        //Assuming server time is in IST
        Date formattedDate = null;
        
        try {
        	formattedDate = dateFormat.parse(dateString);
        } catch (Exception e) {
        	Log.i("HARSH", "Exception in date formatting");
        }
        
       
        if(formattedDate != null){
        	Log.i("HARSH", " dateString "+dateString);
        	 Log.i("HARSH", "formattedDate  "+formattedDate);
        	 
        	 TimeZone tz = TimeZone.getTimeZone(Globals.SERVER_TIME_ZONE);
        	 Calendar c = Calendar.getInstance(tz);
        	// Calendar cal = Calendar.getInstance();
        	 
        	 String time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
        	            String.format("%02d" , c.get(Calendar.MINUTE))+":"+
        	            String.format("%02d" , c.get(Calendar.SECOND)) +" "+ 
        	            Globals.getTwoDigitNo(c.get(Calendar.DAY_OF_MONTH))+"-"+
        	            Globals.getTwoDigitNo(c.get(Calendar.MONTH)+1)+"-"+
        	            Globals.getTwoDigitNo(c.get(Calendar.YEAR));
        	 			
        	 
        	 //+":"+String.format("%03d" , c.get(Calendar.MILLISECOND))
        	 
        	 Log.i("HARSH", "new time  "+time);
        	 
        	 Date currentDate = new Date();
			try {
				currentDate = currentdateFormat.parse(time);
			} catch (ParseException e) {
			}//new Date();
        	 
        	 Log.i("HARSH", "currentGMT "+currentDate);
        	long diffInMilliSec = currentDate.getTime() - formattedDate.getTime();
        	
        	 Log.i("HARSH", "diffInMilliSec "+diffInMilliSec);
        	
        	long hour = diffInMilliSec / (60*60*1000);
        	
        	if( hour> 0 && hour<= 12){
        		
        		if(hour == 1)
        			return hour+" hour ago";
        		
        		return hour+" hours ago";
        	}else{
        		long min = diffInMilliSec / (60*1000);
        		if( min> 0 && min< 60){
        			if(min == 1)
            			return min+" min ago";
            		
            		return min+" mins ago";
        		}
        		else{
        			long sec = diffInMilliSec / (1000);
        			if( sec> 0 && sec< 60){
            			if(sec == 1)
                			return sec+" second ago";
                		
                		return sec+" seconds ago";
            		}
        		}
        	}
        	
        	SimpleDateFormat sdfDDMMYYYY = new SimpleDateFormat("d MMM , yyyy",Locale.ENGLISH);
        	//sdfDDMMYYYY.setTimeZone(TimeZone.getTimeZone("IST"));
        	return sdfDDMMYYYY.format(formattedDate);
        }
        
        	return dateString;
    }
    
}