package se.martenolsson.lah15;

import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParsePush;

import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import se.martenolsson.lah15.classes.ObservableScrollView;
import se.martenolsson.lah15.classes.TinyDB;

public class SingleItemView extends ActionBarActivity implements ObservableScrollView.OnScrollChangedListener {
	// Declare Variables
	TextView txtTitle;
	TextView txtText;
	TextView txtMusik;
	TextView txtPlace;
	ImageView largeImage;
	ImageView smallImage;

	private ObservableScrollView mScrollView;

	String title;
	String musik;
	String place;
	String text;
	String mp3;
	String image;

	float density;

	//MediaPlayer player;
	MediaPlayer myMediaPlayer;

	LinearLayout followBtn;
	LinearLayout stopFollowBtn;

	LinearLayout playBtn;
	LinearLayout stopBtn;
	LinearLayout vanta;

	ArrayList<String> followList = new ArrayList<>();
	TinyDB tinydb;

	Toolbar toolbar;

	@Override
	public void onBackPressed(){
		this.finish();
		if(myMediaPlayer.isPlaying()){
			myMediaPlayer.stop();
		}
	}
	public void onButtonStop(View v) throws IOException {
		playBtn.setVisibility(View.VISIBLE);
		stopBtn.setVisibility(View.GONE);
		myMediaPlayer.pause();
	}
	public void onButtonLyssna(View v) throws IOException {
		playBtn.setVisibility(View.GONE);
		stopBtn.setVisibility(View.VISIBLE);
		myMediaPlayer.start();
	}

	public void onstopButtonFollow(View v) {
		followList = tinydb.getList("followList");
		for(int i = 0; i < followList.size(); i++) {
			if(followList.get(i).contains(title)){
				followList.remove(i);
				tinydb.putList("followList", followList);
				followBtn.setVisibility(View.VISIBLE);
				stopFollowBtn.setVisibility(View.GONE);

				ParsePush.unsubscribeInBackground(title
						.replace(" ", "")
						.replace("*", "")
						.replace("'", "")
						.replace("-", "")
						.replace("/", "")
						.replace(",", "")
						.replace("ü", "u")
						.replace("Ü", "U")
						.replace("é", "e")
						.replace("É", "E")
						.replace("&", "")
						.replace("å", "a")
						.replace("ä", "a")
						.replace("ö", "o")
						.replace("Å", "A")
						.replace("Ä", "A")
						.replace("Ö", "O")
						.replaceAll("[0-9]", "")
				);
			}

		}

	}
	public void onButtonFollow(View v) {
		followList = tinydb.getList("followList");
		Boolean foundInList = false;
		Toast.makeText(getApplicationContext(), "Du kommer att bli påmind när saker händer kring artisten", Toast.LENGTH_LONG).show();
		for(int i = 0; i < followList.size(); i++) {
			if(followList.get(i).contains(title)){
				foundInList = true;
			}

		}
		if(foundInList == false){
			followList.add(title + ";;" + musik + ";;" + place + ";;" + text + ";;" + mp3 + ";;" + image);
			tinydb.putList("followList", followList);
			followBtn.setVisibility(View.GONE);
			stopFollowBtn.setVisibility(View.VISIBLE);

			ParsePush.subscribeInBackground(title
					.replace(" ", "")
					.replace("*", "")
					.replace("'", "")
					.replace("-", "")
					.replace("/", "")
					.replace(",", "")
					.replace("ü", "u")
					.replace("Ü", "U")
					.replace("é", "e")
					.replace("É", "E")
					.replace("&", "")
					.replace("å", "a")
					.replace("ä", "a")
					.replace("ö", "o")
					.replace("Å", "A")
					.replace("Ä", "A")
					.replace("Ö", "O")
					.replaceAll("[0-9]", "")
			);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singleitemview);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);


		followBtn = (LinearLayout) findViewById(R.id.followBtn);
		stopFollowBtn = (LinearLayout) findViewById(R.id.stopfollowBtn);
		playBtn = (LinearLayout) findViewById(R.id.play);
		stopBtn = (LinearLayout) findViewById(R.id.stop);
		vanta = (LinearLayout) findViewById(R.id.vanta);

		mScrollView = (ObservableScrollView)findViewById(R.id.scrollview);
		mScrollView.setOnScrollChangedListener(this);

		tinydb = new TinyDB(this);

		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);
		density  = getResources().getDisplayMetrics().density;
		Point size = new Point();
		display.getSize(size);
		int screenHeight = size.y / (int)density;
		int textHeight = 370;
		int paddingTop = (screenHeight - textHeight) * (int)density;
		//Log.e("test", String.valueOf(paddingTop));
		LinearLayout layout = (LinearLayout) findViewById(R.id.paddinLayout);
		layout.setPadding(0,paddingTop,0,0);

		// Retrieve data from MainActivity on item click event
		Intent i = getIntent();
		title = i.getStringExtra("title");
		musik = i.getStringExtra("musik");
		place = i.getStringExtra("place");
		text = i.getStringExtra("text");
		image = i.getStringExtra("image");
		mp3 = i.getStringExtra("mp3");

		/*Player*/
		String url = mp3.replace(" ","%20").replace("¦", "%C2%A6"); // your URL here
		//Log.e("test", url);

		myMediaPlayer = new MediaPlayer();
		myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			myMediaPlayer.setDataSource(url);
			myMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
			myMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
				@Override
				public boolean onError(final MediaPlayer mp, final int what, final int extra) {
					ProgressBar loadsong = (ProgressBar) findViewById(R.id.loadsong);
					TextView laddarlat = (TextView) findViewById(R.id.laddarlat);
					laddarlat.setText("LÅT EJ TILLGÄNGLIG");
					loadsong.setVisibility(View.GONE);
					vanta.setVisibility(View.GONE);
					return false;
				}
			});

		} catch (IOException e) {
			ProgressBar loadsong = (ProgressBar) findViewById(R.id.loadsong);
			TextView laddarlat = (TextView) findViewById(R.id.laddarlat);
			laddarlat.setText("LÅT EJ TILLGÄNGLIG");
			loadsong.setVisibility(View.GONE);
			vanta.setVisibility(View.GONE);
			//Toast.makeText(this, "mp3 not found", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		//mp3 will be started after completion of preparing...
		myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer player) {
				vanta.setVisibility(View.GONE);
				playBtn.setVisibility(View.VISIBLE);
			}

		});

		/*end Player*/

		followList = tinydb.getList("followList");
		Boolean foundInList = false;
		for(int index = 0; index < followList.size(); index++) {
			if(followList.get(index).contains(title)){
				foundInList = true;
			}

		}
		if(foundInList == true){
			followBtn.setVisibility(View.GONE);
			stopFollowBtn.setVisibility(View.VISIBLE);
		}

		// Locate the TextViews in singleitemview.xml
		txtMusik = (TextView) findViewById(R.id.musik);
		txtTitle = (TextView) findViewById(R.id.title);
		txtPlace = (TextView) findViewById(R.id.place);
		txtText = (TextView) findViewById(R.id.text);
		largeImage = (ImageView) findViewById(R.id.image);
		smallImage = (ImageView) findViewById(R.id.imageamall);
		ImageView arrowDown = (ImageView) findViewById(R.id.arrowdown);


		// Load the results into the TextViews
		txtMusik.setText(musik);
		txtTitle.setText(title);
		txtPlace.setText(place);

		if(!text.isEmpty()){
			txtText.setText(text);
		}else{
			txtText.setVisibility(View.GONE);
			arrowDown.setVisibility(View.GONE);
		}
		Glide.with(this)
				.load(image)
				.centerCrop()
				.bitmapTransform(new BlurTransformation(this, Glide.get(this).getBitmapPool()))
				.into(largeImage);

		Glide.with(this)
				.load(image)
				.bitmapTransform(new CropCircleTransformation(Glide.get(this).getBitmapPool()))
				.into(smallImage);

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
				if(myMediaPlayer.isPlaying()){
					myMediaPlayer.stop();
				}
				break;

		}
		return false;
	}

	@Override
	public void onScrollChanged(int deltaX, int deltaY) {
		int scrollY = mScrollView.getScrollY();
		int ScrollValue = (int) ((scrollY * (int)density)/7.5);

		FrameLayout linearLayout =  (FrameLayout) findViewById(R.id.imgCont);
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)linearLayout.getLayoutParams();
		params.setMargins(0, -ScrollValue, 0, ScrollValue);
		linearLayout.setLayoutParams(params);
		linearLayout.requestLayout();
	}
}