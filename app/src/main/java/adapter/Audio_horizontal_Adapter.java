package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.life_reminder.R;

import java.util.List;

import data.AudioData;

public class Audio_horizontal_Adapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater layoutInflater;
	private List<AudioData> all_data_list;
	ImageView iconImageView;
	int pos;
	int targetWidth=140;
	int targetHeight=140;
	public Audio_horizontal_Adapter(Activity activity, List<AudioData> all_data_list) {
		super();
		this.activity = activity;
		this.all_data_list = all_data_list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return all_data_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public int getViewTypeCount() {

	    if (getCount() != 0)
	        return getCount();

	    return 1;
	}
	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View view = convertview;
		if (layoutInflater == null) {
			layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (view == null) {
			view = layoutInflater
					.inflate(R.layout.horizontal_audio_item, null);
		}
			iconImageView = (ImageView) view.findViewById(R.id.icon_img);

			pos = position;
			//iconImageView.setImageResource(R.drawable.event_audio_attachment);
			convert_graphic_hexagonal();
	//	set_image();
			/*iconImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(activity, "run="+pos, 300).show();
					
				}
			});*/

		
		return view;
	}
	public void convert_graphic_hexagonal() {
		Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.event_audio_attachment_new);
		// get hexagonal shape
		
	
		Bitmap b = getHexagonShape(bitmap);
		// set image in imageview
		iconImageView.setImageBitmap(b);
	}
	public void set_image() {
		String encodedImage = all_data_list.get(pos).getAudio();
		
		System.out.println("=====encodedImage==========="+encodedImage);

		if (encodedImage != null) {

			byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);
		//	Bitmap b =new EventActivity().getHexagonShape(decodedByte);
			Bitmap b = getHexagonShape(decodedByte);
			
			iconImageView.setImageBitmap(b);
			// iconImageView.setImageResource(R.drawable.phone_title_icon);
		}

	}
	public void get_screen_size() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);

		Path path = new Path();
		float stdW = targetWidth;
		float stdH = targetHeight;
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

}
