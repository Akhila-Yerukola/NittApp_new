package com.example.nittapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TempActivity extends Activity {

	Button b;
	Intent intent;
	Boolean work;
	NotificationDB notif;
	String eName, eLoc, eTime, eDate;
	static int c = 0;
	List events = new ArrayList();
	int length, hr, min, date, month, year;
	public Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp);

		Intent event = getIntent();
		Log.e("Activity", "Called!");
		eName = event.getStringExtra("text");
		
		notif = new NotificationDB(TempActivity.this);

		// name.setText(eName);
		
		
		 hr = Integer.parseInt(eTime.substring(0, 2)); 
		 min = Integer.parseInt(eTime.substring(3, 5)); 
		 date = Integer.parseInt(eDate.substring(0,2)); 
		 month = Integer.parseInt(eDate.substring(3,5)); 
		 year =  Integer.parseInt(eDate.substring(6,10));
		 
		/*hr = 9;
		min = 32;
		date = 25;
		month = 5;
		year = 2014;*/

		b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(TempActivity.this, MainActivity.class);

				work = true;
				try {
					notif.open();
					notif.createEntry("Event 1", "25.05.2014", "11:25", "LHC");
					notif.close();

				} catch (Exception e) {
					work = false;
					Toast.makeText(TempActivity.this, "Event Additon failed!",
							Toast.LENGTH_SHORT).show();
				}

				if (work == true) {

					Log.e("Entering", "if");
					notif.open();
					events = notif.getData();
					notif.close();
					length = events.size();

					Log.e("Events:", events.toString());
					Log.e("hour", Integer.toString(hr - 1));
					Log.e("min", Integer.toString(min));
					Log.e("date", Integer.toString(date));
					Log.e("month", Integer.toString(month));
					Log.e("year", Integer.toString(year));

					
					calendar = Calendar.getInstance();
					calendar.set(Calendar.DAY_OF_MONTH, date);
					calendar.set(Calendar.MONTH, month - 1);
					calendar.set(Calendar.YEAR, year);
					// calendar.set(Calendar.HOUR_OF_DAY, 11);
					calendar.set(Calendar.HOUR_OF_DAY, hr - 1);
					calendar.set(Calendar.MINUTE, min);
					calendar.set(Calendar.SECOND, 0);

					Intent intent = new Intent(TempActivity.this,
							AlarmReceiver.class);
					intent.putExtra("name", eName);
					intent.putExtra("venue", eLoc);
					intent.putExtra("date", eDate);
					intent.putExtra("time", eTime);

					PendingIntent pendingIntent = PendingIntent.getBroadcast(
							TempActivity.this, c, intent, 0);
					AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis(), pendingIntent);
					Toast.makeText(TempActivity.this, "Event Added!",
							Toast.LENGTH_SHORT).show();
					c++;
					
				}

				// Should add notification alarm setter here!
				startActivity(i);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.temp, menu);
		return true;
	}

}
