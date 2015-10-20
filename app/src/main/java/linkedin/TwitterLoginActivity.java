package linkedin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.life_reminder.HomeActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppConfig;
import beans.User;
import twitter.Twitt_Sharing;


public class TwitterLoginActivity extends FragmentActivity implements Twitt_Sharing.TwitterSuccess {
	private ProgressDialog pDialog;
	private SharedPreferences.Editor loginPrefsEditor;
	private SharedPreferences loginPreferences;
	RequestParams params = new RequestParams();
	@Override
	public void fail(String error) {
		finish();
		
		System.out.println("=======Hello==fail============");
		
	}

	@Override
	public void success(User info) {
		Intent intent=new Intent();
		intent.putExtra("USER", info);
		setResult(RESULT_OK,intent);
		finish();
		
		
		String id=info.getId();
		
		String name=info.getName();
		
		System.out.println("=twitter======id=========="+id);	
		System.out.println("=twitter======name=========="+name);
		
		System.out.println("=======Hello==Suceess============");
		
		
		SendSignupData(id,name);
		
		
	}
	private void SendSignupData(String id, String name) {
		// TODO Auto-generated method stub
		
		
		params.put("fname", name);
		
		final String emails =name;

		AsyncHttpClient client = new AsyncHttpClient();

		System.out.println();

		client.post(AppConfig.URL_REGISTER, params,
				new AsyncHttpResponseHandler() {
					// When the response returned by REST has Http

					public void onStart() {
						super.onStart();
					//	pDialog = new ProgressDialog(TwitterLoginActivity.this);
					//	pDialog.setMessage("Please wait...");
						//pDialog.setCancelable(false);

						//pDialog.show();
					}

					// response code '200'
					@Override
					public void onSuccess(String response) {
						// Hide Progress Dialog

						System.out.println("=========response=========="
								+ response);
					/*	pDialog.hide();
						if (pDialog != null) {
							pDialog.dismiss();
						}*/

						JSONObject json = null;
						try {
							json = new JSONObject(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						boolean re = json.optBoolean("result");

						if (re == true) {
							
							 loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
								loginPrefsEditor = loginPreferences.edit();
								loginPrefsEditor
								.putString("username", emails);
					       	    loginPrefsEditor.commit();
							Intent home_intent = new Intent(
									getApplicationContext(),
									HomeActivity.class);
							startActivity(home_intent);
							finish();

						} else {
							 loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
								loginPrefsEditor = loginPreferences.edit();
								loginPrefsEditor
								.putString("username", emails);
					       	    loginPrefsEditor.commit();
					       	    
					       		Intent home_intent = new Intent(
										getApplicationContext(),
										HomeActivity.class);
								startActivity(home_intent);
								finish();
							//Toast.makeText(getApplicationContext(),
							//		"Email id already exists" , Toast.LENGTH_LONG).show();
						}

						/*
						 * if(response.equalsIgnoreCase(('\u0022' +
						 * "Register Sucessfully" + '\u0022'))){
						 * Toast.makeText(getApplicationContext(),
						 * "Registration successfully with Web App ",
						 * Toast.LENGTH_LONG).show();
						 * 
						 * finish();
						 * 
						 * 
						 * 
						 * }
						 * 
						 * else{ Toast.makeText(getApplicationContext(),
						 * response, Toast.LENGTH_LONG).show(); }
						 */

					}

					// When the response returned by REST has Http
					// response code other than '200' such as '404',
					// '500' or '403' etc
					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// Hide Progress Dialog
					/*	pDialog.hide();
						if (pDialog != null) {
							pDialog.dismiss();
						}*/
						// When Http response code is '404'
						if (statusCode == 404) {
							Toast.makeText(getApplicationContext(),
									"Connection error",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code is '500'
						else if (statusCode == 500) {
							Toast.makeText(getApplicationContext(),
									"Internal server error",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code other than 404, 500
						else {
							Toast.makeText(getApplicationContext(),
									"Connection error ", Toast.LENGTH_LONG)
									.show();
							/*Toast.makeText(
									getApplicationContext(),
									"Internal server error "
											+ "not be connected to Internet or remote server is not up and running], check for other errors as well",
									Toast.LENGTH_LONG).show();*/
						}
					}
				});
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Twitt_Sharing ts=new Twitt_Sharing(this, "dgDOdxCZW3Zc5Ca6A2p7oWkzp","gWKz4X9t5TjFADiRJmFcITB4j0H54am67EOUOkG7aeCFVeJlYn");
	//	Twitt_Sharing ts=new Twitt_Sharing(this, "mFYrjuUIP6Q7oAQ0IJ5tmt1jE","7J6uORxxBCJfNyFCyMdPdYS12gDlsj4BxJv3ht4DDdFNetR2WZ");
		ts.shareToTwitter(getApplicationContext(), "", null, "login");
		ts.setListner(this);
		
		System.out.println("===========fail==zzz======");
	}

}
