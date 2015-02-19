package com.xercesblue.newspaperapp;

import java.util.ArrayList;


import android.app.Activity;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Custom_AdapterOptions extends BaseAdapter {
	
	LayoutInflater myInflater;
	Activity context;
	ArrayList<Object_Options> listOptions;
	
	public Custom_AdapterOptions(Activity context , ArrayList<Object_Options> listOptions) {
		this.context = context;
		myInflater = LayoutInflater.from(this.context);
		this.listOptions = listOptions;
		
	}

	@Override
	public int getCount() {
		if(listOptions != null)
			return listOptions.size();
		
		return 0;
	}

	@Override
	public Object_Options getItem(int position) {	
		if(listOptions != null && listOptions.size() > position)
			return listOptions.get(position);
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return position + 99;
	
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parentViewGroup) {
		
		if (convertView == null)
			convertView = myInflater.inflate(R.layout.row_item_options, null);

		Object_Options objOption= null;
		
		if(listOptions != null && listOptions.size() > position)
			objOption = listOptions.get(position);
		
		if(objOption == null)
			return convertView;
		
		//Set Option Text
		TextView tv = (TextView) convertView.findViewById(R.id.txtOptions);
		
		String heading = objOption.getText();
		tv.setText(heading);
		
		Point point = Globals.getScreenSize(context);
		//int screenWidth = point.x;
		int screenHeight = point.y;
		
		//Set Option Image
		int imgMargin = 5;
		int imgWidthHeight = (int)screenHeight/15; 
		
		ImageView iv = (ImageView) convertView.findViewById(R.id.imgOptions);
		
		//iv.setImageResource(objOption.getImageResourceId());
		iv.setImageDrawable(objOption.getStateDrawable());
		LinearLayout.LayoutParams lpImg = new LinearLayout.LayoutParams(
				imgWidthHeight,
				imgWidthHeight);
		lpImg.setMargins(imgMargin, imgMargin, imgMargin, imgMargin);
		iv.setLayoutParams(lpImg);
		


		return convertView;
	}
    
}
