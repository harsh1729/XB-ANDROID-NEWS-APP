package com.xercesblue.newspaperapp;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Activity_Parent extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
	
	protected void initSuper(){
		ImageButton imgButtonBack = (ImageButton)(findViewById(R.id.imgHeaderBtnLeft));
		
		imgButtonBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		

		Custom_ThemeUtil.onActivityCreateSetTheme(this);
		
		TextView txtHeading = ((TextView)findViewById(R.id.txtHeader));
		
		if(this.getClass() == Activity_SavedNews.class){
			txtHeading.setText("Saved News");
			
		}else if(this.getClass() == Activity_SelectDateRange.class){
			txtHeading.setText("Select Date");
		}
		else if(this.getClass() == Activity_AboutUs.class){
			txtHeading.setText("About Us");
		}
		else if(this.getClass() == Activity_ContactUs.class){
			txtHeading.setText("Contact Us");
		}
		
		else if(this.getClass() == Activity_DateWiseNewsList.class){
			txtHeading.setText("News By Date");
		}
		else if(this.getClass() == Activity_TermsOfUSe.class){
			txtHeading.setText("Terms Of Use");
		}
		else if(this.getClass() == Activity_Settings.class){
			txtHeading.setText("Settings");
		}
		
	}
	 
}
