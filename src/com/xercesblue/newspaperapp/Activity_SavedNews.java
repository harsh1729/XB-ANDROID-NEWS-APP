package com.xercesblue.newspaperapp;

import java.util.ArrayList;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Activity_SavedNews extends Activity_Parent {

	private ArrayList<Interface_ListItem> listdata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_saved_news);
		super.initSuper();
		initSavedNews();		
	}
	
	private void initSavedNews() {

		ListView listViewNews = (ListView) findViewById(R.id.listSavedNews);
		listViewNews.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				navigateToNewsDetail(position);
			}
		});
		
		
		DBHandler_MainNews db = new DBHandler_MainNews(getApplicationContext());		
		ArrayList<Object_ListItem_MainNews> listSavedNewsdata = db.getSavedNews();
		
		if(listSavedNewsdata.size() > 0){
			//Object_ListItem_NewsCategory catObj = new Object_ListItem_NewsCategory();
			//catObj.setCatName("Saved News");
			
			//getListData().add(catObj);
			
			for (Interface_ListItem object : listSavedNewsdata) {
				getListData().add(object);
			}
		}
		
		if (getListData().size() > 0) {
			Custom_AdapterNewsList news_adaptor = new Custom_AdapterNewsList(this,R.layout.row_item_news_list,
					getListData());
			listViewNews.setAdapter(news_adaptor);
		}else{
			Globals.showAlertDialogOneButton("Alert", "No saved news found !", this, "OK", null, false);
		}

	}
	
	public ArrayList<Interface_ListItem> getListData() {

		if (listdata == null) {
			listdata = new ArrayList<Interface_ListItem>();
		}

		return listdata;
	}
	private void navigateToNewsDetail(int pos) {
		
		if (getListData().size() > pos) {
			if(getListData().get(pos).getClass() == Object_ListItem_MainNews.class){

			Intent i = new Intent(getApplicationContext(),
					Activity_NewsDetail.class);
			
			i.putExtra("newsId", getListData().get(pos).getId());
			
			startActivity(i);
			}
		}

	}
}
