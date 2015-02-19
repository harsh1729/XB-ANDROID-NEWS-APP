package com.xercesblue.newspaperapp;



public class Object_SubNewsItem 
{
	/*		FIELDS OF NEWS TABLE  */
	private int newsId;
	private int newsParentId;
	private String newsHeading;
	private String newsContent;
	private String newsImage;
	private String newsVideo;
	private String newsImageTagline;


	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public int getNewsParentId() {
		return newsParentId;
	}

	public void setNewsParentId(int parentId) {
		this.newsParentId = parentId;
	}

	public String getNewsHeading() {
		return newsHeading;
	}

	public void setNewsHeading(String newsHeading) {
		this.newsHeading = newsHeading;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	public String getNewsImage() {
		return newsImage;
	}

	public void setNewsImage(String newsImage) {
		this.newsImage = newsImage;
	}

	public String getNewsVideo() {
		return newsVideo;
	}

	public void setNewsVideo(String newsVideo) {
		this.newsVideo = newsVideo;
	}

	public String getNewsImageTagline() {
		return newsImageTagline;
	}

	public void setNewsImageTagline(String newsImageTagline) {
		this.newsImageTagline = newsImageTagline;
	}
	
}
