package search;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.life_reminder.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.OldStuffAdapter;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;
import email.EmailActivity;
import etc.NewEtcActivity;
import event.EventActivity;
import faces.FacesActivity;
import fn.LifeActivity;
import phone.PhoneActivity;

public class CurrentActivity extends Activity implements OnItemClickListener {
	private ListView currentListview;
	private TaskListDataBase taskDataBase;
	private Task task;
	private TaskListArray tasksList;
	Context context = this;
	List<Task> currentdata;
	
	 
	 private SharedPreferences loginPreferences;
	    private SharedPreferences.Editor loginPrefsEditor;
	    String email1;
	    TextView noresult_textview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current);
		custom_actionbar();
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		 email1 = loginPreferences.getString("username", "");
			noresult_textview=(TextView)findViewById(R.id.noresult_text);
		currentListview=(ListView)findViewById(R.id.m_old_stuff_list);
	  
	   tasksList = TaskListArray.getInstance(context); // Creating the tasks
		// list
		taskDataBase = TaskListDataBase.getInstance(context);
		tasksList.setTasks(taskDataBase.getAllTasks(email1)); // Setting the tasks
														// from database
		
		currentdata= taskDataBase.getAllTasks(email1);
	  
		
		Intent in= getIntent();
		
		String name= in.getStringExtra("name");
		
		
		
		if(SearchActivity.currentTask.size()!=0){
			OldStuffAdapter adapter = new OldStuffAdapter(CurrentActivity.this, SearchActivity.currentTask);
			  currentListview.setAdapter(adapter);
			  currentListview.setOnItemClickListener(this);
			
		}
		
		else{
			
			SearchActivity.currentTask = taskDataBase.getSearchName(name);
			
			/*OldStuffAdapter	 adapter = new OldStuffAdapter(this, SearchActivity.currentTask);
			  currentListview.setAdapter(adapter);
			  currentListview.setOnItemClickListener(this);
			*/
			noresult_textview.setVisibility(View.VISIBLE);

		//	Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
		}
		
	
		
	/*	tasksList = TaskListArray.getInstance(context); // Creating the tasks
		// list
		taskDataBase = TaskListDataBase.getInstance(context);
		tasksList.setTasks(taskDataBase.getOldStuff()); // Setting the tasks
														// from database
		
		
		Oldtask = taskDataBase.getOldStuff();
		
		OldStuffAdapter	 adapter = new OldStuffAdapter(this, Oldtask);
		mOldStufflistview.setAdapter(adapter);
	//	mOldStufflistview.setOnItemClickListener(this);
*/	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
		
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
			//Toast.makeText(getApplicationContext(), "text", 300).show();
			}
		});

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		 long eventId=SearchActivity.currentTask.get(position).getId();

		  String title=SearchActivity.currentTask.get(position).getTitle();
		 
		
			  String description=SearchActivity.currentTask.get(position).getDescription();
			  System.out.println("=======description================"+description);
      
	  
		
			
		
		 
		 String location=SearchActivity.currentTask.get(position).getLocation();
		 
		 String importance=SearchActivity.currentTask.get(position).getImportance();
		 
		 String event=SearchActivity.currentTask.get(position).getEvent();
		 
		 String audio=SearchActivity.currentTask.get(position).getEvent_Audio();
		 
		 String photo=SearchActivity.currentTask.get(position).getEvent_photo();
		 
		 String tag=SearchActivity.currentTask.get(position).getEvent_tag();
		 
		 String etc=SearchActivity.currentTask.get(position).getEtc();

		 String compose=SearchActivity.currentTask.get(position).getCompose();

		 String name=SearchActivity.currentTask.get(position).getName();

		 String notes=SearchActivity.currentTask.get(position).getNotes();
		 
		 String sex=SearchActivity.currentTask.get(position).getSex();
		 
		 String mail=SearchActivity.currentTask.get(position).getTo_mail();
		 
		 String life=SearchActivity.currentTask.get(position).getLife();
		 
		 System.out.println("=======eventId================"+eventId);
		 
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
			 
			 Intent mEvent=new Intent(CurrentActivity.this, EventActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent);
		 }
		 }
		 if(mail!=null){
		 if(isEmailValid(mail)){
			 Intent mEvent=new Intent(CurrentActivity.this, EmailActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent); 
		 }
		 }

		 if(name!=null){
       if(name.equals("Name")){
			 
			 Intent mEvent=new Intent(CurrentActivity.this, FacesActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent);
		 }
       
		 }
		 if(notes!=null){
       if(notes.equals("NOTES")){
			 
			 Intent mEvent=new Intent(CurrentActivity.this, PhoneActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent);
		 }
		 }
		 if(etc!=null){
        if(etc.equals("etc")){
			 
			 Intent mEvent=new Intent(CurrentActivity.this, NewEtcActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent);
		 }
        }
        
		    if(life!=null){
            if(life.equals("life")){
			 
			 Intent mEvent=new Intent(CurrentActivity.this, LifeActivity.class);
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
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	SearchActivity.filterdata.clear();
}
}
