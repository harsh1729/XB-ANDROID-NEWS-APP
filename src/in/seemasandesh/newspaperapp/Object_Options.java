package in.seemasandesh.newspaperapp;

import android.graphics.drawable.StateListDrawable;

public class Object_Options {

	private String text;
	//private int imageResourceId;
	private StateListDrawable stateDrawable;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	/*
	public int getImageResourceId() {
		return imageResourceId;
	}
	public void setImageResourceId(int imageResourceId) {
		this.imageResourceId = imageResourceId;
	}
	*/
	public StateListDrawable getStateDrawable() {
		return stateDrawable;
	}
	public void setStateDrawable(StateListDrawable stateDrawable) {
		this.stateDrawable = stateDrawable;
	}
	
}
