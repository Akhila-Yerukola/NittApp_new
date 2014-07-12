package com.example.nittapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class NotiReceiver extends BroadcastReceiver{

	static int c = 0;
	int pos;
	List<HashMap<String, String>> events;
	String eName, eTime, eDate, eLoc, eDesc;

	@Override
	public void onReceive(Context context, Intent event) {
		// TODO Auto-generated method stub
		Log.e("reached", "msg");
		eName = event.getStringExtra("name");
		eLoc = event.getStringExtra("venue");

		NotificationCompat.Builder noti = new NotificationCompat.Builder(
				context).setContentTitle(eName)
				.setContentText("Venue : " + eLoc).setSmallIcon(R.drawable.ic_launcher);

		Intent resultIntent = new Intent(context, TempActivity.class);
		//resultIntent.putExtra("lat", lat);
		//resultIntent.putExtra("lng", lng);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		stackBuilder.addParentStack(TempActivity.class);

		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_ONE_SHOT);
		noti.setContentIntent(resultPendingIntent);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		// noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(c, noti.build());
		c++;

	}
}
