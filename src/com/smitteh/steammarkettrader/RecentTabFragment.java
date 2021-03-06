package com.smitteh.steammarkettrader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.smitteh.apis.Steam;
import com.smitteh.struts.SteamInventoryItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link RecentTabFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link RecentTabFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class RecentTabFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	List<SteamInventoryItem> inventoryItems = new ArrayList<SteamInventoryItem>();
	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment RecentTabFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static RecentTabFragment newInstance() {
		RecentTabFragment fragment = new RecentTabFragment();
		return fragment;
	}

	public RecentTabFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Create the View
		View view = inflater.inflate(R.layout.fragment_recent_tab, container,
				false);

		// Grab List View
		ListView listView = (ListView) view.findViewById(R.id.itemList);

		// Add Items to List
		//String[] items = { "AK-47 | Redline", "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" , "AK-47 | Redline" };
		final ArrayList<String> list = new ArrayList<String>();
		//inventoryItems = Steam.getInventory("76561198128825604", 730, 2);
		for (int i = 0; i < inventoryItems.size(); ++i) {
			list.add(inventoryItems.get(i).getName());
		}
		


		// Set View Adapter Can Customize
		final StableArrayAdapter adapter = new StableArrayAdapter(
				getActivity(), android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		
		new DownloadInventoryTask(inventoryItems, adapter).execute("");
		// Event Listeners
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				System.out.println(inventoryItems.get(position).getName());
			}

		});

		// Return View
		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
		List<String> list;
		Context context;

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
			this.list = objects;
			this.context = context;
		}

		@Override
		public void add(String object){
			super.add(object);
			for (int i = 0; i < list.size(); ++i) {
				mIdMap.put(list.get(i), i);
			}
		}
		
		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
			TextView itemName = (TextView) rowView.findViewById(R.id.itemName);
			TextView username = (TextView) rowView.findViewById(R.id.secondLine);
			
			ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
			
			new DownloadImageTask(imageView).execute("http://cdn.steamcommunity.com/economy/image/" + inventoryItems.get(position).getIconURLLarge() + "/128fx128f");
		

			itemName.setText(inventoryItems.get(position).getName());
			username.setText("Jordan");
			// Change the icon for Windows and iPhone
			return rowView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

}

class DownloadInventoryTask extends AsyncTask<String, Void, List<SteamInventoryItem>> {
	List<SteamInventoryItem> list = new ArrayList<SteamInventoryItem>();
	ArrayAdapter<String> adapter;
	
	public DownloadInventoryTask(List<SteamInventoryItem> list, ArrayAdapter<String> adapter) {
		this.list = list;
		this.adapter = adapter;
	}

	protected List<SteamInventoryItem> doInBackground(String... urls) {
		String urldisplay = urls[0];
		List<SteamInventoryItem> list = null;
		try {
			list = Steam.getInventory("76561198128825604", 730, 2);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	protected void onPostExecute(List<SteamInventoryItem> result) {
		if(result != null){
			list.addAll(result);
			System.err.println(list.size());
			ArrayList<String> items = new ArrayList<String>();
			for(SteamInventoryItem item : list)
				adapter.add(item.getName());
			
			//adapter.addAll(items);
			adapter.notifyDataSetChanged();
			
		}
	}
}


class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;

	public DownloadImageTask(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
	}
}
