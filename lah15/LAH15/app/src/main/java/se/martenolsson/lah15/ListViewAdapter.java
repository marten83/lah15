package se.martenolsson.lah15;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParsePush;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.martenolsson.lah15.classes.TinyDB;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<WorldPopulation> worldpopulationlist = null;
	private ArrayList<WorldPopulation> arraylist;

	ArrayList<String> followList = new ArrayList<>();
	TinyDB tinydb;
	Boolean foundInList;

	private static final int TYPE_ITEM1 = 0;
	private static final int TYPE_ITEM2 = 1;

	ViewHolder holder;

	int type;
	@Override
	public int getItemViewType(int position) {
		/*tinydb = new TinyDB(mContext);
		String adPos = tinydb.getString("adPos");
		if(!adPos.isEmpty()){
			if (position == Integer.parseInt(adPos)+1){
				type = TYPE_ITEM1;
			}
			else {
				type= TYPE_ITEM2;
			}
		}
		else{*/
			type= TYPE_ITEM2;
		//}
		return type;
	}

	@Override
	 public int getViewTypeCount() {
		return 2; //Set nubers of different types
	}

	public ListViewAdapter(Context context, List<WorldPopulation> worldpopulationlist) {
		mContext = context;
		this.worldpopulationlist = worldpopulationlist;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<WorldPopulation>();
		this.arraylist.addAll(worldpopulationlist);
	}

	public class ViewHolder {
		TextView title;
		TextView place;
		TextView musik;
		ImageView image;
		ImageView heart;
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
			if (type == TYPE_ITEM1) {
				view = inflater.inflate(R.layout.listview_ad, null);
			}
			else {
				view = inflater.inflate(R.layout.listview_item, null);
				holder.title = (TextView) view.findViewById(R.id.title);
				holder.place = (TextView) view.findViewById(R.id.place);
				holder.musik = (TextView) view.findViewById(R.id.musik);
				holder.image = (ImageView) view.findViewById(R.id.image);
				holder.heart = (ImageView) view.findViewById(R.id.heart);
			}
			view.setTag(holder);
			view.setBackgroundResource(R.drawable.selector);

		} else {
			holder = (ViewHolder) view.getTag();
		}


		/*Use TYPES to chang itemLayous at position*/
		if (type == TYPE_ITEM1) {

		}else {
			ArrayList<Integer> follow = new ArrayList<>();
			followList = tinydb.getList("followList");
			for(int i = 0; i < followList.size(); i++) {
				if(followList.get(i).contains(worldpopulationlist.get(position).getTitle())){
					follow.add(position);
				}
			}
			if(follow.contains(position)) {
				holder.heart.setVisibility(View.VISIBLE);
			}else{
				holder.heart.setVisibility(View.GONE);
			}

			holder.title.setText(worldpopulationlist.get(position).getTitle());
			holder.place.setText(worldpopulationlist.get(position).getPlace());
			holder.musik.setText(worldpopulationlist.get(position).getMusik());
			Glide.with(mContext)
					.load(worldpopulationlist.get(position).getImage())
					.into(holder.image);
		}


		// Listen for ListView Item Click
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, SingleItemView.class);
				intent.putExtra("title", (worldpopulationlist.get(position).getTitle()));
				intent.putExtra("musik", (worldpopulationlist.get(position).getMusik()));
				intent.putExtra("place", (worldpopulationlist.get(position).getPlace()));
				intent.putExtra("text", (worldpopulationlist.get(position).getText()));
				intent.putExtra("image", (worldpopulationlist.get(position).getImage()));
				intent.putExtra("mp3", (worldpopulationlist.get(position).getMp3()));

				mContext.startActivity(intent);
			}
		});

		view.setOnLongClickListener(new View.OnLongClickListener() {
			String title = worldpopulationlist.get(position).getTitle();
			String musik = worldpopulationlist.get(position).getMusik();
			String place = worldpopulationlist.get(position).getPlace();
			String text = worldpopulationlist.get(position).getText();
			String image = worldpopulationlist.get(position).getImage();
			String mp3 = worldpopulationlist.get(position).getMp3();

			@Override
			public boolean onLongClick(View arg0) {
				followList = tinydb.getList("followList");
				foundInList = false;
				for (int i = 0; i < followList.size(); i++) {
					if (followList.get(i).startsWith(title)) {
						foundInList = true;
					}
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Vad vill du göra?");
				builder.setPositiveButton("ÖPPNA", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Send single item click data to SingleItemView Class
						Intent intent = new Intent(mContext, SingleItemView.class);
						intent.putExtra("title", (title));
						intent.putExtra("musik", (musik));
						intent.putExtra("place", (place));
						intent.putExtra("text", (text));
						intent.putExtra("image", (image));
						intent.putExtra("mp3", (mp3));

						mContext.startActivity(intent);
					}
				});
				builder.setNegativeButton("FÖLJ", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (foundInList == false) {
							Toast.makeText(mContext, "Du kommer att bli påmind när saker händer kring artisten", Toast.LENGTH_LONG).show();
							followList.add(title + ";;" + musik + ";;" + place + ";;" + text + ";;" + mp3 + ";;" + image);
							tinydb.putList("followList", followList);

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

						} else {
							for (int i = 0; i < followList.size(); i++) {
								if (followList.get(i).contains(title)) {
									followList.remove(i);
									tinydb.putList("followList", followList);

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
						notifyDataSetChanged();
					}
				});
				builder.setNeutralButton("AVBRYT", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				});
				final AlertDialog alertdialog = builder.create();
				alertdialog.setOnShowListener(new DialogInterface.OnShowListener() {
					@Override
					public void onShow(DialogInterface dialog) {
						if(foundInList == true) {
							((Button) alertdialog.getButton(Dialog.BUTTON_NEGATIVE)).setText("AVFÖLJ");
						}
						((Button) alertdialog.getButton(Dialog.BUTTON_NEGATIVE)).setTextColor(Color.parseColor("#FFFFFF"));
						((Button) alertdialog.getButton(Dialog.BUTTON_NEGATIVE)).setBackgroundColor(Color.parseColor("#C15185"));
						((Button) alertdialog.getButton(Dialog.BUTTON_POSITIVE)).setTextColor(Color.parseColor("#FFFFFF"));
						((Button) alertdialog.getButton(Dialog.BUTTON_POSITIVE)).setBackgroundColor(Color.parseColor("#52C47C"));
					}
				});
				alertdialog.show();

				return false;
			}
		});

		return view;
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
