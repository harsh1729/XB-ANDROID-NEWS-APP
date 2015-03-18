package com.xercesblue.newspaperapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import android.util.Log;
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
	
	int currentSubItemNo;
	int newsId;
	GestureImageView currentImage;
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

		setListOfNewsItems();
  
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
				rq = p.load(R.drawable.no_image);
		 
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
			*/
		}else{
			
			noImagesFound();
		}
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
		/*
		 Intent share = new Intent(Intent.ACTION_SEND);
	        share.setType("image/*");

	        //share.setPackage("com.whatsapp");

	        share.putExtra(android.content.Intent.EXTRA_SUBJECT,  "Subject");
	        
	       Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+"share_red.png");
	       Log.i("HARSH", "URI "+uri);
	        //if (imageUrl!=null){
	            share.putExtra(Intent.EXTRA_STREAM,
	            		uri);
	        //}
	        this.startActivity(Intent.createChooser(share, "Share Image With"));
	        */
		
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
	}
}
