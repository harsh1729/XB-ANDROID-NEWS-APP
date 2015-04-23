package com.androidopentutorials.imageslideshow.utils;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.seemasandesh.newspaperapp.R;
import com.example.zoomslidersample.ImageSource;
import com.example.zoomslidersample.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageSlideAdapter extends PagerAdapter {
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener imageListener;
	FragmentActivity activity;
	List<Product> products;
	HomeFragment homeFragment;

	 //private static final String[] IMAGES = {"test_1.jpg", "test_2.jpg","test_3.jpg","test_4.jpg","test_5.jpg" };
	private static final int[] IMAGES = {R.drawable.test_1, R.drawable.test_2,R.drawable.test_3,R.drawable.test_4,R.drawable.test_5 };
	 
	public ImageSlideAdapter(FragmentActivity activity, List<Product> products,
			HomeFragment homeFragment) {
		this.activity = activity;
		this.homeFragment = homeFragment;
		this.products = products;
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.no_image_large)
				.showStubImage(R.drawable.loading_image_large)
				.showImageForEmptyUri(R.drawable.no_image_large).cacheInMemory()
				.cacheOnDisc().build();

		imageListener = new ImageDisplayListener();
	}

	@Override
	public int getCount() {
		return  IMAGES.length;//products.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.vp_image, container, false);

		SubsamplingScaleImageView mImageView = (SubsamplingScaleImageView) view
				.findViewById(R.id.image_display);
		
		//TO BE CHANGED
		mImageView.setImage(ImageSource.resource(IMAGES[position]));
		//
		/*
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle arguments = new Bundle();
				Fragment fragment = null;
				Log.d("position adapter", "" + position);
				Product product = (Product) products.get(position);
				arguments.putParcelable("singleProduct", product);

				// Start a new fragment
				fragment = new ProductDetailFragment();
				fragment.setArguments(arguments);

				FragmentTransaction transaction = activity
						.getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.content_frame, fragment,
						ProductDetailFragment.ARG_ITEM_ID);
				transaction.addToBackStack(ProductDetailFragment.ARG_ITEM_ID);
				transaction.commit();
			}
		});
		*/
		
		//imageLoader.displayImage(((Product) products.get(position)).getImageUrl(), mImageView,options, imageListener);
		
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	private static class ImageDisplayListener extends
			SimpleImageLoadingListener {

		//static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				
				/*
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
				*/
				
				SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) view;
				//boolean firstDisplay = !displayedImages.contains(imageUri);
				//if (firstDisplay) {
					imageView.setImage(ImageSource.bitmap(loadedImage));
					//displayedImages.add(imageUri);
				//}
				
			}
		}
	}
	
	/*
	private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
			int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);
 
		byte[] array = os.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}
	*/
}