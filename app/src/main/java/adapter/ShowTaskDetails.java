package adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.life_reminder.R;

import alarm.SetReminder;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;

public class ShowTaskDetails extends FragmentActivity 
		 {
	private Context context;

	// Variables for the DatePicker
	//private DatePickerFragment newDateFragment = null;
	private int year = -1, month = -1, day = -1;

	// Variables for the TimePicker
	//private TimePickerFragment newTimeFragment = null;
	private int hour = -1, minute = -1;

	private TextView newDateSeted;
	private TextView newTimeSeted;

	private SetReminder setReminder;
	private TaskListArray array;
	private TaskListDataBase taskDataBase;
	private Task task;

	private TextView reminder;
	private TextView setNewReminder;
	private TextView proximityTitle;

	private EditText title;
	private EditText description;
	private EditText dateCreated;
	private EditText showReminder;
	private EditText proximity;

	private Button edit;
	private Button setNewTime;
	private Button setNewDate;

	private RelativeLayout activityLayout;
	private boolean isEditing;
	private boolean checked;

	private ShareActionProvider myShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_ACTION_BAR);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_task_details); // Setting the UI view with
													// show_task_details
		context = getBaseContext();

		

		array = TaskListArray.getInstance(getBaseContext());
		taskDataBase = TaskListDataBase.getInstance(getBaseContext()); // Creating
																		// the
																		// tasks
																		// list
		task = new Task();

		long id = getIntent().getLongExtra("taskId", -1);
		task = taskDataBase.getTask(id); // Getting the task from the database
											// according to the ID
		
		
		String event= task.getTitle();
		
		System.out.println("========title======"+event);

		//title.setText(task.getTitle());
		//description.setText(task.getDescription());
		//dateCreated.setText(task.getCreationDateString());
		
		System.out.println("==============Task id==============="+id);
		
		

		
	}

	@Override
	public void onStart() {
		super.onStart();
		//EasyTracker.getInstance().setContext(getBaseContext());
	//	EasyTracker.getInstance().activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		//EasyTracker.getInstance().activityStop(this); // Add this method.
	}

}
