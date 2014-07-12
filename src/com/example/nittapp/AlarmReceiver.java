package com.example.nittapp;

import java.util.Calendar;

import android.R.string;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{

	NotificationManager nm;
	static int i=0;
	String eName,eDate,eLoc,eTime;
	int hr,min,date,month,year;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Log.e("Activity", "Called!");
		eName = intent.getExtras().getString("name");
		eTime = intent.getExtras().getString("time");
		eDate = intent.getExtras().getString("date");
		eLoc = intent.getExtras().getString("venue");
		
		hr = Integer.parseInt(eTime.substring(0, 2)); 
		min = Integer.parseInt(eTime.substring(3, 5)); 
		date = Integer.parseInt(eDate.substring(0,2)); 
		month = Integer.parseInt(eDate.substring(3,5)); 
		year = Integer.parseInt(eDate.substring(6,10));
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, date);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR_OF_DAY, hr - 1);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);
		
		Intent myIntent = new Intent(context,MainActivity.class);
		//context.startService(myIntent);
		
		nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
		@SuppressWarnings("deprecation")
		Notification notif = new Notification(R.drawable.ic_launcher,"Test"+i,calendar.getTimeInMillis());
		notif.setLatestEventInfo(context, eName, eTime + "  "+eLoc, contentIntent);
		nm.notify(i,notif);
		i++;
		
		
	}

}
