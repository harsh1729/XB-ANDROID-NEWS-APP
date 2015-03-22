package in.seemasandesh.newspaperapp;


import android.os.Bundle;

public class Activity_TermsOfUSe extends Activity_Parent {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_of_use);
		super.initSuper();
	}
}
