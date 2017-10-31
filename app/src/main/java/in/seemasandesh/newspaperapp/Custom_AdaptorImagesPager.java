package in.seemasandesh.newspaperapp;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.polites.android.GestureImageView;

public class Custom_AdaptorImagesPager extends PagerAdapter {

	ArrayList<GestureImageView> arrayImages;
	Activity context;
	ArrayList<String> arrayURLS;
	public  Custom_AdaptorImagesPager(Activity context , ArrayList<GestureImageView> arrayImages,ArrayList<String> arrayURLS) {
		this.arrayImages = arrayImages;
		this.context = context;
		this.arrayURLS = arrayURLS;
	}
	
	
	public void setImagesArray(ArrayList<GestureImageView> arrayImages){
		this.arrayImages = null;
		this.arrayImages = arrayImages;
	}
	@Override
	public int getCount() {
		return arrayURLS.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
	    
		Log.i("DARSH", "instantiateItem at " + position);
		if(arrayURLS.size() > position){
			/*
			//GestureImageView gImageView = arrayImages.get(position);
			
			ImageView gImageView = new ImageView(context);
			
			Point size= Globals.getScreenSize(context);
			int imgWidth=size.x;
			int imgHeight=size.y ;
		
			RelativeLayout.LayoutParams lpImg =  new RelativeLayout.LayoutParams(imgWidth, imgHeight);//(LinearLayout.LayoutParams) iv.getLayoutParams();//new LinearLayout.LayoutParams(imgWidth,imgHeight);
			gImageView.setLayoutParams(lpImg);		
			gImageView.setMinimumHeight(200);
			//gImageView.setMaxScale(10);
			//gImageView.setMinScale((float) 0.1);
			//gImageView.setStrict(true);
			*/
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			
			View view = null;//inflater.inflate(R.layout.vp_image, container, false);

			//ImageView mImageView = (ImageView) view.findViewById(R.id.image_display);
			
			/*LinearLayout layout = new LinearLayout(activity);
		    layout.setOrientation(LinearLayout.VERTICAL);
		    LayoutParams layoutParams = new LayoutParams(
		    		LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		    layout.setLayoutParams(layoutParams);
		    */
			GestureImageView gImageView = null;//(GestureImageView) view.findViewById(R.id.image_display);//new GestureImageView(activity);
			
		
			//RelativeLayout.LayoutParams lpImg =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);//(LinearLayout.LayoutParams) iv.getLayoutParams();//new LinearLayout.LayoutParams(imgWidth,imgHeight);
			//gImageView.setLayoutParams(lpImg);		
			//gImageView.setMinimumHeight(200);
			gImageView.setMaxScale(10);
			gImageView.setMinScale((float) 0.1);
			gImageView.setStrict(true);
			
			
			Globals.loadImageIntoImageView(gImageView, arrayURLS.get(position), context,R.drawable.loading_image_large, R.drawable.no_image_large);
			
			container.addView(view);
			return view;
		   
		}
		
		 return null;
	}
	
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}

	@Override
	public int getItemPosition(Object object){
	     return POSITION_NONE;
	}
	
	

}
