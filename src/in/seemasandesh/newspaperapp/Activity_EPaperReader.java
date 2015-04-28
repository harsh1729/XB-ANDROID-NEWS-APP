package in.seemasandesh.newspaperapp;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
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
	String image_url;
	int currentPos = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epaper_reader);
		setBehindContentView(R.layout.container_expandable_list_epaper);
		initReader();
		getEPaperDetailsFromServer();
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
		txtHeading.setText("E Paper");
		
		imageViewEpaper = (SubsamplingScaleImageView)findViewById(R.id.imageViewEpaper);
		
	}
	
	
	
	public void onClickPrev(View v){
		if(currentPos > 0)
		{
			onClickNewsPage(currentPos - 1);
			
		}
	}
	
	public void onClickNext(View v){
		
		if(currentPos < list.size()-1)
		{
			onClickNewsPage(currentPos + 1);
			
		}
	}

	private void getEPaperDetailsFromServer(){
		
		try {
			
			mDialog = Globals.showLoadingDialog(mDialog, this,false);			

			expListPapers = (ListView) findViewById(R.id.expListPapers);
			expListPapers.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long id) {
					onClickNewsPage(pos);
				}
				
			});
			
			int slidingWidth = Globals.getScreenSize(this).x*2/5;
			slidingMenu = getSlidingMenu();
			slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
			slidingMenu.setShadowDrawable(R.drawable.shadow_sliding_menu);
			slidingMenu.setFadeDegree(0.85f);
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			slidingMenu.setBehindWidth(slidingWidth);
			slidingMenu.setSlidingEnabled(false);
			
			String url = "http://xercesblue.in/HARSHTEST/get_Epapers.php";
			
			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(Request.Method.POST,
					url, null,
							new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							parseAppEpaperJson(response);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							Log.i("DARSH", "ERROR VolleyError");
							
							Globals.hideLoadingDialog(mDialog);
							Globals.showAlertDialogOneButton(
									Globals.TEXT_CONNECTION_ERROR_HEADING,
									Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
									Activity_EPaperReader.this, "OK", null, false);
							if(err != null){
								Log.i("DARSH", "ERROR Details getLocalizedMessage : "+err.getLocalizedMessage());
								Log.i("DARSH", "ERROR Details getMessage : "+err.getMessage());
								Log.i("DARSH", "ERROR Details getStackTrace : "+err.getStackTrace());
							}

						}
					});

			Custom_AppController.getInstance().addToRequestQueue(
					jsonObjectRQST);

		}

		catch (Exception e) {
			Log.i("HARSH",
					"Excetion FIRSTCALL getEPaperDetailsFromServer");
			Globals.hideLoadingDialog(mDialog);


		}

	}

	
	
	private void parseAppEpaperJson(JSONObject response) {
	
		Globals.hideLoadingDialog(mDialog);
		if (response == null){
			return;
		}
		
		slidingMenu.setSlidingEnabled(true);
		Log.i("DARSH", "RESPONCE parseAppEpaperJson is : "+response.toString());
		try{
			 list = new ArrayList<Object_Epaper>();
			
			if (response.has("epaper")) {
				JSONArray arrayStates = response.getJSONArray("epaper");
				
				for(int i=0; i<arrayStates.length(); i++)
				{
					Object_Epaper ob = new Object_Epaper();
					JSONObject json_State = arrayStates.getJSONObject(i);
					ob.url = json_State.getString("image_url");
					ob.id = i;
					list.add(ob);
				}
			}
			
			
			//createHorizontalNewsSlider(list);
			if(list.size() > 0)
				setSlidingMenu(list);
			else
				Toast.makeText(this, "No paper found !", Toast.LENGTH_SHORT).show();
			
		}catch(Exception ex){
			
		}
		
	}
	
	public void onClickNewsPage(int pos) {
		if(list != null && list.size() > pos){	
			
			currentPos = pos;
			Object_Epaper obj = list.get(pos);			
			image_url = obj.url.replace("_small", "");
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
		
		if(currentPos == 0){
			imgBtnPrev.setVisibility(View.GONE);
		}else{
			imgBtnPrev.setVisibility(View.VISIBLE);
		}
		
		if(currentPos == list.size()-1){
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
	   Picasso.with(this).cancelRequest(target);
	   super.onDestroy();
	   //quit();
	}
	
	 public void quit() {
	       //int pid = android.os.Process.myPid();
	       //android.os.Process.killProcess(pid);
	        System.exit(0);
	    }
	
	
	private void setSlidingMenu(ArrayList<Object_Epaper> list){
		Custom_AdapterEpaper adapter = new Custom_AdapterEpaper(this,
				list);
		expListPapers.setAdapter(adapter);
		onClickNewsPage(0);
	}
	
	public void onClickToggle(View v) {
		this.toggle();
	}

}
