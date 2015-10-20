package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import etc.AttachData;

public class TaskListDataBase extends SQLiteOpenHelper {
	private static TaskListDataBase instance;

	// Table column names
	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_CREATION_DATE = "creationDate";
	public static final String KEY_REMINDER = "reminder";
	public static final String KEY_HAS_REMINDER = "hasReminder";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_IS_DONE = "isDone";

	public static final String KEY_WITH = "with";
	public static final String KEY_PHOTO = "photo";
	public static final String KEY_AUDIO = "audio";
	public static final String KEY_EVENT = "event";
	public static final String KEY_TAG = "tag";

	public static final String KEY_IMPORTENCE = "improtance";

	// email Remind
	public static final String KEY_Mail = "mailid";
	public static final String KEY_COMPOSE = "compose";

	// phone reminder
	public static final String KEY_NOTES = "notes";

	// Faces Reminder
	public static final String KEY_FACE_NAME = "face_name";
	public static final String KEY_FACE_SEX = "face_sex";

	// Etc
	public static final String KEY_ETC = "etc";

	// Life
	public static final String KEY_LIFE = "life";
	
	
	// sync Update Status
		public static final String UPDATE_STATUS = "udpateStatus";
		
		
		// User Status
		public static final String USER_ID = "user_id";
		
		// Date
		public static final String SEARCH_DATE="search_date";
		
		
		// am_pm
		public static final String AM_PM="am_pm";
		
		// repeat daily , weekly, monthly, yearly
		public static final String REPEAT="rep";
		
		// repeat every 2 days
		public static final String REPEAT_DAY_MONTH="repeat_day_month";
		
		// untill date 
		public static final String UNTIL_DATE="until_date";
		// Selected items
		public static final String SELECTED_ITEMS ="selected_items";

	// DataBase name
	public static final String DATABASE_NAME = "tasksDataBase.db";

	// Table name
	public static final String TABLE_TASKS = "tasksTable";
	// Image Table name
	
		public static final String IMAGE_TABLE= "imageTable";
		public static final String PHOTO_ID_KEY = "id";
		public static final String PHOTO_ID = "photo_id";
		public static final String PHOTO_ENCODED_KEY = "photo_encoded_key";
		
		// Image Table name
		
			public static final String AUDIO_TABLE= "audioTable";
			public static final String AUDIO_ID_KEY = "id";
			public static final String AUDIO_ID = "audio_id";
			public static final String AUDIO_ENCODED_KEY = "audio_encoded_key";
			
		
	// DataBase Version
	public static final int DATABASE_VERSION = 21;

	
	public TaskListDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Singleton implement
	public static TaskListDataBase getInstance(Context context) {
		if (instance == null)
			instance = new TaskListDataBase(context);
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" +
		 * KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," +
		 * KEY_DESCRIPTION + " TEXT," + KEY_CREATION_DATE + " TEXT," +
		 * KEY_REMINDER + " TEXT," + KEY_HAS_REMINDER + " INTEGER," +
		 * KEY_LOCATION + " TEXT," + KEY_IS_DONE + " INTEGER" + ")";
		 */

		String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
				+ KEY_DESCRIPTION + " TEXT," + KEY_CREATION_DATE + " TEXT,"
				+ KEY_REMINDER + " TEXT," + KEY_HAS_REMINDER + " INTEGER,"
				+ KEY_LOCATION + " TEXT," + KEY_IS_DONE + " INTEGER,"
				+ KEY_WITH + " TEXT," + KEY_PHOTO + " TEXT," + KEY_AUDIO
				+ " TEXT," + KEY_TAG + " TEXT," + KEY_Mail + " TEXT,"
				+ KEY_COMPOSE + " TEXT," + KEY_NOTES + " TEXT," + KEY_FACE_NAME
				+ " TEXT," + KEY_FACE_SEX + " TEXT," + KEY_ETC + " TEXT,"
				+ KEY_IMPORTENCE + " TEXT," + KEY_EVENT + " TEXT," + KEY_LIFE
				+ " TEXT,"
				+ UPDATE_STATUS+ " TEXT,"
				+ USER_ID+ " TEXT,"
				+ SEARCH_DATE+ " TEXT,"
				+ AM_PM+ " TEXT,"
				+ REPEAT+ " TEXT,"
				+ REPEAT_DAY_MONTH+ " TEXT,"
				+ UNTIL_DATE+ " TEXT,"
				+SELECTED_ITEMS+" TEXT"
				+ ")";
	String CREATE_IMAGE_TABLE = "CREATE TABLE " + IMAGE_TABLE + "(" + PHOTO_ID_KEY
				+ " INTEGER PRIMARY KEY," + PHOTO_ID + " TEXT,"
				+PHOTO_ENCODED_KEY +" TEXT NOT NULL UNIQUE"
				+ ")";
	String CREATE_AUDIO_TABLE = "CREATE TABLE " + AUDIO_TABLE + "(" + AUDIO_ID_KEY
			+ " INTEGER PRIMARY KEY," + AUDIO_ID + " TEXT,"
			+AUDIO_ENCODED_KEY+" TEXT NOT NULL UNIQUE"
			+ ")";


        String CREATE_ATTACHMENT = "CREATE TABLE " + "attchment" + "(" + "a_id"
                + " INTEGER PRIMARY KEY," + "e_id" + " TEXT,"
                +"attachment_path"+" TEXT NOT NULL UNIQUE"
                + ")";

		db.execSQL(CREATE_TASK_TABLE);
		db.execSQL(CREATE_IMAGE_TABLE);
		db.execSQL(CREATE_AUDIO_TABLE);
        db.execSQL(CREATE_ATTACHMENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

		// Create tables again
		onCreate(db);
	}
	// add multiple auido
		public long add_audio(long audio_id, String audio_encoded)
		{
			long rowID;
			SQLiteDatabase database=this.getWritableDatabase();
			ContentValues values=new ContentValues();
			values.put(AUDIO_ENCODED_KEY, audio_encoded);
			values.put(AUDIO_ID, audio_id);
			rowID=database.insert(AUDIO_TABLE, null, values);
			database.close();
			return rowID;
		}
	// add multiple image
	public long add_photo(long photo_id, String photo_encoded)
	{
		long rowID;
		SQLiteDatabase database=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(PHOTO_ENCODED_KEY, photo_encoded);
		values.put(PHOTO_ID, photo_id);
		rowID=database.insert(IMAGE_TABLE, null, values);
		database.close();
		return rowID;
	}
	
	
	// update multiple image
		public long add_photoUpdate(long id,long photo_id, String photo_encoded)
		{
			
			System.out.println("======up id========="+id);
			System.out.println("======up photo_id========="+photo_id);
			System.out.println("======up photo_encoded========="+photo_encoded);
			
			long rowID;
			SQLiteDatabase database=this.getWritableDatabase();
			ContentValues values=new ContentValues();
			values.put(PHOTO_ENCODED_KEY, photo_encoded);
			values.put(PHOTO_ID, id);
			
			rowID=database.insert(IMAGE_TABLE, null, values);
			database.close();
			return rowID;
			//return database.update(IMAGE_TABLE, values, PHOTO_ID + " = ?",
			//		new String[] { String.valueOf(id) });
			
			
		}
		
		
		// update multiple auido
				public long add_audioUpdate(long id,long audio_id, String audio_encoded)
				{
					long rowID;
					SQLiteDatabase database=this.getWritableDatabase();
					ContentValues values=new ContentValues();
					values.put(AUDIO_ENCODED_KEY, audio_encoded);
					values.put(AUDIO_ID, id);
				
					
					rowID=database.insert(AUDIO_TABLE, null, values);
					database.close();
					return rowID;
				}

		
		
	
	// Get all photo
		public ArrayList<PhotoData> getAllPhoto(long id) {
			ArrayList<PhotoData> photolist = new ArrayList<PhotoData>();
			// Select All Query
	    	//String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
			
			String selectQuery = "SELECT  * FROM  imageTable where photo_id=" + "'"+id+"'";
			
			System.out.println("===========Task Table======"+selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToLast()) {
				do {
					PhotoData photo = new PhotoData();

					photo.setId(Integer.parseInt(cursor.getString(0)));
					photo.setPhoto(cursor.getString(2));
					
					// Adding task to list
					photolist.add(photo);
				} while (cursor.moveToPrevious());
			}

			db.close();
			return photolist;
		}
		// Get all Audio
				public ArrayList<AudioData> getAllAudio(long id) {
					ArrayList<AudioData> audiolist = new ArrayList<AudioData>();
					// Select All Query
			    	//String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
					
					String selectQuery = "SELECT  * FROM  audioTable where audio_id=" + "'"+id+"'";
					
					System.out.println("===========Task Table======"+selectQuery);

					SQLiteDatabase db = this.getReadableDatabase();
					Cursor cursor = db.rawQuery(selectQuery, null);

					// looping through all rows and adding to list
					if (cursor.moveToLast()) {
						do {
							AudioData audio = new AudioData();

							audio.setId(Integer.parseInt(cursor.getString(0)));
							audio.setAudio(cursor.getString(2));
							
							// Adding task to list
							audiolist.add(audio);
						} while (cursor.moveToPrevious());
					}

					db.close();
					return audiolist;
				}
			
	// Add new Task and returning the ROWID given from the SQL
	public long addTask(Task newTask) {
		long rowID;
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_TITLE, newTask.getTitle()); // Setting the title
		values.put(KEY_DESCRIPTION, newTask.getDescription()); // Setting the
																// description
		values.put(KEY_CREATION_DATE,
				newTask.getFullDateString(newTask.getCrationDate())); // Setting
																		// the
																		// description
		values.put(KEY_REMINDER,
				newTask.getFullDateString(newTask.getReminder())); // Setting
																	// the
																	// description
		values.put(KEY_HAS_REMINDER, newTask.getHasReminder()); // Setting the
																// hasReminder (
																// 0 = Don't
																// have, 1 =
																// Have).
		values.put(KEY_LOCATION, newTask.getLocation()); // Setting the alert
															// location
		values.put(KEY_IS_DONE, newTask.getIsDone()); // Setting the alert
														// location

		values.put(KEY_WITH, newTask.getEvent_with());
		values.put(KEY_AUDIO, newTask.getEvent_Audio());
		values.put(KEY_PHOTO, newTask.getEvent_photo());
		values.put(KEY_TAG, newTask.getEvent_tag());
		values.put(KEY_EVENT, newTask.getEvent());
		values.put(KEY_IMPORTENCE, newTask.getImportance());
		// Adding the new row to DataBase

		// insert mail
		values.put(KEY_Mail, newTask.getTo_mail());
		values.put(KEY_COMPOSE, newTask.getCompose());

		// insert Phone Notes
		values.put(KEY_NOTES, newTask.getNotes());

		// insert Face Data
		values.put(KEY_FACE_NAME, newTask.getName());
		values.put(KEY_FACE_SEX, newTask.getSex());

		// insert ETC Data
		values.put(KEY_ETC, newTask.getEtc());

		values.put(KEY_LIFE, newTask.getLife());
		
		values.put(UPDATE_STATUS, "no");
		values.put(USER_ID, newTask.getUserId());
		values.put(SEARCH_DATE, newTask.getSearch_date());
		values.put(AM_PM, newTask.getam_pm());
		values.put(REPEAT, newTask.getFinal_repeat());
		values.put(REPEAT_DAY_MONTH, newTask.getFinal_repeat_day_month());
		values.put(UNTIL_DATE, newTask.getFinal_until_date());
		values.put(SELECTED_ITEMS, newTask.getSelected_items());
		rowID = db.insert(TABLE_TASKS, null, values);
		db.close(); // Closing database connection

		return rowID;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	public long addTask1(Task newTask) {
		long rowID;
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_TITLE, newTask.getTitle()); // Setting the title
		values.put(KEY_DESCRIPTION, newTask.getDescription()); // Setting the
																// description
		values.put(KEY_CREATION_DATE,
				newTask.getFullDateString(newTask.getCrationDate())); // Setting
																		// the
																		// description
		values.put(KEY_REMINDER,
				newTask.getSync_reminder()); // Setting
																	// the
																	// description
		values.put(KEY_HAS_REMINDER, newTask.getHasReminder()); // Setting the
																// hasReminder (
																// 0 = Don't
																// have, 1 =
																// Have).
		values.put(KEY_LOCATION, newTask.getLocation()); // Setting the alert
															// location
		values.put(KEY_IS_DONE, newTask.getIsDone()); // Setting the alert
														// location

		values.put(KEY_WITH, newTask.getEvent_with());
		values.put(KEY_AUDIO, newTask.getEvent_Audio());
		values.put(KEY_PHOTO, newTask.getEvent_photo());
		values.put(KEY_TAG, newTask.getEvent_tag());
		values.put(KEY_EVENT, newTask.getEvent());
		values.put(KEY_IMPORTENCE, newTask.getImportance());
		// Adding the new row to DataBase

		// insert mail
		values.put(KEY_Mail, newTask.getTo_mail());
		values.put(KEY_COMPOSE, newTask.getCompose());

		// insert Phone Notes
		values.put(KEY_NOTES, newTask.getNotes());

		// insert Face Data
		values.put(KEY_FACE_NAME, newTask.getName());
		values.put(KEY_FACE_SEX, newTask.getSex());

		// insert ETC Data
		values.put(KEY_ETC, newTask.getEtc());

		values.put(KEY_LIFE, newTask.getLife());
		
		values.put(UPDATE_STATUS, "Yes");
		values.put(USER_ID, newTask.getUserId());
		values.put(SEARCH_DATE, newTask.getSearch_date());
		// event 
		values.put(AM_PM, newTask.getam_pm());
		values.put(REPEAT, newTask.getFinal_repeat());
		values.put(REPEAT_DAY_MONTH, newTask.getFinal_repeat_day_month());
		values.put(UNTIL_DATE, newTask.getFinal_until_date());
		values.put(SELECTED_ITEMS, newTask.getSelected_items());
		rowID = db.insert(TABLE_TASKS, null, values);
		db.close(); // Closing database connection

		return rowID;
	}
	
	
	
	
	// Get specific task
	public Task getTask(long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID, KEY_TITLE,
				KEY_DESCRIPTION, KEY_CREATION_DATE, KEY_REMINDER,
				KEY_HAS_REMINDER, KEY_LOCATION, KEY_IS_DONE, KEY_WITH,
				KEY_PHOTO, KEY_AUDIO, KEY_TAG, KEY_Mail, KEY_COMPOSE,
				KEY_NOTES, KEY_FACE_NAME, KEY_FACE_SEX, KEY_ETC,
				KEY_IMPORTENCE, KEY_EVENT, KEY_LIFE,UPDATE_STATUS,USER_ID,SEARCH_DATE,AM_PM ,REPEAT
				,REPEAT_DAY_MONTH,UNTIL_DATE,SELECTED_ITEMS}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0)
			cursor.moveToFirst();

		Task task = new Task();
		task.setId(Long.parseLong(cursor.getString(0)));
		task.setTitle(cursor.getString(1));
		task.setDescription(cursor.getString(2));
		task.setDateFromString(cursor.getString(3), "CREATION_DATE");
		task.setDateFromString(cursor.getString(4), "REMINDER_DATE");
		task.setHasReminder(cursor.getInt(5));
		task.setLocation(cursor.getString(6));
		task.setIsDone(cursor.getInt(7));
		task.setEvent_with(cursor.getString(8));
		task.setEvent_photo(cursor.getString(9));
		task.setEvent_Audio(cursor.getString(10));
		task.setEvent_tag(cursor.getString(11));
		task.setTo_mail(cursor.getString(12));
		task.setCompose(cursor.getString(13));
		task.setNotes(cursor.getString(14));

		task.setName(cursor.getString(15));
		task.setSex(cursor.getString(16));
		task.setEtc(cursor.getString(17));
		task.setImportance(cursor.getString(18));
		task.setEvent(cursor.getString(19));
		task.setLife(cursor.getString(20));
		
		task.setUpdateStatus(cursor.getString(21));
		task.setUserId(cursor.getString(22));
		task.setSearch_date(cursor.getString(23));
		task.setam_pm(cursor.getString(24));
		task.setFinal_repeat(cursor.getString(25));
		task.setFinal_repeat_day_month(cursor.getString(26));
		task.setFinal_until_date(cursor.getString(27));
		task.setSelected_items(cursor.getString(28));
		db.close();
		return task;
	}

	// Get all Tasks
	public ArrayList<Task> getAllTasks(String user_id) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		// Select All Query
		//String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
		
		String selectQuery = "SELECT  * FROM  tasksTable where user_id=" + "'"+user_id+"'";
		
		System.out.println("===========Task Table======"+selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToLast()) {
			do {
				Task task = new Task();

				task.setId(Integer.parseInt(cursor.getString(0)));
				task.setTitle(cursor.getString(1));
				task.setDescription(cursor.getString(2));
				task.setDateFromString(cursor.getString(3), "CREATION_DATE");
				task.setDateFromString(cursor.getString(4), "REMINDER_DATE");
				task.setHasReminder(cursor.getInt(5));
				task.setLocation(cursor.getString(6));
				task.setIsDone(cursor.getInt(7));
				task.setEvent_with(cursor.getString(8));
				task.setEvent_photo(cursor.getString(9));
				task.setEvent_Audio(cursor.getString(10));
				task.setEvent_tag(cursor.getString(11));
				task.setTo_mail(cursor.getString(12));
				task.setCompose(cursor.getString(13));
				task.setNotes(cursor.getString(14));

				task.setName(cursor.getString(15));
				task.setSex(cursor.getString(16));
				task.setEtc(cursor.getString(17));
				task.setImportance(cursor.getString(18));
				task.setEvent(cursor.getString(19));
				task.setLife(cursor.getString(20));
				task.setUpdateStatus(cursor.getString(21));
				task.setUserId(cursor.getString(22));
				
				task.setSearch_date(cursor.getString(23));
				task.setam_pm(cursor.getString(24));
				task.setFinal_repeat(cursor.getString(25));
				task.setFinal_repeat_day_month(cursor.getString(26));
				task.setFinal_until_date(cursor.getString(27));
				task.setSelected_items(cursor.getString(28));
				// Adding task to list
				taskList.add(task);
			} while (cursor.moveToPrevious());
		}

		db.close();
		return taskList;
	}

	// Get OldStufff
	public ArrayList<Task> getOldStuff() {
		ArrayList<Task> taskList = new ArrayList<Task>();
		// Select All Query
		String selectQuery = "SELECT  * FROM tasksTable WHERE hasReminder=" + '0';

		System.out.println("================oldStuff query======="
				+ selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToLast()) {
			do {
				Task task = new Task();

				task.setId(Integer.parseInt(cursor.getString(0)));
				task.setTitle(cursor.getString(1));
				task.setDescription(cursor.getString(2));
				task.setDateFromString(cursor.getString(3), "CREATION_DATE");
				task.setDateFromString(cursor.getString(4), "REMINDER_DATE");
				task.setHasReminder(cursor.getInt(5));
				task.setLocation(cursor.getString(6));
				task.setIsDone(cursor.getInt(7));
				task.setEvent_with(cursor.getString(8));
				task.setEvent_photo(cursor.getString(9));
				task.setEvent_Audio(cursor.getString(10));
				task.setEvent_tag(cursor.getString(11));
				task.setTo_mail(cursor.getString(12));
				task.setCompose(cursor.getString(13));
				task.setNotes(cursor.getString(14));

				task.setName(cursor.getString(15));
				task.setSex(cursor.getString(16));
				task.setEtc(cursor.getString(17));
				task.setImportance(cursor.getString(18));
				task.setEvent(cursor.getString(19));
				task.setLife(cursor.getString(20));
				
				task.setUpdateStatus(cursor.getString(21));
				task.setUserId(cursor.getString(22));
				task.setSearch_date(cursor.getString(23));
				task.setam_pm(cursor.getString(24));
				
				task.setFinal_repeat(cursor.getString(25));
				task.setFinal_repeat_day_month(cursor.getString(26));
				task.setFinal_until_date(cursor.getString(27));
				task.setSelected_items(cursor.getString(28));
				// Adding task to list
				taskList.add(task);
			} while (cursor.moveToPrevious());
		}

		db.close();
		return taskList;
	}

	// Get number of Tasks in table
	public int getTasksCount() {
		String countQuery = "SELECT  * FROM " + TABLE_TASKS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		db.close();
		return cursor.getCount();
	}

	// Update the given Task
	public int updateTask(Task taskToUpdate) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, taskToUpdate.getTitle());
		values.put(KEY_DESCRIPTION, taskToUpdate.getDescription());
		values.put(KEY_CREATION_DATE,
				taskToUpdate.getFullDateString(taskToUpdate.getCrationDate()));
		values.put(KEY_REMINDER,
				taskToUpdate.getFullDateString(taskToUpdate.getReminder()));
		values.put(KEY_HAS_REMINDER, taskToUpdate.getHasReminder());
		values.put(KEY_LOCATION, taskToUpdate.getLocation());
		values.put(KEY_IS_DONE, taskToUpdate.getIsDone());

		values.put(KEY_WITH, taskToUpdate.getEvent_with());
		values.put(KEY_AUDIO, taskToUpdate.getEvent_Audio());
		values.put(KEY_PHOTO, taskToUpdate.getEvent_photo());
		values.put(KEY_TAG, taskToUpdate.getEvent_tag());
		values.put(KEY_EVENT, taskToUpdate.getEvent());
		values.put(KEY_IMPORTENCE, taskToUpdate.getImportance());
		// Adding the new row to DataBase

		// insert mail
		values.put(KEY_Mail, taskToUpdate.getTo_mail());
		values.put(KEY_COMPOSE, taskToUpdate.getCompose());

		// insert Phone Notes
		values.put(KEY_NOTES, taskToUpdate.getNotes());

		// insert Face Data
		values.put(KEY_FACE_NAME, taskToUpdate.getName());
		values.put(KEY_FACE_SEX, taskToUpdate.getSex());

		// insert ETC Data
		values.put(KEY_ETC, taskToUpdate.getEtc());

		values.put(KEY_LIFE, taskToUpdate.getLife());
		
		values.put(UPDATE_STATUS, "no");
		values.put(USER_ID, taskToUpdate.getUserId());
		
		values.put(SEARCH_DATE, taskToUpdate.getSearch_date());
		values.put(AM_PM, taskToUpdate.getam_pm());
		values.put(REPEAT, taskToUpdate.getFinal_repeat());
		values.put(REPEAT_DAY_MONTH, taskToUpdate.getFinal_repeat_day_month());
		values.put(UNTIL_DATE, taskToUpdate.getFinal_until_date());
		values.put(SELECTED_ITEMS, taskToUpdate.getSelected_items());
		// updating row
		return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(taskToUpdate.getId()) });
	}

	
	
	
	
	// Delete the given Task
	public void deleteTask(Task task2Del) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKS, KEY_ID + " = ?",
				new String[] { String.valueOf(task2Del.getId()) });
		db.close();
	}

	
	
	// Delete the Image 
	public void deleteImage(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(IMAGE_TABLE, PHOTO_ID_KEY + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	
	// Delete the Image 
	public void deleteImageAudio(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(AUDIO_TABLE, AUDIO_ID_KEY + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	
	
	
	/**
	 * Compose JSON out of SQLite records
	 * @return
	 */
	public String composeJSONfromSQLite(){
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		
	
		
		String selectQuery = "SELECT  * FROM tasksTable where udpateStatus = '"+"no"+"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put(KEY_ID, cursor.getString(0));
	        	map.put(KEY_TITLE, cursor.getString(1));
	        	
	        	map.put(KEY_DESCRIPTION, cursor.getString(2));
	        	
	        	map.put(KEY_CREATION_DATE, cursor.getString(3));
	        	
	        	map.put(KEY_REMINDER, cursor.getString(4));
	        	
	        	map.put(KEY_HAS_REMINDER, cursor.getString(5));
	        	
	        	map.put(KEY_LOCATION, cursor.getString(6));
	        	
	        	
	        	map.put(KEY_IS_DONE, cursor.getString(7));
	        	
	        	map.put("with12", cursor.getString(8));
	        	
	        	map.put(KEY_PHOTO, cursor.getString(9));
	        	
	        	map.put(KEY_AUDIO, cursor.getString(10));
	        	
	        	map.put(KEY_TAG, cursor.getString(11));
	        	
	        	map.put(KEY_Mail, cursor.getString(12));
	        	
	        	map.put(KEY_COMPOSE, cursor.getString(13));
	        	
	        	map.put(KEY_NOTES, cursor.getString(14));
	        	
	        	map.put(KEY_FACE_NAME, cursor.getString(15));
	        	
	        	map.put(KEY_FACE_SEX, cursor.getString(16));
	        	
	        	map.put("etc12", cursor.getString(17));
	        	
	        	map.put("imp", cursor.getString(18));
	        	
	        	map.put(KEY_EVENT, cursor.getString(19));
	        	
	        	map.put(KEY_LIFE, cursor.getString(20));
	        	
	        	map.put(USER_ID, cursor.getString(22));
	        	
	        	map.put(SEARCH_DATE, cursor.getString(23));
	        	map.put(AM_PM, cursor.getString(24));
	        	
	        	map.put(REPEAT, cursor.getString(25));
	        	map.put(REPEAT_DAY_MONTH, cursor.getString(26));
	        	map.put(UNTIL_DATE, cursor.getString(27));
	        //	map.put(SELECTED_ITEMS, "2-4-6");
	        //	map.put("userName", cursor.getString(20));
	        	wordList.add(map);
	        	
	        	System.out.println("=============list======="+wordList);
	        	
	        	
	        } while (cursor.moveToNext());
	    }
	    database.close();
		Gson gson = new GsonBuilder().create();
		//Use GSON to serialize Array List to JSON
    	//System.out.println("=============gson======="+gson);

		return gson.toJson(wordList);
	}
	
	
	
	
	
	
	
	/**
	 * Get Sync status of SQLite
	 * @return
	 */
	public String getSyncStatus(){
	    String msg = null;
	    if(this.dbSyncCount() == 0){
	    	msg = "SQLite and Remote MySQL DBs are in Sync!";
	    }else{
	    	msg = "DB Sync needed\n";
	    }
	    return msg;
	}
	
	/**
	 * Get SQLite records that are yet to be Synced
	 * @return
	 */
	public int dbSyncCount(){
		int count = 0;
		String selectQuery = "SELECT  * FROM tasksTable where udpateStatus = '"+"no"+"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    count = cursor.getCount();
	    database.close();
		return count;
	}
	
	
	/**
	 * Update Sync status against each User ID
	 * @param id
	 * @param status
	 */
	public void updateSyncStatus(String id, String status){
		SQLiteDatabase database = this.getWritableDatabase();	 
		String updateQuery = "Update tasksTable set udpateStatus = '"+ status +"' where user_id="+"'"+ id +"'";
		
		System.out.println("============status=========="+status);
		Log.d("query",updateQuery);		
		database.execSQL(updateQuery);
		database.close();
	}
	
	
	
	
	/**
	 * Get SQLite records that are yet to be Synced
	 * @return
	 */
	public int update_complete(long id){
		int count = 0;
		String selectQuery = "Update tasksTable set hasReminder = '"+ "0" +"' where id="+"'"+ id +"'";
		

	System.out.println(selectQuery);
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    count = cursor.getCount();
	    database.close();
		return count;
	}
	
	//Search two input bases
/*	db.query(true, P_TABLE, coloane,KEY_P_CAT_LIST + " LIKE ?" + " AND " +CID + " LIKE ?", new String[] {"%"+CID+"%", "%"+input+"%"}, null, null, null, null);*/
	
	
	
	
	/*
	 if (name.length() != 0) {

	        name = "%" + name + "%";
	    }
	    if (email.length() != 0) {
	        email = "%" + email + "%";
	    }
	    if (Phone.length() != 0) {
	        Phone = "%" + Phone + "%";
	    }
	    String selectQuery = " select * from tbl_Customer where Customer_Name like  '"
	            + name
	            + "' or Customer_Email like '"
	            + email
	            + "' or Customer_Phone like '"
	            + Phone
	            + "' ORDER BY Customer_Id DESC";

	    Cursor cursor = mDb.rawQuery(selectQuery, null);`*/
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * // get Today Task
	 * 
	 * 
	 * 
	 * public Task getTodayTask(long date) { SQLiteDatabase db =
	 * this.getReadableDatabase();
	 * 
	 * Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID, KEY_TITLE,
	 * KEY_DESCRIPTION, KEY_CREATION_DATE , KEY_REMINDER, KEY_HAS_REMINDER,
	 * KEY_LOCATION, KEY_IS_DONE}, KEY_CREATION_DATE + "=?", new String[] {
	 * String.valueOf(date) }, null, null, null, null); if (cursor != null)
	 * cursor.moveToFirst();
	 * 
	 * Task task = new Task(); task.setId(Long.parseLong(cursor.getString(0)));
	 * task.setTitle(cursor.getString(1));
	 * task.setDescription(cursor.getString(2));
	 * task.setDateFromString(cursor.getString(3), "CREATION_DATE");
	 * task.setDateFromString(cursor.getString(4), "REMINDER_DATE");
	 * task.setHasReminder(cursor.getInt(5));
	 * task.setLocation(cursor.getString(6)); task.setIsDone(cursor.getInt(7));
	 * 
	 * db.close(); return task;
	 * 
	 * }
	 */
	
	

	// get search by name and location and importance
	public ArrayList<Task> getSearch(String name, String location,String imortance,String date_time,String today_date,String slice,String with) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		// Select All Query
		//String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
		
		//String selectQuery = "SELECT  * FROM  tasksTable where user_id=" + "'"+user_id+"'";
		
		
		
		String querry = "SELECT * FROM tasksTable ";
		String addComp = "";
		
		
		String append = name;
    //    Toast.makeText(getApplicationContext(), append, Toast.LENGTH_LONG).show();

		if (append!= null && !append.isEmpty()){
			addComp += " title Like " + "'%"+ append +"%'" + " And";
		}
		
		append = location;
		if (append!= null && !append.isEmpty()){
			addComp += " location Like " + "'%"+ append + "%'" + " And";
		}
		
		append = imortance;
		if (append!= null && !append.isEmpty()){
			addComp += " improtance Like " + "'%"+ append +"%'" + " And";
		}
	
		
		append = date_time;
		if (append!= null && !append.isEmpty()){
			addComp += " search_date Like " + "'"+ append +"%'" + " And";
		}
		
		append = today_date;
		if (append!= null && !append.isEmpty()){
			addComp += " search_date Like " + "'"+ append +"%'" + " And";
		}
	
		append = slice;
		if (append!= null && !append.isEmpty()){
			addComp += "  tag Like " + "'"+ append +"'" + " And";
		}
		
		append = with;
		if (append!= null && !append.isEmpty()){
			addComp += "  with Like " + "'%"+ append +"%'" + " And";
		}
		
		if (!addComp.isEmpty()) {
			addComp = addComp.substring(0 , addComp.length() - 3);
			querry += "WHERE " + addComp;
		}
		
		System.out.println("================="+querry);
        //Toast.makeText(getApplicationContext(), querry, Toast.LENGTH_LONG).show();

		
		
		
		
		
		
		//String selectQuery = "SELECT * FROM taskstable where  title LIKE" +"'%"+name+"'"+ " AND location LIKE"+"'%"+location+"'"+ " AND improtance LIKE"+"'%"+imortance+"'";
		
		//SELECT * FROM taskstable where  title LIKE '%ram' AND location LIKE '%gurgaon' AND with LIKE '%seeta' ;
		System.out.println("===========Task Table======"+querry);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(querry, null);

		// looping through all rows and adding to list
		if (cursor.moveToLast()) {
			do {
				Task task = new Task();
               
				task.setId(Integer.parseInt(cursor.getString(0)));
				task.setTitle(cursor.getString(1));
				task.setDescription(cursor.getString(2));
				task.setDateFromString(cursor.getString(3), "CREATION_DATE");
				task.setDateFromString(cursor.getString(4), "REMINDER_DATE");
				task.setHasReminder(cursor.getInt(5));
				task.setLocation(cursor.getString(6));
				task.setIsDone(cursor.getInt(7));
				task.setEvent_with(cursor.getString(8));
				task.setEvent_photo(cursor.getString(9));
				task.setEvent_Audio(cursor.getString(10));
				task.setEvent_tag(cursor.getString(11));
				task.setTo_mail(cursor.getString(12));
				task.setCompose(cursor.getString(13));
				task.setNotes(cursor.getString(14));

				task.setName(cursor.getString(15));
				task.setSex(cursor.getString(16));
				task.setEtc(cursor.getString(17));
				task.setImportance(cursor.getString(18));
				task.setEvent(cursor.getString(19));
				task.setLife(cursor.getString(20));
				task.setUpdateStatus(cursor.getString(21));
				task.setUserId(cursor.getString(22));
				// Adding task to list
				taskList.add(task);
			} while (cursor.moveToPrevious());
		}

		db.close();
		
		return taskList;
		
	}
		
		public ArrayList<Task> getSearchName(String name) {
			ArrayList<Task> taskList = new ArrayList<Task>();
			// Select All Query
			//String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
			
			//String selectQuery = "SELECT  * FROM  tasksTable where user_id=" + "'"+user_id+"'";
			
			String selectQuery = "SELECT * FROM taskstable where  title LIKE" +"'%"+name+"'";
			
			//SELECT * FROM taskstable where  title LIKE '%ram' AND location LIKE '%gurgaon' AND with LIKE '%seeta' ;
			System.out.println("===========Task Table======"+selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToLast()) {
				do {
					Task task = new Task();

					task.setId(Integer.parseInt(cursor.getString(0)));
					task.setTitle(cursor.getString(1));
					task.setDescription(cursor.getString(2));
					task.setDateFromString(cursor.getString(3), "CREATION_DATE");
					task.setDateFromString(cursor.getString(4), "REMINDER_DATE");
					task.setHasReminder(cursor.getInt(5));
					task.setLocation(cursor.getString(6));
					task.setIsDone(cursor.getInt(7));
					task.setEvent_with(cursor.getString(8));
					task.setEvent_photo(cursor.getString(9));
					task.setEvent_Audio(cursor.getString(10));
					task.setEvent_tag(cursor.getString(11));
					task.setTo_mail(cursor.getString(12));
					task.setCompose(cursor.getString(13));
					task.setNotes(cursor.getString(14));

					task.setName(cursor.getString(15));
					task.setSex(cursor.getString(16));
					task.setEtc(cursor.getString(17));
					task.setImportance(cursor.getString(18));
					task.setEvent(cursor.getString(19));
					task.setLife(cursor.getString(20));
					task.setUpdateStatus(cursor.getString(21));
					task.setUserId(cursor.getString(22));
					// Adding task to list
					taskList.add(task);
				} while (cursor.moveToPrevious());
			}

			db.close();
			
			return taskList;
			
		}

    public ArrayList<AttachData> getAllAttachment(long updateid) {

        ArrayList<AttachData> attachdata = new ArrayList<AttachData>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        String selectQuery = "SELECT  * FROM  attchment where e_id=" + "'"+updateid+"'";

        System.out.println("===========Task Table======"+selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                AttachData attachdata1 = new AttachData();

                attachdata1.setId(Integer.parseInt(cursor.getString(0)));
                attachdata1.setFilepath(cursor.getString(2));

                // Adding task to list
                attachdata.add(attachdata1);
            } while (cursor.moveToPrevious());
        }

        db.close();
        return attachdata;
    }

    public long add_AttachFIle(long updateid, String filepath) {

       // System.out.println("======up id========="+id);
        System.out.println("======add photo_id========="+updateid);
        System.out.println("======add photo_encoded========="+filepath);

        long rowID;
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("attachment_path", filepath);
        values.put("e_id", updateid);

        rowID=database.insert("attchment", null, values);
        System.out.println("====return==rowID========" + rowID);
        database.close();
        return rowID;
    }

    public long add_AttachUpdate(long updateid, int id, String filepath) {

        // System.out.println("======up id========="+id);
        System.out.println("======update photo_id========="+updateid);
        System.out.println("======update photo_encoded========="+filepath);

        long rowID;
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("attachment_path", filepath);
        values.put("e_id", updateid);

        rowID=database.insert("attchment", null, values);
        System.out.println("======rowID========" + rowID);
        database.close();
        return rowID;
    }
}
