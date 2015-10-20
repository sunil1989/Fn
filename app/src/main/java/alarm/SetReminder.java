package alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import java.util.Calendar;

import data.Task;


public class SetReminder
{
	
	
	public SetReminder()
	{

	}

	// Method for setting a proximity alert
	public void setProximityAlert(Context context, Task task, double latitude, double longitude)
	{
		Intent intent = new Intent(context, StatusBar.class); // Creating new intent to active with the reminder
		intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
	    PendingIntent pi = PendingIntent.getBroadcast(context, ((int)task.getId())+500, intent, 0); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id+500(not to overwrite the reminder), so multiply reminders is available 
		
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); // Getting the location manager		
		lm.addProximityAlert(latitude, longitude, 300, -1, pi);
		
		Log.i("SetReminder", "Proximity alert for task " + task.getId() + " was added.");
	}
	
	// Method for canceling proximity alert
	public void cancelProximityAlert(Context context, long id)
	{
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); // Getting the location manager
		
		PendingIntent	pi = PendingIntent.getBroadcast(context, ((int)id)+500, new Intent(context, StatusBar.class), 0);
		lm.removeProximityAlert(pi);
		
		Log.i("SetReminder", "Proximity alert for task " + id + " was canceled.");
	}
	
	// Method for setting a one time reminder
	public void setOneTimeReminder(Context context, Calendar cal, Task task)
	{
		Intent intent = new Intent(context, StatusBar.class); // Creating new intent to active with the reminder
		intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
		PendingIntent pi = PendingIntent.getBroadcast(context, (int)task.getId(), intent, 0); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 

		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Adding the alarm manager the pending intent
		 
		Log.i("SetReminder", "Reminder for task " + task.getId() + " was added.");
	}
	
	// Method for canceling a reminder according to the PenidingIntent ID
	public void cancelReminder(Context context, long id)
	{
		AlarmManager	 am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
		
	PendingIntent	 pi = PendingIntent.getBroadcast(context, (int)id, new Intent(context, StatusBar.class), 0);
		am.cancel(pi);
		
		Log.i("SetReminder", "Reminder for task " + id + " was canceled.");
	}
	
	// Method for setting an automatic action with an interval
	public void setAutomaticTaskResolver(Context context, PendingIntent pi, long intervalMillis, boolean cancel)
	{
		Calendar calendar = Calendar.getInstance();
	    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
		
		if (cancel) // Checking if the method was called for canceling the repeating alarm
		{
			am.cancel(pi); // Canceling the alarm
			return;
		}
		
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pi);
	}
	
	// Method for setting a repete time reminder
		public void setRepeatTimeReminder(Context context, Calendar cal, Task task,long intervalMillis)
		{
		
		
			
			
			Intent intent = new Intent(context, StatusBar.class); // Creating new intent to active with the reminder
			intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
		   PendingIntent	 pi = PendingIntent.getBroadcast(context, (int)task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 
			
		AlarmManager	 am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
		//	am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Adding the alarm manager the pending intent
			/* am.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
	                    cal.getTimeInMillis(), intervalMillis, pi);*/
			 am.setRepeating(AlarmManager.RTC_WAKEUP,  
	                    cal.getTimeInMillis(), intervalMillis, pi);
			Log.i("SetReminder", "Reminder for task " + task.getId() + " was added.");
		}
		
		
		
		
		// Method for setting a repete time reminder
				public void setRepeatTimeReminder1(Context context, Calendar cal, Task task,long intervalMillis,int timeid)
				{
				
					
					
					
					Intent intent = new Intent(context, StatusBar.class); // Creating new intent to active with the reminder
			
					   PendingIntent	 pi = PendingIntent.getBroadcast(context, (int)task.getId(), intent, PendingIntent.FLAG_NO_CREATE); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 
					
				  
				AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
				//	am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Adding the alarm manager the pending intent
					 am.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
			                    cal.getTimeInMillis(), intervalMillis, pi);
				 
				   if (pi != null) {
					    // Now cancel the alarm that matches the old PendingIntent
					    am.cancel(pi);
					  
						 pi.cancel();
					    System.out.println("===============match======");
					}
				   
					System.out.println("=====taskid============"+ task.getId());
					
				   
					
					 intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
					  pi = PendingIntent.getBroadcast(context, (int)task.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 
				
					 
					 System.out.println("=====current date============"+ cal.getTimeInMillis());
					 am.setRepeating(AlarmManager.RTC_WAKEUP,  
			                    cal.getTimeInMillis(), intervalMillis, pi);
					 
					 
					// am.cancel(pi);
					// pi.cancel();
					Log.i("SetReminder", "Reminder for task " + task.getId() + " was added.");
				}
		
		// Method for setting a one time reminder
				public void setRepeatweekReminder(Context context, Calendar cal, Task task, int j,long intervalMillis)
				{
					
					System.out.println("=====j========"+j);
					
					Intent intent = new Intent(context, StatusBar.class); // Creating new intent to active with the reminder
					intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
				PendingIntent	 pi = PendingIntent.getBroadcast(context, j, intent, PendingIntent.FLAG_UPDATE_CURRENT); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 
					
				AlarmManager	 am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
				//	am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Adding the alarm manager the pending intent
					/* am.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
			                    cal.getTimeInMillis(), intervalMillis, pi);*/
					 am.setRepeating(AlarmManager.RTC_WAKEUP,  
			                    cal.getTimeInMillis(), intervalMillis, pi);
					//Log.i("SetReminder", "Reminder for task " + task.getId() + " was added.");
				}
		
// Method for alarm contnue until
		/*public void set_alarm_untill(Context context,Calendar timeOff )
		{
		Intent cancellationIntent = new Intent(context, CancelAlarmBroadcastReceiver.class);
		cancellationIntent.putExtra("key", pi);
		PendingIntent cancellationPendingIntent = PendingIntent.getBroadcast(context, 0, cancellationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, timeOff.getTimeInMillis(), cancellationPendingIntent);
		}*/
		// Method for setting a one time reminder
		public void setCanceltimeReminder(Context context, Calendar cal, Task task)
		{
			Intent intent = new Intent(context, CancelAlarmBroadcastReceiver.class); // Creating new intent to active with the reminder
			intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
			
			PendingIntent pi = PendingIntent.getBroadcast(context, (int)task.getId(), intent, 0); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 

			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Adding the alarm manager the pending intent
			 
			Log.i("SetReminder", "Reminder for task " + task.getId() + " was added.");
		}
		
		
		
		public void setCancelDailyReminder(Context context, Calendar cal, Task task)
		{
			Intent intent = new Intent(context, CancelAlarmBroadcastReceiver.class); // Creating new intent to active with the reminder
			intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
			
			PendingIntent pi = PendingIntent.getBroadcast(context, (int)task.getId(), intent, 0); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 

			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Adding the alarm manager the pending intent
			 
			Log.i("SetReminder", "Reminder for task " + task.getId() + " was added.");
		}
		
		
		
		// Method for canceling a reminder according to the PenidingIntent ID
		public void canceldailyReminder(Context context, int id)
		{
			AlarmManager	 am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
			
		PendingIntent	 pi = PendingIntent.getBroadcast(context, id, new Intent(context, StatusBar.class), 0);
	    am.cancel(pi);
			
			
			/*Intent intent = new Intent(context, CancelAlarmBroadcastReceiver.class);
			intent.putExtra("taskId", id);
			PendingIntent sender = PendingIntent.getBroadcast(context, (int)id, intent,
			        PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			am.cancel(sender);*/
			
			Log.i("SetReminder", "Reminder for task " + id + " was canceled.");
			
			//PendingIntent.getBroadcast(context, 0, am,PendingIntent.FLAG_UPDATE_CURRENT).cancel();
		}

		public void setSnooze(Context context, Calendar cal, Task task,
				long interval, int s) {
			// TODO Auto-generated method stub
			
			
			System.out.println("=========s================"+s);
			Intent intent = new Intent(context, StatusBar.class); // Creating new intent to active with the reminder
			intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
		  PendingIntent	 pi = PendingIntent.getBroadcast(context, s, intent, PendingIntent.FLAG_UPDATE_CURRENT); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 
			
		 AlarmManager	 am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
		//am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Adding the alarm manager the pending intent
			// am.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
	          //          cal.getTimeInMillis(), intervalMillis, pi);*/
			 am.setRepeating(AlarmManager.RTC_WAKEUP,  
	                  cal.getTimeInMillis(), interval, pi);
			Log.i("SetReminder", "Reminder for task " + task.getId() + " was added.");
			
			
			/* PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
		                s, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		            AlarmManager am =
		                (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		            am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
		            		interval,pendingIntent);*/
		}

		

		/*public void setRepeatTimeReminder11(Context context, Calendar cal,
				Task newTask, long updateid, long interval) {
			// TODO Auto-generated method stub
			
			Intent intent = new Intent(context, StatusBar.class); // Creating new intent to active with the reminder
			intent.putExtra("taskId", updateid); // Adding to the indent the task id to remind
		   PendingIntent	 pi = PendingIntent.getBroadcast(context, (int)updateid, intent, PendingIntent.FLAG_UPDATE_CURRENT); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 
			
		AlarmManager	 am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
		//	am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Adding the alarm manager the pending intent
			 am.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
	                    cal.getTimeInMillis(), intervalMillis, pi);
			 am.setRepeating(AlarmManager.RTC_WAKEUP,  
	                    cal.getTimeInMillis(), interval, pi);
			Log.i("SetReminder", "Reminder for task " + updateid+ " was added.");
			
		}*/
		
}
