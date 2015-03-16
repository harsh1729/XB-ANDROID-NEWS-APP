package com.xercesblue.newspaperapp;


import java.util.ArrayList;

public class Object_ListItem_MainNews implements Interface_ListItem
{
	/*		FIELDS OF NEWS TABLE  */
	private int id;
	private String heading;
	private String content;
	private int catId;
	private String date;
	private String imagePath;
	private String video;
	private String shareLink;
	private String imageTagline;

	private ArrayList<Object_SubNewsItem> listSubNews = null;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String image) {
		this.imagePath = image;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	public String getImageTagline() {
		return imageTagline;
	}

	public void setImageTagline(String imageTagline) {
		this.imageTagline = imageTagline;
	}

	public ArrayList<Object_SubNewsItem> getListSubNews() {
		if(listSubNews == null)
			listSubNews = new ArrayList<Object_SubNewsItem>();
			
		return listSubNews;
	}

	public Boolean isListSubNewsNull(){
		if(listSubNews == null)
			return true;
		
		return false;
	}
	public void setListSubNews(ArrayList<Object_SubNewsItem> listSubNews) {
		this.listSubNews = listSubNews;
	}

	


}
