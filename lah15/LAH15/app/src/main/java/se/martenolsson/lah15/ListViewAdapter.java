package se.martenolsson.lah15;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import se.martenolsson.lah15.classes.TinyDB;
import se.martenolsson.lah15.classes.mediaPlayer;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<WorldPopulation> worldpopulationlist = null;
	private ArrayList<WorldPopulation> arraylist;
	ArrayList<String> followList = new ArrayList<>();
    ArrayList<String> playList = new ArrayList<>();
	TinyDB tinydb;
	Boolean foundInList;
	private static final int TYPE_ITEM1 = 0;
	private static final int TYPE_ITEM2 = 1;
	ViewHolder holder;
    Typeface geoSans;
    Typeface caviar;

    LinearLayout playBtn;
    TextView playBtnText;
    MediaPlayer myMediaPlayer;

	int type;
	@Override
	public int getItemViewType(int position) {
        type= TYPE_ITEM2;
		return type;
	}

	@Override
	 public int getViewTypeCount() {
		return 2; //Set nubers of different types
	}

	public ListViewAdapter(Context context, List<WorldPopulation> worldpopulationlist, LinearLayout playBtn, TextView playBtnText, MediaPlayer myMediaPlayer) {
		mContext = context;
		this.worldpopulationlist = worldpopulationlist;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<WorldPopulation>();
		this.arraylist.addAll(worldpopulationlist);
        geoSans = ((ApplicationController) mContext.getApplicationContext()).geoSans;
        caviar = ((ApplicationController) mContext.getApplicationContext()).caviar;
        this.playBtn = playBtn;
        this.playBtnText = playBtnText;
        this.myMediaPlayer = myMediaPlayer;
	}

	public class ViewHolder {
        SwipeLayout swipe;
        RelativeLayout click;
		TextView title;
		TextView place;
		TextView musik;
		ImageView image;
		TextView heart;

        LinearLayout playSmenu;
        LinearLayout queSmenu;
        LinearLayout heartSmenu;
        TextView sMenuPlayText;
        TextView sMenuPlayQue;
        TextView sMenuHeartText;
	}

	@Override
	public int getCount() {
		return worldpopulationlist.size();
	}

	@Override
	public WorldPopulation getItem(int position) {
		return worldpopulationlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public View getView(final int position, View view, ViewGroup parent) {
		//final ViewHolder holder;
		tinydb = new TinyDB(mContext);
		if (view == null) {
			holder = new ViewHolder();

			/*Use TYPES to chang itemLayous at position*/
			int type = getItemViewType(position);
            view = inflater.inflate(R.layout.listview_item, null);
            holder.swipe = (SwipeLayout) view.findViewById(R.id.swipe);
            holder.click = (RelativeLayout) view.findViewById(R.id.click);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.title.setTypeface(geoSans);
            holder.place = (TextView) view.findViewById(R.id.place);
            holder.place.setTypeface(caviar);
            holder.musik = (TextView) view.findViewById(R.id.musik);
            holder.musik.setTypeface(caviar);
            holder.image = (ImageView) view.findViewById(R.id.image);
            holder.heart = (TextView) view.findViewById(R.id.heart);

            holder.playSmenu = (LinearLayout) view.findViewById(R.id.playSmenu);
            holder.queSmenu = (LinearLayout) view.findViewById(R.id.queSmenu);
            holder.sMenuPlayQue = (TextView) view.findViewById(R.id.sMenuPlayQue);
            holder.sMenuPlayQue.setTypeface(geoSans);
            holder.heartSmenu = (LinearLayout) view.findViewById(R.id.heartSmenu);
            holder.sMenuPlayText = (TextView) view.findViewById(R.id.sMenuPlayText);
            holder.sMenuPlayText.setTypeface(geoSans);
            holder.sMenuHeartText = (TextView) view.findViewById(R.id.sMenuHeartText);
            holder.sMenuHeartText.setTypeface(geoSans);
			view.setTag(holder);
			//view.setBackgroundResource(R.drawable.selector);

		} else {
			holder = (ViewHolder) view.getTag();
		}

        View.OnClickListener teaserClicklistner = new View.OnClickListener() {
            public void onClick(View view) {
                SwipeLayout swipeTag = (SwipeLayout) view.getTag();
                if(swipeTag.getOpenStatus()==SwipeLayout.Status.Open){
                    swipeTag.close();
                }else {
                    Intent intent = new Intent(mContext, SingleItemView.class);
                    intent.putExtra("title", (worldpopulationlist.get(position).getTitle()));
                    intent.putExtra("musik", (worldpopulationlist.get(position).getMusik()));
                    intent.putExtra("place", (worldpopulationlist.get(position).getPlace()));
                    intent.putExtra("text", (worldpopulationlist.get(position).getText()));
                    intent.putExtra("image", (worldpopulationlist.get(position).getImage()));
                    intent.putExtra("mp3", (worldpopulationlist.get(position).getMp3()));
                    mContext.startActivity(intent);
                }
            }
        };

        View.OnLongClickListener teaserLongClicklistner = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SwipeLayout swipeTag = (SwipeLayout) view.getTag();
                swipeTag.toggle();
                return false;
            }
        };

        View.OnClickListener play = new View.OnClickListener() {
            public void onClick(View view) {
                new mediaPlayer(mContext, worldpopulationlist.get(position).getTitle(), worldpopulationlist.get(position).getMp3(), true, null, null, null, false);
                SwipeLayout swipeTag = (SwipeLayout) view.getTag();

                playBtn.setVisibility(View.VISIBLE);
                playBtnText.setText(Html.fromHtml("&#xf1f9;"));

                swipeTag.close();
                refreshList();
            }
        };

        View.OnClickListener que = new View.OnClickListener() {
            public void onClick(View view) {
                SwipeLayout swipeTag = (SwipeLayout) view.getTag();

                String title = worldpopulationlist.get(position).getTitle();
                String mp3 = worldpopulationlist.get(position).getMp3();

                playList = tinydb.getList("playList");
                foundInList = false;
                for (int i = 0; i < playList.size(); i++) {
                    if (playList.get(i).contains(mp3)) {
                        foundInList = true;
                    }
                }
                if (!foundInList) {
                    playList.add(title + ";;" + mp3 + ";;" + "null");
                    tinydb.putList("playList", playList);
                }

                swipeTag.close();
                refreshList();
            }
        };

        View.OnClickListener heart = new View.OnClickListener() {
            public void onClick(View view) {
                SwipeLayout swipeTag = (SwipeLayout) view.getTag();

                String title = worldpopulationlist.get(position).getTitle();
                String musik = worldpopulationlist.get(position).getMusik();
                String place = worldpopulationlist.get(position).getPlace();
                String text = worldpopulationlist.get(position).getText();
                String image = worldpopulationlist.get(position).getImage();
                String mp3 = worldpopulationlist.get(position).getMp3();

                followList = tinydb.getList("followList");
                foundInList = false;
                for (int i = 0; i < followList.size(); i++) {
                    if (followList.get(i).startsWith(title)) {
                        foundInList = true;
                    }
                }
                if (!foundInList) {
                    Toast.makeText(mContext, "Du kommer att bli påmind när saker händer kring artisten", Toast.LENGTH_LONG).show();
                    followList.add(title + ";;" + musik + ";;" + place + ";;" + text + ";;" + mp3 + ";;" + image);
                    tinydb.putList("followList", followList);

                } else {
                    for (int i = 0; i < followList.size(); i++) {
                        if (followList.get(i).contains(title)) {
                            followList.remove(i);
                            tinydb.putList("followList", followList);
                        }

                    }

                }
                swipeTag.close();
                refreshList();
            }
        };

        holder.click.setOnClickListener(teaserClicklistner);
        holder.click.setOnLongClickListener(teaserLongClicklistner);
        holder.click.setTag(holder.swipe);
        holder.playSmenu.setOnClickListener(play);
        holder.playSmenu.setTag(holder.swipe);
        holder.queSmenu.setOnClickListener(que);
        holder.queSmenu.setTag(holder.swipe);
        holder.heartSmenu.setOnClickListener(heart);
        holder.heartSmenu.setTag(holder.swipe);

		/*Use TYPES to chang itemLayous at position*/
        ArrayList<Integer> follow = new ArrayList<>();
        followList = tinydb.getList("followList");
        for(int i = 0; i < followList.size(); i++) {
            if(followList.get(i).contains(worldpopulationlist.get(position).getTitle())){
                follow.add(position);
            }
        }
        if(follow.contains(position)) {
            holder.heart.setVisibility(View.VISIBLE);
            holder.sMenuHeartText.setText("Avfölj");
        }else{
            holder.heart.setVisibility(View.GONE);
            holder.sMenuHeartText.setText("Följ");
        }

        holder.title.setText(worldpopulationlist.get(position).getTitle());
        holder.place.setText(worldpopulationlist.get(position).getPlace());
        holder.musik.setText(worldpopulationlist.get(position).getMusik().toUpperCase());
        Glide.with(mContext)
                .load(worldpopulationlist.get(position).getImage())
                .bitmapTransform(new CropCircleTransformation(Glide.get(mContext).getBitmapPool()))
                .into(holder.image);


		return view;
	}
    void refreshList(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 100);
    }
	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		worldpopulationlist.clear();
		if (charText.length() == 0) {
			worldpopulationlist.addAll(arraylist);
		} 
		else {
			for (WorldPopulation wp : arraylist) {
				if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
					worldpopulationlist.add(wp);
				}else if (wp.getMusik().toLowerCase(Locale.getDefault()).contains(charText)) {
					worldpopulationlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
