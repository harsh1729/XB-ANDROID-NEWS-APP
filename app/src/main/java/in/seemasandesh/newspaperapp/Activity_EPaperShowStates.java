package in.seemasandesh.newspaperapp;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
		
		int count = 0;
		for(Object_State obj : listStates){

			LayoutInflater layoutInflater = 
				      (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Button btn =(Button) layoutInflater.inflate(R.layout.custom_button, null);
			btn.setText(obj.name);
			btn.setTag(count);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); 
		    layoutParams.setMargins(0, 0, 0, 20); 
		    btn.setLayoutParams(layoutParams);
		    
			parentLayout.addView(btn);
			count ++;
		}
	}
	
	public void btnClick(View v){
		int pos ;
		try{
			pos = Integer.parseInt(v.getTag().toString());
		}catch(Exception ex){
			pos = -1;
		}
		
		if(pos > -1 && listStates.size() > pos){
			Object_State state  = listStates.get(pos);
			if(state.listCities != null && state.listCities.size() > 0){
				Activity_EPaperShowCities.selectedState= state;
				Intent i = new Intent(this, Activity_EPaperShowCities.class);
				startActivity(i);
			}else{
				Toast.makeText(this, "No Epaper found for "+state.name+", Please try again later!", Toast.LENGTH_SHORT).show();
			}
			
		}else{
			Toast.makeText(this, "Some error ocured, Please try again later!", Toast.LENGTH_SHORT).show();
		}
		
	}
}
