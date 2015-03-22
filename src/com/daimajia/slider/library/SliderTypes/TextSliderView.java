package com.daimajia.slider.library.SliderTypes;

import in.seemasandesh.newspaperapp.Custom_ThemeUtil;
import in.seemasandesh.newspaperapp.Globals;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.games.internal.GamesLog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import in.seemasandesh.newspaperapp.R;
/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView {
	Activity context;

	//private String nextHeading = "";
	//private String nextImageURL = "";
	//private String prevHeading = "";
	//private String prevImageURL= "";
	private String imageTag;
	
	public TextSliderView(Activity context) {
		super(context);
		this.context = context;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView() {
		View v = LayoutInflater.from(getContext()).inflate(
				R.layout.slider_type_text, null);

		Point pnt = Globals.getScreenSize(context);

		int containerHeight = pnt.y;
		
		//int containerWidth = pnt.x;
		
		int imgMainHeight = (int) ((int)containerHeight/2.5);
		int sliderDescHeight = (int) (24* context.getResources().getDisplayMetrics().density);//containerHeight/24;
		
		LinearLayout llytSliderDesc = (LinearLayout) v
				.findViewById(R.id.description_layout);
		LinearLayout.LayoutParams lpDesc = (LayoutParams) llytSliderDesc.getLayoutParams();
		lpDesc.height = sliderDescHeight;
		llytSliderDesc.setLayoutParams(lpDesc);
		llytSliderDesc.setBackgroundResource(Custom_ThemeUtil.getThemeColorId(context));
		
		ImageView imgView = (ImageView) v
				.findViewById(R.id.imgNewsDetailBig);
		RelativeLayout.LayoutParams lpImg = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				imgMainHeight);
		imgView.setLayoutParams(lpImg);
		
		imgView.setAdjustViewBounds(true);
		
		TextView txtNewsDesc = (TextView) v.findViewById(R.id.txtNewsDescription);
		txtNewsDesc.setText(getDescription());
		txtNewsDesc.setTextSize(Globals.getAppFontSize_Normal(context));
		
		TextView txtHeading = (TextView) v.findViewById(R.id.txtNewsHeading);
		txtHeading.setText(getHeading());
		txtHeading.setTextSize(Globals.getAppFontSize_Large(context));
		
		TextView txtImagetag = (TextView) v.findViewById(R.id.txtNewsImageTag);
		if(getImageTag() != null && !getImageTag().equals("")){
			txtImagetag.setText(getImageTag());
		}else{
			txtImagetag.setVisibility(View.GONE);
		}
		
		/*
		int imgNextPrevHeightWidth = (int) (containerWidth/5.5);
		 ImageView imgTwoWayArrow = (ImageView) v
				.findViewById(R.id.imgTwoWayArrow);
		
		imgTwoWayArrow.setImageResource(Custom_ThemeUtil.getTwoWayArrowImageId(context));
		boolean nextNotAvailable = true;
		boolean prevNotAvailable = true;
		
		if(!nextHeading.trim().equals("")){
			TextView txtNextHeading = (TextView) v.findViewById(R.id.txtNextHeading);
			txtNextHeading.setText(nextHeading);
			nextNotAvailable = false;
		}
		
		if(!prevHeading.trim().equals("")){
			TextView txtPrevHeading = (TextView) v.findViewById(R.id.txtPrevHeading);
			txtPrevHeading.setText(prevHeading);
			prevNotAvailable = false;
		}
		
		
		
		
		if(!prevImageURL.trim().equals("")){
			ImageView imgPrev = (ImageView)v.findViewById(R.id.imgPrevNews);
			LinearLayout.LayoutParams lpImgPrev = (LayoutParams) imgPrev.getLayoutParams();
			lpImgPrev.height = imgNextPrevHeightWidth;
			lpImgPrev.width = imgNextPrevHeightWidth;
			imgPrev.setLayoutParams(lpImgPrev);
			
			setImage(imgPrev, prevImageURL);
			prevNotAvailable = false;
		}
		
		if(!nextImageURL.trim().equals("")){
			ImageView imgNext = (ImageView)v.findViewById(R.id.imgNextNews);
			LinearLayout.LayoutParams lpImgNext = (LayoutParams) imgNext.getLayoutParams();
			lpImgNext.height = imgNextPrevHeightWidth;
			lpImgNext.width = imgNextPrevHeightWidth;
			imgNext.setLayoutParams(lpImgNext);
			
			setImage(imgNext, nextImageURL);
			nextNotAvailable = false;
		}
		
		if(nextNotAvailable && prevNotAvailable){
			
			LinearLayout ll = (LinearLayout)v.findViewById(R.id.llytNextPrevNews);
			ll.setVisibility(View.GONE);
		}else{
		
		if(nextNotAvailable){
			LinearLayout ll = (LinearLayout)v.findViewById(R.id.llytNextNews);
			ll.setVisibility(View.INVISIBLE);
		}
		
		if(prevNotAvailable){
			LinearLayout ll = (LinearLayout)v.findViewById(R.id.llytPrevNews);
			ll.setVisibility(View.INVISIBLE);
		}
		}
		*/
		

		empty(R.drawable.loading_image_large);
		error(R.drawable.no_image_large);
		bindEventAndShow(v, imgView);
		return v;
	}


	protected void setImage(ImageView targetImageView ,String mUrl){
		
		if(mUrl == null || mUrl.trim().isEmpty()){
			targetImageView.setVisibility(View.GONE);
			return ;
		}
		
		Globals.loadImageIntoImageView(targetImageView, mUrl, mContext, R.drawable.loading_image_large, R.drawable.no_image_large);
		/*
		Picasso p = Picasso.with(context);

		RequestCreator rq = null;
		
		rq = p.load(mUrl);

		rq.placeholder(R.drawable.loading_image_large);
		rq.error(R.drawable.no_image_large);
		
		rq.fit().centerCrop();
		

		rq.into(targetImageView, new Callback() {
			@Override
			public void onSuccess() {
				
			}

			@Override
			public void onError() {
				
			}
		});
		*/
	}
	/*
	public String getNextHeading() {
		return nextHeading;
	}

	public void setNextHeading(String nextHeading) {
		this.nextHeading = nextHeading;
	}


	public String getNextImageURL() {
		return nextImageURL;
	}

	public void setNextImageURL(String nextImageURL) {
		this.nextImageURL = nextImageURL;
	}

	public String getPrevImageURL() {
		return prevImageURL;
	}

	public void setPrevImageURL(String prevImageURL) {
		this.prevImageURL = prevImageURL;
	}

	public String getPrevHeading() {
		return prevHeading;
	}

	public void setPrevHeading(String prevHeading) {
		this.prevHeading = prevHeading;
	}
*/
	public String getImageTag() {
		return imageTag;
	}

	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
	}
	
}
