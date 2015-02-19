package com.xercesblue.newspaperapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



public class Custom_JsonParserNews {

	private String jsonString;
	private ArrayList<Object_ListItem_MainNews> listNews;

	public Custom_JsonParserNews(String jsonString) {
		this.jsonString = jsonString;
	}
	public ArrayList<Object_ListItem_MainNews> getParsedJson()
	{

		if(jsonString != null && !jsonString.trim().equals(""))
		{
			listNews = new ArrayList<Object_ListItem_MainNews>();
			try 
			{
				JSONArray mainArray = new JSONArray(jsonString);
				if(mainArray != null)
				for(int i=0;i<mainArray.length();i++)
				{
					Object_ListItem_MainNews objMainNews = new Object_ListItem_MainNews();

					JSONObject jsonObjNews = mainArray.getJSONObject(i);
					

					int news_id = -1;
					
					if(jsonObjNews.has("news_id")){
						news_id = jsonObjNews.getInt("news_id");
						objMainNews.setId(news_id);
					}
					if(jsonObjNews.has("cat_id")){
						objMainNews.setCatId(jsonObjNews.getInt("cat_id"));
					}
					if(jsonObjNews.has("heading")){
						objMainNews.setHeading(jsonObjNews.getString("heading").trim());
					}
					if(jsonObjNews.has("content")){
						objMainNews.setContent(jsonObjNews.getString("content").trim());
					}
					if(jsonObjNews.has("image")){
						objMainNews.setImage(jsonObjNews.getString("image").trim());
					}
					if(jsonObjNews.has("date")){
						objMainNews.setDate(jsonObjNews.getString("date").trim());
					}
					if(jsonObjNews.has("tagline")){
						objMainNews.setImageTagline(jsonObjNews.getString("tagline").trim());
					}
					if(jsonObjNews.has("share_link")){
						objMainNews.setShareLink(jsonObjNews.getString("share_link").trim());
					}
					if(jsonObjNews.has("video")){
						objMainNews.setVideo(jsonObjNews.getString("video").trim());
					}
					
					
					
					if(jsonObjNews.has("linked_news") && news_id > 0){
					JSONArray subNewsArray = jsonObjNews.getJSONArray("linked_news");
					ArrayList<Object_SubNewsItem> listSubNews = new ArrayList<Object_SubNewsItem>();
					
					for(int j=0;j<subNewsArray.length();j++)
					{
						Object_SubNewsItem objSubNews = new Object_SubNewsItem();

						JSONObject objSubNewsJSON = subNewsArray.getJSONObject(j);
						
						if(objSubNewsJSON.has("news_id")){
							objSubNews.setNewsId(objSubNewsJSON.getInt("news_id"));
						}
						objSubNews.setNewsParentId(news_id);
						
						if(objSubNewsJSON.has("heading")){
							objSubNews.setNewsHeading(objSubNewsJSON.getString("heading").trim());
						}
						if(objSubNewsJSON.has("content")){
							objSubNews.setNewsContent(objSubNewsJSON.getString("content").trim());
						}
						if(objSubNewsJSON.has("image")){
							objSubNews.setNewsImage(objSubNewsJSON.getString("image").trim());
						}
						if(objSubNewsJSON.has("tagline")){
							objSubNews.setNewsImageTagline(objSubNewsJSON.getString("tagline").trim());
						}
						if(objSubNewsJSON.has("video")){
							objSubNews.setNewsVideo(objSubNewsJSON.getString("video").trim());
						}
						
						listSubNews.add(objSubNews);
					}

					objMainNews.setListSubNews(listSubNews);
					}
					listNews.add(objMainNews);
				} 
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}
		return listNews;
	}
}
