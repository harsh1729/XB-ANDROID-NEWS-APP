package in.seemasandesh.newspaperapp;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Activity_EPaperShowStates extends Activity_Parent {

	static ArrayList<Object_State> listStates = new ArrayList<Object_State>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epaper_show_states);
		super.initSuper();
		
		initEPaperShowStates();
	}
	
	private void initEPaperShowStates(){
		LinearLayout parentLayout = (LinearLayout)findViewById(R.id.llytMainBodyAboutApp);
		
		for(Object_State obj : listStates){
			
			
			LayoutInflater layoutInflater = 
				      (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Button btn =(Button) layoutInflater.inflate(R.layout.custom_button, null);
			btn.setText(obj.name);
			btn.setId(obj.id);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); 
		    layoutParams.setMargins(0, 0, 0, 20); 
		    btn.setLayoutParams(layoutParams);
		    
			parentLayout.addView(btn);
		}
	}
	
	public void btnClick(View v){
		Activity_EPaperShowCities.selectedStateId = v.getId();
		Intent i = new Intent(this, Activity_EPaperShowCities.class);
		startActivity(i);
	}
}
