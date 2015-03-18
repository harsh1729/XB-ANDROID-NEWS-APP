package com.xercesblue.newspaperapp;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Activity_Settings extends Activity_Parent {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		super.initSuper();
		initSettings();
	}

	private void initSettings(){

		TextView  txtAppSettings = (TextView)findViewById(R.id.txtAppSettings);		
		TextView  txtSupportFeedback = (TextView)findViewById(R.id.txtSupportFeedback);
		
		LinearLayout llytBottomLineAppSettings = (LinearLayout)findViewById(R.id.llytBottomLineAppSettings);
		LinearLayout llytBottomLineSupportFeedback = (LinearLayout)findViewById(R.id.llytBottomLineSupportFeedback);

		TextView  txtSample = (TextView)findViewById(R.id.txtSampleFont);
		txtSample.setTextSize(Globals.getAppFontSize_Normal(this));
		
		int appColor = Custom_ThemeUtil.getThemeColorId(this);

		Object_AppConfig obj = new Object_AppConfig(Activity_Settings.this);
		float fontfactor =obj.getFontFactor();
		int seekProgress = (int) ((fontfactor-1)*100);
		
		
		SeekBar seek = (SeekBar)findViewById(R.id.seekBarFont);
		
		Drawable dr = getResources().getDrawable(R.drawable.thumb);
		Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
		int widthThumb = Globals.getScreenSize(this).x /10;
		bitmap = Globals.scaleToWidth(bitmap, widthThumb);
		
		Drawable thumbScaled = new BitmapDrawable(getResources(),bitmap);
		
		seek.setThumb(thumbScaled);
		seek.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress));
		seek.setProgress(seekProgress);
		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			float fontFactor = 0;

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				fontFactor = (float) ((float)progress/100.0 + 1);
				setTextFont(fontFactor);
				Log.i("HARSH", "fontFactor -- "+fontFactor);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				//Toast.makeText(Activity_Settings.this,"New font is applied successfully",Toast.LENGTH_SHORT).show();
			}
		});
		
		//Special case
		if(appColor != R.drawable.bg_yellow_gradient){
			txtAppSettings.setTextColor(getResources().getColor(appColor));
			txtSupportFeedback.setTextColor(getResources().getColor(appColor));
			//seek.setProgressDrawable(new ColorDrawable(getResources().getColor(appColor)));
		}
		else{
			txtAppSettings.setTextColor(getResources().getColor(R.color.app_yellow));
			txtSupportFeedback.setTextColor(getResources().getColor(R.color.app_yellow));
			//seek.setProgressDrawable(getResources().getDrawable(appColor));
			
		}

		//getResources().getDrawable(R.drawable.seekbar_progress_red)
		//seek.setBackgroundResource(R.color.app_lightgray);

		llytBottomLineAppSettings.setBackgroundResource(appColor);
		llytBottomLineSupportFeedback.setBackgroundResource(appColor);
		
		LinearLayout llytThemeRed = (LinearLayout)findViewById(R.id.llytThemeRed);
		LinearLayout llytThemeGreen = (LinearLayout)findViewById(R.id.llytThemeGreen);
		LinearLayout llytThemeYellow = (LinearLayout)findViewById(R.id.llytThemeYellow);
		LinearLayout llytThemeBlue = (LinearLayout)findViewById(R.id.llytThemeBlue);
		LinearLayout llytThemeBlack = (LinearLayout)findViewById(R.id.llytThemeBlack);

		int height = Globals.getScreenSize(this).y/15;

		//Toast.makeText(this, "Height is "+llytThemeRed.getLayoutParams().width, Toast.LENGTH_SHORT).show();
		LayoutParams lp = new LayoutParams(0, height);
		lp.weight = 1;
		lp.setMargins(8, 8, 8, 8);
		llytThemeRed.setLayoutParams(lp);
		llytThemeGreen.setLayoutParams(lp);
		llytThemeYellow.setLayoutParams(lp);
		llytThemeBlue.setLayoutParams(lp);
		llytThemeBlack.setLayoutParams(lp);


		LayoutParams lpSelected = new LayoutParams(0, height*12/10);
		lpSelected.setMargins(8, 8, 8, 8);
		lpSelected.weight = (float) 1.2;
		switch (obj.getTheme()) {
		case Custom_ThemeUtil.THEME_RED_VALUE:
			llytThemeRed.setLayoutParams(lpSelected);
			break;
		case Custom_ThemeUtil.THEME_GREEN_VALUE:
			llytThemeGreen.setLayoutParams(lpSelected);
			break;
		case Custom_ThemeUtil.THEME_YELLOW_VALUE:
			llytThemeYellow.setLayoutParams(lpSelected);
			break;
		case Custom_ThemeUtil.THEME_BLUE_VALUE:
			llytThemeBlue.setLayoutParams(lpSelected);
			break;
		case Custom_ThemeUtil.THEME_BLACK_VALUE:
			llytThemeBlack.setLayoutParams(lpSelected);
			break;
		default:
			break;
		}
	}

	public void onClickTheme(View v){
		Object_AppConfig obj = new Object_AppConfig(this);
		
		int newTheme = obj.getTheme();
		
		switch (v.getId()) {
		case R.id.llytThemeRed:
			newTheme = Custom_ThemeUtil.THEME_RED_VALUE;
			break;
		case R.id.llytThemeYellow:
			newTheme = Custom_ThemeUtil.THEME_YELLOW_VALUE;
			break;
		case R.id.llytThemeBlue:
			newTheme = Custom_ThemeUtil.THEME_BLUE_VALUE;
			break;
		case R.id.llytThemeGreen:
			newTheme = Custom_ThemeUtil.THEME_GREEN_VALUE;
			break;
		case R.id.llytThemeBlack:
			newTheme = Custom_ThemeUtil.THEME_BLACK_VALUE;
			break;

		default:
			break;
		}

		if(newTheme != obj.getTheme()){
			obj.setTheme(newTheme);
			Custom_ThemeUtil.onThemeChange(this);
		}
		
	}

	private void setTextFont(float fontFactor){
		Object_AppConfig obj = new Object_AppConfig(Activity_Settings.this);
		obj.setFontFactor(fontFactor);
		
		Log.i("HARSH", "Progress is "+ fontFactor);
		TextView  txtSample = (TextView)findViewById(R.id.txtSampleFont);
		txtSample.setTextSize(Globals.getAppFontSize_Normal(Activity_Settings.this));
	}
	
	public void onToggleNotificationClicked(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	    	
	    } else {
	    }
	}
	public void onSelectSettingOptions(View view) {
		//Toast.makeText(this, "Development in progress", Toast.LENGTH_SHORT).show();
		
		Class<?> nextClass = null;
		switch (view.getId()) {
		case R.id.txtAboutUs:
			nextClass = Activity_AboutUs.class;
			break;
		case R.id.txtTerms:
			nextClass = Activity_TermsOfUSe.class;
			break;
		case R.id.txtShareUs:
			shareClick();
			break;
		case R.id.txtRateUs:
			Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
			if(cd.isConnectingToInternet()){
				 Custom_AppRater.rateIt(this);

			}
			break;
		case R.id.txtContactUs:
			nextClass = Activity_ContactUs.class;
			break;
			
		}
		if(nextClass != null){
			Intent i = new Intent(this, nextClass);
			startActivity(i);
		}
		
		
	}
	
public void shareClick(){
		
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, Globals.getShareAppMsg()+ "\n "+Globals.SHARE_URL);
		//sendIntent.setPackage("com.whatsapp");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
	
}
