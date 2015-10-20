package email;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.life_reminder.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.ContactAdapter;
import adapter.ContactItem;
import alarm.SetReminder;
import app.ConnectionDetector;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;
import place.PlaceJSONParser;
import reminder.CreateGoogleMap;

public class EmailActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	StringBuilder str;
	int dialogid;
	boolean set_time;
	static long interval;
	private TaskListArray array;
	private Task task;
	long updateid;
	boolean setSnooze;

	private double latitude;
	private double longitude;
	private String address; // Empty string represent NONE
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	ImageView mMapButton;
	/* Bottom Section view */
	Dialog alarm_dialog;
	Dialog importance_dialog;
	ImageView priority_ImageView, alarm_ImageView;
	EditText alarm_hour_minuteTextView;
	TextView alarm_minute_text, alarm_hour_text;
	int alarm_final_value;
	boolean minute_hour_bool;
	/* Bottom Section */
	/*------------For hiding keyboard----------------*/
	//InputMethodManager imm;
	/*------------For hiding keyboard----------------*/
	// load Contact Asynctask
	private TaskListDataBase taskDataBase;
	private SetReminder setReminder;
	private Context context;

	String final_date, final_month, final_year, final_date_month_year,
			final_hour, final_minute, final_time, set_when_hour,
			final_date_long;
	int current_date, current_hour, current_minute;
	/*--------text view--------*/

	TextView email_when_TextView, email_where_TextView;
	/* ------------------Subject view-------------------- */

	LinearLayout subjectLinearLayout, inner_subjectLayout;
	ImageView subject_plusImageView;
	EditText subjectEditText;
	TextView email_subject_TextView;
	boolean subject_on;
	/* ------------------subject view End- ------------------- */

	/* ------------------Compose view-------------------- */

	LinearLayout composeLinearLayout, inner_composeLayout;
	ImageView compose_plusImageView;
	EditText composeEditText;
	TextView email_compose_TextView;
	boolean compose_on;
	/* ------------------Compose view End-------------------- */
	/* ------------------with Fuctionality -------------------- */

	LinearLayout mWhereLayout;

	View where_view_location;

	MultiAutoCompleteTextView atvPlaces;
	PlacesTask placesTask;
	ParserTask parserTask;
	ListView mListview;
	SimpleAdapter Location_adapter;

	ImageView where_plus_img;

	// event With Description

	View with_contact;
	EditText mContactListTextview;

	ArrayList<ContactItem> contactList = new ArrayList<ContactItem>();

	ListView mContactListview;

	// Event Coding
	LinearLayout whenLinearLayout;
	ImageView when_ImageView;
	View visible_view;
	TextView event_saveTextView, complete_saveTextView, share_TextView;
	boolean when_on, am_pm_bool, repeat_bool, where_on, with_on;
	View when_calender_view;
	String selected_month, selected_day, selected_year;
	String final_select_am_pm, final_repeat, final_with, final_where,
			final_subject, final_compose;
	// calender
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

	private String importance;

	private SharedPreferences loginPreferences;
	private SharedPreferences.Editor loginPrefsEditor;

	Dialog email_dialog, snooze_dialog;
    int calbool;
	/** Called when the activity is first created.; */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_cal);
		setContentView(R.layout.activity_email);

		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
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
		/* - Begin code for custom Action bar */
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.event_fn_back_icon);

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View customview = layoutInflater.inflate(R.layout.action_bar, null);
		action_bar_img = (ImageView) customview.findViewById(R.id.event_img);
		action_bar_title = (TextView) customview
				.findViewById(R.id.action_bar_title);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setCustomView(customview);
		actionBar.setDisplayShowCustomEnabled(true);
		action_bar_title.setText("email");
		action_bar_img.setImageResource(R.drawable.email_title_icon);

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
				final_date_month_year = date_month_year;

				// show selected date
				// Toast.makeText(getApplicationContext(), "" + date_month_year,
				// 300).show();

			}
		});
		find_id();
		setCurrent_time();

		get_am_pm();
		toggle_repeat();

		// get specific data read oldstuff Activity and Home Activity

		GetOldStuffData();

	}

	private void GetOldStuffData() {
		// TODO Auto-generated method stub

		taskDataBase = TaskListDataBase.getInstance(getBaseContext()); // Creatin
		array = TaskListArray.getInstance(getBaseContext());
		task = new Task();

		updateid = getIntent().getLongExtra("taskId", -1);

		dialogid = getIntent().getIntExtra("dialogid", 0);

		System.out.println("========dialogid===========" + dialogid);

		if (dialogid == 5) {

			emailDialog();

		}

		if (updateid > 0) {
			System.out.println("========long=id==========" + updateid);
			task = taskDataBase.getTask(updateid); // Getting the task from the
													// database

			final_subject = task.getTitle();
			final_where = task.getLocation();
			final_with = task.getTo_mail();
			importance = task.getImportance();
			final_compose = task.getCompose();

			final_select_am_pm = task.getam_pm();
			System.out.println("========long=id==========" + updateid);

			System.out.println("=========title==========" + final_subject);
			System.out.println("==========location===========" + final_where);
			System.out.println("==========with===========" + final_with);

			System.out.println("==========importance===========" + importance);
			set_importance();
			convert_get_time1();

			if (final_where.length() > 0) {
				email_where_TextView.setText(final_where);
				atvPlaces.setText(final_where);
				email_where_TextView.setTextColor(getResources().getColor(
						R.color.Green));
			} else {
				// email_where_TextView.setText("where");
			}
			if (final_with.length() > 0) {

				mContactListTextview.setText(final_with);
			} else {
				mContactListTextview.setHint("Enter email address");
				// event_with_TextView.setText("with");
			}

			if (final_subject.length() > 0) {
				subjectEditText.setText(final_subject);
				email_subject_TextView.setText(final_subject);
				email_subject_TextView.setTextColor(getResources().getColor(
						R.color.Green));
			} else {
				// email_subject_TextView.setText("subject");
			}

			if (final_compose.length() > 0) {
				composeEditText.setText(final_compose);
				email_compose_TextView.setText(final_compose);
				email_compose_TextView.setTextColor(getResources().getColor(
						R.color.Green));
			} else {
				// email_compose_TextView.setText("compose");
			}

			// email_subject_TextView.setText(final_subject);
			// email_compose_TextView.setText(final_compose);
			// email_where_TextView.setText(final_where);
			// mContactListTextview.setText(final_with);
			// subjectEditText.setText(final_subject);
			// composeEditText.setText(final_compose);
			// atvPlaces.setText(final_where);

			// set Update
			// /
			// setUpdate(updateid,title,location,with,audio,photo,importance,tag);

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
		email_when_TextView
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

		email_when_TextView.setText("" + months_str[num] + " " + final_date
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
	 * email_when_TextView
	 * .setTextColor(getResources().getColor(R.color.Green));
	 * 
	 * event_when_TextView.setText("" + months_str[num] + " " + final_date + ","
	 * + final_year + " " + remindertime);
	 * 
	 * 
	 * email_when_TextView .setText("" + months_str[num] + " " + final_date1 +
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

	// end
	private void emailDialog() {
		// TODO Auto-generated method stub
		Button set_Button, editbutton, snooze_Button;

		email_dialog = new Dialog(EmailActivity.this);
		/*
		 * alarm_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		email_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		email_dialog.setContentView(R.layout.send_email_dialog);

		set_Button = (Button) email_dialog.findViewById(R.id.send_button);
		snooze_Button = (Button) email_dialog.findViewById(R.id.snooze_button);
		editbutton = (Button) email_dialog.findViewById(R.id.edit_button);
		editbutton.setText("Cancel");
		snooze_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alarm_dialog();
				toggle_hour_minute();
				email_dialog.dismiss();
			}
		});
		set_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent emailIntent = new Intent(Intent.ACTION_SEND);

				// emailIntent.setData(Uri.parse("mailto:"));
				emailIntent.setType("text/plain");
				// emailIntent.putExtra(Intent.EXTRA_EMAIL, final_with);
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { final_with });
				// emailIntent.putExtra(Intent.EXTRA_CC, CC);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, final_subject);
				emailIntent.putExtra(Intent.EXTRA_TEXT, final_compose);
				emailIntent.setType("message/rfc822");
				try {
					startActivity(Intent.createChooser(emailIntent,
							"Send mail..."));
					// cancel alarm
					SetReminder reminder = new SetReminder();
					reminder.cancelReminder(context, updateid);
					// cancel alarm
					finish();
					Log.i("Finished sending email...", "");
				} catch (ActivityNotFoundException ex) {
					Toast.makeText(EmailActivity.this,
							"There is no email client installed.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		editbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// every_day_text.clearComposingText();
				//email_dialog.dismiss();
				setReminder.canceldailyReminder(context, 5);
				email_dialog.dismiss();
				finish();
			}
		});

		email_dialog.show();

	}

	private void updateinfo() {

		final_subject = subjectEditText.getText().toString();
		final_with = mContactListTextview.getText().toString();
		final_compose = composeEditText.getText().toString();
		final_where = atvPlaces.getText().toString();

		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		String email1 = loginPreferences.getString("username", "");

		// /importance=importance2;
		// encoded_audio_string=audio;
		// encodedImage=photo;
		System.out.println("========update long=id==========" + updateid);

		System.out.println("=========update title==========" + final_subject);
		System.out
				.println("==========update location===========" + final_where);
		System.out.println("==========update with===========" + final_with);

		System.out
				.println("==========updateimportance===========" + importance);

		if (final_with.trim().length() <= 0) {
			Toast.makeText(getApplicationContext(), "please enter email", Toast.LENGTH_SHORT)
					.show();
		}

		else if (final_subject.trim().length() <= 0) {
			Toast.makeText(getApplicationContext(), "please enter subject", Toast.LENGTH_SHORT)
					.show();

		}

		else {
			if (isEmailValid(final_with)) {

				// TODO Auto-generated method stub
				Task newTask = new Task(final_subject);

				newTask.setId(updateid);
				newTask.setCompose(final_compose);
				newTask.setLocation(final_where);
				newTask.setImportance(importance);
				newTask.setTo_mail(final_with);
				newTask.setUserId(email1);
				newTask.setSearch_date(final_date_long);

				newTask.setam_pm(final_select_am_pm);
				Calendar cal = Calendar.getInstance();

				cal.set(Integer.parseInt(final_year),
						Integer.parseInt(final_month) - 1,
						Integer.parseInt(final_date),
						Integer.parseInt(final_hour),
						Integer.parseInt(final_minute), 0); // Setting a new
															// Calendar instance
															// with the user
															// reminder date

				Time reminder = new Time(); // Reminder in Time format to store
											// in
											// the object
				reminder.set(0, Integer.parseInt(final_minute),
						Integer.parseInt(final_hour),
						Integer.parseInt(final_date),
						Integer.parseInt(final_month) - 1,
						Integer.parseInt(final_year)); // Setting the reminder
														// to be
														// saved in the Task
														// object
				newTask.setReminder(reminder);
				newTask.setHasReminder(1); // Setting the boolean flag to
											// indicate
											// that that the task has reminder



                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(final_year), Integer.parseInt(final_month) - 1, Integer.parseInt(final_date),
                        Integer.parseInt(final_hour), Integer.parseInt(final_minute));
				
				 //==================================================
				//Update Google Calendar Event
		       //=======================================================
				if(calbool==1) {
                    Uri local_uri = Uri.parse("content://com.android.calendar/events");
                    Cursor cursor = getContentResolver().query(local_uri, new String[]{"MAX(_id) as max_id"}, null, null, "_id");
                    cursor.moveToFirst();
                    long max_val = cursor.getLong(cursor.getColumnIndex("max_id"));
                    System.out.println("=========max_val===============" + max_val);


                    // UpdateCalendarEntry( max_val);

                    ContentValues event = new ContentValues();

                    event.put("title", final_subject);
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
				// setReminder.setOneTimeReminder(context, cal, newTask);
				
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
						//Toast.makeText(getApplicationContext(), "set snooze update", 300).show();
						System.out.println("=========Set snooze==="+cal);
					}

				if (set_time == true) {
					setReminder.setRepeatTimeReminder(context, cal, newTask,
							interval);
				} else {
					
					if(setSnooze==true){
						
					}else{
					setReminder.setOneTimeReminder(context, cal, newTask);
					//Toast.makeText(getApplicationContext(), "one time", 300).show();
					}
					
				}
				setReminder.setProximityAlert(context, newTask, latitude,
						longitude);

				array.updateTask(newTask, newTask.getId());
				this.finish();
			} else {
				Toast.makeText(getApplicationContext(),
						"please enter valid email id", Toast.LENGTH_SHORT).show();
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

	public void find_id() {

		/* Bottom Section find id */
		mMapButton = (ImageView) findViewById(R.id.map);
		mMapButton.setOnClickListener(this);
		priority_ImageView = (ImageView) findViewById(R.id.priority_img);
		alarm_ImageView = (ImageView) findViewById(R.id.alarm_img);

		priority_ImageView.setOnClickListener(this);
		alarm_ImageView.setOnClickListener(this);
		/* Bottom Section */

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
		autosend_TextView = (TextView) findViewById(R.id.auto_text);

		remind_me_TextView = (TextView) findViewById(R.id.remind_text);

		am_TextView.setOnClickListener(this);
		pm_TextView.setOnClickListener(this);
		autosend_TextView.setOnClickListener(this);

		remind_me_TextView.setOnClickListener(this);

		hour_TextView = (EditText) findViewById(R.id.event_hour_text);
		minute_TextView = (EditText) findViewById(R.id.event_minute_text);
		email_when_TextView = (TextView) findViewById(R.id.email_when_text);
		when_calender_view = findViewById(R.id.complete_calender_layout);
		whenLinearLayout = (LinearLayout) findViewById(R.id.when_layout);
		whenLinearLayout.setOnClickListener(this);
		when_ImageView = (ImageView) findViewById(R.id.when_img);

		/*------------------- Subject layout - Start -------------*/
		email_subject_TextView = (TextView) findViewById(R.id.email_subject_text);
		subjectLinearLayout = (LinearLayout) findViewById(R.id.subject_layout);
		inner_subjectLayout = (LinearLayout) findViewById(R.id.inner_subject_layout);
		subject_plusImageView = (ImageView) findViewById(R.id.subject_plus_img);
		subjectEditText = (EditText) findViewById(R.id.subject_editText);
		subjectLinearLayout.setOnClickListener(this);
		/*------------------- Subject layout -End-------------*/
		/*------------------- Compose layout - Start -------------*/
		email_compose_TextView = (TextView) findViewById(R.id.email_compose_text);
		composeLinearLayout = (LinearLayout) findViewById(R.id.compose_layout);
		inner_composeLayout = (LinearLayout) findViewById(R.id.inner_compose_layout);
		compose_plusImageView = (ImageView) findViewById(R.id.compose_plus_img);
		composeEditText = (EditText) findViewById(R.id.compose_editText);
		composeLinearLayout.setOnClickListener(this);
		/*------------------- Compose layout -End-------------*/

		/*------------------- where layout-------------*/

		email_where_TextView = (TextView) findViewById(R.id.email_where_text);
		mWhereLayout = (LinearLayout) findViewById(R.id.where_layout);
		mWhereLayout.setOnClickListener(this);

		where_view_location = findViewById(R.id.email_where);
		atvPlaces = (MultiAutoCompleteTextView) findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);

		mListview = (ListView) findViewById(R.id.mListview);
		mListview.setOnItemClickListener(this);

		where_plus_img = (ImageView) findViewById(R.id.where_plus_img);

		// event With Layout
		with_contact = findViewById(R.id.event_with);
		mContactListTextview = (EditText) findViewById(R.id.contact_auto_complete);

		mContactListview = (ListView) findViewById(R.id.mContact_Listview);
		// setListViewHeightBasedOnChildren(mContactListview );

		// Write a get Contact

		GetContact();

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
							"No Internet Connection", Toast.LENGTH_LONG).show();

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

	public void toggle_repeat() {

		// result : 0={auto send} 1=remind me

		String repeat[] = { "{auto send}", "{remind me}" };
		switch (event_repeat_value) {
		case 0:
			autosend_TextView.setTextColor(getResources().getColor(
					R.color.DarkGreen));
			remind_me_TextView.setTextColor(getResources().getColor(
					R.color.Gray));

			// Toast.makeText(getApplicationContext(), "" + repeat[0],
			// 300).show();
			final_repeat = repeat[0];
			break;
		case 1:
			remind_me_TextView.setTextColor(getResources().getColor(
					R.color.DarkGreen));
			autosend_TextView.setTextColor(getResources()
					.getColor(R.color.Gray));

			// Toast.makeText(getApplicationContext(), "" + repeat[1],
			// 300).show();
			final_repeat = repeat[1];
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

		case R.id.when_layout:
			when_plus_minus();
			//imm.hideSoftInputFromWindow(composeEditText.getWindowToken(), 0);
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
		case R.id.auto_text:
			event_repeat_value = 0;
			toggle_repeat();
			break;
		case R.id.remind_text:
			event_repeat_value = 1;
			toggle_repeat();
			break;

		case R.id.where_layout:

			where_plus_minus();
			//imm.hideSoftInputFromWindow(composeEditText.getWindowToken(), 0);
			break;

		case R.id.subject_layout:

			subject_plus_minus();
			//imm.hideSoftInputFromWindow(composeEditText.getWindowToken(), 0);
			break;
		case R.id.compose_layout:

			compose_plus_minus();
			//imm.hideSoftInputFromWindow(composeEditText.getWindowToken(), 0);
			break;
		case R.id.priority_img:
			importance_dialog();
			break;
		case R.id.alarm_img:
			alarm_dialog();
			toggle_hour_minute();
			break;
		case R.id.save_button1:

			break;

		case R.id.share_notes:
			share_note();
			// whole_note_data();
			break;
		case R.id.event_save_text:

            final_subject = subjectEditText.getText().toString();
            final_with = mContactListTextview.getText().toString();
            if (final_with.trim().length() <= 0) {
                Toast.makeText(getApplicationContext(), "please enter email", Toast.LENGTH_SHORT)
                        .show();
            }

            else if (final_subject.trim().length() <= 0) {
                Toast.makeText(getApplicationContext(), "please enter subject", Toast.LENGTH_SHORT)
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

            }


          /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    EmailActivity.this);

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
            alertDialog.show();*/





			break;
		case R.id.event_complete_text:
			if (updateid > 0) {
				// Toast.makeText(getApplicationContext(), "update",
				// 300).show();
				/* updateinfo(); */
				taskDataBase.update_complete(updateid);
				SetReminder reminder = new SetReminder();
				reminder.cancelReminder(context, updateid);
				finish();
			}
			break;
		case R.id.map:
			Intent in = new Intent(this, CreateGoogleMap.class);
			startActivityForResult(in, 1);

			break;
		default:
			break;
		}
	}

	public void save_snooze() {
		try {

			// save

			String minute = minute_TextView.getText().toString();

			String hour = hour_TextView.getText().toString();
			if (Integer.parseInt(hour) <= 12 && Integer.parseInt(minute) < 60) {

				convert_date_into_long();
				convert_time_into_long();
				get_notcheck();
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

			close_all();

			if (updateid > 0) {
				// Toast.makeText(getApplicationContext(), "update",
				// 300).show();
				updateinfo();
			} else {
				// Toast.makeText(getApplicationContext(), "save", 300).show();
				event_info();
			}
		} else if (date1.compareTo(date2) < 0) {
			System.out.println("Date1 is before Date2");
			Toast.makeText(getApplicationContext(), "Please select right date",
                    Toast.LENGTH_LONG).show();
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
		// Toast.makeText(getApplicationContext(), "time" + time, 300).show();
		Date EndTime = dateFormat.parse(time);

		Date CurrentTime = dateFormat.parse(dateFormat.format(new Date()));

		if (CurrentTime.after(EndTime)) {
			System.out.println("timeeee end ");
			Toast.makeText(getApplicationContext(), "Please select right time",
                    Toast.LENGTH_SHORT).show();

		} else {

			close_all();
			if (updateid > 0) {// Toast.makeText(getApplicationContext(),
								// "updatetime", 300).show();
				updateinfo();
			} else {// Toast.makeText(getApplicationContext(), " save time",
					// 300).show();
				event_info();
			}

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
				/*
				 * gridcell.setTextColor(getResources().getColor(
				 * R.color.white));
				 */
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
			final_date_month_year = date_month_year;
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

	public Bitmap getHexagonShape(Bitmap scaleBitmapImage) {
		// TODO Auto-generated method stub
		int targetWidth = 110;
		int targetHeight = 110;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);

		Path path = new Path();
		float stdW = 110;
		float stdH = 110;
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

	/* ---------with coding functionality----- */
	// Retrieve get Contact
	private void GetContact() {

		String[] PROJECTION = new String[] {
				Phone.CONTACT_ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				Phone.NUMBER };

		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(
				Phone.CONTENT_URI, PROJECTION,
				null, null, null);
		if (cursor != null) {
			try {
				final int nameIndex = cursor
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
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

		final ContactAdapter chipsAdapter = new ContactAdapter(this,
				contactList);
		// comment for multiautocomplete textview
		/*
		 * mContactListTextview.setAdapter(chipsAdapter);
		 * mContactListTextview.setThreshold(1);
		 * 
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
				// 300).show();

			}
		});
		mContactListTextview.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				// EventActivity.this.chipsAdapter.getFilter().filter(s);

				// /String text =
				// mContactListTextview.getText().toString().toLowerCase(Locale.getDefault());
				chipsAdapter.getFilter().filter(s);
				if (mContactListTextview.getText().toString().length() <= 0) {
					mContactListview.setVisibility(View.GONE);
				} else {
					mContactListview.setVisibility(View.VISIBLE);

				}

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
				// TODO Auto-generated method stub

			}
		});

	}

	private void when_plus_minus() {
		if (when_on == false) {
			when_ImageView.setBackgroundResource(R.drawable.minus);
			when_calender_view.setVisibility(View.VISIBLE);
			when_on = true;

			where_on = true;
			subject_on = true;
			compose_on = true;
			where_plus_minus();
			subject_plus_minus();
			compose_plus_minus();

			email_when_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			email_when_TextView.setText("when");
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

	/* -------------------where Data invoke on click with ----------- */
	private void where_plus_minus() {
		// TODO Auto-generated method stub
		if (where_on == false) {
			where_plus_img.setBackgroundResource(R.drawable.minus);
			where_view_location.setVisibility(View.VISIBLE);
			where_on = true;

			when_on = true;
			subject_on = true;
			compose_on = true;
			when_plus_minus();
			subject_plus_minus();
			compose_plus_minus();
			email_where_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			email_where_TextView.setText("where");
		} else {

			where_plus_img.setBackgroundResource(R.drawable.plus);
			where_view_location.setVisibility(View.GONE);
			where_on = false;
			set_where_text();
		}
	}

	/* -------------------Subject Data invoke on click subject----------- */
	private void subject_plus_minus() {
		// TODO Auto-generated method stub
		if (subject_on == false) {
			subject_plusImageView.setBackgroundResource(R.drawable.minus);
			inner_subjectLayout.setVisibility(View.VISIBLE);
			subject_on = true;

			where_on = true;
			when_on = true;
			compose_on = true;
			where_plus_minus();
			when_plus_minus();
			compose_plus_minus();
			email_subject_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			email_subject_TextView.setText("subject");
		} else {

			subject_plusImageView.setBackgroundResource(R.drawable.plus);
			inner_subjectLayout.setVisibility(View.GONE);
			subject_on = false;
			set_subject_text();
		}
	}

	/* -------------------Compose Data invoke on click subject----------- */
	private void compose_plus_minus() {
		// TODO Auto-generated method stub
		if (compose_on == false) {
			compose_plusImageView.setBackgroundResource(R.drawable.minus);
			inner_composeLayout.setVisibility(View.VISIBLE);
			compose_on = true;

			where_on = true;
			when_on = true;
			subject_on = true;
			where_plus_minus();
			when_plus_minus();
			subject_plus_minus();
			email_compose_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			email_compose_TextView.setText("compose");

		} else {

			compose_plusImageView.setBackgroundResource(R.drawable.plus);
			inner_composeLayout.setVisibility(View.GONE);
			compose_on = false;
			set_compose_text();
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

	/****
	 * Method for Setting the Height of the ListView dynamically. Hack to fix
	 * the issue of not showing all the items of the ListView when placed inside
	 * a ScrollView
	 ****/
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new LayoutParams(desiredWidth,
						LayoutParams.MATCH_PARENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public void convert_date_into_long() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
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

	private boolean isEmailValid(String mail) {
		// TODO Auto-generated method stub
		Pattern pattern;
		Matcher matcher;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(mail);
		return matcher.matches();
	}

	public void event_info() {
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		String email1 = loginPreferences.getString("username", "");
		final_subject = subjectEditText.getText().toString();
		final_with = mContactListTextview.getText().toString();
		if (final_with.trim().length() <= 0) {
			Toast.makeText(getApplicationContext(), "please enter email", Toast.LENGTH_SHORT)
					.show();
		}

		else if (final_subject.trim().length() <= 0) {
			Toast.makeText(getApplicationContext(), "please enter subject", Toast.LENGTH_SHORT)
					.show();

		}

		else {
			if (isEmailValid(final_with)) {

				final_compose = composeEditText.getText().toString();
				final_where = atvPlaces.getText().toString();
				Log.d("with=", "" + final_with);

				Log.d("where=", "" + final_where);

				Log.d("final time", "" + final_time);
				Log.d("am_pm=", "" + final_select_am_pm);
				Log.d("repeat =", "" + final_repeat);

				Log.d("final_start", "" + final_date_month_year);
				// Log.d("final_end", "" + final_end);

				long rowID;

				TaskListArray array = TaskListArray.getInstance(context);
				// mail id set Subject
				Task newTask = new Task(final_subject);
				// mail person mail id
				newTask.setTo_mail(mContactListTextview.getText().toString());
				// mail set compose
				newTask.setCompose(final_compose);
				newTask.setLocation(final_where);
				newTask.setImportance(importance);
				newTask.setUserId(email1);
				newTask.setSearch_date(final_date_long);
				newTask.setam_pm(final_select_am_pm);
				// newTask.setEvent_tag(tag_ArrayList.get(0));
				// newTask.setEvent_Audio(encoded_audio_string);
				// newTask.setEvent_photo(encodedImage);

				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(final_year),
						Integer.parseInt(final_month) - 1,
						Integer.parseInt(final_date),
						Integer.parseInt(final_hour),
						Integer.parseInt(final_minute), 0); // Setting a new
															// Calendar instance
															// with the user
															// reminder date

				Time reminder = new Time(); // Reminder in Time format to store
											// in the object
				reminder.set(0, Integer.parseInt(final_minute),
						Integer.parseInt(final_hour),
						Integer.parseInt(final_date),
						Integer.parseInt(final_month) - 1,
						Integer.parseInt(final_year)); // Setting the reminder
														// to be saved in the
														// Task object
				newTask.setReminder(reminder);
				newTask.setHasReminder(1); // Setting the boolean flag to
											// indicate that that the task has
											// reminder

				rowID = taskDataBase.addTask(newTask); // Adding to DataBase
				newTask.setId(rowID);
				array.addTask(newTask);


                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(final_year), Integer.parseInt(final_month) - 1, Integer.parseInt(final_date),
                        Integer.parseInt(final_hour), Integer.parseInt(final_minute));
				 //=====================================================
				// add Event in Google Calendar
	           //=======================================================
				if(calbool==1) {
                    final ContentValues event = new ContentValues();
                    event.put(Events.CALENDAR_ID, 1);

                    event.put(Events.TITLE, final_subject);
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
				// Creating a new reminder to be added to the alarm manager
				if (set_time == true) {
					setReminder.setRepeatTimeReminder(context, cal, newTask,
							interval);
				} else {
					setReminder.setOneTimeReminder(context, cal, newTask);
				}
				setReminder.setProximityAlert(context, newTask, latitude,
						longitude);
				finish();
				/*
				 * Toast.makeText(getApplicationContext(), "with="+final_with,
				 * 300).show(); Toast.makeText(getApplicationContext(),
				 * "where="+final_where, 300).show();
				 * Toast.makeText(getApplicationContext(), "time="+final_time,
				 * 300).show(); Toast.makeText(getApplicationContext(),
				 * "repeat="+final_with, 300).show();
				 * Toast.makeText(getApplicationContext(), "subject="+
				 * final_subject, 300).show();
				 * Toast.makeText(getApplicationContext(),
				 * "compose="+final_compose, 300).show();
				 * Toast.makeText(getApplicationContext(), "repeat="+final_with,
				 * 300).show(); Toast.makeText(getApplicationContext(),
				 * "------"+final_date+"-"+final_month+"-"+final_year,
				 * 300).show();
				 * 
				 * Toast.makeText(getApplicationContext(),
				 * "------"+final_hour+"-"+final_minute, 300).show();
				 */
			} else {
				Toast.makeText(getApplicationContext(),
						"please enter valid email id", 300).show();
			}
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
		email_when_TextView
				.setTextColor(getResources().getColor(R.color.Green));

		email_when_TextView
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
		email_where_TextView.setTextColor(getResources()
				.getColor(R.color.Green));
		if (atvPlaces.getText().toString().length() == 0) {
			email_where_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			email_where_TextView.setText("where");
		} else {
			email_where_TextView.setText("" + atvPlaces.getText().toString());
		}
	}

	public void set_compose_text() {

		email_compose_TextView.setTextColor(getResources().getColor(
				R.color.Green));
		if (composeEditText.getText().toString().length() == 0) {
			email_compose_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			email_compose_TextView.setText("compose");
		} else {
			email_compose_TextView.setText(""
					+ composeEditText.getText().toString());
		}
	}

	public void set_subject_text() {

		email_subject_TextView.setTextColor(getResources().getColor(
				R.color.Green));
		if (subjectEditText.getText().toString().length() == 0) {
			email_subject_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			email_subject_TextView.setText("subject");
		} else {
			email_subject_TextView.setText(""
					+ subjectEditText.getText().toString());
		}
	}

	public void close_all() {
		when_on = true;

		where_on = true;
		subject_on = true;
		compose_on = true;
		// when_plus_minus();
		where_plus_minus();
		subject_plus_minus();
		compose_plus_minus();

	}

	public void importance_dialog() {
		LinearLayout lowLinearLayout, highLinearLayout, mediumLinearLayout;
		importance_dialog = new Dialog(EmailActivity.this);
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

	public void alarm_dialog() {

		RelativeLayout relativeLayout;
		ImageView upImageView, downImageView;

		Button set_alarm_Button;
		alarm_dialog = new Dialog(EmailActivity.this);
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
				 * alarm_final_value, 300).show();
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
							//set_time = true;
							setSnooze = true;
							alarm_dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(),
									"please enter right minute", Toast.LENGTH_SHORT).show();
						}
					} else {
						// hour
						if (alarm_final_value <= 12) {
							interval = 1000 * 60 * 60 * alarm_final_value;
							//set_time = true;
							setSnooze = true;
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

				if (dialogid == 5) {

					save_snooze();
					finish();

				}
				/*Toast.makeText(getApplicationContext(),
						"value=" + alarm_final_value, 300).show();*/

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

	public void get_notcheck() throws ParseException {
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

		close_all();

		if (updateid > 0) {
			// Toast.makeText(getApplicationContext(), "update", 300).show();
			updateinfo();
		} else {
			// Toast.makeText(getApplicationContext(), "save", 300).show();
			event_info();
		}

	}

	public void share_note() {
		whole_note_data();
		Intent shareintent = new Intent(Intent.ACTION_SEND);
		shareintent.putExtra(Intent.EXTRA_SUBJECT,
				final_subject);
		shareintent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { final_with });
		shareintent.putExtra(Intent.EXTRA_TEXT, str.toString());
		// shareintent.setType("*/*");
		shareintent.setType("text/plain");

		/*
		 * shareintent.putExtra(android.content.Intent.EXTRA_SUBJECT,
		 * "share subject");
		 * shareintent.putExtra(android.content.Intent.EXTRA_TEXT, "shareBody");
		 */

		if (shareintent.resolveActivity(getPackageManager()) != null) {
			startActivity(Intent.createChooser(shareintent, "Share event"));
		}

	}

	public void whole_note_data() {

		final_compose = composeEditText.getText().toString();
		final_where = atvPlaces.getText().toString();
		final_subject = subjectEditText.getText().toString();
		final_with = mContactListTextview.getText().toString();

		String[] months_str = { "", "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };
		int num = Integer.parseInt(final_month);
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

		str = new StringBuilder("\n");

		str.append("When : " + final_when + "\n");
		str.append("Where : " + final_where + "\n");
		str.append("With : " + final_with + "\n");
		str.append("Text : " + final_compose + "\n");
		// Toast.makeText(getApplicationContext(), ""+str, 300).show();
		System.out.println("" + str);

	}

}
