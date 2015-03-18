package com.xercesblue.newspaperapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;;

public class Activity_Home extends SlidingFragmentActivity {

	private ExpandableListView expListCategories;
	private PullToRefreshListView listViewNews;
	//private ArrayList<Object_Category> listNewsCategory = new ArrayList<Object_Category>();
	private Custom_AdapterNewsList news_adaptor;
	private SlidingMenu slidingMenu;
	//private Boolean isNewsSpinnerShown = false;
	public int currentCategoryId;
	private ListView listViewOptions;
	private ArrayList<Object_Options> listOptions;
	private ArrayList<Interface_ListItem> listdata;
	public ProgressDialog mDialog;
	private int selectedTheme;
	private float fontFactor;
	static public Boolean comingFromPushMessage = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setBehindContentView(R.layout.container_expandable_list_categories);

		initHome();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		Object_AppConfig obj = new Object_AppConfig(this);
		
		if(selectedTheme != obj.getTheme() || fontFactor != obj.getFontFactor()){
			Custom_ThemeUtil.onActivityCreateSetTheme(this);
			serverCallForCategoriesAndNews();
			selectedTheme = obj.getTheme();
			fontFactor = obj.getFontFactor();
			//showCategories();
		}
		
		
		if(comingFromPushMessage){
			comingFromPushMessage = false;
			
			Globals.showAlertDialogOneButton("News Flash", GCMIntentService.pushMessageHeader +"\n\n"+GCMIntentService.pushMessageText, this, "OK", null, false);
		}
	}
	
	/*
	 * @Override protected void onResume() { super.onResume();
	 * serverCallForCategoriesAndNews(); }
	 */
	private void initHome() {

		showLoadingScreen();
		//new Custom_GCM_Register(this);

		
		DBHandler_Main db = new DBHandler_Main(this);
		db.createDataBase();

		Object_AppConfig objAppConfig = new Object_AppConfig(this);
		selectedTheme = objAppConfig.getTheme();
		fontFactor = objAppConfig.getFontFactor();
		
		Custom_ThemeUtil.onActivityCreateSetTheme(this);
		resizeLogoImages();

		expListCategories = (ExpandableListView) findViewById(R.id.expListCategories);
		listViewNews = (PullToRefreshListView) findViewById(R.id.lst_cat_wise_news);
		listViewOptions = (ListView) findViewById(R.id.listViewOptions);


		int slidingWidth = Globals.getScreenSize(this).x*3/4;
		slidingMenu = getSlidingMenu();
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow_sliding_menu);
		slidingMenu.setFadeDegree(0.85f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindWidth(slidingWidth);
		slidingMenu.setSlidingEnabled(false);

		ImageButton imgButtonToggle = (ImageButton)(findViewById(R.id.imgHeaderBtnLeft));
		ImageButton imgButtonOptions = (ImageButton)(findViewById(R.id.imgHeaderBtnRight));

		imgButtonToggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickToggle(v);
			}
		});

		imgButtonOptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickOptionsHome(v);

			}
		});

		initPullToRefreshListProperties();
		// Get categories from Server in case of change along with Application
		// Configuration
		serverCallForCategoriesAndNews();
		
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
		if(cd.isConnectingToInternet()){
			 Custom_AppRater.app_launched(this);

		}
		
	}

	private void resizeLogoImages(){
		ImageView imgViewLogo = (ImageView)findViewById(R.id.imgViewLogo);
		ImageView imgViewName = (ImageView)findViewById(R.id.imgViewLogoName);

		LinearLayout llConatiner = (LinearLayout)findViewById(R.id.llytLoadingContainer);
		llConatiner.setEnabled(false);

		int screenWidth = Globals.getScreenSize(this).x;
		int logoWidth = screenWidth/100 * 17 ;// 17%
		int nameWidth = screenWidth/100 * 64 ;// 64%

		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo, options);
		logo = Globals.scaleToWidth(logo,logoWidth);
		Bitmap name = BitmapFactory.decodeResource(getResources(), R.drawable.logo_name, options);
		name = Globals.scaleToWidth(name,nameWidth);

		imgViewLogo.setImageBitmap(logo);
		imgViewName.setImageBitmap(name);
	}

	private void serverCallForCategoriesAndNews() {
		try {
			Log.i("HARSH", "FirstCall");
			showSpinnerCategories();			

			String url = Globals.getURL_CatNewsFirstCall();
			Log.i("HARSH", "Cat URL -- "+url);
			
			//CustomRequest jsObjRequest = new CustomRequest(Method.POST, url, params, this.createRequestSuccessListener(), this.createRequestErrorListener());
			Object_AppConfig objAppConfig = new Object_AppConfig(Activity_Home.this);
			
			Log.i("DARSH", "PARAMS"+Globals.getParams_CatNewsFirstCall(objAppConfig.getVersionNoCategory(),objAppConfig.getVersionNoAppConfig()));
		
			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(Request.Method.POST,
					url, Globals.getParams_CatNewsFirstCall(objAppConfig.getVersionNoCategory(),objAppConfig.getVersionNoAppConfig()),
							new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Log.i("DARSH", "json Response recieved !!"
									+ response.toString());

							parseAppConfigJson(response);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							Log.i("DARSH", "ERROR VolleyError");
							Globals.showAlertDialogOneButton(
									Globals.TEXT_CONNECTION_ERROR_HEADING,
									Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
									Activity_Home.this, "OK", null, false);
							if(err != null){
								Log.i("DARSH", "ERROR Details getLocalizedMessage : "+err.getLocalizedMessage());
								Log.i("DARSH", "ERROR Details getMessage : "+err.getMessage());
								Log.i("DARSH", "ERROR Details getStackTrace : "+err.getStackTrace());
							}
							showCategories();

						}
					});

			Custom_VolleyAppController.getInstance().addToRequestQueue(
					jsonObjectRQST);

		}

		catch (Exception e) {
			Log.i("HARSH",
					"Excetion FIRSTCALL" + e.getMessage() + "\n"
							+ e.getStackTrace());
			showCategories();


		}

	}

	private void initPullToRefreshListProperties() {
		//listViewNews.setDividerDrawable(getResources().getDrawable(R.color.app_lightgray));
		//listViewNews.setD;

		listViewNews
		.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);

		listViewNews.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				
				Object_ListItem_MainNews objMainNewsItem = null;
				
				if (listViewNews.getCurrentMode().toString().equals("PULL_FROM_START"))
				{
					objMainNewsItem = getFirstNewsItem();
					if(objMainNewsItem != null){
						getNewsDataFromServer(currentCategoryId,
								Globals.CALLTYPE_NEW, objMainNewsItem.getId(), true,Globals.FINAL_NEWS_LIMIT_REFRESH);
					}
							
				}

				else if (listViewNews.getCurrentMode().toString().equals("PULL_FROM_END"))
				{
					objMainNewsItem = getLastNewsItem();
					if(objMainNewsItem != null){
						getNewsDataFromServer(currentCategoryId,
								Globals.CALLTYPE_OLD,
								objMainNewsItem.getId(), true,Globals.FINAL_NEWS_LIMIT_REFRESH);
					}
					
				}

				///No news item found
				if(objMainNewsItem == null){
					getNewsDataFromServer(currentCategoryId,
							Globals.CALLTYPE_FRESH, 0, true,Globals.FINAL_NEWS_LIMIT_FIRST_CALL);
				}

			}
		});

		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
				this);
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.reset_sound);
		listViewNews.setOnPullEventListener(soundListener);

		//listViewNews.getLoadingLayoutProxy().setLoadingDrawable(getResources().getDrawable(R.drawable.refresh_black));
		listViewNews.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				navigateToNewsDetail(position);
			}
		});

	}

	private Object_ListItem_MainNews getFirstNewsItem(){
		Object_ListItem_MainNews item = null;
		for (int i =0 ; i < getListData().size();i++){
			if(getListData().get(i).getClass() == Object_ListItem_MainNews.class){
				item =(Object_ListItem_MainNews) getListData().get(i);
				break;
			}
		}
		
		return item;
	}
	
	private Object_ListItem_MainNews getLastNewsItem(){
		Object_ListItem_MainNews item = null;
		for (int i =getListData().size() -1 ; i>=0 ;i--){
			if(getListData().get(i).getClass() == Object_ListItem_MainNews.class){
				item =(Object_ListItem_MainNews) getListData().get(i);
				break;
			}
		}
		
		return item;
	}
	
	private void navigateToNewsDetail(int pos) {
		//

		if (getListData().size() >= pos) {

			if(getListData().get(pos - 1).getClass() == Object_ListItem_MainNews.class){
				Intent i = new Intent(getApplicationContext(),
						Activity_NewsDetail.class);

				i.putExtra("newsId", getListData().get(pos - 1).getId());
				i.putExtra("navFrom", Activity_NewsDetail.NAV_FROM_HOME);
				
				startActivity(i);
			}

		}

	}




	private void parseAppConfigJson(JSONObject response) {

		if (response == null){
			showCategories();
			return;
		}
		try {
			// Set App Config
			if (response.has("appconfig_need_update")) {
				if (response.getInt("appconfig_need_update") > 0) {

					Object_AppConfig objConfig = new Object_AppConfig(this);
					if (response.has("appconfig_version")) {
						objConfig.setVersionNoAppConfig(response
								.getInt("appconfig_version"));
					}
					if (response.has("appconfig")) {
						JSONObject objJsonConfig = response
								.getJSONObject("appconfig");
						if (objJsonConfig != null) {

							if (objJsonConfig.has("server_path")) {
								objConfig.setServerPath(objJsonConfig
										.getString("server_path"));
							}
							if (objJsonConfig.has("root_cat_id")) {
								objConfig.setRootCatId(objJsonConfig
										.getInt("root_cat_id"));
							}

							/*
							if (objJsonConfig.has("newsImagesPath")) {
								objConfig.setNewsImagesPath(objJsonConfig
										.getString("newsImagesPath"));
							}

							if (objJsonConfig.has("categoryImagesPath")) {
								objConfig.setCategoryImagesPath(objJsonConfig
										.getString("categoryImagesPath"));
							}
							*/
						}
					}

				}
			}
			Object_AppConfig objConfig = new Object_AppConfig(this);
			
			//// If news is there insert new news News
					if (response.has("news")) {
						//currentCategoryId = objConfig.getRootCatId();
						insertNewAndDeleteOldNews(response.getJSONArray("news"),objConfig.getRootCatId() , false);
					}

			// Now set Categories
			if (response.has("categories_need_update")) {

				

				if (response.getInt("categories_need_update") > 0) {

					if (response.has("category_version")) {
						objConfig.setVersionNoCategory(response
								.getInt("category_version"));
					}
					GetCategoriesThread thread = new GetCategoriesThread(
							response, Activity_Home.this);
					thread.start();
				} else {
					showCategories();
				}
			} else {
				showCategories();
			}

			
			
		} catch (Exception ex) {
			showCategories();
			Log.i("HARSH", "Error in parsin jSOn" + ex.getMessage());
		}

	}

	class GetCategoriesThread extends Thread {
		JSONObject response;
		Context context;
		ArrayList<Object_Category> Cat_JsonParsed_data;

		public GetCategoriesThread(JSONObject response, Context context) {
			this.response = response;
			this.context = context;
		}

		public void run() {

			Cat_JsonParsed_data = new ArrayList<Object_Category>();
			Custom_JsonParserCategory parserObject = new Custom_JsonParserCategory(
					response.toString(), context);
			Cat_JsonParsed_data = parserObject.getCategoriesFromJson();

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					addNewCategories(Cat_JsonParsed_data);
					showCategories();
				}
			});
		}
	}

	public void addNewCategories(ArrayList<Object_Category> Cat_JsonParsed_data) {

		if (Cat_JsonParsed_data.size() > 0) {
			DBHandler_Category dbH = new DBHandler_Category(this);
			dbH.setCategories(Cat_JsonParsed_data);
		}

	}

	private void showCategories() {

		hideSpinnerCategories();

		DBHandler_Category db = new DBHandler_Category(this);
		ArrayList<Object_Category> listNewsCategory = db.getCategories(this);

		Custom_AdapterCatHome adapter = new Custom_AdapterCatHome(this,
				listNewsCategory);
		expListCategories.setAdapter(adapter);

		/*
		if (listNewsCategory.size() > 0) {
			currentCategoryId = listNewsCategory.get(0).getId();
			//getNewsDataFromServer(currentCategoryId, Globals.CALLTYPE_FRESH, 0,false);
		}else{
			hideLoadingScreen();
		}
		*/
		Object_AppConfig obj = new Object_AppConfig(this);
		showNewsList(obj.getRootCatId());
		hideLoadingScreen();
	}

	public void showSpinnerCategories() {

		/*
		ImageView imgSpinner = (ImageView) findViewById(R.id.imgSpinnerCategories);
		imgSpinner.setDrawingCacheEnabled(true);

		if (imgSpinner != null) {
			Log.i("HARSH", "Showing Spinner Cat");
			imgSpinner.startAnimation(Globals.getRotationAnimation());
			imgSpinner.setVisibility(View.VISIBLE);
		}
		 */
		ProgressBar bar = (ProgressBar)findViewById(R.id.spinnerCat);
		bar.setVisibility(View.VISIBLE);
	}

	public void hideSpinnerCategories() {

		/*
		ImageView imgSpinner = (ImageView) findViewById(R.id.imgSpinnerCategories);
		if (imgSpinner != null) {
			Log.i("HARSH", "Hiding Spinner News");
			imgSpinner.setAnimation(null);
			imgSpinner.setVisibility(View.GONE);
		}*/
		ProgressBar bar = (ProgressBar)findViewById(R.id.spinnerCat);
		bar.setVisibility(View.GONE);
	}

	/*
	private void setCategoryHeading(int catId) {

		DBHandler_Category dbH = new DBHandler_Category(this);
		TextView txt = (TextView) findViewById(R.id.txtCatHeading);

		txt.setText(dbH.getCategoryName(catId));
	}
	 
	*/
	
	public void getNewsDataFromServer(final int catId, final String callType,
			int lastNewsId, final Boolean isPullToRefresh , int limit) 
	{

		try{
			
			//listViewNews.removeAllViews();
			if (!isPullToRefresh)
				listViewNews.setAdapter(null);
			
			Custom_ConnectionDetector cd = new Custom_ConnectionDetector(
					getApplicationContext());

			if (!cd.isConnectingToInternet()) {
				if (!isPullToRefresh) {
					Globals.showAlertDialogOneButton(
							Globals.TEXT_NO_INTERNET_HEADING,
							Globals.TEXT_NO_INTERNET_DETAIL_DIALOG_MAIN_SCREEN,
							Activity_Home.this, "OK", null, false);
					showNewsList(catId);

				} else {
					Toast.makeText(
							Activity_Home.this,
							Globals.TEXT_NO_INTERNET_DETAIL_TOAST,
							Toast.LENGTH_SHORT).show();
					listViewNews.onRefreshComplete();

				}

				hideLoadingScreen();
				return;
			}

			Log.i("HARSH", "getNewsDataFromServer Request CatId = " + catId);
			if (!isPullToRefresh)
				if(!isShowingLoadingScreen())
					mDialog = Globals.showLoadingDialog(mDialog, this,false);

			Custom_VolleyArrayRequest jsonObjectRQST = new Custom_VolleyArrayRequest(Request.Method.POST,
					Globals.getURL_NewsByCategory(), Globals.getParams_NewsByCategory(catId, callType, lastNewsId, limit),
							new Listener<JSONArray>() {


						
						@Override
						public void onResponse(JSONArray response) {
							gotNewsResponce(response, catId, isPullToRefresh);
							
						}

					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							err.printStackTrace();
							listViewNews.onRefreshComplete();

							if (!isPullToRefresh) {
								Globals.showAlertDialogOneButton(
										Globals.TEXT_CONNECTION_ERROR_HEADING,
										Globals.TEXT_CONNECTION_ERROR_DETAIL_DIALOG_MAIN_SCREEN,
										Activity_Home.this, "OK", null, false);
								Globals.hideLoadingDialog(mDialog);
								hideLoadingScreen();
								showNewsList(catId);
							} else {
								Toast.makeText(
										Activity_Home.this,
										Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
										Toast.LENGTH_SHORT).show();
								listViewNews.onRefreshComplete();

							}

						}
					});

		
			Custom_VolleyAppController.getInstance().addToRequestQueue(
					jsonObjectRQST);
			
		}catch(Exception ex){

		}
	}

	private void gotNewsResponce(JSONArray response, int catId,
			Boolean isPullToRefresh) {

		if(insertNewAndDeleteOldNews(response,catId,isPullToRefresh))
			showNewsList(catId);
	}

	private boolean insertNewAndDeleteOldNews(JSONArray response, int catId,
			Boolean isPullToRefresh){
		ArrayList<Object_ListItem_MainNews> listNewsItemServer;

		Log.i("DARSH", "getNewsDataFromServer onResponse" + response);

		Custom_JsonParserNews parserObject = new Custom_JsonParserNews(
				response.toString());
		listNewsItemServer = parserObject.getParsedJsonMainNews(catId);

		if (!isPullToRefresh) {
			Globals.hideLoadingDialog(mDialog);
			hideLoadingScreen();

			DBHandler_MainNews dbH = new DBHandler_MainNews(
					getApplicationContext());
			dbH.clearNewsTable(catId);
			/*
			if (listNewsItemServer == null || listNewsItemServer.size() == 0) {
				Globals.showAlertDialogOneButton("News Not Found",
						"Please try after some time.", this, "OK", null, false);
			}
			*/
		}else if(listNewsItemServer == null || listNewsItemServer.size() == 0){
			Toast.makeText(getApplicationContext(), "No more news", Toast.LENGTH_SHORT).show();
			listViewNews.onRefreshComplete();
			
			return false;
		}

		DBHandler_MainNews dbH = new DBHandler_MainNews(getApplicationContext());
		dbH.insertNewsItemList(listNewsItemServer);
		
		
		return true;
	}
	private void showNewsList(final int catId) {

		getListData().clear();

		DBHandler_MainNews db = new DBHandler_MainNews(getApplicationContext());

		ArrayList<Object_ListItem_MainNews> listMainNewsdata = db.getMainNewsByCategory(catId);

		///Breaking News
		Object_ListItem_BreakingNews objBreakingNews = new Object_ListItem_BreakingNews();
		objBreakingNews.setHeading("Phil Huges an Australian cricketer died by hit on head from a bouncer.");
		objBreakingNews.setImagePath("Phillip-Hughes-Wallpaper.jpg");
		getListData().add(objBreakingNews);
		
		///
		
		Log.i("DARSH ", "Size of inserted news item"+listMainNewsdata.size());
		
		Object_ListItem_NewsCategory catObj = new Object_ListItem_NewsCategory();
		DBHandler_Category dbH = new DBHandler_Category(this);
		catObj.setCatName(dbH.getCategoryName(catId));

		getListData().add(catObj);
		
		if(listMainNewsdata.size() > 0){
			for (Interface_ListItem object : listMainNewsdata) {
				getListData().add(object);
			}
		}else{
			Globals.showAlertDialogOneButton("News Not Found",
					"Please try after some time.", this, "OK", null, false);
		}

		news_adaptor = new Custom_AdapterNewsList(Activity_Home.this,R.layout.row_item_news_list,
				getListData());

		runOnUiThread(new Runnable() {
			public void run() {
				if (news_adaptor != null) {
					currentCategoryId = catId;
					listViewNews.onRefreshComplete();
					//PullToRefreshListView lv1 = (PullToRefreshListView) findViewById(R.id.lst_cat_wise_news);
					// Custom_ExpandableListAdapter.main_page_adaptor.notifyDataSetChanged();
					listViewNews.setAdapter(news_adaptor);
					//setCategoryHeading(currentCategoryId);

				}

			}
		});

	}

	public ArrayList<Interface_ListItem> getListData() {

		if (listdata == null) {
			listdata = new ArrayList<Interface_ListItem>();
		}

		return listdata;
	}

	public void onClickToggle(View v) {
		this.toggle();

	}

	public void onClickOptionsHome(View v) {

		if (listViewOptions == null) {
			//initOptionsList();
			listViewOptions = (ListView) findViewById(R.id.listViewOptions);
		}

		toggleOptionsVisibility(null);
	}

	public void initOptionsList() {

		// new
		// ListView(this);
		// int width = Globals.getScreenSize(this).x / 2;

		// RelativeLayout.LayoutParams lp = new
		// RelativeLayout.LayoutParams(width,RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// lp.addRule(RelativeLayout.BELOW,R.id.llytHomeHeader);

		// listViewOptions.setLayoutParams(lp);
		// listViewOptions.setBackgroundResource(R.color.app_black);
		// listViewOptions.setPadding(2, 2, 2, 2);
		// listViewOptions.setVisibility(View.GONE);

		String[] values = new String[] { Globals.OPTION_SAVED_NEWS, Globals.OPTION_CALENDER, Globals.OPTION_SETTINGS,
				Globals.OPTION_REFRESH };

		ArrayList<StateListDrawable> listDrawable = getDrawableList();

		listOptions = new ArrayList<Object_Options>();

		for (int i = 0; i < values.length; i++) {

			Object_Options obj = new Object_Options();
			obj.setText(values[i]);
			if(listDrawable.size() > i)
				obj.setStateDrawable(listDrawable.get(i));
			//obj.setImageResourceId(imgIds[i]);

			listOptions.add(obj);
		}

		Custom_AdapterOptions adapter = new Custom_AdapterOptions(this,
				listOptions);
		listViewOptions.setAdapter(adapter);

		// RelativeLayout root =
		// (RelativeLayout)findViewById(R.id.rlytHomeRoot);
		// root.addView(listViewOptions);

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

			StateListDrawable stateSettings = new StateListDrawable();
			stateSettings.addState(new int[] {android.R.attr.state_pressed},
					getResources().getDrawable(R.drawable.settings_selected));
			stateSettings.addState(new int[] {android.R.attr.state_focused},
					getResources().getDrawable(R.drawable.settings_selected));
			stateSettings.addState(new int[] { },
					getResources().getDrawable(Custom_ThemeUtil.getSettingsImageId(this)));

			StateListDrawable stateCalender = new StateListDrawable();
			stateCalender.addState(new int[] {android.R.attr.state_pressed},
					getResources().getDrawable(R.drawable.calender_selected));
			stateCalender.addState(new int[] {android.R.attr.state_focused},
					getResources().getDrawable(R.drawable.calender_selected));
			stateCalender.addState(new int[] { },
					getResources().getDrawable(Custom_ThemeUtil.getCalenderImageId(this)));

			StateListDrawable stateRefresh = new StateListDrawable();
			stateRefresh.addState(new int[] {android.R.attr.state_pressed},
					getResources().getDrawable(R.drawable.refresh_selected));
			stateRefresh.addState(new int[] {android.R.attr.state_focused},
					getResources().getDrawable(R.drawable.refresh_selected));
			stateRefresh.addState(new int[] { },
					getResources().getDrawable(Custom_ThemeUtil.getRefreshImageId(this)));


			//int[] imgIds = new int[] { R.drawable.selector_options_save,
			//R.drawable.selector_options_calender,
			//R.drawable.selector_options_settings, R.drawable.selector_options_refresh };


			listDrawable.add(stateSave);
			listDrawable.add(stateCalender);
			listDrawable.add(stateSettings);
			listDrawable.add(stateRefresh);
		}catch(Exception ex){

		}
		return listDrawable;
	}
	public void toggleOptionsVisibility(View v) {

		LinearLayout parent = (LinearLayout) findViewById(R.id.llytOptionsContainer);
		if (listViewOptions.getVisibility() == View.VISIBLE) {
			// Its visible
			parent.setVisibility(View.GONE);
			listViewOptions.setVisibility(View.INVISIBLE);
		} else {
			// Either gone or invisible
			initOptionsList();
			parent.setVisibility(View.VISIBLE);
			listViewOptions.setVisibility(View.VISIBLE);
		}

	}

	private void optionSelected(int pos) {
		toggleOptionsVisibility(null);

		if (listOptions != null && listOptions.size() > pos) {
			Object_Options obj = listOptions.get(pos);
			if (obj != null) {

				Class<?> nextClass = null;

				if(obj.getText().equals(Globals.OPTION_SAVED_NEWS)){
					nextClass = Activity_SavedNews.class;
				}else if(obj.getText().equals(Globals.OPTION_REFRESH)){
					serverCallForCategoriesAndNews();
				}
				else if(obj.getText().equals(Globals.OPTION_SETTINGS)){
					nextClass = Activity_Settings.class;
				}
				else if(obj.getText().equals(Globals.OPTION_CALENDER)){
					nextClass = Activity_SelectDateRange.class;
				}
				if (nextClass != null) {
					Intent intent = new Intent(this, nextClass);
					startActivity(intent);
				}
			}
		}
	}

	private void showLoadingScreen() {

		LinearLayout ll = (LinearLayout) findViewById(R.id.llytLoadingContainer);

		if (ll != null ) {//&& bar != null
			Log.i("HARSH", "Showing Spinner News");
			//bar.setVisibility(View.VISIBLE);
			ll.setVisibility(View.VISIBLE);
		}
	}


	private Boolean isShowingLoadingScreen(){
		LinearLayout ll = (LinearLayout) findViewById(R.id.llytLoadingContainer);

		if (ll != null ) {

			if(ll.getVisibility() == View.VISIBLE)
				return true;
		}

		return false;
	}

	private  void hideLoadingScreen() {

		LinearLayout ll = (LinearLayout) findViewById(R.id.llytLoadingContainer);

		if (ll != null ) {
			ll.setVisibility(View.GONE);
		}

		if(slidingMenu != null)
			slidingMenu.setSlidingEnabled(true);
	}


	public void onClickSpinnerView(View v){
		Toast.makeText(this, "Please wait ... ", Toast.LENGTH_SHORT).show();
	}

}
