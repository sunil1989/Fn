package sync;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.AppConfig;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;

public class StartService extends Service {

Timer timer = new Timer();
private final int TIME_INTERVAL = 120000;
//GPSTracker GPSTracker;
Double latitude  ;
Double longitude ;
RequestParams params = new RequestParams();
private ProgressDialog pDialog;
 private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

private TaskListDataBase taskDataBase;
		private Task task;
		private TaskListArray tasksList;
		Context context = this;
		
		
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
		

@Override
public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
}

@Override
public void onCreate() {
    // TODO Auto-generated method stub
    super.onCreate();
  //  GPSTracker = new GPSTracker(StartService.this);
    loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
    tasksList = TaskListArray.getInstance(context); // Creating the tasks
	// list
	taskDataBase = TaskListDataBase.getInstance(context);
}

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    // TODO Auto-generated method stub
    doTimerThings();
    return super.onStartCommand(intent, flags, startId);
}

private void doTimerThings() 
{
    timer.scheduleAtFixedRate(new TimerTask() {

        @SuppressLint("DefaultLocale")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void run() {



           // latitude = GPSTracker.getLatitude();
           // longitude = GPSTracker.getLongitude();

            // you get the lat and lng , do your server stuff here-----

            System.out.println("lat------ "+latitude);
            System.out.println("lng-------- "+longitude);
            
        	syncSQLiteMySQLDB();
        }

    }, 0, TIME_INTERVAL);

}






@Override
public void onDestroy() {
    super.onDestroy();
}

protected void syncSQLiteMySQLDB() {
	// TODO Auto-generated method stub
	loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
	 email1 = loginPreferences.getString("username", "");
	 
		
	
	//prgDialog = new ProgressDialog(this);
//	prgDialog.setMessage("Synching SQLite Data with Remote MySQL DB. Please wait...");
	//prgDialog.setMessage("Sync in Progress. Please wait...");
//	prgDialog.setCancelable(false);
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
		ArrayList<Task>	userList= taskDataBase.getAllTasks(email1);
			if(userList.size()!=0){
				if(taskDataBase.dbSyncCount() != 0){
					//prgDialog.show();
					params.put("usersJSON", taskDataBase.composeJSONfromSQLite());
					client.post(AppConfig.SYNC_INSERT,params ,new AsyncHttpResponseHandler() {
					
						//http://192.168.2.4:9000/sqlitemysqlsync/insertuser.php
						@Override
						public void onSuccess(String response) {
							System.out.println(response);
							//prgDialog.hide();
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
						//	prgDialog.hide();
							if(statusCode == 404){
								//Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
							}else if(statusCode == 500){
								//Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
							}else{
								//Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
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

}