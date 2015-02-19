package com.xercesblue.newspaperapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class Activity_ZoomedImage extends Activity {
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zoomed_image);
		
		initZoomImage();
	}
	
	private String imageUrl;
	private void initZoomImage(){
		
		Point size= Globals.getScreenSize(this);
		int imgWidth=size.x;
		int  btnCloseHeightWidth = size.x/8;
		int imgHeight=size.y ;//- btnCloseHeightWidth;
			
		final ImageView iv = (ImageView)findViewById(R.id.image_zoom);
        //iv.setFocusable(true);
		LinearLayout.LayoutParams lpImg = (LinearLayout.LayoutParams) iv.getLayoutParams();//new LinearLayout.LayoutParams(imgWidth,imgHeight);
		lpImg.height = imgHeight;
		lpImg.weight = imgWidth;
		iv.setLayoutParams(lpImg);
		
		ImageButton btnClose = (ImageButton)findViewById(R.id.imgBtnClose);
		RelativeLayout.LayoutParams lpbtn =(RelativeLayout.LayoutParams) btnClose.getLayoutParams();//new RelativeLayout.LayoutParams(btnCloseHeightWidth,btnCloseHeightWidth);
		lpbtn.height = btnCloseHeightWidth;
		lpbtn.width = btnCloseHeightWidth;
		btnClose.setLayoutParams(lpbtn);

		imageUrl = getIntent().getStringExtra("Url");
		 Picasso p = Picasso.with(this);
		    RequestCreator rq = null;
		 if(imageUrl!=null)
		 {
			 rq = p.load(imageUrl);
			 rq.resize(imgWidth, imgHeight);
			 rq.centerInside();
			 rq.noFade();
		 }
	     rq.into(iv, new Callback() {
			
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

		ImageView iv = (ImageView)findViewById(R.id.image_zoom);
		
	    Bitmap adv = ((BitmapDrawable)iv.getDrawable()).getBitmap();//BitmapFactory.decodeResource(getResources(), R.drawable.adv);
	    Intent share = new Intent(Intent.ACTION_SEND);
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

	
}
