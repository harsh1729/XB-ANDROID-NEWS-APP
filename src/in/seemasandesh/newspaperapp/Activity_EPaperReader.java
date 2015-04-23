package in.seemasandesh.newspaperapp;

import com.androidopentutorials.imageslideshow.utils.HomeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Activity_EPaperReader extends FragmentActivity {

	private Fragment contentFragment;
	HomeFragment homeFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epaper_reader);
		initReader();
		
		
		FragmentManager fragmentManager = getSupportFragmentManager();

		if (savedInstanceState != null) {
			/*
			if (savedInstanceState.containsKey("content")) {
				String content = savedInstanceState.getString("content");
				
				if (content.equals(ProductDetailFragment.ARG_ITEM_ID)) {
					if (fragmentManager
							.findFragmentByTag(ProductDetailFragment.ARG_ITEM_ID) != null) {
						contentFragment = fragmentManager
								.findFragmentByTag(ProductDetailFragment.ARG_ITEM_ID);
					}
				}
				
			}*/
			if (fragmentManager.findFragmentByTag(HomeFragment.ARG_ITEM_ID) != null) {
				homeFragment = (HomeFragment) fragmentManager
						.findFragmentByTag(HomeFragment.ARG_ITEM_ID);
				contentFragment = homeFragment;
			}
		} else {
			homeFragment = new HomeFragment();
			switchContent(homeFragment, HomeFragment.ARG_ITEM_ID);
		}
	}
	
	private void initReader(){
		ImageButton imgButtonBack = (ImageButton)(findViewById(R.id.imgHeaderBtnLeft));
		
		imgButtonBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		

		Custom_ThemeUtil.onActivityCreateSetTheme(this);
		
		TextView txtHeading = ((TextView)findViewById(R.id.txtHeader));
		txtHeading.setText("E Paper");
		
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (contentFragment instanceof HomeFragment) {
			outState.putString("content", HomeFragment.ARG_ITEM_ID);
		} else {
			//outState.putString("content", ProductDetailFragment.ARG_ITEM_ID);
		}
		super.onSaveInstanceState(outState);
	}

	public void switchContent(Fragment fragment, String tag) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		while (fragmentManager.popBackStackImmediate())
			;

		if (fragment != null) {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.content_frame, fragment, tag);
			// Only ProductDetailFragment is added to the back stack.
			if (!(fragment instanceof HomeFragment)) {
				transaction.addToBackStack(tag);
			}
			transaction.commit();
			contentFragment = fragment;
		}
	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			super.onBackPressed();
		} else if (contentFragment instanceof HomeFragment
				|| fm.getBackStackEntryCount() == 0) {
			finish();
		}
	}

}
