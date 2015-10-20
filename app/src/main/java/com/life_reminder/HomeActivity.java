package com.life_reminder;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.All_Adapter;
import adapter.ExpandListAdapter;
import adapter.Group;
import adapter.TimeAdapter;
import alarm.SetReminder;
import app.AppConfig;
import data.FetchData;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;
import email.EmailActivity;
import etc.NewEtcActivity;
import event.EventActivity;
import faces.FacesActivity;
import fn.LifeActivity;
//import linkedin.FacebookLoginActivity;
import linkedin.GooglePlusLoginActivity;
import phone.PhoneActivity;
import search.CalenderHomeSearch;
import search.SearchActivity;
import settings.SettingActivity;
import sync.StartService;


public class HomeActivity extends GooglePlusLoginActivity implements OnClickListener, OnItemClickListener, ConnectionCallbacks, OnConnectionFailedListener {
	String final_time;
	
	// google plus
	
	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;
	
	ImageView settingImageView,searchImageView;
/*	Calender Vieew*/
	String final_date, final_month, final_year, final_date_month_year;
	private static final String tag = "MyCalendarActivity";
int current_date;
private Button selectedDayMonthYearButton;
private ImageView prevMonth;
private ImageView nextMonth;
private GridView calendarView;
private GridCellAdapter adapter;
private Calendar _calendar;
@SuppressLint("NewApi")
private int month, year;
@SuppressWarnings("unused")
@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })

private static final String dateTemplate = "MMMM yyyy";

/*	Calender Vieew*/

	LinearLayout todayLinearLayout, tomorrowLinearLayout, weekLinearLayout,allLinearLayout,
			dateLinearLayout;
	boolean today_on, tomorrow_on, week_on, date_on,all_on;
	ImageView today_plusImageView, tomorrow_plusImageView, week_plusImageView,
			date_plusImageView,all_plusImageView;
	private TextView currentMonth;
	TextView today_TextView, tomorrow_TextView, week_TextView, date_TextView,all_TextView;
	View today_include_view, tomorrow_include_view, week_include_view,
			date_include_view,all_include_View;

	ListView today_Listview, tomorrow_Listview, week_Listview,all_ListView;

	Dialog dialog;
	ActionBar actionBar;
	Context context = this;

	private static final String[][] data = {
			{ "!! Rita's Birthday Dinneededer ", }, { "Call Steve", },
			{ "Lorem", } };
	private static final String[][] data1 = {
			{ "!! Rita's Birthday Dinnerfgggggg ", "Pick up eggsfff", },
			{ "Call Steve", "Work on FN app" }, { "Lorem", } };
	/* private ExpandableListView expandableListView; */
	DisplayMetrics metrics;
	int width;
	ImageView img1;

	// database and Reminder
	private SetReminder setReminder;

	private TaskListDataBase taskDataBase;
	private Task task;
	private TaskListArray tasksList;

	public static ArrayList<FetchData> mydata;
	ArrayList<FetchData> mydata1;
	ArrayList<FetchData> mydata2;
	private ExpandListAdapter ExpAdapter;
	private ArrayList<Group> ExpListItems;

	ArrayList<String> list1;
	ArrayList<String> list2;

	ArrayList<String> list3;
	
	ArrayList<Task> taskData;
	
	ProgressDialog prgDialog;
	HashMap<String, String> queryValues;
	 
	 private SharedPreferences loginPreferences;
	    private SharedPreferences.Editor loginPrefsEditor;
	    String email1;
	    
	    
	    
	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newactivity_home);
		
		
		
		startService(new Intent(this, StartService.class));
		
		/*Runnable refresh;
		   refresh = new Runnable() {               
		            public void run() {


		            Toast.makeText(HomeActivity.this, "C'Mom no hands!", Toast.LENGTH_SHORT).show();


		                    handler.postDelayed(refresh, 3000);


		            }
		        };
		        handler.post(refresh);*/
		
		
		
		mGoogleApiClient=new GoogleApiClient.Builder(this)
		
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(Plus.API, PlusOptions.builder().build())
		.addScope(Plus.SCOPE_PLUS_LOGIN)
		.build();

		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		context = getBaseContext();
		find_id();
		ExpListItems = new ArrayList<Group>();
		ExpListItems.add(new Group("Today"));
		ExpListItems.add(new Group("Tommarrow"));
		ExpListItems.add(new Group("This week"));

		list1 = new ArrayList<String>();
		list2 = new ArrayList<String>();
		list3 = new ArrayList<String>();

		 mydata = new ArrayList<FetchData>();
		 mydata1=new ArrayList<FetchData>();
		 mydata2=new ArrayList<FetchData>();
		 taskData=new ArrayList<Task>();

		img1 = (ImageView) findViewById(R.id.fn_plus_img);
		img1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				show_dialog();
				img1.setVisibility(View.INVISIBLE);

			}
		});

		/*
		 * expandableListView =
		 * (ExpandableListView)findViewById(R.id.expandableListView1);
		 */

		/*
		 * metrics = new DisplayMetrics();
		 * getWindowManager().getDefaultDisplay().getMetrics(metrics); width =
		 * metrics.widthPixels; //this code for adjusting the group indicator
		 * into right side of the view
		 * expandableListView.setIndicatorBounds(width - GetDipsFromPixel(50),
		 * width - GetDipsFromPixel(10));
		 */
		/*
		 * ActionBar mActionBar = getActionBar();
		 * 
		 * mActionBar.setDisplayShowHomeEnabled(true);
		 * mActionBar.setDisplayHomeAsUpEnabled(true);
		 * mActionBar.setIcon(R.drawable.ic_launcher);
		 * 
		 * LayoutInflater mInflater = LayoutInflater.from(this); View
		 * mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		 * mActionBar.setCustomView(mCustomView);
		 * //mActionBar.setDisplayShowCustomEnabled(true);
		 */
		actionBar = getActionBar();

		actionBar.setHomeButtonEnabled(true);
		// actionBar.setDisplayShowHomeEnabled(true);
		// actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);

		LayoutInflater linflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = linflater.inflate(R.layout.custom_actionbar, null);
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		view.setLayoutParams(lp);
		actionBar.setCustomView(view);

		tasksList = TaskListArray.getInstance(context); // Creating the tasks
		// list
		taskDataBase = TaskListDataBase.getInstance(context);
		
		 email1 = loginPreferences.getString("username", "");
		 
		tasksList.setTasks(taskDataBase.getAllTasks(email1)); // Setting the tasks
														// from database
		
		taskData = taskDataBase.getAllTasks(email1);
		
		if(taskData.size()!=0){
			
			System.out.println("===========Hello=if======");
		}
		
		else{
			
			System.out.println("===========Hello=else======");
	/*	prgDialog = new ProgressDialog(this);
			prgDialog.setMessage("Transferring Data from Remote MySQL DB and Syncing SQLite. Please wait...");
			prgDialog.setCancelable(false);*/
			
			syncSQLiteMySQLDB();
		}
	
		
		/*
		 * List<Task> task = taskDataBase.getAllTasks();
		 * 
		 * 
		 * Calendar c = Calendar.getInstance(); SimpleDateFormat sdf = new
		 * SimpleDateFormat("dd/MM/yyyy");
		 * 
		 * String strDate = sdf.format(c.getTime());
		 * 
		 * 
		 * 
		 * 
		 * //System.out.println("=========time=========="+strDate);
		 * 
		 * Date date1 = null; try { date1 = sdf.parse(strDate); } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * long ds = date1.getTime();
		 * 
		 * System.out.println("=========date=========="+ds);
		 * 
		 * 
		 * 
		 * for (Task cn : task) {
		 * 
		 * 
		 * 
		 * 
		 * //String cretedate= cn.getCreationDateString(); String reminderdate =
		 * cn.getReminderDateString();
		 * //System.out.println("===========reminder"+reminderdate);
		 * 
		 * 
		 * 
		 * // get tomorrow date Calendar calendar = Calendar.getInstance();
		 * calendar.add(Calendar.DAY_OF_YEAR, 1); Date tomorrow =
		 * calendar.getTime();
		 * 
		 * // get sevenWeek Calendar calendar1 = Calendar.getInstance();
		 * calendar1.add(Calendar.DAY_OF_YEAR, 7); Date newDate =
		 * calendar1.getTime();
		 * 
		 * // System.out.println("======Week========="+newDate);
		 * 
		 * 
		 * try{
		 * 
		 * SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		 * 
		 * DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); String
		 * tomorrowAsString = dateFormat.format(tomorrow); String weekAsString
		 * =dateFormat.format(newDate);
		 * 
		 * //String str1 = "12/10/2013"; Date date1 =
		 * formatter.parse(reminderdate);
		 * 
		 * 
		 * Date date2 = formatter.parse(strDate);
		 * 
		 * //System.out.println("===========reminder date"+date1);
		 * 
		 * //System.out.println("===========date2"+date2); Date
		 * date_tomorrow_comapre = formatter.parse(tomorrowAsString);
		 * 
		 * //
		 * System.out.println("==========tomorrow======"+date_tomorrow_comapre);
		 * Date week_as = formatter.parse(weekAsString);
		 * 
		 * // System.out.println("==========week======"+week_as);
		 * 
		 * 
		 * if (date1.compareTo(date2)==0) { System.out.println("Today");
		 * 
		 * 
		 * 
		 * 
		 * long id = cn.getId(); int done = cn.getIsDone(); String name =
		 * cn.getTitle();
		 * 
		 * String des = cn.getDescription();
		 * 
		 * String reminderdate1 = cn.getReminderDateString(); String
		 * remindertime = cn.getReminderTimeString();
		 * 
		 * System.out.println("==========Today===id===="+id);
		 * System.out.println("==========Today===name===="+name);
		 * System.out.println("==========Today===done===="+done);
		 * System.out.println("=========Today====des===="+des);
		 * System.out.println
		 * ("=========Today====Reminder===Today=Date====="+reminderdate1);
		 * System
		 * .out.println("=========Today====Reminder Today==Time===="+remindertime
		 * );
		 * 
		 * 
		 * 
		 * list1.add(name);
		 * 
		 * System.out.println("=====list1========="+list1);
		 * 
		 * // mydata=new ArrayList<FetchData>(); ArrayAdapter<String> adp=new
		 * ArrayAdapter<String>(context,
		 * android.R.layout.simple_list_item_1,list1);
		 * today_Listview.setAdapter(adp); mydata.add(new FetchData(name));
		 * 
		 * //System.out.println("=======list size=========="+mydata);
		 * 
		 * 
		 * }
		 * 
		 * 
		 * if(date1.compareTo(date_tomorrow_comapre)==0){
		 * System.out.println("Tomorrow");
		 * 
		 * 
		 * long id = cn.getId(); int done = cn.getIsDone(); String name =
		 * cn.getTitle();
		 * 
		 * String des = cn.getDescription();
		 * 
		 * String reminderdate1 = cn.getReminderDateString(); String
		 * remindertime = cn.getReminderTimeString();
		 * 
		 * System.out.println("========Tommarrow=====id===="+id);
		 * System.out.println("=========Tommarrow====name===="+name);
		 * System.out.println("==========Tommarrow===done===="+done);
		 * System.out.println("=======Tommarrow======des===="+des);
		 * System.out.println
		 * ("==========Reminder===Tommarrow=Date==="+reminderdate1);
		 * System.out.println
		 * ("=============Reminder Tommarrow==Time=="+remindertime);
		 * 
		 * // mydata=new ArrayList<FetchData>();
		 * 
		 * mydata.add(new FetchData(name));
		 * 
		 * list2.add(name);
		 * 
		 * ArrayAdapter<String> adp2=new ArrayAdapter<String>(context,
		 * android.R.layout.simple_list_item_2,list2);
		 * today_Listview.setAdapter(adp2);
		 * 
		 * System.out.println("=====list2========="+list2);
		 * 
		 * // System.out.println("=====tommarrow==list size=========="+mydata);
		 * 
		 * //mydata1.add(new FetchData(name)); // ExpAdapter = new
		 * ExpandListAdapter(HomeActivity.this, ExpListItems,mydata1); //
		 * expandableListView.setAdapter(ExpAdapter); }
		 * 
		 * 
		 * if(date1.before(week_as)){ System.out.println("Week");
		 * 
		 * 
		 * long id = cn.getId(); int done = cn.getIsDone(); String name =
		 * cn.getTitle();
		 * 
		 * String des = cn.getDescription();
		 * 
		 * String reminderdate1 = cn.getReminderDateString(); String
		 * remindertime = cn.getReminderTimeString();
		 * 
		 * System.out.println("========Week=====id===="+id);
		 * System.out.println("=========week====name===="+name);
		 * System.out.println("==========week===done===="+done);
		 * System.out.println("=======week======des===="+des);
		 * System.out.println
		 * ("==========week===Tommarrow=Date==="+reminderdate1);
		 * System.out.println
		 * ("=============week Tommarrow==Time=="+remindertime); // mydata=new
		 * ArrayList<FetchData>();
		 * 
		 * mydata.add(new FetchData(name));
		 * 
		 * list3.add(name);
		 * 
		 * ArrayAdapter<String> adp3=new ArrayAdapter<String>(context,
		 * android.R.layout.simple_list_item_1,list3);
		 * today_Listview.setAdapter(adp3);
		 * 
		 * System.out.println("=====list3========="+list3);
		 * 
		 * //mydata2.add(new FetchData(name)); //ExpAdapter = new
		 * ExpandListAdapter(HomeActivity.this, ExpListItems,mydata2);
		 * //expandableListView.setAdapter(ExpAdapter); }
		 * 
		 * }catch (ParseException e1){ e1.printStackTrace(); }
		 * 
		 * 
		 * // expandableListView.setAdapter(new
		 * SampleExpandableListAdapter(context, this, data,data1));
		 * 
		 * 
		 * 
		 * 
		 * ExpAdapter = new ExpandListAdapter(HomeActivity.this,
		 * ExpListItems,mydata); expandableListView.setAdapter(ExpAdapter);
		 * 
		 * }
		 */

		// System.out.println("Date - Time in milliseconds : " +
		// date.getTime());

		// c.setTime(date);
		// System.out.println("Calender - Time in milliseconds : "
		// + c.getTimeInMillis());

		/*
		 * Task tasks = new Task(); //long id =
		 * getIntent().getLongExtra("taskId", -1); tasks =
		 * taskDataBase.getTask(ds); // Getting the task from the database
		 * according to the ID
		 * 
		 * String title= tasks.getTitle(); String creation_date=
		 * tasks.getCreationDateString();
		 * 
		 * System.out.println("====dateofCreation title============"+title);
		 * System.out.println("====creation date==========="+creation_date);
		 */

		// title.setText(tasks.getTitle());
		// description.setText(tasks.getDescription());
		// dateCreated.setText(tasks.getCreationDateString());
		
		
		
			/*------Calender Code-----------*/
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

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(android.text.format.DateFormat.format(dateTemplate,
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
				final_date_month_year = date_month_year;
				
				ArrayList<String> arrayList = new ArrayList<String>();
				String str = final_date_month_year;
				String[] tokens = str.split("-");
				for (String s : tokens) {
					arrayList.add(s);
				}
				final_date = arrayList.get(0);
				final_month = arrayList.get(1);
				final_year = arrayList.get(2);
				final_date_month_year=final_date+"/"+final_month+"/"+final_year;
				System.out.println("==========final_date_month_year=========="+final_date_month_year);
				try {
					final_time=	 convert_date_into_long(date_month_year);
					System.out.println("long time"+final_time);
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 for(Task d : taskData){
					 String date= d.getCreationDateString();
					 String searchdate=d.getSearch_date();
			        	System.out.println("=============Date======="+date);
			        	System.out.println("=============search Date======="+searchdate);
if(searchdate!=null)
{
			if(searchdate.equals(final_time))
			{
			
			        	 mydata.add(new FetchData(d.getTitle()));

				
				 }
				 }
		}
				 Intent intent=new Intent(getBaseContext(), CalenderHomeSearch.class);
				  startActivity(intent);
			}
		});
	
		/*------Calender Code-----------*/
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
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mGoogleApiClient.connect();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(mGoogleApiClient.isConnected())
		{
		mGoogleApiClient.disconnect();
		}
	}

	private void syncSQLiteMySQLDB() {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		// Http Request Params Object
		RequestParams params = new RequestParams();
		params.put("user_id", email1);
		
		// Show ProgressBar
	//	prgDialog.show();
		// Make Http call to getusers.php
		client.post(AppConfig.GET_DATA_SYNC, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					// Hide ProgressBar
			//	prgDialog.hide();
					// Update SQLite DB with response sent by getusers.php
					updateSQLite(response);
					 System.out.println("=============sucess=======");
				}
				// When error occured
				@Override
				public void onFailure(int statusCode, Throwable error, String content) {
					// TODO Auto-generated method stub
					// Hide ProgressBar
			//prgDialog.hide();
					if (statusCode == 404) {
						Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
					} else if (statusCode == 500) {
						Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
					} else {
						//Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
						//		Toast.LENGTH_LONG).show();
					}
				}
		});
	}

	protected void updateSQLite(String response) {
		// TODO Auto-generated method stub
		
		
		System.out.println("======Response======================="+response);
		ArrayList<HashMap<String, String>> usersynclist;
		usersynclist = new ArrayList<HashMap<String, String>>();
		// Create GSON object
		Gson gson = new GsonBuilder().create();
		
		
		
		try {
			// Extract JSON array from the response
			
			JSONObject json=new JSONObject(response);
			String result= json.getString("result");
			String data= json.getString("result1");
			
			JSONArray arr = new JSONArray(data);
			System.out.println(arr.length());
			// If no of array elements is not zero
			if(arr.length() != 0){
				// Loop through each array element, get JSON object which has userid and username
				for (int i = 0; i < arr.length(); i++) {
					// Get JSON object
					JSONObject obj = (JSONObject) arr.get(i);
				//	System.out.println(obj.get("id"));
					System.out.println(obj.get("user_id"));
					
				//	String id=(String) obj.get("id");
					
					String user_id=(String) obj.get("user_id");
					
					String title=(String) obj.get("title");
					
					String description=(String) obj.get("description");
					
					String creationDate=(String) obj.get("creationDate");
					
					String reminder=(String) obj.get("reminder");
					
					
					String hasReminder=(String) obj.get("hasReminder");
					
					String location=(String) obj.get("location");
					
					String isDone=(String) obj.get("isDone");
					
					String with12=(String) obj.get("with12");
					
					String photo=(String) obj.get("photo");
					
					String audio=(String) obj.get("audio");
					
					String tag=(String) obj.get("tag");
					
					String mailid=(String) obj.get("mailid");
					
					String compose=(String) obj.get("compose");
					
					String notes=(String) obj.get("notes");
					
					String face_name=(String) obj.get("face_name");
					
					String face_sex=(String) obj.get("face_sex");
					
					String etc12=(String) obj.get("etc12");
					
					String improtance=(String) obj.get("imp");
					
					String event=(String) obj.get("event");
					
					String life=(String) obj.get("life");
				String search_date=(String) obj.get("search_date");
				String am_pm=(String) obj.get("am_pm");
				String final_repeat=(String) obj.get("rep");
				String final_repeat_day_month=(String) obj.get("repeat_day_month");
				String final_until_date=(String) obj.get("until_date")	;
					
					
					
					// DB QueryValues Object to insert into SQLite
					
					
					
					queryValues = new HashMap<String, String>();
					
					
					
					
					// Add userID extracted from Object
					queryValues.put("userId", obj.get("user_id").toString());
					// Add userName extracted from Object
				//	queryValues.put("userName", obj.get("userName").toString());
					// Insert User into SQLite DB
			
					long rowID;
					TaskListArray array = TaskListArray.getInstance(context);
					Task newTask = new Task(title);
					newTask.setEvent_with(with12);
					newTask.setEvent("event");
				    newTask.setEvent_tag(tag);
					newTask.setEvent_Audio(audio);
					newTask.setEvent_photo(photo);
					newTask.setLocation(location);
					newTask.setImportance(improtance);
					newTask.setUserId(email1);
					newTask.setDescription(description);
					newTask.setCompose(compose);
					newTask.setNotes(notes);
					newTask.setEtc(etc12);
					newTask.setName(face_name);
					newTask.setSex(face_sex);
					newTask.setEvent(event);
					newTask.setLife(life);
					newTask.setTo_mail(mailid);
					newTask.setSync_reminder(reminder);
					newTask.setUpdateStatus("yes");
				newTask.setSearch_date(search_date);
					newTask.setam_pm(am_pm);
					newTask.setFinal_until_date(final_until_date);
					newTask.setFinal_repeat(final_repeat);
					newTask.setFinal_repeat_day_month(final_repeat_day_month);
					Calendar cal = Calendar.getInstance();
					cal.set(Integer.parseInt(final_year),
							Integer.parseInt(final_month) - 1,
							Integer.parseInt(final_date),0,
							0, 0); // Setting a new
																// Calendar instance
																// with the user
																// reminder date

					/*Time reminder1 = new Time(); // Reminder in Time format to store in
												// the object
					reminder.set(0, Integer.parseInt("00"),
							Integer.parseInt("00"), Integer.parseInt(final_date),
							Integer.parseInt(final_month) - 1,
							Integer.parseInt(final_year)); // Setting the reminder to be
															// saved in the Task object
					newTask.setReminder(reminder1);*/
					newTask.setHasReminder(Integer.parseInt(hasReminder)); // Setting the boolean flag to indicate
												// that that the task has reminder

					rowID = taskDataBase.addTask1(newTask); // Adding to DataBase
					newTask.setId(rowID);
					array.addTask(newTask);

					//setReminder.setOneTimeReminder(context, cal, newTask); // Creating a
																			// new
																			// reminder
																			// to be
																			// added to
																			// the alarm
																			// manager
					
					
					//setReminder.setProximityAlert(context, newTask, latitude, longitude);
					
					
					HashMap<String, String> map = new HashMap<String, String>();
					// Add status for each User in Hashmap
					map.put("user_id", obj.get("user_id").toString());
					map.put("status", "1");
					usersynclist.add(map);
				}
				// Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
				updateMySQLSyncSts(gson.toJson(usersynclist));
				// Reload the Main Activity
				//reloadActivity();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void updateMySQLSyncSts(String json) {
		// TODO Auto-generated method stub
		System.out.println(json);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("syncsts", json);
		// Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
		client.post(AppConfig.SYNC_STATUS, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
			//	Toast.makeText(getApplicationContext(),	"MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {
					Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
			}
		});
	}

	public int GetDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 15f);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.actionmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void show_dialog() {
		RelativeLayout relativeLayout;
		ImageView event_dialogImageView, email_dialogImageView, etc_dialogImageView, phone_dialogImageView, faces_dialogImageView,life_dialogImageView;
		dialog = new Dialog(HomeActivity.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.copy_of_new_activity);

		relativeLayout = (RelativeLayout) dialog
				.findViewById(R.id.dialog_layout);
		event_dialogImageView = (ImageView) dialog
				.findViewById(R.id.dialog_event_img);
		email_dialogImageView = (ImageView) dialog
				.findViewById(R.id.dialog_email_img);
		etc_dialogImageView = (ImageView) dialog
				.findViewById(R.id.dialog_etc_img);
		phone_dialogImageView = (ImageView) dialog
				.findViewById(R.id.dialog_phone_img);
		faces_dialogImageView = (ImageView) dialog
				.findViewById(R.id.dialog_faces_img);
		life_dialogImageView = (ImageView) dialog
				.findViewById(R.id.dialog_life_img);

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				img1.setVisibility(View.VISIBLE);
			}
		});
		life_dialogImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(HomeActivity.this,
						LifeActivity.class);
				HomeActivity.this.startActivity(mainIntent);

				dialog.dismiss();
				img1.setVisibility(View.VISIBLE);

			}
		});
		faces_dialogImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(HomeActivity.this,
						FacesActivity.class);
				HomeActivity.this.startActivity(mainIntent);

				dialog.dismiss();
				img1.setVisibility(View.VISIBLE);

			}
		});
		phone_dialogImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(HomeActivity.this,
						PhoneActivity.class);
				HomeActivity.this.startActivity(mainIntent);

				dialog.dismiss();

			}
		});
		etc_dialogImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(HomeActivity.this,
						NewEtcActivity.class);
				HomeActivity.this.startActivity(mainIntent);

				dialog.dismiss();

			}
		});
		email_dialogImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(HomeActivity.this,
						EmailActivity.class);
				HomeActivity.this.startActivity(mainIntent);

				dialog.dismiss();

			}
		});
		event_dialogImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(HomeActivity.this,
						EventActivity.class);
				HomeActivity.this.startActivity(mainIntent);

				dialog.dismiss();

			}
		});
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			//FacebookLoginActivity.onClickLogout();
			signOutFromGplus();
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		
	
		finish();
		//new FacebookLoginActivity().onClickLogout();

	}

	/* ----------------------find id of all view ----------------- */
	private void find_id() {
		
		TextView	start_textview=(TextView)findViewById(R.id.start_textview);
		TextView	end_textview=(TextView)findViewById(R.id.end_textview);
		start_textview.setVisibility(View.INVISIBLE);
		end_textview.setVisibility(View.INVISIBLE);

		today_Listview = (ListView) findViewById(R.id.today_Listview);
		 today_Listview.setOnItemClickListener(this);
		tomorrow_Listview = (ListView) findViewById(R.id.tomorrow_Listview);
		week_Listview = (ListView) findViewById(R.id.week_Listview);
		all_ListView= (ListView) findViewById(R.id.all_Listview);
		all_ListView.setOnItemClickListener(this);
		today_include_view = findViewById(R.id.today_include_layout);
		tomorrow_include_view = findViewById(R.id.tomorrow_include_layout);
		week_include_view = findViewById(R.id.week_include_layout);
		all_include_View=findViewById(R.id.all_include_layout);
		date_include_view = findViewById(R.id.homecal_include_layout);

		todayLinearLayout = (LinearLayout) findViewById(R.id.today_layout);
		tomorrowLinearLayout = (LinearLayout) findViewById(R.id.tomorrow_layout);
		weekLinearLayout = (LinearLayout) findViewById(R.id.week_layout);
		allLinearLayout = (LinearLayout) findViewById(R.id.all_layout);
		dateLinearLayout = (LinearLayout) findViewById(R.id.date_layout);

		today_plusImageView = (ImageView) findViewById(R.id.today_plus_img);
		tomorrow_plusImageView = (ImageView) findViewById(R.id.tomorrow_plus_img);
		week_plusImageView = (ImageView) findViewById(R.id.week_plus_img);
		all_plusImageView=(ImageView) findViewById(R.id.all_plus_img);
		date_plusImageView = (ImageView) findViewById(R.id.date_plus_img);
		

		today_TextView = (TextView) findViewById(R.id.today_text);
		tomorrow_TextView = (TextView) findViewById(R.id.tomorrow_text);
		week_TextView = (TextView) findViewById(R.id.week_text);
		all_TextView=(TextView) findViewById(R.id.all_text);
		date_TextView = (TextView) findViewById(R.id.date_text);
		
		settingImageView=(ImageView)findViewById(R.id.setting_img);
		searchImageView=(ImageView)findViewById(R.id.search_img);
		todayLinearLayout.setOnClickListener(this);
		tomorrowLinearLayout.setOnClickListener(this);
		weekLinearLayout.setOnClickListener(this);
		allLinearLayout.setOnClickListener(this);
		dateLinearLayout.setOnClickListener(this);
		settingImageView.setOnClickListener(this);
		searchImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.today_layout:
			today_plus_minus();

			break;
		case R.id.tomorrow_layout:
			tomorrow_plus_minus();
			break;
		case R.id.week_layout:
			week_plus_minus();
			break;
		case R.id.all_layout:
			all_plus_minus();
			break;
		case R.id.date_layout:
			 date_plus_minus();
			break;
		case R.id.setting_img:
			 Intent intent=new Intent(getApplicationContext(), SettingActivity.class);
			 startActivity(intent);
			 break;	
		case R.id.search_img:
			 Intent intent1=new Intent(getApplicationContext(), SearchActivity.class);
			 startActivity(intent1);
			 break;	
	
		default:
			break;
		}
	}

	private void all_plus_minus() {

		if (all_on == false) {
			all_plusImageView.setBackgroundResource(R.drawable.minus);
			all_include_View.setVisibility(View.VISIBLE);
		
			all_on = true;

			show_all_data();
			tomorrow_on = true;
			week_on = true;
			tomorrow_plus_minus();
			week_plus_minus();
			// date_plus_minus();
			all_TextView.setTextColor(getResources().getColor(R.color.Green));

		} else {
			//list1.clear();
			taskData.clear();
			all_plusImageView.setBackgroundResource(R.drawable.plus);
			all_include_View.setVisibility(View.GONE);
			all_on = false;
			all_TextView.setTextColor(getResources().getColor(R.color.Gray));
		}
		
	}

	public void today_plus_minus() {
		mydata.clear();
		if (today_on == false) {
			today_plusImageView.setBackgroundResource(R.drawable.minus);
			today_include_view.setVisibility(View.VISIBLE);
		
			today_on = true;

			LoadData(1);
			tomorrow_on = true;
			week_on = true;

			tomorrow_plus_minus();
			week_plus_minus();
			// date_plus_minus();
			today_TextView.setTextColor(getResources().getColor(R.color.Green));

		} else {
			//list1.clear();
			mydata.clear();
			today_plusImageView.setBackgroundResource(R.drawable.plus);
			today_include_view.setVisibility(View.GONE);
			today_on = false;
			today_TextView.setTextColor(getResources().getColor(R.color.Gray));
		}
	}

	public void tomorrow_plus_minus() {
		mydata1.clear();
		if (tomorrow_on == false) {
			tomorrow_plusImageView.setBackgroundResource(R.drawable.minus);
			tomorrow_include_view.setVisibility(View.VISIBLE);
			today_on = true;
			LoadData(2);
			tomorrow_on = true;
			week_on = true;
			//date_on = true;
			today_plus_minus();
			week_plus_minus();
			// date_plus_minus();
			tomorrow_TextView.setTextColor(getResources().getColor(
					R.color.Green));

		} else {
			mydata1.clear();
			tomorrow_plusImageView.setBackgroundResource(R.drawable.plus);
			tomorrow_include_view.setVisibility(View.GONE);
			tomorrow_on = false;
			tomorrow_TextView.setTextColor(getResources()
					.getColor(R.color.Gray));
		}
	}
public void week_plus_minus() {

		
		mydata2.clear();
		if (week_on == false) {
			week_plusImageView.setBackgroundResource(R.drawable.minus);
			week_include_view.setVisibility(View.VISIBLE);
			today_on = true;
			LoadData(3);
			tomorrow_on = true;
			week_on = true;
			//date_on = true;
			tomorrow_plus_minus();
			today_plus_minus();
			// date_plus_minus();
			week_TextView.setTextColor(getResources().getColor(R.color.Green));

		} else {
           // list3.clear();
			mydata2.clear();
			week_plusImageView.setBackgroundResource(R.drawable.plus);
			week_include_view.setVisibility(View.GONE);
			week_on = false;
			week_TextView.setTextColor(getResources().getColor(R.color.Gray));
		}
	}

public void close_all()
{
	//date_on = true;
	today_on=true;
	 tomorrow_on=true; week_on=true;
	tomorrow_plus_minus();
	today_plus_minus();
	//date_plus_minus();
	week_plus_minus();
	all_plus_minus();
}
	private void show_all_data()
	{
		taskData = taskDataBase.getAllTasks(email1);
		All_Adapter	 adapter = new All_Adapter(this, taskData);
		 all_ListView.setAdapter(adapter);
		
	}
	
	private void LoadData(int i) {
		// TODO Auto-generated method stub

		taskData = taskDataBase.getAllTasks(email1);

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		String strDate = sdf.format(c.getTime());

		// System.out.println("=========time=========="+strDate);
		/*
		 * Date date1 = null; try { date1 = sdf.parse(strDate); } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * long ds = date1.getTime();
		 * 
		 * System.out.println("=========date=========="+ds);
		 */

		for (Task cn : taskData) {
			int hasReminder= cn.getHasReminder();
			// String cretedate= cn.getCreationDateString();
			String reminderdate = cn.getReminderDateString();
			String event= cn.getEvent();
			String mail= cn.getTo_mail();
			String phone_notes= cn.getNotes();
			String face_name= cn.getName();
			
			String etc= cn.getEtc();
			
			
		// System.out.println("===========reminder"+reminderdate);

			// get tomorrow date
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date tomorrow = calendar.getTime();

			// get sevenWeek
			Calendar calendar1 = Calendar.getInstance();
			calendar1.add(Calendar.DAY_OF_YEAR, 7);
			Date newDate = calendar1.getTime();

	      System.out.println("======event========="+event);

			try {

				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String tomorrowAsString = dateFormat.format(tomorrow);
				String weekAsString = dateFormat.format(newDate);
				
				// String str1 = "12/10/2013";
				Date date1 = formatter.parse(reminderdate);

				Date date2 = formatter.parse(strDate);

				// System.out.println("===========reminder date"+date1);

				// System.out.println("===========date2"+date2);
				Date date_tomorrow_comapre = formatter.parse(tomorrowAsString);

				// System.out.println("==========tomorrow======"+date_tomorrow_comapre);
				Date week_as = formatter.parse(weekAsString);

				// System.out.println("==========week======"+week_as);

				if (i == 1) {

					if (date1.compareTo(date2) == 0 && hasReminder==1 ) {
						System.out.println("Today");

						long id = cn.getId();
						int done = cn.getIsDone();
						String name = cn.getTitle();
						String importance = cn.getImportance();

						String des = cn.getDescription();

						String reminderdate1 = cn.getReminderDateString();
						String remindertime = cn.getReminderTimeString();
						
					/*	System.out.println("==========Today===id====" + id);
						System.out.println("==========Today===name====" + name);
						System.out.println("==========Today===done====" + done);
						System.out.println("=========Today====des====" + des);
						System.out
								.println("=========Today====Reminder===Today=Date====="
										+ reminderdate1);
						System.out
								.println("=========Today====Reminder Today==Time===="
										+ remindertime);*/

						list1.add(name);

						//System.out.println("=====list1=========" + list1);

						// mydata=new ArrayList<FetchData>();
					/*	ArrayAdapter<String> adp = new ArrayAdapter<String>(
								context, android.R.layout.simple_list_item_1,
								list1);
						today_Listview.setAdapter(adp);*/
						
						
						mydata.add(new FetchData(name,importance,id,event,mail,phone_notes,face_name,etc));
						
						TimeAdapter	 adapter = new TimeAdapter(this, mydata);
						 today_Listview.setAdapter(adapter);
						

						// System.out.println("=======list size=========="+mydata);

					}

				}

				if (i == 2) {
					if (date1.compareTo(date_tomorrow_comapre) == 0 && hasReminder == 1) {
						System.out.println("Tomorrow");

						long id = cn.getId();
						int done = cn.getIsDone();
						String name = cn.getTitle();
						String importance = cn.getImportance();
						String des = cn.getDescription();

						String reminderdate1 = cn.getReminderDateString();
						String remindertime = cn.getReminderTimeString();

					/*	System.out.println("========Tommarrow=====id====" + id);
						System.out.println("=========Tommarrow====name===="
								+ name);
						System.out.println("==========Tommarrow===done===="
								+ done);
						System.out.println("=======Tommarrow======des===="
								+ des);
						System.out
								.println("==========Reminder===Tommarrow=Date==="
										+ reminderdate1);
						System.out
								.println("=============Reminder Tommarrow==Time=="
										+ remindertime);
*/
						// mydata=new ArrayList<FetchData>();

						//mydata.add(new FetchData(name));

						list2.add(name);

					/*	ArrayAdapter<String> adp2 = new ArrayAdapter<String>(
								context, android.R.layout.simple_list_item_1,
								list2);
						tomorrow_Listview.setAdapter(adp2);*/
						
						
	                    mydata1.add(new FetchData(name,importance,id,event,mail,phone_notes,face_name,etc));
						
	                    TimeAdapter	 adapter = new TimeAdapter(this, mydata1);
						tomorrow_Listview.setAdapter(adapter);
						tomorrow_Listview.setOnItemClickListener(this);

						//System.out.println("=====list2=========" + list2);

						// System.out.println("=====tommarrow==list size=========="+mydata);

						// mydata1.add(new FetchData(name));
						// ExpAdapter = new ExpandListAdapter(HomeActivity.this,
						// ExpListItems,mydata1);
						// expandableListView.setAdapter(ExpAdapter);
					}

				}

				if (i == 3) {
					if (date1.before(week_as) && hasReminder == 1) {
						System.out.println("Week");

						long id = cn.getId();
						int done = cn.getIsDone();
						String name = cn.getTitle();
						String importance = cn.getImportance();

						String des = cn.getDescription();

						String reminderdate1 = cn.getReminderDateString();
						String remindertime = cn.getReminderTimeString();

					/*	System.out.println("========Week=====id====" + id);
						System.out.println("=========week====name====" + name);
						System.out.println("==========week===done====" + done);
						System.out.println("=======week======des====" + des);
						System.out.println("==========week===Tommarrow=Date==="
								+ reminderdate1);
						System.out
								.println("=============week Tommarrow==Time=="
										+ remindertime);*/
						// mydata=new ArrayList<FetchData>();

						mydata2.add(new FetchData(name,importance,id,event,mail,phone_notes,face_name,etc));

						list3.add(name);

						/*ArrayAdapter<String> adp3 = new ArrayAdapter<String>(
								context,
								android.R.layout.simple_expandable_list_item_1,
								list3);
						week_Listview.setAdapter(adp3);*/
						
						
						TimeAdapter adapter = new TimeAdapter(this, mydata2);
						week_Listview.setAdapter(adapter);
						week_Listview.setOnItemClickListener(this);

						System.out.println("=====list3=========" + list3);

						// mydata2.add(new FetchData(name));
						// ExpAdapter = new ExpandListAdapter(HomeActivity.this,
						// ExpListItems,mydata2);
						// expandableListView.setAdapter(ExpAdapter);
					}
				}

			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			// expandableListView.setAdapter(new
			// SampleExpandableListAdapter(context, this, data,data1));

			/*
			 * ExpAdapter = new ExpandListAdapter(HomeActivity.this,
			 * ExpListItems,mydata); expandableListView.setAdapter(ExpAdapter);
			 */

		}
	}

	public void date_plus_minus() {
		if (date_on == false) {
			date_plusImageView
					.setBackgroundResource(R.drawable.etc_calendar_icon_inactive);
			date_include_view.setVisibility(View.VISIBLE);
			today_on = true;

			tomorrow_on = true;
			week_on = true;
			date_on = true;

			tomorrow_plus_minus();
			today_plus_minus();
			week_plus_minus();
			date_TextView.setTextColor(getResources().getColor(R.color.Green));

		} else {

			date_plusImageView
					.setBackgroundResource(R.drawable.etc_calendar_icon_active);
			date_include_view.setVisibility(View.GONE);
			date_on = false;
			date_TextView.setTextColor(getResources().getColor(R.color.Gray));
		}
	}

	
	
	
	/*-------------------------------------Calender--------------------------------*/
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
			private final String[] months = { "1", "2", "3", "4", "5", "6",
					"7", "8", "9", "10", "11", "12" };
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




		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			switch (parent.getId()) {
			case R.id.today_Listview:
			//Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
			SendData(position,mydata);
				break;

           case R.id.tomorrow_Listview:
        	   SendData(position,mydata1);
				break;
				
           case R.id.week_Listview:
        	   SendData(position,mydata2);
				break;
           case R.id.all_Listview:
        	  SendAllData(position, taskData);
        	   break;
			}
			
		}

		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
	//	close_all();
			close();
		}
public void close()
{
	tomorrow_plusImageView.setBackgroundResource(R.drawable.plus);
	tomorrow_include_view.setVisibility(View.GONE);
	tomorrow_on = false;
	tomorrow_TextView.setTextColor(getResources()
			.getColor(R.color.Gray));



today_plusImageView.setBackgroundResource(R.drawable.plus);
	today_include_view.setVisibility(View.GONE);
	today_on = false;
	today_TextView.setTextColor(getResources().getColor(R.color.Gray));

week_plusImageView.setBackgroundResource(R.drawable.plus);
	week_include_view.setVisibility(View.GONE);
	week_on = false;
	week_TextView.setTextColor(getResources().getColor(R.color.Gray));

	date_plusImageView
	.setBackgroundResource(R.drawable.etc_calendar_icon_active);
date_include_view.setVisibility(View.GONE);
date_on = false;
date_TextView.setTextColor(getResources().getColor(R.color.Gray));

}


// all
private void SendAllData(int position, ArrayList<Task> mydata) {
	// TODO Auto-generated method stub
	 long eventId=mydata.get(position).getId();

	 
	 
	 
	 
	      String title=taskData.get(position).getTitle();
	 
	
		  String description=taskData.get(position).getDescription();
		  System.out.println("=======description================"+description);
   
  
	
		
	
	 
	 String location=taskData.get(position).getLocation();
	 
	 String importance=taskData.get(position).getImportance();
	 
	 String event=mydata.get(position).getEvent();
	 
	 String audio=taskData.get(position).getEvent_Audio();
	 
	 String photo=taskData.get(position).getEvent_photo();
	 
	 String tag=taskData.get(position).getEvent_tag();
	 
	 String etc=mydata.get(position).getEtc();

	 String compose=taskData.get(position).getCompose();

	 String name=mydata.get(position).getName();

	 String notes=mydata.get(position).getNotes();
	 
	 String sex=taskData.get(position).getSex();
	 
	 String mail=mydata.get(position).getTo_mail();
	 
	 String life=taskData.get(position).getLife();
	 
	 
	 
	 System.out.println("=======eventId=======fff========="+eventId);
	 
	 System.out.println("=======title================"+title);
	 
	 System.out.println("=======eventId================"+eventId);
	 
	
	 
	 System.out.println("=======location================"+location);
	 
	 System.out.println("=======importance================"+importance);
	 
	 System.out.println("=======event================"+event);
	 
	 System.out.println("=======audio================"+audio);
	 
	 System.out.println("=======photo================"+photo);
	 
	 System.out.println("=======tag================"+tag);
	 
	 System.out.println("=======etc================"+etc);
	 
	 System.out.println("=======compose================"+compose);
	 
	 System.out.println("=======notes================"+notes);
	 
	 System.out.println("=======name================"+name);
	 
	 System.out.println("=======sex================"+sex);
	 
	 System.out.println("=======mail================"+mail);
	 
	 System.out.println("=======life================"+life);
	 
	 
	 
	 if(event!=null){
	 if(event.equals("event")){
		 
		 Intent mEvent=new Intent(HomeActivity.this,EventActivity.class);
		 mEvent.putExtra("taskId", eventId);
		 startActivity(mEvent);
	 }
	 }
	 if(mail!=null){
	 if(isEmailValid(mail)){
		
		 Intent mEvent=new Intent(HomeActivity.this,EmailActivity.class);
		 mEvent.putExtra("taskId", eventId);
		 startActivity(mEvent); 
	 }
	 }

	 if(name!=null){
    if(name.equals("Name")){
   
		 Intent mEvent=new Intent(HomeActivity.this,FacesActivity.class);
		 mEvent.putExtra("taskId", eventId);
		 startActivity(mEvent);
	 }
    
	 }
	 if(notes!=null){
    if(notes.equals("NOTES")){
 
		 Intent mEvent=new Intent(HomeActivity.this,PhoneActivity.class);
		 mEvent.putExtra("taskId", eventId);
		 startActivity(mEvent);
	 }
	 }
	 if(etc!=null){
     if(etc.equals("etc")){
    	
		 Intent mEvent=new Intent(HomeActivity.this,NewEtcActivity.class);
		 mEvent.putExtra("taskId", eventId);
		 startActivity(mEvent);
	 }
     }
     
	    if(life!=null){
         if(life.equals("life")){
        	
		 Intent mEvent=new Intent(HomeActivity.this,LifeActivity.class);
		 mEvent.putExtra("taskId", eventId);
		 startActivity(mEvent);
	 }
	    }
}
// all end
		private void SendData(int position, ArrayList<FetchData> mydata) {
			// TODO Auto-generated method stub
			 long eventId=mydata.get(position).getId();

			 
			 
			 
			 
			 String title=taskData.get(position).getTitle();
			 
			
				  String description=taskData.get(position).getDescription();
				  System.out.println("=======description================"+description);
	       
		  
			
				
			
			 
			 String location=taskData.get(position).getLocation();
			 
			 String importance=taskData.get(position).getImportance();
			 
			 String event=mydata.get(position).getEvent();
			 
			 String audio=taskData.get(position).getEvent_Audio();
			 
			 String photo=taskData.get(position).getEvent_photo();
			 
			 String tag=taskData.get(position).getEvent_tag();
			 
			 String etc=mydata.get(position).getEtc();

			 String compose=taskData.get(position).getCompose();

			 String name=mydata.get(position).getName();

			 String notes=mydata.get(position).getNotes();
			 
			 String sex=taskData.get(position).getSex();
			 
			 String mail=mydata.get(position).getTo_mail();
			 
			 String life=taskData.get(position).getLife();
			 
			 
			 
			 System.out.println("=======eventId=======fff========="+eventId);
			 
			 System.out.println("=======title================"+title);
			 
			 System.out.println("=======eventId================"+eventId);
			 
			
			 
			 System.out.println("=======location================"+location);
			 
			 System.out.println("=======importance================"+importance);
			 
			 System.out.println("=======event================"+event);
			 
			 System.out.println("=======audio================"+audio);
			 
			 System.out.println("=======photo================"+photo);
			 
			 System.out.println("=======tag================"+tag);
			 
			 System.out.println("=======etc================"+etc);
			 
			 System.out.println("=======compose================"+compose);
			 
			 System.out.println("=======notes================"+notes);
			 
			 System.out.println("=======name================"+name);
			 
			 System.out.println("=======sex================"+sex);
			 
			 System.out.println("=======mail================"+mail);
			 
			 System.out.println("=======life================"+life);
			 
			 
			 
			 if(event!=null){
			 if(event.equals("event")){
				 
				 Intent mEvent=new Intent(HomeActivity.this,EventActivity.class);
				 mEvent.putExtra("taskId", eventId);
				 startActivity(mEvent);
			 }
			 }
			 if(mail!=null){
			 if(isEmailValid(mail)){
				
				 Intent mEvent=new Intent(HomeActivity.this,EmailActivity.class);
				 mEvent.putExtra("taskId", eventId);
				 startActivity(mEvent); 
			 }
			 }

			 if(name!=null){
	        if(name.equals("Name")){
	       
				 Intent mEvent=new Intent(HomeActivity.this,FacesActivity.class);
				 mEvent.putExtra("taskId", eventId);
				 startActivity(mEvent);
			 }
	        
			 }
			 if(notes!=null){
	        if(notes.equals("NOTES")){
	     
				 Intent mEvent=new Intent(HomeActivity.this,PhoneActivity.class);
				 mEvent.putExtra("taskId", eventId);
				 startActivity(mEvent);
			 }
			 }
			 if(etc!=null){
	         if(etc.equals("etc")){
	        	
				 Intent mEvent=new Intent(HomeActivity.this,NewEtcActivity.class);
				 mEvent.putExtra("taskId", eventId);
				 startActivity(mEvent);
			 }
	         }
	         
			    if(life!=null){
	             if(life.equals("life")){
	            	
				 Intent mEvent=new Intent(HomeActivity.this,LifeActivity.class);
				 mEvent.putExtra("taskId", eventId);
				 startActivity(mEvent);
			 }
			    }
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

		@Override
		public void onConnected(Bundle arg0) {
			// TODO Auto-generated method stub
			Log.d("Debug","Connected");
		}

		@Override
		public void onConnectionSuspended(int arg0) {
			// TODO Auto-generated method stub


		
		}

		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
			Log.d("Debug","Connected fialed");
		}
		private void gpluslogout()
		{
			if(mGoogleApiClient.isConnected())
			{
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				mGoogleApiClient.disconnect();
				mGoogleApiClient.connect();
				
			}
		}
}