package com.xercesblue.newspaperapp;

import java.util.ArrayList;
import java.util.Calendar;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Activity_SelectDateRange extends Activity_Parent {

	private TextView pDisplayStartDate;
	private ImageButton pPickStartDate;
	private int pStartYear=0;
	private int pStartMonth;
	private int pStartDay;
	
	private TextView pDisplayEndDate;
	private ImageButton pPickEndDate;
	private int pEndYear = 0;
	private int pEndMonth;
	private int pEndDay;
	private int selectedCatId = -1;
	private ArrayList<Object_Category> arrayCat;
	
	final int DATE_DIALOG_ID_START = 99;
	final int DATE_DIALOG_ID_END = 104;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_date_range);
		initSuper();
		init_SelectDateRange();
	}

	private void init_SelectDateRange()
	{
		 pDisplayStartDate = (TextView) findViewById(R.id.txtStartDate);
	      pPickStartDate = (ImageButton) findViewById(R.id.btnStartDate);
	 
	        /** Listener for click event of the button */
	        pPickStartDate.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                showDialog(DATE_DIALOG_ID_START);
	            }
	        });
	        
	        //End DATE
	        pDisplayEndDate = (TextView) findViewById(R.id.txtEndDate);
		      pPickEndDate = (ImageButton) findViewById(R.id.btnEndDate);
		 
		        /** Listener for click event of the button */
		      pPickEndDate.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                showDialog(DATE_DIALOG_ID_END);
		            }
		        });
	 
	        /** Get the current date */
	        final Calendar cal = Calendar.getInstance();
	        pStartYear = cal.get(Calendar.YEAR);
	        pStartMonth = cal.get(Calendar.MONTH);
	        pStartDay = cal.get(Calendar.DAY_OF_MONTH);
	 
	        /**Set Categories*/
	      
	        DBHandler_Category dbH = new DBHandler_Category(this);
	        
	        arrayCat = dbH.getAllCategories(this);
			
	        Object_Category objAll = new Object_Category();
	        objAll.setId(-353);
	        objAll.setName("All");
	        objAll.setParentId(0);
	        
	        arrayCat.add(0, objAll);
	        
			ArrayList<String> my_array = new ArrayList<String>();
			
			for(Object_Category obj : arrayCat){
				my_array.add(obj.getName());
			}
	        
	        Spinner My_spinner = (Spinner) findViewById(R.id.spinner1);
	        
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_spinner_item,my_array);
	 
	        // Drop down layout style - list view with radio button
	        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	        // attaching data adapter to spinner
	        My_spinner.setAdapter(dataAdapter);
	    
	        My_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v, int pos,
					long id) {
				if(arrayCat != null && arrayCat.size() > pos)
					selectedCatId = arrayCat.get(pos).getId();
				Log.i("DARSH", "Selected CatId =  "+selectedCatId);
		}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID_START:
			return new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, 
					pStartDateSetListener,
					pStartYear, pStartMonth, pStartDay);

		case DATE_DIALOG_ID_END:
			if(pEndYear == 0){
				pEndYear = pStartYear;
				pEndMonth = pStartMonth;
				pEndDay = pStartDay;
			}
		return new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, 
				pEndDateSetListener,
				pEndYear, pEndMonth, pEndDay);

	}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pStartDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			pStartYear = year;
			pStartMonth = monthOfYear;
			pStartDay = dayOfMonth;
			updateStartDisplay();
		}
	};

	private void updateStartDisplay() {
		pDisplayStartDate.setText(
				new StringBuilder()
				.append(pStartDay).append("/")
				// Month is 0 based so add 1
				.append(pStartMonth + 1).append("/")
				.append(pStartYear).append(" "));
		
		if(pEndYear == 0){
			pEndYear = pStartYear;
			pEndMonth = pStartMonth;
			pEndDay = pStartDay;
		
			updateEndDisplay();
		}
	}
	
	private DatePickerDialog.OnDateSetListener pEndDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			
			boolean isLessThanStartDate = false;
			if(year < pStartYear)
				isLessThanStartDate = true;
			else if(year == pStartYear && monthOfYear < pStartMonth)
				isLessThanStartDate = true;
			else if(year == pStartYear && monthOfYear == pStartMonth && dayOfMonth<pStartDay)
				isLessThanStartDate = true;
			
			if(isLessThanStartDate){
				Toast.makeText(Activity_SelectDateRange.this, "End date cannot be less than start date", Toast.LENGTH_SHORT).show();
				
			}else{
				pEndYear = year;
				pEndMonth = monthOfYear;
				pEndDay = dayOfMonth;
				updateEndDisplay();
			}
		}
	};

	private void updateEndDisplay() {
		pDisplayEndDate.setText(
				new StringBuilder()
				.append(pEndDay).append("/")
				// Month is 0 based so add 1
				.append(pEndMonth + 1).append("/")
				.append(pEndYear).append(" "));
	}
	
	public void btnGetNewsClick(View v){
		
		if( validate()){			
			Intent i = new Intent(getApplicationContext(),
					Activity_DateWiseNewsList.class);

			i.putExtra("catId", selectedCatId);
			i.putExtra("startDate", pStartDay +"-"+pStartMonth+1 +"-"+pStartYear);
			i.putExtra("endDate", pEndDay +"-"+pEndMonth+1 +"-"+pEndYear);
			
			startActivity(i);
		}
	}
	
	private boolean validate(){
		boolean returnVal = true;
		if( pDisplayStartDate.getText() == null || pDisplayStartDate.getText().toString().trim().equals("Select")){
			returnVal = false;
			Toast.makeText(this, "Please select start date", Toast.LENGTH_SHORT).show();
		}else if(selectedCatId == -1){
			returnVal = false;
			Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
		}
		
		return returnVal;
	}
}
