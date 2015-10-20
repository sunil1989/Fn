package scan;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import ocrsdk.BusCardSettings;
import ocrsdk.Client;
import ocrsdk.Task;
import xmlparser.XMLParser;

public class AsyncProcessTask extends AsyncTask<String, String, Boolean> {
	private ProgressDialog dialog;
	private String resultUrl = "result.xml";
	/** application context. */
	//private final NewEtcActivity activity;
	
	Context activity;
	String Name,phone;
	public AsyncProcessTask(Context activity) {
		this.activity = activity;
		dialog = new ProgressDialog(activity);
	}

	

	protected void onPreExecute() {
		dialog.setMessage("Processing...");
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	protected void onPostExecute(Boolean result) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		
	updateResults(result);
	}

	private void updateResults(Boolean success) {
		// TODO Auto-generated method stub
		if (!success)
			return;
		try {
			StringBuffer contents = new StringBuffer();

			FileInputStream fis = activity.openFileInput(resultUrl);
			try {
				Reader reader = new InputStreamReader(fis, "UTF-8");
				BufferedReader bufReader = new BufferedReader(reader);
				String text = null;
				while ((text = bufReader.readLine()) != null) {
					contents.append(text).append(System.getProperty("line.separator"));
				}
			} finally {
				fis.close();
			}

			displayMessage(contents.toString());
		} catch (Exception e) {
			displayMessage("Error: " + e.getMessage());
		}
	}



	private void displayMessage(String string) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		XMLParser parser = new XMLParser();
		System.out.println("=======string============="+string);
	//	Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
		
		String response = string.replaceAll("[^\\x20-\\x7e]", "");
		Document doc = parser.getDomElement(response); // getting DOM element
		System.out.println("========doc========="+doc);
		
	//	TextView mBusinesscard=(TextView)findViewById(R.id.business_card);
		//mBusinesscard.setVisibility(View.VISIBLE);
		NodeList nl = doc.getElementsByTagName("field");
		  
		System.out.println("======nl=========="+nl);
		
		for (int i = 0; i < nl.getLength(); i++) {
		
			
		
			Node node= nl.item(i);
		
			///System.out.println("====node name========="+node.getNodeName());
			if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;

                System.out.println("======type==========" + e.getAttribute("type"));

                System.out.println("======ddgd==========" + e.getElementsByTagName("value").item(0).getTextContent());

                if (e.getAttribute("type").equals("Phone")) {
                    phone = e.getElementsByTagName("value").item(0).getTextContent();
                    System.out.println("====phone===========" + phone);
                }


                if (e.getAttribute("type").equals("Name")) {
                    Name = e.getElementsByTagName("value").item(0).getTextContent();
                    System.out.println("====Name===========" + Name);
                }

                //mBusinesscard.append(""+e.getAttribute("type")+":"+e.getElementsByTagName("value").item(0).getTextContent()+"\n");


                if (phone != null && Name != null) {
                    ArrayList<ContentProviderOperation> ops =
                            new ArrayList<ContentProviderOperation>();

                    int rawContactID = ops.size();

                    // Adding insert operation to operations list
                    // to insert a new raw contact in the table ContactsContract.RawContacts
                    ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                            .withValue(RawContacts.ACCOUNT_TYPE, null)
                            .withValue(RawContacts.ACCOUNT_NAME, null)
                            .build());
                    // Adding insert operation to operations list
                    // to insert display name in the table ContactsContract.Data
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                            .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(StructuredName.DISPLAY_NAME, Name)
                            .build());

                    // Adding insert operation to operations list
                    // to insert Mobile Number in the table ContactsContract.Data
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                            .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                            .withValue(Phone.NUMBER, phone)
                            .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                            .build());


                    try {
                        // Executing all the insert operations as a single database transaction
                        activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        Toast.makeText(activity, "Contact is successfully added", Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    } catch (OperationApplicationException e1) {
                        e1.printStackTrace();
                    }
                }

                else{

                    Toast.makeText(activity, "Please Upload Valid Business Card", Toast.LENGTH_SHORT).show();
                }
            }
	           
	            
		}
	
	}



	@Override
	protected Boolean doInBackground(String... args) {

		String inputFile = args[0];
		String outputFile = args[1];

		try {
			Client restClient = new Client();
			
			//!!! Please provide application id and password and remove this line. !!!
			// To create an application and obtain a password,
			// register at http://cloud.ocrsdk.com/Account/Register
			// More info on getting your application id and password at
			// http://ocrsdk.com/documentation/faq/#faq3
			
			// Name of application you created
			restClient.applicationId = "B Card Scan";
			// You should get e-mail from ABBYY Cloud OCR SDK service with the application password
			restClient.password = "HO3q90KudIILZwoidVSDGiD8";
			
			
			
			// Obtain installation id when running the application for the first time
			SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(activity);
			String instIdName = "installationId";
			if( !settings.contains(instIdName)) {
				// Get installation id from server using device id
				String deviceId = android.provider.Settings.Secure.getString(activity.getContentResolver(), 
						android.provider.Settings.Secure.ANDROID_ID);
				
				// Obtain installation id from server
				publishProgress( "First run: obtaining installation id..");
				String installationId = restClient.activateNewInstallation(deviceId);
				publishProgress( "Done. Installation id is '" + installationId + "'");
				
				SharedPreferences.Editor editor = settings.edit();
				editor.putString(instIdName, installationId);
				editor.commit();
			} 
			
			String installationId = settings.getString(instIdName, "");
			restClient.applicationId += installationId;
			
			publishProgress( "Uploading image...");
			
			String language = "English"; // Comma-separated list: Japanese,English or German,French,Spanish etc.
			
		/*	ProcessingSettings processingSettings = new ProcessingSettings();
			processingSettings.setOutputFormat( ProcessingSettings.OutputFormat.xml );
			processingSettings.setLanguage(language);*/
			
			BusCardSettings processingSettings = new BusCardSettings();
			processingSettings.setOutputFormat(BusCardSettings.OutputFormat.xml );
			processingSettings.setLanguage(language);
			
			publishProgress("Uploading..");

			// If you want to process business cards, uncomment this
			/*
			BusCardSettings busCardSettings = new BusCardSettings();
			busCardSettings.setLanguage(language);
			busCardSettings.setOutputFormat(BusCardSettings.OutputFormat.xml);
			Task task = restClient.processBusinessCard(filePath, busCardSettings);
			*/
			Task task = restClient.processBusinessCard(inputFile, processingSettings);
			
			while( task.isTaskActive() ) {
				// Note: it's recommended that your application waits
				// at least 2 seconds before making the first getTaskStatus request
				// and also between such requests for the same task.
				// Making requests more often will not improve your application performance.
				// Note: if your application queues several files and waits for them
				// it's recommended that you use listFinishedTasks instead (which is described
				// at http://ocrsdk.com/documentation/apireference/listFinishedTasks/).

				Thread.sleep(5000);
				publishProgress( "Waiting.." );
				task = restClient.getTaskStatus(task.Id);
			}
			
			if( task.Status == Task.TaskStatus.Completed ) {
				publishProgress( "Downloading.." );
				FileOutputStream fos = activity.openFileOutput(outputFile,Context.MODE_PRIVATE);
				
				try {
					restClient.downloadResult(task, fos);
				} finally {
					fos.close();
				}
				
				publishProgress( "Ready" );
			} else if( task.Status == Task.TaskStatus.NotEnoughCredits ) {
				throw new Exception( "Not enough credits to process task. Add more pages to your application's account." );
			} else {
				throw new Exception( "Task failed" );
			}
			
			return true;
		} catch (Exception e) {
			final String message = "Error: " + e.getMessage();
			publishProgress( message);
		    displayMessage(message);
			return false;
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		String stage = values[0];
		dialog.setMessage(stage);
		// dialog.setProgress(values[0]);
	}

}
