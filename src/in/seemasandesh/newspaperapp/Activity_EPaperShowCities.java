package in.seemasandesh.newspaperapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity_EPaperShowCities extends Activity_Parent {

	static ArrayList<Object_Cities>listCities = new ArrayList<Object_Cities>();
	static int selectedStateId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epaper_show_cities);
		super.initSuper();
		
		//initEPaperShowCities();
	}
	
	private void initEPaperShowCities(){
		for(Object_Cities obj : listCities){
			
		}
	}
	
	public void onClickNews(View v){
		Intent i = new Intent(this, Activity_EPaperReader.class);
		startActivity(i);
	}
}
