package in.seemasandesh.newspaperapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

public class Activity_NewsDetail extends SherlockFragmentActivity implements
		BaseSliderView.OnSliderClickListener {
	int newsId;
	private SliderLayout slider;

	// private String shareLink;
	private ListView listViewOptions;
	private ArrayList<Object_Options> listOptions;
	private Object_ListItem_MainNews currentNewsItem;
	private ArrayList<Object_SubNewsItem> listAllCurrentNewsItem;
	private Boolean firstTime = true;
	private ProgressDialog mDialog;
	private LinearLayout sliderContainer;

	// private boolean comingFromHomeScreen = false;

	public final static int NAV_FROM_HOME = 1;
	public final static int NAV_FROM_SAVED_NEWS = 2;
	public final static int NAV_FROM_DATE_WISE = 3;

	private int navFrom = NAV_FROM_HOME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		initNewsDetail();
	}

	@Override
	protected void onResume() {

		super.onResume();
		if (firstTime) {
			firstTime = false;
			if (navFrom == NAV_FROM_SAVED_NEWS) {
				showNewsDeatils();
			} else {
				getNewsDetail();
			}
		}
	}

	private void initNewsDetail() {
		Custom_ThemeUtil.onActivityCreateSetTheme(this);

		if (getIntent().hasExtra("newsId"))
			newsId = getIntent().getIntExtra("newsId", 0);
		else {
			Globals.showAlertDialogError(this,
					"Error Occured, Please try again");
		}

		if (getIntent().hasExtra("navFrom"))
			navFrom = getIntent().getIntExtra("navFrom", NAV_FROM_HOME);

		sliderContainer = (LinearLayout) findViewById(R.id.llytSliderContainer);

		ImageButton imgButtonBack = (ImageButton) (findViewById(R.id.imgHeaderBtnLeft));
		ImageButton imgButtonOptions = (ImageButton) (findViewById(R.id.imgHeaderBtnRight));

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

	private void getNewsDetail() {
		try {
			mDialog = Globals.showLoadingDialog(mDialog, this, true);
			Custom_ConnectionDetector cd = new Custom_ConnectionDetector(
					getApplicationContext());

			if (!cd.isConnectingToInternet()) {

				Globals.showAlertDialogError(this,
						Globals.TEXT_NO_INTERNET_DETAIL_DIALOG_MAIN_SCREEN);
			}

			Custom_VolleyArrayRequest jsonObjectRQST = new Custom_VolleyArrayRequest(
					Request.Method.POST,
					Custom_URLs_Params.getURL_NewsDetail(),
					Custom_URLs_Params.getParams_NewsDetail(newsId),
					new Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							gotNewsDetailResponce(response);

						}

					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							err.printStackTrace();
							handleNetworkError();
						}
					});

			Custom_AppController.getInstance().addToRequestQueue(
					jsonObjectRQST);

		} catch (Exception ex) {
			handleNetworkError();
		}
	}

	private void handleNetworkError() {
		Globals.showAlertDialogOneButton(Globals.TEXT_CONNECTION_ERROR_HEADING,
				Globals.TEXT_CONNECTION_ERROR_DETAIL_DIALOG_MAIN_SCREEN, this,
				"OK", null, false);
		Globals.hideLoadingDialog(mDialog);
		showNewsDeatils();
	}

	private void gotNewsDetailResponce(JSONArray response) {

		ArrayList<Object_SubNewsItem> listNewsItemServer;
		Log.i("DARSH", "gotNewsDetailResponce onResponse" + response);
		Custom_JsonParserNews parserObject = new Custom_JsonParserNews();

		listNewsItemServer = parserObject
				.getParsedJsonSubNews(response, newsId);

		DBHandler_SubNews dbH = new DBHandler_SubNews(getApplicationContext());
		dbH.insertSubNewsItemList(listNewsItemServer);

		showNewsDeatils();
	}

	private void showNewsDeatils() {
		// mDialog = Globals.showLoadingDialog(mDialog, this, true);

		if (sliderContainer == null) {
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

	private void taskThread() {

		DBHandler_MainNews dbHMain = new DBHandler_MainNews(this);

		// HashMap<String, Object_ListItem_MainNews> items =
		// dbHMain.getNewsItemsWithId(newsId);

		currentNewsItem = dbHMain.getNewsItemWithId(newsId);
		// nextNewsItem = dbHMain.getNextNewsItem(newsId);
		// prevNewsItem = dbHMain.getPrevNewsItem(newsId);

		// currentNewsItem = items.get(Globals.NEWS_ITEM);
		// nextNewsItem = items.get(Globals.NEWS_ITEM_NEXT);
		// prevNewsItem = items.get(Globals.NEWS_ITEM_PREV);

		String parentHeading = "";
		if (currentNewsItem != null) {
			parentHeading = currentNewsItem.getHeading();
		}

		DBHandler_SubNews dbHSub = new DBHandler_SubNews(
				Activity_NewsDetail.this);
		listAllCurrentNewsItem = dbHSub
				.getAllSubNewsItem(newsId, parentHeading);
		// Object_AppConfig objConfig = new Object_AppConfig(this);

		if (currentNewsItem != null) {
			Object_SubNewsItem temp = new Object_SubNewsItem();
			temp.setNewsContent(currentNewsItem.getContent());
			temp.setNewsHeading(currentNewsItem.getHeading());
			temp.setNewsImagePath(currentNewsItem.getImagePath());
			temp.setNewsImageTagline(currentNewsItem.getImageTagline());
			temp.setNewsVideo(currentNewsItem.getVideo());

			listAllCurrentNewsItem.add(0, temp);

			// shareLink = currentNewsItem.getShareLink();
		}

		Log.i("HARSH", "Count of news item is " + listAllCurrentNewsItem.size());

		int itemNo = 0;

		if (listAllCurrentNewsItem.size() > 1) {
			
			
			
			for (Object_SubNewsItem item : listAllCurrentNewsItem) {
				final TextSliderView textSliderView = new TextSliderView(this);
				textSliderView.description(item.getNewsContent())
						.image(item.getNewsImagePath())
						.Heading(item.getNewsHeading())
						.setScaleType(BaseSliderView.ScaleType.CenterCrop)
						.setOnSliderClickListener(this);

				textSliderView.setImageTag(item.getNewsImageTagline());

				/*
				 * if (nextNewsItem != null) {
				 * textSliderView.setNextHeading(nextNewsItem.getHeading());
				 * textSliderView.setNextImageURL(objConfig
				 * .getNewsImagesFullPath() + nextNewsItem.getImage());
				 * nextNewsId = nextNewsItem.getId(); }
				 * 
				 * if (prevNewsItem != null) {
				 * textSliderView.setPrevHeading(prevNewsItem.getHeading());
				 * textSliderView.setPrevImageURL(objConfig
				 * .getNewsImagesFullPath() + prevNewsItem.getImage());
				 * prevNewsId = prevNewsItem.getId(); }
				 */
				// textSliderView.getBundle().putString("imgURL",objConfig.getNewsImagesFullPath()
				// + item.getNewsImage());

				textSliderView.getBundle().putInt("itemNo", itemNo);
				itemNo++;
				slider.addSlider(textSliderView);

			}
			this.runOnUiThread(new Runnable() {
				public void run() {
					slider.notifyDataSetChange();
					Globals.hideLoadingDialog(mDialog);
					sliderContainer.addView(slider);

					// list.add(nextNewsItem);
					// list.add(prevNewsItem);

					if (navFrom == NAV_FROM_HOME)
						createHorizontalNewsSlider();
				}
			});
		} else if (currentNewsItem != null) {
			this.runOnUiThread(new Runnable() {
				public void run() {
					addSingleNewsItem();
				}
			});
		}

	}

	private void addSingleNewsItem(){
		
		LayoutInflater layoutInflater = LayoutInflater.from(this);

		final View v = layoutInflater.inflate(R.layout.slider_type_text, null);

		int sliderDescHeight = (int) (24* this.getResources().getDisplayMetrics().density);
		LinearLayout llytSliderDesc = (LinearLayout) v
				.findViewById(R.id.description_layout);
		LinearLayout.LayoutParams lpDesc = (LayoutParams) llytSliderDesc.getLayoutParams();
		lpDesc.height = sliderDescHeight;
		llytSliderDesc.setLayoutParams(lpDesc);
		llytSliderDesc.setBackgroundResource(Custom_ThemeUtil.getThemeColorId(this));
		
		int imgMainHeight = (int) ((int) Globals.getScreenSize(this).y / 2.5);
		
		ImageView imgView = (ImageView) v
				.findViewById(R.id.imgNewsDetailBig);
		
		
		if (setImage(imgView, currentNewsItem.getImagePath())) {
			RelativeLayout.LayoutParams lpImg = new RelativeLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, imgMainHeight);
			imgView.setLayoutParams(lpImg);
			imgView.setClickable(true);
			imgView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					showZoomedImage(0);
				}
			});
			
		} else {
			imgView.setVisibility(View.GONE);
		}
		
		
		TextView txtNewsDesc = (TextView) v
				.findViewById(R.id.txtNewsDescription);
		txtNewsDesc.setText( currentNewsItem.getContentSpan());
		txtNewsDesc.setTextSize(Globals.getAppFontSize_Normal(this));

		TextView txtHeading = (TextView) v
				.findViewById(R.id.txtNewsHeading);
		txtHeading.setText(currentNewsItem.getHeadingSpan());
		txtHeading.setTextSize(Globals.getAppFontSize_Large(this));

		TextView txtImagetag = (TextView) v
				.findViewById(R.id.txtNewsImageTag);
		if (currentNewsItem.getImageTagline() != null
				&& !currentNewsItem.getImageTagline().isEmpty()) {
			txtImagetag.setText(currentNewsItem.getImageTagline());
		} else {
			txtImagetag.setVisibility(View.GONE);
		}
				
		Globals.hideLoadingDialog(mDialog);
		sliderContainer.addView(v);
		
		if (navFrom == NAV_FROM_HOME)
			createHorizontalNewsSlider();
			
	}
	private void createHorizontalNewsSlider() {
		LinearLayout container = (LinearLayout) findViewById(R.id.llytscrollNewsContainer);
		container.removeAllViews();
		HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.scrollViewHorizontalNews);
		scrollView.scrollTo(0, 0);

		if (currentNewsItem == null) {
			return;
		}
		List<Object_ListItem_MainNews> list = null;
		// list.add(currentNewsItem);

		DBHandler_MainNews dbH = new DBHandler_MainNews(this);
		list = dbH.getFellowCategoryNewsItemsForId(currentNewsItem.getId(),
				currentNewsItem.getCatId());

		if (list.size() == 0) {
			return;
		} else if (list.size() < 3) {
			HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			container.setLayoutParams(params);
		}

		// Object_AppConfig objConfig = new Object_AppConfig(this);
		Point pnt = Globals.getScreenSize(this);

		// int containerHeight = pnt.y;
		int containerWidth = pnt.x;
		// Toast.makeText(this, "List count is "+list.size(),
		// Toast.LENGTH_LONG).show();
		for (Object_ListItem_MainNews obj : list) {

			try {
				LayoutInflater inflater = LayoutInflater.from(this);
				View rowItem = inflater
						.inflate(R.layout.row_item_news_horizontal_scroller,
								null, false);
				LinearLayout.LayoutParams lp = new LayoutParams(
						containerWidth / 3,
						LinearLayout.LayoutParams.MATCH_PARENT);

				rowItem.setLayoutParams(lp);

				int imgNextPrevHeightWidth = (int) (containerWidth / 6.5);

				TextView txtNextHeading = (TextView) rowItem
						.findViewById(R.id.txtHeading);
				txtNextHeading.setText(obj.getHeadingSpan());

				ImageView imgView = (ImageView) rowItem
						.findViewById(R.id.imgNews);

				if (setImage(imgView, obj.getImagePath())) {
					LinearLayout.LayoutParams lpImgPrev = (LayoutParams) imgView
							.getLayoutParams();
					lpImgPrev.height = imgNextPrevHeightWidth;
					lpImgPrev.width = imgNextPrevHeightWidth;
					imgView.setLayoutParams(lpImgPrev);
					
				} else {
					imgView.setVisibility(View.GONE);
				}

				TextView txtHiddenNewsId = (TextView) rowItem
						.findViewById(R.id.txtHiddenNewsId);
				txtHiddenNewsId.setText(obj.getId() + "");

				container.addView(rowItem);
			} catch (Exception e) {

				Log.i("HARSH", "EXCEPTION IN ADD NEWS ROW");
			}

		}

	}

	protected boolean setImage(ImageView targetImageView, String mUrl) {

		if (mUrl != null && !mUrl.isEmpty()) {

			Globals.loadImageIntoImageView(targetImageView, mUrl, this,
					R.drawable.loading_image_large, R.drawable.no_image_large);

		

			return true;
		}

		return false;
	}

	public void onBackClick(View v) {
		finish();
	}

	public void onClickNews(View v) {

		try {

			TextView txtHiddenNewsId = (TextView) v
					.findViewById(R.id.txtHiddenNewsId);
			int nextNewsId = Integer.parseInt(txtHiddenNewsId.getText()
					.toString());
			if (nextNewsId >= 0) {
				newsId = nextNewsId;
				getNewsDetail();
			} else {
				Toast.makeText(this, "Some error occured ", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception ex) {

			Toast.makeText(this, "Some error occured Please try again ",
					Toast.LENGTH_SHORT).show();
		}

	}

	/*
	 * public void onClickNext(View v) { if (nextNewsId >= 0) {
	 * 
	 * newsId = nextNewsId; showNewsDeatils(); }
	 * 
	 * }
	 * 
	 * public void onClickPrev(View v) { if (prevNewsId >= 0) {
	 * 
	 * newsId = prevNewsId; showNewsDeatils(); } }
	 */

	@Override
	public void onSliderClick(BaseSliderView slider) {

		// String url = (String) slider.getBundle().get("imgURL");
		int itemNo = slider.getBundle().getInt("itemNo");

		if (slider.isImageload) {
			showZoomedImage(itemNo);
		}
	}
	
	
	private void showZoomedImage(int itemNo){
		Intent i = new Intent(this, Activity_ZoomedImage.class);
		// i.putExtra("Url", url);
		i.putExtra("newsId", newsId);
		i.putExtra("itemNo", itemNo);
		startActivity(i);
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

		// String[] values = new String[] { "Save News", "Share News" };
		// int[] imgIds = new int[] {
		// R.drawable.selector_options_save,R.drawable.selector_options_share };

		String[] values = new String[] { Globals.OPTION_SAVE,
				Globals.OPTION_SHARE };// , Globals.OPTION_SETTINGS };
		ArrayList<StateListDrawable> listDrawable = getDrawableList();

		listOptions = new ArrayList<Object_Options>();

		for (int i = 0; i < values.length; i++) {

			Object_Options obj = new Object_Options();
			obj.setText(values[i]);
			// obj.setImageResourceId(imgIds[i]);
			if (listDrawable.size() > i)
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

	private ArrayList<StateListDrawable> getDrawableList() {
		ArrayList<StateListDrawable> listDrawable = new ArrayList<StateListDrawable>();

		try {

			StateListDrawable stateSave = new StateListDrawable();
			stateSave.addState(new int[] { android.R.attr.state_pressed },
					getResources().getDrawable(R.drawable.save_selected));
			stateSave.addState(new int[] { android.R.attr.state_focused },
					getResources().getDrawable(R.drawable.save_selected));
			stateSave.addState(
					new int[] {},
					getResources().getDrawable(
							Custom_ThemeUtil.getSaveImageId(this)));

			StateListDrawable stateShare = new StateListDrawable();
			stateShare.addState(new int[] { android.R.attr.state_pressed },
					getResources().getDrawable(R.drawable.share_selected));
			stateShare.addState(new int[] { android.R.attr.state_focused },
					getResources().getDrawable(R.drawable.share_selected));
			stateShare.addState(
					new int[] {},
					getResources().getDrawable(
							Custom_ThemeUtil.getShareImageId(this)));

			StateListDrawable stateSettings = new StateListDrawable();
			stateSettings.addState(new int[] { android.R.attr.state_pressed },
					getResources().getDrawable(R.drawable.settings_selected));
			stateSettings.addState(new int[] { android.R.attr.state_focused },
					getResources().getDrawable(R.drawable.settings_selected));
			stateSettings.addState(
					new int[] {},
					getResources().getDrawable(
							Custom_ThemeUtil.getSettingsImageId(this)));

			listDrawable.add(stateSave);
			listDrawable.add(stateShare);
			listDrawable.add(stateSettings);
		} catch (Exception ex) {

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
		// toggleOptionsVisibility(null);

		if (listOptions != null && listOptions.size() > pos) {
			Object_Options obj = listOptions.get(pos);
			if (obj != null) {

				Class<?> nextClass = null;

				if (obj.getText().equals(Globals.OPTION_SAVE)) {
					saveNews();

				} else if (obj.getText().equals(Globals.OPTION_SHARE)) {
					shareNews(pos);
				} else if (obj.getText().equals(Globals.OPTION_SETTINGS)) {

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

		Toast.makeText(this, "News saved successfully", Toast.LENGTH_SHORT)
				.show();
	}

	/*
	 * Selective Share
	 * 
	 * private void shareNews(int pos) {
	 * 
	 * if (currentNewsItem != null) {
	 * 
	 * // Intent sendIntent = new Intent();
	 * sendIntent.setAction(Intent.ACTION_SEND);
	 * sendIntent.putExtra(Intent.EXTRA_TEXT, currentNewsItem.getHeading() +
	 * "\n\n" + currentNewsItem.getContent() + " \n\nCourtesy: XB NEWS ");
	 * sendIntent.setPackage("com.whatsapp");
	 * sendIntent.setPackage("com.twitter.android");
	 * sendIntent.setPackage("com.facebook.katana");
	 * sendIntent.setType("text/plain"); startActivity(sendIntent); //
	 * 
	 * List<Intent> targetShareIntents=new ArrayList<Intent>(); Intent
	 * shareIntent=new Intent(); shareIntent.setAction(Intent.ACTION_SEND);
	 * shareIntent.setType("text/plain"); List<ResolveInfo>
	 * resInfos=getPackageManager().queryIntentActivities(shareIntent, 0);
	 * if(!resInfos.isEmpty()){ System.out.println("Have package");
	 * for(ResolveInfo resInfo : resInfos){ String
	 * packageName=resInfo.activityInfo.packageName; Log.i("Package Name",
	 * packageName); if(packageName.contains("com.twitter.android") ||
	 * packageName.contains("android.gm")||
	 * packageName.contains("android.email")|| packageName.contains("mms") ||
	 * packageName.contains("com.facebook.katana") ||
	 * packageName.contains("com.whatsapp")){ Intent intent=new Intent();
	 * intent.setComponent(new ComponentName(packageName,
	 * resInfo.activityInfo.name)); intent.setAction(Intent.ACTION_SEND);
	 * intent.setType("text/plain");
	 * intent.putExtra(Intent.EXTRA_SUBJECT,currentNewsItem.getHeading()+"\n\n"
	 * ); if(currentNewsItem.getShareLink() != null &&
	 * !currentNewsItem.getShareLink().trim().equals("")){
	 * intent.putExtra(Intent.EXTRA_TEXT,
	 * currentNewsItem.getContent()+"\n\nCourtesy: XB NEWS \n"
	 * +currentNewsItem.getShareLink()); }else{
	 * intent.putExtra(Intent.EXTRA_TEXT,
	 * currentNewsItem.getContent()+"\n\nCourtesy: XB NEWS \n"
	 * +Globals.DEFAULT_APP_WEBSITE); }
	 * 
	 * 
	 * 
	 * intent.setPackage(packageName); targetShareIntents.add(intent); } }
	 * if(!targetShareIntents.isEmpty()){ System.out.println("Have Intent");
	 * Intent chooserIntent=Intent.createChooser(targetShareIntents.remove(0),
	 * "Choose app to share news");
	 * chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
	 * targetShareIntents.toArray(new Parcelable[]{}));
	 * startActivity(chooserIntent); }else{ Log.i("HARSH","Do not Have Intent");
	 * } } }
	 * 
	 * }
	 */

	@SuppressLint("NewApi")
	private void shareNews(int pos) {

		if (currentNewsItem != null) {

			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			if (android.os.Build.VERSION.SDK_INT >= 13) 
			{
				sendIntent.putExtra(Intent.EXTRA_SUBJECT,
						currentNewsItem.getHeading()  + "\n\n");
			}else{
				sendIntent.putExtra(Intent.EXTRA_SUBJECT,
						currentNewsItem.getHeading()  + "\n\n");
			}
			
			if (currentNewsItem.getShareLink() != null
					&& !currentNewsItem.getShareLink().trim().equals("")) {
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						// currentNewsItem.getContent()
						"Read more @\n"
								+ currentNewsItem.getShareLink()
								+ "\nvia "
								+ getResources().getString(
										R.string.news_paper_name));
			} else {
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						"Read more @\n"
								+ getResources().getString(
										R.string.txt_company_website)+"/detail/"+currentNewsItem.getId()
								+ "\nvia "
								+ getResources().getString(
										R.string.news_paper_name));
			}
			// sendIntent.setPackage("com.whatsapp");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
		}
	}

}
