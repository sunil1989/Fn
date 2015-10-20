package reminder;



import java.io.IOException;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.life_reminder.R;

public class CreateGoogleMap extends Activity
{
	//static final LatLng TEL_AVIV = new LatLng(32.0833, 34.8000);
	
	static  LatLng TEL_AVIV=null;
	private GoogleMap map;
	private MapFragment mapFragment;
	private Marker marker;
	private LatLng position;
	private List<Address> address;

	private ImageView addressTitle;
	private EditText addressField;

	private Button setButton;
	private Button dropPinButton;

	private boolean fromAddress; // If true get LatLng from the address field

	// GPSTracker class
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment_view);
		MapsInitializer.initialize(getApplicationContext());
		custom_actionbar();
	

		 gps = new GPSTracker(CreateGoogleMap.this);
		 
		// check if GPS enabled		
	        if(gps.canGetLocation()){
	        	
	        	double latitude = gps.getLatitude();
	        	double longitude = gps.getLongitude();
	        	
	        	
	        	TEL_AVIV = new LatLng(latitude, longitude);
	        	
	        	// \n is for new line
	        	//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
	        }else{
	        	// can't get location
	        	// GPS or Network is not enabled
	        	// Ask user to enable GPS/network in settings
	        	gps.showSettingsAlert();
	        }
			
		
		addressTitle = (ImageView) findViewById(R.id.enter_address_title);
		addressField = (EditText) findViewById(R.id.address_field);
		setButton = (Button) findViewById(R.id.set);
	/*	dropPinButton = (Button) findViewById(R.id.drop_pin);*/

		fromAddress = false;
		mapFragment = (MapFragment) getFragmentManager().findFragmentById(
				R.id.mapview);
		
		map = mapFragment.getMap();
		marker = map.addMarker(new MarkerOptions() // error
				.position(TEL_AVIV)
				.visible(false)
				.title("Alert Me Here")
				.draggable(true)
				.icon(BitmapDescriptorFactory.defaultMarker()));
		
		/*.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.ic_launcher)));*/

		// Move the camera instantly to Tel-Aviv with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(TEL_AVIV, 15));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		/*dropPinButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				marker.setPosition(map.getCameraPosition().target);
				marker.setVisible(true);
			}
		});*/

		setButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent resultIntent = new Intent(); // Creating new result
													// intent to hold results
													// for the calling activity
				Geocoder gc = new Geocoder(getBaseContext());

				if (fromAddress) // If the user want to get the point from an
									// address
				{
					try
					{
						address = gc.getFromLocationName(addressField.getText()
								.toString(), 1); // Getting the LatLng from the
													// address field

						resultIntent.putExtra("Lat", address.get(0)
								.getLatitude()); // Adding the latitude to the
													// IntentResult
						resultIntent.putExtra("Lng", address.get(0)
								.getLongitude()); // Adding the longitude to the
													// IntentResult
						resultIntent.putExtra("location", addressField
								.getText().toString()); // Adding the address
														// string to the
														// IntentResult

						setResult(300, resultIntent); // Setting
																		// the
																		// results
																		// for
																		// the
																		// calling
																		// activity
						Toast.makeText(getApplicationContext(),
								"Location was successfully retrieved",
								Toast.LENGTH_SHORT).show();
					}

					// If the there is a bad network connection
					catch (IOException e)
					{
						Toast.makeText(getApplicationContext(),
								"Location could not be found, Sorry.",
								Toast.LENGTH_SHORT).show();
						return;
					}

					// If the address entered is not legal
					catch (Exception e)
					{
						Toast.makeText(getApplicationContext(),
								"The address is not valid.", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} else
				// Else get the point from the map
				{
					position = marker.getPosition(); // Getting the LatLng from
														// the marker on map
					try
					{
						address = gc.getFromLocation(position.latitude,
								position.longitude, 1);

						String addressString = address.get(0).getAddressLine(0)
								+ " " + address.get(0).getAddressLine(1) + " "
								+ address.get(0).getAddressLine(2);

						resultIntent.putExtra("Lat", position.latitude); // Adding
																			// the
																			// latitude
																			// to
																			// the
																			// IntentResult
						resultIntent.putExtra("Lng", position.longitude); // Adding
																			// the
																			// longitude
																			// to
																			// the
																			// IntentResult
						resultIntent.putExtra("location", addressString); // Adding
																			// the
																			// address
																			// string
																			// to
																			// the
																			// IntentResult

						setResult(300, resultIntent); // Setting
																		// the
																		// results
																		// for
																		// the
																		// calling
																		// activity

						Toast.makeText(getApplicationContext(),
								"Location was successfully retrieved",
								Toast.LENGTH_SHORT).show();
					} catch (IOException e)
					{
						Toast.makeText(getApplicationContext(),
								"Location could not be found, Sorry.",
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				finish();
			}
		});
		
		drop_pin();
		map.setOnMarkerDragListener(new OnMarkerDragListener() {
			
			@Override
			public void onMarkerDragStart(Marker arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMarkerDragEnd(Marker arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
public void drop_pin()
{
	marker.setPosition(map.getCameraPosition().target);
	marker.setVisible(true);
}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.map_action_bar, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.point_from_address:
			// Switching to address mode
			mapFragment.getView().setVisibility(View.INVISIBLE);
			dropPinButton.setVisibility(View.INVISIBLE);
			addressTitle.setVisibility(View.VISIBLE);
			addressField.setVisibility(View.VISIBLE);
			fromAddress = true;

			mapFragment.getActivity().invalidateOptionsMenu(); // Refreshing the
																// activity view
			break;

		case R.id.point_from_map:
			// Switching to map mode
			mapFragment.getView().setVisibility(View.VISIBLE);
			dropPinButton.setVisibility(View.VISIBLE);
			addressTitle.setVisibility(View.INVISIBLE);
			addressField.setVisibility(View.INVISIBLE);
			fromAddress = false;

			mapFragment.getActivity().invalidateOptionsMenu(); // Refreshing the
																// activity view
			break;
		}
		return true;
	}*/
	/* - Change title and imag of custom action bar */
	public void custom_actionbar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.event_fn_back_icon);

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View customview = layoutInflater.inflate(R.layout.action_bar, null);

		ImageView action_bar_img = (ImageView) customview
				.findViewById(R.id.event_img);
		TextView action_bar_title = (TextView) customview
				.findViewById(R.id.action_bar_title);

		actionBar.setCustomView(customview);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		action_bar_title.setText("map");
		action_bar_img.setImageResource(android.R.drawable.ic_menu_myplaces);

	}
	// Google Analytics
	@Override
	public void onStart()
	{
		super.onStart();
		// EasyTracker.getInstance().setContext(getBaseContext());
		// EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		// EasyTracker.getInstance().activityStop(this);
	}
}
