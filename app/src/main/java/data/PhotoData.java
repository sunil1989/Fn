package data;

public class PhotoData {
	
	int id;
	String photo;
	String audio_tag;
	public PhotoData(){}
	public PhotoData(String encodedImage) {
		// TODO Auto-generated constructor stub
		this.photo=encodedImage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getAudio_tag() {
		return audio_tag;
	}
	public void setAudio_tag(String audio_tag) {
		this.audio_tag = audio_tag;
	}
	

}
