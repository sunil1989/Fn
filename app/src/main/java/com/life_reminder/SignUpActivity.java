package com.life_reminder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.AppConfig;

public class SignUpActivity extends Activity implements OnClickListener {
	EditText mFirstName, mLastName, mEmail, mPhone, mPassword,
			mConifirmPassword;
	String fname, lname, email, phone, pass, confirm_pass;
	TextView sign_up_using_textTextView;
	private ProgressDialog pDialog;
	RequestParams params = new RequestParams();
	Button submitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_up);

		mFirstName = (EditText) findViewById(R.id.first_name);

		mLastName = (EditText) findViewById(R.id.last_name);
		mEmail = (EditText) findViewById(R.id.email_editText);

		mPhone = (EditText) findViewById(R.id.phone_number);
		mPassword = (EditText) findViewById(R.id.password_editText);
		mConifirmPassword = (EditText) findViewById(R.id.confirm_password_editText);

		submitButton = (Button) findViewById(R.id.submit_button);
		submitButton.setOnClickListener(this);
		sign_up_using_textTextView = (TextView) findViewById(R.id.sign_up_using_text);
		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), "ColabThi.ttf");
		mFirstName.setTypeface(tf);
		mLastName.setTypeface(tf);
		mEmail.setTypeface(tf);
		mPhone.setTypeface(tf);
		mPassword.setTypeface(tf);
		mConifirmPassword.setTypeface(tf);
		submitButton.setTypeface(tf);
		sign_up_using_textTextView.setTypeface(tf);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	public void get_data() {
		fname = mFirstName.getText().toString();
		lname = mLastName.getText().toString();
		email = mEmail.getText().toString();
		phone = mPhone.getText().toString();
		pass = mPassword.getText().toString();
		confirm_pass = mConifirmPassword.getText().toString();

		check_data();
	}

	public void check_data() {
		boolean invalid = false;
		if (fname.trim().isEmpty()) {
			invalid = true;
			Toast.makeText(getApplicationContext(), "Enter your First Name",
					Toast.LENGTH_SHORT).show();
		} else if (lname.trim().isEmpty()) {
			invalid = true;
			Toast.makeText(getApplicationContext(),
					"Please enter your Last Name", Toast.LENGTH_SHORT).show();

		} else if (email.trim().isEmpty()) {
			invalid = true;
			Toast.makeText(getApplicationContext(),
					"Please enter your Valid Email", Toast.LENGTH_SHORT).show();
		} else if (phone.trim().isEmpty()) {

			Toast.makeText(getApplicationContext(),
					"Please enter your Phone Number", Toast.LENGTH_SHORT)
					.show();
		} else if (phone.trim().isEmpty()) {
			invalid = true;
			Toast.makeText(getApplicationContext(),
					"Please enter your Valid Phone Number", Toast.LENGTH_SHORT)
					.show();
		} else if (pass.trim().isEmpty()) {
			invalid = true;
			Toast.makeText(getApplicationContext(),
					"Please enter your Password", Toast.LENGTH_SHORT).show();
		} else if (confirm_pass.trim().isEmpty()) {
			invalid = true;
			Toast.makeText(getApplicationContext(),
					"Please Enter confirm password", Toast.LENGTH_SHORT).show();
		} else if (!isValidEmail(email)) {
			invalid = true;
			Toast.makeText(getApplicationContext(),
					"Please enter your Valid Email", Toast.LENGTH_SHORT).show();
		} else

		if (!confirm_pass.equals(pass)) {
			invalid = true;

			Toast.makeText(getApplicationContext(),
					"Password and Password do not match", Toast.LENGTH_SHORT)
					.show();
		} else if (invalid == false) {

			// Toast.makeText(getApplicationContext(), "Hello11",
			// Toast.LENGTH_LONG).show();
			params.put("fname", fname);
			params.put("lname", lname);
			params.put("email", email);
			params.put("phone", phone);
			params.put("pass", pass);

			SendParameter();

			// finish();
		}

	}

	private void SendParameter()
    {
        // TODO Auto-generated method stub
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        System.out.println();

        client.post(AppConfig.URL_REGISTER, params,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http

                    public void onStart() {
                        super.onStart();
                        pDialog = new ProgressDialog(SignUpActivity.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.setCancelable(false);

                        pDialog.show();
                    }

                    // response code '200'
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

                        if (re == true) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successfully",
                                    Toast.LENGTH_LONG).show();

                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Email id already exists" , Toast.LENGTH_LONG).show();
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
                        pDialog.hide();
                        if (pDialog != null) {
                            pDialog.dismiss();
                        }
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
	private boolean isValidEmail(String email) {
		// TODO Auto-generated method stub
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_button:
			get_data();

			break;

		default:
			break;
		}

	}

}
