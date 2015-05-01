package in.seemasandesh.newspaperapp;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_EPaperShowCities extends Activity_Parent {

	static Object_State selectedState ;
	private TextView pDisplayStartDate;
	private ImageButton pPickStartDate;
	private int pStartYear=0;
	private int pStartMonth;
	private int pStartDay;
	
	public ProgressDialog mDialog;
	final int DATE_DIALOG_ID_START = 99;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epaper_show_cities);
		super.initSuper();

		initEPaperShowCities();
	}

	private void initEPaperShowCities(){

		TextView txtHeading = ((TextView)findViewById(R.id.txtHeader));
		txtHeading.setText(selectedState.name);

		pDisplayStartDate = (TextView) findViewById(R.id.txtStartDate);
		pPickStartDate = (ImageButton) findViewById(R.id.btnStartDate);

		/** Listener for click event of the button */
		pPickStartDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID_START);
			}
		});

		boolean isDateSelected = false;
		try{
			if(selectedState.date != null){
				String[] array = selectedState.date.split("-");
				if(array.length > 2){
					pStartYear = Integer.parseInt(array[2])  ;
					//Month is 0 based
					pStartMonth = Integer.parseInt(array[1]) - 1;
					pStartDay = Integer.parseInt(array[0]);
					isDateSelected = true;
					
				}
			}
		}catch(Exception ex){

		}
		
		if(!isDateSelected){
			final Calendar cal = Calendar.getInstance();
	        pStartYear = cal.get(Calendar.YEAR);
	        pStartMonth = cal.get(Calendar.MONTH);
	        pStartDay = cal.get(Calendar.DAY_OF_MONTH);
		}
		
		updateStartDisplay();
		
		showEpaperView();
	}

	private void showEpaperView(){
		LinearLayout mainContainer = (LinearLayout)findViewById(R.id.llytContainerNewsItem);
		mainContainer.removeAllViews();
		LayoutInflater li = LayoutInflater.from(this);
		LinearLayout horizontalContainer = null;

		int count = 1;
		if(selectedState.listCities != null)
		for(Object_Cities obj : selectedState.listCities){
			if(count % 2 == 1){
				horizontalContainer = (LinearLayout)li.inflate(R.layout.custom_epaper_cities, null);
				mainContainer.addView(horizontalContainer);
			}
			LinearLayout llytChild = null;
			ImageView imgChild = null;
			TextView txtChild = null;
			if(count % 2 == 1){
				llytChild = (LinearLayout)horizontalContainer.findViewById(R.id.llytLeft);
				imgChild = (ImageView)horizontalContainer.findViewById(R.id.imageLeft);
				txtChild = (TextView)horizontalContainer.findViewById(R.id.txtLeft);
			}else{
				llytChild= (LinearLayout)horizontalContainer.findViewById(R.id.llytRight);
				imgChild = (ImageView)horizontalContainer.findViewById(R.id.imageRight);
				txtChild = (TextView)horizontalContainer.findViewById(R.id.txtRight);
			}

			llytChild.setTag(count);
			llytChild.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					onClickNews(arg0);
				}
			});
			imgChild.getLayoutParams().height = Globals.getScreenSize(this).y/3;
			Globals.loadImageIntoImageView(imgChild, obj.thumb_image_url, this, R.drawable.loading_image_small, R.drawable.no_image_small);
			txtChild.setText(obj.name);
			txtChild.setBackgroundResource(R.color.app_tranparent_black);

			count ++;

		}

		if(count == 1)
		{
			Toast.makeText(this, "No EPaper found for selected date!", Toast.LENGTH_SHORT).show();
		}
	}
	public void onClickNews(View v){
		int pos ;
		try{
			pos = Integer.parseInt(v.getTag().toString());
		}catch(Exception ex){
			pos = 0;
		}

		//POS is 1 based not 0 based

		if(pos > 0 && selectedState.listCities.size() >= pos){
			Object_Cities city = selectedState.listCities.get(pos-1);
			Activity_EPaperReader.selectedCity = city;
			Intent i = new Intent(this, Activity_EPaperReader.class);
			startActivity(i);
		}else{
			Toast.makeText(this, "Some error ocured, Please try again later!", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID_START:
			return new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, 
					pStartDateSetListener,
					pStartYear, pStartMonth, pStartDay);


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
			getEPaperDetailsFromServer();
		}
	};

	private void updateStartDisplay() {
		pDisplayStartDate.setText(
				new StringBuilder()
				.append(Globals.getTwoDigitNo(pStartDay)).append("-")
				// Month is 0 based so add 1
				.append(Globals.getTwoDigitNo(pStartMonth + 1)).append("-")
				.append(Globals.getTwoDigitNo(pStartYear)));


	}

	private String getSelectedDate(){
		 
		return Globals.getTwoDigitNo(pStartDay)+"-" +Globals.getTwoDigitNo(pStartMonth+1)+"-"+Globals.getTwoDigitNo(pStartYear) ;
	}
private void getEPaperDetailsFromServer(){
		
		try {
			
			mDialog = Globals.showLoadingDialog(mDialog, this,false);			

			String url = Custom_URLs_Params.getURL_EpaperStatesnCities();
			
			Custom_VolleyArrayRequest jsonObjectRQST = new Custom_VolleyArrayRequest(Request.Method.POST,
					url, Custom_URLs_Params.getParams_EpaperStatesnCities(getSelectedDate(),selectedState.id),
							new Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							parseAppEpaperJson(response);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							Log.i("DARSH", "ERROR VolleyError");
							
							Globals.hideLoadingDialog(mDialog);
							Globals.showAlertDialogOneButton(
									Globals.TEXT_CONNECTION_ERROR_HEADING,
									Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
									Activity_EPaperShowCities.this, "OK", null, false);
							if(err != null){
								Log.i("DARSH", "ERROR Details getLocalizedMessage : "+err.getLocalizedMessage());
								Log.i("DARSH", "ERROR Details getMessage : "+err.getMessage());
								Log.i("DARSH", "ERROR Details getStackTrace : "+err.getStackTrace());
							}

						}
					});

			Custom_AppController.getInstance().addToRequestQueue(
					jsonObjectRQST);

		}

		catch (Exception e) {
			Log.i("HARSH",
					"Excetion FIRSTCALL getEPaperDetailsFromServer");
			Globals.hideLoadingDialog(mDialog);


		}

	}
	private void parseAppEpaperJson(JSONArray arrayStates) {
		
		Globals.hideLoadingDialog(mDialog);
		if (arrayStates == null){
			return;
		}
		Log.i("DARSH", "RESPONCE parseAppEpaperJson is : "+arrayStates.toString());
		try{
			
			if(arrayStates.length() > 0)
				{
					selectedState = new Object_State();
					JSONObject json_State = arrayStates.getJSONObject(0);
					
					selectedState.id = json_State.getInt("id");
					selectedState.name = json_State.getString("name");
					selectedState.date = json_State.getString("publishdate");
					
					if(json_State.has("areas")){
						
						JSONArray arrayCities = json_State.getJSONArray("areas");
						if(arrayCities.length() > 0){
							selectedState.listCities = new ArrayList<Object_Cities>();
						}
						for(int j=0; j<arrayCities.length(); j++)
						{
							Object_Cities ob = new Object_Cities();
							JSONObject json_City = arrayCities.getJSONObject(j);
							ob.id = json_City.getInt("id");
							ob.pdf_url = json_City.getString("filename");
							ob.name = json_City.getString("name");
							ob.epaper_id = json_City.getInt("epaperid");
							ob.total_pages = json_City.getInt("totalpages");
							ob.area_code = json_City.getString("areacode");
							ob.thumb_image_url = json_City.getString("previewimage");
							
							selectedState.listCities.add(ob);
						}
					}
				}

				showEpaperView();	
			}
			catch(Exception ex){
			
		}
		
	}

	@Override
	protected void onDestroy() {
		selectedState = null;
		super.onDestroy();

	}
}
