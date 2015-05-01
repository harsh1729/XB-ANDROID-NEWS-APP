package in.seemasandesh.newspaperapp;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoomslidersample.ImageSource;
import com.example.zoomslidersample.SubsamplingScaleImageView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class Activity_EPaperReader extends SlidingFragmentActivity {

	public ProgressDialog mDialog;
	private ListView expListPapers;
	private SlidingMenu slidingMenu;
	SubsamplingScaleImageView imageViewEpaper ;
	ArrayList<Object_Epaper> list;
	static Object_Cities selectedCity;
	String image_url;
	int currentPos = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epaper_reader);
		setBehindContentView(R.layout.container_expandable_list_epaper);
		initReader();
	}
	
	private void initReader(){
		ImageButton imgButtonToggle = (ImageButton)(findViewById(R.id.imgHeaderBtnLeft));

		imgButtonToggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickToggle(v);
			}
		});


		Custom_ThemeUtil.onActivityCreateSetTheme(this);
		
		TextView txtHeading = ((TextView)findViewById(R.id.txtHeader));
		txtHeading.setText("ई पेपर");//
		
		txtHeading.setPadding(30, 0, 0, 0);
		
		imageViewEpaper = (SubsamplingScaleImageView)findViewById(R.id.imageViewEpaper);
		
		mDialog = Globals.showLoadingDialog(mDialog, this,false);			

		expListPapers = (ListView) findViewById(R.id.expListPapers);
		expListPapers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				onClickNewsPage(pos+1);
			}
			
		});
		
		int slidingWidth = Globals.getScreenSize(this).x*2/5;
		slidingMenu = getSlidingMenu();
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow_sliding_menu);
		slidingMenu.setFadeDegree(0.85f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindWidth(slidingWidth);
		
		configureEpaper();
		
	}
	
	
	
	public void onClickPrev(View v){
		if(currentPos > 1)
		{
			onClickNewsPage(currentPos - 1);
			
		}
	}
	
	public void onClickNext(View v){
		
		if(currentPos < selectedCity.total_pages)
		{
			onClickNewsPage(currentPos + 1);
			
		}
	}

	
	
	
	private void configureEpaper() {
	
		try{
			 list = new ArrayList<Object_Epaper>();
			
				
				for(int i=1; i <= selectedCity.total_pages; i++)
				{
					Object_Epaper ob = new Object_Epaper();
					ob.url = getEpaperThumbImageUrl(i, selectedCity.thumb_image_url);
					ob.id = i;
					list.add(ob);
				}
				
			if(list.size() > 0)
				setSlidingMenu(list);
			else
				Toast.makeText(this, "No paper found !", Toast.LENGTH_SHORT).show();
			
		}catch(Exception ex){
			
		}
		
	}
	
	private String getEpaperThumbImageUrl(int pos , String url){
		
		return url.replace("_1.jpg", "_"+pos+".jpg");
	}
	
private String getEpaperBigImageUrl(int pos , String url){
		
		return url.replace(".pdf", "_"+pos+".jpg");
	}
	
	public void onClickNewsPage(int pos) {

		if(selectedCity.total_pages >= pos){	
			
			currentPos = pos;			
			image_url = getEpaperBigImageUrl(pos, selectedCity.pdf_url);
			this.showContent();
				
			mDialog = Globals.showLoadingDialog(mDialog, this, false);
			Bitmap bitImage = loadFromExternalMemory(image_url);
			if( bitImage != null){
				showImage(bitImage);
				Log.i("DARSH", "Image Found");
			}else{
				Picasso.with(this).load(image_url).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(target);		//.downloader(new OkHttpDownloader(this))
			}
				
			setNextPrevButtonsState();	
		}   
	}
	
	private void setNextPrevButtonsState(){
		ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
		ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
		
		if(currentPos == 1){
			imgBtnPrev.setVisibility(View.GONE);
		}else{
			imgBtnPrev.setVisibility(View.VISIBLE);
		}
		
		if(currentPos == list.size()){
			imgBtnNext.setVisibility(View.GONE);
		}else{
			imgBtnNext.setVisibility(View.VISIBLE);
		}
		if(currentPos < list.size()){
			//txtHead.setText(listAllCurrentNewsItem.get(currentSubItemNo).getNewsHeading());
			//txtIndicator.setText(currentSubItemNo+1 +" / "+ listAllCurrentNewsItem.size());
		}
	}
	private Bitmap loadFromExternalMemory(String url){
		
		String imageName = "";
		if(url != null && !url.trim().isEmpty()){
			  String[] array = url.split(File.separator);
			  
			  if(array.length > 0){
				  imageName = array[array.length -1];
			  }
		  }else{
			  return null;
		  }
		
		String filePath =  Environment.getExternalStorageDirectory()
	            + File.separator +Globals.getStringFromResources(this, R.string.app_name);
		String imagePath = filePath  + File.separator +imageName;
	    
		Bitmap bitmap = null;
		try{
			bitmap = BitmapFactory.decodeFile(imagePath);
		}catch(java.lang.OutOfMemoryError ex){
			
			//Toast.makeText(Activity_EPaperReader.this, "Failed to load image, please try again.",Toast.LENGTH_SHORT ).show();
		}
		 return bitmap;  	
	}
	
	private void saveToExternalMemory(Bitmap bitmap, String url){
		
		String imageName = "";
		if(url != null && !url.trim().isEmpty()){
			  String[] array = url.split(File.separator);
			  
			  if(array.length > 0){
				  imageName = array[array.length -1];
			  }
		  }else{
			  return;
		  }
		
		String filePath =  Environment.getExternalStorageDirectory()
	            + File.separator +Globals.getStringFromResources(this, R.string.app_name);
		String imagePath = filePath  + File.separator +imageName;
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    File f = new File(filePath);
	    try {
	    	if (!f.exists()) {
                f.mkdirs();
            }
	    	
	    	f= new File(imagePath);
	        f.createNewFile();
	        new FileOutputStream(f).write(bytes.toByteArray());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	}
	
	private void showImage(Bitmap bitmap){
		imageViewEpaper.setImage(ImageSource.bitmap(bitmap));;
    	image_url = "";
    	Globals.hideLoadingDialog(mDialog);
	}
	
	private Target target = new Target() {
	    @Override
	     public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
	    	saveToExternalMemory(bitmap, image_url);
	    	showImage(bitmap);
	     }
		@Override
		public void onBitmapFailed(Drawable arg0) {
			
			Toast.makeText(Activity_EPaperReader.this, "Failed to load image, please try again.",Toast.LENGTH_SHORT ).show();
			Globals.hideLoadingDialog(mDialog);
			
			//picassoCache.clear();
			
		}
		@Override
		public void onPrepareLoad(Drawable arg0) {
			
		}
	};
	
	@Override 
	public void onDestroy() {  // could be in onPause or onStop
		Log.i("DARSH", "onDestroy");

		selectedCity = null;
	   Picasso.with(this).cancelRequest(target);
	   super.onDestroy();
	}

	private void setSlidingMenu(ArrayList<Object_Epaper> list){
		Custom_AdapterEpaper adapter = new Custom_AdapterEpaper(this,
				list);
		expListPapers.setAdapter(adapter);
		onClickNewsPage(1);
	}
	
	public void onClickToggle(View v) {
		this.toggle();
	}
	
	

}
