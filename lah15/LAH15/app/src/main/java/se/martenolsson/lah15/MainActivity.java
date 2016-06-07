package se.martenolsson.lah15;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import se.martenolsson.lah15.classes.TinyDB;

public class MainActivity extends Activity  {

	// Declare Variables
	ListView list;
	ListViewAdapter adapter;
	EditText editsearch;
	DrawerLayout mDrawerLayout;
	LinearLayout sok;

	MediaPlayer myMediaPlayer;
	LinearLayout playBtn;
    TextView playBtnText;

	Boolean searchHasFocus = false;

	ArrayList<String> listofitems = new ArrayList<>();
	TinyDB tinydb;

	ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();

	private static final String TAG_ARTICLE = "article";
	private static final String TAG_TITLE = "title";
	private static final String TAG_MUSIK = "musik";
	private static final String TAG_PLACE = "place";
	private static final String TAG_TEXT = "text";
	private static final String TAG_MP3 = "mp3";
	private static final String TAG_IMAGE = "image";

    /*private static final String TAG_ARTICLE = "payload";
    private static final String TAG_TITLE = "namenormalized";
    private static final String TAG_MUSIK = "category";
    private static final String TAG_PLACE = "city";
    private static final String TAG_TEXT = "description";
    private static final String TAG_MP3 = "songurl";
    private static final String TAG_IMAGE = "image";*/

	JSONArray allmarkers = null;

	Typeface titleText;
    Context mContext;

	//Handle backbtn
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		if(mDrawerLayout.isDrawerVisible(GravityCompat.START)){
			DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
			mDrawerLayout.closeDrawers();
		}else if(searchHasFocus == true){
			editsearch.clearFocus();
			editsearch.getText().clear();
		}else{
			this.finish();
		}
	}
	public void onMenuBtn1(View v){
		Intent intent = new Intent(this, chemeView.class);
		this.startActivity(intent);
	}
	public void onMenuBtn2(View v){
		Intent intent = new Intent(this, FollowView.class);
		this.startActivity(intent);
	}
	public void onMenuBtn3(View v){
		Intent intent = new Intent(this, AboutView.class);
		this.startActivity(intent);
	}

	public void onFollow(View v){
		Intent intent = new Intent(this, FollowView.class);
		this.startActivity(intent);
	}
	public void onChem(View v){
		Intent intent = new Intent(this, chemeView.class);
		this.startActivity(intent);
	}

    public void onPlay(View v){
        if(myMediaPlayer.getCurrentPosition()>0 && !myMediaPlayer.isPlaying()){
            myMediaPlayer.start();
            playBtnText.setText(Html.fromHtml("&#xf1f9;"));
        }
        else if(myMediaPlayer.getCurrentPosition()>0){
            myMediaPlayer.pause();
            playBtnText.setText(Html.fromHtml("&#xf115;"));
        }
    }

	public void onMenu(View v){
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}

    public void onPlaylist(View v){
        Intent intent = new Intent(this, playListView.class);
        this.startActivity(intent);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_main);

        mContext = this;

		//Debugg in chrome
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}
		Typeface geoSans = ((ApplicationController) mContext.getApplicationContext()).geoSans;
		TextView menuText1 = (TextView) findViewById(R.id.menu1Text);
		TextView menuText2 = (TextView) findViewById(R.id.menu2Text);
		TextView menuText3 = (TextView) findViewById(R.id.menu3Text);
		menuText1.setTypeface(geoSans);
		menuText2.setTypeface(geoSans);
		menuText3.setTypeface(geoSans);

		myMediaPlayer = ((ApplicationController) getApplicationContext()).myMediaPlayer;
		playBtn = (LinearLayout) findViewById(R.id.playBtn);
        playBtnText = (TextView) findViewById(R.id.playBtnText);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

		tinydb = new TinyDB(this);
		listofitems = tinydb.getList("listofitems");

		if(listofitems.size()==0){
			new JSONParse().execute();
		}else{
			loadInList();
		}

		File cacheDir = this.getCacheDir();
		File tab1 = new File(cacheDir, "tab1.html");
		File tab2 = new File(cacheDir, "tab2.html");
		File tab3 = new File(cacheDir, "tab3.html");
		File tab4 = new File(cacheDir, "tab4.html");
		if(!tab1.exists()){
			new AlarmReceiver.saveFile(this, "tab1").execute("http://martenolsson.se/lah15/tab1.php?3");
		}
		if(!tab2.exists()){
			new AlarmReceiver.saveFile(this, "tab2").execute("http://martenolsson.se/lah15/tab2.php?3");
		}
		if(!tab3.exists()){
			new AlarmReceiver.saveFile(this, "tab3").execute("http://martenolsson.se/lah15/tab3.php?3");
		}
		if(!tab4.exists()){
			new AlarmReceiver.saveFile(this, "tab4").execute("http://martenolsson.se/lah15/tab4.php?3");
		}

		/*BackgroundTask*/
		Intent alarmIntent = new Intent(this, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		int interval = 1800000;
		//int interval = 30000; //30sek
		manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
		/*end BackgroundTask*/

        titleText = ((ApplicationController) getApplicationContext()).geoSans;
		sok = (LinearLayout) findViewById(R.id.sok);
		editsearch = (EditText) findViewById(R.id.search);
        editsearch.setTypeface(titleText);
		editsearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					searchHasFocus = true;
					//sok.setVisibility(View.GONE);
					list.smoothScrollToPosition(0);

				} else {
					searchHasFocus = false;
					//sok.setVisibility(View.VISIBLE);
				}
			}
		});
		// Capture Text in EditText
		editsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}
		});
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	void loadInList(){
		list = (ListView) findViewById(R.id.listview);

		arraylist.clear();
		list.setAdapter(null);

		Log.e("test", String.valueOf(listofitems.size()));

		for (int i = 0; i < listofitems.size(); i++) {
			String[] listitem = listofitems.get(i).split(";;");

			String title = listitem[0];
			String musik = listitem[1];
			String place = listitem[2];
			String text = listitem[3];
			String mp3 = listitem[4];
			String image = listitem[5];

			WorldPopulation wp = new WorldPopulation(title, musik, text,  place, mp3, image);
			// Binds all strings into an array
			arraylist.add(wp);
		}

		// Pass results to ListViewAdapter Class
		adapter = new ListViewAdapter(this, arraylist, playBtn, playBtnText, myMediaPlayer);

		// Binds the Adapter to the ListView
		list.setAdapter(adapter);
	}

    @Override
    public void onPause() {
        super.onPause();
    }

	@Override
	public void onResume() {
		super.onResume();
		if(adapter!=null) {
			adapter.notifyDataSetChanged();
		}

        if(myMediaPlayer.isPlaying()) {
            playBtn.setVisibility(View.VISIBLE);
            playBtnText.setText(Html.fromHtml("&#xf1f9;"));
        }else{
            playBtn.setVisibility(View.GONE);
        }
	}
	/*Get json for map*/
	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		@Override
		protected void onPreExecute() {
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			//JSONObject json = jParser.getJSONFromUrl("http://lah16.bastardcreative.se/api/artists");
			JSONObject json = jParser.getJSONFromUrl("http://martenolsson.se/lah15/lah15_2.js?9");

			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			if(json!=null){
				try {
					// Getting JSON Array from URL
					allmarkers = json.getJSONArray(TAG_ARTICLE);

					for(int i = 0; i < allmarkers.length(); i++){
						JSONObject c = allmarkers.getJSONObject(i);
						if (c.has(TAG_TITLE)) {
							String title = c.getString(TAG_TITLE);
							String musik = c.getString(TAG_MUSIK);
							String place = c.getString(TAG_PLACE);
							String text = c.getString(TAG_TEXT);
							String mp3 = c.getString(TAG_MP3);
							String image = c.getString(TAG_IMAGE);

							/*if(i==3){
								tinydb.putString("adPos", String.valueOf(i));
								listofitems.add("adPos" + ";;" + "url" + ";;" + "url" + ";;" + "url" + ";;" + "url" + ";;" + "url");
							}else{*/
								listofitems.add(title + ";;" + musik + ";;" + place + ";;" + text + ";;" + mp3 + ";;" + image);
							//}

						}
					}
					Collections.sort(listofitems, String.CASE_INSENSITIVE_ORDER);
					tinydb.putList("listofitems", listofitems);

					loadInList();
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
    /*Get json for map*/


	// Not using options menu in this tutorial
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}*/
}
