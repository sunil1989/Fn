package faces;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.life_reminder.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import adapter.Priority_dialog_adapter;
import alarm.SetReminder;
import app.ConnectionDetector;
import camerawork.ImageCaptureActivity;
import camerawork.ImageEditor;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;
import place.PlaceJSONParser;
import reminder.CreateGoogleMap;

//import android.view.inputmethod.InputMethodManager;

public class FacesActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	long timeinmilisecod;
	Uri selectedImage;
	String img_path;
	File imgfile;
	String path;
	Uri imageuri;
	boolean set_time, save_in_database;
	StringBuilder str;
	static long interval;
	private TaskListArray array;
	private Task task;
	long updateid;
	private String tag1;
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	private double latitude;
	private double longitude;
	private String address; // Empty string represent NONE
	int targetWidth = 210;
	int targetHeight = 210;
	int current_date, current_hour, current_minute;
	String set_when_hour;
	EditText face_name_editText;
	TextView male_TextView, female_TextView;

	private Context context;
	TextView startTextView, endTextView;
	String final_start, final_end;
	boolean start_bool, end;
	/* Bottom Section view */
	Dialog importance_dialog, alarm_dialog;
	ImageView priority_ImageView, alarm_ImageView;
	TextView alarm_hour_minuteTextView;
	TextView alarm_minute_text, alarm_hour_text;
	int alarm_final_value;
	boolean minute_hour_bool;
	Dialog snooze_dialog;
	int snooze_dialog_id;
	/* Bottom Section */
	// value
	String event_text;
	String final_date, final_month, final_year, final_date_month_year,
			final_hour, final_minute, final_time, final_description,
			final_face_name, final_male_female;
	String final_select_am_pm, final_repeat, final_with, final_where,
			final_date_long;
	ArrayList<String> tag_ArrayList = new ArrayList<String>();
	// camera_coding start
	AlertDialog dialog;
	ImageView event_camera_img, capture_ImageView, graphic_imageView;
	Builder builder;
	private static final int CAMERA_REQUEST = 4;

	private static final int PICK_FROM_GALLERY = 2;
	FileInputStream fis;
	String encodedImage;
	// camera_coding

	/* Slice */

	LinearLayout sliceLinearLayout;
	ImageView slice_plus_img;
	View slice_tag_view;
	boolean slice_on;

	EditText tags_editText;
	TextView save_Button;
	LinearLayout linearLayout, ll;
	int i = 0;
	/* ------------------Notes view-------------------- */

	LinearLayout descriptionLinearLayout, inner_descriptionLayout;
	ImageView description_plusImageView;
	EditText descriptionEditText;
	TextView faces_descriptionTextView;
	boolean description_on;
	/* ------------------Notes view End- ------------------- */

	/* ------------------with Fuctionality -------------------- */

	LinearLayout mWhereLayout;
	ImageView mMapButton;
	View where_view_location;

	MultiAutoCompleteTextView atvPlaces;
	PlacesTask placesTask;
	ParserTask parserTask;
	ListView mListview;
	SimpleAdapter Location_adapter;

	ImageView where_plus_img;

	//InputMethodManager imm;
	// Event Coding
	LinearLayout whenLinearLayout;
	ImageView when_ImageView;
	View visible_view;
	TextView event_saveTextView, faces_when_TextView, faces_where_TextView,
			faces_why_TextView, event_with_TextView, complete_saveTextView,
			share_TextView;
	boolean when_on, am_pm_bool, repeat_bool, where_on, male_female_bool;
	View when_calender_view;
	String selected_month, selected_day, selected_year;

	// calender
	LinearLayout reminder_textLinearLayout;
	ImageView hour_up_ImageView, hour_down_ImageView, minute_up_ImageView,
			minute_down_ImageView;
	TextView am_TextView, pm_TextView, autosend_TextView, remind_me_TextView,
			monthly_;
	EditText hour_TextView, minute_TextView;
	int hour_value = 12;
	int minute_value = 60;
	int event_repeat_value, am_pm_value;
	// event end coding

	private static final String tag = "MyCalendarActivity";
	public byte imageInByte[];
	private TextView currentMonth;
	private Button selectedDayMonthYearButton;
	private LinearLayout prevMonth;
	private LinearLayout nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	@SuppressLint("NewApi")
	private int month, year;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";

	// Action bar Image View AND Textview
	ImageView action_bar_img;
	TextView action_bar_title;

	private TaskListDataBase taskDataBase;
	private SetReminder setReminder;
	private String importance;

	private SharedPreferences loginPreferences;
	private SharedPreferences.Editor loginPrefsEditor;
    int calbool;
	/** Called when the activity is first created.; */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_cal);
		setContentView(R.layout.activity_faces);

		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		custom_actionbar();
		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());
		// database
		context = getBaseContext();
		setReminder = new SetReminder();
		taskDataBase = TaskListDataBase.getInstance(getBaseContext()); // Getting
																		// database
																		// instance
		//imm = (InputMethodManager) this
		//		.getSystemService(Service.INPUT_METHOD_SERVICE);
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);
		final_date = String.valueOf(_calendar.get(Calendar.DAY_OF_MONTH));
		final_month = String.valueOf(month);
		final_year = String.valueOf(year);
		final_date_month_year = final_date + "-" + final_month + "-"
				+ final_year;
		/*
		 * selectedDayMonthYearButton = (Button) this
		 * .findViewById(R.id.selectedDayMonthYear);
		 * selectedDayMonthYearButton.setText("Selected: ");
		 */
		// get_screen_size();
		prevMonth = (LinearLayout) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (LinearLayout) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
		calendarView.setDrawSelectorOnTop(false);
		calendarView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String date_month_year = (String) view.getTag();
				final_date_month_year = date_month_year;
				/*
				 * if (start_bool == true) { final_end = date_month_year;
				 * Toast.makeText(getApplicationContext(), "end" + final_end,
				 * 300).show(); } else { final_date_month_year =
				 * date_month_year; Toast.makeText(getApplicationContext(),
				 * "final" + final_date_month_year, 300).show(); }
				 */
				// show selected date
				// Toast.makeText(getApplicationContext(), "" + date_month_year,
				// 300).show();

			}
		});
		find_id();
		convert_graphic_hexagonal();
		//imm.hideSoftInputFromWindow(face_name_editText.getWindowToken(), 0);
		setCurrent_time();
		get_am_pm();
		toggle_male_female();
		// toggle_repeat();
		open_gallery_dialog();
		GetOldStuffData();
		
	}
	
	public void get_check() throws ParseException {
		ArrayList<String> arrayList1 = new ArrayList<String>();
		String str1 = final_date_month_year;
		String[] tokens = str1.split("-");
		for (String s : tokens) {
			arrayList1.add(s);
		}
		final_date = arrayList1.get(0);
		final_month = arrayList1.get(1);
		final_year = arrayList1.get(2);

		String str2 = final_date + "/" + final_month + "/" + final_year;

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		String strDate = sdf.format(c.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date2 = formatter.parse(strDate);
		Date date1 = formatter.parse(str2);
		/*
		 * if (date1.compareTo(date2) == 0) {
		 * Toast.makeText(getApplicationContext(), "Current_date", 300).show();
		 * }
		 */

		if (date1.compareTo(date2) > 0) {
			System.out.println("Date1 is after Date2");
			save_in_database = false;
			Add_text();
			tags_editText.getText().clear();

			close_all();
			if (updateid > 0) {
				// Toast.makeText(getApplicationContext(), "update",
				// 300).show();
				updateinfo();
			} else {
				// Toast.makeText(getApplicationContext(), "save", 300).show();
				event_info();
			}
			// finish();

		} else if (date1.compareTo(date2) < 0) {
			System.out.println("Date1 is before Date2");
			// Toast.makeText(getApplicationContext(),
			// "Please select right date", 300).show();
			save_in_database = true;

			Add_text();
			tags_editText.getText().clear();

			close_all();
			if (updateid > 0) {
				// Toast.makeText(getApplicationContext(), "update",
				// 300).show();
				updateinfo();
			} else {
				// Toast.makeText(getApplicationContext(), "save", 300).show();

				event_info();
			}
		} else if (date1.compareTo(date2) == 0) {
			save_in_database = false;
			System.out.println("Date1 is equal to Date2");
			match_time();

		} else {
			System.out.println("How to get here?");
		}

	}

	public void match_time() throws ParseException {
		String time = final_hour + ":" + final_minute;
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		// Toast.makeText(getApplicationContext(), "time" + time, 300).show();
		Date EndTime = dateFormat.parse(time);

		Date CurrentTime = dateFormat.parse(dateFormat.format(new Date()));

		/*if (CurrentTime.after(EndTime)) {
			System.out.println("timeeee end ");
			Toast.makeText(getApplicationContext(), "Please select right time",
					300).show();

		} else {*/
			Add_text();
			tags_editText.getText().clear();

			close_all();
			if (updateid > 0) {
				// Toast.makeText(getApplicationContext(), "update",
				// 300).show();
				updateinfo();
			} else {
				// Toast.makeText(getApplicationContext(), "save", 300).show();
				event_info();
			}
			// finish();

		//}
	}

	public void get_am_pm() {
		Date date = new Date(); // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new
																// calendar
																// instance
		calendar.setTime(date); // assigns calendar to given date
		current_hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h
															// format
		if (12 <= current_hour) {
			am_pm_value = 1;
			selcet_am_pm();
		} else {
			am_pm_value = 0;
			selcet_am_pm();
		}
	}

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
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		action_bar_title.setText("faces");
		action_bar_img.setImageResource(R.drawable.faces_page_title_icon);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; goto parent activity.
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void find_id() {

		mMapButton = (ImageView) findViewById(R.id.map);
		mMapButton.setOnClickListener(this);
		face_name_editText = (EditText) findViewById(R.id.face_name_editText);
		male_TextView = (TextView) findViewById(R.id.face_male_text);
		male_TextView.setOnClickListener(this);

		female_TextView = (TextView) findViewById(R.id.face_female_text);
		female_TextView.setOnClickListener(this);
		/* Bottom Section find id */

		priority_ImageView = (ImageView) findViewById(R.id.priority_img);
		alarm_ImageView = (ImageView) findViewById(R.id.alarm_img);

		priority_ImageView.setOnClickListener(this);
		alarm_ImageView.setOnClickListener(this);
		/* Bottom Section */
		reminder_textLinearLayout = (LinearLayout) findViewById(R.id.reminder_text);
		reminder_textLinearLayout.setVisibility(View.GONE);
		startTextView = (TextView) findViewById(R.id.start_textview);
		endTextView = (TextView) findViewById(R.id.end_textview);
		startTextView.setOnClickListener(this);
		endTextView.setOnClickListener(this);
		event_saveTextView = (TextView) findViewById(R.id.event_save_text);
		event_saveTextView.setOnClickListener(this);

		complete_saveTextView = (TextView) findViewById(R.id.event_complete_text);
		complete_saveTextView.setOnClickListener(this);

		share_TextView = (TextView) findViewById(R.id.share_notes);
		share_TextView.setOnClickListener(this);

		hour_up_ImageView = (ImageView) findViewById(R.id.event_hour_up);
		hour_up_ImageView.setOnClickListener(this);
		hour_down_ImageView = (ImageView) findViewById(R.id.event_hour_down);
		hour_down_ImageView.setOnClickListener(this);

		minute_up_ImageView = (ImageView) findViewById(R.id.event_minute_up);
		minute_up_ImageView.setOnClickListener(this);
		minute_down_ImageView = (ImageView) findViewById(R.id.event_minute_down);
		minute_down_ImageView.setOnClickListener(this);

		am_TextView = (TextView) findViewById(R.id.event_am_text);
		pm_TextView = (TextView) findViewById(R.id.event_pm_text);

		am_TextView.setOnClickListener(this);
		pm_TextView.setOnClickListener(this);

		hour_TextView = (EditText) findViewById(R.id.event_hour_text);
		minute_TextView = (EditText) findViewById(R.id.event_minute_text);
		faces_when_TextView = (TextView) findViewById(R.id.faces_when_text);
		when_calender_view = findViewById(R.id.complete_calender_layout);
		whenLinearLayout = (LinearLayout) findViewById(R.id.when_layout);
		whenLinearLayout.setOnClickListener(this);
		when_ImageView = (ImageView) findViewById(R.id.when_img);

		/*------------------- notes layout - Start -------------*/
		faces_descriptionTextView = (TextView) findViewById(R.id.faces_description_text);
		descriptionLinearLayout = (LinearLayout) findViewById(R.id.description_layout);
		inner_descriptionLayout = (LinearLayout) findViewById(R.id.inner_description_layout);
		description_plusImageView = (ImageView) findViewById(R.id.description_plus_img);
		descriptionEditText = (EditText) findViewById(R.id.description_editText);
		descriptionLinearLayout.setOnClickListener(this);
		/*------------------- notes layout -End-------------*/
		// Slice
		faces_why_TextView = (TextView) findViewById(R.id.faces_why_text);
		slice_tag_view = findViewById(R.id.event_slice);
		sliceLinearLayout = (LinearLayout) findViewById(R.id.slice_layout);
		sliceLinearLayout.setOnClickListener(this);
		slice_plus_img = (ImageView) findViewById(R.id.slice_plus_img);

		event_camera_img = (ImageView) findViewById(R.id.event_camera_img);
		event_camera_img.setOnClickListener(this);
		capture_ImageView = (ImageView) findViewById(R.id.capture_imageView);
		graphic_imageView = (ImageView) findViewById(R.id.graphic_imageView);
		capture_ImageView.setOnClickListener(this);

		/* slice */

		linearLayout = (LinearLayout) findViewById(R.id.rootlayout);
		tags_editText = (EditText) findViewById(R.id.tag_editText);
		tags_editText.setHint("Enter Reason");
		save_Button = (TextView) findViewById(R.id.save_button1);
		save_Button.setOnClickListener(this);

		/*------------------- where layout-------------*/
		faces_where_TextView = (TextView) findViewById(R.id.faces_where_text);
		mWhereLayout = (LinearLayout) findViewById(R.id.where_layout);
		mWhereLayout.setOnClickListener(this);

		where_view_location = findViewById(R.id.event_where);
		atvPlaces = (MultiAutoCompleteTextView) findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);

		mListview = (ListView) findViewById(R.id.mListview);
		mListview.setOnItemClickListener(this);

		where_plus_img = (ImageView) findViewById(R.id.where_plus_img);
		// Write a get Contact

		atvPlaces.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				isInternetPresent = cd.isConnectingToInternet();

				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					// make HTTP requests
					// Toast.makeText(getApplicationContext(),
					// "You have internet connection", 300).show();
					placesTask = new PlacesTask();
					placesTask.execute(s.toString());

				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					Toast.makeText(getApplicationContext(),
							"No Internet Connection", 300).show();

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

	}

	public void setCurrent_time() {
		Calendar cal = Calendar.getInstance();
		int millisecond = cal.get(Calendar.MILLISECOND);
		int second = cal.get(Calendar.SECOND);
		int minute = cal.get(Calendar.MINUTE);
		// 12 hour format
		int hour = cal.get(Calendar.HOUR);
		minute_value = minute;

		if (hour == 0) {
			hour = 12;

		}

		hour_TextView.setText("" + new DecimalFormat("00").format(hour));
		minute_TextView.setText("" + new DecimalFormat("00").format(minute));
		final_minute = String.valueOf(minute);
		final_hour = String.valueOf(hour);
		current_minute = minute;
		current_hour = hour;
		set_when_hour = final_hour;
	}

	public void minute_up_click() {
		if (minute_value == 59) {

			minute_value = 0;
			minute_TextView.setText(""
					+ new DecimalFormat("00").format(minute_value));
		} else {
			minute_value = minute_value + 1;
			minute_TextView.setText(""
					+ new DecimalFormat("00").format(minute_value));
		}
		final_minute = String.valueOf(minute_value);

	}

	public void minute_down_click() {
		if (minute_value == 0) {

			minute_value = 59;
			minute_TextView.setText(""
					+ new DecimalFormat("00").format(minute_value));
		} else {
			minute_value = minute_value - 1;
			minute_TextView.setText(""
					+ new DecimalFormat("00").format(minute_value));
		}
		final_minute = String.valueOf(minute_value);
	}

	public void hour_up_click() {

		if (hour_value == 12) {

			hour_value = 1;
			hour_TextView.setText(""
					+ new DecimalFormat("00").format(hour_value));
		} else {
			hour_value = hour_value + 1;
			hour_TextView.setText(""
					+ new DecimalFormat("00").format(hour_value));
		}

		final_hour = String.valueOf(hour_value);
		set_when_hour = final_hour;
	}

	public void hour_down_click() {
		if (hour_value == 1) {

			hour_value = 12;
			hour_TextView.setText(""
					+ new DecimalFormat("00").format(hour_value));
		} else {
			hour_value = hour_value - 1;
			hour_TextView.setText(""
					+ new DecimalFormat("00").format(hour_value));
		}

		final_hour = String.valueOf(hour_value);
		set_when_hour = final_hour;
	}

	public void toggle_male_female() {
		// if am_pm_bool = true=pm else false = am
		String select_male_female = "";
		if (male_female_bool == false) {
			male_TextView.setTextColor(getResources().getColor(
					R.color.DarkGreen));
			female_TextView.setTextColor(getResources().getColor(R.color.Gray));
			male_female_bool = true;
		} else {

			female_TextView.setTextColor(getResources().getColor(
					R.color.DarkGreen));
			male_TextView.setTextColor(getResources().getColor(R.color.Gray));
			male_female_bool = false;
		}

		if (male_female_bool == true) {
			select_male_female = "male";
			final_male_female = select_male_female;
			// Toast.makeText(getApplicationContext(), "" + select_am_pm,
			// 300).show();
		} else {
			select_male_female = "female";
			final_male_female = select_male_female;
			// Toast.makeText(getApplicationContext(), "" + select_am_pm,
			// 300).show();

		}

	}

	public void selcet_am_pm() {
		// if am_pm_bool = true=pm else false = am
		String repeat1[] = { "am", "pm" };

		switch (am_pm_value) {

		case 0:
			am_TextView
					.setTextColor(getResources().getColor(R.color.DarkGreen));
			pm_TextView.setTextColor(getResources().getColor(R.color.Gray));
			// Toast.makeText(getApplicationContext(), "" + repeat[0],
			// 300).show();
			final_select_am_pm = repeat1[0];

			break;
		case 1:
			pm_TextView
					.setTextColor(getResources().getColor(R.color.DarkGreen));
			am_TextView.setTextColor(getResources().getColor(R.color.Gray));
			final_select_am_pm = repeat1[1];
			break;

		default:
			break;
		}

	}

	public void toggle_repeat() {

		// result : 0={auto send} 1=remind me
		String repeat[] = { "{auto send}", "{remind me}" };
		switch (event_repeat_value) {
		case 0:
			autosend_TextView.setTextColor(getResources().getColor(
					R.color.Green));
			remind_me_TextView.setTextColor(getResources().getColor(
					R.color.Gray));

			// Toast.makeText(getApplicationContext(), "" + repeat[0],
			// 300).show();

			break;
		case 1:
			remind_me_TextView.setTextColor(getResources().getColor(
					R.color.Green));
			autosend_TextView.setTextColor(getResources()
					.getColor(R.color.Gray));

			// Toast.makeText(getApplicationContext(), "" + repeat[1],
			// 300).show();
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

		switch (v.getId()) {
            case R.id.capture_imageView:
                if (encodedImage != null) {
                    show_capture_image();
                }

                break;
            case R.id.when_layout:

                when_plus_minus();
                //imm.hideSoftInputFromWindow(descriptionEditText.getWindowToken(), 0);
                break;
            case R.id.event_hour_up:
                hour_value = Integer.parseInt(hour_TextView.getText().toString());
                hour_up_click();
                break;
            case R.id.event_hour_down:
                hour_value = Integer.parseInt(hour_TextView.getText().toString());
                hour_down_click();
                break;
            case R.id.event_minute_up:
                minute_value = Integer.parseInt(minute_TextView.getText()
                        .toString());
                minute_up_click();
                break;
            case R.id.event_minute_down:
                minute_value = Integer.parseInt(minute_TextView.getText()
                        .toString());
                minute_down_click();
                break;
            case R.id.event_am_text:
                am_pm_value = 0;
                selcet_am_pm();
                break;
            case R.id.event_pm_text:
                am_pm_value = 1;
                selcet_am_pm();
                break;
            case R.id.map:
                Intent in = new Intent(this, CreateGoogleMap.class);
                startActivityForResult(in, 1);

                break;
            case R.id.face_male_text:
                toggle_male_female();
                break;
            case R.id.face_female_text:
                toggle_male_female();
                break;
            case R.id.auto_text:
                event_repeat_value = 0;
                toggle_repeat();
                break;
            case R.id.remind_text:
                event_repeat_value = 1;
                toggle_repeat();
                break;
            case R.id.event_camera_img:
                dialog.show();
                break;

            case R.id.where_layout:

                where_plus_minus();
                //	imm.hideSoftInputFromWindow(descriptionEditText.getWindowToken(), 0);
                break;

            case R.id.slice_layout:

                slice_plus_minus();
                //imm.hideSoftInputFromWindow(descriptionEditText.getWindowToken(), 0);
                break;
            case R.id.description_layout:

                description_plus_minus();
                //imm.hideSoftInputFromWindow(descriptionEditText.getWindowToken(), 0);
                break;

            case R.id.save_button1:
                Add_text();
                tags_editText.getText().clear();
                break;

            case R.id.share_notes:
                share_note();
                // whole_note_data();
                break;
            case R.id.event_save_text:

                final_face_name = face_name_editText.getText().toString();
                if (final_face_name.trim().length() <= 0) {

                    Toast.makeText(getApplicationContext(), "Please enter Person Name",
                            300).show();

                    // Toast.makeText(getApplicationContext(),
                    // "------"+final_date+"-"+final_month+"-"+final_year, 300).show();
                    // Toast.makeText(getApplicationContext(),
                    // "------"+final_hour+"-"+final_minute, 300).show();
                } else {
                    LayoutInflater factory = LayoutInflater.from(this);
                    final View deleteDialogView = factory.inflate(
                            R.layout.custon_calendar_dialog, null);
                    final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
                    deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    deleteDialog.setView(deleteDialogView);
                    deleteDialogView.findViewById(R.id.btn_yes).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //your business logic
                            deleteDialog.dismiss();


                            calbool = 1;
                            try {

                                // save

                                String minute = minute_TextView.getText().toString();

                                String hour = hour_TextView.getText().toString();
                                if (Integer.parseInt(hour) <= 12
                                        && Integer.parseInt(minute) < 60) {

                                    convert_date_into_long();
                                    convert_time_into_long();
                                    get_check();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "please Select right hour minute", Toast.LENGTH_SHORT).show();
                                }


                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                    deleteDialogView.findViewById(R.id.btn_no).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {


                            try {

                                // save

                                String minute = minute_TextView.getText().toString();

                                String hour = hour_TextView.getText().toString();
                                if (Integer.parseInt(hour) <= 12
                                        && Integer.parseInt(minute) < 60) {

                                    convert_date_into_long();
                                    convert_time_into_long();
                                    get_check();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "please Select right hour minute", Toast.LENGTH_SHORT).show();
                                }


                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            // dialog.cancel();
                            deleteDialog.dismiss();

                        }
                    });

                    deleteDialog.show();

                }

/*

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    FacesActivity.this);

            // set title
            alertDialogBuilder.setTitle("FN");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Do You Want Add Event In Google Calendar")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            calbool=1;

                            try {

                                // save

                                String minute = minute_TextView.getText().toString();

                                String hour = hour_TextView.getText().toString();
                                if (Integer.parseInt(hour) <= 12
                                        && Integer.parseInt(minute) < 60) {

                                    convert_date_into_long();
                                    convert_time_into_long();
                                    get_check();
                                } else {
                                    //Toast.makeText(getApplicationContext(),
                                    //		"please Select right hour minute", 300).show();
                                    convert_date_into_long();
                                    convert_time_into_long();
                                    get_check();
                                }

                            }

                            catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing


                            try {

                                // save

                                String minute = minute_TextView.getText().toString();

                                String hour = hour_TextView.getText().toString();
                                if (Integer.parseInt(hour) <= 12
                                        && Integer.parseInt(minute) < 60) {

                                    convert_date_into_long();
                                    convert_time_into_long();
                                    get_check();
                                } else {
                                    //Toast.makeText(getApplicationContext(),
                                    //		"please Select right hour minute", 300).show();
                                    convert_date_into_long();
                                    convert_time_into_long();
                                    get_check();
                                }

                            }

                            catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
*/


			break;

		case R.id.event_complete_text:
			if (updateid > 0) {
				// Toast.makeText(getApplicationContext(), "update",
				// 300).show();
				/* updateinfo(); */
				taskDataBase.update_complete(updateid);
				finish();
			}
			break;
		case R.id.priority_img:
			// priority_dialog();
			importance_dialog();
			break;
		case R.id.alarm_img:
			alarm_dialog();
			toggle_hour_minute();
			break;
		case R.id.start_textview:
			start_bool = false;
			start_end_click();
			break;
		case R.id.end_textview:
			start_bool = true;
			start_end_click();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	private void show_capture_image() {
		ImageView dialog_capture_imageView;
		Button share_capture_imageView, delete_Button;
		final Dialog capture_dialog = new Dialog(this);
		/*
		 * capture_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */

		capture_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		capture_dialog.setContentView(R.layout.capture_image_dialog);
		dialog_capture_imageView = (ImageView) capture_dialog
				.findViewById(R.id.capture_imageView1);
		share_capture_imageView = (Button) capture_dialog
				.findViewById(R.id.share_btn);
		delete_Button = (Button) capture_dialog.findViewById(R.id.delete_btn);
		// capture_imageView.setImageResource(R.drawable.ic_launcher);

		if (encodedImage != null) {

			File imgFile = new File(encodedImage);

			if (imgFile.exists()) {
				/*Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());*/
				Bitmap bi = decodeSampledBitmapFromFile(imgFile.getAbsolutePath(), 500, 250);
				
				dialog_capture_imageView.setImageBitmap(bi);
			}

		}
		share_capture_imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (encodedImage != null) {

					File imgFile = new File(encodedImage);

					if (imgFile.exists()) {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_SEND);

						intent.setType("image/*");

						Uri uri = Uri.fromFile(imgFile);
						System.out.println("immmmm" + uri);

						intent.putExtra(Intent.EXTRA_STREAM, uri);

						if (intent.resolveActivity(getPackageManager()) != null) {
							startActivity(Intent.createChooser(intent, "share"));
						}
					}

				}
			}
		});
		dialog_capture_imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				capture_dialog.cancel();
			}
		});
		delete_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				encodedImage = null;
				convert_graphic_hexagonal();
				
				capture_dialog.cancel();
			}
		});
		capture_dialog.show();

	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months_str = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final String[] months = { "01", "02", "03", "04", "05", "06",
				"07", "08", "09", "10", "11", "12" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private TextView gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);
				// if (i == getCurrentDayOfMonth()) {
				if (i == Integer.parseInt(final_date)) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
				int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (TextView) row.findViewById(R.id.calendar_day_gridcell);
			// gridcell.setOnClickListener(this);

			// ACCOUNT FOR SPACING

			Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			final_date = String.valueOf(getCurrentDayOfMonth());
			current_date = getCurrentDayOfMonth();
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			selected_day = theday;
			selected_month = themonth;
			selected_year = theyear;

			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			row.setTag(theday + "-" + themonth + "-" + theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
					+ theyear);

			if (day_color[1].equals("GREY")) {

				gridcell.setTextColor(getResources().getColor(R.color.gray));
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(R.color.gray));
			}
			if (day_color[1].equals("BLUE")) {

				// set current date Background color
				/*
				 * gridcell.setBackgroundColor((getResources()
				 * .getColor(R.color.Green)));
				 */
				// set current date Text color
				gridcell.setTextColor(getResources().getColor(
						R.color.darkorrange));
			}
			return row;
		}

		@Override
		public void onClick(View view) {
			String date_month_year = (String) view.getTag();
			// show selected date
			// Toast.makeText(getApplicationContext(), "" + date_month_year,
			// 300).show();

			// selectedDayMonthYearButton.setText("Selected: " +
			// date_month_year);
			Log.e("Selected date", date_month_year);
			try {
				Date parsedDate = dateFormatter.parse(date_month_year);
				Log.d(tag, "Parsed Date: " + parsedDate.toString());

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}

	public void get_screen_size() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;

		if (height == 800 && width == 480) {
			targetWidth = 180;
			targetHeight = 180;
		}
		if (height == 1280 && width == 720) {
			targetWidth = 210;
			targetHeight = 210;

		}
		if (height == 1184 && width == 720) {
			targetWidth = 210;
			targetHeight = 210;

		}
		if (height == 1920 && width == 1080) {
			targetWidth = 310;
			targetHeight = 310;

		}
	}

	public Bitmap getHexagonShape(Bitmap scaleBitmapImage) {
		// TODO Auto-generated method stub
		/*
		 * int targetWidth = 210; int targetHeight = 210;
		 */
		get_screen_size();
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);

		Path path = new Path();
		float stdW = targetWidth;
		float stdH = targetHeight;
		float w3 = stdW / 2;
		float h2 = stdH / 2;

		float radius = stdH / 2 - 10;
		float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
		float centerX = stdW / 2;
		float centerY = stdH / 2;
		path.moveTo(centerX, centerY + radius);
		path.lineTo(centerX - triangleHeight, centerY + radius / 2);
		path.lineTo(centerX - triangleHeight, centerY - radius / 2);
		path.lineTo(centerX, centerY - radius);
		path.lineTo(centerX + triangleHeight, centerY - radius / 2);
		path.lineTo(centerX + triangleHeight, centerY + radius / 2);
		path.moveTo(centerX, centerY + radius);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public void when_plus_minus() {
		if (when_on == false) {
			when_ImageView.setBackgroundResource(R.drawable.minus);
			when_calender_view.setVisibility(View.VISIBLE);
			when_on = true;

			where_on = true;
			slice_on = true;
			description_on = true;
			where_plus_minus();
			slice_plus_minus();
			description_plus_minus();
			faces_when_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			faces_when_TextView.setText("when");

			setGridCellAdapterToDate(month, year);

		} else {
			hour_validation();
			minute_validation();
			/*
			 * when_ImageView.setBackgroundResource(R.drawable.plus);
			 * when_calender_view.setVisibility(View.GONE); when_on = false;
			 * if(only_when_on==true) { set_when_text(); }
			 */
		}
	}

	/* -------------------where slice invoke on click with ----------- */

	public void slice_plus_minus() {
		if (slice_on == false) {
			slice_plus_img.setBackgroundResource(R.drawable.minus);
			slice_tag_view.setVisibility(View.VISIBLE);
			slice_on = true;

			description_on = true;
			when_on = true;
			where_on = true;
			description_plus_minus();
			when_plus_minus();
			where_plus_minus();
		} else {

			slice_plus_img.setBackgroundResource(R.drawable.plus);
			slice_tag_view.setVisibility(View.GONE);
			slice_on = false;
			set_slice_text();
		}
	}

	private void set_slice_text() {
		// TODO Auto-generated method stub
		faces_why_TextView.setTextColor(getResources().getColor(
				R.color.Green));
		if (tags_editText.getText().toString().length() == 0) {
			faces_why_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			faces_why_TextView.setText("Why");
		} else {
			faces_why_TextView.setText(""
					+ tags_editText.getText().toString());
		}
	
	}

	/* -------------------where Data invoke on click with ----------- */
	private void where_plus_minus() {
		// TODO Auto-generated method stub
		if (where_on == false) {
			where_plus_img.setBackgroundResource(R.drawable.minus);
			where_view_location.setVisibility(View.VISIBLE);
			where_on = true;

			when_on = true;
			slice_on = true;
			description_on = true;
			when_plus_minus();
			slice_plus_minus();
			description_plus_minus();
			faces_where_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			faces_where_TextView.setText("where");
		} else {

			where_plus_img.setBackgroundResource(R.drawable.plus);
			where_view_location.setVisibility(View.GONE);
			where_on = false;
			set_where_text();

		}
	}

	/* -------------------Subject Data invoke on click subject----------- */
	private void description_plus_minus() {
		// TODO Auto-generated method stub
		if (description_on == false) {
			description_plusImageView.setBackgroundResource(R.drawable.minus);
			inner_descriptionLayout.setVisibility(View.VISIBLE);
			description_on = true;
			where_on = true;
			when_on = true;
			slice_on = true;
			when_plus_minus();
			slice_plus_minus();
			faces_descriptionTextView.setTextColor(getResources().getColor(
					R.color.Gray));
			faces_descriptionTextView.setText("description");

		} else {

			description_plusImageView.setBackgroundResource(R.drawable.plus);
			inner_descriptionLayout.setVisibility(View.GONE);
			description_on = false;
			set_description_text();
		}
	}

	/**
	 * ------------- A method to download json data from url
	 * ------------------------
	 */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/*--- -------------------Fetches all places from GooglePlaces AutoComplete Web Service----------------*/
	private class PlacesTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";

			// Obtain browser key from https://code.google.com/apis/console
			String key = "key=AIzaSyCHF80MJz22esrjQpW0lrfl7dH44FfkGH0";

			String input = "";

			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=geocode";

			// Sensor enabled
			String sensor = "sensor=false";

			// Building the parameters to the web service
			String parameters = input + "&" + types + "&" + sensor + "&" + key;

			// Output format
			String output = "json";

			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
					+ output + "?" + parameters;

			try {
				// Fetching the data from web service in background
				data = downloadUrl(url);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Creating ParserTask
			parserTask = new ParserTask();

			// Starting Parsing the JSON string returned by Web Service
			parserTask.execute(result);
		}
	}

	/**
	 * ------------- A class to parse the Google Places in JSON format
	 * -----------------
	 */
	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				// Getting the parsed data as a List construct
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			int[] to = new int[] { android.R.id.text1 };

			// Creating a SimpleAdapter for the AutoCompleteTextView
			Location_adapter = new SimpleAdapter(getBaseContext(), result,
					android.R.layout.simple_list_item_1, from, to);

			// Setting the adapter
			/* For multiauto complete text view */
			atvPlaces.setAdapter(Location_adapter);
			// atvPlaces.setThreshold(1);
			atvPlaces
					.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
			/* For multiauto complete text view */

			// mListview.setAdapter(Location_adapter);
			adapter.notifyDataSetChanged();
			Location_adapter.notifyDataSetChanged();

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		HashMap<String, String> hm = (HashMap<String, String>) parent
				.getAdapter().getItem(position);
		atvPlaces.setText("" + hm.get("description"));
		final_where = hm.get("description");
	}

	/* Slice functionality */
	public void Add_text() {
		ll = new LinearLayout(this);

		ll.setOrientation(LinearLayout.HORIZONTAL);

		ll.setId(i);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		TextView product = new TextView(this);
		if (tags_editText.getText().toString().trim().length() <= 0) {

		} else {
			product.setTextSize(14.0f);
			product.setTextColor(Color.parseColor("#78C854"));// #78C854
			product.setText("" + tags_editText.getText().toString());

			ll.addView(product);
			tag_ArrayList.add(tags_editText.getText().toString());

			ImageView btn = new ImageView(this);
			btn.setPadding(5, 0, 20, 40);
			ll.addView(btn);

			btn.setLayoutParams(params);
			btn.setImageResource(R.drawable.event_awith_delete);
			btn.setDuplicateParentStateEnabled(true);

			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LinearLayout linearParent = (LinearLayout) v.getParent()
							.getParent();
					LinearLayout linearChild = (LinearLayout) v.getParent();
					linearParent.removeView(linearChild);
				}
			});

			linearLayout.addView(ll);
			i++;
		}

	}

	public void convert_date_into_long() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy",
				Locale.getDefault());
		String str_date = final_date_month_year;
		Date date = simpleDateFormat.parse(str_date);
		System.out.println(str_date);
		System.out.println("Date - Time in milliseconds : " + date.getTime());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		System.out.println("Date : Calender - date in milliseconds : "
				+ calendar.getTimeInMillis());
		final_date_long = String.valueOf(calendar.getTimeInMillis());

	}

	public void convert_time_into_long() throws ParseException {

		switch (final_select_am_pm) {
		case "am":

			int time1 = Integer.parseInt(final_hour) - 12;
			if (time1 < 0) {

			} else {
				final_hour = String.valueOf(time1);
				// Toast.makeText(getApplicationContext(),
				// "am"+final_hour+":"+final_minute, 300).show();
			}
			break;
		case "pm":
			if (Integer.parseInt(final_hour) == 12) {

			} else {
				int time = Integer.parseInt(final_hour) + 12;
				if (time > 24) {

				} else {
					final_hour = String.valueOf(time);
					// Toast.makeText(getApplicationContext(),
					// "pm"+final_hour+":"+final_minute, 300).show();
					break;
				}
			}
		default:
			break;
		}
		;

		final_time = final_date_month_year + " " + final_hour + ":"
				+ final_minute;
		Log.d("final_time =", "" + final_time);

		// SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm",
				Locale.getDefault());
		// String dateInString = "22-01-2015 10:20:56";
		String dateInString = final_time;
		Date date = sdf.parse(dateInString);

		System.out.println(dateInString);
		System.out.println("Date - Time in milliseconds : " + date.getTime());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		System.out.println("Calender - Time in milliseconds : "
				+ calendar.getTimeInMillis());
		final_time = String.valueOf(calendar.getTimeInMillis());
		// event info

	}

	private void GetOldStuffData() {
		// TODO Auto-generated method stub

		taskDataBase = TaskListDataBase.getInstance(getBaseContext()); // Creatin
		array = TaskListArray.getInstance(getBaseContext());
		task = new Task();

		updateid = getIntent().getLongExtra("taskId", -1);
		
		
		snooze_dialog_id = getIntent().getIntExtra("snooze_dialogid", 0);
		if (snooze_dialog_id == 6) {

			snooze_Dialog();
			
			

		}
		

		if (updateid > 0) {
			System.out.println("========long=id==========" + updateid);
			task = taskDataBase.getTask(updateid); // Getting the task from the
													// database

			final_face_name = task.getTitle();
			final_description = task.getDescription();
			final_male_female = task.getSex();
			final_with = task.getEvent_with();
			final_where = task.getLocation();

			encodedImage = task.getEvent_photo();
			importance = task.getImportance();
			tag1 = task.getEvent_tag();
			final_select_am_pm = task.getam_pm();

			System.out.println("========long=id==========" + updateid);

			System.out.println("=========title==========" + final_face_name);
			System.out.println("==========final_male_female==========="
					+ final_male_female);
			System.out.println("==========location===========" + final_where);
			System.out.println("==========Description==========="
					+ final_description);

			System.out.println("==========photo===========" + encodedImage);
			System.out.println("==========importance===========" + importance);
			System.out.println("==========tag===========" + tag1);
			System.out.println("==========final_select_am_pm==========="
					+ final_select_am_pm);

			switch (final_male_female) {
			case "male":
				male_female_bool = false;
				toggle_male_female();
				break;
			case "female":
				male_female_bool = true;
				toggle_male_female();
				break;
			default:
				break;

			}

			set_importance();
			if (final_where.length() > 0) {
				faces_where_TextView.setText(final_where);
				atvPlaces.setText(final_where);
				faces_where_TextView.setTextColor(getResources().getColor(
						R.color.Green));
			} else {
				// event_where_TextView.setText("where");
			}

			/*
			 * if(tag1.length()>0) { faces_why_TextView.setText(tag1);
			 * tags_editText.setText(tag1); }else {
			 * //faces_why_TextView.setText(tag1);
			 * //event_with_TextView.setText("with"); }
			 */
			if (tag1 == null) {

			} else {
				faces_why_TextView.setText(tag1);
				tags_editText.setText(tag1);
				faces_why_TextView.setTextColor(getResources().getColor(
						R.color.Green));
			}
			if (final_description.length() > 0) {
				faces_descriptionTextView.setText(final_description);
				descriptionEditText.setText(final_description);
				faces_descriptionTextView.setTextColor(getResources().getColor(
						R.color.Green));
			} else {
				faces_descriptionTextView.setText("description");

			}

			face_name_editText.setText(final_face_name);
			// faces_descriptionTextView.setText(final_description);
			// descriptionEditText.setText(final_description);
			// atvPlaces.setText(final_where);
			// faces_where_TextView.setText(final_where);
			// faces_why_TextView.setText(tag1);
			// tags_editText.setText(tag1);

			convert_get_time1();
			convert_string_image();

			// set Update
			// /
			// setUpdate(updateid,title,location,with,audio,photo,importance,tag);

		}

	}

	private void snooze_Dialog() {
		// TODO Auto-generated method stub
		Button set_Button, editbutton,snooze_button;
		TextView textview;

		snooze_dialog = new Dialog(FacesActivity.this);

		/*
		 * alarm_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		snooze_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		snooze_dialog.setContentView(R.layout.send_email_dialog);

		set_Button = (Button) snooze_dialog.findViewById(R.id.send_button);
		
		editbutton = (Button) snooze_dialog.findViewById(R.id.edit_button);
		snooze_button= (Button) snooze_dialog.findViewById(R.id.snooze_button);
		snooze_button.setText("Edit");
		snooze_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				snooze_dialog.dismiss();
			}
		});
		textview = (TextView) snooze_dialog.findViewById(R.id.email_phone_text);
		textview.setText("    Do you want to snooze     ");
		set_Button.setText("Snooze");
		editbutton.setText("Cancel");
		//snooze_button.setVisibility(View.GONE);
		set_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				alarm_dialog();
				toggle_hour_minute();
				snooze_dialog.dismiss();

				// event_saveTextView.performClick();

			}
		});
		editbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// every_day_text.clearComposingText();
				setReminder.canceldailyReminder(context, (int)updateid);
				snooze_dialog.dismiss();
				finish();

			}
		});

		snooze_dialog.show();
	}

	private void set_importance() {
		if (importance != null) {
			switch (importance) {
			case "Low":
				priority_ImageView.setImageResource(R.drawable.importance_one);
				break;
			case "Medium":
				priority_ImageView.setImageResource(R.drawable.importance_two);
				break;
			case "High":
				priority_ImageView
						.setImageResource(R.drawable.importance_three);
				break;
			default:
				break;
			}
		}
	}

	public void convert_get_time1() {
		String reminderdate = task.getReminderDateString();
		String remindertime = task.getReminderTimeString();
		int update_hour = 0;

		ArrayList<String> arrayList = new ArrayList<String>();
		ArrayList<String> arrayList1 = new ArrayList<String>();
		String[] tokens = reminderdate.split("/");
		String[] tokens11 = remindertime.split(":");
		for (String s : tokens) {
			arrayList.add(s);

		}

		for (String s : tokens11) {
			arrayList1.add(s);

		}
		final_date = arrayList.get(0);
		final_month = arrayList.get(1);
		final_year = arrayList.get(2);
		final_hour = arrayList1.get(0);
		final_minute = arrayList1.get(1);

		final_date_month_year = final_date + "-" + final_month + "-"
				+ final_year;
		/*
		 * try { convert_time_into_long(); } catch (ParseException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		String[] months_str = { "", "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };
		int num = Integer.parseInt(final_month);
		faces_when_TextView
				.setTextColor(getResources().getColor(R.color.Green));
		/*
		 * event_when_TextView.setText("" + months_str[num] + " " + final_date +
		 * "," + final_year + " " + remindertime);
		 */
		switch (final_select_am_pm) {
		case "am":
			am_TextView
					.setTextColor(getResources().getColor(R.color.DarkGreen));
			pm_TextView.setTextColor(getResources().getColor(R.color.Gray));
			break;
		case "pm":
			if (Integer.parseInt(final_hour) == 12) {

			} else {

				int time1 = Integer.parseInt(final_hour) - 12;
				final_hour = String.valueOf(time1);
			}

			pm_TextView
					.setTextColor(getResources().getColor(R.color.DarkGreen));
			am_TextView.setTextColor(getResources().getColor(R.color.Gray));
			break;
		}
		faces_when_TextView
				.setTextColor(getResources().getColor(R.color.Green));
		faces_when_TextView.setText("" + months_str[num] + " " + final_date
				+ "," + final_year + " "
				+ new DecimalFormat("00").format(Integer.valueOf(final_hour))
				+ ":"
				+ new DecimalFormat("00").format(Integer.valueOf(final_minute))
				+ final_select_am_pm);

		hour_TextView.setText(""
				+ new DecimalFormat("00").format(Integer.valueOf(final_hour)));
		minute_TextView
				.setText(""
						+ new DecimalFormat("00").format(Integer
								.valueOf(final_minute)));

		hour_value = Integer.valueOf(final_hour);
		minute_value = Integer.valueOf(final_minute);
		setGridCellAdapterToDate(Integer.parseInt(final_month),
				Integer.parseInt(final_year));

	}

	/*
	 * public void convert_get_time() { String reminderdate =
	 * task.getReminderDateString(); String remindertime =
	 * task.getReminderTimeString();
	 * 
	 * 
	 * ArrayList<String> arrayList = new ArrayList<String>(); ArrayList<String>
	 * arrayList1 = new ArrayList<String>(); String[] tokens =
	 * reminderdate.split("/"); String[] tokens11 = remindertime.split(":"); for
	 * (String s : tokens) { arrayList.add(s);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * for (String s : tokens11) { arrayList1.add(s);
	 * 
	 * } String final_date1 = arrayList.get(0); String final_month1 =
	 * arrayList.get(1); String final_year1 = arrayList.get(2); String
	 * set_when_hour1=arrayList1.get(0); String final_minute1=arrayList1.get(1);
	 * 
	 * 
	 * String[] months_str = { "", "January", "February", "March", "April",
	 * "May", "June", "July", "August", "September", "October", "November",
	 * "December" }; int num = Integer.parseInt(final_month1);
	 * faces_when_TextView
	 * .setTextColor(getResources().getColor(R.color.Green));
	 * 
	 * event_when_TextView.setText("" + months_str[num] + " " + final_date + ","
	 * + final_year + " " + remindertime);
	 * 
	 * 
	 * faces_when_TextView .setText("" + months_str[num] + " " + final_date1 +
	 * "," + final_year1 + " " + new DecimalFormat("00").format(Integer
	 * .valueOf(set_when_hour1)) + ":" + new DecimalFormat("00").format(Integer
	 * .valueOf(final_minute1)) );
	 * 
	 * hour_TextView.setText("" + new DecimalFormat("00").format(Integer
	 * .valueOf(set_when_hour1))); minute_TextView.setText("" + new
	 * DecimalFormat("00").format(Integer .valueOf(final_minute1)) );
	 * final_minute = String.valueOf(final_minute1); final_hour =
	 * String.valueOf(set_when_hour1);
	 * hour_value=Integer.valueOf(set_when_hour1);
	 * minute_value=Integer.valueOf(final_minute1);
	 * setGridCellAdapterToDate(Integer.parseInt(final_month1),
	 * Integer.parseInt(final_year1));
	 * 
	 * }
	 */
	private void convert_string_image() {
		
		
		
		if (encodedImage != null) {

			File imgFile = new File(encodedImage);

			if (imgFile.exists()) {
				/*Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());*/
				Bitmap bit = getHexagonShape(decodeSampledBitmapFromFile(
						imgFile.getAbsolutePath(), 500, 250));
				capture_ImageView.setImageBitmap(bit);
				capture_ImageView.setVisibility(View.VISIBLE);
			
			}
		}
	}

	private void updateinfo() {

		final_face_name = face_name_editText.getText().toString();
		final_where = atvPlaces.getText().toString();
		final_description = descriptionEditText.getText().toString();

		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		String email1 = loginPreferences.getString("username", "");

		if (final_face_name.trim().length() <= 0) {

			Toast.makeText(getApplicationContext(), "Please enter Person Name",
					300).show();

			// Toast.makeText(getApplicationContext(),
			// "------"+final_date+"-"+final_month+"-"+final_year, 300).show();
			// Toast.makeText(getApplicationContext(),
			// "------"+final_hour+"-"+final_minute, 300).show();
		}

		else {
			System.out.println("========update long=id==========" + updateid);

			System.out.println("=========update title=========="
					+ final_face_name);
			System.out.println("==========update location==========="
					+ final_where);
			System.out.println("==========update with===========" + final_with);
			System.out.println("==========update gender==========="
					+ final_male_female);
			System.out.println("==========update description==========="
					+ final_description);
			System.out.println("==========update photo==========="
					+ encodedImage);
			System.out.println("==========updateimportance==========="
					+ importance);
			System.out.println("==========update tag===========" + tag1);

			// TODO Auto-generated method stub
			Task newTask = new Task(final_face_name);
			newTask.setDescription(final_description);
			newTask.setSex(final_male_female);
			newTask.setEvent_with(final_with);
			newTask.setName("Name");
			newTask.setId(updateid);
			newTask.setEvent_tag(tag1);
			newTask.setEvent_photo(encodedImage);
			newTask.setLocation(final_where);
			newTask.setImportance(importance);
			newTask.setUserId(email1);
			newTask.setSearch_date(final_date_long);
			newTask.setam_pm(final_select_am_pm);
			Calendar cal = Calendar.getInstance();

			cal.set(Integer.parseInt(final_year),
					Integer.parseInt(final_month) - 1,
					Integer.parseInt(final_date), Integer.parseInt(final_hour),
					Integer.parseInt(final_minute), 0); // Setting a new
														// Calendar instance
														// with the user
														// reminder date

			Time reminder = new Time(); // Reminder in Time format to store in
										// the object
			reminder.set(0, Integer.parseInt(final_minute),
					Integer.parseInt(final_hour), Integer.parseInt(final_date),
					Integer.parseInt(final_month) - 1,
					Integer.parseInt(final_year)); // Setting the reminder to be
													// saved in the Task object
			newTask.setReminder(reminder);
			newTask.setHasReminder(0); // Setting the boolean flag to indicate
										// that that the task has reminder

			// setReminder.setOneTimeReminder(context, cal, newTask);
			
			
			 //==================================================
			//Update Google Calendar Event
	       //=======================================================

            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(final_year), Integer.parseInt(final_month) - 1, Integer.parseInt(final_date),
                    Integer.parseInt(final_hour), Integer.parseInt(final_minute));

            if(calbool==1) {
                Uri local_uri = Uri.parse("content://com.android.calendar/events");
                Cursor cursor = getContentResolver().query(local_uri, new String[]{"MAX(_id) as max_id"}, null, null, "_id");
                cursor.moveToFirst();
                long max_val = cursor.getLong(cursor.getColumnIndex("max_id"));
                System.out.println("=========max_val===============" + max_val);


                // UpdateCalendarEntry( max_val);

                ContentValues event = new ContentValues();

                event.put("title", final_face_name);
                event.put(Events.EVENT_LOCATION, final_where);

                event.put(Events.DTSTART, System.currentTimeMillis());
                event.put(Events.DTEND, c.getTimeInMillis());
                event.put(Events.ALL_DAY, 0);   // 0 for false, 1 for true
                event.put(Events.HAS_ALARM, 1); // 0 for false, 1 for true
                // event.put("hasAlarm", 1); // 0 for false, 1 for true

                Uri baseUri;
                if (Build.VERSION.SDK_INT >= 8) {
                    baseUri = Uri.parse("content://com.android.calendar/events");
                } else {
                    baseUri = Uri.parse("content://calendar/events");
                }
                Uri eventUri = ContentUris.withAppendedId(baseUri, max_val);

                getContentResolver().update(eventUri, event, null,
                        null);

            }

			    //Log.i("Log", "Updated " + iNumRowsUpdated + " calendar entry.");
			    //==================================================
				//Update Google Calendar Event
		       //=======================================================

			/*if (set_time == true) {
				setReminder.setRepeatTimeReminder(context, cal, newTask,
						interval);
			} else {
				setReminder.setOneTimeReminder(context, cal, newTask);
			}
			setReminder
					.setProximityAlert(context, newTask, latitude, longitude);
*/

			array.updateTask(newTask, newTask.getId());
			this.finish();
		}

	}

	public void event_info() {

		final_face_name = face_name_editText.getText().toString();
		final_where = atvPlaces.getText().toString();
		final_description = descriptionEditText.getText().toString();

		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		String email1 = loginPreferences.getString("username", "");
		if (tag_ArrayList.isEmpty()) {

		} else {
			tag1 = tag_ArrayList.get(0);
		}
		Log.d("face Name", "" + final_face_name);
		Log.d("MAle or female", "" + final_male_female);
		Log.d("description=", "" + final_description);
		Log.d("where=", "" + final_where);

		Log.d("final time", "" + final_time);
		Log.d("am_pm=", "" + final_select_am_pm);

		Log.d("final_start", "" + final_date_month_year);
		Log.d("final_end", "" + final_end);
		for (String str : tag_ArrayList) {
			Log.d("tag=", "" + str);
		}

		if (final_face_name.trim().length() <= 0) {

			Toast.makeText(getApplicationContext(), "Please enter Person Name",
					300).show();

			// Toast.makeText(getApplicationContext(),
			// "------"+final_date+"-"+final_month+"-"+final_year, 300).show();
			// Toast.makeText(getApplicationContext(),
			// "------"+final_hour+"-"+final_minute, 300).show();
		}

		else {

			long rowID;

			TaskListArray array = TaskListArray.getInstance(context);
			Task newTask = new Task(final_face_name);
			newTask.setDescription(final_description);
			newTask.setSex(final_male_female);
			newTask.setEvent_with(final_with);
			newTask.setName("Name");

			newTask.setEvent_tag(tag1);
			newTask.setEvent_photo(encodedImage);
			newTask.setImportance(importance);
			newTask.setLocation(final_where);
			newTask.setUserId(email1);
			newTask.setSearch_date(final_date_long);
			newTask.setam_pm(final_select_am_pm);
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(final_year),
					Integer.parseInt(final_month) - 1,
					Integer.parseInt(final_date), Integer.parseInt(final_hour),
					Integer.parseInt(final_minute), 0); // Setting a new
														// Calendar instance
														// with the user
														// reminder date

			Time reminder = new Time(); // Reminder in Time format to store in
										// the object
			reminder.set(0, Integer.parseInt(final_minute),
					Integer.parseInt(final_hour), Integer.parseInt(final_date),
					Integer.parseInt(final_month) - 1,
					Integer.parseInt(final_year)); // Setting the reminder to be
													// saved in the Task object
			newTask.setReminder(reminder);
			newTask.setHasReminder(0); // Setting the boolean flag to indicate
										// that that the task has reminder

			rowID = taskDataBase.addTask(newTask); // Adding to DataBase
			newTask.setId(rowID);
			array.addTask(newTask);
			
			

			 //=====================================================
			// add Event in Google Calendar
          //=======================================================
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(final_year), Integer.parseInt(final_month) - 1, Integer.parseInt(final_date),
                    Integer.parseInt(final_hour), Integer.parseInt(final_minute));
            if(calbool==1) {
                final ContentValues event = new ContentValues();
                event.put(Events.CALENDAR_ID, 1);

                event.put(Events.TITLE, final_face_name);
                //event.put(Events.DESCRIPTION, "Description");
                event.put(Events.EVENT_LOCATION, final_where);

                event.put(Events.DTSTART, System.currentTimeMillis());
                event.put(Events.DTEND, c.getTimeInMillis());
                event.put(Events.ALL_DAY, 0);   // 0 for false, 1 for true
                event.put(Events.HAS_ALARM, 1); // 0 for false, 1 for true

                String timeZone = TimeZone.getDefault().getID();
                event.put(Events.EVENT_TIMEZONE, timeZone);

                Uri baseUri;
                if (Build.VERSION.SDK_INT >= 8) {
                    baseUri = Uri.parse("content://com.android.calendar/events");
                } else {
                    baseUri = Uri.parse("content://calendar/events");
                }

                getContentResolver().insert(baseUri, event);


                AccountManager aM = AccountManager.get(this);
                Account[] accounts = aM.getAccounts();

                for (Account account : accounts) {
                    int isSyncable = getContentResolver().getIsSyncable(account, CalendarContract.AUTHORITY);

                    if (isSyncable > 0) {
                        Bundle extras = new Bundle();
                        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                        getContentResolver().requestSync(accounts[0], CalendarContract.AUTHORITY, extras);
                    }
                }
            }
			
			 //=====================================================
			// add Event in Google Calendar
          //=======================================================
			

			// setReminder.setOneTimeReminder(context, cal, newTask); //
			// Creating a
			// new
			// reminder
			// to be
			// added to
			// the alarm
			// manager
			if (save_in_database == true) {
				/*Toast.makeText(getApplicationContext(), "Saved in database",
						300).show();*/
			} else {

				/*if (set_time == true) {
					setReminder.setRepeatTimeReminder(context, cal, newTask,
							interval);
				} else {
					setReminder.setOneTimeReminder(context, cal, newTask);
				}*/
			}
			// Toast.makeText(getApplicationContext(),
			// "------"+final_date+"-"+final_month+"-"+final_year, 300).show();
			// Toast.makeText(getApplicationContext(),
			// "------"+final_hour+"-"+final_minute, 300).show();
			finish();
		}

	}

	/*-------------Camera Functionality----------------------*/

	public void open_gallery_dialog() {
		/**
		 * open dialog for choose camera/gallery
		 */

		final String[] option = new String[] { "Take from Camera",
				"Select from Gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, option);
		Builder builder = new Builder(this);

		builder.setTitle("Select Option");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.e("Selected Item", String.valueOf(which));
				if (which == 0) {
					// callCamera();
					//call_new();
					
					Intent in =new Intent(FacesActivity.this, ImageCaptureActivity.class);
					startActivityForResult(in, CAMERA_REQUEST);


				}
				if (which == 1) {
					callGallery();

				}

			}
		});
		dialog = builder.create();

	}

	public void call_new() {
		timeinmilisecod = System.currentTimeMillis();
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "image" + timeinmilisecod + ".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, CAMERA_REQUEST);

		/*
		 * Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		 * startActivityForResult(intent, CAMERA_REQUEST);
		 */

	}

	// create helping method cropCapturedImage(Uri picUri)
	public void cropCapturedImage(Uri picUri) {
		// call the standard crop action intent
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		// indicate image type and Uri of image
		cropIntent.setDataAndType(picUri, "image/*");
		// set crop properties
		cropIntent.putExtra("crop", "true");
		// indicate aspect of desired crop
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		// indicate output X and Y
		cropIntent.putExtra("outputX", 256);
		cropIntent.putExtra("outputY", 256);
		// retrieve data on return
		cropIntent.putExtra("return-data", true);
		// start the activity - we handle returning in onActivityResult
		startActivityForResult(cropIntent, 2);
	}

	/**
	 * open gallery method
	 */
	public void callGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");

		intent.setAction(Intent.ACTION_GET_CONTENT);
		/*
		 * intent.putExtra("crop", "true"); intent.putExtra("aspectX", 0);
		 * intent.putExtra("aspectY", 0); intent.putExtra("outputX", 200);
		 * intent.putExtra("outputY", 150); intent.putExtra("return-data",
		 * true);
		 */
		startActivityForResult(
				Intent.createChooser(intent, "Complete action using"),
				PICK_FROM_GALLERY);

	}

	private Uri getImageUri() {
		Uri m_imgUri = null;
		File m_file;
		try {
			SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String m_curentDateandTime = m_sdf.format(new Date());
			String m_imagePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ m_curentDateandTime
					+ ".jpg";
			m_file = new File(m_imagePath);
			m_imgUri = Uri.fromFile(m_file);
		} catch (Exception p_e) {
		}
		return m_imgUri;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		/*
		 * super.onActivityResult(requestCode, resultCode, data); if (resultCode
		 * != RESULT_OK) return;
		 */
		// Toast.makeText(getApplicationContext(),
		// "resultcode="+resultCode+"requestcode="+requestCode,Toast.LENGTH_SHORT).show();
		if (resultCode == 300) {

			latitude = data.getDoubleExtra("Lat", -1);
			longitude = data.getDoubleExtra("Lng", -1);
			address = data.getStringExtra("location");

			atvPlaces.setText(address);
			// Toast.makeText(getApplicationContext(), ""+latitude,
			// Toast.LENGTH_SHORT).show();
			// Toast.makeText(getApplicationContext(), ""+latitude,
			// Toast.LENGTH_SHORT).show();
			// Toast.makeText(getApplicationContext(), ""+longitude,
			// Toast.LENGTH_SHORT).show();
			// Toast.makeText(getApplicationContext(), address,
			// Toast.LENGTH_SHORT).show();

		}



        if(requestCode==4){
            // img_path=data.getStringExtra("result");



            // Toast.makeText(this,"Image saved"+img_path,Toast.LENGTH_SHORT).show();


            img_path="/storage/emulated/0/M_CamScanner"+File.separator+ ImageEditor.names;

            System.out.println("===path===================" + img_path);

            Bitmap bi = getHexagonShape(decodeSampledBitmapFromFile(
                    img_path, 500, 250));
            capture_ImageView.setImageBitmap(bi);
            capture_ImageView.setVisibility(View.VISIBLE);
            encodedImage = img_path;
           // addimages();
          //  setAdapter();
        }
		switch (requestCode) {

		case CAMERA_REQUEST:
		//	convert_graphic_hexagonal();
			//File file = new File(Environment.getExternalStorageDirectory()
			//		+ File.separator + "image" + timeinmilisecod + ".jpg");

			//img_path = file.getAbsolutePath();
			//System.out.println("===path===================" + img_path);
			
			if(data!=null){
				 img_path=data.getStringExtra("result");
				System.out.println("===path===================" + img_path);
			
					Bitmap bi = getHexagonShape(decodeSampledBitmapFromFile(
							img_path, 500, 250));
					capture_ImageView.setImageBitmap(bi);
					capture_ImageView.setVisibility(View.VISIBLE);
					encodedImage = img_path;
					//addimages();
					//setAdapter();
				}
			/*if(file.exists()){
			
			Bitmap bi = getHexagonShape(decodeSampledBitmapFromFile(
					file.getAbsolutePath(), 500, 250));
			capture_ImageView.setImageBitmap(bi);
			capture_ImageView.setVisibility(View.VISIBLE);

			encodedImage = img_path;
			}*/
			break;
		case PICK_FROM_GALLERY:
			System.out.println("data" + data);
			if (data != null) {
				Uri selectedImage = data.getData();

				// capture_ImageView.setImageURI(selectedImage);
				try {
					// Get the file path from the URI
					img_path = FileUtils.getPath(this, selectedImage);
					

					if (img_path != null) {
						File f=new File(img_path);
						Bitmap bit = getHexagonShape(decodeSampledBitmapFromFile(f.getAbsolutePath(), 500, 250));
						capture_ImageView.setImageBitmap(bit);
						capture_ImageView.setVisibility(View.VISIBLE);
						encodedImage = img_path;
					}
					
				} catch (Exception e) {
					Log.e("FileSelectorTestActivity", "File select error", e);
				}
			}
			break;
		}
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) { // BEST QUALITY MATCH

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inPreferredConfig = Config.RGB_565;
		int inSampleSize = 1;

		if (height > reqHeight) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		}

		int expectedWidth = width / inSampleSize;

		if (expectedWidth > reqWidth) {
			// if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
			// If bigger SampSize..
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}

		options.inSampleSize = inSampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	public void convert_graphic_hexagonal() {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.faces_icon_shape);
		// get hexagonal shape
		Bitmap b = getHexagonShape(bitmap);
		// set image in imageview
		capture_ImageView.setImageBitmap(b);
	}

	public Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o);

		final int REQUIRED_SIZE = 100;

		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o2);
	}

	public void fg() {
		File f = new File(Environment.getExternalStorageDirectory().toString());
		for (File temp : f.listFiles()) {
			if (temp.getName().equals("temp.jpg")) {
				f = temp;
				break;
			}
		}
		try {

			Bitmap bitmap;
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

			bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
					bitmapOptions);
			// USED FOR CROP
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, 90, 90);

			// get hexagonal shape
			Bitmap b = getHexagonShape(bitmap);
			// set image in imageview
			capture_ImageView.setImageBitmap(b);

			String path = Environment.getExternalStorageDirectory()
					+ File.separator + "Phoenix" + File.separator + "default";
			f.delete();
			OutputStream outFile = null;
			File file = new File(path, String.valueOf(System
					.currentTimeMillis()) + ".jpg");
			try {
				outFile = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
				outFile.flush();
				outFile.close();
				//

				capture_ImageView.setImageBitmap(b);

				capture_ImageView.setVisibility(View.VISIBLE);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close_all() {
		when_on = true;

		description_on = true;
		where_on = true;
		slice_on = true;
		// when_plus_minus();
		where_plus_minus();
		slice_plus_minus();
		description_plus_minus();
	}

	public void importance_dialog() {
		LinearLayout lowLinearLayout, highLinearLayout, mediumLinearLayout;
		importance_dialog = new Dialog(FacesActivity.this);
		/*
		 * alarm_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		importance_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		importance_dialog.setContentView(R.layout.importance_dialog);
		lowLinearLayout = (LinearLayout) importance_dialog
				.findViewById(R.id.low_layout);
		mediumLinearLayout = (LinearLayout) importance_dialog
				.findViewById(R.id.medium_layout);
		highLinearLayout = (LinearLayout) importance_dialog
				.findViewById(R.id.high_layout);
		lowLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				importance_dialog.dismiss();
				importance = "Low";
				priority_ImageView.setImageResource(R.drawable.importance_one);
			}
		});
		mediumLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				importance_dialog.dismiss();
				importance = "Medium";
				priority_ImageView.setImageResource(R.drawable.importance_two);
			}
		});
		highLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				importance_dialog.dismiss();
				importance = "High";
				priority_ImageView
						.setImageResource(R.drawable.importance_three);
			}
		});
		importance_dialog.show();
	}

	/* Bottom Section */
	public void priority_dialog() {

		Builder builder = new Builder(this);
		builder.setTitle("Importance");

		ListView priorityListView = new ListView(this);
		priorityListView.setAdapter(new Priority_dialog_adapter(this));
		priorityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				/*
				 * Toast.makeText( getApplicationContext(), "" +
				 * Priority_dialog_adapter.priority_text_arr[position],
				 * 300).show();
				 */
			}
		});
		builder.setView(priorityListView);

		/*
		 * CharSequence colors[] = new CharSequence[] {"low", "medium", "high"};
		 * builder.setItems(colors, new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { //
		 * TODO Auto-generated method stub
		 * Toast.makeText(getApplicationContext(), ""+which, 300).show(); } });
		 */
		builder.show();
	}

	@SuppressLint("NewApi")
	public void alarm_dialog() {

		RelativeLayout relativeLayout;
		ImageView upImageView, downImageView;

		Button set_alarm_Button;
		alarm_dialog = new Dialog(FacesActivity.this);
		/*
		 * alarm_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		alarm_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alarm_dialog.setContentView(R.layout.alarm_reminder);
		alarm_hour_minuteTextView = (TextView) alarm_dialog
				.findViewById(R.id.alarm_minute_hour_text);
		set_alarm_Button = (Button) alarm_dialog
				.findViewById(R.id.set_alarm_btn);
		upImageView = (ImageView) alarm_dialog.findViewById(R.id.alarm_up);
		downImageView = (ImageView) alarm_dialog.findViewById(R.id.alarm_down);
		alarm_minute_text = (TextView) alarm_dialog
				.findViewById(R.id.alarm_minute_text);
		alarm_hour_text = (TextView) alarm_dialog
				.findViewById(R.id.alarm_hour_text);
		alarm_minute_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toggle_hour_minute();
			}
		});
		alarm_hour_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toggle_hour_minute();
			}
		});
		set_alarm_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), ""+alarm_final_value,
				// 300).show();
				if (alarm_hour_minuteTextView.getText().toString().trim()
						.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"please enter some value", 300).show();

				} else if (Integer.parseInt(alarm_hour_minuteTextView.getText()
						.toString()) == 0) {
					Toast.makeText(getApplicationContext(),
							"you can't set zero", 300).show();
				} else {
					alarm_final_value = Integer
							.parseInt(alarm_hour_minuteTextView.getText()
									.toString());

					

					if (minute_hour_bool == true) {
						// minute
						if (alarm_final_value <= 60) {
							interval = 1000 * 60 * alarm_final_value;
							set_time = true;
							alarm_dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(),
									"please enter right minute", 300).show();
						}
					} else {
						// hour
						if (alarm_final_value <= 12) {
							interval = 1000 * 60 * 60 * alarm_final_value;
							set_time = true;
							alarm_dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(),
									"please enter right hour", 300).show();

						}
					}
				}
				/*
				 * // code end if (minute_hour_bool == true) {
				 * 
				 * Toast.makeText(getApplicationContext(), "minute=" +
				 * alarm_final_value, 300).show();
				 * 
				 * interval = 1000 * 60 * alarm_final_value;
				 * 
				 * Toast.makeText(getApplicationContext(), "interval=" +
				 * interval, 300).show();
				 * 
				 * } else {
				 * 
				 * Toast.makeText(getApplicationContext(), "hour=" +
				 * alarm_final_value, 300).show();
				 * 
				 * interval = 1000 * 60 * 60 * alarm_final_value;
				 * 
				 * Toast.makeText(getApplicationContext(), "interval=" +
				 * interval, 300).show();
				 * 
				 * }
				 */

				if (snooze_dialog_id == 6) {

					save_snooze();
					finish();
				}
				

			}
		});
		upImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				invoke_minute_hour_up();
			}
		});
		downImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				invoke_minute_hour_down();
			}
		});

		alarm_dialog.show();

	}

	protected void save_snooze() {
		// TODO Auto-generated method stub
		try {

			// save

			String minute = minute_TextView.getText().toString();

			String hour = hour_TextView.getText().toString();
			if (Integer.parseInt(hour) <= 12 && Integer.parseInt(minute) < 60) {

				convert_date_into_long();
				convert_time_into_long();
				get_not_check();
			} else {
				Toast.makeText(getApplicationContext(),
						"please Select right hour minute", 300).show();
			}

		}

		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void get_not_check() throws ParseException {
		// TODO Auto-generated method stub
		ArrayList<String> arrayList1 = new ArrayList<String>();
		String str1 = final_date_month_year;
		String[] tokens = str1.split("-");
		for (String s : tokens) {
			arrayList1.add(s);
		}
		final_date = arrayList1.get(0);
		final_month = arrayList1.get(1);
		final_year = arrayList1.get(2);

		String str2 = final_date + "/" + final_month + "/" + final_year;

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		String strDate = sdf.format(c.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date2 = formatter.parse(strDate);
		Date date1 = formatter.parse(str2);

		Add_text();
		tags_editText.getText().clear();
		//audio_into_byte();
		close_all();
		if (updateid > 0) {
			System.out.println("=======sunil============="+updateid);
			updateinfo();
		} else {
			// Toast.makeText(getApplicationContext(), "save"+final_hour,
			// 300).show();
			event_info();
		}
	}

	public void alarm_minute_up_click() {
		if (alarm_final_value == 60) {

			alarm_final_value = 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {
			alarm_final_value = alarm_final_value + 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void alarm_minute_down_click() {
		if (alarm_final_value == 1) {

			alarm_final_value = 60;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {
			alarm_final_value = alarm_final_value - 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void alarm_hour_up_click() {

		if (alarm_final_value == 12) {

			alarm_final_value = 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {
			alarm_final_value = alarm_final_value + 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void alarm_hour_down_click() {
		if (alarm_final_value == 1) {

			alarm_final_value = 12;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {
			alarm_final_value = alarm_final_value - 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void toggle_hour_minute() {
		// if am_pm_bool = true=pm else false = am

		// alarm_hour_minuteTextView.setText("00");
		if (minute_hour_bool == false) {
			alarm_minute_text.setTextColor(getResources().getColor(
					R.color.Green));
			alarm_hour_text.setTextColor(getResources().getColor(R.color.Gray));
			minute_hour_bool = true;
			alarm_final_value = 60;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {

			alarm_hour_text
					.setTextColor(getResources().getColor(R.color.Green));
			alarm_minute_text.setTextColor(getResources()
					.getColor(R.color.Gray));
			minute_hour_bool = false;
			alarm_final_value = 12;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void invoke_minute_hour_up() {
		if (minute_hour_bool == true) {

			alarm_minute_up_click();

		} else {

			alarm_hour_up_click();

		}
	}

	public void invoke_minute_hour_down() {
		if (minute_hour_bool == true) {

			alarm_minute_down_click();

		} else {

			alarm_hour_down_click();

		}
	}

	/*-------Start end-------*/

	public void start_end_click() {
		if (start_bool == true) {
			startTextView.setTextColor(getResources().getColor(R.color.Gray));
			endTextView.setTextColor(getResources().getColor(R.color.Green));

			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);

		} else {
			startTextView.setTextColor(getResources().getColor(R.color.Green));
			endTextView.setTextColor(getResources().getColor(R.color.Gray));
		}
	}

	/*-------Start end functionality -------*/

	public void set_when_text() {

		ArrayList<String> arrayList = new ArrayList<String>();
		String str = final_date_month_year;
		String[] tokens = str.split("-");
		for (String s : tokens) {
			arrayList.add(s);
		}
		final_date = arrayList.get(0);
		final_month = arrayList.get(1);
		final_year = arrayList.get(2);

		String[] months_str = { "", "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };
		int num = Integer.parseInt(final_month);
		faces_when_TextView
				.setTextColor(getResources().getColor(R.color.Green));
		faces_when_TextView
				.setText(""
						+ months_str[num]
						+ " "
						+ final_date
						+ ", "
						+ final_year
						+ " "
						+ new DecimalFormat("00").format(Integer
								.valueOf(set_when_hour))
						+ ":"
						+ new DecimalFormat("00").format(Integer
								.valueOf(final_minute)) + final_select_am_pm);
	}

	public void set_where_text() {
		faces_where_TextView.setTextColor(getResources()
				.getColor(R.color.Green));
		if (atvPlaces.getText().toString().length() == 0) {
			faces_where_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			faces_where_TextView.setText("where");
		} else {
			faces_where_TextView.setText("" + atvPlaces.getText().toString());
		}
	}

	public void set_description_text() {

		faces_descriptionTextView.setTextColor(getResources().getColor(
				R.color.Green));
		if (descriptionEditText.getText().toString().length() == 0) {
			faces_descriptionTextView.setTextColor(getResources().getColor(
					R.color.Gray));
			faces_descriptionTextView.setText("description");
		} else {
			faces_descriptionTextView.setText(""
					+ descriptionEditText.getText().toString());
		}
	}

	public void hour_validation() {

		String hour = hour_TextView.getText().toString();

		String minute = minute_TextView.getText().toString();
		if (hour.trim().length() == 0) {
			Toast.makeText(getApplicationContext(), "please enter hour value",
					300).show();

		} else if (minute.trim().length() == 0) {
			Toast.makeText(getApplicationContext(),
					"please enter minute value", 300).show();
		} else if (Integer.parseInt(hour) > 12) {
			Toast.makeText(getApplicationContext(),
					"please enter hour less than 12", 300).show();

		} else {

			if (Integer.parseInt(hour) <= 12 && Integer.parseInt(minute) < 60) {

				final_hour = hour;

				set_when_hour = final_hour;
				when_ImageView.setBackgroundResource(R.drawable.plus);
				when_calender_view.setVisibility(View.GONE);
				when_on = false;
				set_when_text();
				/*
				 * if (only_when_on == true) {
				 * 
				 * set_when_text(); // only_when_on=false; }
				 */
			}
		}

	}

	public void minute_validation() {

		String minute = minute_TextView.getText().toString();
		String hour = hour_TextView.getText().toString();
		if (minute.trim().length() == 0) {
			Toast.makeText(getApplicationContext(), "please enter minute", 300)
					.show();

		} else if (Integer.parseInt(minute) >= 60) {
			Toast.makeText(getApplicationContext(),
					"please enter minute less than 60", 300).show();

		} else {
			// hour_value=Integer.parseInt(hour);
			if (Integer.parseInt(hour) <= 12 && Integer.parseInt(minute) < 60) {
				final_minute = minute;

				/*
				 * Toast.makeText(getApplicationContext(),
				 * "please "+final_hour+":"+final_minute, 300) .show();
				 */
				when_ImageView.setBackgroundResource(R.drawable.plus);
				when_calender_view.setVisibility(View.GONE);
				when_on = false;
				set_when_text();
				/*
				 * if (only_when_on == true) {
				 * 
				 * set_when_text(); // only_when_on=false; }
				 */
			}
		}

	}

	public void whole_note_data() {
		final_face_name = face_name_editText.getText().toString();
		final_where = atvPlaces.getText().toString();
		final_description = descriptionEditText.getText().toString();

		String[] months_str = { "", "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };
		int num = Integer.parseInt(final_month);

		final_where = atvPlaces.getText().toString();

		String final_when = ""
				+ months_str[num]
				+ " "
				+ final_date
				+ ", "
				+ final_year
				+ " "
				+ new DecimalFormat("00")
						.format(Integer.valueOf(set_when_hour)) + ":"
				+ new DecimalFormat("00").format(Integer.valueOf(final_minute))
				+ final_select_am_pm;
		// set_final_end();
		if (tag_ArrayList.isEmpty()) {

		} else {
			tag1 = tag_ArrayList.get(0);
		}
		str = new StringBuilder("\n");

		str.append("When :" + final_when + "\n");
		str.append("Where :" + final_where + "\n");
		str.append("Text :" + final_description + "\n");
		str.append("Why :" + tags_editText.getText().toString() + "\n");
		
		
		// Toast.makeText(getApplicationContext(), ""+str, 300).show();
		System.out.println("" + str);

	}

	public void share_image() {
		if (encodedImage != null) {

			File imgFile = new File(encodedImage);

			if (imgFile.exists()) {

				imageuri = Uri.fromFile(imgFile);
				System.out.println("immmmm" + imageuri);

			}

		}

	}

	public void share_note() {
		whole_note_data();

		share_image();
		ArrayList<Uri> imageUris = new ArrayList<Uri>();
		if (imageuri != null) {
			imageUris.add(imageuri);
		}

		Intent shareintent = new Intent();
		if (imageUris != null) {
			shareintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
					imageUris);
		}

		shareintent.putExtra(Intent.EXTRA_TEXT, str.toString());
		shareintent.putExtra(Intent.EXTRA_SUBJECT, "Faces");
		if (imageuri != null) {
			shareintent.setAction(Intent.ACTION_SEND_MULTIPLE);
			shareintent.setType("*/*");
		} else {

			shareintent.setAction(Intent.ACTION_SEND);
			shareintent.setType("text/plain");
		}

		/*
		 * shareintent.putExtra(android.content.Intent.EXTRA_SUBJECT,
		 * "share subject");
		 * shareintent.putExtra(android.content.Intent.EXTRA_TEXT, "shareBody");
		 */

		if (shareintent.resolveActivity(getPackageManager()) != null) {
			startActivity(Intent.createChooser(shareintent, "Share event"));
		}

	}
}
