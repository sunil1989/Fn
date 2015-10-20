package app;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


//download Google Account profile image, to complete profile
	public class LoadProfileImage extends AsyncTask {
		ImageView downloadedImage;

		public LoadProfileImage(ImageView image) {
			this.downloadedImage = image;
		}

		protected Bitmap doInBackground(String... urls) {
			String url = urls[0];
			Bitmap icon = null;
			try {
				InputStream in = new java.net.URL(url).openStream();
				icon = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return icon;
		}

		protected void onPostExecute(Bitmap result) {
			downloadedImage.setImageBitmap(result);
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}
