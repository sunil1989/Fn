package search;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.life_reminder.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import data.FetchData;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;

public class SearchActivity extends Activity implements OnClickListener {
	 String arr [];
	 Dialog tag_dialog;
	// CharSequence[] items;
	Dialog importance_dialog;
	ScrollView searchScrollView;
	EditText search_dateEditText,currentEditText;
	Button search_btn;
	TextView event_saveTextView;
	boolean date_on;
	int current_date;
	View date_include_view;
    String 	final_time;
	TextView startTextView, endTextView;
	String final_start, final_end;
	boolean start_bool, end;
	String final_date, final_month, final_year, final_date_month_year,importance;
	private static final String tag = "MyCalendarActivity";
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	private TextView currentMonth;
	
	EditText mOldStuff;
	@SuppressLint("NewApi")
	private int month, year;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	
	private ListView mOldStufflistview;
	private TaskListDataBase taskDataBase;
	private Task task;
	private TaskListArray tasksList;
	Context context = this;
	public static List<Task> currentTask;
	
	static ArrayList<FetchData> filterdata;
	
	static ArrayList<Task> tag_list;
	ArrayList<Task> taskData;
	String	today_date;
	//search by name
	 ArrayList<String>mList;
	
	EditText mName,mLocation,mDate,mWhenName,mToday,mTommarrow,with;
	
	TextView slice,mIportance;
	
	 private SharedPreferences loginPreferences;
	 private SharedPreferences.Editor loginPrefsEditor;
	 String email1;
	    
	 int currentmonth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search);
		taskDataBase = TaskListDataBase.getInstance(context);
		find_id();
		
		 
		custom_actionbar();
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
		
		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
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
					final_end = date_month_year;
					// Toast.makeText(context, "end"+final_end, 300).show();
				} else {
					final_date_month_year = date_month_year;
					// Toast.makeText(context, "final"+final_date_month_year,
					// 300).show();
				}
				
				// show selected date
				// Toast.makeText(getApplicationContext(), "" + date_month_year,
				// 300).show();
				try {
					convert_time_into_long();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					Log.d("final_start", "" + final_date_month_year);
					Log.d("final_end", "" + final_end);
					date_include_view.setVisibility(View.GONE);
					search_dateEditText.setVisibility(View.VISIBLE);
					set_when_text();
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
		action_bar_title.setText("Search Result");
		action_bar_img.setImageResource(R.drawable.home_search_icon);
		action_bar_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
		//	Toast.makeText(getApplicationContext(), "text", 300).show();
			}
		});

	}
	public void find_id() {
		TextView	start_textview=(TextView)findViewById(R.id.start_textview);
		TextView	end_textview=(TextView)findViewById(R.id.end_textview);
		start_textview.setVisibility(View.INVISIBLE);
		end_textview.setVisibility(View.INVISIBLE);
		
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		email1 = loginPreferences.getString("username", "");
	
		currentEditText=(EditText)findViewById(R.id.current_editText);
		search_btn=(Button)findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
		currentEditText.setOnClickListener(this);
		searchScrollView=(ScrollView)findViewById(R.id.search_scroll);
		searchScrollView.pageScroll(ScrollView.FOCUS_UP);
		event_saveTextView = (TextView) findViewById(R.id.event_save_text);
		event_saveTextView.setOnClickListener(this);
		startTextView = (TextView) findViewById(R.id.start_textview);
		endTextView = (TextView) findViewById(R.id.end_textview);
		startTextView.setOnClickListener(this);
		endTextView.setOnClickListener(this);

		search_dateEditText = (EditText) findViewById(R.id.mset);
		search_dateEditText.setOnClickListener(this);
		date_include_view = findViewById(R.id.homecal_include_layout);


		mOldStuff=(EditText)findViewById(R.id.old_stuff);
		mOldStuff.setOnClickListener(this);

		 filterdata=new ArrayList<FetchData>();
       // Search  EditText
	
		mName=(EditText)findViewById(R.id.name);

	  mLocation=(EditText)findViewById(R.id.location_search);

	  mIportance=(TextView)findViewById(R.id.importence);
	  mIportance.setOnClickListener(this);
	  mWhenName=(EditText)findViewById(R.id.when_name);
	 slice=(TextView)findViewById(R.id.slice_name);
slice.setOnClickListener(this);
	 mList=new ArrayList<String>();
	 get_tag();
	//String arr1[]={"a","a1","a2","a3","b1","b2","b3"};
/*	 ArrayAdapter<String> tag_adapter=new ArrayAdapter<>(getApplicationContext(), R.layout.text,R.id.tag_multitextView,mList );
	 slice.setAdapter(tag_adapter);
	 slice.setThreshold(1);*/
	// slice.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

	  with=(EditText)findViewById(R.id.with_name);
	  
	  

	  mToday=(EditText)findViewById(R.id.today_fetch);
	  mToday.setOnClickListener(this);
	  
	  mTommarrow=(EditText)findViewById(R.id.tomorrow_fetch);
	  mTommarrow.setOnClickListener(this);
	  
		

	}

	public void get_tag()
	{
		
		
		 taskData=new ArrayList<Task>();
		 tag_list=new ArrayList<Task>();
		taskData = taskDataBase.getAllTasks(email1);
		 for(Task d : taskData){
			 tag_list.add(new Task(d.getEvent_tag()));
			if(d.getEvent_tag()!=null)
			{
			 mList.add(d.getEvent_tag());
			 
			 System.out.println("list"+mList);
			
			 
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
		search_dateEditText
				.setTextColor(getResources().getColor(R.color.Green));
		
		/*search_dateEditText.setText("" + months_str[num] + " " + final_date +
		  ", " + final_year);*/
		 
if(final_end==null)
{
	/*	search_dateEditText
				.setText(""
						+ final_date
						+ "/"
						+ final_month
						+ "/"
						+ final_year 
						);*/
		
		
		search_dateEditText.setText("" + months_str[num] + " " + final_date +
				  ", " + final_year);
}else{
	
	ArrayList<String> arrayList1 = new ArrayList<String>();
	String str1 = final_end;
	String[] tokens1 = str1.split("-");
	for (String s : tokens) {
		arrayList1.add(s);
	}
String	final_end_date = arrayList.get(0);
String		final_end_month = arrayList.get(1);
String		final_end_year = arrayList.get(2);

		search_dateEditText
		.setText(""
				+ final_month
				+ "/"
				+ final_date
				+ "/"
				+ final_year +" to "+final_end_month+"/"+final_end_date+"/"+final_end_year
				);
}

	}

	public void date_plus_minus() {
		if (date_on == false) {

			date_include_view.setVisibility(View.VISIBLE);

			date_on = true;
			search_dateEditText.setVisibility(View.GONE);
			search_dateEditText.setTextColor(getResources().getColor(
					R.color.Green));

		} else {
			search_dateEditText.setVisibility(View.VISIBLE);
			date_include_view.setVisibility(View.GONE);
			date_on = false;
			search_dateEditText.setTextColor(getResources().getColor(
					R.color.Gray));
			

		}
	}
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

		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mset:
			
			date_plus_minus();
			break;
		case R.id.start_textview:
			start_bool = false;
			start_end_click();
			break;
		case R.id.end_textview:
			start_bool = true;
			start_end_click();
			break;
		case	R.id.slice_name:
			tag_list_Dialog();
		//custom_tag_dialog();
			break;
		case R.id.event_save_text:
		
			try {
				convert_time_into_long();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				Log.d("final_start", "" + final_date_month_year);
				Log.d("final_end", "" + final_end);
				date_include_view.setVisibility(View.GONE);
				search_dateEditText.setVisibility(View.VISIBLE);
				set_when_text();
				
				
		

			break;
		case R.id.current_editText:
			
			SearchCurrent();
			Intent intent=new Intent(getBaseContext(), CurrentActivity.class);
			startActivity(intent);
			break;
	case R.id.search_btn:
			
			SearchCurrent();
			Intent intent4=new Intent(getBaseContext(), CurrentActivity.class);
			startActivity(intent4);
			
			slice.setText("");
			mIportance.setText("");
			break;
		case R.id.old_stuff:
			Intent intent1=new Intent(getBaseContext(), OldStuffActivity.class);
			startActivity(intent1);
			break;
			
		case R.id.today_fetch:
			Intent in=new Intent(getBaseContext(), CurrentActivity.class);
			startActivity(in);
			
			  String currentdate=currentmonth+"-"+month+"-"+year;
			  
				try {
				today_date =convert_date_into_long(currentdate);
			
			System.out.println("===========today_date========"+today_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  System.out.println("Hello"+currentdate);
			  
			  SearchCurrent();
			break;
			
		case R.id.tomorrow_fetch:
			Intent in1=new Intent(getBaseContext(), CurrentActivity.class);
			startActivity(in1);
			SearchCurrent();
			break;
			
		case R.id.importence:
			
			importance_dialog();
			break;
		default:
			break;
		}
		
	}
	
	
	public void importance_dialog() {
		LinearLayout lowLinearLayout, highLinearLayout, mediumLinearLayout;
		importance_dialog = new Dialog(SearchActivity.this);
		/*
		 * alarm_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		importance_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		importance_dialog.setContentView(R.layout.search_importance_dialog);
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
				mIportance.setText("Low");
			//	priority_ImageView.setImageResource(R.drawable.importance_one);
			}
		});
		mediumLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				importance_dialog.dismiss();
				importance = "Medium";
				mIportance.setText("Medium");
				//priority_ImageView.setImageResource(R.drawable.importance_two);
			}
		});
		highLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				importance_dialog.dismiss();
				importance = "High";
				mIportance.setText("High");
			//	priority_ImageView.setImageResource(R.drawable.importance_three);
			}
		});
		importance_dialog.show();
	}
	private String convert_date_into_long(String currentdate )throws ParseException {
		// TODO Auto-generated method stub
		
		final_time = currentdate ;
		Log.d("final_time =", "" + final_time);

		// SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
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
		System.out.println(" time final_time ="+ final_time);
	//	Log.d(" time final_time =", "" + final_time);
		return final_time;
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
	public void convert_time_into_long() throws ParseException {

		

		final_time = final_date_month_year ;
		Log.d("final_time =", "" + final_time);

		// SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
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
		
		Log.d(" time final_time =", "" + final_time);
        
		// event info

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
				
				 currentmonth=getCurrentDayOfMonth();

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
					if (i == getCurrentDayOfMonth()) {
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
					// set current date Background color

				/*	gridcell.setBackgroundColor((getResources()
							.getColor(R.color.Green)));*/
					// set current date Text color
					// gridcell.setTextColor(getResources().getColor(R.color.white));
					gridcell.setTextColor(getResources().getColor(
							R.color.darkorrange));
					// final_date = gridcell.getText().toString();

					// Toast.makeText(getApplicationContext(), "final=" +
					// final_date,300).show();

				}

				return row;
			}

			@Override
			public void onClick(View view) {
				String date_month_year = (String) view.getTag();
				final_date_month_year = date_month_year;

				// show selected date
				// Toast.makeText(getApplicationContext(), "" + date_month_year,
				// 300) .show();

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

		private void SearchCurrent() 
		{
			 String name=mName.getText().toString();
	           
	           String location=mLocation.getText().toString();
	          
	           String sDate=search_dateEditText.getText().toString();
	           
	           String importance=mIportance.getText().toString();
	           
	           String whenit=mWhenName.getText().toString();
	           
	           
	           String slice_it=slice.getText().toString();
	           String with_it=with.getText().toString();
	           
	           Log.d("current date", currentmonth+"-"+month+"-"+year);
	           
	           
	           
	           String currentdate=currentmonth+"-"+month+"-"+year;
	           
	          
	           
	           
	          // SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
	          // String formattedDate2 = df2.format(c.getTime());

	           
	           
	          
	           System.out.println("=============search name========"+name);
	           System.out.println("=============location======="+location);
	           System.out.println("=============sDate======="+sDate);
	           
	           
	          
	           
	           
	           
	           Calendar c = Calendar.getInstance();
	           System.out.println("Current time => " + c.getTime());
	           
	           
	           
	           
	           
	           
	           
	           
	           
	           
	           
	        
	            Log.d("final_time =", "" + final_time);
	           
				tasksList = TaskListArray.getInstance(context); // Creating the tasks
				// list
				taskDataBase = TaskListDataBase.getInstance(context);
				
				
				currentTask = taskDataBase.getSearch(name, location,importance,final_time,today_date,slice_it,with_it);
				/*if(name!=null){
				currentTask = taskDataBase.getSearchName(name);
				
				}*/
				
				//if(name!=null){
					//currentTask = taskDataBase.getSearch(name, location,importance);
					
				//	}
				
				/*if(name!=null){
					//currentTask = taskDataBase.getSearch(name, location,importance);
					currentTask = taskDataBase.getSearchName(name);
					}
				
				if(name!=null&&location!=null){
					currentTask = taskDataBase.getSearch(name, location,importance);
				}
				*/
				
		}
/*		
	   	private void SearchCurrent() {
			// TODO Auto-generated method stub
			
           String name=mName.getText().toString().toUpperCase();
           
           String location=mLocation.getText().toString().toUpperCase();
          
           String sDate=search_dateEditText.getText().toString();
           
           String importance=mIportance.getText().toString().toUpperCase();
           
           String whenit=mWhenName.getText().toString().toUpperCase();
           
           
           String s1="5/10/2012";
           String s2="5/10/2012";
           if(s1.equals(s2)){
        	   System.out.println("=============Hello======");
           }
           
           
          
           System.out.println("=============search name========"+name);
           System.out.println("=============location======="+location);
           System.out.println("=============sDate======="+sDate);
           
			tasksList = TaskListArray.getInstance(context); // Creating the tasks
			// list
			taskDataBase = TaskListDataBase.getInstance(context);
			tasksList.setTasks(taskDataBase.getAllTasks(email1)); // Setting the tasks
															// from database
			
			
			currentTask = taskDataBase.getAllTasks(email1);
			
			 for(Task d : currentTask){
				 String date= d.getCreationDateString();
		        	System.out.println("=============Date======="+date);
				if(name != null && !name.isEmpty() &&!name.equals(null)){
				 if(d.getTitle() != null &&d.getTitle().toUpperCase().startsWith(name)){
					 
					 filterdata.add(new FetchData(d.getTitle()));
					 System.out.println("name");
				 }
				 
				}
				if(location != null && !location.isEmpty() &&!location.equals(null)){
                if(d.getLocation() != null &&d.getLocation().toUpperCase().startsWith(location)){
					 
					 System.out.println("location");
					 filterdata.add(new FetchData(d.getLocation()));
				 }
				 }
				
				if(sDate != null && !sDate.isEmpty() &&!sDate.equals(null)){
	                if(d.getCreationDateString() != null &&d.getCreationDateString().equals(sDate)){
						 
						 System.out.println("date");
						 filterdata.add(new FetchData(d.getCreationDateString()));
					 }
					 }
				
				
				if(importance != null && !importance.isEmpty() &&!importance.equals(null)){
	                if(d.getImportance() != null &&d.getImportance().toUpperCase().startsWith(importance)){
						 
						 System.out.println("improtance");
						 filterdata.add(new FetchData(d.getImportance()));
					 }
					 }
				
				if(whenit != null && !whenit.isEmpty() &&!whenit.equals(null)){
	                if(d.getEvent_with() != null &&d.getEvent_with().toUpperCase().startsWith(whenit)){
						 
						 System.out.println("improtance");
						 filterdata.add(new FetchData(d.getEvent_with()));
					 }
					 }
				
				
				
			        if((d.getTitle() != null &&d.getTitle().toUpperCase().startsWith(name))||(d.getLocation() != null &&d.getLocation().toUpperCase().startsWith(location))||(d.getCreationDateString()) != null &&d.getCreationDateString().startsWith(sDate)){
			 
			        	
			        	filterdata.add(new FetchData(d.getTitle()));
			        	
			        	String date= d.getCreationDateString();
			        	System.out.println("=============Date======="+date);
			        	//filterdata.add(new FetchData(d.getLocation()));
			        	System.out.println("=============search name=s======="+d.getTitle());
			        	System.out.println("============Location=s====="+d.getLocation());
			        }
			           //something here
			    }
			
			
		}*/
		
		public void custom_tag_dialog()
		{
			
		
			/*ArrayList to Array Conversion */
			String array[] = new String[mList.size()];              
			for(int j =0;j<mList.size();j++){
			  array[j] = mList.get(j);
			}
			
			Toast.makeText(getApplicationContext(), "size="+mList.size(), 300).show();
			Toast.makeText(getApplicationContext(), "size="+mList, 300).show();
			 tag_dialog=new Dialog(this);
			 tag_dialog.setContentView(R.layout.tag_list_dialog);
			 ListView tag_listview=(ListView)tag_dialog.findViewById(R.id.tag_listview);
			// ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.tag_list_dialog, a);
			 
			 
			 
			
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList);
		       
			 
			 tag_listview.setAdapter(adapter);
			 tag_dialog.setTitle("Select Tag");
				
			 tag_dialog.show();
			 
			
			
			
		}
		public void tag_list_Dialog()
		{
			//final CharSequence items[]={"raj","sharma"};
			 System.out.println("=====list======="+mList.size());
			
			 List<String> list = new ArrayList<String>();

			    for(String s : mList) {
			       if(s != null && s.length() > 0) {
			          list.add(s);
			       }
			    }
			 
			    
			  /*  List<String> list1 = new ArrayList<String>();
			    list1.add("");
			    list1.add("");
			    List<String> list2 = new ArrayList<String>();
			    
			    for(String s : list1) {
				       if(s != null && s.length() > 0) {
				    	   list2.add(s);
				       }
				    }*/
			    
	final CharSequence[] items = list.toArray(new CharSequence[list.size()]);
			Builder builder=new Builder(this);
			//builder.setTitle("Select Tag");
			
			builder.setTitle( Html.fromHtml("<font color='#6bc04b'>Select Tag</font>"));
			
		
			
		//	ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_list_item_1, charSequence);
			  builder.setItems(items, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // Do something with the selection
		                slice.setText(items[which]);
		            }
		        });
			   AlertDialog alert = builder.create();
		        alert.show();
		}
}
