package in.seemasandesh.newspaperapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Object_AppConfig {

	private int versionAppConfig;
	private int versionCat;
	private int theme;
	private float fontFactor;
	
	

	private String serverPath;
	//private String newsImagesPath;
	//private String categoryImagesPath;
	private Context context;
	
	private final String KEY_APP_CONFIG = "appConfig";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor = null;
	
	public Object_AppConfig(Context context){
		
		this.context = context;
		prefs = this.context.getSharedPreferences(KEY_APP_CONFIG, 0);
		editor = prefs.edit();
	}
	
	public int getVersionNoAppConfig() {
		if(prefs != null)
			versionAppConfig = prefs.getInt("appConfig_VersionNoAppConfig",0);
		else
			versionAppConfig = 0;
		
		return versionAppConfig;
	}
	public void setVersionNoAppConfig(int versionAppConfig) {
		if (editor != null) {
			editor.putInt("appConfig_VersionNoAppConfig", versionAppConfig);
			editor.commit();
		}

		this.versionAppConfig = versionAppConfig;
	}
	
	public int getTheme() {
		if(prefs != null)
			theme = prefs.getInt("appConfig_Theme",Custom_ThemeUtil.DEFAULT_THEME);
		else
			theme = 0;
		
		return theme;
	}
	public void setTheme(int theme) {
		if (editor != null) {
			editor.putInt("appConfig_Theme", theme);
			editor.commit();
		}

		this.theme = theme;
	}
	
	public int getRootCatId() {
		if(prefs != null) 
			return prefs.getInt("appConfig_RootCatId",1);
		else
			return 1;
	}
	public void setRootCatId(int rootCatId) {
		if (editor != null) {
			editor.putInt("appConfig_RootCatId", rootCatId);
			editor.commit();
		}
	}
	
	public float getFontFactor() {
		if(prefs != null)
			fontFactor = prefs.getFloat("appConfig_FontFactor",1);
		else
			fontFactor = 1;
		
		return fontFactor;
	}

	public void setFontFactor(float fontFactor) {
		
		
		if (editor != null) {
			editor.putFloat("appConfig_FontFactor", fontFactor);
			editor.commit();
		}

		this.fontFactor = fontFactor;
	}
	
	public int getVersionNoCategory() {
		if(prefs != null)
			versionCat = prefs.getInt("appConfig_VersionNoCategory",0);
		else
			versionCat = 0;
		
		return versionCat;
	}
	public void setVersionNoCategory(int versionCat) {
		if (editor != null) {
			editor.putInt("appConfig_VersionNoCategory", versionCat);
			editor.commit();
		}

		this.versionCat = versionCat;
	}
	
	public String getServerPath() {
		if(prefs != null)
			serverPath = prefs.getString("appConfig_ServerPath", Globals.DEFAULT_APP_SERVER_PATH);
		else
			serverPath = Globals.DEFAULT_APP_SERVER_PATH;

		return serverPath;
	}
	public void setServerPath(String serverPath) {
		if (editor != null) {
			editor.putString("appConfig_ServerPath", serverPath);
			editor.commit();
		}
		
		this.serverPath = serverPath;
	}
	
	/* Sending full path fro server now
	  
	 
	
	
	public String getNewsImagesPath() {
		if(prefs != null)
			newsImagesPath = prefs.getString("appConfig_NewsImagesPath", Globals.DEFAULT_APP_NEWS_IMAGES_PATH);
		else
			newsImagesPath = Globals.DEFAULT_APP_NEWS_IMAGES_PATH;
		
		return newsImagesPath;
	}
	public String getCategoryImagesPath() {
		if(prefs != null)
			categoryImagesPath = prefs.getString("appConfig_CatImagesPath", Globals.DEFAULT_APP_CAT_IMAGES_PATH);
		else
			categoryImagesPath = Globals.DEFAULT_APP_CAT_IMAGES_PATH;
		
		return categoryImagesPath;
	}
	
	public String getNewsImagesFullPath() {
		
		return getServerPath()+getNewsImagesPath();
	}
	
	public String getCategoryImagesFullPath() {
		
		return getServerPath()+getCategoryImagesPath();
	}
	

	public void setNewsImagesPath(String newsImagesPath) {
		if (editor != null) {
			editor.putString("appConfig_NewsImagesPath", newsImagesPath);
			editor.commit();
		}
		
		this.newsImagesPath = newsImagesPath;
	}
	
	
	public void setCategoryImagesPath(String categoryImagesPath) {
		if (editor != null) {
			editor.putString("appConfig_CatImagesPath", categoryImagesPath);
			editor.commit();
		}
		
		this.categoryImagesPath = categoryImagesPath;
	}
		*/
	
	
}
