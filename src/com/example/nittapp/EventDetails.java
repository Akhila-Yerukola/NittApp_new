package com.example.nittapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EventDetails extends Activity {
	
	TextView event_name, event_time, event_date, event_venue, event_desc;
	EventDb events;
	List<HashMap<String,String>> list;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_details);
		list = new ArrayList<HashMap<String,String>>();
		events= new EventDb(getApplicationContext());
		event_name=(TextView)findViewById(R.id.tvName);
		event_time=(TextView)findViewById(R.id.tvTime);
		event_date=(TextView)findViewById(R.id.tvDate);
		event_venue=(TextView)findViewById(R.id.tvVenue);
		event_desc=(TextView)findViewById(R.id.tvDesc);
		Intent intent=getIntent();
		events.open();
		list=events.getEventDetails(Integer.toString(intent.getIntExtra("EventId", 0)));
		events.close();
		event_name.setText(list.get(0).get("name"));
		event_date.setText(list.get(0).get("date"));
		event_time.setText(list.get(0).get("time"));
		event_venue.setText(list.get(0).get("venue"));
		event_desc.setText(list.get(0).get("desc"));
		
	}

	

}
