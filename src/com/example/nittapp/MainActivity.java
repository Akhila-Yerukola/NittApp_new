package com.example.nittapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends Activity {

	ListView eventsList;
	ListView updatesList;
	static int orgid, clusterid;
	List<String> listForEvents, ListForUpdates;
	int lengthOfUpdates;
	static int page_no = 1;
	LoadDataUpdates updates;
	LoadDataEvents event;
	ArrayAdapter<String> adapter1;
	EventDb events;

	// temp variables
	ArrayList<String> venue = new ArrayList<String>();
	ArrayList<String> edesc = new ArrayList<String>();
	ArrayList<String> ename = new ArrayList<String>();
	ArrayList<String> edate = new ArrayList<String>();
	ArrayList<String> etime = new ArrayList<String>();
	ArrayList<String> evenue = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listForEvents = new ArrayList<String>();
		ListForUpdates = new ArrayList<String>();
		updates = new LoadDataUpdates();
		event = new LoadDataEvents();
		events = new EventDb(MainActivity.this);

		TabHost th = (TabHost) findViewById(R.id.tabhost);
		th.setup();
		TabSpec specs = th.newTabSpec("tag1");
		specs.setContent(R.id.tab1);
		specs.setIndicator("Events");
		th.addTab(specs);
		specs = th.newTabSpec("tag2");
		specs.setContent(R.id.tab2);
		specs.setIndicator("Updates");
		th.addTab(specs);

		eventsList = (ListView) findViewById(R.id.list1);
		updatesList = (ListView) findViewById(R.id.list2);

		setList();
		event.execute("http://10.0.2.2:8080");
		// updates.execute("http://10.0.2.2:8080");
		adapter1 = new MyCustomAdapter(MainActivity.this, R.layout.row, ename);
		// updatesList.setAdapter(adapter1);

		eventsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (page_no == 1){
					orgid = position + 1;
					page_no++;
				}
				else if (page_no == 2) {
					events.open();
					Log.e("name of clicked position",
							listForEvents.get(position));
					clusterid = events.getClusterId(listForEvents.get(position));
					events.close();
					page_no++;
				}
				else{
					events.open();
					Intent intent=new Intent(MainActivity.this, EventDetails.class);
					intent.putExtra("EventId",events.getEventId(listForEvents.get(position)) );
					startActivity(intent);
					events.close();
				}

				
				setList();

			}
		});
	}

	private void setList() {
		// TODO Auto-generated method stub
		events.open();
		switch (page_no) {
		case 1:
			listForEvents = events.getOrgList();
			eventsList.setAdapter(new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_list_item_1, listForEvents));
			break;

		case 2:
			listForEvents = events.getClusters(orgid + "");
			eventsList.setAdapter(new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_list_item_1, listForEvents));
			break;
		case 3:
			listForEvents = events.getEvents(clusterid + "");
			eventsList.setAdapter(new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_list_item_1, listForEvents));
			Log.e("Setting events", "yayy");
			break;

		}
		events.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// Things to Do

			page_no--;
			if (page_no == 0) {
				page_no = 1;
				finish();
			}

			setList();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class MyCustomAdapter extends ArrayAdapter<String> {
		public int cnt = 0;

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<String> ename) {
			super(context, textViewResourceId, ename);
			Log.d("ADAPT", "Constructing");

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Inflate the layout, mainlvitem.xml, in each row.
			LayoutInflater inflater = MainActivity.this.getLayoutInflater();
			View row1 = inflater.inflate(R.layout.row, parent, false);
			// Log.d("ADAPT", Integer.toString(cnt++));

			TextView item = (TextView) row1.findViewById(R.id.Name);
			item.setText(ename.get(position));
			//
			TextView item1 = (TextView) row1.findViewById(R.id.By);
			item1.setText(edate.get(position));
			//

			TextView item3 = (TextView) row1.findViewById(R.id.Venue);
			item3.setText(evenue.get(position));

			return row1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ename.size();
		}
	}

	public class LoadDataUpdates extends AsyncTask<String, Void, String> {

		ArrayList<String> venue = new ArrayList<String>();
		ArrayList<String> edesc = new ArrayList<String>();
		ArrayList<String> ename = new ArrayList<String>();
		ArrayList<String> edate = new ArrayList<String>();
		ArrayList<String> etime = new ArrayList<String>();
		ArrayList<String> evenue = new ArrayList<String>();

		private static final String TAG_NAME = "name";
		private static final String TAG_DATE = "date";
		private static final String TAG_TIME = "time";
		private static final String TAG_DESC = "desc";
		private static final String TAG_VENUE = "venue";
		/*
		 * private static final String TAG_LAT = "lat"; private static final
		 * String TAG_PIC = "pic"; private static final String TAG_LNG = "lng";
		 * private static final String TAG_EID = "eid"; private static final
		 * String TAG_UID = "uid";
		 */
		JSONArray contacts = null;

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		protected void onPreExecute(String json) {
			// TODO Auto-generated method stub

			System.out.println("pre-execute");

		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			String url = urls[0];
			try {
				// defaultHttpClient

				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				System.out.println("sb : " + sb);
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("post-execute");
			try {
				System.out.println("post-execute");
				jObj = new JSONObject(json);
				contacts = jObj.getJSONArray("event");
				int i;
				for (i = 0; i < contacts.length(); i++) {
					JSONObject obj = contacts.getJSONObject(i);
					String Ename = obj.getString(TAG_NAME);
					String Edate = obj.getString(TAG_DATE);
					String Etime = obj.getString(TAG_TIME);
					String Edesc = obj.getString(TAG_DESC);
					String Evenue = obj.getString(TAG_VENUE);

					ename.add(i, Ename);
					edate.add(i, Edate);
					etime.add(i, Etime);
					edesc.add(i, Edesc);
					evenue.add(i, Evenue);

					Log.e("name", ename.get(i));
				}

			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

			Log.e("hema ", "akhila");

		}

	}

	public class LoadDataEvents extends AsyncTask<String, Void, String> {

		ArrayList<String> venue = new ArrayList<String>();
		ArrayList<String> edesc = new ArrayList<String>();
		ArrayList<String> ename = new ArrayList<String>();
		ArrayList<String> edate = new ArrayList<String>();
		ArrayList<String> etime = new ArrayList<String>();
		ArrayList<String> evenue = new ArrayList<String>();

		private static final String TAG_NAME = "name";
		private static final String TAG_UPDATE = "update";
		private static final String TAG_DATE = "date";
		private static final String TAG_TIME = "time";
		private static final String TAG_DESC = "desc";
		private static final String TAG_VENUE = "venue";
		private static final String TAG_DELETE = "delete";
		private static final String TAG_ORGANISATIONID = "orgid";
		private static final String TAG_EVENTID = "eventid";
		private static final String TAG_CLUSTERID = "clusterid";
		private static final String TAG_CLUSTERNAME = "clustername";

		/*
		 * private static final String TAG_LAT = "lat"; private static final
		 * String TAG_PIC = "pic"; private static final String TAG_LNG = "lng";
		 * private static final String TAG_EID = "eid"; private static final
		 * String TAG_UID = "uid";
		 */
		JSONArray contacts = null;

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		protected void onPreExecute(String json) {
			// TODO Auto-generated method stub

			System.out.println("pre-execute");

		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			String url = urls[0];
			try {
				// defaultHttpClient

				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				System.out.println("sb : " + sb);
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("post-execute");
			try {
				events.open();
				System.out.println("post-execute");
				jObj = new JSONObject(json);
				contacts = jObj.getJSONArray("event");
				int i;
				for (i = 0; i < contacts.length(); i++) {
					JSONObject obj = contacts.getJSONObject(i);
					String Ename = obj.getString(TAG_NAME);
					String Edate = obj.getString(TAG_DATE);
					String Etime = obj.getString(TAG_TIME);
					String Edesc = obj.getString(TAG_DESC);
					String Evenue = obj.getString(TAG_VENUE);
					String Eorg = obj.getString(TAG_ORGANISATIONID);
					String Eclusterid = obj.getString(TAG_CLUSTERID);
					String Eclustername = obj.getString(TAG_CLUSTERNAME);
					String Eeventid = obj.getString(TAG_EVENTID);
					String deleted = obj.getString(TAG_DELETE);
					String update = obj.getString(TAG_UPDATE);

					if (!deleted.equals("1")) {
						if (update.equals("1")) {
							events.delete(Eeventid);
						}
						if (events.checkId(Eeventid) == 0) {
							events.createEntry(Ename, Edate, Etime, Edesc,
									Evenue, Eorg, Eeventid, Eclusterid,
									Eclustername, deleted, update);

						}
					} else
						events.delete(Eeventid);

				}
				events.close();

			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

			setList();
		}

	}

}
