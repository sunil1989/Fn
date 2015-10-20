package alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CancelAlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		long id = 0;
		id = intent.getLongExtra("taskId", -1); // Getting the task id that supposed to be reminded
		System.out.println("cancel id="+id);
		SetReminder reminder=new SetReminder();
		reminder.cancelReminder(context, id);
		//Toast.makeText(context, "---------cancel alarm -----------", 300).show();
		
		  /*PendingIntent pendingIntent = intent.getParcelableExtra("key");
	        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	        am.cancel(pendingIntent);*/
	}

}
