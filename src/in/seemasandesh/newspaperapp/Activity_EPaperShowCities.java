package in.seemasandesh.newspaperapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_EPaperShowCities extends Activity_Parent {

	static ArrayList<Object_Cities>listCities = new ArrayList<Object_Cities>();
	static int selectedStateId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epaper_show_cities);
		super.initSuper();

		initEPaperShowCities();
	}

	private void initEPaperShowCities(){

		LinearLayout mainContainer = (LinearLayout)findViewById(R.id.llytMainBodyAboutApp);
		LayoutInflater li = LayoutInflater.from(this);
		LinearLayout horizontalContainer = null;

		int count = 1;
		for(Object_Cities obj : listCities){

			if(obj.state_id == selectedStateId){
				if(count % 2 == 1){
					horizontalContainer = (LinearLayout)li.inflate(R.layout.custom_epaper_cities, null);
					mainContainer.addView(horizontalContainer);
				}

				if(horizontalContainer != null){
					//LinearLayout llytChild = null;
					ImageView imgChild = null;
					TextView txtChild = null;
					if(count % 2 == 1){
						//llytChild = (LinearLayout)horizontalContainer.findViewById(R.id.llytLeft);
						imgChild = (ImageView)horizontalContainer.findViewById(R.id.imageLeft);
						txtChild = (TextView)horizontalContainer.findViewById(R.id.txtLeft);
					}else{
						//llytChild= (LinearLayout)horizontalContainer.findViewById(R.id.llytRight);
						imgChild = (ImageView)horizontalContainer.findViewById(R.id.imageRight);
						txtChild = (TextView)horizontalContainer.findViewById(R.id.txtRight);
					}
					
					imgChild.getLayoutParams().height = Globals.getScreenSize(this).y/3;
					Globals.loadImageIntoImageView(imgChild, obj.image_url, this, R.drawable.loading_image_small, R.drawable.no_image_small);
					txtChild.setText(obj.name);
					txtChild.setBackgroundResource(R.color.app_tranparent_black);
					

				}

				count ++;
			}
		}
		
		if(count == 1)
		{
			Globals.showAlertDialogError(this, "No Cities found!");
		}
	}

	public void onClickNews(View v){
		Intent i = new Intent(this, Activity_EPaperReader.class);
		startActivity(i);
	}
}
