package in.seemasandesh.newspaperapp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class Activity_Splash extends Activity {

private final int SPLASH_TIME_OUT = 500;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		resizeImages();
		
			
		DBHandler_Main db = new DBHandler_Main(this);
		db.createDataBase();
		hideScreenAfterTimeOut();
	}

	
	private void resizeImages(){
		ImageView imgViewLogo = (ImageView)findViewById(R.id.imgViewLogo);
		ImageView imgViewName = (ImageView)findViewById(R.id.imgViewLogoName);

		int screenWidth = Globals.getScreenSize(this).x;
		int logoWidth = screenWidth/100 * 20 ;// 17%
		int nameWidth = screenWidth/100 * 78 ;// 64%

		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo, options);
		logo = Globals.scaleToWidth(logo,logoWidth);
		Bitmap name = BitmapFactory.decodeResource(getResources(), R.drawable.logo_name, options);
		name = Globals.scaleToWidth(name,nameWidth);

		imgViewLogo.setImageBitmap(logo);
		imgViewName.setImageBitmap(name);
	}
	private void hideScreenAfterTimeOut(){
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				Intent i = null;
				int canPublish = 1;
				
				try{
					canPublish = 1;//Globals.getAppConfig(Activity_Splash.this).canLogin;
				}catch(Exception ex){
					Log.e("HARSH","Error in hideScreenAfterTimeOut , Activity Splash");
				}
				
				if(canPublish > 0){ // already selected
					i = new Intent(Activity_Splash.this, Activity_Home.class);
					startActivity(i);
					finish();
				}else{
					Globals.showAlertDialogError(Activity_Splash.this, "App is blocked ,please contact app provider.");
				}

				
			}
		}, SPLASH_TIME_OUT);
	}
		
}

