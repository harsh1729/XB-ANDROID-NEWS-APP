package com.xercesblue.newspaperapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class Custom_JsonParserCategory {
	
	private String jsonString;
	private ArrayList<Object_Category> listCategories;
	Context context;
	
	public Custom_JsonParserCategory(String jsonString,Context context) {
		this.jsonString = jsonString;
		this.context = context;
	}
	
	public ArrayList<Object_Category> getCategoriesFromJson()
	{
		Log.i("HARSH","In GEtParsedJson");
		if(jsonString != null)
		{
			listCategories = new ArrayList<Object_Category>();
			try
			{
				JSONObject  mainObject= new JSONObject(jsonString);
				
				if(!mainObject.has("categories")){
					return listCategories;
				}
				int i;
				JSONArray Cat_Object_Array = mainObject.getJSONArray("categories");
				for(i=0; i<Cat_Object_Array.length(); i++)
				{
					Object_Category ob = new Object_Category();
					JSONObject Single_Cat = Cat_Object_Array.getJSONObject(i);
					ob.setId(Single_Cat.getInt("id"));
					ob.setName(Single_Cat.getString("name").trim());
					ob.setImageName(Single_Cat.getString("image").trim());
					
					//byte[] image = getBitmapFromURL(ob.getImageURL());
					try{
					if(!ob.getImageName().equals("")){
						Log.i("HARSH","Fething Picasso for "+ob.getName());
						Picasso p = Picasso.with(this.context);
					    RequestCreator rq = null;
					    
					    rq=p.load(ob.getImageName());
					    
					    //rq.placeholder(R.drawable.selector_options_search);
					    
					    rq.resize(100,100);
					    rq.centerCrop();
					    ob.setImage(rq.get());
					}
					}catch(Exception ex){
						Log.i("HARSH","Exception Fetching Picasso for "+ob.getName());
					}
					    //ob.setImage(image);
					
					Log.i("HARSH","name"+ob.getName());
					
					ob.setParentId(Single_Cat.getInt("parent_id"));
					listCategories.add(ob);
				}
				
				
			}
			catch(Exception e)
			{
				Log.i("HARSH","error getPArsed JSON -- "+e.getMessage() +"\n"+e.getStackTrace());
				e.printStackTrace();
			}
		}
		return listCategories;
	}
	
	/*
	public static byte[] getBitmapFromURL(String link) 
	{
		  
		Log.i("HARSH","getBitmapFromURL called: "+link);
		   try 
		   {
			   if(link.trim().equalsIgnoreCase("")){
				   return null;
			   }
			   DefaultHttpClient mHttpClient = new DefaultHttpClient();
			   HttpGet mHttpGet = new HttpGet(link);
			   Log.i("HARSH","Downloading image from : "+link);
			   HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
			   if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			   {
				   HttpEntity entity = mHttpResponse.getEntity();
			       if ( entity != null) 
			       {
			    	   Log.i("HARSH","Returning image: "+link);
			    	   byte[] imageData = EntityUtils.toByteArray(entity);	// returns blob data , pass it to query
			    	   
			    	   if(imageData != null){
			    		   Log.i("HARSH","imageData length "+imageData.length);
			    		   return imageData;
			    	   }
			    	   
			    	   return null;
			       }
			   }
		   }
		   catch (Exception e) 
		   {
			   Log.i("HARSH","Exception in fetcing"+link +" --"+e.getMessage() +"\n"+e.getStackTrace());
		   }
		return null;
	}
*/

}
