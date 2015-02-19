package com.xercesblue.newspaperapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Custom_ThemeUtil

{

	public static final int THEME_RED_VALUE = 1;
	public static final int THEME_YELLOW_VALUE = 2;	
	public static final int THEME_GREEN_VALUE = 3;
	public static final int THEME_BLACK_VALUE = 5;
	public static final int THEME_BLUE_VALUE = 4;

	public static final String THEME_RED_POST_TEXT = "_red";
	public static final String THEME_BLUE_POST_TEXT = "_blue";
	public static final String THEME_YELLOW_POST_TEXT = "_yellow";
	public static final String THEME_GREEN_POST_TEXT = "_green";
	public static final String THEME_BLACK_POST_TEXT = "_black";
	

	public static final int THEME_RED_BG_COLOR = R.color.app_lightred;
	public static final int THEME_YELLOW_BG_COLOR = R.color.app_lightgray;	
	public static final int THEME_GREEN_BG_COLOR = R.color.app_lightgreen;
	public static final int THEME_BLACK_BG_COLOR = R.color.app_lightgray;
	public static final int THEME_BLUE_BG_COLOR = R.color.app_verylightblue;

	public static final int DEFAULT_THEME = THEME_RED_VALUE;

	public static void onThemeChange(Activity activity)
	{
		activity.finish();
		activity.startActivity(new Intent(activity, activity.getClass()));
	}

	public static void onActivityCreateSetTheme(Activity activity)
	{
		try
		{
			setGenericTheme(activity);

			if(activity.getClass() == Activity_Home.class){
				setThemeHome(activity);
			}else if(activity.getClass() == Activity_NewsDetail.class){
				setThemeNewsDetail(activity);
			}else {
				setThemeGeneralHeaderBack(activity);
			}


		}
		catch(Exception ex)
		{

		}

	}

	private static void setGenericTheme(Activity activity){
		Object_AppConfig obj = new Object_AppConfig(activity);
		int currentTheme = obj.getTheme();

		//Get controls
		LinearLayout llHeader =(LinearLayout) activity.findViewById(R.id.llytHeader);
		RelativeLayout rlytRoot =(RelativeLayout) activity.findViewById(R.id.rlytRoot);
		
		TextView txtScreenHeading = ((TextView)activity.findViewById(R.id.txtHeader));
		TextView txtUpdater1	 = ((TextView)activity.findViewById(R.id.txtHeaderUpdater1));
		TextView txtUpdater2 = ((TextView)activity.findViewById(R.id.txtHeaderUpdater2));
		TextView txtUpdater3 = ((TextView)activity.findViewById(R.id.txtHeaderUpdater3));

		
		if(currentTheme == THEME_YELLOW_VALUE){
			llHeader.setBackgroundResource(R.color.app_black);
			txtScreenHeading.setTextColor(activity.getResources().getColor(R.color.app_white));
			txtUpdater1.setTextColor(activity.getResources().getColor(R.color.app_white));
			txtUpdater2.setTextColor(activity.getResources().getColor(R.color.app_white));
			txtUpdater3.setTextColor(activity.getResources().getColor(R.color.app_white));
		}else{
			llHeader.setBackgroundResource(R.color.app_white);
			txtScreenHeading.setTextColor(activity.getResources().getColor(R.color.app_black));
			txtUpdater1.setTextColor(activity.getResources().getColor(R.color.app_black));
			txtUpdater2.setTextColor(activity.getResources().getColor(R.color.app_black));
			txtUpdater3.setTextColor(activity.getResources().getColor(R.color.app_black));
		}
		
		
		switch (currentTheme)
		{

		case THEME_YELLOW_VALUE:	
			rlytRoot.setBackgroundResource(THEME_YELLOW_BG_COLOR);
			break;

		case THEME_RED_VALUE:
			rlytRoot.setBackgroundResource(THEME_RED_BG_COLOR);

			break;

		case THEME_GREEN_VALUE:
			rlytRoot.setBackgroundResource(THEME_GREEN_BG_COLOR);
			break;

		case THEME_BLUE_VALUE:	
			rlytRoot.setBackgroundResource(THEME_BLUE_BG_COLOR);

			break;

		case THEME_BLACK_VALUE:
			rlytRoot.setBackgroundResource(THEME_BLACK_BG_COLOR);
			break;

		default:
			rlytRoot.setBackgroundResource( R.color.app_lightgray);
			break;

		}

	}
	private static void setThemeHome(Activity activity){

		Object_AppConfig obj = new Object_AppConfig(activity);

		//Get controls
		RelativeLayout catConatiner =(RelativeLayout) activity.findViewById(R.id.expListCategoriesContainer);


		ImageButton imgButtonToggle = (ImageButton)(activity.findViewById(R.id.imgHeaderBtnLeft));
		ImageButton imgButtonOptions = (ImageButton)(activity.findViewById(R.id.imgHeaderBtnRight));

		ExpandableListView expandableListView = (ExpandableListView)(activity.findViewById(R.id.expListCategories));
		
		//int dividerHeight = 1;
		
		ListView listOptions = (ListView)(activity.findViewById(R.id.listViewOptions));
		
		//expandableListView.setDivider(activity.getResources().getDrawable(R.color.app_lightgray));			
		//expandableListView.setChildDivider(activity.getResources().getDrawable(R.color.app_lightgray));
		expandableListView.setDividerHeight(0);
		
		//listOptions.setDivider(activity.getResources().getDrawable(R.color.app_lightgray));	
		//listOptions.setBackgroundResource(R.color.app_lightgray);
		//listOptions.setDividerHeight(0);
		listOptions.setPadding(0, 1, 0, 0);
		
		
		int appBackgroundThemeColor = THEME_BLACK_BG_COLOR;
		String imagePostFix = Custom_ThemeUtil.THEME_RED_POST_TEXT;

		switch (obj.getTheme())
		{

		case THEME_YELLOW_VALUE:		
			appBackgroundThemeColor = THEME_YELLOW_BG_COLOR;
			catConatiner.setBackgroundResource(appBackgroundThemeColor);
			
			//expandableListView.setDivider(activity.getResources().getDrawable(R.color.app_lightgray));			
			//expandableListView.setChildDivider(activity.getResources().getDrawable(R.color.app_lightgray));
			//expandableListView.setDividerHeight(dividerHeight);
			/*
			if(txtCategoryHeading != null){
				txtCategoryHeading.setBackgroundResource(R.drawable.bg_yellow_gradient);
				txtCategoryHeading.setTextColor(activity.getResources().getColor(R.color.app_black));
			}
			 */
			imagePostFix = Custom_ThemeUtil.THEME_YELLOW_POST_TEXT;
			break;

		case THEME_RED_VALUE:
			appBackgroundThemeColor = THEME_RED_BG_COLOR;
			catConatiner.setBackgroundResource(appBackgroundThemeColor);
			
			//expandableListView.setDivider(activity.getResources().getDrawable(R.color.app_lightred));			
			//expandableListView.setChildDivider(activity.getResources().getDrawable(R.color.app_lightred));
			//expandableListView.setDividerHeight(dividerHeight);
			
			/*
			if(txtCategoryHeading != null){
				txtCategoryHeading.setBackgroundResource(R.color.app_red);
				txtCategoryHeading.setTextColor(activity.getResources().getColor(R.color.app_white));
			}
			 */
			imagePostFix = Custom_ThemeUtil.THEME_RED_POST_TEXT;

			break;

		case THEME_GREEN_VALUE:
			appBackgroundThemeColor = THEME_GREEN_BG_COLOR;
			catConatiner.setBackgroundResource(appBackgroundThemeColor);
			
			//expandableListView.setDivider(activity.getResources().getDrawable(R.color.app_lightgreen));			
			//expandableListView.setChildDivider(activity.getResources().getDrawable(R.color.app_lightgreen));
			//expandableListView.setDividerHeight(dividerHeight);
			/*
			if(txtCategoryHeading != null){
				txtCategoryHeading.setBackgroundResource(R.color.app_green);
				txtCategoryHeading.setTextColor(activity.getResources().getColor(R.color.app_black));
			}
			 */
			imagePostFix = Custom_ThemeUtil.THEME_GREEN_POST_TEXT;
			break;

		case THEME_BLUE_VALUE:	
			appBackgroundThemeColor = THEME_BLUE_BG_COLOR;
			catConatiner.setBackgroundResource(appBackgroundThemeColor);
			
			//expandableListView.setDivider(activity.getResources().getDrawable(R.color.app_verylightblue));			
			//expandableListView.setChildDivider(activity.getResources().getDrawable(R.color.app_verylightblue));
			//expandableListView.setDividerHeight(dividerHeight);
			/*
			if(txtCategoryHeading != null){
				txtCategoryHeading.setBackgroundResource(R.color.app_darkblue);
				txtCategoryHeading.setTextColor(activity.getResources().getColor(R.color.app_white));
			}
			*/
			imagePostFix = Custom_ThemeUtil.THEME_BLUE_POST_TEXT;

			break;

		case THEME_BLACK_VALUE:
			appBackgroundThemeColor = THEME_BLACK_BG_COLOR;
			catConatiner.setBackgroundResource(appBackgroundThemeColor);
			
			//expandableListView.setDivider(activity.getResources().getDrawable(R.color.app_lightgray));			
			//expandableListView.setChildDivider(activity.getResources().getDrawable(R.color.app_lightgray));
			//expandableListView.setDividerHeight(dividerHeight);
			/*
			if(txtCategoryHeading != null){
				txtCategoryHeading.setBackgroundResource(R.color.app_black);
				txtCategoryHeading.setTextColor(activity.getResources().getColor(R.color.app_white));
			}
			*/
			
			imagePostFix = Custom_ThemeUtil.THEME_BLACK_POST_TEXT;

			break;

		default:
			break;

		}

		//int idOption = activity.getResources().getIdentifier("options"+imagePostFix, "drawable", activity.getPackageName());
		//int idOptionSelected = activity.getResources().getIdentifier("options"+imagePostFix+"_selected", "drawable", activity.getPackageName());
		
		StateListDrawable stateOptionsBG = new StateListDrawable();
		stateOptionsBG.addState(new int[] {android.R.attr.state_pressed},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateOptionsBG.addState(new int[] {android.R.attr.state_focused},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateOptionsBG.addState(new int[] { },
				activity.getResources().getDrawable(R.color.app_transparent ));
		
		StateListDrawable stateToggleBG = new StateListDrawable();
		stateToggleBG.addState(new int[] {android.R.attr.state_pressed},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateToggleBG.addState(new int[] {android.R.attr.state_focused},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateToggleBG.addState(new int[] { },
				activity.getResources().getDrawable(R.color.app_transparent ));
		
		int idDrawers = activity.getResources().getIdentifier("drawers"+imagePostFix, "drawable", activity.getPackageName());
		int idOptions = activity.getResources().getIdentifier("options"+imagePostFix, "drawable", activity.getPackageName());
		
		if (android.os.Build.VERSION.SDK_INT >= 16) 
		{
			imgButtonOptions.setBackground(stateOptionsBG);
			imgButtonToggle.setBackground(stateToggleBG);
		}
		
		imgButtonToggle.setImageResource(idDrawers);
		imgButtonOptions.setImageResource(idOptions);
/*
		int idDrawers = activity.getResources().getIdentifier("drawers"+imagePostFix, "drawable", activity.getPackageName());
		int idDrawersSelected = activity.getResources().getIdentifier("drawers"+imagePostFix+"_selected", "drawable", activity.getPackageName());
		
		StateListDrawable stateDrawers = new StateListDrawable();
		stateDrawers.addState(new int[] {android.R.attr.state_pressed},
				activity.getResources().getDrawable(idDrawersSelected));
		stateDrawers.addState(new int[] {android.R.attr.state_focused},
				activity.getResources().getDrawable(idDrawersSelected));
		stateDrawers.addState(new int[] { },
				activity.getResources().getDrawable(idDrawers));
		imgButtonToggle.setImageDrawable(stateDrawers);
		*/
		
	}

	private static void setThemeNewsDetail(Activity activity){
		
		Object_AppConfig obj = new Object_AppConfig(activity);
		int currentTheme = obj.getTheme();

		//Get controls
		
		ImageButton imgButtonBack = (ImageButton)(activity.findViewById(R.id.imgHeaderBtnLeft));
		ImageButton imgButtonOptions = (ImageButton)(activity.findViewById(R.id.imgHeaderBtnRight));

		ListView listOptions = (ListView)(activity.findViewById(R.id.listViewOptions));
		
		listOptions.setPadding(0, 1, 0, 0);
		
		String imagePostFix = Custom_ThemeUtil.THEME_RED_POST_TEXT;
		int appBackgroundThemeColor = THEME_BLACK_BG_COLOR;
		
		switch (currentTheme)
		{

		case THEME_YELLOW_VALUE:
			appBackgroundThemeColor = THEME_BLACK_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_YELLOW_POST_TEXT;
			break;

		case THEME_RED_VALUE:
			appBackgroundThemeColor = THEME_RED_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_RED_POST_TEXT;

			break;

		case THEME_GREEN_VALUE:
			appBackgroundThemeColor = THEME_GREEN_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_GREEN_POST_TEXT;
			break;

		case THEME_BLUE_VALUE:	
			appBackgroundThemeColor = THEME_BLUE_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_BLUE_POST_TEXT;

			break;

		case THEME_BLACK_VALUE:
			appBackgroundThemeColor = THEME_BLACK_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_BLACK_POST_TEXT;

			break;

		default:
			break;

		}
		
		
		StateListDrawable stateBackBG = new StateListDrawable();
		stateBackBG.addState(new int[] {android.R.attr.state_pressed},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateBackBG.addState(new int[] {android.R.attr.state_focused},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateBackBG.addState(new int[] { },
				activity.getResources().getDrawable(R.color.app_transparent ));
		
		StateListDrawable stateOptionsBG = new StateListDrawable();
		stateOptionsBG.addState(new int[] {android.R.attr.state_pressed},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateOptionsBG.addState(new int[] {android.R.attr.state_focused},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateOptionsBG.addState(new int[] { },
				activity.getResources().getDrawable(R.color.app_transparent ));
		
		
		int idOptions = activity.getResources().getIdentifier("options"+imagePostFix, "drawable", activity.getPackageName());		
		int idBack = activity.getResources().getIdentifier("back"+imagePostFix, "drawable", activity.getPackageName());
	
		if (android.os.Build.VERSION.SDK_INT >= 16) 
		{
			imgButtonBack.setBackground(stateBackBG);
			imgButtonOptions.setBackground(stateOptionsBG);
		}
		
		imgButtonBack.setImageResource(idBack);		
		imgButtonOptions.setImageResource(idOptions);
		
	}
	
	
	private static void setThemeGeneralHeaderBack(Activity activity){
		
		Object_AppConfig obj = new Object_AppConfig(activity);
		int currentTheme = obj.getTheme();

		ImageButton imgButtonBack = (ImageButton)(activity.findViewById(R.id.imgHeaderBtnLeft));

		String imagePostFix = Custom_ThemeUtil.THEME_RED_POST_TEXT;
		int appBackgroundThemeColor = THEME_BLACK_BG_COLOR;
		
		switch (currentTheme)
		{

		case THEME_YELLOW_VALUE:
			appBackgroundThemeColor = THEME_BLACK_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_YELLOW_POST_TEXT;
			break;

		case THEME_RED_VALUE:
			appBackgroundThemeColor = THEME_RED_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_RED_POST_TEXT;

			break;

		case THEME_GREEN_VALUE:
			appBackgroundThemeColor = THEME_GREEN_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_GREEN_POST_TEXT;
			break;

		case THEME_BLUE_VALUE:	
			appBackgroundThemeColor = THEME_BLUE_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_BLUE_POST_TEXT;

			break;

		case THEME_BLACK_VALUE:
			appBackgroundThemeColor = THEME_BLACK_BG_COLOR;
			imagePostFix = Custom_ThemeUtil.THEME_BLACK_POST_TEXT;

			break;

		default:
			break;

		}
		
		StateListDrawable stateBackBG = new StateListDrawable();
		stateBackBG.addState(new int[] {android.R.attr.state_pressed},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateBackBG.addState(new int[] {android.R.attr.state_focused},
				activity.getResources().getDrawable(appBackgroundThemeColor));
		stateBackBG.addState(new int[] { },
				activity.getResources().getDrawable(R.color.app_transparent ));
		
		int idBack = activity.getResources().getIdentifier("back"+imagePostFix, "drawable", activity.getPackageName());
	
		if (android.os.Build.VERSION.SDK_INT >= 16) 
		{
			imgButtonBack.setBackground(stateBackBG);
			}
		
		imgButtonBack.setImageResource(idBack);		
		
	}
	


	

	private static String getImagePostFix(Activity activity){

		Object_AppConfig obj = new Object_AppConfig(activity);
		String imagePostFix = THEME_RED_POST_TEXT;

		switch (obj.getTheme())
		{

		case THEME_YELLOW_VALUE:

			imagePostFix = Custom_ThemeUtil.THEME_YELLOW_POST_TEXT;
			break;

		case THEME_RED_VALUE:

			imagePostFix = Custom_ThemeUtil.THEME_RED_POST_TEXT;

			break;

		case THEME_GREEN_VALUE:

			imagePostFix = Custom_ThemeUtil.THEME_GREEN_POST_TEXT;
			break;

		case THEME_BLUE_VALUE:		

			imagePostFix = Custom_ThemeUtil.THEME_BLUE_POST_TEXT;

			break;

		case THEME_BLACK_VALUE:

			imagePostFix = Custom_ThemeUtil.THEME_BLACK_POST_TEXT;

			break;

		default:
			break;

		}
		return imagePostFix;
	}

	/*
	public static int getExpandImageId(Activity activity){	

		return activity.getResources().getIdentifier("expand"+getImagePostFix(activity), "drawable", activity.getPackageName());
	}
	public static int getNextImageId(Activity activity){	

		return activity.getResources().getIdentifier("next"+getImagePostFix(activity), "drawable", activity.getPackageName());
	}*/
	public static StateListDrawable getDrawableExpandImage(Activity activity){
		
		int id = activity.getResources().getIdentifier("expand"+getImagePostFix(activity), "drawable", activity.getPackageName());

		StateListDrawable stateBG = new StateListDrawable();
		stateBG.addState(new int[] {android.R.attr.state_pressed},
				activity.getResources().getDrawable(R.drawable.expand_selected));
		stateBG.addState(new int[] {android.R.attr.state_focused},
				activity.getResources().getDrawable(R.drawable.expand_selected));
		stateBG.addState(new int[] { },
				activity.getResources().getDrawable(id));
		
		return stateBG;
	}
	
	public static StateListDrawable getDrawableNextImage(Activity activity){
		
		int id = activity.getResources().getIdentifier("next"+getImagePostFix(activity), "drawable", activity.getPackageName());

		StateListDrawable stateBG = new StateListDrawable();
		stateBG.addState(new int[] {android.R.attr.state_pressed},
				activity.getResources().getDrawable(R.drawable.next_selected));
		stateBG.addState(new int[] {android.R.attr.state_focused},
				activity.getResources().getDrawable(R.drawable.next_selected));
		stateBG.addState(new int[] { },
				activity.getResources().getDrawable(id));
		
		return stateBG;
	}
	public static int getSaveImageId(Activity activity){	

		return activity.getResources().getIdentifier("save"+getImagePostFix(activity), "drawable", activity.getPackageName());
	}
	public static int getRefreshImageId(Activity activity){	

		return activity.getResources().getIdentifier("refresh"+getImagePostFix(activity), "drawable", activity.getPackageName());
	}
	public static int getSettingsImageId(Activity activity){	

		return activity.getResources().getIdentifier("settings"+getImagePostFix(activity), "drawable", activity.getPackageName());
	}
	public static int getCalenderImageId(Activity activity){	

		return activity.getResources().getIdentifier("calender"+getImagePostFix(activity), "drawable", activity.getPackageName());
	}
	public static int getShareImageId(Activity activity){	

		return activity.getResources().getIdentifier("share"+getImagePostFix(activity), "drawable", activity.getPackageName());
	}
	public static int getTwoWayArrowImageId(Activity activity){	

		return activity.getResources().getIdentifier("two_way_arrow"+getImagePostFix(activity), "drawable", activity.getPackageName());
	}
	
	public static int getThemeColorId(Activity activity){
		
		Object_AppConfig obj = new Object_AppConfig(activity);
		int colorId = R.color.app_black;

		switch (obj.getTheme())
		{

		case THEME_YELLOW_VALUE:

			colorId = R.drawable.bg_yellow_gradient;//R.color.app_yellow;//
			break;

		case THEME_RED_VALUE:

			colorId = R.color.app_red;

			break;

		case THEME_GREEN_VALUE:

			colorId = R.color.app_green;
			break;

		case THEME_BLUE_VALUE:		

			colorId = R.color.app_darkblue;

			break;

		case THEME_BLACK_VALUE:

			colorId = R.color.app_black;

			break;

		default:
			break;

		}
		return colorId;
	}
	
}