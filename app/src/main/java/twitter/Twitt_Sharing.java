package twitter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;

import beans.User;

public class Twitt_Sharing {

	private final Twitter_Handler mTwitter;
	private final Activity activity;
	private String twitt_msg, login;
	private File image_path;
	Context context;
	private TwitterSuccess listner;


	public Twitt_Sharing(Activity act, String consumer_key,
			String consumer_secret) {
		this.activity = act;
		mTwitter = new Twitter_Handler(activity, consumer_key, consumer_secret);
	}
public void setListner(TwitterSuccess l){
	listner=l;
}
	public void shareToTwitter(Context context, String msg, File Image_url, String login) 
	{
		this.twitt_msg = msg;
		this.image_path = Image_url;
		this.login=login;
		this.context=context;
		mTwitter.setListener(mTwLoginDialogListener);
		
		//System.out.println("===========fail==msg======"+msg);
		

		if (mTwitter.hasAccessToken()) 
		{
			// this will post data in asyn background thread
			showTwittDialog();
			System.out.println("===========show dialog==="+mTwitter.hasAccessToken());

		} else {
			mTwitter.authorize();
			System.out.println("===========fail==authorize======"+msg);
			
		}
	}

	private void showTwittDialog() {

		new PostTwittTask().execute(twitt_msg);

	}

	private final Twitter_Handler.TwDialogListener mTwLoginDialogListener = new Twitter_Handler.TwDialogListener() {

		@Override
		public void onError(String value) {
			showToast("Login Failed");
			listner.fail("Login Failed");
			mTwitter.resetAccessToken();
			
			System.out.println("===========fail========");
		}

		@Override
		public void onComplete(String value) {
			showTwittDialog();
		}
	};

	void showToast(final String msg) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

			}
		});

	}

	class PostTwittTask extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(activity);
			if(login.equalsIgnoreCase("login"))
			{

				//				pDialog.setMessage("Login Twitt...");
				//				pDialog.setCancelable(false);
				//				pDialog.show();
			}else
			{
				pDialog.setMessage("Posting Twitt...");
				pDialog.setCancelable(false);
				pDialog.show();
			}


			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... twitt) {
			try {
				// mTwitter.updateStatus(twitt[0]);
				// File imgFile = new File("/sdcard/bluetooth/Baby.jpg");
				if(login.equalsIgnoreCase("login"))
				{
				}else
				{
				//Share_Pic_Text_Titter(image_path, twitt_msg, mTwitter.twitterObj);
				}

				return "success";

			} catch (Exception e) {
				if (e.getMessage().toString().contains("duplicate")) {
					return "Posting Failed because of Duplicate message...";
				}
				e.printStackTrace();
				return "Posting Failed!!!";
			}

		}

		@Override
		protected void onPostExecute(String result) 
		{

			try {
				pDialog.dismiss();
				pDialog = null;
			} catch (Exception e) {
				// nothing
			}

			if (null != result && result.equals("success")) {
				if(login.equalsIgnoreCase("login"))
				{
					//					showToast("Login Successfully");
					User info=new User();
					info.setProfilepic(mTwitter.getProfileImageUrl());
					info.setId(mTwitter.getUserId());
					info.setName(mTwitter.getUsername());
				
					
					
					
                   info.setType("T");
                   listner.success(info);
				}
				if(login.equalsIgnoreCase("loginforfrnd"))
				{
					
				}

			} else {
				showToast(result);
			}

			super.onPostExecute(result);
		}


	}
public interface TwitterSuccess{
	abstract void fail(String error);
	abstract void success(User info);
}
	
	
	
}
