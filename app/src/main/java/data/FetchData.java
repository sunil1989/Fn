package data;

public class FetchData {

	String eventName;
    String importance;
    long id;
    
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
   	private String event;
   	
   	private String search_date;
	
	public FetchData(String name, String importance){
		this.eventName=name;
		this.importance=importance;
	}
	public FetchData(String name, String importance2, long id,String event,String mail,String notes, String facename,String etc) {
		// TODO Auto-generated constructor stub
		this.eventName=name;
		this.id=id;
		this.importance=importance2;
		this.event=event;
		this.to_mail=mail;
		this.notes=notes;
		this.name=facename;
		this.etc=etc;
	}

	public FetchData(String name) {
		// TODO Auto-generated constructor stub
		this.eventName=name;
	}
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getImportance() {
		return importance;
	}
	public void setImportance(String importance) {
		this.importance = importance;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getSync_reminder() {
		return sync_reminder;
	}
	public void setSync_reminder(String sync_reminder) {
		this.sync_reminder = sync_reminder;
	}
	public String getSearch_date() {
		return search_date;
	}
	public void setSearch_date(String search_date) {
		this.search_date = search_date;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
}
