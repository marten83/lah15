package se.martenolsson.lah15;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import se.martenolsson.lah15.classes.TinyDB;

public class FollowView extends SwipeBackActivity {

	// Declare Variables
	ListView list;
	FollowListViewAdapter adapter;
	Toolbar toolbar;

	ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();

	ArrayList<String> followList = new ArrayList<>();
	TinyDB tinydb;

	private static final String TAG_ARTICLE = "article";
	private static final String TAG_TITLE = "title";
	private static final String TAG_MUSIK = "musik";
	private static final String TAG_PLACE = "place";
	private static final String TAG_TEXT = "text";
	private static final String TAG_MP3 = "mp3";
	private static final String TAG_IMAGE = "image";
	JSONArray allmarkers = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fallowlistview);

		tinydb = new TinyDB(this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);

		Typeface geoSans = ((ApplicationController) getApplicationContext()).geoSans;
		TextView headerText = (TextView) findViewById(R.id.headerText);
		headerText.setTypeface(geoSans);

        TextView closeView = (TextView) findViewById(R.id.closeView);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

		loadInList();

	}

	void loadInList(){
		list = (ListView) findViewById(R.id.listview);
		arraylist.clear();
		list.setAdapter(null);
		tinydb = new TinyDB(this);
		followList = tinydb.getList("followList");

		for(int i = 0; i < followList.size(); i++) {

			String[] listitem = followList.get(i).split(";;");
			String title = listitem[0];
			String musik = listitem[1];
			String place = listitem[2];
			String text = listitem[3];
			String mp3 = listitem[4];
			String image = listitem[5];

			WorldPopulation wp = new WorldPopulation(title, musik, text,  place, mp3, image);
			arraylist.add(wp);
		}

		// Pass results to ListViewAdapter Class
		adapter = new FollowListViewAdapter(this, arraylist);

		// Binds the Adapter to the ListView
		list.setAdapter(adapter);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			//Top Icon
			case android.R.id.home:
				this.finish();
				break;

		}
		return false;
	}
}
