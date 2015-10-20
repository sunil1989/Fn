package alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.life_reminder.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;
import email.EmailActivity;
import etc.NewEtcActivity;
import event.EventActivity;
import faces.FacesActivity;
import fn.LifeActivity;
import phone.PhoneActivity;

public class StatusBar extends BroadcastReceiver
{
	private NotificationManager nm;
	//List<Task> taskData;
	//private TaskListDataBase taskDataBase;
	//private Task task;
	//private TaskListArray tasksList;
	String email1;
	Intent newIntent;
	// private SharedPreferences loginPreferences;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		long id = 0;
		/*taskDataBase = TaskListDataBase.getInstance(context);
		loginPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
		 email1 = loginPreferences.getString("username", "");
		taskData = taskDataBase.getAllTasks(email1);
		*/
		
		Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
		TaskListDataBase taskDataBase = TaskListDataBase.getInstance(context); // Getting an instance of the application SQLite database
		TaskListArray tasksList = TaskListArray.getInstance(context); // Creating the tasks list
		
		id = intent.getLongExtra("taskId", -1); // Getting the task id that supposed to be reminded
		Task task = (Task) taskDataBase.getTask(id); // Getting this task from the database
		String event_tag=  task.getEvent();
		String mail_tag=task.getTo_mail();
		String name_Tag=task.getName();
		String notes_tag=task.getNotes();
		String etc_tag=task.getEtc();
	
		String life_tag=task.getLife();
	
		task.setHasReminder(0);
		tasksList.updateTask(task, id);
		
		System.out.println("========id==nnn====="+id);
		
		System.out.println("========event====="+event_tag);
		
		System.out.println("========mail_tag====="+mail_tag);
		
		System.out.println("========name_Tag====="+name_Tag);
		
		System.out.println("========etc_tag====="+etc_tag);
		
		System.out.println("========notes_tag====="+notes_tag);
		
		System.out.println("========life_tag====="+life_tag);
		
		 if(event_tag!=null){
			 if(event_tag.equals("event")){
				 
				 newIntent=new Intent(context, EventActivity.class);
				 //mEvent.putExtra("taskId", eventId);
				// startActivity(mEvent);
			 }
			 }
			 if(mail_tag!=null){
			 if(isEmailValid(mail_tag)){
				 newIntent=new Intent(context, EmailActivity.class);
				// mEvent.putExtra("taskId", eventId);
				// startActivity(mEvent); 
			 }
			 }

			 if(name_Tag!=null){
	        if(name_Tag.equals("Name")){
				 
	        	newIntent=new Intent(context, FacesActivity.class);
				// mEvent.putExtra("taskId", eventId);
				// startActivity(mEvent);
			 }
	        
			 }
			 if(notes_tag!=null){
	        if(notes_tag.equals("NOTES")){
				 
	        	newIntent=new Intent(context, PhoneActivity.class);
				// mEvent.putExtra("taskId", eventId);
				// startActivity(mEvent);
			 }
			 }
			 if(etc_tag!=null){
	         if(etc_tag.equals("etc")){
				 
	        	 newIntent=new Intent(context, NewEtcActivity.class);
				 //mEvent.putExtra("taskId", eventId);
				// startActivity(mEvent);
			 }
	         }
	         
			    if(life_tag!=null){
	             if(life_tag.equals("life")){
				 
	            	 newIntent=new Intent(context, LifeActivity.class);
				// mEvent.putExtra("taskId", eventId);
				 //startActivity(mEvent);
			 }
			    }
		
		
		//Intent newIntent = new Intent(context, HomeActivity.class); // Creating an Intent to start the ToDoApplication
		newIntent.putExtra("taskId", id); // Adding the bundle to the intent
		newIntent.putExtra("dialogid", 5)	;
		newIntent.putExtra("snooze_dialogid", 6)	;
		PendingIntent pi = PendingIntent.getActivity(context, (int)task.getId(), newIntent, 0); // Wrapping the intent with PendingIntent to set in the notification
		
		nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // Getting the Notification Manager
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		
		builder.setContentTitle(task.getTitle())
				.setContentText(task.getDescription())
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setContentIntent(pi)
				.setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon(icon)
				.setSound(Uri.parse("android.resource://il.ac.shenkar.classproject/"+ R.raw.bell_ringing));
		
		Notification newNotification = builder.build(); // Building the new notification with the task details
		newNotification.flags |= Notification.FLAG_AUTO_CANCEL; // Dismiss the notification after been selected
		nm.notify((int)task.getId(), newNotification); // Adding the new notification to the Notification Manager with the task id
	}
	private boolean isEmailValid(String mail_tag) {
		// TODO Auto-generated method stub
		Pattern pattern;
	    Matcher matcher;
	    String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(mail_tag);
	    return matcher.matches();
	}
}
