package se.martenolsson.lah15;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FollowListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<WorldPopulation> worldpopulationlist = null;
	private ArrayList<WorldPopulation> arraylist;
	Typeface titleText;


	public FollowListViewAdapter(Context context, List<WorldPopulation> worldpopulationlist) {
		mContext = context;
		this.worldpopulationlist = worldpopulationlist;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<WorldPopulation>();
		this.arraylist.addAll(worldpopulationlist);
		titleText = ((ApplicationController) mContext.getApplicationContext()).geoSansBold;
	}

	public class ViewHolder {
		TextView title;
		TextView place;
		TextView musik;
		ImageView image;
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

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.fallowlistview_item, null);

			holder.title = (TextView) view.findViewById(R.id.title);
			holder.title.setTypeface(titleText);
			holder.place = (TextView) view.findViewById(R.id.place);
			holder.musik = (TextView) view.findViewById(R.id.musik);
			holder.image = (ImageView) view.findViewById(R.id.image);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.title.setText(worldpopulationlist.get(position).getTitle());
		holder.place.setText(worldpopulationlist.get(position).getPlace());
		holder.musik.setText(worldpopulationlist.get(position).getMusik());
		Glide.with(mContext)
				.load(worldpopulationlist.get(position).getImage())
				.into(holder.image);

		//Typeface flama = Typeface.createFromAsset(mContext.getAssets(), "Flama-Bold.otf");
		//holder.title.setTypeface(flama);

		// Listen for ListView Item Click
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, SingleItemView.class);
				intent.putExtra("title",(worldpopulationlist.get(position).getTitle()));
				intent.putExtra("musik",(worldpopulationlist.get(position).getMusik()));
				intent.putExtra("place",(worldpopulationlist.get(position).getPlace()));
				intent.putExtra("text",(worldpopulationlist.get(position).getText()));
				intent.putExtra("image",(worldpopulationlist.get(position).getImage()));
				intent.putExtra("mp3",(worldpopulationlist.get(position).getMp3()));
				mContext.startActivity(intent);
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
		else
		{
			for (WorldPopulation wp : arraylist)
			{
				if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
				{
					worldpopulationlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
