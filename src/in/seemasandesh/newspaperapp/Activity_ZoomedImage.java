package in.seemasandesh.newspaperapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.polites.android.GestureImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;


public class Activity_ZoomedImage extends Activity {

	private ArrayList<Object_SubNewsItem> listAllCurrentNewsItem;
	//private Custom_AdaptorImagesPager pagerAdp;
	//private ViewPager viewPager;
	private TextView txtHead;
	private TextView txtIndicator;
	private int currentSubItemNo;
	private int newsId;
	
	private int successCount = 0;
	//private final int MIN_DISTANCE = 150;
	
	GestureImageView currentImage;
	ArrayList<GestureImageView> arrayImages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zoomed_image);
		
		if (getIntent().hasExtra("newsId"))
			newsId = getIntent().getIntExtra("newsId", 0);
		else {
			Globals.showAlertDialogError(this,
					"Error Occured, Please try again");
		}
		
		initZoomImage();
	}
	
	//private String imageUrl;
	private void initZoomImage(){
		
		Point size= Globals.getScreenSize(this);
		int  btnCloseHeightWidth = size.x/8;
		
		ImageButton btnClose = (ImageButton)findViewById(R.id.imgBtnClose);
		RelativeLayout.LayoutParams lpbtn =(RelativeLayout.LayoutParams) btnClose.getLayoutParams();//new RelativeLayout.LayoutParams(btnCloseHeightWidth,btnCloseHeightWidth);
		lpbtn.height = btnCloseHeightWidth;
		lpbtn.width = btnCloseHeightWidth;
		btnClose.setLayoutParams(lpbtn);


		//viewPager = (ViewPager)findViewById(R.id.viewpager);
		txtHead = (TextView)findViewById(R.id.txtNewsHeadingZoom);
	    txtIndicator = (TextView)findViewById(R.id.txtNewsImageIndicator);
		
	    setListOfNewsItems();
  
	}

	
	private void noImagesFound(){
		Toast.makeText(this, "No images to show!", Toast.LENGTH_SHORT).show();
		this.finish();
	}
	
	public void onCickCancel(View v)
	{
		this.finish();
	}
	
	public void onCickShare(View v)
	{
		
		shareImageWhatsApp();
	}
	
	public void shareImageWhatsApp() {

		//ImageView iv = (ImageView)findViewById(R.id.image_zoom);
		
		if(currentImage == null){
			Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(currentImage.getDrawable() == null)
		{
			Toast.makeText(this, "Please wait, Image not loaded", Toast.LENGTH_SHORT).show();
			return;
		}
		if(((BitmapDrawable)currentImage.getDrawable()).getBitmap() == null)
		{
			Toast.makeText(this, "Please wait, Image not loaded", Toast.LENGTH_SHORT).show();
			return;
		}
	    Bitmap adv = ((BitmapDrawable)currentImage.getDrawable()).getBitmap();//BitmapFactory.decodeResource(getResources(), R.drawable.adv);
	    Intent share = new Intent(Intent.ACTION_SEND);
	    if(listAllCurrentNewsItem.size() > currentSubItemNo){
			Object_SubNewsItem item = listAllCurrentNewsItem.get(currentSubItemNo);
			 share.putExtra(Intent.EXTRA_TEXT, item.getNewsHeading());
			  share.setType("text/plain");
	    }
	   
		
	    share.setType("image/jpeg");
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    adv.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    File f = new File(Environment.getExternalStorageDirectory()
	            + File.separator + "temporary_file.jpg");
	    try {
	        f.createNewFile();
	        new FileOutputStream(f).write(bytes.toByteArray());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    share.putExtra(Intent.EXTRA_STREAM,
	            Uri.parse( Environment.getExternalStorageDirectory()+ File.separator+"temporary_file.jpg"));
	    if(isPackageInstalled("com.whatsapp",this)){
	          share.setPackage("com.whatsapp"); 
	          startActivity(Intent.createChooser(share, "Share Image"));

	    }else{

	        Toast.makeText(getApplicationContext(), "Please Install Whatsapp", Toast.LENGTH_LONG).show();
	    }

	}

	private boolean isPackageInstalled(String packagename, Context context) {
	    PackageManager pm = context.getPackageManager();
	    try {
	        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
	        return true;
	    } catch (NameNotFoundException e) {
	        return false;
	    }
	}

	public void onClickPrev(View v){
		if(currentSubItemNo > 0)
		{
			setImage(currentSubItemNo - 1);
			
		}
	}
	
	public void onClickNext(View v){
		
		if(currentSubItemNo < listAllCurrentNewsItem.size()-1)
		{
			setImage(currentSubItemNo + 1);
			//if(viewPager != null){
				//viewPager.setCurrentItem(currentSubItemNo + 1, true);
			//}
			
		}
	}
	
	
	
	
	private void setImage(int itemNo){
		Object_SubNewsItem item = null;
		
		if(listAllCurrentNewsItem.size() > itemNo)
			item = listAllCurrentNewsItem.get(itemNo);
		else{
			noImagesFound();
			return ;
		}
		
		if(item == null){
			noImagesFound();
			return ;
		}
		
		currentSubItemNo = itemNo;
		//imageUrl = getIntent().getStringExtra("Url");
		String imageUrl = item.getNewsImagePath();
		Point size= Globals.getScreenSize(this);
		int imgWidth=size.x;
		int imgHeight=size.y ;
		
		 
		//ImageView iv = (ImageView)findViewById(R.id.image_zoom);
		RelativeLayout container = (RelativeLayout)findViewById(R.id.rlytImageContainer);
		//container.removeAllViews();
		if(currentImage != null)
			container.removeView(currentImage);
		
		currentImage = new GestureImageView(this);
		
        //iv.setFocusable(true);
		RelativeLayout.LayoutParams lpImg =  new RelativeLayout.LayoutParams(imgWidth, imgHeight);//(LinearLayout.LayoutParams) iv.getLayoutParams();//new LinearLayout.LayoutParams(imgWidth,imgHeight);
		//lpImg.height = imgHeight;
		//lpImg.weight = imgWidth;
		//lpImg.
		currentImage.setLayoutParams(lpImg);
		
		currentImage.setMinimumHeight(200);
		currentImage.setMaxScale(10);
		currentImage.setMinScale((float) 0.1);
		currentImage.setStrict(true);
		
		/*
		currentImage.setOnSwipeRightDetectedListener(new OnSwipeRightDetectedListener() {
			
			@Override
			public void onSwipe() {
				Log.i("DARSH", "Swipe Right");
				onClickNext(null);
				
			}
		});
		
		currentImage.setOnSwipeLeftDetectedListener(new OnSwipeLeftDetectedListener() {
			
			@Override
			public void onSwipe() {
				Log.i("DARSH", "Swipe Left");
				onClickPrev(null);
				
			}
		});
		*/
		//final ImageView iv = (ImageView)findViewById(R.id.image_zoom);
		container.addView(currentImage);
		
	     txtHead.setText(item.getNewsHeading());
	     
	     txtIndicator.setText(currentSubItemNo+1 +" / "+ listAllCurrentNewsItem.size());
	     
		ProgressBar pBar = (ProgressBar)findViewById(R.id.loading_bar);
		pBar.setVisibility(View.VISIBLE);
		
		 Picasso p = Picasso.with(this);
		    RequestCreator rq = null;
		 if(imageUrl!=null && !imageUrl.trim().isEmpty())
		 {
			 rq = p.load(imageUrl);
			 rq.resize(imgWidth, imgHeight);
			 rq.centerInside();
			 rq.noFade();
		 }else
				rq = p.load(R.drawable.no_image_large);
		 
	     rq.into(currentImage, new Callback() {
			
			
			@Override
			public void onSuccess() {
				
				ProgressBar p = (ProgressBar)findViewById(R.id.loading_bar);
				 if(p != null){
	                    
					 p.setVisibility(View.GONE);  
	                    
	                }	
			}
			
			@Override
			public void onError() {
				ProgressBar p = (ProgressBar)findViewById(R.id.loading_bar);
				 if(p != null){
					 
	                    p.setVisibility(View.GONE);
	                }
			}

			
		});

	     setNextPrevButtonsState();
	    
	}
	private void setListOfNewsItems(){
		
		if(listAllCurrentNewsItem != null){
			listAllCurrentNewsItem.clear();
		}

		DBHandler_MainNews dbHMain = new DBHandler_MainNews(this);
		Object_ListItem_MainNews currentNewsItem = dbHMain.getNewsItemWithId(newsId);

		String parentHeading = "";
		if (currentNewsItem != null) {
			parentHeading = currentNewsItem.getHeading();
		}
		DBHandler_SubNews dbHSub = new DBHandler_SubNews(
				this);
		listAllCurrentNewsItem = dbHSub.getAllSubNewsItem(newsId,
				parentHeading);

		if (currentNewsItem != null) {
			Object_SubNewsItem temp = new Object_SubNewsItem();
			temp.setNewsContent(currentNewsItem.getContent());
			temp.setNewsHeading(currentNewsItem.getHeading());
			temp.setNewsImagePath(currentNewsItem.getImagePath());
			temp.setNewsImageTagline(currentNewsItem.getImageTagline());
			temp.setNewsVideo(currentNewsItem.getVideo());

			listAllCurrentNewsItem.add(0, temp);
		}
		
		if(listAllCurrentNewsItem.size() > 0){
			
			if (getIntent().hasExtra("itemNo"))
				setImage(getIntent().getIntExtra("itemNo", 0));
			else
				setImage(0);
			
			/*
			if(listAllCurrentNewsItem.size() == 1){
				ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
				ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
				
				imgBtnNext.setVisibility(View.GONE);
				imgBtnPrev.setVisibility(View.GONE);
			}
			*/
		}else{
			
			noImagesFound();
		}
	}
	



	
	private void setNextPrevButtonsState(){
		ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
		ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
		
		if(currentSubItemNo == 0){
			//imgBtnPrev.setEnabled(false);
			imgBtnPrev.setVisibility(View.GONE);
		}else{
			//imgBtnPrev.setEnabled(true);
			imgBtnPrev.setVisibility(View.VISIBLE);
		}
		
		if(currentSubItemNo == listAllCurrentNewsItem.size()-1){
			//imgBtnNext.setEnabled(false);
			imgBtnNext.setVisibility(View.GONE);
		}else{
			//imgBtnNext.setEnabled(true);
			imgBtnNext.setVisibility(View.VISIBLE);
		}
		if(currentSubItemNo < listAllCurrentNewsItem.size()){
			txtHead.setText(listAllCurrentNewsItem.get(currentSubItemNo).getNewsHeading());
			txtIndicator.setText(currentSubItemNo+1 +" / "+ listAllCurrentNewsItem.size());
		}
	}
	
	

}

/*
 
 	
	
	private void setListOfNewsItems(){
		
		if(listAllCurrentNewsItem != null){
			listAllCurrentNewsItem.clear();
		}

		DBHandler_MainNews dbHMain = new DBHandler_MainNews(this);
		Object_ListItem_MainNews currentNewsItem = dbHMain.getNewsItemWithId(newsId);

		String parentHeading = "";
		if (currentNewsItem != null) {
			parentHeading = currentNewsItem.getHeading();
		}
		DBHandler_SubNews dbHSub = new DBHandler_SubNews(
				this);
		listAllCurrentNewsItem = dbHSub.getAllSubNewsItem(newsId,
				parentHeading);

		if (currentNewsItem != null) {
			Object_SubNewsItem temp = new Object_SubNewsItem();
			temp.setNewsContent(currentNewsItem.getContent());
			temp.setNewsHeading(currentNewsItem.getHeading());
			temp.setNewsImagePath(currentNewsItem.getImagePath());
			temp.setNewsImageTagline(currentNewsItem.getImageTagline());
			temp.setNewsVideo(currentNewsItem.getVideo());

			listAllCurrentNewsItem.add(0, temp);
		}
		
		if(listAllCurrentNewsItem.size() > 0){
			
			setViewPager();
			
			//setArrayImages();
			/*
			if (getIntent().hasExtra("itemNo"))
				setImage(getIntent().getIntExtra("itemNo", 0));
			else
				setImage(0);
			
			
			if(listAllCurrentNewsItem.size() == 1){
				ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
				ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
				
				imgBtnNext.setVisibility(View.GONE);
				imgBtnPrev.setVisibility(View.GONE);
			}
			///
		}else{
			
			noImagesFound();
		}
	}
 
 private void setImage(int itemNo){
		Object_SubNewsItem item = null;
		
		if(listAllCurrentNewsItem.size() > itemNo)
			item = listAllCurrentNewsItem.get(itemNo);
		else{
			noImagesFound();
			return ;
		}
		
		if(item == null){
			noImagesFound();
			return ;
		}
		
		currentSubItemNo = itemNo;
		//imageUrl = getIntent().getStringExtra("Url");
		String imageUrl = item.getNewsImagePath();
		Point size= Globals.getScreenSize(this);
		int imgWidth=size.x;
		int imgHeight=size.y ;
		
		 
		//ImageView iv = (ImageView)findViewById(R.id.image_zoom);
		RelativeLayout container = (RelativeLayout)findViewById(R.id.rlytImageContainer);
		//container.removeAllViews();
		if(currentImage != null)
			container.removeView(currentImage);
		
		currentImage = new GestureImageView(this);
		
        //iv.setFocusable(true);
		RelativeLayout.LayoutParams lpImg =  new RelativeLayout.LayoutParams(imgWidth, imgHeight);//(LinearLayout.LayoutParams) iv.getLayoutParams();//new LinearLayout.LayoutParams(imgWidth,imgHeight);
		//lpImg.height = imgHeight;
		//lpImg.weight = imgWidth;
		//lpImg.
		currentImage.setLayoutParams(lpImg);
		
		currentImage.setMinimumHeight(200);
		currentImage.setMaxScale(10);
		currentImage.setMinScale((float) 0.1);
		currentImage.setStrict(true);
		
		/*
		currentImage.setOnSwipeRightDetectedListener(new OnSwipeRightDetectedListener() {
			
			@Override
			public void onSwipe() {
				Log.i("DARSH", "Swipe Right");
				onClickNext(null);
				
			}
		});
		
		currentImage.setOnSwipeLeftDetectedListener(new OnSwipeLeftDetectedListener() {
			
			@Override
			public void onSwipe() {
				Log.i("DARSH", "Swipe Left");
				onClickPrev(null);
				
			}
		});
		////
		//final ImageView iv = (ImageView)findViewById(R.id.image_zoom);
		container.addView(currentImage);
		
		 TextView txtHead = (TextView)findViewById(R.id.txtNewsHeadingZoom);
		 Log.i("HARSH1", "txtHead is" + findViewById(R.id.txtNewsHeadingZoom));
	     txtHead.setText(item.getNewsHeading());
	     
	     TextView txtIndicator = (TextView)findViewById(R.id.txtNewsImageIndicator);
	     txtIndicator.setText(currentSubItemNo+1 +" / "+ listAllCurrentNewsItem.size());
	     
		ProgressBar pBar = (ProgressBar)findViewById(R.id.loading_bar);
		pBar.setVisibility(View.VISIBLE);
		
		 Picasso p = Picasso.with(this);
		    RequestCreator rq = null;
		 if(imageUrl!=null && !imageUrl.trim().isEmpty())
		 {
			 rq = p.load(imageUrl);
			 rq.resize(imgWidth, imgHeight);
			 rq.centerInside();
			 rq.noFade();
		 }else
				rq = p.load(R.drawable.no_image_large);
		 
	     rq.into(currentImage, new Callback() {
			
			@SuppressLint("NewApi")
			@Override
			public void onSuccess() {
				
				ProgressBar p = (ProgressBar)findViewById(R.id.loading_bar);
				 if(p != null){
	                    
					 p.setVisibility(View.GONE);  
	                    
	                }	
			}
			
			@Override
			public void onError() {
				ProgressBar p = (ProgressBar)findViewById(R.id.loading_bar);
				 if(p != null){
					 
	                    p.setVisibility(View.GONE);
	                }
			}

			
		});

	     setNextPrevButtonsState();
	    
	}
	private void setListOfNewsItems(){
		
		if(listAllCurrentNewsItem != null){
			listAllCurrentNewsItem.clear();
		}

		DBHandler_MainNews dbHMain = new DBHandler_MainNews(this);
		Object_ListItem_MainNews currentNewsItem = dbHMain.getNewsItemWithId(newsId);

		String parentHeading = "";
		if (currentNewsItem != null) {
			parentHeading = currentNewsItem.getHeading();
		}
		DBHandler_SubNews dbHSub = new DBHandler_SubNews(
				this);
		listAllCurrentNewsItem = dbHSub.getAllSubNewsItem(newsId,
				parentHeading);

		if (currentNewsItem != null) {
			Object_SubNewsItem temp = new Object_SubNewsItem();
			temp.setNewsContent(currentNewsItem.getContent());
			temp.setNewsHeading(currentNewsItem.getHeading());
			temp.setNewsImagePath(currentNewsItem.getImagePath());
			temp.setNewsImageTagline(currentNewsItem.getImageTagline());
			temp.setNewsVideo(currentNewsItem.getVideo());

			listAllCurrentNewsItem.add(0, temp);
		}
		
		if(listAllCurrentNewsItem.size() > 0){
			
			if (getIntent().hasExtra("itemNo"))
				setImage(getIntent().getIntExtra("itemNo", 0));
			else
				setImage(0);
			
			/*
			if(listAllCurrentNewsItem.size() == 1){
				ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
				ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
				
				imgBtnNext.setVisibility(View.GONE);
				imgBtnPrev.setVisibility(View.GONE);
			}
			///
		}else{
			
			noImagesFound();
		}
	}
	
	//////
	  private void setArrayImages1(){
	successCount = 0;
	arrayImages = new ArrayList<GestureImageView>();
	
	for(Object_SubNewsItem obj : listAllCurrentNewsItem){
    
		String imageUrl = obj.getNewsImagePath();
		GestureImageView gImageView = new GestureImageView(this);
	
		Point size= Globals.getScreenSize(this);
		int imgWidth=size.x;
		int imgHeight=size.y ;
	
		RelativeLayout.LayoutParams lpImg =  new RelativeLayout.LayoutParams(imgWidth, imgHeight);//(LinearLayout.LayoutParams) iv.getLayoutParams();//new LinearLayout.LayoutParams(imgWidth,imgHeight);
		gImageView.setLayoutParams(lpImg);		
		gImageView.setMinimumHeight(200);
		gImageView.setMaxScale(10);
		gImageView.setMinScale((float) 0.1);
		gImageView.setStrict(true);
	
	//Globals.loadImageIntoImageView(gImageView, imageUrl, context,R.drawable.loading_image_large, R.drawable.no_image_large);
	
	try {
		Picasso p = Picasso.with(this);
		RequestCreator rq = null;

		if(!imageUrl.trim().isEmpty()){
			rq = p.load(imageUrl);
			rq.placeholder(R.drawable.loading_image_large);
			rq.error(R.drawable.no_image_large);
		}
		else
			rq = p.load(R.drawable.no_image_large);
		
		rq.into(gImageView, new Callback() {
			
			@Override
			public void onSuccess() {
				Log.i("DARSH", "Image Loaded");
				{
					successCount++;
					
					if(successCount == listAllCurrentNewsItem.size())
					{
						//setViewPager();
					//viewPager.refreshDrawableState();
						//Log.i("DARSH", "pagerAdp loading again");
					}
					//viewPager.removeAllViews();
					//viewPager.setAdapter(pagerAdp);
				
				}
				
					
			}

			@Override
			public void onError() {
				Log.e("DARSH", "Image Loaded ERRROR");
			}
		  
			
		});
		
		
	} catch (Exception e) {
		Log.e("DARSH", "Error in loading image with url "+ imageUrl);
	}
	
	
    	arrayImages.add(gImageView);
	}
}

	private void setViewPager(){
		
	
		if(viewPager != null){
			//setArrayImages();
			ArrayList<String> arrayURLS = new ArrayList<String>();
			for(Object_SubNewsItem obj : listAllCurrentNewsItem){
				arrayURLS.add(obj.getNewsImagePath());
			}
			pagerAdp= new Custom_AdaptorImagesPager(this, arrayImages,arrayURLS );
			
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {
		    	 
				
				@Override
				public void onPageSelected(int arg0) {
					if(arg0 < listAllCurrentNewsItem.size()){
						currentSubItemNo = arg0;
						setNextPrevButtonsState();
						//String imageUrl = listAllCurrentNewsItem.get(arg0).getNewsImagePath();

					}

					Log.i("HARSH1", "onPageSelected arg0 " + arg0);
					
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					//Log.i("HARSH1", "onPageScrolled arg0 " + arg0 +" , arg1 "+arg1 +", arg2 "+ arg2);
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					//txtHead.setText("");
					Log.i("HARSH1", "onPageScrollStateChanged arg0 " + arg0);
					
				}
			});

			viewPager.removeAllViews();
			viewPager.setAdapter(pagerAdp);
			
			
			if (getIntent().hasExtra("itemNo")){
				viewPager.setCurrentItem(getIntent().getIntExtra("itemNo", 0));
				currentSubItemNo = getIntent().getIntExtra("itemNo", 0);
			}
			setNextPrevButtonsState();

		}
	}
	 
	
*/