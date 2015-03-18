package com.xercesblue.newspaperapp;

import android.os.Bundle;



public class Activity_AboutUs extends Activity_Parent {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		super.initSuper();
	}
}
