package data;

import android.text.format.Time;

public class Task implements Comparable<Task>
{
	private String title; // Task title
	private String description; // Task description
	private String location;
	private Time creationDate; // Date when the task was created
	private Time reminder;
	private long Id;
	private int hasReminder;
	private int isDone;
	private enum Dates {CREATION_DATE, REMINDER_DATE};
	
	private String importance;
	
	private String event_with;
	private String event_photo;
	private String event_Audio;
	private String event_tag;
	private String event;
	private String final_select_am_pm;
	private String final_repeat;
	private String final_repeat_day_month;
	private String final_until_date;
	private String selected_items;
	
	
  // email data
	

	private String to_mail;
	private String compose;
	
	 // Phone data
	private String notes;
	
	//Faces data
	private String name;
	private String Sex;
	
	// Etc Data
	private String etc;
	
	// Etc Data
	private String life;
	
	
	private String updateStatus;
	
	private String userId;
	private String sync_reminder;
	
	
	private String search_date;
	public Task()
	{
		this.title = "";
		this.description = "";
		this.location = "";
		creationDate = new Time();
		this.reminder = new Time();
		this.reminder.set(0, 0, 0, 0, 0, 0);
		this.hasReminder = 0;
		this.isDone = 0;
		
		this.event_with="";
		this.event_photo="";
		this.event_Audio="";
		this.event_tag="";
		this.to_mail="";
		this.compose="";
		this.notes="";
		this.etc="";
		this.event="";
		this.search_date="";
		
		
		this.sync_reminder="";
	}
	
	public Task(String title)
	{
		this.title = title;
		this.creationDate = new Time();
		this.creationDate.setToNow();
		this.reminder = new Time();
		this.reminder.set(0, 0, 0, 0, 0, 0);
		this.hasReminder = 0;
		this.isDone = 0;
				
	}

	public void setDateFromString(String date, String dates)
	{
		System.out.print("----daste-----"+date);
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6)) - 1;
		int day = Integer.parseInt(date.substring(6, 8));
		
		int hour = Integer.parseInt(date.substring(9, 11));
		int minute = Integer.parseInt(date.substring(11, 13));
		
		int second = Integer.parseInt(date.substring(13, 15));
		
		Dates which = Dates.valueOf(dates);
		switch (which)
		{
			case CREATION_DATE:
				this.creationDate.set(second, minute, hour, day, month, year);
				break;
				
			case REMINDER_DATE:
				this.reminder.set(second, minute, hour, day, month, year);
				break;
		}
	}

	public long getId()
	{
		return Id;
	}

	public void setId(long id)
	{
		Id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Time getCrationDate()
	{
		return creationDate;
	}
	
	public Time getReminder()
	{
		return reminder;
	}

	public void setReminder(Time reminder)
	{
		this.reminder = reminder;
	}
	
	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public int getIsDone()
	{
		return isDone;
	}

	public void setIsDone(int isDone)
	{
		this.isDone = isDone;
	}

	public boolean hasReminder()
	{
		if (hasReminder == 0) // If the first char in the toString is 0 there is no reminder for this task
			return false;
		return true;
	}
	
	public String getCreationDateString()
	{	
		return String.valueOf(creationDate.monthDay + "/" +  (creationDate.month+1) + "/" + creationDate.year);
	}
	
	public String getReminderDateString()
	{
		return String.valueOf(reminder.monthDay + "/" +  (reminder.month+1) + "/" + reminder.year);
	}
	
	public int getHasReminder()
	{
		return hasReminder;
	}

	public void setHasReminder(int hasReminder)
	{
		this.hasReminder = hasReminder;
	}
	
	public String getReminderTimeString()
	{
		if (reminder.minute < 10)
			return String.valueOf(reminder.hour + ":0" +  reminder.minute);
		else
			return String.valueOf(reminder.hour + ":" +  reminder.minute);
	}
	
	public String getFullDateString(Time time)
	{
		return time.toString().substring(0, 15); // Returns only the required fields from the toString()
														 // YYYYMMDDTHHMMSS
	}

	@Override
	public int compareTo(Task another)
	{
		return Time.compare(creationDate, another.creationDate);
	}

	public String getEvent_with() {
		return event_with;
	}

	public void setEvent_with(String event_with) {
		this.event_with = event_with;
	}

	public String getEvent_photo() {
		return event_photo;
	}

	public void setEvent_photo(String event_photo) {
		this.event_photo = event_photo;
	}

	public String getEvent_Audio() {
		return event_Audio;
	}

	public void setEvent_Audio(String event_Audio) {
		this.event_Audio = event_Audio;
	}

	public String getEvent_tag() {
		return event_tag;
	}

	public void setEvent_tag(String event_tag) {
		this.event_tag = event_tag;
	}

	public String getTo_mail() {
		return to_mail;
	}

	public void setTo_mail(String to_mail) {
		this.to_mail = to_mail;
	}

	public String getCompose() {
		return compose;
	}

	public void setCompose(String compose) {
		this.compose = compose;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getLife() {
		return life;
	}

	public void setLife(String life) {
		this.life = life;
	}

	public String getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSearch_date() {
		return search_date;
	}

	public void setSearch_date(String search_date) {
		this.search_date = search_date;
	}

	public String getSync_reminder() {
		return sync_reminder;
	}

	public void setSync_reminder(String sync_reminder) {
		this.sync_reminder = sync_reminder;
	}

	public void setam_pm(String final_select_am_pm) {
		// TODO Auto-generated method stub
		this.final_select_am_pm=final_select_am_pm;
	}

	public String getam_pm() {
		return final_select_am_pm;
	}

	public String getFinal_repeat() {
		return final_repeat;
	}

	public void setFinal_repeat(String final_repeat) {
		this.final_repeat = final_repeat;
	}

	public String getFinal_repeat_day_month() {
		return final_repeat_day_month;
	}

	public void setFinal_repeat_day_month(String final_repeat_day_month) {
		this.final_repeat_day_month = final_repeat_day_month;
	}

	public String getFinal_until_date() {
		return final_until_date;
	}

	public void setFinal_until_date(String final_until_date) {
		this.final_until_date = final_until_date;
	}
	
	public String getSelected_items() {
		return selected_items;
	}

	public void setSelected_items(String selected_items) {
		this.selected_items = selected_items;
	}

}
