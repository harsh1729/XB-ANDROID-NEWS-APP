package in.seemasandesh.newspaperapp;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Custom_AdapterCatHome extends BaseExpandableListAdapter  {
	
	public ArrayList<Integer> Current_catId = new ArrayList<Integer>();
	private final ArrayList<Object_Category> listNewsCategory;
	public LayoutInflater inflater;
	public Activity_Home activity;
	
	private final int DEFAULT_PADDING_ROW = 8;
	private  int rowHeight = 35;
	
	public Custom_AdapterCatHome(Activity_Home act,
			ArrayList<Object_Category> list) {
		activity = act;
		listNewsCategory = list;
		inflater = act.getLayoutInflater();
		
		rowHeight = Globals.getScreenSize(activity).y/16;
	}

	
	@Override
	public Object getChild(int groupPosition, int childPosition) {

		if (listNewsCategory != null && listNewsCategory.size() > groupPosition) 
			if (listNewsCategory.get(groupPosition) != null
					&& listNewsCategory.get(groupPosition)
							.getListChildCategory() != null)
				if (listNewsCategory.get(groupPosition).getListChildCategory()
						.size() > childPosition)
					return listNewsCategory.get(groupPosition)
							.getListChildCategory().get(childPosition);

		return null;
	}

	

	public void updateNewNewsForCategory(int catId) {

		activity.toggle();
		//Globals.showSpinnerNews(activity.mDialog, activity);
		activity.getNewsDataFromServer(catId,Globals.CALLTYPE_FRESH, 0,false,Globals.FINAL_NEWS_LIMIT_FIRST_CALL);
		
		System.out.println("child id : " + catId);

	}

	
	@Override
	public int getChildrenCount(int groupPosition) {

		// System..out.println("getChild count Called!");

		if (listNewsCategory != null && listNewsCategory.size() > groupPosition)
			if (listNewsCategory.get(groupPosition).getListChildCategory() != null)
				return listNewsCategory.get(groupPosition)
						.getListChildCategory().size();

		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {

		// System..out.println("get group Called!");
		if (listNewsCategory != null && listNewsCategory.size() > groupPosition)
			return listNewsCategory.get(groupPosition);

		return null;
	}

	@Override
	public int getGroupCount() {

		if (listNewsCategory != null)
			return listNewsCategory.size();

		return 0;
	}

	@Override
	public long getGroupId(int groupPos) {

		if (listNewsCategory != null && listNewsCategory.size() > groupPos)
			if (listNewsCategory.get(groupPos) != null)
				return listNewsCategory.get(groupPos).getId();
		return 0;
	}

	
	@Override
	public void onGroupCollapsed(int groupPosition) {

		if (getChildrenCount(groupPosition) < 1) {
			Object_Category group = (Object_Category) getGroup(groupPosition);
			final int group_cat_id = group.getId();

			updateNewNewsForCategory(group_cat_id);
			Log.i("HARSH", "onGroupExpanded");
		}

		super.onGroupCollapsed(groupPosition);
		
	}

	@Override
	public void onGroupExpanded(int groupPosition) {

		if (getChildrenCount(groupPosition) < 1) {
			Object_Category cat = (Object_Category) getGroup(groupPosition);

			final int group_cat_id = cat.getId();

			updateNewNewsForCategory(group_cat_id);

			Log.i("HARSH", "onGroupExpanded");
		}

		super.onGroupExpanded(groupPosition);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_item_categories, null);
		}
		
		final Object_Category childCat = (Object_Category) getChild(groupPosition, childPosition);
		
		if(childCat==null){
			
			return convertView;
		}
		

		ImageView imgViewChild = (ImageView) convertView
				.findViewById(R.id.img_GroupCatgeory);
		
		//Globals.loadImageIntoImageView(imgViewChild, childCat.getImageName(), activity, R.drawable.loading_image_small, R.drawable.no_image_small);
		

		TextView tv = (TextView) convertView.findViewById(R.id.txt_GroupText);
		tv.setText(childCat.getName());
		tv.getLayoutParams().height = rowHeight;
		
		ImageView indicator = ((ImageView) convertView
				.findViewById(R.id.img_GroupIndicator));
		indicator.setImageResource(R.color.app_transparent);
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateNewNewsForCategory(childCat.getId());
			}
		});
		
		convertView.setPadding(DEFAULT_PADDING_ROW * 4, DEFAULT_PADDING_ROW, DEFAULT_PADDING_ROW, DEFAULT_PADDING_ROW);
		
		return convertView;
	}
	

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_item_categories, null);
		}

		
		Object obj = getGroup(groupPosition);
		Object_Category cat;

		if (obj != null)
			cat = (Object_Category) obj;
		else
			return convertView;

		Log.i("HARSH", "getGroupView "+cat.getName() +" Position" + groupPosition);
		TextView tv = (TextView) convertView.findViewById(R.id.txt_GroupText);
		tv.setText(cat.getName());
		
		tv.getLayoutParams().height = rowHeight;

		ImageView img = (ImageView) convertView
				.findViewById(R.id.img_GroupCatgeory);
		
		Globals.loadImageIntoImageView(img, cat.getImageName(), activity, R.drawable.loading_image_small, R.drawable.no_image_small);
		if (getChildrenCount(groupPosition) > 0) {
			ImageView indicator = ((ImageView) convertView
					.findViewById(R.id.img_GroupIndicator));
			//indicator.setImageResource(isExpanded ? Custom_ThemeUtil.getExpandImageId(activity): Custom_ThemeUtil.getNextImageId(activity));
			indicator.setImageDrawable(isExpanded ? Custom_ThemeUtil.getDrawableExpandImage(activity) : Custom_ThemeUtil.getDrawableNextImage(activity));
		
		} else {
			ImageView indicator = ((ImageView) convertView
					.findViewById(R.id.img_GroupIndicator));
			indicator.setImageResource(R.color.app_transparent);

		}

	
		convertView.setPadding(DEFAULT_PADDING_ROW , DEFAULT_PADDING_ROW, DEFAULT_PADDING_ROW, DEFAULT_PADDING_ROW);
		Log.i("HARSH","getGroupView " + groupPosition);
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	@Override
	public long getChildId(int groupPos, int childPos) {
		if (listNewsCategory != null && listNewsCategory.size() > groupPos)
			if (listNewsCategory.get(groupPos).getListChildCategory() != null
					&& listNewsCategory.get(groupPos).getListChildCategory()
							.size() > childPos)
				if (listNewsCategory.get(groupPos).getListChildCategory()
						.get(childPos) != null)
					return listNewsCategory.get(groupPos)
							.getListChildCategory().get(childPos).getId();

		return 0;
	}

	

}
