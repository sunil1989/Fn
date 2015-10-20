package settings;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.evernote.edam.type.Note;
import com.life_reminder.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.AppConfig;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;

public class SettingActivity extends Activity implements OnClickListener , EvernoteLoginFragment.ResultCallback {
	RadioGroup radioGroup;
	RadioButton sync_onRadioButton, sync_offRadioButton;
	boolean profile_on, sync_on, c, sound_on, preference_on,password_on;
	View include_profile_view,include_sound_view,include_password_view,setting_prefrences;
	InputMethodManager imm;
	LinearLayout syncLinearLayout, profileLinearLayout,
			preferencesLinearLayout, passwordLinearLayout, soundLinearLayout;
	ImageView syncplusImageView, profileplusImageView,
			preferencesplusImageView, passwordplusImageView,
			soundplusImageView;
	TextView syncTextView, profileTextView, preferencesTextView,
			passwordTextView, soundTextView;
    ArrayList<Task> taskData;;
	RequestParams params = new RequestParams();
	private ProgressDialog pDialog;
	 private SharedPreferences loginPreferences;
	    private SharedPreferences.Editor loginPrefsEditor;
	
	//Sound increase decrease
	SeekBar mSeekbar;
	//an AudioManager object, to change the volume settings
	private AudioManager amanager;
	
	ProgressDialog prgDialog;
	//get Profile
	EditText fname;
	EditText lname;
	EditText phone;
	Button updatemButton;
	
	String email1 ;
	String fname1 ;
	String lname1;
	String phone1;
	
	EditText mOldPassword;
	EditText mNewPassword;
	Button mChangePasswordButton;

    public static int tokan;
	
	private RadioGroup sync_group;
	  private RadioButton mPrefSetting;
	  
	  private TaskListDataBase taskDataBase;
		private Task task;
		private TaskListArray tasksList;
		Context context = this;

    Button mSyncevernote;
    EvernoteNoteStoreClient noteStoreClient;

    private static final String CONSUMER_KEY = "sunil1987";
    private static final String CONSUMER_SECRET = "6153f2d4906ca409";
    /*
     * Initial development is done on Evernote's testing service, the sandbox.
     *
     * Change to PRODUCTION to use the Evernote production service
     * once your code is complete.
     */
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    /*
        * Set this to true if you want to allow linked notebooks for accounts that
        * can only access a single notebook.
        */
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;
    EvernoteSession mEvernoteSession;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

        String consumerKey;
        if ("Your consumer key".equals(CONSUMER_KEY)) {
            consumerKey = CONSUMER_KEY;
        } else {
            // isn't the default value anymore
            consumerKey = CONSUMER_KEY;
        }

        String consumerSecret;
        if ("Your consumer secret".equals(CONSUMER_SECRET)) {
            consumerSecret = CONSUMER_SECRET;
        } else {
            // isn't the default value anymore
            consumerSecret = CONSUMER_SECRET;
        }


        //Set up the Evernote singleton session, use EvernoteSession.getInstance() later
         mEvernoteSession=  new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(SUPPORT_APP_LINKED_NOTEBOOKS)
                .setForceAuthenticationInThirdPartyApp(true)
//                .setLocale(Locale.SIMPLIFIED_CHINESE)
                .build(consumerKey, consumerSecret)
                .asSingleton();

      // this.registerActivityLifecycleCallbacks(new LoginChecker());

       //mEvernoteSession.authenticate(this);



		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		/* ---------Hide the keyboard from window------ */
		imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

		/* end -- Hide the keyboard from window */
		/* Show the Custom Action bar */
		custom_actionbar();
		findid();
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
		action_bar_title.setText("Setting");
		action_bar_img.setImageResource(R.drawable.home_settings_icon_color);

	}

	private void findid() {

		// TODO Auto-generated method stub
		syncLinearLayout = (LinearLayout) findViewById(R.id.sync_layout);
		profileLinearLayout = (LinearLayout) findViewById(R.id.profile_layout);
		preferencesLinearLayout = (LinearLayout) findViewById(R.id.prefrences_layout);
		passwordLinearLayout = (LinearLayout) findViewById(R.id.password_layout);
		soundLinearLayout = (LinearLayout) findViewById(R.id.sound_layout);

		syncplusImageView = (ImageView) findViewById(R.id.sync_plus_img);
		profileplusImageView = (ImageView) findViewById(R.id.profile_plus_img);
		preferencesplusImageView = (ImageView) findViewById(R.id.prefrences_plus_img);
		passwordplusImageView = (ImageView) findViewById(R.id.password_plus_img);
		soundplusImageView = (ImageView) findViewById(R.id.sound_plus_img);

		syncTextView = (TextView) findViewById(R.id.sync_text);
		profileTextView = (TextView) findViewById(R.id.profile_text);
		preferencesTextView = (TextView) findViewById(R.id.prefrences_text);
		passwordTextView = (TextView) findViewById(R.id.password_text);
		soundTextView = (TextView) findViewById(R.id.sound_text);

		syncLinearLayout.setOnClickListener(this);
		profileLinearLayout.setOnClickListener(this);
		preferencesLinearLayout.setOnClickListener(this);
		passwordLinearLayout.setOnClickListener(this);
		soundLinearLayout.setOnClickListener(this);
        mSyncevernote=(Button)findViewById(R.id.sync_evernote);
        mSyncevernote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater factory = LayoutInflater.from(SettingActivity.this);
                final View deleteDialogView = factory.inflate(
                        R.layout.sync_dialog, null);
                final AlertDialog deleteDialog = new AlertDialog.Builder(SettingActivity.this).create();
                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                deleteDialog.setView(deleteDialogView);

                deleteDialogView.findViewById(R.id.btn_yes).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //your business logic
                        deleteDialog.dismiss();

                        tokan=1;

                        mEvernoteSession.authenticate(SettingActivity.this);

                    }
                });
                deleteDialogView.findViewById(R.id.btn_no).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        syncSQLiteMySQLDB();

                        // dialog.cancel();
                        deleteDialog.dismiss();

                    }
                });

                deleteDialog.show();




             /*   EvernoteSession.getInstance().authenticate(SettingActivity.this);
                mSyncevernote.setEnabled(false);

                if (!EvernoteSession.getInstance().isLoggedIn()) {

                    return;
                }
                else{
                    noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

                    Note note = new Note();
                    note.setTitle("My title");
                    note.setContent(EvernoteUtil.NOTE_PREFIX + "My content" + EvernoteUtil.NOTE_SUFFIX);
                    noteStoreClient.createNoteAsync(note,new EvernoteCallback<Note>() {
                        @Override
                        public void onSuccess(Note note) {
                            Toast.makeText(getApplicationContext(), note.getTitle() + " has been created", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onException(Exception exception) {
                            Log.e("Logtag", "Error creating note", exception);
                        }
                    });
                }*/


             /*   noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
                    @Override
                    public void onSuccess(Note result) {
                        Toast.makeText(getApplicationContext(), result.getTitle() + " has been created", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onException(Exception exception) {
                        Log.e("Logtag", "Error creating note", exception);
                    }
                });*/

            }
        });
		
		

		/*------------------- Prefeerence view-------------------*/
		radioGroup = (RadioGroup) findViewById(R.id.sync_group);
		sync_onRadioButton = (RadioButton) findViewById(R.id.sync_on);
		sync_offRadioButton = (RadioButton) findViewById(R.id.sync_off);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.sync_on:
					
					break;
				case R.id.sync_off:
				
					break;
				default:
					break;
				}

			}
		});
		/*------------------- include view-------------------*/
		include_profile_view = findViewById(R.id.include_profile);
		include_sound_view = findViewById(R.id.include_sound);
		include_password_view = findViewById(R.id.include_password);
		setting_prefrences=findViewById(R.id.include_prefrence);
		imm.hideSoftInputFromWindow(syncTextView.getWindowToken(), 0);
		
		// get id sound activity
		mSeekbar=(SeekBar)findViewById(R.id.sb_volumebar);
		
		//profile settings
		
		 fname=(EditText)findViewById(R.id.first_name);
	     lname=(EditText)findViewById(R.id.last_name);
		 phone=(EditText)findViewById(R.id.phone_number);
		 updatemButton=(Button)findViewById(R.id.update_button);

			loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
	        loginPrefsEditor = loginPreferences.edit(); 
	        
	        // change Password
	        mOldPassword=(EditText)findViewById(R.id.password_editText);
	       mNewPassword=(EditText)findViewById(R.id.confirm_password_editText);
	       mChangePasswordButton=(Button)findViewById(R.id.change_password_update_button);
	       
	       tasksList = TaskListArray.getInstance(context); // Creating the tasks
			// list
			taskDataBase = TaskListDataBase.getInstance(context);
        email1 = loginPreferences.getString("username", "");
        taskData = taskDataBase.getAllTasks(email1);
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sync_layout:
			sync_plus_minus();
			imm.hideSoftInputFromWindow(syncTextView.getWindowToken(), 0);
			break;
		case R.id.profile_layout:
			// get_profile_info();

			profile_plus_minus();
			imm.hideSoftInputFromWindow(syncTextView.getWindowToken(), 0);
			break;
		case R.id.prefrences_layout:
			prefrence_plus_minus();
			imm.hideSoftInputFromWindow(syncTextView.getWindowToken(), 0);
			break;
		case R.id.password_layout:
			password_plus_minus();
			imm.hideSoftInputFromWindow(syncTextView.getWindowToken(), 0);
			break;
		case R.id.sound_layout:
			sound_plus_minus();
			imm.hideSoftInputFromWindow(syncTextView.getWindowToken(), 0);
			break;

		default:
			break;
		}

	}

	private void prefrence_plus_minus() {
		// TODO Auto-generated method stub
		
		if (preference_on== false) {
			preferencesplusImageView.setBackgroundResource(R.drawable.minus);
			setting_prefrences.setVisibility(View.VISIBLE);
			profile_on = true;
			sync_on = true;
			password_on = true;
			sound_on = true;
			preference_on = true;

			//sync_plus_minus();
			profile_plus_minus();
			sound_plus_minus();
			preference_plus_minus();
			//passwordTextView.setTextColor(getResources().getColor(R.color.Gray));
			//passwordTextView.setText("password");
			
			//ChangePassword();
			
			//prefrenceSyncSettings();
		} else {

			preferencesplusImageView.setBackgroundResource(R.drawable.plus);
			setting_prefrences.setVisibility(View.GONE);
			preference_on = false;

			//passwordTextView.setTextColor(getResources().getColor(R.color.Gray));
		//	passwordTextView.setText("password");
		}
	}

	private void prefrenceSyncSettings() {
		// TODO Auto-generated method stub
		
		
		syncSQLiteMySQLDB();
		
		
		
		
		/*sync_group = (RadioGroup) findViewById(R.id.sync_group);
		SharedPreferences preferences = getSharedPreferences("sync", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = preferences.edit();
	
		
		final RadioButton rdSelection=(RadioButton)findViewById(sync_group.getCheckedRadioButtonId());
		int child_index=sync_group.indexOfChild(rdSelection);
		
		if(child_index==0){
			editor.putString("child_index",""+child_index);
			editor.commit();
			
		}else{
			
		}
		
		Toast.makeText(getApplicationContext(), "hello"+child_index, Toast.LENGTH_LONG).show();

		switch(child_index)
		{
		    case 1: 
		        sharedpreferences.putint("",child_index);
		        break;
		    case 2:
		       sharedpreferences.putint("",child_index);
		       break;
		}
		
		
		int selectedId = sync_group.getCheckedRadioButtonId();
		
		 
		// find the radiobutton by returned id
		mPrefSetting = (RadioButton) findViewById(selectedId);
		mPrefSetting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
			}
		});*/
	}
	
	private void syncSQLiteMySQLDB() {
		// TODO Auto-generated method stub
		//Create AsycHttpClient object
		
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		 email1 = loginPreferences.getString("username", "");
		
		prgDialog = new ProgressDialog(this);
	//	prgDialog.setMessage("Synching SQLite Data with Remote MySQL DB. Please wait...");
		prgDialog.setMessage("Sync in Progress. Please wait...");
		prgDialog.setCancelable(false);
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();
			ArrayList<Task>	userList= taskDataBase.getAllTasks(email1);
				if(userList.size()!=0){
					if(taskDataBase.dbSyncCount() != 0){
						prgDialog.show();
						params.put("usersJSON", taskDataBase.composeJSONfromSQLite());
						client.post(AppConfig.SYNC_INSERT,params ,new AsyncHttpResponseHandler() {
						
							//http://192.168.2.4:9000/sqlitemysqlsync/insertuser.php
							@Override
							public void onSuccess(String response) {
								System.out.println(response);
								prgDialog.hide();
								try {
									JSONArray arr = new JSONArray(response);
									System.out.println(arr.length());
									for(int i=0; i<arr.length();i++){
										JSONObject obj = (JSONObject)arr.get(i);
										System.out.println(obj.get("user_id"));
										System.out.println(obj.get("status"));
										taskDataBase.updateSyncStatus(obj.get("user_id").toString(),obj.get("status").toString());
									}
									//Toast.makeText(getApplicationContext(), "Sync completed!", Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									//Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}
				    
							@Override
							public void onFailure(int statusCode, Throwable error,
								String content) {
								// TODO Auto-generated method stub
								prgDialog.hide();
								if(statusCode == 404){
								//	Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
								}else if(statusCode == 500){
								//	Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
								}else{
								//	Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
								}
							}
						});
					}else{
						//Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
					}
				}else{
						//Toast.makeText(getApplicationContext(), "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();
						//Toast.makeText(getApplicationContext(), "No data exist for sync, ", Toast.LENGTH_LONG).show();
				}
			}
	

	/* -------------------profile Data invoke on click with ----------- */
	private void profile_plus_minus() {
		// TODO Auto-generated method stub
		if (profile_on == false) {
			profileplusImageView.setBackgroundResource(R.drawable.minus);
			include_profile_view.setVisibility(View.VISIBLE);
			profile_on = true;
			sync_on = true;
			password_on = true;
			sound_on = true;
			preference_on = true;

			//sync_plus_minus();
			password_plus_minus();
			sound_plus_minus();
			preference_plus_minus();
			profileTextView.setTextColor(getResources().getColor(R.color.Gray));
			profileTextView.setText("profile");
			
			profileSetting();
			
		} else {

			profileplusImageView.setBackgroundResource(R.drawable.plus);
			include_profile_view.setVisibility(View.GONE);
			profile_on = false;

			profileTextView.setTextColor(getResources().getColor(R.color.Gray));
			profileTextView.setText("profile");
		}
	}

	private void profileSetting() {
		// TODO Auto-generated method stub
		
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		 email1 = loginPreferences.getString("username", "");
		 fname1 = loginPreferences.getString("fname", "");
		 lname1 = loginPreferences.getString("lname", "");
		 phone1 = loginPreferences.getString("phone", "");
		
		fname.setText(fname1);
		lname.setText(lname1);
		phone.setText(phone1);
		
		updatemButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				System.out.println("=====email1========"+email1);
				System.out.println("=====fname1========"+fname.getText().toString());
				System.out.println("=====lname1========"+lname.getText().toString());
				System.out.println("=====phone1========"+phone.getText().toString());
				
				params.put("email", email1);
				params.put("fname", fname.getText().toString());
				params.put("lname", lname.getText().toString());
				params.put("phone", phone.getText().toString());
				// Make RESTful webservice call using AsyncHttpClient object
				AsyncHttpClient client = new AsyncHttpClient();

				client.post(AppConfig.URL_UPDATE, params,
						new AsyncHttpResponseHandler() {
							// When the response returned by REST has Http

							public void onStart() {
								super.onStart();
								pDialog = new ProgressDialog(SettingActivity.this);
								pDialog.setMessage("Please wait...");
								pDialog.setCancelable(false);

								pDialog.show();
							}

							// response code '200'
							@Override
							public void onSuccess(String response) {
								// Hide Progress Dialog

								System.out.println("=========response=========="
										+ response);
								pDialog.hide();
								if (pDialog != null) {
									pDialog.dismiss();
								}

								JSONObject json = null;
								try {
									json = new JSONObject(response);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								boolean re = json.optBoolean("result");
								
								
								try {
									// JSONObject jsonResponse = new JSONObject(data);
								        JSONArray jsonarray = json.getJSONArray("result1");
									for(int i=0;i<jsonarray.length();i++){
									JSONObject jsonobject = jsonarray.getJSONObject(i);
									
									String email1=jsonobject.optString("email");
									String fname1=jsonobject.optString("fname");
									String lname1=jsonobject.optString("lname");
									String phone1=jsonobject.optString("phone");
									
									 loginPrefsEditor.putString("fname", fname1);
									 loginPrefsEditor.putString("lname", lname1);
									 loginPrefsEditor.putString("phone", phone1);
									 loginPrefsEditor.commit();
									 
									    fname.setText(fname1);
										lname.setText(lname1);
										phone.setText(phone1);
									
									System.out.println("=====email============"+email1);
									System.out.println("=====fname============"+fname1);
									System.out.println("=====lname============"+lname1);
									System.out.println("=====phone============"+phone1);
									
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if (re == true) {
									Toast.makeText(getApplicationContext(),
											"Your  Profile Succesful Update",
											Toast.LENGTH_LONG).show();
									password_plus_minus();
								

								} 
							

							}

							// When the response returned by REST has Http
							// response code other than '200' such as '404',
							// '500' or '403' etc
							@Override
							public void onFailure(int statusCode, Throwable error,
									String content) {
								// Hide Progress Dialog
								pDialog.hide();
								if (pDialog != null) {
									pDialog.dismiss();
								}
								// When Http response code is '404'
								if (statusCode == 404) {
									Toast.makeText(getApplicationContext(),
											"Connection error", Toast.LENGTH_LONG)
											.show();
								}
								// When Http response code is '500'
								else if (statusCode == 500) {
									Toast.makeText(getApplicationContext(),
											"Internal server error", Toast.LENGTH_LONG)
											.show();
								}
								// When Http response code other than 404, 500
								else {
									Toast.makeText(getApplicationContext(),
											"Connection error ", Toast.LENGTH_LONG)
											.show();

								}
							}
						});
				
			}
		});
		
	}

	private void sync_plus_minus() {
		
		 int selectedId = radioGroup.getCheckedRadioButtonId();
		switch (selectedId) {
		case R.id.sync_on:
	
			syncSQLiteMySQLDB();
			
			break;
		case R.id.sync_off:
			Toast.makeText(getApplicationContext(), "please select sync_on from prefrences ", Toast.LENGTH_SHORT).show();
			
			break;
		default:
			break;
		}
		

	}
	private void password_plus_minus() {
		// TODO Auto-generated method stub
		if (password_on== false) {
			passwordplusImageView.setBackgroundResource(R.drawable.minus);
			include_password_view.setVisibility(View.VISIBLE);
			profile_on = true;
			sync_on = true;
			password_on = true;
			sound_on = true;
			preference_on = true;

			//sync_plus_minus();
			profile_plus_minus();
			sound_plus_minus();
			preference_plus_minus();
			passwordTextView.setTextColor(getResources().getColor(R.color.Gray));
			passwordTextView.setText("password");
			
			ChangePassword();
		} else {

			passwordplusImageView.setBackgroundResource(R.drawable.plus);
			include_password_view.setVisibility(View.GONE);
			password_on = false;

			passwordTextView.setTextColor(getResources().getColor(R.color.Gray));
			passwordTextView.setText("password");
		}
	}


	
	private void ChangePassword() {
		// TODO Auto-generated method stub
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		 email1 = loginPreferences.getString("username", "");
		
		 mChangePasswordButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 params.put("email", email1);
				 params.put("pass", mOldPassword.getText().toString());
				 params.put("password", mNewPassword.getText().toString());
					
				// Make RESTful webservice call using AsyncHttpClient object
				AsyncHttpClient client = new AsyncHttpClient();

				client.post(AppConfig.URL_CHANGE_PASSWORD, params,
						new AsyncHttpResponseHandler() {
							// When the response returned by REST has Http

							public void onStart() {
								super.onStart();
								pDialog = new ProgressDialog(SettingActivity.this);
								pDialog.setMessage("Please wait...");
								pDialog.setCancelable(false);

								pDialog.show();
							}

							// response code '200'
							@Override
							public void onSuccess(String response) {
								// Hide Progress Dialog

								System.out.println("=========response=========="
										+ response);
								pDialog.hide();
								if (pDialog != null) {
									pDialog.dismiss();
								}

								JSONObject json = null;
								try {
									json = new JSONObject(response);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								boolean re = json.optBoolean("result");
								
								
							/*	try {
									// JSONObject jsonResponse = new JSONObject(data);
								        JSONArray jsonarray = json.getJSONArray("result1");
									for(int i=0;i<jsonarray.length();i++){
									JSONObject jsonobject = jsonarray.getJSONObject(i);
									
									String email1=jsonobject.optString("email");
									String fname1=jsonobject.optString("fname");
									String lname1=jsonobject.optString("lname");
									String phone1=jsonobject.optString("phone");
									
									 loginPrefsEditor.putString("fname", fname1);
									 loginPrefsEditor.putString("lname", lname1);
									 loginPrefsEditor.putString("phone", phone1);
									 loginPrefsEditor.commit();
									 
									    fname.setText(fname1);
										lname.setText(lname1);
										phone.setText(phone1);
									
									System.out.println("=====email============"+email1);
									System.out.println("=====fname============"+fname1);
									System.out.println("=====lname============"+lname1);
									System.out.println("=====phone============"+phone1);
									
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}*/
								
								if (re == true) {
									Toast.makeText(getApplicationContext(),
											"Your  Password Succesful Updated",
											Toast.LENGTH_LONG).show();
									
								password_plus_minus();

								} 
							

							}

							// When the response returned by REST has Http
							// response code other than '200' such as '404',
							// '500' or '403' etc
							@Override
							public void onFailure(int statusCode, Throwable error,
									String content) {
								// Hide Progress Dialog
								pDialog.hide();
								if (pDialog != null) {
									pDialog.dismiss();
								}
								// When Http response code is '404'
								if (statusCode == 404) {
									Toast.makeText(getApplicationContext(),
											"Connection error", Toast.LENGTH_LONG)
											.show();
								}
								// When Http response code is '500'
								else if (statusCode == 500) {
									Toast.makeText(getApplicationContext(),
											"Internal server error", Toast.LENGTH_LONG)
											.show();
								}
								// When Http response code other than 404, 500
								else {
									Toast.makeText(getApplicationContext(),
											"Connection error ", Toast.LENGTH_LONG)
											.show();

								}
							}
						});

			}
		});
		}
	
		 
		 
	

	private void preference_plus_minus() {

	}

	private void sound_plus_minus() {
		// TODO Auto-generated method stub
		if (sound_on == false) {
			soundplusImageView.setBackgroundResource(R.drawable.minus);
			include_sound_view.setVisibility(View.VISIBLE);
			profile_on = true;
			sync_on = true;
			password_on = true;
			sound_on = true;
			preference_on = true;

			//sync_plus_minus();
			password_plus_minus();
			profile_plus_minus();
			preference_plus_minus();
			soundTextView.setTextColor(getResources().getColor(R.color.Gray));
			soundTextView.setText("sound");
			
		   SoundSetting();
			
		} else {

			soundplusImageView.setBackgroundResource(R.drawable.plus);
			include_sound_view.setVisibility(View.GONE);
			sound_on = false;

			soundTextView.setTextColor(getResources().getColor(R.color.Gray));
			soundTextView.setText("sound");
		}
	}

	private void SoundSetting() {
		// TODO Auto-generated method stub
	    
        //get the audio manager
        amanager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        
        //seek bar settings//
        //sets the range between 0 and the max volume
        mSeekbar.setMax(amanager.getStreamMaxVolume(AudioManager.STREAM_RING));
        //set the seek bar progress to 1
        mSeekbar.setKeyProgressIncrement(1);
        
        //sets the progress of the seek bar based on the system's volume
        mSeekbar.setProgress(amanager.getStreamVolume(AudioManager.STREAM_RING));
        
        //register OnSeekBarChangeListener, so that the seek bar can change the volume
        mSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		{
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) 
			{	
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) 
			{
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				//change the volume, displaying a toast message containing the current volume and playing a feedback sound
				amanager.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
			}
		});  
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		//if one of the volume keys were pressed
		if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)
		{
			//change the seek bar progress indicator position
			mSeekbar.setProgress(amanager.getStreamVolume(AudioManager.STREAM_RING));
		}
		//propagate the key event
		return super.onKeyDown(keyCode, event);
	}

	public void get_profile_info() {
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		String user_name = loginPreferences.getString("username", "");
		String user_pass = loginPreferences.getString("password", "");

	System.out.println("===========user_name=========" + user_name);

		/*
		 * Toast.makeText(getApplicationContext(), "Email=" + user_name +
		 * "password=" + user_pass, 300).show();
		 */

		params.put("email", user_name);

		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();

		client.post(AppConfig.URL_LOGIN, params,
				new AsyncHttpResponseHandler() {
					// When the response returned by REST has Http

					public void onStart() {
						super.onStart();
						pDialog = new ProgressDialog(SettingActivity.this);
						pDialog.setMessage("Please wait...");
						pDialog.setCancelable(false);

						pDialog.show();
					}

					// response code '200'
					@Override
					public void onSuccess(String response) {
						// Hide Progress Dialog

						System.out.println("=========response=========="
								+ response);
						pDialog.hide();
						if (pDialog != null) {
							pDialog.dismiss();
						}

						JSONObject json = null;
						try {
							json = new JSONObject(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					// When the response returned by REST has Http
					// response code other than '200' such as '404',
					// '500' or '403' etc
					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// Hide Progress Dialog
						pDialog.hide();
						if (pDialog != null) {
							pDialog.dismiss();
						}
						// When Http response code is '404'
						if (statusCode == 404) {
							Toast.makeText(getApplicationContext(),
									"Connection error", Toast.LENGTH_LONG)
									.show();
						}
						// When Http response code is '500'
						else if (statusCode == 500) {
							Toast.makeText(getApplicationContext(),
									"Internal server error", Toast.LENGTH_LONG)
									.show();
						}
						// When Http response code other than 404, 500
						else {
							Toast.makeText(getApplicationContext(),
									"Connection error ", Toast.LENGTH_LONG)
									.show();

						}
					}
				});
	}

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, SettingActivity.class));
    }

    @Override
    public void onLoginFinished(boolean successful) {
        if (successful) {
            finish();
        } else {
            mSyncevernote.setEnabled(true);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EvernoteSession.REQUEST_CODE_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    // handle success
                    System.out.println("======Suceess===============");


                    for(Task task:taskData) {


                        noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

                        Note note = new Note();
                        note.setTitle(task.getTitle());
                        // note.setContent("hello");
                        note.setContent(EvernoteUtil.NOTE_PREFIX + task.getDescription() + EvernoteUtil.NOTE_SUFFIX);
                        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
                            @Override
                            public void onSuccess(Note note) {
                               // Toast.makeText(getApplicationContext(), note.getTitle() + " has been created", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onException(Exception exception) {
                                Log.e("Logtag", "Error creating note", exception);
                            }
                        });
                    }
                } else {
                    // handle failure
                    System.out.println("======Fail===============");
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
}}
