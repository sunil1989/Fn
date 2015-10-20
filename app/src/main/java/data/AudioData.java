package data;

public class AudioData {
	
	int id;
	String audio;
	String audio_tag;
	public AudioData(){}
	public AudioData(String encodedImage) {
		// TODO Auto-generated constructor stub
		this.audio=encodedImage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	

}
