package event;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Base64;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.life_reminder.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

import adapter.Audio_horizontal_Adapter;
import adapter.ContactAdapter;
import adapter.ContactItem;
import adapter.SelectUserAdapter;
import adapter.horizontal_Adapter;
import alarm.SetReminder;
import app.ConnectionDetector;
import camerawork.ImageCaptureActivity;
import camerawork.ImageEditor;
import data.AudioData;
import data.PhotoData;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;
import getset.SelectUser;
import it.sephiroth.android.library.widget.HListView;
import place.PlaceJSONParser;
import reminder.CreateGoogleMap;


//Niteshp552@gmail.com
public class EventActivity extends FragmentActivity implements OnClickListener,
		OnPreparedListener, OnItemClickListener,
		it.sephiroth.android.library.widget.AdapterView.OnItemClickListener {
	
	ContactAdapter chipsAdapter;
	ArrayList<Uri> imageUris, audioUris;
	long imageid;
	
	String img_path;
    adapter.horizontal_Adapter	 horizontal_Adapter;
	Audio_horizontal_Adapter audio_horizontal_Adapter;

	Activity activity;
	HListView horizontallistview, audio_horizontallistview;
	int img_position;
	//
	Dialog snooze_dialog;
	int snooze_dialog_id;
	/* share whole note */
	Uri imageuri, audiouri;
	StringBuilder str;
	String selected_items;
	/* share end whole note */

	/* Date Dialog */
	DatePickerFragment date;
	int dialog_day, dialog_month, dialog_year;
	/* end date dialog */
	Builder builder1;
	// DatePickerDialog datePickerDialog;
	File destination;

	TextView days_month_text;
	private EditText dateView;
	private Calendar calendar;
	private int calendar_year, calendar_month, calendar_day;
	int repeat_day;
	Dialog weekly_dialog;
	TextView week_save_text;
	TextView sunTextView, monTextView, tuesTextView, wedTextView, thuTextView,
			friTextView, satTextView;
	TextView week_arr[] = { sunTextView, monTextView, tuesTextView,
			wedTextView, thuTextView, friTextView, satTextView };
	int week_id[] = { R.id.sun_text, R.id.mon_text, R.id.Tues_text,
			R.id.Wed_text, R.id.Thu_text, R.id.Fri_text, R.id.Sat_text };

	boolean set_time;
	ArrayList<Integer> seletedItems;
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	ScrollView scrollView, cal_scrollview;
	// Connection detector class
	ConnectionDetector cd;

	static long interval;
	int targetWidth = 140;
	int targetHeight = 140;
	TextView startTextView, endTextView;
	String final_start, final_until_end, previous_until_end;
	boolean start_bool, end;
	/* Bottom Section view */
	Dialog alarm_dialog;
	Dialog importance_dialog, daily_dialog;
	ImageView priority_ImageView, alarm_ImageView;
	TextView alarm_hour_minuteTextView;
	TextView alarm_minute_text, alarm_hour_text;
	int alarm_final_value;
	boolean minute_hour_bool;
	/* Bottom Section */
	ImageView mMapButton;
	// test
	Button playbutton;
	boolean click;
	public byte[] fileByteArray;
	long rowID;
	// value
	String event_text;
	String final_date, final_month, final_year, final_date_month_year,
			final_hour, final_minute, final_time, set_when_hour,
			final_date_long, final_end_date, final_end_month, final_end_year;
	int current_date, current_hour, current_minute;
	String final_select_am_pm, final_repeat, final_with, final_where,
			final_repeat_every_day_month;
	ArrayList<String> tag_ArrayList = new ArrayList<String>();
	public static ArrayList<PhotoData> img_ArrayList;
	ArrayList<AudioData> audio_ArrayList;
	/* Slice */

	LinearLayout sliceLinearLayout;
	ImageView slice_plus_img;
	View slice_tag_view;
	boolean slice_on;

	EditText tags_editText;
	TextView save_Button, event_slice_text;
	LinearLayout linearLayout, ll;
	int i = 0;
	/* ------------------with Fuctionality -------------------- */

	LinearLayout mWhereLayout;

	View where_view_location;

	MultiAutoCompleteTextView atvPlaces;
	PlacesTask placesTask;
	ParserTask parserTask;
	ListView mListview;
	SimpleAdapter Location_adapter;

	ImageView where_plus_img;
	ImageView with_plus_img;

	// event With Description
	LinearLayout mWithLayout;
	View with_contact;
    TextView mContactListTextview;

	ArrayList<ContactItem> contactList = new ArrayList<ContactItem>();

	ListView mContactListview;

	/*----------------------	start here for audio dilaog--------------------------------------*/

	private boolean running = true;
	private int current = 0;
	private int duration = 0;
	ImageView audioImageView;
	Dialog audiodialog;
	ImageView start_recordingImageView, start_playImageView, delete_ImageView,
			share_audioImageView;
	TextView recording_TextView, recording_total_time_TextView,
			cancel_recordingTextView, save_recordingTextView;
	private MediaRecorder myRecorder;
	private MediaPlayer myPlayer;
	boolean isrecord, isplay, ispause;

	private String outputFile = null;
	String encoded_audio_string;
	LinearLayout saveLinearLayout;

	// timer

	private long startTime = 0L;

	private Handler customHandler = new Handler();

	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;

	// seek bar
	private SeekBar mSeekBarPlayer;

	/*--------------------------------------------	end here for audio dilaog-----------------------------------------------------------------------*/

	// Event Coding

	EditText event_EditText;
	LinearLayout whenLinearLayout, image_sound_layout1;
	ImageView when_ImageView, capture_audio_ImageView;
	View visible_view;
	TextView event_saveTextView, event_when_TextView, event_where_TextView,
			event_with_TextView, complete_saveTextView, share_TextView;
	boolean when_on, am_pm_bool, repeat_bool, where_on, with_on, when_close;

	View when_calender_view;
	String selected_month, selected_day, selected_year;

	// calender
	ImageView hour_up_ImageView, hour_down_ImageView, minute_up_ImageView,
			minute_down_ImageView;
	TextView am_TextView, pm_TextView, daily_TextView, weekly_TextView,
			monthly_TextView, yearly_TextView;
	EditText hour_TextView, minute_TextView;
	int hour_value = 12;
	int minute_value = 60;
	int event_repeat_value, am_pm_value;
	// event end codingmm

	// camera_coding start
	AlertDialog dialog;
	// captured picture uri
	private Uri picUri;
	ImageView event_camera_img, capture_ImageView;
	Builder builder;
	private static final int CAMERA_REQUEST = 4;
	// keep track of cropping intent
	private static final int PIC_CROP = 3;
	private static final int PICK_FROM_GALLERY = 2;
	FileInputStream fis;
	String encodedImage;
	// camera_coding
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
	ImageView event_img;
	//InputMethodManager imm;

	// send Set Event

	private TaskListDataBase taskDataBase;
	private SetReminder setReminder;
	private TaskListArray array;
	private Context context;
	private Task task;
	private PhotoData photoData;
	private double latitude;
	private double longitude;
	private String address; // Empty string represent NONE

	private String importance, tag1;

	long updateid;
	long timeinmilisecod;
	private SharedPreferences loginPreferences;
	private Editor loginPrefsEditor;
	
	  // Pop up
    ContentResolver resolver;
    Cursor phones, email;
    // ArrayList
    ArrayList<SelectUser> selectUsers;
    SelectUserAdapter adapter1;
    private ProgressDialog pDialog;
    private final int TRIGGER_SERACH = 1;
    private final long SEARCH_TRIGGER_DELAY_IN_MS = Toast.LENGTH_SHORT;
   String searchText;

   boolean setSnooze;
   long audioid;
    int calbool;
	/** Called when the activity is first created.; */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_cal);
		setContentView(R.layout.activity_event);
		show_dialog();

		this.activity = activity;
		seletedItems = new ArrayList();
		img_ArrayList = new ArrayList<PhotoData>();
		audio_ArrayList = new ArrayList<AudioData>();
		
		
		
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());
		audioUris = new ArrayList<Uri>();
		// database
		context = getBaseContext();
		setReminder = new SetReminder();
		taskDataBase = TaskListDataBase.getInstance(getBaseContext()); // Getting
		// database
		// instance

		/*imm = (InputMethodManager) this
				.getSystemService(Service.INPUT_METHOD_SERVICE);*/

		/* - Begin code for custom Action bar */
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.event_fn_back_icon);

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View customview = layoutInflater.inflate(R.layout.action_bar, null);
		event_img = (ImageView) customview.findViewById(R.id.event_img);

		actionBar.setCustomView(customview);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// actionBar.setDisplayHomeAsUpEnabled(true);
		/* - End code for custom Action bar End */
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

				if (start_bool == true) {
					final_until_end = date_month_year;
					get_end_date();
					// Toast.makeText(context, "end"+final_end, Toast.LENGTH_SHORT).show();
				} else {
					final_date_month_year = date_month_year;
					// Toast.makeText(context, "final"+final_date_month_year,
					// Toast.LENGTH_SHORT).show();
				}

				// show selected date
				// Toast.makeText(getApplicationContext(), "" + date_month_year,
				// Toast.LENGTH_SHORT).show();

			}
		});
		find_id();
		setCurrent_time();
		get_am_pm();
		// toggle_repeat();
		open_gallery_dialog();

		// get specific data read oldstuff Activity and Home Activity

		GetOldStuffData();
		daily_monthly_dialog();
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
			
			
			
			
		
			task = taskDataBase.getTask(updateid); // Getting the task from the
													// database

			event_text = task.getTitle();
			final_where = task.getLocation();
			final_with = task.getEvent_with();
			//encoded_audio_string = task.getEvent_Audio();
			//encodedImage = task.getEvent_photo();

			importance = task.getImportance();
			tag1 = task.getEvent_tag();

			final_repeat = task.getFinal_repeat();
			
			System.out.println("=======final_repeat============"+final_repeat);
			
			final_repeat_every_day_month = task.getFinal_repeat_day_month();
			final_until_end = task.getFinal_until_date();
			previous_until_end = final_until_end;

			final_select_am_pm = task.getam_pm();
			selected_items = task.getSelected_items();
			
			//img_ArrayList.clear();
			
			img_ArrayList = taskDataBase.getAllPhoto(updateid);
			
			System.out.println("======load event activity===img_ArrayList========"+img_ArrayList.size());
			setAdapter();
			
			audio_ArrayList = taskDataBase.getAllAudio(updateid);
			System.out.print("audio list ="+audio_ArrayList);
			setAudioAdapter();
			
/*
			System.out.println("========long=id==========" + updateid);

			System.out.println("=========title==========" + event_text);
			System.out.println("==========location===========" + final_where);
			System.out.println("==========with===========" + final_with);
			System.out.println("==========audio==========="
					+ encoded_audio_string);
			System.out.println("==========photo===========" + encodedImage);
			System.out.println("==========importance===========" + importance);
			System.out.println("==========tag===========" + tag1);
			System.out.println("==========get final_select_am_pm==========="
					+ final_select_am_pm);

			System.out.println("==========get final_repeat==========="
					+ final_repeat);
			System.out
					.println("==========get final_repeat_every_day_month==========="
							+ final_repeat_every_day_month);
			System.out.println("==========get final_until_end==========="
					+ final_until_end);
			System.out.println("========Selected items=========="
					+ selected_items);*/
			/*
			 * Toast.makeText(getApplicationContext(), "where" + final_where,
			 * Toast.LENGTH_SHORT) .show();
			 */
			if (selected_items != null) {
				get_selected_items();
			}

			set_importance();
			convert_get_time1();
			set_repeat();
			
			  if(final_repeat != null && !final_repeat.isEmpty() && !final_repeat.equals("null")){
			toggle_repeat();
			  }
			get_end_date();

			if (final_where.length() > 0) {

				event_where_TextView.setText(final_where);
				atvPlaces.setText(final_where);
				event_where_TextView.setTextColor(getResources().getColor(
						R.color.Green));
			} else {
				// event_where_TextView.setText("where");
			}
			if (final_with.length() > 0) {

				event_with_TextView.setText(final_with);
				mContactListTextview.setText(final_with);
				event_with_TextView.setTextColor(getResources().getColor(
						R.color.Green));
			} else {
				// event_with_TextView.setText("with");
			}
			if (tag1 == null) {

			} else {
				event_slice_text.setText(tag1);
				tags_editText.setText(tag1);
				event_slice_text.setTextColor(getResources().getColor(
						R.color.Green));
			}
			/*
			 * if(tag1.length()>0 || tag1 !=null) {
			 * event_slice_text.setText(tag1); tags_editText.setText(tag1);
			 * }else { //event_slice_text.setText(tag1);
			 * //event_with_TextView.setText("with"); }
			 */

			event_EditText.setText(event_text);
			/*
			 * convert_string_image();
			 * 
			 * if (encoded_audio_string != null) { // show audio image a
			 * capture_audio_ImageView.setVisibility(View.VISIBLE); Bitmap
			 * bitmap = BitmapFactory.decodeResource( context.getResources(),
			 * R.drawable.event_audio_attachment_new); // get hexagonal shape
			 * Bitmap b = getHexagonShape(bitmap); // set image in imageview
			 * capture_audio_ImageView.setImageBitmap(b); // show audio image }
			 */

		}

	}

	private void snooze_Dialog() {
		// TODO Auto-generated method stub
		Button set_Button, editbutton,snooze_button;
		TextView textview;

		snooze_dialog = new Dialog(EventActivity.this);

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
				setReminder.canceldailyReminder(context, 5);
				snooze_dialog.dismiss();
				finish();

			}
		});

		snooze_dialog.show();

	}

	public void save_snooze() {
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
						"please Select right hour minute", Toast.LENGTH_SHORT).show();
			}

		}

		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void get_not_check() throws ParseException {
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
		audio_into_byte();
		close_all();
		if (updateid > 0) {
			System.out.println("=======sunil============="+updateid);
			updateinfo();
		} else {
			// Toast.makeText(getApplicationContext(), "save"+final_hour,
			// Toast.LENGTH_SHORT).show();
			event_info();
		}

	}

	private void set_repeat() {
		if (final_repeat != null && !final_repeat.isEmpty() && !final_repeat.equals("null")) {

			switch (final_repeat) {
			case "daily":
				event_repeat_value = 0;

				break;
			case "weekly":
				event_repeat_value = 1;
				break;
			case "monthly":
				event_repeat_value = 2;
				break;
			case "yearly":
				event_repeat_value = 3;
				break;
			default:
				break;
			}
			toggle_repeat();
		}
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
		event_when_TextView
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

		event_when_TextView.setText("" + months_str[num] + " " + final_date
				+ ", " + final_year + " "
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
	 * event_when_TextView
	 * .setTextColor(getResources().getColor(R.color.Green));
	 * 
	 * event_when_TextView.setText("" + months_str[num] + " " + final_date + ","
	 * + final_year + " " + remindertime);
	 * 
	 * 
	 * event_when_TextView .setText("" + months_str[num] + " " + final_date1 +
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

	private void show_capture_image(int position) {

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

		encodedImage = img_ArrayList.get(position).getPhoto();
		
		System.out.println("===== camera==path========"+encodedImage);
		
		if (encodedImage != null) {
			
			

			File imgFile = new File(encodedImage);
			System.out.println("===== camerafg==path========"+imgFile);

			if (imgFile.exists()) {
				//Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
				//		.getAbsolutePath());
				
				Bitmap myBitmap=	decodeSampledBitmapFromFile(imgFile.getAbsolutePath(), 500, 250);
				dialog_capture_imageView.setImageBitmap(myBitmap);
			}

		}
		share_capture_imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * if (encodedImage != null) {
				 * 
				 * byte[] decodedString = Base64.decode(encodedImage,
				 * Base64.DEFAULT); Bitmap decodedByte =
				 * BitmapFactory.decodeByteArray( decodedString, 0,
				 * decodedString.length); // you can create a new file name
				 * "test.jpg" in sdcard // folder. File f = new
				 * File(Environment.getExternalStorageDirectory() +
				 * File.separator + "test.jpg");
				 * 
				 * destination = new File(
				 * Environment.getExternalStorageDirectory(),
				 * System.currentTimeMillis() + ".jpg");
				 * 
				 * try { f.createNewFile();
				 * 
				 * // write the bytes in file FileOutputStream fo = new
				 * FileOutputStream(f); fo.write(decodedString);
				 * 
				 * // remember close de FileOutput fo.close();
				 * 
				 * } catch (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

				// encodedImage = img_ArrayList.get(position).getPhoto();
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
				if(updateid>0){
					taskDataBase.deleteImage(imageid);
					removeimages(img_position);
					//System.out.println("====imageid============="+imageid);
					//Toast.makeText(getApplicationContext(),""+ imageid, Toast.LENGTH_LONG).show();
				}else{
				removeimages(img_position);
				}
				capture_dialog.cancel();
			}
		});
		capture_dialog.show();

	}

	private void convert_string_image() {
		// if (!encodedImage.isEmpty()&&encodedImage.length() != -1) {

		/*
		 * if (encodedImage==null) { // Toast.makeText(getApplicationContext(),
		 * "No image", Toast.LENGTH_SHORT).show(); } else { byte[] decodedString =
		 * Base64.decode(encodedImage, Base64.DEFAULT); Bitmap decodedByte =
		 * BitmapFactory.decodeByteArray(decodedString, 0,
		 * decodedString.length);
		 * 
		 * Bitmap b = getHexagonShape(decodedByte); // set image in imageview
		 * capture_ImageView.setVisibility(View.VISIBLE);
		 * capture_ImageView.setImageBitmap(b); }
		 */

		if (encodedImage != null) {

			byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);

			Bitmap b = getHexagonShape(decodedByte);
			// set image in imageview
			capture_ImageView.setVisibility(View.VISIBLE);
			capture_ImageView.setImageBitmap(b);
		}

	}

	private void updateinfo() {
		
		
		
		
		/*
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarm = new Intent(this,
                StatusBar.class);
        PendingIntent morningIntent = PendingIntent.getBroadcast(this, 5,
                intentAlarm, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(morningIntent);
        morningIntent.cancel();*/
	/*	
		Intent intent = new Intent(this, CancelAlarmBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 5, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(sender);*/
		
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		String email1 = loginPreferences.getString("username", "");
		event_text = event_EditText.getText().toString();
		final_with = event_with_TextView.getText().toString();
		final_with = mContactListTextview.getText().toString();
		// Toast.makeText(getApplicationContext(), ""+final_with, Toast.LENGTH_SHORT).show();

		final_where = atvPlaces.getText().toString();
		set_final_end();
		// /importance=importance2;
		// encoded_audio_string=audio;
		// encodedImage=photo;
		System.out.println("========update long=id==========" + updateid);

	/*	System.out.println("=========update title==========" + event_text);
		System.out
				.println("==========update location===========" + final_where);
		System.out.println("==========update with===========" + final_with);
		System.out.println("==========update audio==========="
				+ encoded_audio_string);
		System.out.println("==========update photo===========" + encodedImage);
		System.out
				.println("==========updateimportance===========" + importance);
		System.out.println("==========update tag===========" + tag1);
		System.out.println("==========update final date==========="
				+ final_date);
		System.out.println("==========update final_month==========="
				+ final_month);
		System.out.println("==========update final year==========="
				+ final_year);
		System.out.println("==========update final date==========="
				+ final_date);
		System.out.println("==========update final_hour==========="
				+ final_hour);
		System.out.println("==========update final minute==========="
				+ final_minute);

		System.out.println("==========update final_am_pm==========="
				+ final_select_am_pm);
		System.out.println("==========update final end==========="
				+ final_until_end);*/

		// TODO Auto-generated method stub
		Task newTask = new Task(event_text);
		newTask.setEvent_with(final_with);
		newTask.setEvent("event");
		newTask.setId(updateid);
		newTask.setEvent_tag(tag1);
		//newTask.setEvent_Audio(encoded_audio_string);
	//	newTask.setEvent_photo(encodedImage);
		newTask.setLocation(final_where);
		newTask.setImportance(importance);
		newTask.setSearch_date(final_date_long);
		newTask.setam_pm(final_select_am_pm);

		newTask.setFinal_repeat(final_repeat);// daily monthly
		newTask.setFinal_repeat_day_month(final_repeat_every_day_month); // every
																			// 2nd
																			// day
		newTask.setFinal_until_date(final_until_end);// last date of event
		newTask.setUserId(email1);
		
		set_selected_items();
		newTask.setSelected_items(selected_items);
		
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

            event.put("title", event_text);
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

            //Log.i("Log", "Updated " + iNumRowsUpdated + " calendar entry.");
            //==================================================
            //Update Google Calendar Event
            //=======================================================

        }
		
		/*if(img_ArrayList!=null){
			img_ArrayList.clear();
			}
			if(audio_ArrayList!=null){
			audio_ArrayList.clear();
			}*/
		// add multiple images
		
		
					if (img_ArrayList != null) {
						for (int i = 0; i < img_ArrayList.size(); i++) {

							//System.out.println("=========image id======sunil========="+img_ArrayList.get(i).getId());
						System.out.println("=========image photo=====sunil=========="+img_ArrayList.get(i).getPhoto());
							
							//System.out.println("==========img_arrayList sunil============="+img_ArrayList.size());
							//remove duplicate value in arraylist
							
							
				           /* if(img_ArrayList.size()!=1){
				            	String a1 = img_ArrayList.get(i).getPhoto();
					            String a2 = img_ArrayList.get(i-1).getPhoto();
				            	 if (a1.equals(a2)) {
						            img_ArrayList.remove(a1);*/
						            	
						            	
						            	 taskDataBase.add_photoUpdate(updateid, img_ArrayList.get(i).getId(),img_ArrayList.get(i)
													.getPhoto());
						         // }	
				           // }
				           
				           
						}
					}

					// add multiple audio
					if (audio_ArrayList != null) {
						for (int i = 0; i < audio_ArrayList.size(); i++) {
							//remove duplicate value in arraylist
						/*	String a1 = audio_ArrayList.get(i).getAudio();
				            String a2 = audio_ArrayList.get(i-1).getAudio();
				            if (a1.equals(a2)) {
				            	audio_ArrayList.remove(a1);
				            }*/
							taskDataBase.add_audioUpdate(updateid,audio_ArrayList.get(i).getId(), audio_ArrayList.get(i)
									.getAudio());
						}
					}
		/*
		 * Calendar cal = Calendar.getInstance();
		 * 
		 * cal.set(Integer.parseInt(final_year), Integer.parseInt(final_month) -
		 * 1, Integer.parseInt(final_date), Integer.parseInt(final_hour),
		 * Integer.parseInt(final_minute), 0); // Setting a new // Calendar
		 * instance // with the user // reminder date
		 */
		Time reminder = new Time(); // Reminder in Time format to store in
									// the object
		reminder.set(0, Integer.parseInt(final_minute),
				Integer.parseInt(final_hour), Integer.parseInt(final_date),
				Integer.parseInt(final_month) - 1, Integer.parseInt(final_year)); // Setting
																					// the
																					// reminder
																					// to
																					// be
																					// saved
																					// in
																					// the
																					// Task
																					// object
		newTask.setReminder(reminder);
		newTask.setHasReminder(1); // Setting the boolean flag to indicate
									// that that the task has reminder

		// setReminder.setOneTimeReminder(context, cal, newTask);
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(final_year),
				Integer.parseInt(final_month) - 1,
				Integer.parseInt(final_date), Integer.parseInt(final_hour),
				Integer.parseInt(final_minute), 0); // Setting a new
													// Calendar instance
													// with the user
													// reminder date
		
		
		System.out.println("====Update===final_year================"+final_year);
		
		System.out.println("====Update===final_month================"+final_month);
		
		System.out.println("====Update===final_date================"+final_date);
	      

	       

		Log.d("after final hour =", "" + final_hour);
		Log.d("after final_minute =", "" + final_minute);
		
		
		
		if(setSnooze==true){
		
		/*	Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH); // Note: zero based!
			int day = now.get(Calendar.DAY_OF_MONTH);
			int hour = now.get(Calendar.HOUR_OF_DAY);
			int minute = now.get(Calendar.MINUTE);
			int second = now.get(Calendar.SECOND);
			
		
			
			now.set(year, month, day, hour, minute,second);*/
			
			
			   Calendar calendar = Calendar.getInstance();
	            calendar.setTimeInMillis(System.currentTimeMillis());
	            calendar.add(Calendar.MINUTE, alarm_final_value);
			
			System.out.println("========interval======="+interval);
			
			System.out.println("========alarm_final_value======="+alarm_final_value);
			
			System.out.println("====now=============="+calendar);
			//int millis = now.get(Calendar.MILLISECOND);
			/*Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
					+ year);
			final_date = String.valueOf(_calendar.get(Calendar.DAY_OF_MONTH));
			final_month = String.valueOf(month);
			final_year = String.valueOf(year);
			final_date_month_year = final_date + "-" + final_month + "-"
					+ final_year;*/
			
			setReminder.setSnooze(context, calendar, newTask,
					interval,5);
			//Toast.makeText(getApplicationContext(), "set snooze update", Toast.LENGTH_SHORT).show();
			System.out.println("=========Set snooze==="+cal);
		}
		
		System.out.println("==========calender===update==="+cal);
		if (set_time == true) {
			if (seletedItems == null || seletedItems.isEmpty()) {

				System.out.println("daily " + interval);
				// setReminder.setOneTimeReminder(context, cal, newTask);
				//setReminder.canceldailyReminder(context, 5);
				
			
				setReminder.setRepeatTimeReminder(context, cal, newTask,
						interval);
				Toast.makeText(getApplicationContext(), "daily", Toast.LENGTH_SHORT).show();
				System.out.println("seletedItems=");
			} else {

				switch (event_repeat_value) {
				case 1:// week
					interval = 7 * 24 * 60 * 60 * 1000;
					for (int j = 0; j < seletedItems.size(); j++) {

						cal.set(Calendar.DAY_OF_WEEK, seletedItems.get(j));
						System.out.println("seletedItems="
								+ seletedItems.get(j));

						setReminder.setRepeatweekReminder(context, cal,
								newTask, j, interval);
						Toast.makeText(getApplicationContext(), "week", Toast.LENGTH_SHORT).show();
					}
					seletedItems.clear();

					break;
				case 3:// yearly

					interval = 365 * 24 * 60 * 60 * 1000;
					for (int j = 0; j < seletedItems.size(); j++) {
						cal.set(Calendar.DAY_OF_MONTH, seletedItems.get(j));

						setReminder.setRepeatweekReminder(context, cal,
								newTask, j, interval);
					}
					seletedItems.clear();

					break;
				default:
					break;

				}
			}
			
			
			

			if (final_end_date != null) {
				System.out.println("end date " + final_end_date);
				System.out.println("end month" + final_end_month);
				System.out.println("end year" + final_end_year);
				Calendar timeOff = Calendar.getInstance();
				timeOff.set(Integer.parseInt(final_end_year),
						Integer.parseInt(final_end_month) - 1,
						Integer.parseInt(final_end_date)
						);
				/*
				 * timeOff.set(Calendar.HOUR_OF_DAY,12);
				 * timeOff.set(Calendar.MINUTE,00); timeOff.set(Calendar.SECOND,
				 * 00);
				 */

			setReminder.setCanceltimeReminder(context, timeOff, newTask);
			}

		} else {
			
			if(setSnooze==true){
				
			}else{
				
				//Toast.makeText(getApplicationContext(), "alarm", Toast.LENGTH_LONG).show();
				
			setReminder.setOneTimeReminder(context, cal, newTask);
			//Toast.makeText(getApplicationContext(), "one time", Toast.LENGTH_SHORT).show();
			}
		}

		setReminder.setProximityAlert(context, newTask, latitude, longitude);

		// Toast.makeText(getApplicationContext(),
		// "------"+final_date+"-"+final_month+"-"+final_year, Toast.LENGTH_SHORT).show();
		// Toast.makeText(getApplicationContext(),
		// "------"+final_hour+"-"+final_minute, Toast.LENGTH_SHORT).show();

		setReminder.setProximityAlert(context, newTask, latitude, longitude);

		array.updateTask(newTask, newTask.getId());
		this.finish();

	}

	public void check_internet_status() {
		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		// check for Internet status
		if (isInternetPresent) {
			// Internet Connection is Present
			// make HTTP requests
			/*
			 * Toast.makeText(getApplicationContext(),
			 * "You have internet connection", Toast.LENGTH_SHORT).show();
			 */

		} else {
			// Internet connection is not present
			// Ask user to connect to Internet
			Toast.makeText(getApplicationContext(), "No Internet Connection",
					Toast.LENGTH_SHORT).show();

		}
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
		 * Toast.makeText(getApplicationContext(), "Current_date", Toast.LENGTH_SHORT).show();
		 * }
		 */

		if (date1.compareTo(date2) > 0) {
			System.out.println("Date1 is after Date2");
			Add_text();
			tags_editText.getText().clear();
			audio_into_byte();
			close_all();
			if (updateid > 0) {
				// Toast.makeText(getApplicationContext(), "update",
				// Toast.LENGTH_SHORT).show();
				System.out.println("=====hello=====================");
				updateinfo();
			} else {
				// Toast.makeText(getApplicationContext(), "save"+final_hour,
				// Toast.LENGTH_SHORT).show();
				event_info();
			}

		} else if (date1.compareTo(date2) < 0) {

			System.out.println("Date1 is before Date2");
			Toast.makeText(getApplicationContext(), "Please select right date",
					Toast.LENGTH_SHORT).show();
		} else if (date1.compareTo(date2) == 0) {
			System.out.println("Date1 is equal to Date2");
			match_time();

		} else {
			System.out.println("How to get here?");
		}

	}

	public void match_time() throws ParseException {
		String time = final_hour + ":" + final_minute;
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		// Toast.makeText(getApplicationContext(), "time" + time, Toast.LENGTH_SHORT).show();
		Date EndTime = dateFormat.parse(time);

		Date CurrentTime = dateFormat.parse(dateFormat.format(new Date()));

		if (CurrentTime.after(EndTime)) {
			System.out.println("timeeee end ");
			Toast.makeText(getApplicationContext(), "Please select right time",
					Toast.LENGTH_SHORT).show();

		} else {
			Add_text();
			tags_editText.getText().clear();
			audio_into_byte();

			close_all();
			if (updateid > 0) {// Toast.makeText(getApplicationContext(),
								// "updatetime", Toast.LENGTH_SHORT).show();
				updateinfo();
			} else {// Toast.makeText(getApplicationContext(), " save time",
					// Toast.LENGTH_SHORT).show();
				event_info();
				// Toast.makeText(getApplicationContext(), "save"+final_hour,
				// Toast.LENGTH_SHORT).show();
			}

		}
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

		horizontallistview = (HListView) findViewById(R.id.list6);
		horizontallistview.setOnItemClickListener(this);

		audio_horizontallistview = (HListView) findViewById(R.id.audio_list);
		audio_horizontallistview.setOnItemClickListener(this);
		/* Bottom Section find id */

		priority_ImageView = (ImageView) findViewById(R.id.priority_img);
		alarm_ImageView = (ImageView) findViewById(R.id.alarm_img);

		priority_ImageView.setOnClickListener(this);
		alarm_ImageView.setOnClickListener(this);
		/* Bottom Section */

		startTextView = (TextView) findViewById(R.id.start_textview);
		endTextView = (TextView) findViewById(R.id.end_textview);
		startTextView.setOnClickListener(this);
		endTextView.setOnClickListener(this);

		event_EditText = (EditText) findViewById(R.id.event_editText);

		event_when_TextView = (TextView) findViewById(R.id.event_when_text);
		event_where_TextView = (TextView) findViewById(R.id.event_where_text);
		event_with_TextView = (TextView) findViewById(R.id.event_with_text);

		event_saveTextView = (TextView) findViewById(R.id.event_save_text);
		event_saveTextView.setOnClickListener(this);
		complete_saveTextView = (TextView) findViewById(R.id.event_complete_text);
		complete_saveTextView.setOnClickListener(this);
		share_TextView = (TextView) findViewById(R.id.event_share_text);
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
		daily_TextView = (TextView) findViewById(R.id.event_daily_text);
		monthly_TextView = (TextView) findViewById(R.id.event_monthly_text);
		weekly_TextView = (TextView) findViewById(R.id.event_weekly_text);
		yearly_TextView = (TextView) findViewById(R.id.event_yearly_text);

		am_TextView.setOnClickListener(this);
		pm_TextView.setOnClickListener(this);
		daily_TextView.setOnClickListener(this);
		monthly_TextView.setOnClickListener(this);
		weekly_TextView.setOnClickListener(this);
		yearly_TextView.setOnClickListener(this);
		hour_TextView = (EditText) findViewById(R.id.event_hour_text);
		hour_TextView.setOnClickListener(this);
		minute_TextView = (EditText) findViewById(R.id.event_minute_text);
		minute_TextView.setOnClickListener(this);

		when_calender_view = findViewById(R.id.complete_calender_layout);
		whenLinearLayout = (LinearLayout) findViewById(R.id.when_layout);
		whenLinearLayout.setOnClickListener(this);
		when_ImageView = (ImageView) findViewById(R.id.when_img);

		// Slice
		event_slice_text = (TextView) findViewById(R.id.event_slice_text);
		slice_tag_view = findViewById(R.id.event_slice);
		sliceLinearLayout = (LinearLayout) findViewById(R.id.slice_layout);
		sliceLinearLayout.setOnClickListener(this);
		slice_plus_img = (ImageView) findViewById(R.id.slice_plus_img);

		event_camera_img = (ImageView) findViewById(R.id.event_camera_img);
		event_camera_img.setOnClickListener(this);
		capture_ImageView = (ImageView) findViewById(R.id.capture_imageView);
		capture_ImageView.setOnClickListener(this);
		capture_audio_ImageView = (ImageView) findViewById(R.id.capture_audio_imageView);
		capture_audio_ImageView.setOnClickListener(this);
		image_sound_layout1 = (LinearLayout) findViewById(R.id.image_sound_layout1);
		visible_view = (View) findViewById(R.id.visible_view_layout);

		audioImageView = (ImageView) findViewById(R.id.audio_imageView);
		audioImageView.setOnClickListener(this);

		/* slice */

		linearLayout = (LinearLayout) findViewById(R.id.rootlayout);
		tags_editText = (EditText) findViewById(R.id.tag_editText);
		save_Button = (TextView) findViewById(R.id.save_button1);
		save_Button.setOnClickListener(this);

		/*------------------- where layout-------------*/
		mWhereLayout = (LinearLayout) findViewById(R.id.where_layout);
		mWhereLayout.setOnClickListener(this);

		where_view_location = findViewById(R.id.event_where);
		atvPlaces = (MultiAutoCompleteTextView) findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);

		mMapButton = (ImageView) findViewById(R.id.map);
		mMapButton.setOnClickListener(this);

		mListview = (ListView) findViewById(R.id.mListview);
		mListview.setOnItemClickListener(this);

		where_plus_img = (ImageView) findViewById(R.id.where_plus_img);

		// event With Layout
		with_contact = findViewById(R.id.event_with);
		mContactListTextview = (TextView) findViewById(R.id.contact_auto_complete);
		mContactListTextview.setInputType(InputType.TYPE_NULL);
		//InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		//im.hideSoftInputFromWindow(mContactListTextview.getWindowToken(), 0);
		mContactListTextview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				//im.hideSoftInputFromWindow(mContactListTextview.getWindowToken(), 0);
				 Intent intent= new Intent(Intent.ACTION_PICK,  Contacts.CONTENT_URI);

			        startActivityForResult(intent, 5);
			        
			        
				
			}
		});

		mWithLayout = (LinearLayout) findViewById(R.id.with_layout);

		with_plus_img = (ImageView) findViewById(R.id.with_plus_img);
		mWithLayout.setOnClickListener(this);
		mContactListview = (ListView) findViewById(R.id.mContact_Listview);

		// Write a get Contact

		//GetContact();
		
		//GetContact1();

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
					// "You have internet connection", Toast.LENGTH_SHORT).show();
					placesTask = new PlacesTask();
					placesTask.execute(s.toString());

				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					Toast.makeText(getApplicationContext(),
							"No Internet Connection", Toast.LENGTH_SHORT).show();

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

	private void GetContact1() {
		// TODO Auto-generated method stub
		    selectUsers = new ArrayList<SelectUser>();
		    resolver = this.getContentResolver();
		    phones = getContentResolver().query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME + " ASC");
	        LoadContact loadContact = new LoadContact();
	        loadContact.execute();
		 
	}
	
	 // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(EventActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);

			pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                   // Toast.makeText(EventActivity.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }

                while (phones.moveToNext()) {
                    Bitmap bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(Phone.NUMBER));
                    String EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    String image_thumb = phones.getString(phones.getColumnIndex(Phone.PHOTO_THUMBNAIL_URI));
                    try {
                        if (image_thumb != null) {
                            bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                        } else {
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SelectUser selectUser = new SelectUser();
                    selectUser.setThumb(bit_thumb);
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);
                    selectUser.setEmail(id);
                    selectUser.setCheckedBox(false);
                    selectUsers.add(selectUser);
                    
                    contactList.add(new ContactItem(name, phoneNumber, bit_thumb));
                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }
            //phones.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
          //  adapter1 = new SelectUserAdapter(selectUsers, EventActivity.this);
           
           // mContactListTextview.setAdapter(adapter1);
            
            ContactAdapter chipsAdapter = new ContactAdapter(EventActivity.this,
    				contactList);
    		//mContactListTextview.setAdapter(chipsAdapter);
    		
    		if (pDialog != null) {
				pDialog.dismiss();
			}

          /*  // Select item on listclick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Log.e("search", "here---------------- listener");

                    SelectUser data = selectUsers.get(i);
                }
            });*/

           // listView.setFastScrollEnabled(true);
        }
    }

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
					
					Intent in =new Intent(EventActivity.this, ImageCaptureActivity.class);
					startActivityForResult(in, CAMERA_REQUEST);

				}
				if (which == 1) {
					callGallery();

				}

			}
		});
		dialog = builder.create();

	}

	public  void call_new() {

		timeinmilisecod = System.currentTimeMillis();
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "image" + timeinmilisecod + ".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, CAMERA_REQUEST);

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

	public void when_plus_minus() {
		if (when_on == false) {

			when_ImageView.setBackgroundResource(R.drawable.minus);
			when_calender_view.setVisibility(View.VISIBLE);
			when_on = true;
			// only_when_on = true;
			with_on = true;
			where_on = true;
			slice_on = true;
			WithData();
			where_plus_minus();
			slice_plus_minus();
			event_when_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			event_when_TextView.setText("when");

			setGridCellAdapterToDate(month, year);

		} else {
			hour_validation();
			minute_validation();
			/*
			 * when_ImageView.setBackgroundResource(R.drawable.plus);
			 * when_calender_view.setVisibility(View.GONE); when_on = false;
			 * if(only_when_on==true) {
			 * 
			 * set_when_text(); //only_when_on=false; }
			 */
		}
	}

	/* -------------------With Data invoke on click with ----------- */
	private void WithData() {
		// TODO Auto-generated method stub
		if (with_on == false) {
			with_plus_img.setBackgroundResource(R.drawable.minus);
			with_contact.setVisibility(View.VISIBLE);
			with_on = true;

			when_on = true;
			where_on = true;
			slice_on = true;
			when_plus_minus();
			where_plus_minus();
			slice_plus_minus();
			event_with_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			event_with_TextView.setText("with");
		} else {

			with_plus_img.setBackgroundResource(R.drawable.plus);
			with_contact.setVisibility(View.GONE);
			with_on = false;
			set_with_text();
		}
	}

	/* -------------------where Data invoke on click with ----------- */
	private void where_plus_minus() {
		// TODO Auto-generated method stub
		if (where_on == false) {
			where_plus_img.setBackgroundResource(R.drawable.minus);
			where_view_location.setVisibility(View.VISIBLE);
			where_on = true;

			with_on = true;
			when_on = true;
			slice_on = true;
			WithData();
			when_plus_minus();
			slice_plus_minus();
			event_where_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			event_where_TextView.setText("where");
		} else {

			where_plus_img.setBackgroundResource(R.drawable.plus);
			where_view_location.setVisibility(View.GONE);
			where_on = false;
			set_where_text();

		}
	}

	public void slice_plus_minus() {
		if (slice_on == false) {
			slice_plus_img.setBackgroundResource(R.drawable.minus);
			slice_tag_view.setVisibility(View.VISIBLE);
			slice_on = true;

			with_on = true;
			when_on = true;
			where_on = true;
			WithData();
			when_plus_minus();
			where_plus_minus();
		} else {

			slice_plus_img.setBackgroundResource(R.drawable.plus);
			slice_tag_view.setVisibility(View.GONE);
			slice_on = false;
			set_slice_text();
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
			// Toast.LENGTH_SHORT).show();
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

	public void weekly_dialog() {

		int j = 0;

		weekly_dialog = new Dialog(this);
		weekly_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		weekly_dialog.setContentView(R.layout.week_dialog);

		week_save_text = (TextView) weekly_dialog
				.findViewById(R.id.week_save_text);

		for (TextView i : week_arr) {

			week_arr[j] = (TextView) weekly_dialog.findViewById(week_id[j]);
			week_arr[j].setOnClickListener(this);
			j++;

		}
		week_save_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				weekly_dialog.dismiss();
			}
		});
		weekly_dialog.show();

	}

	public void repeat_interval() {

		switch (final_repeat) {
		case "{daily}":
			// interval=1 * 24 * 60 * 60 * 1000;
			// AlarmManager am = (AlarmManager)
			// context.getSystemService(Context.ALARM_SERVICE);
			interval = AlarmManager.INTERVAL_DAY;
			// Toast.makeText(getApplicationContext(), " daily",Toast.LENGTH_SHORT).show();
			break;
		case "{weekly}":
			interval = AlarmManager.INTERVAL_DAY * 7;

			// interval= 7 * 24 * 60 * 60 * 1000;
			break;
		case "{monthly}":
			interval = AlarmManager.INTERVAL_DAY * 30;

			// interval= 30 * 24 * 60 * 60 * 1000;
			break;
		case "{yearly}":
			interval = AlarmManager.INTERVAL_DAY * 365;
			// interval= 365 * 24 * 60 * 60 * 1000;
			break;
		default:
			break;
		}
	}

	public void toggle_repeat() {

		// result : 0={daily} 1=weekly 2=monthly, 3=yearly
		String repeat[] = { "{daily}", "{weekly}", "{monthly}", "{yearly}" };
		
		
		switch (event_repeat_value) {
		case 0:
			daily_TextView.setTextColor(getResources().getColor(
					R.color.DarkGreen));
			weekly_TextView.setTextColor(getResources().getColor(R.color.Gray));
			monthly_TextView
					.setTextColor(getResources().getColor(R.color.Gray));
			yearly_TextView.setTextColor(getResources().getColor(R.color.Gray));
			// Toast.makeText(getApplicationContext(), "" + repeat[0],
			// Toast.LENGTH_SHORT).show();

			/* final_repeat = repeat[0]; repeat_interval(); */

			break;
		case 1:
			weekly_TextView.setTextColor(getResources().getColor(
					R.color.DarkGreen));
			daily_TextView.setTextColor(getResources().getColor(R.color.Gray));
			monthly_TextView
					.setTextColor(getResources().getColor(R.color.Gray));
			yearly_TextView.setTextColor(getResources().getColor(R.color.Gray));
			// Toast.makeText(getApplicationContext(), "" + repeat[1],
			// Toast.LENGTH_SHORT).show();

			/* final_repeat = repeat[1]; repeat_interval(); */

			break;
		case 2:
			monthly_TextView.setTextColor(getResources().getColor(
					R.color.DarkGreen));
			daily_TextView.setTextColor(getResources().getColor(R.color.Gray));
			weekly_TextView.setTextColor(getResources().getColor(R.color.Gray));
			yearly_TextView.setTextColor(getResources().getColor(R.color.Gray));
			// Toast.makeText(getApplicationContext(), "" + repeat[2],
			// Toast.LENGTH_SHORT).show();

			/* final_repeat = repeat[2]; repeat_interval(); */

			break;
		case 3:
			yearly_TextView.setTextColor(getResources().getColor(
					R.color.DarkGreen));
			daily_TextView.setTextColor(getResources().getColor(R.color.Gray));
			monthly_TextView
					.setTextColor(getResources().getColor(R.color.Gray));
			weekly_TextView.setTextColor(getResources().getColor(R.color.Gray));
			// Toast.makeText(getApplicationContext(), "" + repeat[3],
			// Toast.LENGTH_SHORT).show();

			/* final_repeat = repeat[3]; repeat_interval(); */

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
            case R.id.capture_audio_imageView:

                // play();
                // byte_into_audio();

                update_audio_dialog();
                // Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show();
                break;
            case R.id.capture_imageView:

                // show_capture_image();

                break;
            case R.id.when_layout:
                when_plus_minus();

                //imm.hideSoftInputFromWindow(event_EditText.getWindowToken(), 0);

                break;
            case R.id.where_layout:
                // only_when_on = false;
                where_plus_minus();

                //imm.hideSoftInputFromWindow(event_EditText.getWindowToken(), 0);
                break;
            case R.id.with_layout:
                // GetContact();
                // only_when_on = false;
                WithData();
                //imm.hideSoftInputFromWindow(event_EditText.getWindowToken(), 0);
                break;
            case R.id.slice_layout:
                // only_when_on = false;
                slice_plus_minus();
                //imm.hideSoftInputFromWindow(event_EditText.getWindowToken(), 0);
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
            case R.id.event_hour_text:

                break;
            case R.id.event_minute_text:
                break;

            case R.id.event_daily_text:

                //setReminder.canceldailyReminder(context,updateid);
                event_repeat_value = 0;
                toggle_repeat();
                // daily_monthly_dialog();
                daily_dialog.show();
                days_month_text.setText("days");
                break;
            case R.id.event_weekly_text:
                if (event_repeat_value == 3) {
                    selected_items = null;
                    seletedItems.clear();
                }

                event_repeat_value = 1;
                toggle_repeat();
                // weekly_dialog();
                Week_list_dialog();
                break;
            case R.id.event_monthly_text:

                event_repeat_value = 2;
                toggle_repeat();
                // daily_monthly_dialog();
                daily_dialog.show();
                days_month_text.setText("months");

                break;
            case R.id.event_yearly_text:
                if (event_repeat_value == 1) {
                    selected_items = null;
                    seletedItems.clear();
                }

                event_repeat_value = 3;
                toggle_repeat();
                Week_list_dialog();
                break;
            case R.id.event_camera_img:
                dialog.show();
                break;
            case R.id.audio_imageView: // capture audio file
                show_audio_dialog();
                break;

            case R.id.save_button1:
                Add_text();
                tags_editText.getText().clear();
                break;
		/*
		 * case R.id.event_save_text:
		 * 
		 * try { Add_text(); tags_editText.getText().clear(); audio_into_byte();
		 * convert_time_into_long();
		 * 
		 * close_all(); event_info();
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } break;
		 */

		/*
		 * case R.id.event_save_text: try {
		 * 
		 * // save if (only_when_on == true) {
		 * 
		 * String minute = minute_TextView.getText().toString();
		 * 
		 * String hour = hour_TextView.getText().toString(); if
		 * (Integer.parseInt(hour) <= 12 && Integer.parseInt(minute) < 60){
		 * 
		 * convert_date_into_long(); convert_time_into_long(); get_check();
		 * }else { Toast.makeText(getApplicationContext(),
		 * "please Select right hour minute", Toast.LENGTH_SHORT).show(); }
		 * 
		 * } else { Toast.makeText(getApplicationContext(),
		 * "please Select date", Toast.LENGTH_SHORT).show(); }
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * break;
		 */
            case R.id.event_save_text:

                event_text = event_EditText.getText().toString();

                if (event_text.trim().length() <= 0) {

                    Toast.makeText(getApplicationContext(), "please enter event", Toast.LENGTH_SHORT)
                            .show();
                }
                else {
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





               /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        EventActivity.this);

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
                                        Toast.makeText(getApplicationContext(),
                                                "please Select right hour minute", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getApplicationContext(),
                                                "please Select right hour minute", Toast.LENGTH_SHORT).show();
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

                }


			
		
		

			break;

		case R.id.event_share_text:
			share_note(v);
			// onClickWhatsApp(v);
			// whole_note_data();
			break;
		case R.id.priority_img:
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
		case R.id.map:
			Intent in = new Intent(this, CreateGoogleMap.class);
			startActivityForResult(in, 1);

			break;
		case R.id.event_complete_text:
			// complete_event();

			if (updateid > 0) {

				/* updateinfo(); */
				taskDataBase.update_complete(updateid);
				SetReminder reminder = new SetReminder();
				reminder.cancelReminder(context, updateid);

				finish();
			}

			break;
		case R.id.sun_text:
			week_arr[0].setTextColor(getResources().getColor(R.color.Green));

			break;
		case R.id.mon_text:
			week_arr[1].setTextColor(getResources().getColor(R.color.Green));

			break;
		case R.id.Tues_text:
			week_arr[2].setTextColor(getResources().getColor(R.color.Green));

			break;
		case R.id.Wed_text:
			week_arr[3].setTextColor(getResources().getColor(R.color.Green));

			break;
		case R.id.Thu_text:
			week_arr[4].setTextColor(getResources().getColor(R.color.Green));

			break;
		case R.id.Fri_text:
			week_arr[5].setTextColor(getResources().getColor(R.color.Green));

			break;
		case R.id.Sat_text:
			week_arr[6].setTextColor(getResources().getColor(R.color.Green));

			break;
		default:
			break;
		}
	}

	private void complete_event() {
		if (updateid > 0) {

			taskDataBase.update_complete(updateid);
			SetReminder reminder = new SetReminder();
			reminder.cancelReminder(context, updateid);
			System.out.println("above run" + final_repeat);
			if (final_repeat != null && final_repeat.length() != 0) {
				System.out.println("run" + final_repeat);
				switch (final_repeat) {
				case "daily":

					System.out.println("run1" + final_date);
					if (final_until_end == previous_until_end) {
						int plus_date = Integer.parseInt(final_date) + 1;
						final_date = String.valueOf(plus_date);
						System.out.println("run2" + final_date);
					}

					System.out.println("run3" + final_date);
					updateinfo();

					break;
				case "monthly":
					if (final_until_end == previous_until_end) {
						int plus_date = Integer.parseInt(final_date) + 1;
						final_date = String.valueOf(plus_date);
					}

					updateinfo();
					break;

				default:

					break;
				}
			}

			finish();
		}
	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
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
				/*
				 * gridcell.setTextColor(getResources().getColor(
				 * R.color.white));
				 */

			}
			if (day_color[1].equals("BLUE")) {

				gridcell.setTextColor(getResources().getColor(
						R.color.darkorrange));
				// set current date Background color

				/*
				 * gridcell.setBackgroundColor((getResources()
				 * .getColor(R.color.Green)));
				 */
				// set current date Text color
				// gridcell.setTextColor(getResources().getColor(R.color.white));
				/*
				 * gridcell.setTextColor(getResources().getColor(
				 * R.color.darkorrange));
				 */
				// final_date = gridcell.getText().toString();

				// Toast.makeText(getApplicationContext(), "final=" +
				// final_date,Toast.LENGTH_SHORT).show();

			}
			// Toast.makeText(context, "out run"+gridcell.getText().toString(),
			// Toast.LENGTH_SHORT).show();

			return row;
		}

		@Override
		public void onClick(View view) {
			String date_month_year = (String) view.getTag();
			final_date_month_year = date_month_year;

			// show selected date
			// Toast.makeText(getApplicationContext(), "" + date_month_year,
			// Toast.LENGTH_SHORT) .show();

			/*
			 * view.setBackgroundColor((getResources()
			 * .getColor(R.color.Green))); // set current date Text color
			 * gridcell.setTextColor(getResources().getColor(R.color.white));
			 */

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

	public void fg() {
		File f = new File(Environment.getExternalStorageDirectory().toString());
		for (File temp : f.listFiles()) {
			if (temp.getName().equals("temp.jpg")) {
				f = temp;
				break;
			}
		}
		try {
			// image_sound_layout1.setVisibility(View.VISIBLE);
			capture_ImageView.setVisibility(View.VISIBLE);
			visible_view.setVisibility(View.GONE);
			Bitmap bitmap;
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

			bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
					bitmapOptions);
			// USED FOR CROP
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, 90, 90);

			// add by raj
			// convert bitmap to byte
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			imageInByte = stream.toByteArray();

			Log.e("output before conversion", imageInByte.toString());
			// Inserting Contacts
			Log.d("Insert: ", "Inserting ..");

			encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);

			// end by raj
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		/*
		 * super.onActivityResult(requestCode, resultCode, data); if (resultCode
		 * != RESULT_OK) return;
		 */
		// Toast.makeText(getApplicationContext(),
		// "resultcode="+resultCode+"requestcode="+requestCode,Toast.LENGTH_SHORT).show();


       // Toast.makeText(EventActivity.this,"Image saved1"+resultCode,Toast.LENGTH_SHORT).show();
        //Toast.makeText(EventActivity.this,"Image saved1"+requestCode,Toast.LENGTH_SHORT).show();

		if (resultCode == 1) {

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

            if(ImageEditor.names!=null) {

                img_path = "/storage/emulated/0/M_CamScanner" + File.separator + ImageEditor.names;

                System.out.println("===path===================" + img_path);

                Bitmap bi = getHexagonShape(decodeSampledBitmapFromFile(
                        img_path, 500, 250));
                capture_ImageView.setImageBitmap(bi);
                capture_ImageView.setVisibility(View.VISIBLE);

                addimages();
                setAdapter();
            }
        }
		switch (requestCode) {
		case PIC_CROP:
			// Create an instance of bundle and get the returned data
			Bundle extras = data.getExtras();
			// get the cropped bitmap from extras
			Bitmap thePic = extras.getParcelable("data");
			// set image bitmap to image view
			capture_ImageView.setImageBitmap(thePic);
			break;
		case CAMERA_REQUEST:
			//File file = new File(Environment.getExternalStorageDirectory()
			//		+ File.separator + "image" + timeinmilisecod + ".jpg");

			//img_path = file.getAbsolutePath();
			if(data!=null){
			 img_path=data.getStringExtra("result");

                Toast.makeText(this,"Image saved"+img_path,Toast.LENGTH_SHORT).show();
			   System.out.println("===path===================" + img_path);
		
				Bitmap bi = getHexagonShape(decodeSampledBitmapFromFile(
						img_path, 500, 250));
				capture_ImageView.setImageBitmap(bi);
				capture_ImageView.setVisibility(View.VISIBLE);

				addimages();
				setAdapter();
			}
			/*if(file.exists()){
			Bitmap bi = getHexagonShape(decodeSampledBitmapFromFile(
					file.getAbsolutePath(), 500, 250));
			capture_ImageView.setImageBitmap(bi);
			capture_ImageView.setVisibility(View.VISIBLE);

			addimages();
			setAdapter();
			}*/
			
			
			if (data != null) {
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {
				

					/*
					 * Bitmap thumbnail = extras2.getParcelable("data");
					 * 
					 * ByteArrayOutputStream bytes = new
					 * ByteArrayOutputStream();
					 * thumbnail.compress(Bitmap.CompressFormat.PNG, 100,
					 * bytes);
					 * 
					 * imageInByte = bytes.toByteArray();
					 * 
					 * Log.e("output before conversion",
					 * imageInByte.toString()); // Inserting Contacts
					 * Log.d("Insert: ", "Inserting ..");
					 * 
					 * encodedImage = Base64.encodeToString(imageInByte,
					 * Base64.DEFAULT); destination = new File(
					 * Environment.getExternalStorageDirectory(),
					 * System.currentTimeMillis() + ".jpg");
					 * 
					 * 
					 * System.out.print("path="+destination); FileOutputStream
					 * fo; try { destination.createNewFile(); fo = new
					 * FileOutputStream(destination);
					 * fo.write(bytes.toByteArray()); fo.close(); } catch
					 * (FileNotFoundException e) { e.printStackTrace(); } catch
					 * (IOException e) { e.printStackTrace(); }
					 * 
					 * Bitmap b = getHexagonShape(thumbnail);
					 * capture_ImageView.setImageBitmap(b);
					 * capture_ImageView.setVisibility(View.VISIBLE);
					 * addimages(); setAdapter();
					 */}
			} else {
				// Toast.makeText(getApplicationContext(), "if No pic selected",
				// Toast.LENGTH_SHORT).show();
			}

			break;
		case PICK_FROM_GALLERY:
			System.out.println("data" + data);
			if (data != null) {
				Uri selectedImage = data.getData();

				// capture_ImageView.setImageURI(selectedImage);
				try {
					// Get the file path from the URI
					img_path = FileUtils.getPath(this, selectedImage);
					System.out.println("uri  datapath " + img_path);
					addimages();
					setAdapter();
				} catch (Exception e) {
					Log.e("FileSelectorTestActivity", "File select error", e);
				}

				/*
				 * try { Bitmap bitmapImage = decodeBitmap(selectedImage); //
				 * convert bitmap to byte ByteArrayOutputStream stream = new
				 * ByteArrayOutputStream(); bitmapImage
				 * .compress(Bitmap.CompressFormat.PNG, 100, stream);
				 * imageInByte = stream.toByteArray();
				 * 
				 * Log.e("output before conversion", imageInByte.toString());
				 * 
				 * encodedImage = Base64.encodeToString(imageInByte,
				 * Base64.DEFAULT); //
				 * capture_ImageView.setImageBitmap(yourImage); Bitmap b =
				 * getHexagonShape(bitmapImage);
				 * capture_ImageView.setImageBitmap(b);
				 * capture_ImageView.setVisibility(View.VISIBLE);
				 * System.out.print("path=" + selectedImage); addimages();
				 * setAdapter(); } catch (FileNotFoundException e) {
				 * e.printStackTrace(); }
				 */

			}

			break;
			
			
		 case (5) :
		      if (resultCode == Activity.RESULT_OK) {
		        Uri contactData = data.getData();
		        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
		        if (c.moveToFirst()) {
		          String name = c.getString(c.getColumnIndex(Contacts.DISPLAY_NAME));
		          // TODO Fetch other Contact details as you want to use
		          mContactListTextview.setText(name);
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

	public void addimages() {

     System.out.println("=====imagepath============="+img_path);

		img_ArrayList.add(new PhotoData(img_path));

		
		
		System.out.println("============img_ArrayList=========="+img_ArrayList.size());

	}

	public void setAdapter() {
		horizontal_Adapter = new horizontal_Adapter(this, img_ArrayList);
		horizontallistview.setAdapter(horizontal_Adapter);

	horizontal_Adapter.notifyDataSetChanged();
	
	}

	public void removeimages(int position) {

		img_ArrayList.remove(position).getPhoto();
		horizontal_Adapter.notifyDataSetChanged();
		System.out.println("img list" + img_ArrayList);
	}

	public void addaudio() {
		
		audio_ArrayList.add(new AudioData(encoded_audio_string));
		// img_ArrayList.add(imageInByte.toString());
		System.out.println("audio list" + audio_ArrayList);
		
		System.out.println("====encoded_audio_stringass============"+encoded_audio_string);
	    

	}

	public void setAudioAdapter() {
		audio_horizontal_Adapter = new Audio_horizontal_Adapter(this,
				audio_ArrayList);
		audio_horizontallistview.setAdapter(audio_horizontal_Adapter);

		audio_horizontal_Adapter.notifyDataSetChanged();

	}

	public void removeaudio(int position) {

		audio_ArrayList.remove(position).getAudio();
		audio_horizontal_Adapter.notifyDataSetChanged();
		System.out.println("img list" + audio_ArrayList);
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

	public void get_screen_size() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;
		if (height == 800 && width == 480) {
			targetWidth = 110;
			targetHeight = 110;
		}
		if (height == 1280 && width == 720) {
			targetWidth = 140;
			targetHeight = 140;
		}

	}

	public Bitmap getHexagonShape(Bitmap scaleBitmapImage) {

		// TODO Auto-generated method stub
		/*
		 * int targetWidth = 140; int targetHeight = 140;
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

	/*---------------------------Start here for audio-------------------------------------*/
	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		try {
			duration = myPlayer.getDuration();
			mSeekBarPlayer.setMax(duration);
			mSeekBarPlayer.postDelayed(onEverySecond, 1000);
		} catch (NullPointerException e) {
			// TODO: handle exception
		}

	}

	/* ----------------store audio file to sd card-------------------- */
	public void store() {
		timeinmilisecod = System.currentTimeMillis();
		// store it to sd card
		outputFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/lifereminder"+timeinmilisecod +".3gpp";
		

		
		
		/*
		 * outputFile =
		 * Environment.getExternalStorageDirectory().getAbsolutePath()+
		 * System.currentTimeMillis() + "lifereminder.3gpp";
		 */

		myRecorder = new MediaRecorder();
		myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		myRecorder.setOutputFile(outputFile);
	}

	/*--------------------audio dialog that open when click on audio image view--------------------*/
	public void show_audio_dialog() {
		RelativeLayout relativeLayout;

		audiodialog = new Dialog(EventActivity.this);
		audiodialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		audiodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		audiodialog.setContentView(R.layout.dialog_activity);

		relativeLayout = (RelativeLayout) audiodialog
				.findViewById(R.id.dialog_layout);
		saveLinearLayout = (LinearLayout) audiodialog
				.findViewById(R.id.save_layout);
		mSeekBarPlayer = (SeekBar) audiodialog.findViewById(R.id.seekBar1);
		recording_total_time_TextView = (TextView) audiodialog
				.findViewById(R.id.recording_total_time_text);
		cancel_recordingTextView = (TextView) audiodialog
				.findViewById(R.id.cancel_recording_text);

		delete_ImageView = (ImageView) audiodialog
				.findViewById(R.id.delete_recording_img1);
		delete_ImageView.setVisibility(View.GONE);
		save_recordingTextView = (TextView) audiodialog
				.findViewById(R.id.save_recording_text);
		start_recordingImageView = (ImageView) audiodialog
				.findViewById(R.id.start_recording_img);
		recording_TextView = (TextView) audiodialog
				.findViewById(R.id.recording_text);
		
		start_playImageView = (ImageView) audiodialog
				.findViewById(R.id.start_play_img);
		share_audioImageView = (ImageView) audiodialog
				.findViewById(R.id.share_audio);
		
		saveLinearLayout.setVisibility(View.GONE);
		
/*
		if (updateid > 0) {
			saveLinearLayout.setVisibility(View.VISIBLE);
		} else {
			saveLinearLayout.setVisibility(View.GONE);
		}*/
		// cancel recording
		delete_ImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				audiodialog.dismiss();
			}
		});
		// cancel recording
		cancel_recordingTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audiodialog.dismiss();
			}
		});
		// share
		share_audioImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				share_audio();
			}
		});
		// save recording
		save_recordingTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				visible_view.setVisibility(View.GONE);
				/*
				 * if(capture_ImageView.isShown()) {
				 * 
				 * }else { //capture_ImageView.setVisibility(visibility) }
				 */

				// image_sound_layout1.setVisibility(View.VISIBLE);

				capture_audio_ImageView.setVisibility(View.VISIBLE);
				Bitmap bitmap = BitmapFactory.decodeResource(
						context.getResources(),
						R.drawable.event_audio_attachment_new);
				// get hexagonal shape
				Bitmap b = getHexagonShape(bitmap);
				// set image in imageview
				capture_audio_ImageView.setImageBitmap(b);

				audio_into_byte();

				addaudio();
				setAudioAdapter();
				audiodialog.dismiss();

			}
		});

		mSeekBarPlayer
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (fromUser) {
							myPlayer.seekTo(progress);
							updateTime();
						}
					}
				});
		start_playImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show();
				running = true;
				if (isplay == false) // play
				{

					if (ispause == true) {
						myPlayer.start();
						ispause = false;
						isplay = true;
						mSeekBarPlayer.postDelayed(onEverySecond, 1000);
						start_playImageView
								.setImageResource(R.drawable.event_pause_button);
					} else {
						play();
						isplay = true;

						start_playImageView
								.setImageResource(R.drawable.event_pause_button);

					}

				} else // pause
				{
					isplay = false;
					ispause = true;
					myPlayer.pause();
					start_playImageView
							.setImageResource(R.drawable.event_play_button);

					// start_playImageView.setImageResource(R.drawable.abc_btn_check_material);
				}

			}
		});
		start_recordingImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (isrecord == false) {
					start_recording();
					isrecord = true;

				} else {
					stop_recording();

					isrecord = false;
					
					//recording_TextView.setText("Press to Play");
				}

				// dialog.dismiss();

			}
		});
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// dialog.dismiss();

			}
		});
		audiodialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(),
				// "dialog dismiss",Toast.LENGTH_SHORT).show();
				try {

					isrecord = false;
					isplay = false;
					ispause = false;
					isrecord = false;
					startTime = 0L;
					timeInMilliseconds = 0L;
					timeSwapBuff = 0L;

					running = false;
					current = 0;
					duration = 0;
					customHandler.removeCallbacks(onEverySecond);
					stopPlay();

					// outputFile = null;
					myRecorder.stop();
					myRecorder.release();
					myRecorder = null;

					customHandler.removeCallbacks(updateTimerThread);

				} catch (NullPointerException e) {
					// TODO: handle exception
				}

			}
		});
		audiodialog.show();
	}

	/*-------------------update time of the seek bar---------------------*/
	private void updateTime() {
		do {
			current = myPlayer.getCurrentPosition();
			System.out.println("duration - " + duration + " current- "
					+ current);
			int dSeconds = (int) (duration / 1000) % 60;
			int dMinutes = (int) ((duration / (1000 * 60)) % 60);
			int dHours = (int) ((duration / (1000 * 60 * 60)) % 24);

			int cSeconds = (int) (current / 1000) % 60;
			int cMinutes = (int) ((current / (1000 * 60)) % 60);
			int cHours = (int) ((current / (1000 * 60 * 60)) % 24);

			if (dHours == 0) {
				recording_TextView.setText(String.format(
						"%02d:%02d / %02d:%02d", cMinutes, cSeconds, dMinutes,
						dSeconds));
				recording_total_time_TextView.setText("" + dMinutes + ":"
						+ String.format("%02d", dSeconds));

			} else {
				recording_TextView.setText(String.format(
						"%02d:%02d:%02d / %02d:%02d:%02d", cHours, cMinutes,
						cSeconds, dHours, dMinutes, dSeconds));
				recording_total_time_TextView.setText("" + dMinutes + ":"
						+ String.format("%02d", dSeconds));
			}

			try {
				Log.d("Value: ",
						String.valueOf((int) (current * 100 / duration)));
				if (mSeekBarPlayer.getProgress() >= 100) {
					break;
				}
			} catch (Exception e) {
			}
		} while (mSeekBarPlayer.getProgress() <= 100);
	}

	/*-----------------	update progress of seek bar------------------------*/
	private Runnable onEverySecond = new Runnable() {
		@Override
		public void run() {
			if (true == running) {
				if (mSeekBarPlayer != null) {

					mSeekBarPlayer.setProgress(myPlayer.getCurrentPosition());
				}

				if (myPlayer.isPlaying()) {
					mSeekBarPlayer.postDelayed(onEverySecond, 1000);
					updateTime();
				}
			}
		}
	};

	/*--------------------------	play recording audio file-----------------------------*/

	public void play() {
		try {

			myPlayer = new MediaPlayer();
			myPlayer.setDataSource(outputFile);
			myPlayer.setOnPreparedListener(this);
			myPlayer.prepare();
			myPlayer.start();
			myPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(getApplicationContext(), "complete",
					// Toast.LENGTH_SHORT).show();
					start_playImageView
							.setImageResource(R.drawable.event_play_button);

				}
			});
			// seek bar
			mSeekBarPlayer.postDelayed(onEverySecond, 1000);
			// Toast.makeText(getApplicationContext(),"Start play the recording...",
			// Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * ------------------------start recording and update recording time
	 * ----------------------
	 */
	private Runnable updateTimerThread = new Runnable() {

		@Override
		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeSwapBuff + timeInMilliseconds;
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			recording_TextView.setText("" + mins + ":"
					+ String.format("%02d", secs) + ":"
					+ String.format("%03d", milliseconds));
			customHandler.postDelayed(this, 0);
		}
	};

	/*--------------------------start Recording--------------------------*/
	public void start_recording() {
		store();
		try {
			start_recordingImageView.setClickable(true);
			myRecorder.prepare();
			myRecorder.start();
			// start timer also
			startTime = SystemClock.uptimeMillis();
			customHandler.postDelayed(updateTimerThread, 0);
		} catch (IllegalStateException e) {
			// start:it is called before prepare()
			// prepare: it is called after start() or before setOutputFormat()
			e.printStackTrace();
		} catch (IOException e) {
			// prepare() fails
			e.printStackTrace();
		}
		start_recordingImageView.setImageResource(R.drawable.event_stop_button);
		recording_TextView.setText("0:00:25");
		recording_TextView.setText("");
		// Toast.makeText(getApplicationContext(),
		// "Start recording...",Toast.LENGTH_SHORT).show();
	}

	/*-----------------stop  recording audio file---------------- */

	public void stop_recording() {
		try {
			myRecorder.stop();
			myRecorder.release();
			myRecorder = null;
			// get stop time
			timeSwapBuff += timeInMilliseconds;
			customHandler.removeCallbacks(updateTimerThread);
			//
			saveLinearLayout.setVisibility(View.VISIBLE);
			delete_ImageView.setVisibility(View.VISIBLE);
			if (saveLinearLayout.isShown()) {
				// Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
				audiodialog.setCanceledOnTouchOutside(false);
				audiodialog.setCancelable(false);
			} else {
				// Toast.makeText(getApplicationContext(), "dismiss",
				// Toast.LENGTH_SHORT).show();
			}
			start_recordingImageView
					.setImageResource(R.drawable.event_record_button);
			recording_TextView.setText("press to play");
			
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			recording_total_time_TextView.setText("" + mins + ":"
					+ String.format("%02d", secs));
			start_playImageView.setVisibility(View.VISIBLE);
			start_recordingImageView.setClickable(false);
			// Toast.makeText(getApplicationContext(),
			// "Stop recording...",Toast.LENGTH_SHORT).show();
		} catch (IllegalStateException e) { /* it is called before start() */
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/*----------------------Stop playing----------------------*/
	public void stopPlay() {
		try {
			if (myPlayer != null) {
				myPlayer.stop();
				myPlayer.release();
				myPlayer = null;
				// Toast.makeText(getApplicationContext(),"Stop playing the recording...",
				// Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* ---------with coding functionality----- */
	// Retrieve get Contact
	private void GetContact() {

		String[] PROJECTION = new String[] {
				Phone.CONTACT_ID,
				Contacts.DISPLAY_NAME,
				Phone.NUMBER };

		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(
				Phone.CONTENT_URI, PROJECTION,
				null, null, null);
		if (cursor != null) {
			try {
				final int nameIndex = cursor
						.getColumnIndex(Contacts.DISPLAY_NAME);
				final int numberIndex = cursor
						.getColumnIndex(Phone.NUMBER);

				final int id = cursor
						.getColumnIndex(Phone.CONTACT_ID);

				System.out.println("=================id=====" + id);
				/*
				 * final String strId= cursor .getString(cursor
				 * .getColumnIndex(ContactsContract
				 * .CommonDataKinds.Phone.CONTACT_ID));
				 */

				String name, number, photo_id;

				while (cursor.moveToNext()) {
					name = cursor.getString(nameIndex);
					number = cursor.getString(numberIndex);
					photo_id = cursor.getString(id);
					Bitmap image = null;
					/*
					 * Uri contact_Uri = Uri.withAppendedPath(
					 * ContactsContract.Contacts.CONTENT_URI, photo_id);
					 * InputStream photo_stream = ContactsContract.Contacts
					 * .openContactPhotoInputStream(getContentResolver(),
					 * contact_Uri); Bitmap image = null; if (photo_stream !=
					 * null) { // Retrieve and add image if present in //
					 * database BufferedInputStream buf = new
					 * BufferedInputStream(photo_stream, 8192); image =
					 * BitmapFactory.decodeStream(buf); //
					 * contactImages.add(image); } else { // otherwise add
					 * 'noImage' // contactImages.add(noImage); }
					 */

					// System.out.println("====id========="+contactId);
					System.out.println("====displayName=========" + name);
					System.out.println("=======number======" + number);

					contactList.add(new ContactItem(name, number, image));
					// System.out.println("=======Image======"+image);

				}
			} finally {
				cursor.close();
			}
		}

		/*
		 * ContentResolver cResolver=context.getContentResolver();
		 * ContentProviderClient mCProviderClient =
		 * cResolver.acquireContentProviderClient
		 * (ContactsContract.Contacts.CONTENT_URI); String strId ;
		 * 
		 * try { Cursor mCursor =
		 * mCProviderClient.query(ContactsContract.Contacts.CONTENT_URI, null,
		 * null, null, null); if (mCursor != null && mCursor.getCount() > 0) {
		 * 
		 * mCursor.moveToFirst(); while (!mCursor.isLast()) { String displayName
		 * =
		 * mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts
		 * .DISPLAY_NAME));
		 * 
		 * String number = mCursor .getString(mCursor
		 * .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		 * 
		 * 
		 * 
		 * strId = mCursor .getString(mCursor
		 * .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
		 * 
		 * Uri contact_Uri = Uri.withAppendedPath(
		 * ContactsContract.Contacts.CONTENT_URI, strId); InputStream
		 * photo_stream = ContactsContract.Contacts
		 * .openContactPhotoInputStream(getContentResolver(), contact_Uri);
		 * Bitmap image = null; if (photo_stream != null) { // Retrieve and add
		 * image if present in // database BufferedInputStream buf = new
		 * BufferedInputStream(photo_stream, 8192); image =
		 * BitmapFactory.decodeStream(buf); // contactImages.add(image); } else
		 * { // otherwise add 'noImage' // contactImages.add(noImage); }
		 * contactList.add(new ContactItem(displayName, number, image));
		 * 
		 * System.out.println("====displayName========="+displayName);
		 * System.out.println("=======number======"+number);
		 * System.out.println("=======image======"+image);
		 * 
		 * mCursor.moveToNext(); } if (mCursor.isLast()) { String displayName =
		 * mCursor
		 * .getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts
		 * .DISPLAY_NAME)); String number = mCursor .getString(mCursor
		 * .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		 * 
		 * 
		 * strId = mCursor .getString(mCursor
		 * .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
		 * 
		 * Uri contact_Uri = Uri.withAppendedPath(
		 * ContactsContract.Contacts.CONTENT_URI, strId); InputStream
		 * photo_stream = ContactsContract.Contacts
		 * .openContactPhotoInputStream(getContentResolver(), contact_Uri);
		 * Bitmap image = null; if (photo_stream != null) { // Retrieve and add
		 * image if present in // database BufferedInputStream buf = new
		 * BufferedInputStream(photo_stream, 8192); image =
		 * BitmapFactory.decodeStream(buf); // contactImages.add(image); } else
		 * { // otherwise add 'noImage' // contactImages.add(noImage); }
		 * 
		 * contactList.add(new ContactItem(displayName, number, image)); }
		 * 
		 * 
		 * 
		 * }
		 * 
		 * mCursor.close(); } catch (RemoteException e) { e.printStackTrace();
		 * contactList = null; } catch (Exception e) { e.printStackTrace();
		 * contactList=null;
		 * 
		 * }
		 */

		// TODO Auto-generated method stub
		/*
		 * Bitmap noImage = BitmapFactory.decodeResource(getResources(),
		 * R.drawable.ic_launcher);
		 * 
		 * Cursor phones = getContentResolver().query(
		 * ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
		 * Phone.DISPLAY_NAME + " COLLATE NOCASE ASC");
		 * 
		 * phones.moveToFirst(); do { String name = phones .getString(phones
		 * .getColumnIndex
		 * (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		 * 
		 * String number = phones .getString(phones
		 * .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		 * 
		 * String strId = phones .getString(phones
		 * .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
		 * 
		 * Uri contact_Uri = Uri.withAppendedPath(
		 * ContactsContract.Contacts.CONTENT_URI, strId); InputStream
		 * photo_stream = ContactsContract.Contacts
		 * .openContactPhotoInputStream(getContentResolver(), contact_Uri);
		 * Bitmap image = null; if (photo_stream != null) { // Retrieve and add
		 * image if present in // database BufferedInputStream buf = new
		 * BufferedInputStream(photo_stream, 8192); image =
		 * BitmapFactory.decodeStream(buf); // contactImages.add(image); } else
		 * { // otherwise add 'noImage' // contactImages.add(noImage); }
		 * 
		 * // contactNames.add(name); // contactNumbers.add(number);
		 * 
		 * contactList.add(new ContactItem(name, number, image));
		 * 
		 * } while (phones.moveToNext());
		 */

		/*
		 * String[] names = new String[contactNames.size()]; names =
		 * contactNames.toArray(names);
		 * 
		 * String[] numbers = new String[contactNumbers.size()]; numbers =
		 * contactNumbers.toArray(numbers);
		 * 
		 * Bitmap[] images = new Bitmap[contactImages.size()]; images =
		 * contactImages.toArray(images);
		 */
		// Get AutoCompleteTextView reference from xml
		// textView = (AutoCompleteTextView) findViewById(R.id.Months);

		// Each row in the list stores country name, currency and flag
		// List<HashMap<String,String>> aList = new
		// ArrayList<HashMap<String,String>>();

		// textView.setAdapter(new ChipsAdapter(names, images));

	//chipsAdapter = new ContactAdapter(this,
	//			contactList);
		/*final ContactAdapter chipsAdapter = new ContactAdapter(this,
				contactList);*/
	//	mContactListTextview.setAdapter(chipsAdapter);
		//mContactListTextview.setThreshold(1);

		/*
		 * mContactListTextview .setTokenizer(new
		 * MultiAutoCompleteTextView.CommaTokenizer());
		 */

		// mContactListview.setAdapter(chipsAdapter);

		mContactListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				mContactListTextview.setText(""
						+ ContactAdapter.suggestions.get(position).getName());
				try {
					final_with = ContactAdapter.suggestions.get(position)
							.getNumber().toString();
				} catch (Exception e) {
					// TODO: handle exception
				}

				// Toast.makeText(getApplicationContext(),
				// "="+ContactAdapter.suggestions.get(position).getName(),
				// Toast.LENGTH_SHORT).show();

			}
		});
		mContactListTextview.addTextChangedListener(new TextWatcher() {
			
			
			private Handler handler = new Handler() {
	            @Override
	            public void handleMessage(Message msg) {
	                if (msg.what == TRIGGER_SERACH) {  
	                    try{
	                        //if(searchText.length() > 0){  
	                            chipsAdapter.getFilter().filter(searchText);                             
	                        //}
	                    }catch(Exception e){
	                        e.printStackTrace(); 
	                    }
	                }
	            }
	        };


			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				
				
				
				
				// TODO Auto-generated method stub
				// EventActivity.this.chipsAdapter.getFilter().filter(s);

				// /String text =
				// mContactListTextview.getText().toString().toLowerCase(Locale.getDefault());
				/*chipsAdapter.getFilter().filter(s);
				if (mContactListTextview.getText().toString().length() <= 0) {
					mContactListview.setVisibility(View.GONE);
				} else {
					mContactListview.setVisibility(View.VISIBLE);

				}*/

				/*
				 * String
				 * searchString=mContactListTextview.getText().toString(); int
				 * textLength=searchString.length();
				 * 
				 * //clear the initial data set contactList.clear();
				 * 
				 * for(int i=0;i<contactList.size();i++) { String
				 * playerName=contactList.get(i).getName().toString();
				 * if(textLength<=playerName.length()){ //compare the String in
				 * EditText with Names in the ArrayList
				 * if(searchString.equalsIgnoreCase
				 * (playerName.substring(0,textLength)))
				 * contactList.add(originalValues.get(i)); } }
				 * 
				 * adapter.notifyDataSetChanged();
				 */
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				
				
				    searchText = s.toString();
		            handler.removeMessages(TRIGGER_SERACH);
		            handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
				/*chipsAdapter.getFilter().filter(s);
				chipsAdapter.notifyDataSetChanged();*/
				
				
				
			}
				
				/*try {
					chipsAdapter.getFilter().filter(s);
					chipsAdapter.notifyDataSetChanged();
					
					if (mContactListTextview.getText().toString().length() <= 0) {
						mContactListview.setVisibility(View.GONE);
					} else {
						mContactListview.setVisibility(View.VISIBLE);

					}
				} catch (IllegalStateException e) {
					// TODO: handle exception
					e.printStackTrace();
				}*/
			
		/*	
					try {
			asyn(s);
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
		}catch(IllegalStateException e)
		{
		
		}*/
				
				// new Async_caller().execute(s);
				// TODO Auto-generated method stub
				/*chipsAdapter.getFilter().filter(s);
				chipsAdapter.notifyDataSetChanged();
				
				if (mContactListTextview.getText().toString().length() <= 0) {
					mContactListview.setVisibility(View.GONE);
				} else {
					mContactListview.setVisibility(View.VISIBLE);

				}
*/
			
		});

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
			isInternetPresent = cd.isConnectingToInternet();

			// check for Internet status
			if (isInternetPresent) {
				// Internet Connection is Present
				// make HTTP requests
				// Toast.makeText(getApplicationContext(),
				// "You have internet connection", Toast.LENGTH_SHORT).show();
				// Creating ParserTask
				parserTask = new ParserTask();

				// Starting Parsing the JSON string returned by Web Service
				parserTask.execute(result);

			} else {
				// Internet connection is not present
				// Ask user to connect to Internet
				Toast.makeText(getApplicationContext(),
						"No Internet Connection", Toast.LENGTH_SHORT).show();

			}

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
		// atvPlaces.setText("" + hm.get("description"));
		// final_where = hm.get("description");
		// Toast.makeText(getApplicationContext(), ""+hm.get("description"),
		// Toast.LENGTH_SHORT).show();
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

		// set_when_hour=final_hour;
		// Toast.makeText(getApplicationContext(), ""+final_select_am_pm,
		// Toast.LENGTH_SHORT).show();
		switch (final_select_am_pm) {
		case "am":

			int time1 = Integer.parseInt(final_hour) - 12;
			if (time1 < 0) {

			} else {
				final_hour = String.valueOf(time1);
				/*
				 * Toast.makeText(getApplicationContext(), "am" + final_hour +
				 * ":" + final_minute, Toast.LENGTH_SHORT).show();
				 */
			}
			break;
		case "pm":
			if (Integer.parseInt(final_hour) == 12) {

			} else {
				int time = Integer.parseInt(final_hour) + 12;
				if (time > 24) {

				} else {
					final_hour = String.valueOf(time);
					/*
					 * Toast.makeText(getApplicationContext(), "pm" + final_hour
					 * + ":" + final_minute, Toast.LENGTH_SHORT).show();
					 */
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

	public void event_info() {

		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		String email1 = loginPreferences.getString("username", "");
		event_text = event_EditText.getText().toString();
		final_with = mContactListTextview.getText().toString();
		final_where = atvPlaces.getText().toString();

		set_final_end();
		if (tag_ArrayList.isEmpty()) {

		} else {
			tag1 = tag_ArrayList.get(0);
		}
		if (event_text.trim().length() <= 0) {

			Toast.makeText(getApplicationContext(), "please enter event", Toast.LENGTH_SHORT)
					.show();

			// Toast.makeText(getApplicationContext(),
			// "------"+final_date+"-"+final_month+"-"+final_year, Toast.LENGTH_SHORT).show();
			// Toast.makeText(getApplicationContext(),
			// "------"+final_hour+"-"+final_minute, Toast.LENGTH_SHORT).show();
		}

		else {

			Log.d("with=", "" + final_with);
			Log.d("event=", "" + event_text);
			Log.d("where=", "" + final_where);
			Log.d("image_byte=", "" + imageInByte);
			Log.d("audio byte=", "" + fileByteArray);
			Log.d("final time", "" + final_time);
			Log.d("am_pm=", "" + final_select_am_pm);
			Log.d("final_start", "" + final_date_month_year);
			Log.d("final_end", "" + final_until_end);

			Log.d("final_longdate =", "" + final_date_long);

			Log.d("final hour =", "" + final_hour);
			Log.d("final_minute =", "" + final_minute);

			Log.d("repeat =", "" + final_repeat);
			Log.d("final repeat day_ month =", ""
					+ final_repeat_every_day_month);
			Log.d("final_end_date =", "" + final_end_date);
			Log.d("final_end_month =", "" + final_end_month);
			Log.d("final_end_year =", "" + final_end_year);
			
			

			
			
			// Log.d("final untill date =", "" + final_untill_date);
			for (String str : tag_ArrayList) {
				Log.d("tag=", "" + str);
			}

			set_selected_items();
			TaskListArray array = TaskListArray.getInstance(context);
			Task newTask = new Task(event_text);
			newTask.setEvent_with(final_with);
			newTask.setEvent("event");
			newTask.setEvent_tag(tag1);
			// newTask.setEvent_Audio(encoded_audio_string);
			// newTask.setEvent_photo(encodedImage);
			newTask.setLocation(final_where);
			newTask.setImportance(importance);
			newTask.setSearch_date(final_date_long);
			newTask.setam_pm(final_select_am_pm);
			newTask.setFinal_repeat(final_repeat);// daily monthly
			newTask.setFinal_repeat_day_month(final_repeat_every_day_month); // every
																				// 2nd
																				// day
			newTask.setFinal_until_date(final_until_end);// last date of event
			newTask.setSelected_items(selected_items);
			newTask.setUserId(email1);

			/*
			 * Calendar cal = Calendar.getInstance();
			 * cal.set(Calendar.DAY_OF_WEEK, repeat_day);
			 * cal.set(Integer.parseInt(final_year),
			 * Integer.parseInt(final_month) - 1, Integer.parseInt(final_date),
			 * Integer.parseInt(final_hour), Integer.parseInt(final_minute), 0);
			 * // Setting a new // Calendar instance // with the user //
			 * reminder date
			 */
			Time reminder = new Time(); // Reminder in Time format to store in
										// the object
			reminder.set(0, Integer.parseInt(final_minute),
					Integer.parseInt(final_hour), Integer.parseInt(final_date),
					Integer.parseInt(final_month) - 1,
					Integer.parseInt(final_year)); // Setting the reminder to be
													// saved in the Task object
			newTask.setReminder(reminder);
			newTask.setHasReminder(1); // Setting the boolean flag to indicate
										// that that the task has reminder

			rowID = taskDataBase.addTask(newTask); // Adding to DataBase

			System.out.print("row id" + rowID);


            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(final_year), Integer.parseInt(final_month) - 1, Integer.parseInt(final_date),
                    Integer.parseInt(final_hour), Integer.parseInt(final_minute));

           /// public final void set (int year, int month, int day, int hourOfDay, int minute)

			/*
			 * Toast.makeText(getApplicationContext(), "row id" + rowID, Toast.LENGTH_SHORT)
			 * .show();
			 */


            //=====================================================
            // add Event in Google Calendar
            //=======================================================

            System.out.println("====zzzzz========="+ c);

            if(calbool==1) {
                final ContentValues event = new ContentValues();
                event.put(Events.CALENDAR_ID, 1);

                event.put(Events.TITLE, event_text);
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





			newTask.setId(rowID);
			array.addTask(newTask);
			
			// add multiple images
			if (img_ArrayList != null) {
				for (int i = 0; i < img_ArrayList.size(); i++) {

					taskDataBase.add_photo(rowID, img_ArrayList.get(i)
							.getPhoto());
				}
			}
			System.out.print(""+audio_ArrayList);

			// add multiple audio
			if (audio_ArrayList != null) {
				for (int i = 0; i < audio_ArrayList.size(); i++) {

					taskDataBase.add_audio(rowID, audio_ArrayList.get(i)
							.getAudio());
				}
			}

			// setReminder.setOneTimeReminder(context, cal, newTask); //
			// Creating a
			// new
			// reminder
			// to be
			// added to
			// the alarm
			// manager
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(final_year),
					Integer.parseInt(final_month) - 1,
					Integer.parseInt(final_date), Integer.parseInt(final_hour),
					Integer.parseInt(final_minute), 0); // Setting a new
														// Calendar instance
														// with the user
					
			// reminder date
			System.out.println("====insert===final_year================"+final_year);
			
			System.out.println("====insert===final_month================"+final_month);
			
			System.out.println("====insert===final_date================"+final_date);
		      
			System.out.println("==========calender===insert==="+cal);
			
			
			if(setSnooze==true){
				setReminder.setSnooze(context, cal, newTask,
						interval,5);
				System.out.println("=========Set snooze==="+cal);
			}

			Log.d("after final hour =", "" + final_hour);
			Log.d("after final_minute =", "" + final_minute);
			if (set_time == true) {
				if (seletedItems == null || seletedItems.isEmpty()) {

					System.out.println("daily " + interval);
					// setReminder.setOneTimeReminder(context, cal, newTask);
					setReminder.setRepeatTimeReminder(context, cal, newTask,
							interval);
					System.out.println("seletedItems=");
				} else {

					switch (event_repeat_value) {
					case 1:// week

						// set_selected_items();

						interval = 7 * 24 * 60 * 60 * 1000;
						for (int j = 0; j < seletedItems.size(); j++) {

							cal.set(Calendar.DAY_OF_WEEK, seletedItems.get(j));
							System.out.println("seletedItems in saving="
									+ seletedItems.get(j));

							setReminder.setRepeatweekReminder(context, cal,
									newTask, j, interval);
						}
						seletedItems.clear();

						break;
					case 3:// yearly

						interval = 365 * 24 * 60 * 60 * 1000;
						for (int j = 0; j < seletedItems.size(); j++) {
							cal.set(Calendar.DAY_OF_MONTH, seletedItems.get(j));

							setReminder.setRepeatweekReminder(context, cal,
									newTask, j, interval);
						}
						seletedItems.clear();

						break;
					default:
						break;

					}
				}

			} else {
				
				
				System.out.println("==Update=====Cal========="+cal);
				setReminder.setOneTimeReminder(context, cal, newTask);
				
			}

			// setReminder.setProximityAlert(context, newTask, latitude,
			// longitude);

			// Toast.makeText(getApplicationContext(),
			// "------"+final_date+"-"+final_month+"-"+final_year, Toast.LENGTH_SHORT).show();
			// Toast.makeText(getApplicationContext(),
			// "------"+final_hour+"-"+final_minute, Toast.LENGTH_SHORT).show();

			if (final_end_date != null) {
				System.out.println("end date " + final_end_date);
				System.out.println("end month" + final_end_month);
				System.out.println("end year" + final_end_year);
				Calendar timeOff = Calendar.getInstance();
				timeOff.set(Integer.parseInt(final_end_year),
						Integer.parseInt(final_end_month) - 1,
						Integer.parseInt(final_end_date),
						Integer.parseInt(final_hour),
						Integer.parseInt(final_minute), 0);
				/*
				 * timeOff.set(Calendar.HOUR_OF_DAY,12);
				 * timeOff.set(Calendar.MINUTE,00); timeOff.set(Calendar.SECOND,
				 * 00);
				 */
				
				

				setReminder.setCanceltimeReminder(context, timeOff, newTask);
			}
			finish();
		}
	}

	public void set_selected_items() {
		if (seletedItems != null) {
			StringBuilder stringBuilder = new StringBuilder();

			for (int j = 0; j < seletedItems.size(); j++) {
				stringBuilder.append(seletedItems.get(j) + "-");

				System.out.println("seletedItems=" + seletedItems.get(j));

			}
			selected_items = stringBuilder.toString();
			System.out.println("seletedItems=" + selected_items);
		}
	}

	public void get_selected_items() {
		ArrayList<String> arrayList = new ArrayList<String>();
		String str = selected_items;
		String[] tokens = str.split("-");

		for (String s : tokens) {
			arrayList.add(s);
			try {
				seletedItems.add(Integer.parseInt(s));
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		/*
		 * for(int j=0;j<arrayList.size();j++) {
		 * System.out.print("retrieve selcted="+seletedItems.get(j));
		 * 
		 * }
		 */

	}

	public void get_end_date() {
		ArrayList<String> arrayList = new ArrayList<String>();
		String str = final_until_end;
		String[] tokens = str.split("-");
		for (String s : tokens) {
			arrayList.add(s);
		}
		final_end_date = arrayList.get(0);
		final_end_month = arrayList.get(1);
		final_end_year = arrayList.get(2);

	}

	public void set_final_end() {
		final_until_end = final_end_date + "-" + final_end_month + "-"
				+ final_end_year;
	}

	public void audio_into_byte() {
		if (outputFile == null) {
			// Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
		} else {
			try {
				fis = new FileInputStream(outputFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int read;
			try {
				while ((read = fis.read(buffer)) != -1) {
					baos.write(buffer, 0, read);
					baos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			fileByteArray = baos.toByteArray();

			encoded_audio_string = Base64.encodeToString(fileByteArray, 0);

		}

	}

	public void byte_into_audio(int position) {

		encoded_audio_string = audio_ArrayList.get(position).getAudio();

		byte[] decoded = Base64.decode(encoded_audio_string, 0);
		// Utilities.log("~~~~~~~~ Decoded: ", Arrays.toString(decoded));

		try {
			/*
			 * File file2 = new File(Environment.getExternalStorageDirectory() +
			 * "/hello-5.3gpp");
			 */
			File file2 = new File(Environment.getExternalStorageDirectory(),
					System.currentTimeMillis() + ".3gpp");
			FileOutputStream os = new FileOutputStream(file2, true);
			os.write(decoded);
			os.close();
			// Toast.makeText(getApplicationContext(), ""+file2, Toast.LENGTH_SHORT).show();

			myPlayer = new MediaPlayer();
			myPlayer.setDataSource(file2.toString());
			myPlayer.setOnPreparedListener(this);
			myPlayer.prepare();
			myPlayer.start();
			System.out.println("time duration of file" + duration);

			myPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(getApplicationContext(),
					// "complete",Toast.LENGTH_SHORT).show();
					// start_playImageView.setImageResource(R.drawable.event_play_button);

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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
		event_when_TextView
				.setTextColor(getResources().getColor(R.color.Green));
		/*
		 * event_when_TextView.setText("" + months_str[num] + " " + final_date +
		 * "," + final_year + " " + final_hour + ":" + final_minute +
		 * final_select_am_pm);
		 */

		event_when_TextView
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
		event_where_TextView.setTextColor(getResources()
				.getColor(R.color.Green));
		if (atvPlaces.getText().toString().length() == 0) {
			event_where_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			event_where_TextView.setText("where");
		} else {
			final_where = atvPlaces.getText().toString();
			event_where_TextView.setText("" + atvPlaces.getText().toString());
		}
	}

	public void set_slice_text() {
		event_slice_text.setTextColor(getResources().getColor(R.color.Green));
		if (tag_ArrayList.size() == 0) {
			event_slice_text
					.setTextColor(getResources().getColor(R.color.Gray));
			event_slice_text.setText("slice");
		} else {
			tag1 = tag_ArrayList.get(0);
			event_slice_text.setText("" + tag_ArrayList.get(0).toString());
		}
	}

	public void set_with_text() {
		event_with_TextView
				.setTextColor(getResources().getColor(R.color.Green));
		if (mContactListTextview.getText().toString().length() == 0) {
			event_with_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			event_with_TextView.setText("with");
		} else {
			final_with = mContactListTextview.getText().toString();
			event_with_TextView.setText(""
					+ mContactListTextview.getText().toString());
		}
	}

	public void close_all() {
		when_close = true;
		when_on = true;

		with_on = true;
		where_on = true;
		slice_on = true;
		// when_plus_minus();
		where_plus_minus();
		slice_plus_minus();
		WithData();
	}

	/* Bottom Section */
	public void priority_dialog() {
		String[] priority_text_arr = { "Sun", "Mon", "Tues", "Wed", "Thu",
				"Fri", "Sat", "1", "2", "3", "4", "5", "6", "7" };

		Builder builder = new Builder(this);
		builder.setTitle("Importance");

		ListView priorityListView = new ListView(this);
		ArrayAdapter<String> a = new ArrayAdapter<>(getApplicationContext(),
				android.R.layout.select_dialog_multichoice, priority_text_arr);
		priorityListView.setAdapter(a);
		// priorityListView.setAdapter(new Priority_dialog_adapter(this));
		priorityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				/*
				 * Toast.makeText( getApplicationContext(), "" +
				 * Priority_dialog_adapter.priority_text_arr[position],
				 * Toast.LENGTH_SHORT).show();
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
		 * Toast.makeText(getApplicationContext(), ""+which, Toast.LENGTH_SHORT).show(); } });
		 */
		builder.show();
	}

	@SuppressLint("NewApi")

	public void alarm_dialog() {

		RelativeLayout relativeLayout;
		ImageView upImageView, downImageView;

		Button set_alarm_Button;
		alarm_dialog = new Dialog(EventActivity.this);
		/*
		 * alarm_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		alarm_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alarm_dialog.setContentView(R.layout.alarm_reminder);
		alarm_hour_minuteTextView = (EditText) alarm_dialog
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
				/*
				 * Toast.makeText(getApplicationContext(), "" +
				 * alarm_final_value, Toast.LENGTH_SHORT).show();
				 */

				if (alarm_hour_minuteTextView.getText().toString().trim()
						.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"please enter some value", Toast.LENGTH_SHORT).show();

				} else if (Integer.parseInt(alarm_hour_minuteTextView.getText()
						.toString()) == 0) {
					Toast.makeText(getApplicationContext(),
							"you can't set zero", Toast.LENGTH_SHORT).show();
				} else {
					alarm_final_value = Integer
							.parseInt(alarm_hour_minuteTextView.getText()
									.toString());

					

					if (minute_hour_bool == true) {
						// minute
						if (alarm_final_value <= 60) {
							interval = 1000 * 60 * alarm_final_value;
							setSnooze = true;
							//set_time = true;
							alarm_dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(),
									"please enter right minute", Toast.LENGTH_SHORT).show();
						}
					} else {
						// hour
						if (alarm_final_value <= 12) {
							interval = 1000 * 60 * 60 * alarm_final_value;
							setSnooze = true;
							//set_time = true;
							alarm_dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(),
									"please enter right hour", Toast.LENGTH_SHORT).show();

						}
					}
				}
				/*
				 * // code end if (minute_hour_bool == true) {
				 * 
				 * Toast.makeText(getApplicationContext(), "minute=" +
				 * alarm_final_value, Toast.LENGTH_SHORT).show();
				 * 
				 * interval = 1000 * 60 * alarm_final_value;
				 * 
				 * Toast.makeText(getApplicationContext(), "interval=" +
				 * interval, Toast.LENGTH_SHORT).show();
				 * 
				 * } else {
				 * 
				 * Toast.makeText(getApplicationContext(), "hour=" +
				 * alarm_final_value, Toast.LENGTH_SHORT).show();
				 * 
				 * interval = 1000 * 60 * 60 * alarm_final_value;
				 * 
				 * Toast.makeText(getApplicationContext(), "interval=" +
				 * interval, Toast.LENGTH_SHORT).show();
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

				if (alarm_hour_minuteTextView.getText().toString().trim()
						.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"please enter some value", Toast.LENGTH_SHORT).show();

				} else {
					alarm_final_value = Integer
							.parseInt(alarm_hour_minuteTextView.getText()
									.toString());
					if (minute_hour_bool == true) {
						// minute
						if (alarm_final_value <= 60) {
							invoke_minute_hour_up();
						} else {
							alarm_final_value = 60;
							invoke_minute_hour_up();
						}
					} else {
						// hour
						if (alarm_final_value <= 12) {
							invoke_minute_hour_up();
						} else {
							alarm_final_value = 12;
							invoke_minute_hour_up();

						}
					}
				}

			}
		});
		downImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alarm_hour_minuteTextView.getText().toString().trim()
						.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"please enter some value", Toast.LENGTH_SHORT).show();

				} else {
					alarm_final_value = Integer
							.parseInt(alarm_hour_minuteTextView.getText()
									.toString());
					if (minute_hour_bool == true) {
						// minute
						if (alarm_final_value <= 60) {
							invoke_minute_hour_down();
						} else {
							alarm_final_value = 2;
							invoke_minute_hour_down();
						}
					} else {
						// hour
						if (alarm_final_value <= 12) {
							invoke_minute_hour_down();
						} else {
							alarm_final_value = 2;
							invoke_minute_hour_down();
						}
					}
				}
			}
		});

		alarm_dialog.show();

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
					R.color.DarkGreen));
			alarm_hour_text.setTextColor(getResources().getColor(R.color.Gray));
			minute_hour_bool = true;
			alarm_final_value = 60;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {

			alarm_hour_text.setTextColor(getResources().getColor(
					R.color.DarkGreen));
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

	public void importance_dialog() {
		LinearLayout lowLinearLayout, highLinearLayout, mediumLinearLayout;
		importance_dialog = new Dialog(EventActivity.this);
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

	public void perform_crop() {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", true);
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);

		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/* Bottom Section */
	public void Week_list_dialog() {

	
		
		System.out.println("=============test=========="+seletedItems.size());
		
		// following code will be in your activity.java file
		final CharSequence[] items = { " Sunday ", " Monday ", " Tuesday ",
				" Wednesday ", "Thursday", "Friday", "Saturday" };
		final CharSequence[] months = { " January ", " February ", " March ",
				" April ", " May ", " June ", "July", "August", "September",
				"October", " November ", " December " };
		CharSequence[] items1 = null;
		final Builder builder = new Builder(this);

		builder.setCancelable(false);
		switch (event_repeat_value) {
		case 1:
			items1 = items;
			builder.setTitle("Select The Repeating Day");
			break;
		case 3:
			items1 = months;
			builder.setTitle("Select The Repeating Month");
			break;
		default:
			break;
		}
		calendar = Calendar.getInstance();
		calendar_year = calendar.get(Calendar.YEAR);

		calendar_month = calendar.get(Calendar.MONTH);
		calendar_day = calendar.get(Calendar.DAY_OF_MONTH);
		// showDate(calendar_year, calendar_month + 1, calendar_day);

		// arraylist to keep the selected items
		// seletedItems = new ArrayList();

		boolean[] checkedValues = new boolean[items1.length];
		if (selected_items != null) {
			for (int i = 0; i < seletedItems.size(); i++) {

				checkedValues[seletedItems.get(i) - 1] = true;
				/*
				 * Toast.makeText(getApplicationContext(), "" +
				 * seletedItems.get(i), Toast.LENGTH_SHORT).show();
				 */

			}
		}
		/*
		 * checkedValues[0] = true; checkedValues[1] = true; checkedValues[2] =
		 * true;
		 */

		builder.setMultiChoiceItems(items1, checkedValues,
				new DialogInterface.OnMultiChoiceClickListener() {
					// indexSelected contains the index of item (of which
					// checkbox checked)
					@Override
					public void onClick(DialogInterface dialog,
							int indexSelected, boolean isChecked) {
						if (isChecked) {
							// If the user checked the item, add it to the
							// selected items
							// write your code when user checked the checkbox
							seletedItems.add(indexSelected + 1);
						} else if (seletedItems.contains(indexSelected)) {
							// Else, if the item is already in the array, remove
							// it
							// write your code when user Uchecked the checkbox
							seletedItems.remove(Integer
									.valueOf(indexSelected - 1));
						}
					}
				})

				// Set the action buttons
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog1, int id) {
						// Your code when user clicked on OK
						// You can write the code to save the selected item here

						set_time = true;

						/* showDialog(999); */
						showDatePicker();
						set_selected_items();
						switch (event_repeat_value) {
						case 1:
							final_repeat = "weekly";
							break;
						case 3:
							final_repeat = "yearly";
							builder.setTitle("Select The Repeating Month");
							break;
						default:
							break;
						}

						for (int i = 0; i < seletedItems.size(); i++) {

							/*
							 * * Toast.makeText(getApplicationContext(), "" +
							 * seletedItems.get(i), Toast.LENGTH_SHORT).show();
							 */

						}

					}
				})

				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Your code when user clicked on Cancel
								seletedItems.clear();
							}
						});
		/*
		 * .setNeutralButton("Until", new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int id) { //
		 * Your code when user clicked on Cancel
		 * 
		 * showDialog(999);
		 * 
		 * } });
		 */

		dialog = builder.create();// AlertDialog dialog; create like this
									// outside onClick
		dialog.show();

	}

	public void daily_monthly_dialog() {
		final EditText every_day_text;

		ImageView date_plus_img;
		Button set_Button, cancel_Button;
		daily_dialog = new Dialog(EventActivity.this);
		/*
		 * alarm_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		daily_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		daily_dialog.setContentView(R.layout.daily_dialog);
		every_day_text = (EditText) daily_dialog
				.findViewById(R.id.every_day_editText);
		days_month_text = (TextView) daily_dialog
				.findViewById(R.id.days_month_text);
		set_Button = (Button) daily_dialog.findViewById(R.id.set_day_btn);
		cancel_Button = (Button) daily_dialog.findViewById(R.id.set_cancel_btn);

		dateView = (EditText) daily_dialog.findViewById(R.id.textView3);

		date_plus_img = (ImageView) daily_dialog
				.findViewById(R.id.date_plus_img);
		/*
		 * switch (event_repeat_value) { case 0:
		 * days_month_text.setText("days"); break; case 2:
		 * days_month_text.setText("months");
		 * 
		 * break; default: break; }
		 */
		if (updateid > 0) {
			calendar_year = Integer.parseInt(final_end_year);

			calendar_month = Integer.parseInt(final_end_month) - 1;
			calendar_day = Integer.parseInt(final_end_date);
			every_day_text.setText(final_repeat_every_day_month);
			/*
			 * Toast.makeText(getApplicationContext(), "perform click " +
			 * final_end_date, Toast.LENGTH_SHORT).show();
			 */
			// set_Button.performClick();
			set_interval_at_complete();// call
		}

		else {
			calendar = Calendar.getInstance();
			calendar_year = calendar.get(Calendar.YEAR);

			calendar_month = calendar.get(Calendar.MONTH);
			calendar_day = calendar.get(Calendar.DAY_OF_MONTH);
		}

		showDate(calendar_year, calendar_month + 1, calendar_day);
		// fg
		date_plus_img.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// daily_dialog.dismiss();

				/* showDialog(999); */
				showDatePicker();

			}
		});
		set_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String every_day = every_day_text.getText().toString();
				// Log.d("every_day", every_day);
				/*
				 * Toast.makeText(getApplicationContext(), "perform click done",
				 * Toast.LENGTH_SHORT).show();
				 */
				if (every_day != null && every_day.length() > 0) {
					int repeat_every_day_month = Integer.valueOf(every_day);
					final_repeat_every_day_month = every_day;
					switch (event_repeat_value) {
					case 0:// dialy

						// interval=AlarmManager.INTERVAL_DAY*repeat_every_day;
						interval = 1000 * 60 * 60 * 24 * repeat_every_day_month;
						System.out.println("daily repeat"
								+ repeat_every_day_month);
						System.out.println("daily repeat interval" + interval);
						final_repeat = "daily";

						break;
					case 2:// Monthly
							// interval = AlarmManager.INTERVAL_DAY
							// *30*repeat_every_day;
						interval = (AlarmManager.INTERVAL_DAY * 31 * repeat_every_day_month);
						/*
						 * interval = 1000 * 60 * 60 * 30 * 24
						 * repeat_every_day_month;
						 */
						System.out.println("monthly repeat"
								+ repeat_every_day_month);
						final_repeat = "monthly";
						break;
					default:
						break;

					}
					set_time = true;

					seletedItems.clear();
					selected_items = null;
					daily_dialog.dismiss();
					Log.d("if every_day", every_day);

				} else {
					Toast.makeText(getApplicationContext(),
							"You can't set blank", Toast.LENGTH_SHORT).show();
					Log.d("else every_day", every_day);
				}
				// gfdjjbk

			}
		});
		cancel_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				every_day_text.clearComposingText();
				daily_dialog.dismiss();

			}
		});

		// daily_dialog.show();

	}

	private void showDate(int year, int month, int day) {
		final_end_date = String.valueOf(day);
		final_end_month = String.valueOf(month);
		final_end_year = String.valueOf(year);
		String[] months = { "", "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };

		dateView.setText(new StringBuilder().append(day).append("-")
				.append(months[month]).append("-").append(year));
	}

	public void hour_validation() {

		String hour = hour_TextView.getText().toString();

		String minute = minute_TextView.getText().toString();
		if (hour.trim().length() == 0) {
			Toast.makeText(getApplicationContext(), "please enter hour value",
					Toast.LENGTH_SHORT).show();

		} else if (minute.trim().length() == 0) {
			Toast.makeText(getApplicationContext(),
					"please enter minute value", Toast.LENGTH_SHORT).show();
		} else if (Integer.parseInt(hour) > 12) {
			Toast.makeText(getApplicationContext(),
					"please enter hour less than 12", Toast.LENGTH_SHORT).show();

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
			Toast.makeText(getApplicationContext(), "please enter minute", Toast.LENGTH_SHORT)
					.show();

		} else if (Integer.parseInt(minute) >= 60) {
			Toast.makeText(getApplicationContext(),
					"please enter minute less than 60", Toast.LENGTH_SHORT).show();

		} else {
			// hour_value=Integer.parseInt(hour);
			if (Integer.parseInt(hour) <= 12 && Integer.parseInt(minute) < 60) {
				final_minute = minute;

				/*
				 * Toast.makeText(getApplicationContext(),
				 * "please "+final_hour+":"+final_minute, Toast.LENGTH_SHORT) .show();
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

	/* ----------------------update audio dialog---------------------------- */
	/*--------------------audio dialog that open when click on audio image view--------------------*/
	public void update_audio_dialog() {
		RelativeLayout relativeLayout;

		audiodialog = new Dialog(EventActivity.this);
		audiodialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		audiodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		audiodialog.setContentView(R.layout.dialog_activity);

		relativeLayout = (RelativeLayout) audiodialog
				.findViewById(R.id.dialog_layout);
		saveLinearLayout = (LinearLayout) audiodialog
				.findViewById(R.id.save_layout);
		mSeekBarPlayer = (SeekBar) audiodialog.findViewById(R.id.seekBar1);
		recording_total_time_TextView = (TextView) audiodialog
				.findViewById(R.id.recording_total_time_text);
		cancel_recordingTextView = (TextView) audiodialog
				.findViewById(R.id.cancel_recording_text);

		delete_ImageView = (ImageView) audiodialog
				.findViewById(R.id.delete_recording_img1);
		save_recordingTextView = (TextView) audiodialog
				.findViewById(R.id.save_recording_text);
		start_recordingImageView = (ImageView) audiodialog
				.findViewById(R.id.start_recording_img);
		recording_TextView = (TextView) audiodialog
				.findViewById(R.id.recording_text);
		recording_TextView.setText("press to play");
		start_playImageView = (ImageView) audiodialog
				.findViewById(R.id.start_play_img);

		share_audioImageView = (ImageView) audiodialog
				.findViewById(R.id.share_audio);

		save_recordingTextView.setText("{Share}");
		start_recordingImageView.setClickable(false);
		start_recordingImageView.setEnabled(false);

		// cancel recording
		delete_ImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(updateid>0){
				
					taskDataBase.deleteImageAudio(audioid);
					removeaudio(img_position);
					//System.out.println("====imageid============="+imageid);
					//Toast.makeText(getApplicationContext(),""+ imageid, Toast.LENGTH_LONG).show();
				}else{
					removeaudio(img_position);
				}
				
				
				audiodialog.dismiss();
				
			}
		});
		// cancel recording
		cancel_recordingTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audiodialog.dismiss();
			}
		});

		// share audio
		share_audioImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				share_audio();

				audiodialog.dismiss();
			}
		});
		// save recording
		save_recordingTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				share_audio();

				audiodialog.dismiss();

			}
		});

		mSeekBarPlayer
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (fromUser) {
							myPlayer.seekTo(progress);
							updateTime();
						}
					}
				});
		start_playImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show();
				running = true;
				if (isplay == false) // play
				{

					if (ispause == true) {
						myPlayer.start();
						ispause = false;
						isplay = true;
						mSeekBarPlayer.postDelayed(onEverySecond, 1000);
						start_playImageView
								.setImageResource(R.drawable.event_pause_button);
					} else {
						// play();
						byte_into_audio(img_position);
						isplay = true;

						start_playImageView
								.setImageResource(R.drawable.event_pause_button);

					}

				} else // pause
				{
					isplay = false;
					ispause = true;
					myPlayer.pause();
					start_playImageView
							.setImageResource(R.drawable.event_play_button);

					// start_playImageView.setImageResource(R.drawable.abc_btn_check_material);
				}

			}
		});
		start_recordingImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (isrecord == false) {
					start_recording();
					isrecord = true;

				} else {
					stop_recording();
					

					isrecord = false;
				}

				// dialog.dismiss();

			}
		});
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// dialog.dismiss();

			}
		});
		audiodialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(),
				// "dialog dismiss",Toast.LENGTH_SHORT).show();
				try {

					isrecord = false;
					isplay = false;
					ispause = false;
					isrecord = false;
					startTime = 0L;
					timeInMilliseconds = 0L;
					timeSwapBuff = 0L;

					running = false;
					current = 0;
					duration = 0;
					customHandler.removeCallbacks(onEverySecond);
					stopPlay();

					// outputFile = null;
					myRecorder.stop();
					myRecorder.release();
					myRecorder = null;

					customHandler.removeCallbacks(updateTimerThread);

				} catch (NullPointerException e) {
					// TODO: handle exception
				}

			}
		});
		audiodialog.show();
	}

	public void share_audio() {

		// TODO Auto-generated method stub
		if (encoded_audio_string != null) {

			byte[] decoded = Base64.decode(encoded_audio_string, 0);
			// Utilities.log("~~~~~~~~ Decoded: ", Arrays.toString(decoded));

			// you can create a new file name "test.jpg" in sdcard folder.
			File f = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "test.3gpp");
			/*
			 * destination = new File(
			 * Environment.getExternalStorageDirectory(),
			 * System.currentTimeMillis() + ".jpg");
			 */
			try {
				f.createNewFile();

				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(decoded);

				// remember close de FileOutput
				fo.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);

			intent.setType("audio/*");
			// Make sure you put example png image named myImage.png in your
			// directory

			audiouri = Uri.fromFile(f);
			System.out.println("immmmm" + audiouri);

			intent.putExtra(Intent.EXTRA_STREAM, audiouri);

			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(Intent.createChooser(intent, "share"));
			}

		}

	}

	private void set_interval_at_complete() {

		// TODO Auto-generated method stub
		// String every_day = every_day_text.getText().toString();
		String every_day = final_repeat_every_day_month;
		// Log.d("every_day", every_day);
		/*
		 * Toast.makeText(getApplicationContext(), "perform click done", Toast.LENGTH_SHORT)
		 * .show();
		 */
		if (every_day != null && every_day.length() > 0) {
			int repeat_every_day_month = Integer.valueOf(every_day);
			final_repeat_every_day_month = every_day;

			switch (event_repeat_value) {
			case 0:// dialy

				// interval=AlarmManager.INTERVAL_DAY*repeat_every_day;
				interval = 1000 * 60 * 60 * 24 * repeat_every_day_month;
				System.out.println("daily repeat" + repeat_every_day_month);
				System.out.println("daily repeat interval" + interval);
				final_repeat = "daily";

				break;
			case 2:// Monthly
					// interval = AlarmManager.INTERVAL_DAY
					// *30*repeat_every_day;
				interval = 1000 * 60 * 60 * 30 * 24 * repeat_every_day_month;
				System.out.println("monthly repeat" + repeat_every_day_month);
				final_repeat = "monthly";
				break;
			default:
				break;

			}
			set_time = true;
			daily_dialog.dismiss();
			Log.d("if every_day", every_day);

		} else {
			/*
			 * Toast.makeText(getApplicationContext(), "You can't set blank",
			 * Toast.LENGTH_SHORT) .show();
			 */
			// Log.d("else every_day", every_day);
		}

		// gfdjjbk

	}

	@SuppressWarnings("deprecation")
	public void get_check_until() throws ParseException {

		ArrayList<String> arrayList1 = new ArrayList<String>();
		String str1 = final_until_end;
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
		 * Toast.makeText(getApplicationContext(), "Current_date", Toast.LENGTH_SHORT).show();
		 * }
		 */

		if (date1.compareTo(date2) > 0) {
			System.out.println("Date1 is after Date2");
			// do

			showDate(dialog_year, dialog_month, dialog_day);

		} else if (date1.compareTo(date2) < 0) {
			System.out.println("Date1 is before Date2");
			/*
			 * Toast.makeText(getApplicationContext(),
			 * "Please select right date", Toast.LENGTH_SHORT).show();
			 */
			showDatePicker();

		} else if (date1.compareTo(date2) == 0) {
			System.out.println("Date1 is equal to Date2");
			showDate(dialog_year, dialog_month, dialog_day);

		} else {
			System.out.println("How to get here?");
		}

	}

	private void showDatePicker() {
		// TODO Auto-generated method stub
		date = new DatePickerFragment();
		/**
		 * Set Up Current Date Into dialog
		 */
		Calendar calender = Calendar.getInstance();
		Bundle args = new Bundle();
		args.putInt("year", calender.get(Calendar.YEAR));
		args.putInt("month", calender.get(Calendar.MONTH));
		args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));

		date.setArguments(args);

		/**
		 * Set Call back to capture selected date
		 */
		date.setCallBack(ondate);
		date.show(getSupportFragmentManager(), "Date Picker");

	}

	public void show_dialog() {
		builder = new Builder(this);
		builder.setTitle("Title");
		builder.setMessage("Message");

		builder.create();

	}

	OnDateSetListener ondate = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			try {
				dialog_day = dayOfMonth;
				dialog_month = monthOfYear + 1;
				dialog_year = year;
				final_until_end = dialog_day + "-" + dialog_month + "-"
						+ dialog_year;

				get_check_until();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public void share_all_images() {
		imageUris = new ArrayList<Uri>();

		if (img_ArrayList != null) {
			for (int i = 0; i < img_ArrayList.size(); i++) {
				encodedImage = img_ArrayList.get(i).getPhoto();
				File imgFile = new File(encodedImage);
				imageuri = Uri.fromFile(imgFile);
				imageUris.add(imageuri);
			}
			System.out.print("img uri" + imageUris);
		}

	}

	public void share_all_audio() {
		
		audioUris = new ArrayList<Uri>();
		if (audio_ArrayList != null) {
			for (int i = 0; i < audio_ArrayList.size(); i++) {
				encoded_audio_string = audio_ArrayList.get(i).getAudio();
				byte[] decoded = Base64.decode(encoded_audio_string, 0);
			/*	File f = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "test.3gpp");*/
				timeinmilisecod = System.currentTimeMillis();
				File f = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "/lifereminder"+timeinmilisecod +".3gpp");
			
				
		


				
				
			
				try {
					f.createNewFile();
					// write the bytes in file
					FileOutputStream fo = new FileOutputStream(f);
					fo.write(decoded);
					// remember close de FileOutput
					fo.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				audiouri = Uri.fromFile(f);
				audioUris.add(audiouri);
			}
			System.out.print("Audio uri" + audioUris);
		}
		/*if (audio_ArrayList != null) {
			for (int i = 0; i < audio_ArrayList.size(); i++) {
				encoded_audio_string = audio_ArrayList.get(i).getAudio();
				File imgFile = new File(encoded_audio_string);
				audiouri = Uri.fromFile(imgFile);
				audioUris.add(audiouri);
			}
			System.out.print("Audio uri" + audioUris);
		}*/

	}

	public void share_image() {

		if (encodedImage != null) {

			byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);
			// you can create a new file name "test.jpg" in sdcard
			// folder.
			File imagefile = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "test.jpg");
			/*
			 * destination = new File(
			 * Environment.getExternalStorageDirectory(),
			 * System.currentTimeMillis() + ".jpg");
			 */
			try {
				imagefile.createNewFile();

				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(imagefile);
				fo.write(decodedString);

				// remember close de FileOutput
				fo.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			imageuri = Uri.fromFile(imagefile);
			System.out.println("immmmm" + imageuri);

		}
	}

	public void share_audio_with_note() {

		// TODO Auto-generated method stub
		if (encoded_audio_string != null) {

			byte[] decoded = Base64.decode(encoded_audio_string, 0);
			File f = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "test.3gpp");
			try {
				f.createNewFile();
				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(decoded);
				// remember close de FileOutput
				fo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			audiouri = Uri.fromFile(f);
			System.out.println("immmmm" + audiouri);

		}

	}

	public void whole_note_data() {

		String[] months_str = { "", "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };
		int num = Integer.parseInt(final_month);
		event_text = event_EditText.getText().toString();
		final_where = atvPlaces.getText().toString();
		final_with = mContactListTextview.getText().toString();
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
		set_final_end();
		if (tag_ArrayList.isEmpty()) {

		} else {
			tag1 = tag_ArrayList.get(0);
		}
		str = new StringBuilder("\n");
		str.append("Event : " + event_text + "\n");
		str.append("When :" + final_when + "\n");
		str.append("Where :" + final_where + "\n");
		str.append("With :" + final_with + "\n");
		str.append("Slice :" + tags_editText.getText().toString() + "\n");
		// Toast.makeText(getApplicationContext(), ""+str, Toast.LENGTH_SHORT).show();
		System.out.println("" + str);

	}

	public void share_note(View v) {

		whole_note_data();
		share_all_images();
		share_all_audio();
		Intent shareintent = new Intent();
		
	
		
		if (audioUris.size() != 0) {
			System.out.print("audio uri at share" + audioUris);
			shareintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
					audioUris);
		}

		if (imageUris.size() != 0) {
			System.out.print("img uri at share" + imageUris);
			shareintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
					imageUris);
		}
		

		if (imageUris.size() != 0 && audioUris.size() != 0) {
			audioUris.addAll(imageUris);
			shareintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
					audioUris);
		}

		// shareintent.setPackage("com.whatsapp");
		shareintent.putExtra(Intent.EXTRA_TEXT, str.toString());
		shareintent.putExtra(Intent.EXTRA_SUBJECT, "Event");
		if (imageUris.size() != 0 || audioUris.size() != 0) {

			
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
			imageUris.clear();
			audioUris.clear();
		}

	}

	@Override
	public void onItemClick(
			it.sephiroth.android.library.widget.AdapterView<?> parent,
			View view, int position, long id) {
		img_position = position;
	
		switch (parent.getId()) {
		case R.id.list6:
       
			 imageid= img_ArrayList.get(position).getId();
			System.out.println("====imageid============="+imageid);
			//Toast.makeText(getApplicationContext(),""+ imageid, Toast.LENGTH_LONG).show();
			show_capture_image(position);
			
			break;
		case R.id.audio_list:
			audioid= audio_ArrayList.get(position).getId();
			update_audio_dialog();
			break;
		default:
			break;
		}
		// TODO Auto-generated method stub

	}

	public void onClickWhatsApp(View view) {

		// PackageManager pm = getPackageManager();
		try {

			Intent waIntent = new Intent(Intent.ACTION_SEND);
			waIntent.setType("text/plain");
			String text = "YOUR TEXT HERE";

			/*
			 * PackageInfo info = pm.getPackageInfo("com.whatsapp",
			 * PackageManager.GET_META_DATA);
			 */
			// Check if package exists or not. If not then code
			// in catch block will be called
			waIntent.setPackage("com.whatsapp");

			waIntent.putExtra(Intent.EXTRA_TEXT, text);
			startActivity(Intent.createChooser(waIntent, "Share with"));

		} catch (Exception e) {
			Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
					.show();
		}

	}
	
	public void asyn(final Editable edittext)
	{
	
	class Async_caller extends AsyncTask<Void, Void, String>{

		

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			chipsAdapter.getFilter().filter(edittext);
			//chipsAdapter.notifyDataSetChanged();
			
			/*if (mContactListTextview.getText().toString().length() <= 0) {
				mContactListview.setVisibility(View.GONE);
			} else {
				mContactListview.setVisibility(View.VISIBLE);

			}*/



			return null;
		}
		
	}
	
	Async_caller async_caller=new  Async_caller();
	async_caller.execute();
	
	
	}
}

