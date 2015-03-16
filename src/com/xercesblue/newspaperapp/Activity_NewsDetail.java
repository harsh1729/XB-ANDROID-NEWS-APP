package com.xercesblue.newspaperapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class Activity_NewsDetail extends SherlockFragmentActivity implements
BaseSliderView.OnSliderClickListener {
	int newsId;
	private SliderLayout slider;

	//int nextNewsId;
	//int prevNewsId;
	private String shareLink;
	private ListView listViewOptions;
	private ArrayList<Object_Options> listOptions;
	private Object_ListItem_MainNews currentNewsItem;
	//private Object_ListItem_MainNews nextNewsItem;
	//private Object_ListItem_MainNews prevNewsItem;
	private ArrayList<Object_SubNewsItem> listAllCurrentNewsItem;
	private Boolean firstTime = true;
	private ProgressDialog mDialog;
	private LinearLayout sliderContainer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		initNewsDetail();
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(firstTime){

			firstTime = false;
			showNewsDeatils();
			//handler.sendEmptyMessage(0);
		}
	}


	private void initNewsDetail(){
		Custom_ThemeUtil.onActivityCreateSetTheme(this);

		if (getIntent().hasExtra("newsId"))
			newsId = getIntent().getIntExtra("newsId", 0);
		else {
			Globals.showAlertDialogError(this,
					"Error Occured, Please try again");
		}

		// headerHeight =
		// getIntent().getIntExtra("headerHeight",Globals.getScreenSize(this).y/10);

		//nextNewsId = -1;
		//prevNewsId = -1;
		sliderContainer = (LinearLayout) findViewById(R.id.llytSliderContainer);

		ImageButton imgButtonBack = (ImageButton)(findViewById(R.id.imgHeaderBtnLeft));
		ImageButton imgButtonOptions = (ImageButton)(findViewById(R.id.imgHeaderBtnRight));

		imgButtonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackClick(v);
			}
		});

		imgButtonOptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickOptions(v);

			}
		});
	}

	private void showNewsDeatils() {
		mDialog = Globals.showLoadingDialog(mDialog, this,true);

		if(sliderContainer == null){
			sliderContainer = (LinearLayout) findViewById(R.id.llytSliderContainer);
		}

		sliderContainer.removeAllViews();
		slider = new SliderLayout(this);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		slider.setLayoutParams(lp);

		slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
		slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
		slider.setDuration(2000);

		Thread thread = new Thread() {  
			public void run() {
				taskThread();

			}
		};
		thread.start();

		// Globals.hideLoadingDialog(mDialog);   

	}

	private void taskThread(){

		DBHandler_MainNews dbHMain = new DBHandler_MainNews(this);
		
		//HashMap<String, Object_ListItem_MainNews> items = dbHMain.getNewsItemsWithId(newsId);

		currentNewsItem = dbHMain.getNewsItemWithId(newsId);
		//nextNewsItem = dbHMain.getNextNewsItem(newsId);
		//prevNewsItem = dbHMain.getPrevNewsItem(newsId);

		//currentNewsItem = items.get(Globals.NEWS_ITEM);
		//nextNewsItem = items.get(Globals.NEWS_ITEM_NEXT);
		//prevNewsItem = items.get(Globals.NEWS_ITEM_PREV);

		String parentHeading = "";
		if (currentNewsItem != null) {
			parentHeading = currentNewsItem.getHeading();
		}
		DBHandler_SubNews dbHSub = new DBHandler_SubNews(
				Activity_NewsDetail.this);
		listAllCurrentNewsItem = dbHSub.getAllSubNewsItem(newsId,
				parentHeading);

		//Object_AppConfig objConfig = new Object_AppConfig(this);

		if (currentNewsItem != null) {
			Object_SubNewsItem temp = new Object_SubNewsItem();
			temp.setNewsContent(currentNewsItem.getContent());
			temp.setNewsHeading(currentNewsItem.getHeading());
			temp.setNewsImagePath(currentNewsItem.getImagePath());
			temp.setNewsImageTagline(currentNewsItem.getImageTagline());
			temp.setNewsVideo(currentNewsItem.getVideo());

			listAllCurrentNewsItem.add(0, temp);

			shareLink = currentNewsItem.getShareLink();
		}

		Log.i("HARSH", "Count of news item is " + listAllCurrentNewsItem.size());
		
		int itemNo = 0;
		for (Object_SubNewsItem item : listAllCurrentNewsItem) {
			final TextSliderView textSliderView = new TextSliderView(this);
			textSliderView
			.description(item.getNewsContent())
			.image(
					item.getNewsImagePath())
					.Heading(item.getNewsHeading())
					.setScaleType(BaseSliderView.ScaleType.CenterCrop)
					.setOnSliderClickListener(this);

			textSliderView.setImageTag(item.getNewsImageTagline());

			/*
			if (nextNewsItem != null) {
				textSliderView.setNextHeading(nextNewsItem.getHeading());
				textSliderView.setNextImageURL(objConfig
						.getNewsImagesFullPath() + nextNewsItem.getImage());
				nextNewsId = nextNewsItem.getId();
			}

			if (prevNewsItem != null) {
				textSliderView.setPrevHeading(prevNewsItem.getHeading());
				textSliderView.setPrevImageURL(objConfig
						.getNewsImagesFullPath() + prevNewsItem.getImage());
				prevNewsId = prevNewsItem.getId();
			}

			 */
			//textSliderView.getBundle().putString("imgURL",objConfig.getNewsImagesFullPath() + item.getNewsImage());

			textSliderView.getBundle().putInt("itemNo", itemNo);
			itemNo ++;
			slider.addSlider(textSliderView);



		}
		this.runOnUiThread(new Runnable() {
			public void run() {
				slider.notifyDataSetChange();
				Globals.hideLoadingDialog(mDialog);
				sliderContainer.addView(slider);
				
				
				//list.add(nextNewsItem);
				//list.add(prevNewsItem);
				createHorizontalNewsSlider();
			}
		});
	}
	
	private void createHorizontalNewsSlider()
	{		
		LinearLayout container = (LinearLayout)findViewById(R.id.llytscrollNewsContainer);
		container.removeAllViews();
		HorizontalScrollView scrollView = (HorizontalScrollView)findViewById(R.id.scrollViewHorizontalNews);
		scrollView.scrollTo(0, 0);
		
		if(currentNewsItem == null)
		{
			return ;
		}
		List<Object_ListItem_MainNews> list = null;
		//list.add(currentNewsItem);
		
		DBHandler_MainNews dbH = new DBHandler_MainNews(this);
		list = dbH.getFellowCategoryNewsItemsForId(currentNewsItem.getId(), currentNewsItem.getCatId());
		
		if( list.size() == 0){
			return ;
		}else if(list.size() < 3){
			HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			container.setLayoutParams(params);
		}
		
		//Object_AppConfig objConfig = new Object_AppConfig(this);
		Point pnt = Globals.getScreenSize(this);

		//int containerHeight = pnt.y;
		int containerWidth = pnt.x;
		//Toast.makeText(this, "List count is "+list.size(), Toast.LENGTH_LONG).show();
		for(Object_ListItem_MainNews obj : list){
			
			try{
			LayoutInflater inflater = LayoutInflater.from(this);
			View rowItem = inflater.inflate(R.layout.row_item_news_horizontal_scroller, null, false);
			LinearLayout.LayoutParams lp = new LayoutParams(containerWidth/3, LinearLayout.LayoutParams.WRAP_CONTENT);
			
			rowItem.setLayoutParams(lp);
			
			int imgNextPrevHeightWidth = (int) (containerWidth/6.5);
			
			
			TextView txtNextHeading = (TextView) rowItem.findViewById(R.id.txtHeading);
			txtNextHeading.setText(obj.getHeading());

			ImageView imgView= (ImageView)rowItem.findViewById(R.id.imgNews);
			LinearLayout.LayoutParams lpImgPrev = (LayoutParams) imgView.getLayoutParams();
			lpImgPrev.height = imgNextPrevHeightWidth;
			lpImgPrev.width = imgNextPrevHeightWidth;
			imgView.setLayoutParams(lpImgPrev);
			setImage(imgView,  obj.getImagePath());
			
			TextView txtHiddenNewsId = (TextView) rowItem.findViewById(R.id.txtHiddenNewsId);
			txtHiddenNewsId.setText(obj.getId()+"");
			
			container.addView(rowItem);
			}catch (Exception e) {
				
				Log.i("HARSH", "EXCEPTION IN ADD NEWS ROW");
			}
			
		}
		
		
	}
	protected void setImage(ImageView targetImageView ,String mUrl){
		Picasso p = Picasso.with(this);

		RequestCreator rq = null;
		if (mUrl != null) {

			rq = p.load(mUrl);

		} 

		if (rq == null) {
			return;
		}

		rq.placeholder(R.drawable.loading_with_boundry);
		rq.error(R.drawable.no_image);
		
		rq.fit().centerCrop();
		

		rq.into(targetImageView, new Callback() {
			@Override
			public void onSuccess() {
				
			}

			@Override
			public void onError() {
				
			}
		});
	}
	public void onBackClick(View v) {
		finish();
	}

	
	
	public void onClickNews(View v) {
		
		try{
			
		TextView txtHiddenNewsId = (TextView) v.findViewById(R.id.txtHiddenNewsId);
		int nextNewsId = Integer.parseInt(txtHiddenNewsId.getText().toString());
			if (nextNewsId >= 0) {
				newsId = nextNewsId;
				showNewsDeatils();
			}
		}catch(Exception ex){
			
			Toast.makeText(this, "Some error occured Please try again ", Toast.LENGTH_SHORT ).show();
		}

	}
	/*
	public void onClickNext(View v) {
		if (nextNewsId >= 0) {

			newsId = nextNewsId;
			showNewsDeatils();
		}

	}

	public void onClickPrev(View v) {
		if (prevNewsId >= 0) {

			newsId = prevNewsId;
			showNewsDeatils();
		}
	}
	*/

	@Override
	public void onSliderClick(BaseSliderView slider) {
		
		//String url = (String) slider.getBundle().get("imgURL");
		int itemNo = slider.getBundle().getInt("itemNo");
		
		if (slider.isImageload) {
			Intent i = new Intent(this, Activity_ZoomedImage.class);
			//i.putExtra("Url", url);
			i.putExtra("newsId", newsId);
			i.putExtra("itemNo", itemNo);
			startActivity(i);
		}
	}

	public void onClickOptions(View v) {

		if (listViewOptions == null) {
			listViewOptions = (ListView) findViewById(R.id.listViewOptions);
		}

		toggleOptionsVisibility(null);
	}

	public void initOptionsList() {

		// new
		// ListView(this);

		//String[] values = new String[] { "Save News", "Share News" };
		//int[] imgIds = new int[] { R.drawable.selector_options_save,R.drawable.selector_options_share };

		String[] values = new String[] { Globals.OPTION_SAVE, Globals.OPTION_SHARE};//, Globals.OPTION_SETTINGS };
		ArrayList<StateListDrawable> listDrawable = getDrawableList();

		listOptions = new ArrayList<Object_Options>();

		for (int i = 0; i < values.length; i++) {

			Object_Options obj = new Object_Options();
			obj.setText(values[i]);
			//obj.setImageResourceId(imgIds[i]);
			if(listDrawable.size() > i)
				obj.setStateDrawable(listDrawable.get(i));

			listOptions.add(obj);
		}

		Custom_AdapterOptions adapter = new Custom_AdapterOptions(this,
				listOptions);
		listViewOptions.setAdapter(adapter);

		listViewOptions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {

				optionSelected(pos);
			}
		});
	}

	private ArrayList<StateListDrawable> getDrawableList(){
		ArrayList<StateListDrawable> listDrawable = new ArrayList<StateListDrawable>();

		try{

			StateListDrawable stateSave = new StateListDrawable();
			stateSave.addState(new int[] {android.R.attr.state_pressed},
					getResources().getDrawable(R.drawable.save_selected));
			stateSave.addState(new int[] {android.R.attr.state_focused},
					getResources().getDrawable(R.drawable.save_selected));
			stateSave.addState(new int[] { },
					getResources().getDrawable(Custom_ThemeUtil.getSaveImageId(this)));

			StateListDrawable stateShare = new StateListDrawable();
			stateShare.addState(new int[] {android.R.attr.state_pressed},
					getResources().getDrawable(R.drawable.share_selected));
			stateShare.addState(new int[] {android.R.attr.state_focused},
					getResources().getDrawable(R.drawable.share_selected));
			stateShare.addState(new int[] { },
					getResources().getDrawable(Custom_ThemeUtil.getShareImageId(this)));

			StateListDrawable stateSettings = new StateListDrawable();
			stateSettings.addState(new int[] {android.R.attr.state_pressed},
					getResources().getDrawable(R.drawable.settings_selected));
			stateSettings.addState(new int[] {android.R.attr.state_focused},
					getResources().getDrawable(R.drawable.settings_selected));
			stateSettings.addState(new int[] { },
					getResources().getDrawable(Custom_ThemeUtil.getSettingsImageId(this)));



			listDrawable.add(stateSave);
			listDrawable.add(stateShare);
			listDrawable.add(stateSettings);
		}catch(Exception ex){

		}
		return listDrawable;
	}
	public void toggleOptionsVisibility(View v) {

		LinearLayout parent = (LinearLayout) findViewById(R.id.llytOptionsContainer);
		if (listViewOptions.getVisibility() == View.VISIBLE) {
			// Its visible
			parent.setVisibility(View.GONE);
			listViewOptions.setVisibility(View.GONE);
		} else {
			// Either gone or invisible
			initOptionsList();
			parent.setVisibility(View.VISIBLE);
			listViewOptions.setVisibility(View.VISIBLE);
		}
	}

	private void optionSelected(int pos) {
		toggleOptionsVisibility(null);
		//toggleOptionsVisibility(null);

		if (listOptions != null && listOptions.size() > pos) {
			Object_Options obj = listOptions.get(pos);
			if (obj != null) {

				Class<?> nextClass = null;

				if(obj.getText().equals(Globals.OPTION_SAVE)){
					saveNews();

				}else if(obj.getText().equals(Globals.OPTION_SHARE)){
					shareNews(pos);
				}
				else if(obj.getText().equals(Globals.OPTION_SETTINGS)){

					nextClass = Activity_Settings.class;
				}
				if (nextClass != null) {
					Intent intent = new Intent(this, nextClass);
					startActivity(intent);
				}
			}
		}
	}

	private void saveNews() {

		DBHandler_MainNews dbH = new DBHandler_MainNews(this);
		dbH.saveNews(newsId);

		Toast.makeText(this,"News saved successfully", Toast.LENGTH_SHORT)
		.show();
	}

	/* Selective Share

	 private void shareNews(int pos) {

		if (currentNewsItem != null) {

			//
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, currentNewsItem.getHeading()
					+ "\n\n" + currentNewsItem.getContent() + " \n\nCourtesy: XB NEWS ");
			sendIntent.setPackage("com.whatsapp");
			sendIntent.setPackage("com.twitter.android");
			sendIntent.setPackage("com.facebook.katana");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			//

			List<Intent> targetShareIntents=new ArrayList<Intent>();
		    Intent shareIntent=new Intent();
		    shareIntent.setAction(Intent.ACTION_SEND);
		    shareIntent.setType("text/plain");
		    List<ResolveInfo> resInfos=getPackageManager().queryIntentActivities(shareIntent, 0);
		    if(!resInfos.isEmpty()){
		        System.out.println("Have package");
		        for(ResolveInfo resInfo : resInfos){
		            String packageName=resInfo.activityInfo.packageName;
		            Log.i("Package Name", packageName);
		            if(packageName.contains("com.twitter.android")  || packageName.contains("android.gm")|| packageName.contains("android.email")||  packageName.contains("mms") || packageName.contains("com.facebook.katana") || packageName.contains("com.whatsapp")){
		                Intent intent=new Intent();
		                intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
		                intent.setAction(Intent.ACTION_SEND);
		                intent.setType("text/plain");
		                intent.putExtra(Intent.EXTRA_SUBJECT,currentNewsItem.getHeading()+"\n\n" );
		                if(currentNewsItem.getShareLink()  != null && !currentNewsItem.getShareLink().trim().equals("")){
		                		intent.putExtra(Intent.EXTRA_TEXT, currentNewsItem.getContent()+"\n\nCourtesy: XB NEWS \n"+currentNewsItem.getShareLink());
		                }else{
		                		intent.putExtra(Intent.EXTRA_TEXT, currentNewsItem.getContent()+"\n\nCourtesy: XB NEWS \n"+Globals.DEFAULT_APP_WEBSITE);
		                }



		                intent.setPackage(packageName);
		                targetShareIntents.add(intent);
		            }
		        }
		        if(!targetShareIntents.isEmpty()){
		            System.out.println("Have Intent");
		            Intent chooserIntent=Intent.createChooser(targetShareIntents.remove(0), "Choose app to share news");
		            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
		            startActivity(chooserIntent);
		        }else{
		            Log.i("HARSH","Do not Have Intent");
		        }
		    }
		}

	}
	 */

	private void shareNews(int pos) {

		if (currentNewsItem != null) {

			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_SUBJECT,currentNewsItem.getHeading()+"\n\n" );
			if(currentNewsItem.getShareLink()  != null && !currentNewsItem.getShareLink().trim().equals("")){
				sendIntent.putExtra(Intent.EXTRA_TEXT, currentNewsItem.getContent()+"\n\nCourtesy: XB NEWS \n"+currentNewsItem.getShareLink());
			}else{
				sendIntent.putExtra(Intent.EXTRA_TEXT, currentNewsItem.getContent()+"\n\nCourtesy: XB NEWS \n"+Globals.DEFAULT_APP_WEBSITE);
			}
			//sendIntent.setPackage("com.whatsapp");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
		}
	}

}
