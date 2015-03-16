package com.xercesblue.newspaperapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_MainNews extends SQLiteOpenHelper {

	// CREATE TABLE MainNewsData (NewsID INTEGER NOT NULL PRIMARY KEY,Heading
	// TEXT NOT NULL,
	// Content TEXT,CatId INTEGER NOT NULL,"Date" TEXT NOT NULL,Image TEXT NOT
	// NULL,
	// Video text,ShareLink text,ReporterId integer,ImageTagline text)

	// CREATE TABLE SavedNews (Id integer NOT NULL PRIMARY
	// KEY AUTOINCREMENT UNIQUE,NewsId text)

	private final String TABLE_NEWS = "MainNewsData";
	private final String KEY_NEWS_ID = "NewsID";
	private final String KEY_NEWS_HEADING = "Heading";
	private final String KEY_NEWS_CONTENT = "Content";
	private final String KEY_NEWS_CAT_ID = "CatId";
	private final String KEY_NEWS_DATE = "Date";
	private final String KEY_NEWS_IMAGE = "Image";
	private final String KEY_NEWS_VIDEO = "Video";
	private final String KEY_NEWS_SHARE_LINK = "ShareLink";
	private final String KEY_NEWS_IMAGE_TAGLINE = "ImageTagline";

	private final String TABLE_SAVED_NEWS = "SavedNews";
	private final String KEY_SAVED_NEWS_ID = "Id";
	private final String KEY_SAVED_NEWS_NEWS_ID = "NewsId";
	Context context;

	public DBHandler_MainNews(Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public ArrayList<Object_ListItem_MainNews> getMainNewsByCategory(int catId) {

		ArrayList<Object_ListItem_MainNews> list = new ArrayList<Object_ListItem_MainNews>();

		String selectQuary = " SELECT * FROM " + TABLE_NEWS + " WHERE "
				+ KEY_NEWS_CAT_ID + " = " + catId + " ORDER BY " + KEY_NEWS_ID
				+ " DESC";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery(selectQuary, null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					Object_ListItem_MainNews objNews = getNewsItem(cur);
					list.add(objNews);

				} while (cur.moveToNext());
			}

		}
		db.close();
		return list;
	}

	/*
	private Object_ListItem_MainNews getPrevNewsItem(int newsId ,SQLiteDatabase db) {
		String query = "SELECT * FROM " + TABLE_NEWS + " WHERE "
				+ KEY_NEWS_CAT_ID + " IN (SELECT " + KEY_NEWS_CAT_ID + " FROM "
				+ TABLE_NEWS + " WHERE " + KEY_NEWS_ID + " =" + newsId + ") ";
		query += " AND " + KEY_NEWS_ID + " > " + newsId + " ORDER BY "
				+ KEY_NEWS_ID + " ASC LIMIT 1";

		Cursor cur = db.rawQuery(query, null);
		Object_ListItem_MainNews objNews = null;
		if (cur != null) {
			if (cur.moveToFirst()) {
				objNews = getNewsItem(cur);
			}
		}
		return objNews;
	}

	private Object_ListItem_MainNews getNextNewsItem(int newsId,SQLiteDatabase db) {
		String query = "SELECT * FROM " + TABLE_NEWS + " WHERE "
				+ KEY_NEWS_CAT_ID + " IN (SELECT " + KEY_NEWS_CAT_ID + " FROM "
				+ TABLE_NEWS + " WHERE " + KEY_NEWS_ID + " =" + newsId + ") ";
		query += " AND " + KEY_NEWS_ID + " < " + newsId + " ORDER BY "
				+ KEY_NEWS_ID + " DESC LIMIT 1";
		Cursor cur = db.rawQuery(query, null);
		Object_ListItem_MainNews objNews = null;
		if (cur != null) {
			if (cur.moveToFirst()) {
				objNews = getNewsItem(cur);
			}
		}
		return objNews;
	}
	 */
	public Object_ListItem_MainNews getNewsItemWithId(int newsId) {
		String query = "SELECT * FROM " + TABLE_NEWS + " WHERE " + KEY_NEWS_ID
				+ " = " + newsId;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery(query, null);
		Object_ListItem_MainNews objNews = null;
		if (cur != null) {
			if (cur.moveToFirst()) {
				objNews = getNewsItem(cur);
			}
		}
		db.close();
		return objNews;
	}



	public ArrayList<Object_ListItem_MainNews> getFellowCategoryNewsItemsForId(int newsId, int catId){

		ArrayList<Object_ListItem_MainNews> list = new ArrayList<Object_ListItem_MainNews>();
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = " SELECT * FROM " + TABLE_NEWS +" WHERE " + KEY_NEWS_ID
				+ " <> " + newsId + " AND "+KEY_NEWS_CAT_ID +" = "+catId + " ORDER BY "+KEY_NEWS_ID + " DESC"; 

		Cursor cur = db.rawQuery(selectQuery, null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					Object_ListItem_MainNews objNews = getNewsItem(cur);
					list.add(objNews);

				} while (cur.moveToNext());
			}

		}
		db.close();
		return list;
	}

	/*
	public HashMap<String,Object_ListItem_MainNews> getNewsItemsWithId(int newsId){

		HashMap<String,Object_ListItem_MainNews> map = new HashMap<String, Object_ListItem_MainNews>();
		SQLiteDatabase db = this.getReadableDatabase();

		map.put(Globals.NEWS_ITEM, getNewsItemWithId(newsId, db));
		map.put(Globals.NEWS_ITEM_NEXT, getNextNewsItem(newsId, db));
		map.put(Globals.NEWS_ITEM_PREV, getPrevNewsItem(newsId, db));

		db.close();
		return map;
	}
	 */
	private Object_ListItem_MainNews getNewsItem(Cursor cur) {

		Object_ListItem_MainNews objNews = new Object_ListItem_MainNews();
		objNews.setId(cur.getInt(cur.getColumnIndex(KEY_NEWS_ID)));
		objNews.setCatId(cur.getInt(cur.getColumnIndex(KEY_NEWS_CAT_ID)));
		objNews.setHeading(cur.getString(cur
				.getColumnIndex(KEY_NEWS_HEADING)));
		objNews.setImagePath(cur.getString(cur.getColumnIndex(KEY_NEWS_IMAGE)));
		objNews.setContent(cur.getString(cur
				.getColumnIndex(KEY_NEWS_CONTENT)));
		objNews.setDate(cur.getString(cur.getColumnIndex(KEY_NEWS_DATE)));
		objNews.setVideo(cur.getString(cur.getColumnIndex(KEY_NEWS_VIDEO)));
		objNews.setShareLink(cur.getString(cur
				.getColumnIndex(KEY_NEWS_SHARE_LINK)));
		objNews.setImageTagline(cur.getString(cur
				.getColumnIndex(KEY_NEWS_IMAGE_TAGLINE)));

		return objNews;
	}

	public void insertNewsItemList(ArrayList<Object_ListItem_MainNews> list) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		for (Object_ListItem_MainNews obMain : list) {

			try {
				values.put(KEY_NEWS_CONTENT, obMain.getContent());
				values.put(KEY_NEWS_DATE, obMain.getDate());
				values.put(KEY_NEWS_HEADING, obMain.getHeading());
				values.put(KEY_NEWS_IMAGE, obMain.getImagePath());
				values.put(KEY_NEWS_CAT_ID, obMain.getCatId());
				values.put(KEY_NEWS_ID, obMain.getId());
				values.put(KEY_NEWS_VIDEO, obMain.getVideo());
				values.put(KEY_NEWS_SHARE_LINK, obMain.getShareLink());
				values.put(KEY_NEWS_IMAGE_TAGLINE, obMain.getImageTagline());

				db.insert(TABLE_NEWS, null, values);
				Log.i("DARSH", "Inserting News");
				if (!obMain.isListSubNewsNull()) {
					DBHandler_SubNews dbHChild = new DBHandler_SubNews(context);
					dbHChild.insertSubNewsItemList(obMain.getListSubNews(), db);

				}
			}
			catch (Exception ex) {
				Log.i("HARSH", "Exception in inserting main news");
			}
		}

	db.close();

}

public void clearNewsTable(int catID) {
	SQLiteDatabase db = this.getWritableDatabase();
	System.out.println("empty table called");
	String queryPostFix = " FROM " + TABLE_NEWS + " WHERE "
			+ KEY_NEWS_CAT_ID + " = " + catID;
	String deleteQuery = "DELETE " + queryPostFix;
	String childQuery = "SELECT " + KEY_NEWS_ID + " " + queryPostFix;

	DBHandler_SubNews dbHCHild = new DBHandler_SubNews(context);
	dbHCHild.clearSubNewsTable(childQuery, db);

	db.execSQL(deleteQuery);

	db.close();
}

public ArrayList<Object_ListItem_MainNews> getSavedNews() {

	ArrayList<Object_ListItem_MainNews> list = new ArrayList<Object_ListItem_MainNews>();
	// SELECT * FROM MainNewsData MND INNER JOIN SavedNews SN ON SN.NewsId =
	// MND.NewsID ORDER BY SN.Id DESC
	String selectQuary = " SELECT * FROM " + TABLE_NEWS
			+ " MND INNER JOIN "+TABLE_SAVED_NEWS+" SN ON SN."+KEY_SAVED_NEWS_NEWS_ID+
			" = MND."+KEY_NEWS_ID+" GROUP BY MND."+KEY_NEWS_ID+" ORDER BY SN." + KEY_SAVED_NEWS_ID + " DESC";
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cur = db.rawQuery(selectQuary, null);
	if (cur != null) {
		if (cur.moveToFirst()) {
			do {
				Object_ListItem_MainNews objNews = getNewsItem(cur);
				list.add(objNews);

			} while (cur.moveToNext());
		}

	}
	db.close();
	return list;
}

public void saveNews(int newsId){

	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();

	try{
	values.put(KEY_SAVED_NEWS_NEWS_ID, newsId);
	db.insert(TABLE_SAVED_NEWS, null, values);
	}catch(Exception ex){
		Log.i("HARSH", "Exception in inserting saved news");
	}
	db.close();
}
public void clearSavedNews() {
	SQLiteDatabase db = this.getWritableDatabase();

	String deleteQuery = "DELETE  FROM " + TABLE_SAVED_NEWS;
	db.execSQL(deleteQuery);

	db.close();
}
}
