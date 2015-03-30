package in.seemasandesh.newspaperapp;

import org.json.JSONArray;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_ContactUs extends Activity_Parent{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_us);
		super.initSuper();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		EditText txtEmail = (EditText)findViewById(R.id.edtEmail);	
		EditText txtName = (EditText)findViewById(R.id.edtName);
		
		Object_AppConfig obj =  new Object_AppConfig(this);
		
		txtEmail.setText(obj.getUserContact());
		txtName.setText(obj.getUserName());
		
	}
	public void onClickSendMessage(View v){
		if(!validateMessage())
			return;
 
		
		EditText txtMsg = (EditText)findViewById(R.id.edtMessage);
		EditText txtEmail = (EditText)findViewById(R.id.edtEmail);
		
		EditText txtName = (EditText)findViewById(R.id.edtName);
		
		postToServer(Globals.getDeviceIMEI(this),txtEmail.getText().toString(),txtMsg.getText().toString(),txtName.getText().toString());
		
		Object_AppConfig obj =  new Object_AppConfig(this);
		obj.setUserName(txtName.getText().toString());
		obj.setUserContact(txtEmail.getText().toString());
		
		txtEmail.setText("");
		txtMsg.setText("");
		txtName.setText("");
		
		new Custom_GCM_Register(this);
   	 	//Toast.makeText(this, "Message sent successfully", Toast.LENGTH_SHORT).show();
	}
	
	private void postToServer(String imeiNo,String contactDetail, String message ,String name){
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(
				getApplicationContext());

		try{
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
		}

		Custom_VolleyArrayRequest jsonObjectRQST = new Custom_VolleyArrayRequest(Request.Method.POST,
				Custom_URLs_Params.getURL_ContactUs(), Custom_URLs_Params.getParams_ContactUs(imeiNo, contactDetail, message,name),
						new Listener<JSONArray>() {
				
					@Override
					public void onResponse(JSONArray response) {
						Toast.makeText(Activity_ContactUs.this, "Posted Successfully! We will get back to you soon. ", Toast.LENGTH_SHORT ).show();
						
					}

				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError err) {
						err.printStackTrace();
						
							Toast.makeText(
									Activity_ContactUs.this,
									Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
									Toast.LENGTH_SHORT).show();

						}

				});

	
		Custom_VolleyAppController.getInstance().addToRequestQueue(
				jsonObjectRQST);
		
	}catch(Exception ex){

	}
	}
	private Boolean validateMessage(){
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(getApplicationContext());

		if(!cd.isConnectingToInternet()){
			Globals.showAlertDialogOneButton("Alert", "No Internet connection", this, "OK", null, true);
			return false;
		}
		EditText txtE = (EditText)findViewById(R.id.edtEmail);
		if(txtE.getText().toString().trim().equals("")){
			Globals.showAlertDialogOneButton("Error", "Please enter your email address", this, "OK", null, true);
			return false;
		}
		
		EditText txt = (EditText)findViewById(R.id.edtMessage);
		if(txt.getText().toString().trim().equals("")){
			Globals.showAlertDialogOneButton("Error", "Please write a message", this, "OK", null, true);
			return false;
		}
		return true;
	}
}
