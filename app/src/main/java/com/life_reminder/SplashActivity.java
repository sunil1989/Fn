package com.life_reminder;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import animation.GifView;

public class SplashActivity extends Activity {

	
	GifView gifView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		 gifView = (GifView)findViewById(R.id.gifview);
		
		// create a Method of Splash
		SplashScreen();
		

	}

	private void SplashScreen() {
		// TODO Auto-generated method stub
		  new Handler().postDelayed(new Runnable(){
	            @Override
	            public void run() {
	                // Create an Intent that will start the Menu-Activity. 
	       Intent mainIntent = new Intent(SplashActivity.this,SignInActivity.class);
	                SplashActivity.this.startActivity(mainIntent);
	                SplashActivity.this.finish();
	                
	                
	              /*  Intent mainIntent = new Intent(SplashActivity.this,EventActivity.class);
	                SplashActivity.this.startActivity(mainIntent);
	                SplashActivity.this.finish();*/
	            }
	        }, 3000);
	    }
	

	

	
}
