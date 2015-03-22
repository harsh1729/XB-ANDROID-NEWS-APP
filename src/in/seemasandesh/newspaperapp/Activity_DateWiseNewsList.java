package in.seemasandesh.newspaperapp;

import java.util.ArrayList;

import org.json.JSONArray;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_DateWiseNewsList extends Activity_Parent {

	
	private PullToRefreshListView listViewNews;
	private Custom_AdapterNewsList news_adaptor;
	
	public int currentCategoryId;
	private ArrayList<Interface_ListItem> listdata;
	public ProgressDialog mDialog;
	
	String startDate ="";
	String endDate ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_wise_news_list);
		
		initSuper();
		init_DateWiseNews();
	}
	
	private void init_DateWiseNews(){
		
		if (getIntent().hasExtra("catId"))
			currentCategoryId = getIntent().getIntExtra("catId", 0);
		else 
			Globals.showAlertDialogError(this,
					"Error Occured, Please try again");
		
		if (getIntent().hasExtra("startDate"))
			startDate = getIntent().getStringExtra("startDate");
		
		if (getIntent().hasExtra("endDate"))
			endDate = getIntent().getStringExtra("endDate");
		
		listViewNews = (PullToRefreshListView) findViewById(R.id.lst_cat_wise_news);
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

		getNewsDataFromServer(currentCategoryId, Globals.CALLTYPE_FRESH, 0, false, Globals.FINAL_NEWS_LIMIT_FIRST_CALL);
	}
	
	
	
	private void navigateToNewsDetail(int pos) {
		//

		if (getListData().size() >= pos) {

			if(getListData().get(pos - 1).getClass() == Object_ListItem_MainNews.class){
				Intent i = new Intent(getApplicationContext(),
						Activity_NewsDetail.class);

				i.putExtra("newsId", getListData().get(pos - 1).getId());
				i.putExtra("navFrom", Activity_NewsDetail.NAV_FROM_DATE_WISE);
				
				startActivity(i);
			}

		}

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
	public ArrayList<Interface_ListItem> getListData() {

		if (listdata == null) {
			listdata = new ArrayList<Interface_ListItem>();
		}

		return listdata;
	}
	
	public void getNewsDataFromServer(final int catId, final String callType,
			int lastNewsId, final Boolean isPullToRefresh , int limit) 
	{

		try{
			
			if (!isPullToRefresh)
				listViewNews.setAdapter(null);
			
			Custom_ConnectionDetector cd = new Custom_ConnectionDetector(
					getApplicationContext());

			if (!cd.isConnectingToInternet()) {
				if (!isPullToRefresh) {
					DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed
							dialog.cancel();
							Activity_DateWiseNewsList.this.finish();
						}
					};
					
					Globals.showAlertDialogOneButton(
							Globals.TEXT_NO_INTERNET_HEADING,
							"Please check your network connection.",
							Activity_DateWiseNewsList.this, "OK", listener, false);

				} else {
					Toast.makeText(
							Activity_DateWiseNewsList.this,
							Globals.TEXT_NO_INTERNET_DETAIL_TOAST,
							Toast.LENGTH_SHORT).show();
					listViewNews.onRefreshComplete();

				}

				return;
			}

			Log.i("HARSH", "getNewsDataFromServer Request CatId = " + catId);
			if (!isPullToRefresh)
				mDialog = Globals.showLoadingDialog(mDialog, this,false);
			
			Custom_VolleyArrayRequest jsonObjectRQST = new Custom_VolleyArrayRequest(Request.Method.POST,
					Globals.getURL_NewsByCategory(), Globals.getParams_NewsByCategoryDateWise(catId, callType, lastNewsId, limit,startDate,endDate),
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
								DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// Write your code here to execute after dialog closed
										dialog.cancel();
										Activity_DateWiseNewsList.this.finish();
									}
								};
								

								Globals.showAlertDialogOneButton(
										"Error",
										"Some error has occured, please try again later !",
										Activity_DateWiseNewsList.this, "OK", listener, false);
								Globals.hideLoadingDialog(mDialog);
								//showNewsList(catId);
							} else {
								Toast.makeText(
										Activity_DateWiseNewsList.this,
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

		ArrayList<Object_ListItem_MainNews> listNewsItemServer;

		Log.i("DARSH", "getNewsDataFromServer onResponse" + response);

		Custom_JsonParserNews parserObject = new Custom_JsonParserNews(
				response.toString());
		listNewsItemServer = parserObject.getParsedJsonMainNews(catId);
		if (!isPullToRefresh) {
			Globals.hideLoadingDialog(mDialog);

		}
		if (listNewsItemServer == null || listNewsItemServer.size() == 0) {
			if (!isPullToRefresh) {
				DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog closed
						dialog.cancel();
						Activity_DateWiseNewsList.this.finish();
					}
				};
				Globals.showAlertDialogOneButton("News Not Found",
						"Please try after some time.", this, "OK", listener, false);
				

			}else{
				Toast.makeText(getApplicationContext(), "No more news", Toast.LENGTH_SHORT).show();
				listViewNews.onRefreshComplete();
			}
			
			return;
		}
		//Insert Into database
		DBHandler_MainNews dbH = new DBHandler_MainNews(getApplicationContext());
		dbH.insertNewsItemList(listNewsItemServer);
		
		getListData().addAll(listNewsItemServer);
		
		news_adaptor = new Custom_AdapterNewsList(Activity_DateWiseNewsList.this,R.layout.row_item_news_list,
				getListData());

		runOnUiThread(new Runnable() {
			public void run() {
				if (news_adaptor != null) {
					listViewNews.onRefreshComplete();
					listViewNews.setAdapter(news_adaptor);

				}

			}
		});

	}

	
}
