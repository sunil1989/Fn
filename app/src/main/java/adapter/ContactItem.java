package adapter;

import android.graphics.Bitmap;

public class ContactItem {

	private String Name;
	private String Number;
	private Bitmap bitmap;
    private String email;
	public ContactItem() {

	}

	public ContactItem(String name, String number, Bitmap image) {
		// TODO Auto-generated constructor stub
		this.Name = name;
		this.Number = number;
		this.bitmap = image;
	}

	public ContactItem(String email2) {
		// TODO Auto-generated constructor stub
		this.email=email2;
	}

	public ContactItem(String name2, String phoneNo) {
		// TODO Auto-generated constructor stub
		this.Name=name2;
		this.Number=phoneNo;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
