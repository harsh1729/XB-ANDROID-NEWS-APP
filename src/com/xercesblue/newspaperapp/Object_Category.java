package com.xercesblue.newspaperapp;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Object_Category {
	private int id;
	private int parentId;
	private String Name;
	private String imageName;
	private Bitmap image;
	private ArrayList<Object_Category> listChildCategory= null;
	
	
	public ArrayList<Object_Category> getListChildCategory() {
		return listChildCategory;
	}

	public void setListChildCategory(ArrayList<Object_Category> listChildCategory) {
		this.listChildCategory = listChildCategory;
	}

	public String getImageName() {
		return imageName.trim();
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public int getParentId()
	{
		return parentId;
	}
	
	public void setParentId(int parentId)
	{
		this.parentId=parentId;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public void setName(String Name)
	{
		this.Name=Name;
	}

}
