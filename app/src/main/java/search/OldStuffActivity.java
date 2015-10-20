package search;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.life_reminder.HomeActivity;
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

public class OldStuffActivity extends Activity implements OnItemClickListener {
	
	private ListView mOldStufflistview;
	private TaskListDataBase taskDataBase;
	private Task task;
	private TaskListArray tasksList;
	Context context = this;
	List<Task> Oldtask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current);
		custom_actionbar();
		mOldStufflistview=(ListView)findViewById(R.id.m_old_stuff_list);
		
		tasksList = TaskListArray.getInstance(context); // Creating the tasks
		// list
		taskDataBase = TaskListDataBase.getInstance(context);
		tasksList.setTasks(taskDataBase.getOldStuff()); // Setting the tasks
														// from database
		
		
		Oldtask = taskDataBase.getOldStuff();
		
		OldStuffAdapter adapter = new OldStuffAdapter(this, Oldtask);
		mOldStufflistview.setAdapter(adapter);
		mOldStufflistview.setOnItemClickListener(this);
		
		
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
		action_bar_title.setText("Search");
		action_bar_img.setImageResource(R.drawable.home_search_icon);

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
			Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		 long eventId=Oldtask.get(position).getId();

		  String title=Oldtask.get(position).getTitle();
		 
		
			  String description=Oldtask.get(position).getDescription();
			  System.out.println("=======description================"+description);
       
	  
		
			
		
		 
		 String location=Oldtask.get(position).getLocation();
		 
		 String importance=Oldtask.get(position).getImportance();
		 
		 String event=Oldtask.get(position).getEvent();
		 
		 String audio=Oldtask.get(position).getEvent_Audio();
		 
		 String photo=Oldtask.get(position).getEvent_photo();
		 
		 String tag=Oldtask.get(position).getEvent_tag();
		 
		 String etc=Oldtask.get(position).getEtc();

		 String compose=Oldtask.get(position).getCompose();

		 String name=Oldtask.get(position).getName();

		 String notes=Oldtask.get(position).getNotes();
		 
		 String sex=Oldtask.get(position).getSex();
		 
		 String mail=Oldtask.get(position).getTo_mail();
		 
		 String life=Oldtask.get(position).getLife();
		 
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
			 
			 Intent mEvent=new Intent(OldStuffActivity.this, EventActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent);
		 }
		 }
		 if(mail!=null){
		 if(isEmailValid(mail)){
			 Intent mEvent=new Intent(OldStuffActivity.this, EmailActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent); 
		 }
		 }

		 if(name!=null){
        if(name.equals("Name")){
			 
			 Intent mEvent=new Intent(OldStuffActivity.this, FacesActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent);
		 }
        
		 }
		 if(notes!=null){
        if(notes.equals("NOTES")){
			 
			 Intent mEvent=new Intent(OldStuffActivity.this, PhoneActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent);
		 }
		 }
		 if(etc!=null){
         if(etc.equals("etc")){
			 
			 Intent mEvent=new Intent(OldStuffActivity.this, NewEtcActivity.class);
			 mEvent.putExtra("taskId", eventId);
			 startActivity(mEvent);
		 }
         }
         
		    if(life!=null){
             if(life.equals("life")){
			 
			 Intent mEvent=new Intent(OldStuffActivity.this, LifeActivity.class);
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

	

}
