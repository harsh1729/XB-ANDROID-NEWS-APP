package com.xercesblue.newspaperapp;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	// private static final String TAG = "GCM Tutorial::Service";

	// Use your PROJECT ID from Google API into SENDER_ID
	public static final String SENDER_ID = "430241764032";
	
	public static String pushMessageText = "";
	public static String pushMessageHeader = "";
	public static long pushNewsId = 0;
	private final int KEY_HOME_SCREEN = 0;
	private final int KEY_DOWNLOAD_SCREEN = 1;
	private final int KEY_CURRENTGK_SCREEN = 2;
	private final int KEY_CONTACTUS_SCREEN = 3;
	private final int KEY_REGISTER_SCREEN = 4;
	private final int KEY_EXAMALERT_SCREEN= 5;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {

		Log.i(TAG, "onRegistered: registrationId=" + registrationId);
		System.out.println("registration Id : " + registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {

		Log.i(TAG, "onUnregistered: registrationId=" + registrationId);
		System.out.println("registration Id : " + registrationId);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMessage(Context context, Intent data) {
		try {
			// Message from PHP server
			System.out.println("Inside onMessage" );
			String heading = Globals.APP_NAME;
			pushMessageText = Html.fromHtml( data.getStringExtra("content")).toString();
			pushMessageHeader = Html.fromHtml( data.getStringExtra("heading")).toString();
			
			
			int screenId = 0;
			try {
				screenId=Integer.parseInt(data.getStringExtra("ScreenId"));
				pushNewsId = Long.parseLong(Html.fromHtml( data.getStringExtra("newsId")).toString());
			}catch(NumberFormatException ex){
				
			}

			System.out.println("Message recieved is : " + pushMessageText + " ID is "+screenId);
			Class<?> cls = null;
			cls = Activity_Home.class;
			switch (screenId) {
			case KEY_HOME_SCREEN:
				cls = Activity_Home.class;
				break;
			case KEY_DOWNLOAD_SCREEN:
				//cls = Activity_Settings_DownloadMore.class;
				break;
			case KEY_CURRENTGK_SCREEN:
				//cls = Activity_Current_GK_Type_Select.class;
				break;
			case KEY_CONTACTUS_SCREEN:
				//cls = Activity_Settings_ContactUs.class;
				break;
			case KEY_REGISTER_SCREEN:

				 SharedPreferences prefs = this.getSharedPreferences("appregister", 0);				 
				 if(prefs.getBoolean("register_done", false)){
					 cls = Activity_Home.class;
				 }else{
					// cls = Activity_Settings_Registration.class;
				 }
				break;
			case KEY_EXAMALERT_SCREEN:
				//cls = Activity_ExamAlert.class;
				break;

			default:
				cls = Activity_Home.class;
				break;
			}

			// Open a new activity called GCMMessageView
			Activity_Home.comingFromPushMessage = true;
			Intent intent = new Intent(this, cls);

			if (android.os.Build.VERSION.SDK_INT < 11) {
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
						this).setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle(heading).setContentText(pushMessageHeader);
				// Creates an explicit intent for an Activity in your app

				// The stack builder object will contain an artificial back
				// stack for the
				// started Activity.
				// This ensures that navigating backward from the Activity leads
				// out of
				// your application to the Home screen.
				TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
				// Adds the back stack for the Intent (but not the Intent
				// itself)
				stackBuilder.addParentStack(cls);
				// Adds the Intent that starts the Activity to the top of the
				// stack
				stackBuilder.addNextIntent(intent);
				PendingIntent resultPendingIntent = stackBuilder
						.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setContentIntent(resultPendingIntent);
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(999, mBuilder.build());

				return;
			}

			// Starts the activity on notification click
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			// Create the notification with a notification builder
			Notification notification;

			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				notification = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setWhen(System.currentTimeMillis())
				.setContentTitle(heading).setContentText(pushMessageHeader)
				.setContentIntent(pIntent)
				// .setContentInfo(
				// String.valueOf(++MainActivity.numOfNotifications) )
				.setLights(0xFFFF0000, 500, 500).getNotification();
			} else {
				notification = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setWhen(System.currentTimeMillis())
				.setContentTitle("Bank PO / Clerical")
				.setContentText(pushMessageHeader).setContentIntent(pIntent)
				// .setContentInfo(
				// String.valueOf(++MainActivity.numOfNotifications) )
				.setLights(0xFFFF0000, 500, 500).build();
			}

			// Remove the notification on click
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// manager.notify(R.string.app_name, notification);

			{
				// Wake Android Device when notification received
				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				final PowerManager.WakeLock mWakelock = pm.newWakeLock(
						PowerManager.FULL_WAKE_LOCK
						| PowerManager.ACQUIRE_CAUSES_WAKEUP,
						"GCM_PUSH");
				mWakelock.acquire();

				// Play default notification sound
				// notification.defaults |= Notification.DEFAULT_SOUND;

				Uri notificationuri = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				Ringtone ringer = RingtoneManager.getRingtone(
						getApplicationContext(), notificationuri);
				ringer.play();

				// Vibrate if vibrate is enabled
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				// manager.notify( (int)Calendar.getInstance().getTimeInMillis()
				// , notification);
				manager.notify(0, notification);

				// Timer before putting Android Device to sleep mode.
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					public void run() {
						mWakelock.release();
					}
				};
				timer.schedule(task, 5000);
			}
		} catch (Exception ex) {
			Log.i("HARSH", "Exception in GCM");
		}
	}

	@Override
	protected void onError(Context arg0, String errorId) {

		Log.e(TAG, "onError: errorId=" + errorId);
		System.out.println("error receiving: " + errorId);
	}

}