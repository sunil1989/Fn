package com.life_reminder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppConfig;
import app.ConnectionDetector;
import beans.User;
//import linkedin.FacebookLoginActivity;
import linkedin.GooglePlusLoginActivity;
import linkedin.TwitterLoginActivity;

public class SignInActivity extends Activity implements OnClickListener {
	
	// Connection detector class
	ConnectionDetector cd;
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	/*------remember me------*/
	private String username, password;
	private SharedPreferences loginPreferences;
	private SharedPreferences.Editor loginPrefsEditor;
	private Boolean saveLogin;

	/*------remember me------*/
	private TextView username_textview, emailLabel, sign_in_using_textview,
			create_account_text;
	public Button signinButton;
	static ImageView gplus_ImageView;

	ImageView fb_ImageView, twitter_ImageView;
    LoginButton login;

	TextView forgotTextView;
	LinearLayout createaccount_LinearLayout;
	EditText login_username, login_password;
	CheckBox remember_checkBox;
	private ProgressDialog pDialog;
	RequestParams params = new RequestParams();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_in);
		
		
		//get facebook hash key
		
		
		/*
		 // Add code to print out the key hash
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.life_reminder", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
		
		*/
		
		
		
		
		// creating connection detector class instance
				cd = new ConnectionDetector(getApplicationContext());
				
		login_username = (EditText) findViewById(R.id.username_editText);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(login_username.getWindowToken(), 0);
		login_password = (EditText) findViewById(R.id.password_editText);
		remember_checkBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
		forgotTextView = (TextView) findViewById(R.id.forgot_password_text);
		forgotTextView.setOnClickListener(this);
		sign_in_using_textview = (TextView) findViewById(R.id.sign_in_using_text);
		create_account_text = (TextView) findViewById(R.id.button1);
		signinButton = (Button) findViewById(R.id.signin_button);
		signinButton.setOnClickListener(this);

		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		loginPrefsEditor = loginPreferences.edit();

		saveLogin = loginPreferences.getBoolean("saveLogin", false);
		if (saveLogin == true) {
			login_username.setText(loginPreferences.getString("username", ""));
			login_password.setText(loginPreferences.getString("password", ""));
			remember_checkBox.setChecked(true);
		}

		gplus_ImageView = (ImageView) findViewById(R.id.gplus_imageView);
		twitter_ImageView = (ImageView) findViewById(R.id.twitter_imageView);
		fb_ImageView = (ImageView) findViewById(R.id.fb_imageView);
		fb_ImageView.setOnClickListener(this);
		gplus_ImageView.setOnClickListener(this);
		twitter_ImageView.setOnClickListener(this);
		createaccount_LinearLayout = (LinearLayout) findViewById(R.id.craeteaccount_layout);
		createaccount_LinearLayout.setOnClickListener(this);
		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), "ColabThi.ttf");

		login_username.setTypeface(tf);
		login_password.setTypeface(tf);

		forgotTextView.setTypeface(tf);
		sign_in_using_textview.setTypeface(tf);
		create_account_text.setTypeface(tf);
		login_password.setTypeface(tf);
		remember_checkBox.setTypeface(tf);
		signinButton.setTypeface(tf);

	}
	public void check_internet_status() {
	
		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();
		// check for Internet status
		if (isInternetPresent) {
			// Internet Connection is Present
			
			/*
			 * Toast.makeText(getApplicationContext(),
			 * "You have internet connection", 300).show();
			 */
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(login_username.getWindowToken(), 0);

			username = login_username.getText().toString();
			password = login_password.getText().toString();

			if (remember_checkBox.isChecked()) {
				loginPrefsEditor.putBoolean("saveLogin", true);
				loginPrefsEditor.putString("username", username);
				loginPrefsEditor.putString("password", password);
				loginPrefsEditor.commit();
			} else {
				loginPrefsEditor.clear();
				loginPrefsEditor.commit();
			}
			SignIn();

		} else {
			// Internet connection is not present
			// Ask user to connect to Internet
			Toast.makeText(getApplicationContext(), "No Internet Connection",
					Toast.LENGTH_SHORT).show();
			
			Intent home_intent=new Intent(getApplicationContext(),
					 HomeActivity.class); startActivity(home_intent);
		}
	}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	
}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.forgot_password_text:
			forgot_password_dialog();
			
			break;
		case R.id.fb_imageView:
			/*startActivityForResult(new Intent(SignInActivity.this,
					FacebookLoginActivity.class), 2);*/
			break;
		case R.id.gplus_imageView:
			startActivityForResult(new Intent(SignInActivity.this,
					GooglePlusLoginActivity.class), 3);
			break;
		case R.id.twitter_imageView:
			startActivityForResult(new Intent(SignInActivity.this,
					TwitterLoginActivity.class), 4);

			break;
		case R.id.craeteaccount_layout:
			Intent signup_intent = new Intent(getApplicationContext(),
					SignUpActivity.class);
			startActivity(signup_intent);

			break;
		case R.id.signin_button:

			
		check_internet_status();
			break;
		
		default:
			break;
		}

	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 || requestCode == 2 || requestCode == 3
				|| requestCode == 4) {
			if (resultCode == RESULT_OK && data != null) {
				User user = data.getParcelableExtra("USER");
				// Toast.makeText(getApplicationContext(),user.getName()+" "
				// +user.getBirthday()+" "+user.getEmail()+""+user.getId(),
				// 500).show();
			} else {
				Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	// sign In Method
	private void SignIn() {
		// TODO Auto-generated method stub
		// params.put("fname", usernameEditText.getText().toString());
		// params.put("tag", "login");
		params.put("email", login_username.getText().toString());
		params.put("pass", login_password.getText().toString());

		if (login_username.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(),
					"Please enter your Valid Email", Toast.LENGTH_SHORT).show();
		} else if (login_password.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "Please enter Password",
					Toast.LENGTH_SHORT).show();
		} else {

			// Make RESTful webservice call using AsyncHttpClient object
			AsyncHttpClient client = new AsyncHttpClient();

			client.post(AppConfig.URL_LOGIN, params,
					new AsyncHttpResponseHandler() {
						// When the response returned by REST has Http

						public void onStart() {
							super.onStart();
							pDialog = new ProgressDialog(SignInActivity.this);
							pDialog.setMessage("Please wait...");
							pDialog.setCancelable(false);

							pDialog.show();
						}

						// response code '200'
						@SuppressLint("NewApi")
						@Override
						public void onSuccess(String response) {
							// Hide Progress Dialog

							System.out.println("=========response=========="
									+ response);
							pDialog.hide();
							if (pDialog != null) {
								pDialog.dismiss();
							}

							JSONObject json = null;
							try {
								json = new JSONObject(response);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							boolean re = json.optBoolean("result");

							try {
								// JSONObject jsonResponse = new
								// JSONObject(data);
								JSONArray jsonarray = json
										.getJSONArray("result1");
								for (int i = 0; i < jsonarray.length(); i++) {
									JSONObject jsonobject = jsonarray
											.getJSONObject(i);

									String email = jsonobject
											.optString("email");
									String fname = jsonobject
											.optString("fname");
									String lname = jsonobject
											.optString("lname");
									String phone = jsonobject
											.optString("phone");

									loginPrefsEditor.putString("fname", fname);
									loginPrefsEditor.putString("lname", lname);
									loginPrefsEditor.putString("phone", phone);
									loginPrefsEditor.commit();

									System.out.println("=====email============"
											+ email);
									System.out.println("=====fname============"
											+ fname);
									System.out.println("=====lname============"
											+ lname);
									System.out.println("=====phone============"
											+ phone);

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (re == true) {
								//Toast.makeText(getApplicationContext(),
								//		"Login successfully  ",
									//	Toast.LENGTH_LONG).show();
								finish();
								login_username.setText("");
								login_password.setText("");

								loginPrefsEditor
										.putString("username", username);
								loginPrefsEditor.commit();

								Intent home_intent = new Intent(
										getApplicationContext(),
										HomeActivity.class);
								startActivity(home_intent);

							} else {
								Toast.makeText(
										getApplicationContext(),
										"Please enter valid email id and password ",
										Toast.LENGTH_LONG).show();
							}

							/*
							 * if (response .equalsIgnoreCase(('\u0022' +
							 * "Login sucessfully" + '\u0022'))) {
							 * 
							 * Intent new_intent = new Intent(
							 * getApplicationContext(), HomeActivity.class);
							 * startActivity(new_intent);
							 * Toast.makeText(getApplicationContext(),
							 * "Login successfully with Web App ",
							 * Toast.LENGTH_LONG).show();
							 * 
							 * login_username.setText("");
							 * login_password.setText("");
							 * 
							 * Intent home_intent=new
							 * Intent(getApplicationContext(),
							 * HomeActivity.class); startActivity(home_intent);
							 * 
							 * }
							 * 
							 * else { Toast.makeText(getApplicationContext(),
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
							pDialog.hide();
							if (pDialog != null) {
								pDialog.dismiss();
							}
							// When Http response code is '404'
							if (statusCode == 404) {
								Toast.makeText(getApplicationContext(),
										"Connection error", Toast.LENGTH_LONG)
										.show();
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
								/*
								 * Toast.makeText( getApplicationContext(),
								 * "Unexpected Error occcured! [Most common Error: Device might "
								 * +
								 * "not be connected to Internet or remote server is not up and running], check for other errors as well"
								 * , Toast.LENGTH_LONG).show();
								 */
							}
						}
					});
		}

	}

	public void forgot_password_alertdialog() {
		
		Builder builder = new Builder(this);
		builder.setTitle("Forgot Password");
		EditText forgot_editText = new EditText(this);
		forgot_editText.setHint("Email..");
		forgot_editText.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

		builder.setView(forgot_editText);

		builder.setPositiveButton("Submit",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						dialog.cancel();

					}

				});
		builder.create();
		builder.show();

	}
	private void forgot_password_dialog() {
		// TODO Auto-generated method stub
		final Dialog forgot_password_dialog=new Dialog(this);
		forgot_password_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgot_password_dialog.setContentView(R.layout.forgot_password_dialog);
	
	
		
		 final EditText forgot_emailEditText=(EditText)forgot_password_dialog.findViewById(R.id.forgot_email_editText);
		Button submit_emailButton=(Button)forgot_password_dialog.findViewById(R.id.submit_email_btn);
		submit_emailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String forgot_email=forgot_emailEditText.getText().toString();
				params.put("email", forgot_email);
				if (forgot_email.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter your Valid Email", Toast.LENGTH_SHORT).show();
				}  else {
					
					AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
					asyncHttpClient.post(AppConfig.URL_FORGOT_PASSWORD, params, new AsyncHttpResponseHandler()
					{
						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
							pDialog = new ProgressDialog(SignInActivity.this);
							pDialog.setMessage("Please wait...");
							pDialog.setCancelable(false);
							pDialog.show();
						}
						
						@Override
						@Deprecated
						public void onSuccess(String response) {
							// TODO Auto-generated method stub
							super.onSuccess(response);
						
								pDialog.dismiss();
								
								
							
							try {
								JSONObject jsonObject=new JSONObject(response);
								boolean result=jsonObject.optBoolean("result");
								
							//	forgot_password_dialog.cancel();
							
								
								if(result==true)
								{
									Toast.makeText(getApplicationContext(),
											"Password is send to your mail id",
											Toast.LENGTH_LONG).show();
									forgot_password_dialog.cancel();
								}else
								{
									Toast.makeText(
											getApplicationContext(),
											"Please enter valid email id",
											Toast.LENGTH_LONG).show();
									pDialog.dismiss();
									forgot_password_dialog.show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						}
					@Override
					@Deprecated
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, error, content);
						
						switch (statusCode) {
						case 404:
							Toast.makeText(getApplicationContext(),
									"Connection error ", Toast.LENGTH_LONG)
									.show();
							break;
						case 500:
							Toast.makeText(getApplicationContext(),
									"Inter server error ", Toast.LENGTH_LONG)
									.show();
							break;

						default:
							Toast.makeText(getApplicationContext(),
									"Please enter valid email id", Toast.LENGTH_LONG)
									.show();
							break;
						}
					}
					});
					
				}
			
			}
		});
		

		forgot_password_dialog.show();
	
		
		
	}
	
}
