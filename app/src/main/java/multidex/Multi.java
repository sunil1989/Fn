package multidex;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.evernote.client.android.EvernoteSession;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import evernote.LoginChecker;
import settings.SettingActivity;


public class Multi extends Application {

    /*
     * Your Evernote API key. See http://dev.evernote.com/documentation/cloud/
     * Please obfuscate your code to help keep these values secret.
     */
    private static final String CONSUMER_KEY = "sunil1987";
    private static final String CONSUMER_SECRET = "6153f2d4906ca409";
    /*
     * Initial development is done on Evernote's testing service, the sandbox.
     *
     * Change to PRODUCTION to use the Evernote production service
     * once your code is complete.
     */
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    /*
        * Set this to true if you want to allow linked notebooks for accounts that
        * can only access a single notebook.
        */
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;

    public void onCreate() {
	        super.onCreate();
        printHashKey();




        if(SettingActivity.tokan==1) {

            String consumerKey;
            if ("Your consumer key".equals(CONSUMER_KEY)) {
                consumerKey = CONSUMER_KEY;
            } else {
                // isn't the default value anymore
                consumerKey = CONSUMER_KEY;
            }

            String consumerSecret;
            if ("Your consumer secret".equals(CONSUMER_SECRET)) {
                consumerSecret = CONSUMER_SECRET;
            } else {
                // isn't the default value anymore
                consumerSecret = CONSUMER_SECRET;
            }


            //Set up the Evernote singleton session, use EvernoteSession.getInstance() later
            new EvernoteSession.Builder(this)
                    .setEvernoteService(EVERNOTE_SERVICE)
                    .setSupportAppLinkedNotebooks(SUPPORT_APP_LINKED_NOTEBOOKS)
                    .setForceAuthenticationInThirdPartyApp(true)
//                .setLocale(Locale.SIMPLIFIED_CHINESE)
                    .build(consumerKey, consumerSecret)
                    .asSingleton();

            registerActivityLifecycleCallbacks(new LoginChecker());

        }

	    }

    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.life_reminder",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
               // Toast.makeText(getApplicationContext(), "" + Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
	    protected void attachBaseContext(Context base) {
	        super.attachBaseContext(base);
	       MultiDex.install(this);
	    }
}
