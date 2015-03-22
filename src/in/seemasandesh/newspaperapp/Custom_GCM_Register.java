package in.seemasandesh.newspaperapp;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.google.android.gcm.GCMRegistrar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;



public class Custom_GCM_Register {

	Thread_RegisterOnPhpServer thread_RegisterOnPhpServer ;
	Custom_ConnectionDetector cd;
	Activity context;
	public Custom_GCM_Register(Activity act) {
		context = act;
		registerDeviceOnServer();
	}
	
	
	private void registerDeviceOnServer(){
		Log.i("HARSH","registerDeviceOnServer");
		try
        {
        	GCMRegistrar.checkDevice(context);
        	GCMRegistrar.checkManifest(context);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        

    	cd = new Custom_ConnectionDetector(context);
        
        // Check if Internet present
        if (cd.isConnectingToInternet())
        {
        	String regId = GCMRegistrar.getRegistrationId(context);
        	Log.i("HARSH","At Line 70");
        	if (regId.equals("")) 
        	{
        		//Log.i("HARSH","At Line 73");
        		//Toast.makeText(context, "GCM request send!", Toast.LENGTH_SHORT).show();
        		GCMRegistrar.register(context, GCMIntentService.SENDER_ID);
        		regId = GCMRegistrar.getRegistrationId(context);
            } 
        	Globals.GCM_REG_ID = regId;
        	if(!GCMRegistrar.isRegisteredOnServer(context))
        	{
        		//Log.i("HARSH","At Line 80");
        		int MAX_LOOP = 2;
        		int counter =1;
        		//  device is not registered on php server. First register it on php server and then call GCMRegistrar.setRegisteredOnServer(context, true)
        		do{
        			Log.i("HARSH","Do "+counter);
        			if(!regId.trim().equals("")){
        				Log.i("HARSH"," REGISTERING WITH ID "+regId);
        				thread_RegisterOnPhpServer = new Thread_RegisterOnPhpServer(Globals.APP_ID,regId,context);
        				thread_RegisterOnPhpServer.start();
        				break;
        			}
        			try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        			counter++;
        		}while(  counter < MAX_LOOP);
        		}
        } 
        else
        { 
        	//Toast.makeText(context, "No INTERNET Connection", Toast.LENGTH_SHORT).show();
        	Log.i("HARSH","No INTERNET Connection");
        }
	}
	
	class Thread_RegisterOnPhpServer extends Thread
    {
    	Context context;
    	private String regId ;
    	private int AppId;
    	public Thread_RegisterOnPhpServer(int AppId,String regId,Context context)
    	{
    		this.regId = regId;
    		this.context = context;
    		this.AppId = AppId;
    		Log.i("HARSH","In RegisterOnPhpServer constructor");
    	}
    	

    	AndroidHttpClient httpClient;
		@SuppressLint("NewApi")
		public void run()
    	{
    		try 
    		{
    			httpClient =  AndroidHttpClient.newInstance("Android");
    			String url = Globals.getURLPushNotification(regId,AppId);
    			
    			HttpGet httpGet = new HttpGet(url);
    			Log.i("HARSH","ADDress is : "+url);			
    			
    			HttpResponse response = httpClient.execute(httpGet);
    			String data = Globals.convertInputStreamToString(response.getEntity().getContent());
    			if(data.equalsIgnoreCase("registered"))
    			{
    				GCMRegistrar.setRegisteredOnServer(context, true);
    				Log.i("HARSH","Recieved registered!");
    				
    			}
    			else if(data.equalsIgnoreCase("false"))
    			{
    				GCMRegistrar.setRegisteredOnServer(context, false);
    				Log.i("HARSH","Recieved false!");					
    			}
    			
    			
    			
    			}
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    		finally{
    			
    			if(httpClient != null ){
    				httpClient.close();
    				httpClient = null;
    			}
    		}
    		
    	}
		
    }
}
