package fn;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.life_reminder.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import adapter.Audio_horizontal_Adapter;
import adapter.Priority_dialog_adapter;
import adapter.horizontal_Adapter;
import alarm.SetReminder;
import camerawork.ImageCaptureActivity;
import camerawork.ImageEditor;
import data.AudioData;
import data.PhotoData;
import data.Task;
import data.TaskListArray;
import data.TaskListDataBase;
import it.sephiroth.android.library.widget.HListView;

//import android.view.inputmethod.InputMethodManager;

public class LifeActivity extends Activity implements OnClickListener,
		OnPreparedListener,
		it.sephiroth.android.library.widget.AdapterView.OnItemClickListener {

	long rowID;
	LinearLayout audio_image_layout;
	ArrayList<Uri> imageUris, audioUris;
	String img_path;
	adapter.horizontal_Adapter horizont;
	Audio_horizontal_Adapter audio_horizontal_Adapter;
	ArrayList<PhotoData> img_ArrayList;
	ArrayList<AudioData> audio_ArrayList;
	long timeinmilisecod;

	Activity activity;
	HListView horizontallistview, audio_horizontallistview;
	int img_position;
	/* share whole note */
	Uri imageuri, audiouri;
	boolean set_time;
	StringBuilder str;
	static long interval;
	private TaskListArray array;
	private Task task;
	private String tag1;
	long updateid;
	int targetWidth = 140;
	int targetHeight = 140;
	/* ------------------Remember view-------------------- */

	LinearLayout remember_LinearLayout, inner_rememberLayout;
	ImageView remember_plusImageView;
	EditText rememberEditText;
	TextView life_remember_TextView;
	boolean remember_on;
	/* ------------------Remember view End- ------------------- */
	/* ------------------This way-------------------- */

	LinearLayout thisway_LinearLayout;
	ImageView thisway_plusImageView;
	TextView life_thisway_TextView;
	boolean thisway_on;
	View include_thisway_view;
	/* ------------------Remember view End- ------------------- */
	/* Bottom Section view */
	Dialog alarm_dialog;
	Dialog importance_dialog;
	ImageView priority_ImageView, alarm_ImageView;
	TextView alarm_hour_minuteTextView;
	TextView alarm_minute_text, alarm_hour_text;
	int alarm_final_value;
	boolean minute_hour_bool;
	/* Bottom Section */

	// test
	Button playbutton;
	boolean click;
	public byte[] fileByteArray;
	// value
	String remember_text;

	ArrayList<String> tag_ArrayList = new ArrayList<String>();

	/* Slice */

	LinearLayout recall_layout;
	ImageView recall_plus_img;
	View include_recall_view;
	TextView life_recall_text;
	boolean recall_on;

	EditText tags_editText;
	TextView save_Button;
	LinearLayout linearLayout, ll;
	int i = 0;
	private String importance;

	/*----------------------	start here for audio dilaog--------------------------------------*/

	private boolean running = true;
	private int current = 0;
	private int duration = 0;
	ImageView audioImageView;
	Dialog audiodialog;
	ImageView start_recordingImageView, start_playImageView, delete_ImageView,
			share_audioImageView;
	TextView recording_TextView, recording_total_time_TextView,
			cancel_recordingTextView, save_recordingTextView, share_TextView;
	private MediaRecorder myRecorder;
	private MediaPlayer myPlayer;
	boolean isrecord, isplay, ispause;

	private String outputFile = null;
	String encoded_audio_string;
	LinearLayout saveLinearLayout;

	// timer

	private long startTime = 0L;

	private Handler customHandler = new Handler();

	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;

	// seek bar
	private SeekBar mSeekBarPlayer;

	/*--------------------------------------------	end here for audio dilaog-----------------------------------------------------------------------*/

	// Event Coding

	EditText event_EditText;

	TextView event_saveTextView, complete_saveTextView;

	// camera_coding start
	AlertDialog dialog;
	ImageView event_camera_img;
	Builder builder;
	private static final int CAMERA_REQUEST = 1;

	private static final int PICK_FROM_GALLERY = 2;
	FileInputStream fis;
	String encodedImage;
	// camera_coding

	public byte imageInByte[];

	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	ImageView event_img;
	//InputMethodManager imm;

	// Database

	private TaskListDataBase taskDataBase;
	private SetReminder setReminder;
	private Context context;

	private SharedPreferences loginPreferences;
	private SharedPreferences.Editor loginPrefsEditor;
	
	long imageid;
	long audioid;

	/** Called when the activity is first created.; */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_cal);
		setContentView(R.layout.activity_life);

		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		// database
		context = getBaseContext();
		setReminder = new SetReminder();
		taskDataBase = TaskListDataBase.getInstance(getBaseContext()); // Getting
																		// database
																		// instance

		//imm = (InputMethodManager) this
		//		.getSystemService(Service.INPUT_METHOD_SERVICE);
		audioUris = new ArrayList<Uri>();
		img_ArrayList = new ArrayList<PhotoData>();
		audio_ArrayList = new ArrayList<AudioData>();
		custom_actionbar();
		find_id();
		open_gallery_dialog();
		GetOldStuffData();
		//imm.hideSoftInputFromWindow(rememberEditText.getWindowToken(), 0);

	}

	private void GetOldStuffData() {
		// TODO Auto-generated method stub

		taskDataBase = TaskListDataBase.getInstance(getBaseContext()); // Creatin
		array = TaskListArray.getInstance(getBaseContext());
		task = new Task();

		updateid = getIntent().getLongExtra("taskId", -1);
		
		img_ArrayList = taskDataBase.getAllPhoto(updateid);
		setAdapter();
		audio_ArrayList = taskDataBase.getAllAudio(updateid);
		System.out.print("audio list =" + audio_ArrayList);
		setAudioAdapter();
		if (updateid > 0) {
			System.out.println("========long=id==========" + updateid);
			task = taskDataBase.getTask(updateid); // Getting the task from the
													// database

			remember_text = task.getTitle();

			// encoded_audio_string= task.getEvent_Audio();
			// encodedImage= task.getEvent_photo();
			importance = task.getImportance();
			tag1 = task.getEvent_tag();

			System.out.println("========long=id==========" + updateid);

			System.out.println("=========title==========" + remember_text);

			System.out.println("==========audio==========="
					+ encoded_audio_string);
			System.out.println("==========photo===========" + encodedImage);
			System.out.println("==========importance===========" + importance);
			System.out.println("==========tag===========" + tag1);
			set_importance();
			if (remember_text.length() > 0) {
				rememberEditText.setText(remember_text);
				life_remember_TextView.setText(remember_text);
			} else {
				// event_where_TextView.setText("where");
			}
			if (tag1 == null) {

			} else {
				life_recall_text.setText(tag1);
				tags_editText.setText(tag1);
				// event_with_TextView.setText("with");
			}

			// convert_string_image();
			thisway_plus_minus();
			/*
			 * if (encoded_audio_string != null) { // show audio image a
			 * capture_audio_ImageView.setVisibility(View.VISIBLE); Bitmap
			 * bitmap = BitmapFactory.decodeResource( context.getResources(),
			 * R.drawable.event_audio_attachment_new); // get hexagonal shape
			 * Bitmap b = getHexagonShape(bitmap); // set image in imageview
			 * capture_audio_ImageView.setImageBitmap(b); // show audio image }
			 */
		}

	}

	private void set_importance() {
		if (importance != null) {
			switch (importance) {
			case "Low":
				priority_ImageView.setImageResource(R.drawable.importance_one);
				break;
			case "Medium":
				priority_ImageView.setImageResource(R.drawable.importance_two);
				break;
			case "High":
				priority_ImageView
						.setImageResource(R.drawable.importance_three);
				break;
			default:
				break;
			}
		}
	}

	private void updateinfo() {

		remember_text = rememberEditText.getText().toString();
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		String email1 = loginPreferences.getString("username", "");

		if (remember_text.trim().length() <= 0) {

			Toast.makeText(getApplicationContext(),
					"please enter text in remember field", Toast.LENGTH_SHORT).show();

			// Toast.makeText(getApplicationContext(),
			// "------"+final_date+"-"+final_month+"-"+final_year, 300).show();
			// Toast.makeText(getApplicationContext(),
			// "------"+final_hour+"-"+final_minute, 300).show();
		}

		else {
			System.out.println("========update long=id==========" + updateid);

			System.out.println("=========update title=========="
					+ remember_text);

			System.out.println("==========update audio==========="
					+ encoded_audio_string);
			System.out.println("==========update photo==========="
					+ encodedImage);
			System.out.println("==========updateimportance==========="
					+ importance);
			System.out.println("==========update tag===========" + tag1);

			// TODO Auto-generated method stub
			Task newTask = new Task(remember_text);
			newTask.setLife("life");

			newTask.setEvent_tag(tag1);
			newTask.setId(updateid);
			newTask.setEvent_tag(tag1);
			// newTask.setEvent_Audio(encoded_audio_string);
			// newTask.setEvent_photo(encodedImage);

			newTask.setImportance(importance);
			newTask.setUserId(email1);
			// add multiple images
			if (img_ArrayList != null) {
				for (int i = 0; i < img_ArrayList.size(); i++) {

					taskDataBase.add_photo(updateid, img_ArrayList.get(i)
							.getPhoto());
				}
			}

			// add multiple audio
			if (audio_ArrayList != null) {
				for (int i = 0; i < audio_ArrayList.size(); i++) {

					taskDataBase.add_audio(updatedTime, audio_ArrayList.get(i)
							.getAudio());
				}
			}

			array.updateTask(newTask, newTask.getId());
			this.finish();
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
		action_bar_title.setText("{life}");
		action_bar_img.setImageResource(R.drawable.new_etc_icon_active);

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

	public void find_id() {

		audio_image_layout = (LinearLayout) findViewById(R.id.audio_image_layout);

		horizontallistview = (HListView) findViewById(R.id.list6);
		horizontallistview.setOnItemClickListener(this);

		audio_horizontallistview = (HListView) findViewById(R.id.audio_list);
		audio_horizontallistview.setOnItemClickListener(this);
		/* Bottom Section find id */

		priority_ImageView = (ImageView) findViewById(R.id.priority_img);
		alarm_ImageView = (ImageView) findViewById(R.id.alarm_img);

		priority_ImageView.setOnClickListener(this);
		alarm_ImageView.setOnClickListener(this);

		event_EditText = (EditText) findViewById(R.id.event_editText);

		event_saveTextView = (TextView) findViewById(R.id.event_save_text);
		event_saveTextView.setOnClickListener(this);

		complete_saveTextView = (TextView) findViewById(R.id.event_complete_text);
		complete_saveTextView.setOnClickListener(this);
		share_TextView = (TextView) findViewById(R.id.share_notes);
		share_TextView.setOnClickListener(this);

		// Slice
		life_recall_text = (TextView) findViewById(R.id.life_recall_text);
		include_recall_view = (View) findViewById(R.id.include_life_recall);
		recall_layout = (LinearLayout) findViewById(R.id.recall_layout);
		recall_layout.setOnClickListener(this);
		recall_plus_img = (ImageView) findViewById(R.id.recall_plus_img);

		/* slice */

		/*------------------- remember layout - Start -------------*/
		life_remember_TextView = (TextView) findViewById(R.id.life_remember_text);
		remember_LinearLayout = (LinearLayout) findViewById(R.id.remember_layout);
		inner_rememberLayout = (LinearLayout) findViewById(R.id.inner_remember_layout);
		remember_plusImageView = (ImageView) findViewById(R.id.remember_plus_img);
		rememberEditText = (EditText) findViewById(R.id.remember_editText);
		remember_LinearLayout.setOnClickListener(this);
		/*------------------- Remember layout -End-------------*/

		/*------------------- this way layout - Start -------------*/
		include_thisway_view = findViewById(R.id.include_life_this_way);
		life_thisway_TextView = (TextView) findViewById(R.id.life_thisway_text);
		thisway_LinearLayout = (LinearLayout) findViewById(R.id.this_way_layout);
		thisway_plusImageView = (ImageView) findViewById(R.id.thisway_plus_img);
		thisway_LinearLayout.setOnClickListener(this);

		event_camera_img = (ImageView) findViewById(R.id.event_camera_img);
		event_camera_img.setOnClickListener(this);

		audioImageView = (ImageView) findViewById(R.id.audio_imageView);
		audioImageView.setOnClickListener(this);
		/*------------------- This layout -End-------------*/

		linearLayout = (LinearLayout) findViewById(R.id.rootlayout);
		tags_editText = (EditText) findViewById(R.id.tag_editText);
		save_Button = (TextView) findViewById(R.id.save_button1);
		save_Button.setOnClickListener(this);

	}

	public void open_gallery_dialog() {
		/**
		 * open dialog for choose camera/gallery
		 */

		final String[] option = new String[] { "Take from Camera",
				"Select from Gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, option);
		Builder builder = new Builder(this);

		builder.setTitle("Select Option");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.e("Selected Item", String.valueOf(which));
				if (which == 0) {
					// callCamera();
					//call_new();
					Intent in =new Intent(LifeActivity.this, ImageCaptureActivity.class);
					startActivityForResult(in, CAMERA_REQUEST);
				}
				if (which == 1) {
					callGallery();

				}

			}
		});
		dialog = builder.create();

	}

	public void call_new() {

		timeinmilisecod = System.currentTimeMillis();
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "image" + timeinmilisecod + ".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, CAMERA_REQUEST);

	}

	// create helping method cropCapturedImage(Uri picUri)
	public void cropCapturedImage(Uri picUri) {
		// call the standard crop action intent
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		// indicate image type and Uri of image
		cropIntent.setDataAndType(picUri, "image/*");
		// set crop properties
		cropIntent.putExtra("crop", "true");
		// indicate aspect of desired crop
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		// indicate output X and Y
		cropIntent.putExtra("outputX", 256);
		cropIntent.putExtra("outputY", 256);
		// retrieve data on return
		cropIntent.putExtra("return-data", true);
		// start the activity - we handle returning in onActivityResult
		startActivityForResult(cropIntent, 2);
	}

	/**
	 * open camera method
	 */
	public void callCamera() {
		Intent cameraIntent = new Intent(
				MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra("crop", "true");
		cameraIntent.putExtra("aspectX", 0);
		cameraIntent.putExtra("aspectY", 0);
		cameraIntent.putExtra("outputX", 200);
		cameraIntent.putExtra("outputY", 150);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);

	}

	/**
	 * open gallery method
	 */

	public void callGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/*
		 * intent.putExtra("crop", "true"); intent.putExtra("aspectX", 0);
		 * intent.putExtra("aspectY", 0); intent.putExtra("outputX", 200);
		 * intent.putExtra("outputY", 150); intent.putExtra("return-data",
		 * true);
		 */
		startActivityForResult(
				Intent.createChooser(intent, "Complete action using"),
				PICK_FROM_GALLERY);

	}

	public void set_remember_text() {

		life_remember_TextView.setTextColor(getResources().getColor(
				R.color.Green));
		if (rememberEditText.getText().toString().length() == 0) {
			life_remember_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			life_remember_TextView.setText("remember this...");
		} else {
			life_remember_TextView.setText(""
					+ rememberEditText.getText().toString());
		}
	}

	public void recall_plus_minus() {
		if (recall_on == false) {
			recall_plus_img.setBackgroundResource(R.drawable.minus);
			include_recall_view.setVisibility(View.VISIBLE);
			recall_on = true;
			thisway_on = true;
			remember_on = true;
			remember_plus_minus();
			thisway_plus_minus();
			life_recall_text
					.setTextColor(getResources().getColor(R.color.Gray));
			life_recall_text.setText("recall by");

		} else {

			recall_plus_img.setBackgroundResource(R.drawable.plus);
			include_recall_view.setVisibility(View.GONE);
			recall_on = false;

		}
	}

	public void remember_plus_minus() {
		if (remember_on == false) {
			remember_plusImageView.setBackgroundResource(R.drawable.minus);
			inner_rememberLayout.setVisibility(View.VISIBLE);
			remember_on = true;
			thisway_on = true;
			recall_on = true;
			recall_plus_minus();
			thisway_plus_minus();
			life_remember_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			life_remember_TextView.setText("remember this...");

		} else {

			remember_plusImageView.setBackgroundResource(R.drawable.plus);
			inner_rememberLayout.setVisibility(View.GONE);
			remember_on = false;
			set_remember_text();
		}
	}

	public void thisway_plus_minus() {
		if (thisway_on == false) {
			thisway_plusImageView.setBackgroundResource(R.drawable.minus);
			include_thisway_view.setVisibility(View.VISIBLE);
			recall_on = true;
			thisway_on = true;
			remember_on = true;
			remember_plus_minus();
			recall_plus_minus();
			life_thisway_TextView.setTextColor(getResources().getColor(
					R.color.Gray));
			life_thisway_TextView.setText("this way");

		} else {

			thisway_plusImageView.setBackgroundResource(R.drawable.plus);
			include_thisway_view.setVisibility(View.GONE);
			thisway_on = false;

		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.capture_imageView:

			// show_capture_image();

			break;
		case R.id.capture_audio_imageView:

			// play();
			// byte_into_audio();
			// Toast.makeText(getApplicationContext(), "play", 300).show();
			update_audio_dialog();
			break;
		case R.id.remember_layout:
			remember_plus_minus();
			//imm.hideSoftInputFromWindow(rememberEditText.getWindowToken(), 0);
			break;
		case R.id.this_way_layout:
			thisway_plus_minus();
			//imm.hideSoftInputFromWindow(rememberEditText.getWindowToken(), 0);
			break;
		case R.id.recall_layout:
			recall_plus_minus();
			//imm.hideSoftInputFromWindow(rememberEditText.getWindowToken(), 0);
			break;

		case R.id.event_camera_img:
			dialog.show();
			break;
		case R.id.audio_imageView: // capture audio file
			show_audio_dialog();
			break;

		case R.id.save_button1:
			Add_text();
			tags_editText.getText().clear();
			break;
		case R.id.share_notes:
			share_note(v);
			// whole_note_data();
			break;
		case R.id.event_save_text:
			Add_text();
			tags_editText.getText().clear();
			audio_into_byte();
			// close_all();

			if (updateid > 0) {
				// Toast.makeText(getApplicationContext(), "update",
				// 300).show();
				updateinfo();
			} else {
				// Toast.makeText(getApplicationContext(), "save", 300).show();
				event_info();
			}

			break;
		case R.id.event_complete_text:
			if (updateid > 0) {
				// Toast.makeText(getApplicationContext(), "update",
				// 300).show();
				/* updateinfo(); */
				taskDataBase.update_complete(updateid);
				finish();
			}
			break;
		case R.id.priority_img:
			importance_dialog();
			break;
		case R.id.alarm_img:
			alarm_dialog();
			toggle_hour_minute();
			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	private void show_capture_image(int position) {

		ImageView dialog_capture_imageView;
		Button share_capture_imageView, delete_Button;
		final Dialog capture_dialog = new Dialog(this);

		/*
		 * capture_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */

		capture_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		capture_dialog.setContentView(R.layout.capture_image_dialog);
		dialog_capture_imageView = (ImageView) capture_dialog
				.findViewById(R.id.capture_imageView1);
		share_capture_imageView = (Button) capture_dialog
				.findViewById(R.id.share_btn);
		delete_Button = (Button) capture_dialog.findViewById(R.id.delete_btn);

		encodedImage = img_ArrayList.get(position).getPhoto();
		if (encodedImage != null) {

			File imgFile = new File(encodedImage);

			if (imgFile.exists()) {
				/*Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				dialog_capture_imageView.setImageBitmap(myBitmap);*/
				
				Bitmap myBitmap=	decodeSampledBitmapFromFile(imgFile.getAbsolutePath(), 500, 250);
				dialog_capture_imageView.setImageBitmap(myBitmap);
			}

		}
		share_capture_imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * if (encodedImage != null) {
				 * 
				 * byte[] decodedString = Base64.decode(encodedImage,
				 * Base64.DEFAULT); Bitmap decodedByte =
				 * BitmapFactory.decodeByteArray( decodedString, 0,
				 * decodedString.length); // you can create a new file name
				 * "test.jpg" in sdcard // folder. File f = new
				 * File(Environment.getExternalStorageDirectory() +
				 * File.separator + "test.jpg");
				 * 
				 * destination = new File(
				 * Environment.getExternalStorageDirectory(),
				 * System.currentTimeMillis() + ".jpg");
				 * 
				 * try { f.createNewFile();
				 * 
				 * // write the bytes in file FileOutputStream fo = new
				 * FileOutputStream(f); fo.write(decodedString);
				 * 
				 * // remember close de FileOutput fo.close();
				 * 
				 * } catch (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

				// encodedImage = img_ArrayList.get(position).getPhoto();
				if (encodedImage != null) {

					File imgFile = new File(encodedImage);

					if (imgFile.exists()) {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_SEND);

						intent.setType("image/*");

						Uri uri = Uri.fromFile(imgFile);
						System.out.println("immmmm" + uri);

						intent.putExtra(Intent.EXTRA_STREAM, uri);

						if (intent.resolveActivity(getPackageManager()) != null) {
							startActivity(Intent.createChooser(intent, "share"));
						}
					}

				}
			}
		});
		dialog_capture_imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				capture_dialog.cancel();
			}
		});
		delete_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(updateid>0){
					taskDataBase.deleteImage(imageid);
					removeimages(img_position);
					//System.out.println("====imageid============="+imageid);
					//Toast.makeText(getApplicationContext(),""+ imageid, Toast.LENGTH_LONG).show();
				}else{
				removeimages(img_position);
				}
				capture_dialog.cancel();
			
			}
		});
		capture_dialog.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		/*
		 * super.onActivityResult(requestCode, resultCode, data); if (resultCode
		 * != RESULT_OK) return;
		 */
		// Toast.makeText(getApplicationContext(),
		// "resultcode="+resultCode+"requestcode="+requestCode,Toast.LENGTH_SHORT).show();




        if(requestCode==4){
            // img_path=data.getStringExtra("result");



            // Toast.makeText(this,"Image saved"+img_path,Toast.LENGTH_SHORT).show();


            img_path="/storage/emulated/0/M_CamScanner"+File.separator+ ImageEditor.names;

            System.out.println("===path===================" + img_path);

           /* Bitmap bi = getHexagonShape(decodeSampledBitmapFromFile(
                    img_path, 500, 250));
            capture_ImageView.setImageBitmap(bi);
            capture_ImageView.setVisibility(View.VISIBLE);
*/

            addimages();
            setAdapter();
        }
		switch (requestCode) {

		case CAMERA_REQUEST:
			
			
			if(data!=null){
				 img_path=data.getStringExtra("result");
				System.out.println("===path===================" + img_path);
			
					/*Bitmap bi = getHexagonShape(decodeSampledBitmapFromFile(
							img_path, 500, 250));
					capture_ImageView.setImageBitmap(bi);
					capture_ImageView.setVisibility(View.VISIBLE);*/

					addimages();
					setAdapter();
				}
			/*File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "image" + timeinmilisecod + ".jpg");

			img_path = file.getAbsolutePath();

			System.out.println("===path===================" + img_path);
			if(file.exists()){
			addimages();
			setAdapter();
			}*/
			
			if (data != null) {
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {

					/*
					 * Bitmap thumbnail = extras2.getParcelable("data");
					 * 
					 * ByteArrayOutputStream bytes = new
					 * ByteArrayOutputStream();
					 * thumbnail.compress(Bitmap.CompressFormat.PNG, 100,
					 * bytes);
					 * 
					 * imageInByte = bytes.toByteArray();
					 * 
					 * Log.e("output before conversion",
					 * imageInByte.toString()); // Inserting Contacts
					 * Log.d("Insert: ", "Inserting ..");
					 * 
					 * encodedImage = Base64.encodeToString(imageInByte,
					 * Base64.DEFAULT); destination = new File(
					 * Environment.getExternalStorageDirectory(),
					 * System.currentTimeMillis() + ".jpg");
					 * 
					 * 
					 * System.out.print("path="+destination); FileOutputStream
					 * fo; try { destination.createNewFile(); fo = new
					 * FileOutputStream(destination);
					 * fo.write(bytes.toByteArray()); fo.close(); } catch
					 * (FileNotFoundException e) { e.printStackTrace(); } catch
					 * (IOException e) { e.printStackTrace(); }
					 * 
					 * Bitmap b = getHexagonShape(thumbnail);
					 * capture_ImageView.setImageBitmap(b);
					 * capture_ImageView.setVisibility(View.VISIBLE);
					 * addimages(); setAdapter();
					 */}
			} else {
				// Toast.makeText(getApplicationContext(), "if No pic selected",
				// 300).show();
			}
			break;
		case PICK_FROM_GALLERY:
			System.out.println("data" + data);
			if (data != null) {
				Uri selectedImage = data.getData();

				// capture_ImageView.setImageURI(selectedImage);
				try {
					// Get the file path from the URI
					img_path = FileUtils.getPath(this, selectedImage);
					System.out.println("uri  datapath " + img_path);
					addimages();
					setAdapter();
				} catch (Exception e) {
					Log.e("FileSelectorTestActivity", "File select error", e);
				}

				/*
				 * try { Bitmap bitmapImage = decodeBitmap(selectedImage); //
				 * convert bitmap to byte ByteArrayOutputStream stream = new
				 * ByteArrayOutputStream(); bitmapImage
				 * .compress(Bitmap.CompressFormat.PNG, 100, stream);
				 * imageInByte = stream.toByteArray();
				 * 
				 * Log.e("output before conversion", imageInByte.toString());
				 * 
				 * encodedImage = Base64.encodeToString(imageInByte,
				 * Base64.DEFAULT); //
				 * capture_ImageView.setImageBitmap(yourImage); Bitmap b =
				 * getHexagonShape(bitmapImage);
				 * capture_ImageView.setImageBitmap(b);
				 * capture_ImageView.setVisibility(View.VISIBLE);
				 * System.out.print("path=" + selectedImage); addimages();
				 * setAdapter(); } catch (FileNotFoundException e) {
				 * e.printStackTrace(); }
				 */

			}
			break;
		}
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) { // BEST QUALITY MATCH

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inPreferredConfig = Config.RGB_565;
		int inSampleSize = 1;

		if (height > reqHeight) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		}

		int expectedWidth = width / inSampleSize;

		if (expectedWidth > reqWidth) {
			// if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
			// If bigger SampSize..
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}

		options.inSampleSize = inSampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	public void addimages() {
		
		img_ArrayList.add(new PhotoData(img_path));

		// img_ArrayList.add(imageInByte.toString());
		System.out.println("img list" + img_ArrayList);

	}

	public void setAdapter() {
		audio_image_layout.setVisibility(View.VISIBLE);
        horizont = new horizontal_Adapter(LifeActivity.this, img_ArrayList);
		horizontallistview.setAdapter(horizont);

        horizont.notifyDataSetChanged();
	}

	public void removeimages(int position) {

		img_ArrayList.remove(position).getPhoto();
        horizont.notifyDataSetChanged();
		System.out.println("img list" + img_ArrayList);
	}

	public void addaudio() {
		audio_image_layout.setVisibility(View.VISIBLE);
		audio_ArrayList.add(new AudioData(encoded_audio_string));
		// img_ArrayList.add(imageInByte.toString());
		System.out.println("audio list" + audio_ArrayList);

	}

	public void setAudioAdapter() {
		audio_image_layout.setVisibility(View.VISIBLE);
		audio_horizontal_Adapter = new Audio_horizontal_Adapter(this,
				audio_ArrayList);
		audio_horizontallistview.setAdapter(audio_horizontal_Adapter);

		audio_horizontal_Adapter.notifyDataSetChanged();

	}

	public void removeaudio(int position) {

		audio_ArrayList.remove(position).getAudio();
		audio_horizontal_Adapter.notifyDataSetChanged();
		System.out.println("img list" + audio_ArrayList);
	}

	public Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o);

		final int REQUIRED_SIZE = 100;

		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o2);
	}

	public void get_screen_size() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;
		if (height == 800 && width == 480) {
			targetWidth = 110;
			targetHeight = 110;
		}
		if (height == 1280 && width == 720) {
			targetWidth = 140;
			targetHeight = 140;
		}

	}

	public Bitmap getHexagonShape(Bitmap scaleBitmapImage) {
		// TODO Auto-generated method stub
		/*
		 * int targetWidth = 140; int targetHeight = 140;
		 */
		get_screen_size();
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);

		Path path = new Path();
		float stdW = targetWidth;
		float stdH = targetWidth;
		float w3 = stdW / 2;
		float h2 = stdH / 2;

		float radius = stdH / 2 - 10;
		float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
		float centerX = stdW / 2;
		float centerY = stdH / 2;
		path.moveTo(centerX, centerY + radius);
		path.lineTo(centerX - triangleHeight, centerY + radius / 2);
		path.lineTo(centerX - triangleHeight, centerY - radius / 2);
		path.lineTo(centerX, centerY - radius);
		path.lineTo(centerX + triangleHeight, centerY - radius / 2);
		path.lineTo(centerX + triangleHeight, centerY + radius / 2);
		path.moveTo(centerX, centerY + radius);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/*---------------------------Start here for audio-------------------------------------*/
	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		try {
			duration = myPlayer.getDuration();
			mSeekBarPlayer.setMax(duration);
			mSeekBarPlayer.postDelayed(onEverySecond, 1000);
		} catch (NullPointerException e) {
			// TODO: handle exception
		}

	}

	/* ----------------store audio file to sd card-------------------- */
	public void store() {
		timeinmilisecod = System.currentTimeMillis();
		// store it to sd card
		outputFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/lifereminder"
				+ timeinmilisecod
				+ ".3gpp";

		myRecorder = new MediaRecorder();
		myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		myRecorder.setOutputFile(outputFile);
	}

	/*--------------------audio dialog that open when click on audio image view--------------------*/
	public void show_audio_dialog() {
		RelativeLayout relativeLayout;

		audiodialog = new Dialog(LifeActivity.this);
		audiodialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		audiodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		audiodialog.setContentView(R.layout.dialog_activity);

		relativeLayout = (RelativeLayout) audiodialog
				.findViewById(R.id.dialog_layout);
		saveLinearLayout = (LinearLayout) audiodialog
				.findViewById(R.id.save_layout);
		mSeekBarPlayer = (SeekBar) audiodialog.findViewById(R.id.seekBar1);
		recording_total_time_TextView = (TextView) audiodialog
				.findViewById(R.id.recording_total_time_text);
		cancel_recordingTextView = (TextView) audiodialog
				.findViewById(R.id.cancel_recording_text);

		delete_ImageView = (ImageView) audiodialog
				.findViewById(R.id.delete_recording_img1);
		delete_ImageView.setVisibility(View.GONE);
		save_recordingTextView = (TextView) audiodialog
				.findViewById(R.id.save_recording_text);
		start_recordingImageView = (ImageView) audiodialog
				.findViewById(R.id.start_recording_img);
		recording_TextView = (TextView) audiodialog
				.findViewById(R.id.recording_text);
		start_playImageView = (ImageView) audiodialog
				.findViewById(R.id.start_play_img);
		share_audioImageView = (ImageView) audiodialog
				.findViewById(R.id.share_audio);

		saveLinearLayout.setVisibility(View.GONE);
		// cancel recording
		delete_ImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audiodialog.dismiss();
			}
		});
		// cancel recording
		cancel_recordingTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audiodialog.dismiss();
			}
		});
		// save recording
		save_recordingTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				audio_into_byte();

				addaudio();
				setAudioAdapter();
				audiodialog.dismiss();

			}
		});

		mSeekBarPlayer
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (fromUser) {
							myPlayer.seekTo(progress);
							updateTime();
						}
					}
				});
		start_playImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "play", 300).show();
				running = true;
				if (isplay == false) // play
				{

					if (ispause == true) {
						myPlayer.start();
						ispause = false;
						isplay = true;
						mSeekBarPlayer.postDelayed(onEverySecond, 1000);
						start_playImageView
								.setImageResource(R.drawable.event_pause_button);
					} else {
						play();
						isplay = true;

						start_playImageView
								.setImageResource(R.drawable.event_pause_button);

					}

				} else // pause
				{
					isplay = false;
					ispause = true;
					myPlayer.pause();
					start_playImageView
							.setImageResource(R.drawable.event_play_button);

					// start_playImageView.setImageResource(R.drawable.abc_btn_check_material);
				}

			}
		});
		start_recordingImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (isrecord == false) {
					start_recording();
					isrecord = true;

				} else {
					delete_ImageView.setVisibility(View.VISIBLE);
					stop_recording();

					isrecord = false;
				}

				// dialog.dismiss();

			}
		});
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// dialog.dismiss();

			}
		});
		audiodialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(),
				// "dialog dismiss",Toast.LENGTH_SHORT).show();
				try {

					isrecord = false;
					isplay = false;
					ispause = false;
					isrecord = false;
					startTime = 0L;
					timeInMilliseconds = 0L;
					timeSwapBuff = 0L;

					running = false;
					current = 0;
					duration = 0;
					customHandler.removeCallbacks(onEverySecond);
					stopPlay();

					// outputFile = null;
					myRecorder.stop();
					myRecorder.release();
					myRecorder = null;

					customHandler.removeCallbacks(updateTimerThread);

				} catch (NullPointerException e) {
					// TODO: handle exception
				}

			}
		});
		audiodialog.show();
	}

	/*-------------------update time of the seek bar---------------------*/
	private void updateTime() {
		do {
			current = myPlayer.getCurrentPosition();
			System.out.println("duration - " + duration + " current- "
					+ current);
			int dSeconds = (int) (duration / 1000) % 60;
			int dMinutes = (int) ((duration / (1000 * 60)) % 60);
			int dHours = (int) ((duration / (1000 * 60 * 60)) % 24);

			int cSeconds = (int) (current / 1000) % 60;
			int cMinutes = (int) ((current / (1000 * 60)) % 60);
			int cHours = (int) ((current / (1000 * 60 * 60)) % 24);

			if (dHours == 0) {
				recording_TextView.setText(String.format(
						"%02d:%02d / %02d:%02d", cMinutes, cSeconds, dMinutes,
						dSeconds));
			} else {
				recording_TextView.setText(String.format(
						"%02d:%02d:%02d / %02d:%02d:%02d", cHours, cMinutes,
						cSeconds, dHours, dMinutes, dSeconds));
			}

			try {
				Log.d("Value: ",
						String.valueOf((int) (current * 100 / duration)));
				if (mSeekBarPlayer.getProgress() >= 100) {
					break;
				}
			} catch (Exception e) {
			}
		} while (mSeekBarPlayer.getProgress() <= 100);
	}

	/*-----------------	update progress of seek bar------------------------*/
	private Runnable onEverySecond = new Runnable() {
		@Override
		public void run() {
			if (true == running) {
				if (mSeekBarPlayer != null) {

					mSeekBarPlayer.setProgress(myPlayer.getCurrentPosition());
				}

				if (myPlayer.isPlaying()) {
					mSeekBarPlayer.postDelayed(onEverySecond, 1000);
					updateTime();
				}
			}
		}
	};

	/*--------------------------	play recording audio file-----------------------------*/

	public void play() {
		try {

			myPlayer = new MediaPlayer();
			myPlayer.setDataSource(outputFile);
			myPlayer.setOnPreparedListener(this);
			myPlayer.prepare();
			myPlayer.start();
			myPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(getApplicationContext(), "complete",
					// Toast.LENGTH_SHORT).show();
					start_playImageView
							.setImageResource(R.drawable.event_play_button);

				}
			});
			// seek bar
			mSeekBarPlayer.postDelayed(onEverySecond, 1000);
			// Toast.makeText(getApplicationContext(),"Start play the recording...",
			// Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * ------------------------start recording and update recording time
	 * ----------------------
	 */
	private Runnable updateTimerThread = new Runnable() {

		@Override
		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeSwapBuff + timeInMilliseconds;
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			recording_TextView.setText("" + mins + ":"
					+ String.format("%02d", secs) + ":"
					+ String.format("%03d", milliseconds));
			customHandler.postDelayed(this, 0);
		}
	};

	/*--------------------------start Recording--------------------------*/
	public void start_recording() {
		store();
		try {
			myRecorder.prepare();
			myRecorder.start();
			// start timer also
			startTime = SystemClock.uptimeMillis();
			customHandler.postDelayed(updateTimerThread, 0);
		} catch (IllegalStateException e) {
			// start:it is called before prepare()
			// prepare: it is called after start() or before setOutputFormat()
			e.printStackTrace();
		} catch (IOException e) {
			// prepare() fails
			e.printStackTrace();
		}
		start_recordingImageView.setImageResource(R.drawable.event_stop_button);
		recording_TextView.setText("0:00:25");
		recording_TextView.setText("");
		// Toast.makeText(getApplicationContext(),
		// "Start recording...",Toast.LENGTH_SHORT).show();
	}

	/*-----------------stop  recording audio file---------------- */

	public void stop_recording() {
		try {
			myRecorder.stop();
			myRecorder.release();
			myRecorder = null;
			// get stop time
			timeSwapBuff += timeInMilliseconds;
			customHandler.removeCallbacks(updateTimerThread);
			//
			saveLinearLayout.setVisibility(View.VISIBLE);
			if (saveLinearLayout.isShown()) {
				// Toast.makeText(getApplicationContext(), "not", 300).show();
				audiodialog.setCanceledOnTouchOutside(false);
				audiodialog.setCancelable(false);
			} else {
				// Toast.makeText(getApplicationContext(), "dismiss",
				// 300).show();
			}
			start_recordingImageView
					.setImageResource(R.drawable.event_record_button);
			recording_TextView.setText("press to record");
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			recording_total_time_TextView.setText("" + mins + ":"
					+ String.format("%02d", secs));
			start_playImageView.setVisibility(View.VISIBLE);
			start_recordingImageView.setClickable(false);
			// Toast.makeText(getApplicationContext(),
			// "Stop recording...",Toast.LENGTH_SHORT).show();
		} catch (IllegalStateException e) { /* it is called before start() */
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/*----------------------Stop playing----------------------*/
	public void stopPlay() {
		try {
			if (myPlayer != null) {
				myPlayer.stop();
				myPlayer.release();
				myPlayer = null;
				// Toast.makeText(getApplicationContext(),"Stop playing the recording...",
				// Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Slice functionality */
	public void Add_text() {
		ll = new LinearLayout(this);

		ll.setOrientation(LinearLayout.HORIZONTAL);

		ll.setId(i);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		TextView product = new TextView(this);
		if (tags_editText.getText().toString().trim().length() <= 0) {

		} else {
			product.setTextSize(14.0f);
			product.setTextColor(Color.parseColor("#78C854"));// #78C854
			product.setText("" + tags_editText.getText().toString());

			ll.addView(product);
			tag_ArrayList.add(tags_editText.getText().toString());

			ImageView btn = new ImageView(this);
			btn.setPadding(5, 0, 20, 40);
			ll.addView(btn);

			btn.setLayoutParams(params);
			btn.setImageResource(R.drawable.event_awith_delete);
			btn.setDuplicateParentStateEnabled(true);

			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LinearLayout linearParent = (LinearLayout) v.getParent()
							.getParent();
					LinearLayout linearChild = (LinearLayout) v.getParent();
					linearParent.removeView(linearChild);
				}
			});

			linearLayout.addView(ll);
			i++;
		}

	}

	public void event_info() {

		remember_text = rememberEditText.getText().toString();
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		String email1 = loginPreferences.getString("username", "");

		if (tag_ArrayList.isEmpty()) {

		} else {
			tag1 = tag_ArrayList.get(0);
		}
		if (remember_text.trim().length() <= 0) {

			Toast.makeText(getApplicationContext(),
					"please enter text in remember field", Toast.LENGTH_SHORT).show();

			// Toast.makeText(getApplicationContext(),
			// "------"+final_date+"-"+final_month+"-"+final_year, 300).show();
			// Toast.makeText(getApplicationContext(),
			// "------"+final_hour+"-"+final_minute, 300).show();
		}

		else {

			Log.d("remember=", "" + remember_text);
			Log.d("image_String=", "" + encodedImage);
			Log.d("audio Sring=", "" + encoded_audio_string);
			/*
			 * Log.d("image_byte=", "" + imageInByte); Log.d("audio byte=", "" +
			 * fileByteArray);
			 */
			for (String str : tag_ArrayList) {
				Log.d("tag=", "" + str);
			}
			// Log.d("tag=", "" + tag_ArrayList.get(0));

			TaskListArray array = TaskListArray.getInstance(context);
			Task newTask = new Task(remember_text);
			newTask.setLife("life");
			newTask.setImportance(importance);
			newTask.setEvent_tag(tag1);
			// newTask.setEvent_Audio(encoded_audio_string);
			// newTask.setEvent_photo(encodedImage);
			newTask.setUserId(email1);

			rowID = taskDataBase.addTask(newTask); // Adding to DataBase
			newTask.setId(rowID);
			array.addTask(newTask);
			// add multiple images
			if (img_ArrayList != null) {
				for (int i = 0; i < img_ArrayList.size(); i++) {

					taskDataBase.add_photo(rowID, img_ArrayList.get(i)
							.getPhoto());
				}
			}

			// add multiple audio
			if (audio_ArrayList != null) {
				for (int i = 0; i < audio_ArrayList.size(); i++) {

					taskDataBase.add_audio(rowID, audio_ArrayList.get(i)
							.getAudio());
				}
			}

			finish();
		}
	}

	public void audio_into_byte() {
		if (outputFile == null) {
			// Toast.makeText(getApplicationContext(), "null", 300).show();
		} else {
			try {
				fis = new FileInputStream(outputFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int read;
			try {
				while ((read = fis.read(buffer)) != -1) {
					baos.write(buffer, 0, read);
					baos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			fileByteArray = baos.toByteArray();

			encoded_audio_string = Base64.encodeToString(fileByteArray, 0);

		}

	}

	public void byte_into_audio(int position) {

		encoded_audio_string = audio_ArrayList.get(position).getAudio();

		byte[] decoded = Base64.decode(encoded_audio_string, 0);
		// Utilities.log("~~~~~~~~ Decoded: ", Arrays.toString(decoded));

		try {
			/*
			 * File file2 = new File(Environment.getExternalStorageDirectory() +
			 * "/hello-5.3gpp");
			 */
			File file2 = new File(Environment.getExternalStorageDirectory(),
					System.currentTimeMillis() + ".3gpp");
			FileOutputStream os = new FileOutputStream(file2, true);
			os.write(decoded);
			os.close();
			// Toast.makeText(getApplicationContext(), ""+file2, 300).show();

			myPlayer = new MediaPlayer();
			myPlayer.setDataSource(file2.toString());
			myPlayer.setOnPreparedListener(this);
			myPlayer.prepare();
			myPlayer.start();
			System.out.println("time duration of file" + duration);

			myPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(getApplicationContext(),
					// "complete",Toast.LENGTH_SHORT).show();
					// start_playImageView.setImageResource(R.drawable.event_play_button);

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/* Bottom Section */
	public void priority_dialog() {

		Builder builder = new Builder(this);
		builder.setTitle("Importance");

		ListView priorityListView = new ListView(this);
		priorityListView.setAdapter(new Priority_dialog_adapter(this));
		priorityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				/*
				 * Toast.makeText( getApplicationContext(), "" +
				 * Priority_dialog_adapter.priority_text_arr[position],
				 * 300).show();
				 */
			}
		});
		builder.setView(priorityListView);

		/*
		 * CharSequence colors[] = new CharSequence[] {"low", "medium", "high"};
		 * builder.setItems(colors, new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { //
		 * TODO Auto-generated method stub
		 * Toast.makeText(getApplicationContext(), ""+which, 300).show(); } });
		 */
		builder.show();
	}

	@SuppressLint("NewApi")
	public void alarm_dialog() {

		RelativeLayout relativeLayout;
		ImageView upImageView, downImageView;

		Button set_alarm_Button;
		alarm_dialog = new Dialog(LifeActivity.this);
		/*
		 * alarm_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		alarm_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alarm_dialog.setContentView(R.layout.alarm_reminder);
		alarm_hour_minuteTextView = (EditText) alarm_dialog
				.findViewById(R.id.alarm_minute_hour_text);
		set_alarm_Button = (Button) alarm_dialog
				.findViewById(R.id.set_alarm_btn);
		upImageView = (ImageView) alarm_dialog.findViewById(R.id.alarm_up);
		downImageView = (ImageView) alarm_dialog.findViewById(R.id.alarm_down);
		alarm_minute_text = (TextView) alarm_dialog
				.findViewById(R.id.alarm_minute_text);
		alarm_hour_text = (TextView) alarm_dialog
				.findViewById(R.id.alarm_hour_text);

		alarm_minute_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toggle_hour_minute();
			}
		});
		alarm_hour_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toggle_hour_minute();
			}
		});
		set_alarm_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*
				 * Toast.makeText(getApplicationContext(), "" +
				 * alarm_final_value, 300).show();
				 */

				if (alarm_hour_minuteTextView.getText().toString().trim()
						.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"please enter some value", Toast.LENGTH_SHORT).show();

				} else if (Integer.parseInt(alarm_hour_minuteTextView.getText()
						.toString()) == 0) {
					Toast.makeText(getApplicationContext(),
							"you can't set zero", Toast.LENGTH_SHORT).show();
				} else {
					alarm_final_value = Integer
							.parseInt(alarm_hour_minuteTextView.getText()
									.toString());

				//	Toast.makeText(getApplicationContext(), "run", 300).show();

					if (minute_hour_bool == true) {
						// minute
						if (alarm_final_value <= 60) {
							interval = 1000 * 60 * alarm_final_value;
							set_time = true;
							alarm_dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(),
									"please enter right minute", Toast.LENGTH_SHORT).show();
						}
					} else {
						// hour
						if (alarm_final_value <= 12) {
							interval = 1000 * 60 * 60 * alarm_final_value;
							set_time = true;
							alarm_dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(),
									"please enter right hour", Toast.LENGTH_SHORT).show();

						}
					}
				}
				/*
				 * // code end if (minute_hour_bool == true) {
				 * 
				 * Toast.makeText(getApplicationContext(), "minute=" +
				 * alarm_final_value, 300).show();
				 * 
				 * interval = 1000 * 60 * alarm_final_value;
				 * 
				 * Toast.makeText(getApplicationContext(), "interval=" +
				 * interval, 300).show();
				 * 
				 * } else {
				 * 
				 * Toast.makeText(getApplicationContext(), "hour=" +
				 * alarm_final_value, 300).show();
				 * 
				 * interval = 1000 * 60 * 60 * alarm_final_value;
				 * 
				 * Toast.makeText(getApplicationContext(), "interval=" +
				 * interval, 300).show();
				 * 
				 * }
				 */

				/*Toast.makeText(getApplicationContext(),
						"value=" + alarm_final_value, 300).show();*/

			}
		});
		upImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (alarm_hour_minuteTextView.getText().toString().trim()
						.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"please enter some value", Toast.LENGTH_SHORT).show();

				} else {
					alarm_final_value = Integer
							.parseInt(alarm_hour_minuteTextView.getText()
									.toString());
					if (minute_hour_bool == true) {
						// minute
						if (alarm_final_value <= 60) {
							invoke_minute_hour_up();
						} else {
							alarm_final_value = 60;
							invoke_minute_hour_up();
						}
					} else {
						// hour
						if (alarm_final_value <= 12) {
							invoke_minute_hour_up();
						} else {
							alarm_final_value = 12;
							invoke_minute_hour_up();

						}
					}
				}

			}
		});
		downImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alarm_hour_minuteTextView.getText().toString().trim()
						.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"please enter some value", Toast.LENGTH_SHORT).show();

				} else {
					alarm_final_value = Integer
							.parseInt(alarm_hour_minuteTextView.getText()
									.toString());
					if (minute_hour_bool == true) {
						// minute
						if (alarm_final_value <= 60) {
							invoke_minute_hour_down();
						} else {
							alarm_final_value = 2;
							invoke_minute_hour_down();
						}
					} else {
						// hour
						if (alarm_final_value <= 12) {
							invoke_minute_hour_down();
						} else {
							alarm_final_value = 2;
							invoke_minute_hour_down();
						}
					}
				}
			}
		});

		alarm_dialog.show();

	}

	public void alarm_minute_up_click() {

		if (alarm_final_value == 60) {

			alarm_final_value = 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {
			alarm_final_value = alarm_final_value + 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void alarm_minute_down_click() {
		if (alarm_final_value == 1) {

			alarm_final_value = 60;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {
			alarm_final_value = alarm_final_value - 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void alarm_hour_up_click() {

		if (alarm_final_value == 12) {

			alarm_final_value = 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {
			alarm_final_value = alarm_final_value + 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void alarm_hour_down_click() {
		if (alarm_final_value == 1) {

			alarm_final_value = 12;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {
			alarm_final_value = alarm_final_value - 1;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void toggle_hour_minute() {
		// if am_pm_bool = true=pm else false = am

		// alarm_hour_minuteTextView.setText("00");
		if (minute_hour_bool == false) {
			alarm_minute_text.setTextColor(getResources().getColor(
					R.color.Green));
			alarm_hour_text.setTextColor(getResources().getColor(R.color.Gray));
			minute_hour_bool = true;
			alarm_final_value = 60;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		} else {

			alarm_hour_text
					.setTextColor(getResources().getColor(R.color.Green));
			alarm_minute_text.setTextColor(getResources()
					.getColor(R.color.Gray));
			minute_hour_bool = false;
			alarm_final_value = 12;
			alarm_hour_minuteTextView.setText(""
					+ new DecimalFormat("00").format(alarm_final_value));
		}

	}

	public void invoke_minute_hour_up() {
		if (minute_hour_bool == true) {

			alarm_minute_up_click();

		} else {

			alarm_hour_up_click();

		}
	}

	public void invoke_minute_hour_down() {
		if (minute_hour_bool == true) {

			alarm_minute_down_click();

		} else {

			alarm_hour_down_click();

		}
	}

	public void importance_dialog() {
		LinearLayout lowLinearLayout, highLinearLayout, mediumLinearLayout;
		importance_dialog = new Dialog(LifeActivity.this);
		/*
		 * alarm_dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		importance_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		importance_dialog.setContentView(R.layout.importance_dialog);
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
				priority_ImageView.setImageResource(R.drawable.importance_one);
			}
		});
		mediumLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				importance_dialog.dismiss();
				importance = "Medium";
				priority_ImageView.setImageResource(R.drawable.importance_two);
			}
		});
		highLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				importance_dialog.dismiss();
				importance = "High";
				priority_ImageView
						.setImageResource(R.drawable.importance_three);
			}
		});
		importance_dialog.show();
	}

	/* ----------------------update audio dialog---------------------------- */
	/*--------------------audio dialog that open when click on audio image view--------------------*/
	public void update_audio_dialog() {
		RelativeLayout relativeLayout;

		audiodialog = new Dialog(LifeActivity.this);
		audiodialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		audiodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		audiodialog.setContentView(R.layout.dialog_activity);

		relativeLayout = (RelativeLayout) audiodialog
				.findViewById(R.id.dialog_layout);
		saveLinearLayout = (LinearLayout) audiodialog
				.findViewById(R.id.save_layout);
		mSeekBarPlayer = (SeekBar) audiodialog.findViewById(R.id.seekBar1);
		recording_total_time_TextView = (TextView) audiodialog
				.findViewById(R.id.recording_total_time_text);
		cancel_recordingTextView = (TextView) audiodialog
				.findViewById(R.id.cancel_recording_text);

		delete_ImageView = (ImageView) audiodialog
				.findViewById(R.id.delete_recording_img1);
		
		save_recordingTextView = (TextView) audiodialog
				.findViewById(R.id.save_recording_text);
		start_recordingImageView = (ImageView) audiodialog
				.findViewById(R.id.start_recording_img);
		start_recordingImageView.setClickable(false);
		recording_TextView = (TextView) audiodialog
				.findViewById(R.id.recording_text);
		start_playImageView = (ImageView) audiodialog
				.findViewById(R.id.start_play_img);

		save_recordingTextView.setText("{Share}");
		start_recordingImageView.setClickable(false);
		start_recordingImageView.setEnabled(false);

		// cancel recording
		delete_ImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(updateid>0){
					
					taskDataBase.deleteImageAudio(audioid);
					removeaudio(img_position);
					//System.out.println("====imageid============="+imageid);
					//Toast.makeText(getApplicationContext(),""+ imageid, Toast.LENGTH_LONG).show();
				}else{
					removeaudio(img_position);
				}
				
				
				audiodialog.dismiss();
			}
		});
		// cancel recording
		cancel_recordingTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audiodialog.dismiss();
			}
		});
		// save recording
		save_recordingTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				share_audio();

				audiodialog.dismiss();

			}
		});

		mSeekBarPlayer
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (fromUser) {
							myPlayer.seekTo(progress);
							updateTime();
						}
					}
				});
		start_playImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "play", 300).show();
				running = true;
				if (isplay == false) // play
				{

					if (ispause == true) {
						myPlayer.start();
						ispause = false;
						isplay = true;
						mSeekBarPlayer.postDelayed(onEverySecond, 1000);
						start_playImageView
								.setImageResource(R.drawable.event_pause_button);
					} else {
						// play();
						byte_into_audio(img_position);
						isplay = true;

						start_playImageView
								.setImageResource(R.drawable.event_pause_button);

					}

				} else // pause
				{
					isplay = false;
					ispause = true;
					myPlayer.pause();
					start_playImageView
							.setImageResource(R.drawable.event_play_button);

					// start_playImageView.setImageResource(R.drawable.abc_btn_check_material);
				}

			}
		});
		start_recordingImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (isrecord == false) {
					start_recording();
					isrecord = true;

				} else {
					delete_ImageView.setVisibility(View.VISIBLE);
					stop_recording();

					isrecord = false;
				}

				// dialog.dismiss();

			}
		});
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// dialog.dismiss();

			}
		});
		audiodialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(),
				// "dialog dismiss",Toast.LENGTH_SHORT).show();
				try {

					isrecord = false;
					isplay = false;
					ispause = false;
					isrecord = false;
					startTime = 0L;
					timeInMilliseconds = 0L;
					timeSwapBuff = 0L;

					running = false;
					current = 0;
					duration = 0;
					customHandler.removeCallbacks(onEverySecond);
					stopPlay();

					// outputFile = null;
					myRecorder.stop();
					myRecorder.release();
					myRecorder = null;

					customHandler.removeCallbacks(updateTimerThread);

				} catch (NullPointerException e) {
					// TODO: handle exception
				}

			}
		});
		audiodialog.show();
	}

	public void share_audio() {

		// TODO Auto-generated method stub
		if (encoded_audio_string != null) {

			byte[] decoded = Base64.decode(encoded_audio_string, 0);
			// Utilities.log("~~~~~~~~ Decoded: ", Arrays.toString(decoded));

			// you can create a new file name "test.jpg" in sdcard folder.
			File f = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "test.3gpp");
			/*
			 * destination = new File(
			 * Environment.getExternalStorageDirectory(),
			 * System.currentTimeMillis() + ".jpg");
			 */
			try {
				f.createNewFile();

				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(decoded);

				// remember close de FileOutput
				fo.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);

			intent.setType("audio/*");
			// Make sure you put example png image named myImage.png in your
			// directory

			Uri uri = Uri.fromFile(f);
			System.out.println("immmmm" + uri);

			intent.putExtra(Intent.EXTRA_STREAM, uri);

			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(Intent.createChooser(intent, "share"));
			}

		}

	}

	public void whole_note_data() {
		remember_text = rememberEditText.getText().toString();
		str = new StringBuilder("\n");
		str.append("Text : " + remember_text + "\n");

		// Toast.makeText(getApplicationContext(), ""+str, 300).show();
		System.out.println("" + str);

	}

	public void share_note(View v) {

		whole_note_data();
		share_all_images();
		share_all_audio();
		Intent shareintent = new Intent();
		if (audioUris.size() != 0) {
			System.out.print("audio uri at share" + audioUris);
			shareintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
					audioUris);
		}

		if (imageUris.size() != 0) {
			System.out.print("img uri at share" + imageUris);
			shareintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
					imageUris);
		}
		if (imageUris.size() != 0 && audioUris.size() != 0) {
			audioUris.addAll(imageUris);
			shareintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
					audioUris);
		}

		// shareintent.setPackage("com.whatsapp");
		shareintent.putExtra(Intent.EXTRA_TEXT, str.toString());
		shareintent.putExtra(Intent.EXTRA_SUBJECT, "Event");
		if (imageUris.size() != 0 || audioUris.size() != 0) {

			/*Toast.makeText(getApplicationContext(), "images" + imageUris, 300)
					.show();

			Toast.makeText(getApplicationContext(), "audio" + audioUris, 300)
					.show();*/
			shareintent.setAction(Intent.ACTION_SEND_MULTIPLE);
			shareintent.setType("*/*");
		} else {
			//Toast.makeText(getApplicationContext(), "text", 300).show();
			shareintent.setAction(Intent.ACTION_SEND);
			shareintent.setType("text/plain");
		}

		/*
		 * shareintent.putExtra(android.content.Intent.EXTRA_SUBJECT,
		 * "share subject");
		 * shareintent.putExtra(android.content.Intent.EXTRA_TEXT, "shareBody");
		 */

		if (shareintent.resolveActivity(getPackageManager()) != null) {
			startActivity(Intent.createChooser(shareintent, "Share event"));
			imageUris.clear();
			audioUris.clear();
		}

	}

	public void share_image() {
		if (encodedImage != null) {

			byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);
			// you can create a new file name "test.jpg" in sdcard
			// folder.
			File imagefile = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "test.jpg");
			/*
			 * destination = new File(
			 * Environment.getExternalStorageDirectory(),
			 * System.currentTimeMillis() + ".jpg");
			 */
			try {
				imagefile.createNewFile();

				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(imagefile);
				fo.write(decodedString);

				// remember close de FileOutput
				fo.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			imageuri = Uri.fromFile(imagefile);
			System.out.println("immmmm" + imageuri);

		}
	}

	public void share_all_images() {
		imageUris = new ArrayList<Uri>();

		if (img_ArrayList != null) {
			for (int i = 0; i < img_ArrayList.size(); i++) {
				encodedImage = img_ArrayList.get(i).getPhoto();
				File imgFile = new File(encodedImage);
				imageuri = Uri.fromFile(imgFile);
				imageUris.add(imageuri);
			}
			System.out.print("img uri" + imageUris);
		}

	}

	public void share_all_audio() {

		audioUris = new ArrayList<Uri>();
		if (audio_ArrayList != null) {
			for (int i = 0; i < audio_ArrayList.size(); i++) {
				encoded_audio_string = audio_ArrayList.get(i).getAudio();
				byte[] decoded = Base64.decode(encoded_audio_string, 0);
				/*
				 * File f = new File(Environment.getExternalStorageDirectory() +
				 * File.separator + "test.3gpp");
				 */
				timeinmilisecod = System.currentTimeMillis();
				File f = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "/lifereminder" + timeinmilisecod
						+ ".3gpp");

				try {
					f.createNewFile();
					// write the bytes in file
					FileOutputStream fo = new FileOutputStream(f);
					fo.write(decoded);
					// remember close de FileOutput
					fo.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				audiouri = Uri.fromFile(f);
				audioUris.add(audiouri);
			}
			System.out.print("Audio uri" + audioUris);
		}
		/*
		 * if (audio_ArrayList != null) { for (int i = 0; i <
		 * audio_ArrayList.size(); i++) { encoded_audio_string =
		 * audio_ArrayList.get(i).getAudio(); File imgFile = new
		 * File(encoded_audio_string); audiouri = Uri.fromFile(imgFile);
		 * audioUris.add(audiouri); } System.out.print("Audio uri" + audioUris);
		 * }
		 */

	}

	public void share_audio_with_note() {

		// TODO Auto-generated method stub
		if (encoded_audio_string != null) {

			byte[] decoded = Base64.decode(encoded_audio_string, 0);
			// Utilities.log("~~~~~~~~ Decoded: ", Arrays.toString(decoded));

			// you can create a new file name "test.jpg" in sdcard folder.
			File f = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "test.3gpp");
			/*
			 * destination = new File(
			 * Environment.getExternalStorageDirectory(),
			 * System.currentTimeMillis() + ".jpg");
			 */
			try {
				f.createNewFile();

				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(decoded);

				// remember close de FileOutput
				fo.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			audiouri = Uri.fromFile(f);
			System.out.println("immmmm" + audiouri);

		}

	}

	@Override
	public void onItemClick(
			it.sephiroth.android.library.widget.AdapterView<?> parent,
			View view, int position, long id) {
		img_position = position;
		switch (parent.getId()) {
		case R.id.list6:
			 imageid= img_ArrayList.get(position).getId();
			show_capture_image(position);
			//Toast.makeText(getApplicationContext(), "position=" + position, 300)	.show();
			break;
		case R.id.audio_list:
			audioid= audio_ArrayList.get(position).getId();
			update_audio_dialog();
			break;
		default:
			break;
		}
		// TODO Auto-generated method stub
	}

}
