package in.seemasandesh.newspaperapp;


import android.os.Bundle;
import android.webkit.WebView;

public class Activity_TermsOfUSe extends Activity_Parent {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_of_use);
		super.initSuper();
		
		WebView browser = (WebView) findViewById(R.id.webview);
		browser.loadUrl("http://xercesblue.in/privacy_policy/seema_sandesh.html");
	}
}
