package linkedin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.life_reminder.HomeActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppConfig;
import beans.User;

public class GooglePlusLoginActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener{
private static final int RC_SIGN_IN = 2;
private GoogleApiClient mGoogleApiClient;
private boolean mSignInClicked;
private boolean mIntentInProgress;
private ConnectionResult mConnectionResult;
RequestParams params = new RequestParams();
private SharedPreferences.Editor loginPrefsEditor;
private SharedPreferences loginPreferences;
//private ProgressDialog pDialog;
private ProgressDialog mConnectionProgressDialog;
@Override
protected void onCreate(Bundle arg0) {
	super.onCreate(arg0);
	mGoogleApiClient = new GoogleApiClient.Builder(this)
	.addConnectionCallbacks(this)
	.addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
	.addScope(Plus.SCOPE_PLUS_LOGIN).build();


	try {


		signInWithGplus();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}




}
/**
 * Sign-in into google
 * */
private void signInWithGplus() {
	if (!mGoogleApiClient.isConnecting()) {
		mSignInClicked = true;
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
       // mConnectionProgressDialog.show();
		resolveSignInError();



	}
}

/**
 * Sign-out from google
 * */
public void signOutFromGplus() {
	if (mGoogleApiClient.isConnected()) {
		Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
		mGoogleApiClient.disconnect();
		mGoogleApiClient.connect();
		updateUI(false);
	}
}
protected void onStart() {

	mGoogleApiClient.connect();
	super.onStart();
}

protected void onStop() {

	if (mGoogleApiClient.isConnected()) {
		mGoogleApiClient.disconnect();
	}
	super.onStop();
}
/**
 * Method to resolve any signin errors
 * */
private void resolveSignInError() {
	if (mConnectionResult!=null&&mConnectionResult.hasResolution()) {
		try {
			mIntentInProgress = true;
            mConnectionProgressDialog.dismiss();
			mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
		} catch (SendIntentException e) {
			mIntentInProgress = false;
			mGoogleApiClient.connect();
            mConnectionProgressDialog.dismiss();

		}
	}

}

@Override
public void onConnectionFailed(ConnectionResult result) {

	if (!result.hasResolution()) {
		GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
				0).show();
		return;
	}

	if (!mIntentInProgress) {
		// Store the ConnectionResult for later usage
		mConnectionResult = result;
        mConnectionProgressDialog.dismiss();
		if (mSignInClicked) {
			// The user has already clicked 'sign-in' so we attempt to
			// resolve all
			// errors until the user is signed in, or they cancel.

			resolveSignInError();
		}
	}

}

@Override
protected void onActivityResult(int requestCode, int responseCode,
		Intent intent) {

	if (requestCode == RC_SIGN_IN) {
		if (responseCode != RESULT_OK) {
			mSignInClicked = false;
			finish();
		}

		mIntentInProgress = false;

		if (!mGoogleApiClient.isConnecting()) {
			mGoogleApiClient.connect();
            mConnectionProgressDialog.dismiss();
		}
	}
}

@Override
public void onConnected(Bundle arg0) {
	mSignInClicked = false;
		/*Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG)
				.show();*/
		//Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_LONG)
		//.show();
		

		// Get user's information
		getProfileInformation();

		// Update the UI after signin
		updateUI(true);

}

/**
 * Updating the UI, showing/hiding buttons and profile layout
 * */
private void updateUI(boolean isSignedIn) {
	finish();
}

/**
 * Fetching user's information name, email, profile pic
 * */
private void getProfileInformation() {
	try {
		
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person currentPerson = Plus.PeopleApi
					.getCurrentPerson(mGoogleApiClient);
			
			String personName = currentPerson.getDisplayName();
			String personPhotoUrl = currentPerson.getImage().getUrl();
			String personGooglePlusProfile = currentPerson.getUrl();
			String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
			String bday=currentPerson.getBirthday();
			int sex=currentPerson.getGender();
			String id=currentPerson.getId();
			String gender=sex==0?"MALE":"FEMALE";
			Log.e("google", "Name: " + personName + ", plusProfile: "
					+ personGooglePlusProfile + ", email: " + email
					+ ", Image: " + personPhotoUrl);
            User user=new User();
            user.setBirthday(bday);
            user.setEmail(email);
            user.setName(personName);
            user.setId(id);
            user.setGender(gender);
            user.setProfilepic(personPhotoUrl);
            user.setType("G");
            Intent intent=new Intent();
            intent.putExtra("USER", user);
            setResult(RESULT_OK, intent);
            
            
            System.out.println("=====G + Email==========="+email);
            System.out.println("=====G + personName==========="+personName);
            System.out.println("=====G + id==========="+id);
          
            SendSignupData(email,personName);
			// txtName.setText(personName);
			// txtEmail.setText(email);

			// by default the profile url gives 50x50 px image only
			// we can replace the value with whatever dimension we want by
			// replacing sz=X
			// personPhotoUrl =
			// personPhotoUrl.substring(0,personPhotoUrl.length() - 2)+
			// PROFILE_PIC_SIZE;

			// new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
            
          

		} else {
			//Toast.makeText(getApplicationContext(),
			//		"Person information is null", Toast.LENGTH_LONG).show();
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}

private void SendSignupData(String email, String personName) {
	// TODO Auto-generated method stub

	params.put("email", email);
	params.put("fname", personName);
	
	final String emails =email;
	AsyncHttpClient client = new AsyncHttpClient();

	System.out.println();

	client.post(AppConfig.URL_REGISTER, params,
			new AsyncHttpResponseHandler() {
				// When the response returned by REST has Http

				public void onStart() {
					super.onStart();
                 /*   pDialog = new ProgressDialog(GooglePlusLoginActivity.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);

                    pDialog.show();*/
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

                   /* pDialog.hide();
                    if (pDialog != null) {
                        pDialog.dismiss();
                    }*/
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
						//Toast.makeText(getApplicationContext(),
						//		"Email id already exists" , Toast.LENGTH_LONG).show();
						Intent home_intent = new Intent(
								getApplicationContext(),
								HomeActivity.class);
						startActivity(home_intent);
						finish();
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

                  /*  pDialog.hide();
                    if (pDialog != null) {
                        pDialog.dismiss();
                    }*/
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
public void onConnectionSuspended(int arg0) {
	mGoogleApiClient.connect();
	updateUI(false);
}

}
