package camerawork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.life_reminder.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import custom.CustomProgressDialog;
import db.MyPrefs;
import scan.AsyncProcessTask;

/**
 * Created by Sunil Rathour on 1/20/2015.
 */
public class ImageCaptureActivity extends Activity implements OnClickListener {

    ImageButton captureImage;
    ImageButton settingButton;
    ImageButton mBusinessCardButton;
    protected static final int MEDIA_TYPE_IMAGE = 1427;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private Camera mCamera;
    Camera.Parameters dCP;
    MyPrefs myPrefs;
    File pictureFile = null;
    CustomProgressDialog customProgressDialog;
    long timeinmilisecod;
	private static final int CAMERA_REQUEST = 4;
	private final int TAKE_PICTURE = 0;
	String imageFilePath = null;
	private String resultUrl = "result.xml";
	int i;
	
	ArrayList<String> alist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_layout);
        //  mCamera = getCameraInstance();
        
        alist=new ArrayList<>();
        initPalletes();
        customProgressDialog = new CustomProgressDialog(ImageCaptureActivity.this, R.drawable.progress_img);
       
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            alertCameraServiceFailed(e.getMessage());
        }
        return c;
    }

    private void alertCameraServiceFailed(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ImageCaptureActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog));
        builder.setTitle("Alert")
                .setMessage(message + "." + "Please restart your device.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {

        int result = ImageCaptureActivity.getCameraDisplayOrientation(activity,
                cameraId, camera);
        if (android.os.Build.VERSION.SDK_INT <= 14) {
            camera.stopPreview();
            camera.setDisplayOrientation(result);
            camera.startPreview();
        } else {
            camera.setDisplayOrientation(result);
        }
    }

    public static int getCameraDisplayOrientation(Activity activity,
                                                  int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90; 
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    //returns current exif orientation of the image being captured, by taking the fixed orientation of the camera and
    //subtracting the current device orientation
    private int getScreenOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0, info);

        int orientation = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                orientation = 0;
                break;
            case Surface.ROTATION_90:
                orientation = 90;
                break;
            case Surface.ROTATION_180:
                orientation = 180;
                break;
            case Surface.ROTATION_270:
                orientation = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + orientation) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - orientation + 360) % 360;
        }

        switch (result) {
            case 0:
                orientation = ExifInterface.ORIENTATION_NORMAL;
                break;
            case 90:
                orientation = ExifInterface.ORIENTATION_ROTATE_90;
                break;
            case 180:
                orientation = ExifInterface.ORIENTATION_ROTATE_180;
                break;
            case 270:
                orientation = ExifInterface.ORIENTATION_ROTATE_270;
                break;
            default:
                Log.e("PHOTO", "Unknown screen orientation. Defaulting to " +
                        "portrait.");
                orientation = ExifInterface.ORIENTATION_UNDEFINED;
                break;
        }

        return orientation;
    }

    private void saveImageOrientation() {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(pictureFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("orientation   " + String.valueOf(getScreenOrientation()));
        if (exif != null) {
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(getScreenOrientation()));
            try {
                exif.saveAttributes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void initPalletes() {
        myPrefs = new MyPrefs(getApplicationContext());
//        customProgressDialog=new CustomProgressDialog(getApplicationContext(),R.drawable.progress_img);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        captureImage = (ImageButton) findViewById(R.id.captureImg);
        settingButton = (ImageButton) findViewById(R.id.setBt);
        mBusinessCardButton= (ImageButton) findViewById(R.id.gallt);
        mBusinessCardButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				i=2;
				mCamera.takePicture(null, null, mPicture);
				// TODO Auto-generated method stub
				/*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		    	Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
		        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
		        
		        startActivityForResult(intent, TAKE_PICTURE);*/
			}
		});
        
        
        
        settingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i=1;
				mCamera.takePicture(null, null, mPicture);
				/*timeinmilisecod = System.currentTimeMillis();
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				File file = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "image" + timeinmilisecod + ".jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(intent, CAMERA_REQUEST);*/
			}
		});
        captureImage.setOnClickListener(this);
    }

    private static Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "ABBYY Cloud OCR SDK Demo App");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    }

	    // Create a media file name
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "image.jpg" );

	    return mediaFile;
	}

	boolean isDeviceZoomSupported() {
        dCP = mCamera.getParameters();
        if (dCP.isZoomSupported()) {
            System.out.println("Yaahoooo!! Zoom supported.");
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
        preview.removeView(mPreview);
        mPreview = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCameraInstance();
        }
        try {
            if (mPreview == null) {
                mPreview = new CameraPreview(getApplicationContext(), mCamera, this);
                preview.addView(mPreview);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            // mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        if (mPreview != null) {
            mPreview.getHolder().removeCallback(mPreview);
        }

    }


    //*****************************Image Capture**************************************//


    Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            new SavePhotoTask().execute(data);
//            mCamera.startPreview();
//            captureImage.setEnabled(true);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.captureImg:
            	i=1;
                mCamera.takePicture(null, null, mPicture);
                break;
        }
    }

    class SavePhotoTask extends AsyncTask<byte[], String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressDialog.show();
        }

        @Override
        protected String doInBackground(byte[]... data) {


            try {
                pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                System.out.println("picture path " + pictureFile.getAbsolutePath());
                
                alist.add(pictureFile.getAbsolutePath());
                System.out.println("===alist========"+alist);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (pictureFile == null) {

                return null;
            }

            byte[] photoData = data[0];
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(photoData);
                fos.close();
                saveImageOrientation();
            } catch (FileNotFoundException e) {
                Log.d("DEV", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("DEV", "Error accessing file: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            customProgressDialog.dismiss();
           
            if(i==0){
            	//initPalletes();
            	  //Intent intent = getIntent();
            	Intent returnIntent = new Intent();
            	returnIntent.putExtra("result",pictureFile.getAbsolutePath());
            	setResult(RESULT_OK,returnIntent);
            	finish();
            	    finish();
            	   // startActivity(intent);
            	// Toast.makeText(getApplicationContext(), "Saved",
                 //        Toast.LENGTH_SHORT).show();
            }
            if(i==1){
            	//Toast.makeText(getApplicationContext(), "Saved",
                //        Toast.LENGTH_SHORT).show();
            Intent iImgEditor = new Intent(getApplicationContext(), ImageEditor.class);
            iImgEditor.putExtra("ImagePath", pictureFile.getAbsolutePath());
            ImageCaptureActivity.this.finish();
            startActivity(iImgEditor);
            }
            
            if(i==2){
            	deleteFile(resultUrl);
    			System.out.println("=============resultUrl======"+imageFilePath);
    			System.out.println("=============resultUrl======"+resultUrl);
    			//if(imageFilePath!=null){
    			// Starting recognition process
                Intent iImgEditor = new Intent(getApplicationContext(), ImageEditor.class);
                iImgEditor.putExtra("ImagePath", pictureFile.getAbsolutePath());
                iImgEditor.putExtra("bcard", 2);
                ImageCaptureActivity.this.finish();
                startActivity(iImgEditor);


    			//finish();
            }
        }
    }


    private File getOutputMediaFile(int type) throws IOException {

        File mediaFile;
        File dir = new File(Environment.getExternalStorageDirectory(),
                "/M_CamScanner");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File imgFolder = new File(dir.getAbsolutePath(), "/.Images");
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }
        if (type == 1427) {
            File imgFolder1 = new File(imgFolder.getAbsolutePath(), "/.Main");
            if (!imgFolder1.exists()) {
                imgFolder1.mkdirs();
            }
            mediaFile = new File(imgFolder1.getAbsolutePath() + File.separator
                    + "CamScanner_Img" + myPrefs.getDocCount() + ".jpg");
            myPrefs.setDocCount(myPrefs.getDocCount() + 1);
        } else {
            return null;
        }

        return mediaFile;
    }
    
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		/*
		 * super.onActivityResult(requestCode, resultCode, data); if (resultCode
		 * != RESULT_OK) return;
		 */
		// Toast.makeText(getApplicationContext(),
		// "resultcode="+resultCode+"requestcode="+requestCode,Toast.LENGTH_SHORT).show();
	/*	if (resultCode == 300) {

			latitude = data.getDoubleExtra("Lat", -1);
			longitude = data.getDoubleExtra("Lng", -1);
			address = data.getStringExtra("location");

			atvPlaces.setText(address);
			Toast.makeText(getApplicationContext(), "" + latitude,
					Toast.LENGTH_SHORT).show();
			// Toast.makeText(getApplicationContext(), ""+latitude,
			// Toast.LENGTH_SHORT).show();
			// Toast.makeText(getApplicationContext(), ""+longitude,
			// Toast.LENGTH_SHORT).show();
			// Toast.makeText(getApplicationContext(), address,
			// Toast.LENGTH_SHORT).show();

		}*/
    	
    	System.out.println("========requestCode="+requestCode);
    	System.out.println("========resultCode="+requestCode);
		switch (requestCode) {
		/*case PIC_CROP:
			// Create an instance of bundle and get the returned data
			Bundle extras = data.getExtras();
			// get the cropped bitmap from extras
			Bitmap thePic = extras.getParcelable("data");
			// set image bitmap to image view
			capture_ImageView.setImageBitmap(thePic);
			break;*/
	/*	case CAMERA_REQUEST:

			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "image" + timeinmilisecod + ".jpg");

			img_path = file.getAbsolutePath();

			System.out.println("===path===================" + img_path);
			if(file.exists()){

			addimages();
			setAdapter();
			}
			
			
			if (data != null) {
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {
			

					
					 * Bitmap thumbnail = extras2.getParcelable("data");
					 * 
					 * ByteArrayOutputStream bytes = new
					 * ByteArrayOutputStream();
					 * thumbnail.compress(Bitmap.CompressFormat.PNG, 100,
					 * bytes);
					 * 
					 * imageInByte = bytes.toByteArray();
					 * 
					 * Log.e("output before conversion",
					 * imageInByte.toString()); // Inserting Contacts
					 * Log.d("Insert: ", "Inserting ..");
					 * 
					 * encodedImage = Base64.encodeToString(imageInByte,
					 * Base64.DEFAULT); destination = new File(
					 * Environment.getExternalStorageDirectory(),
					 * System.currentTimeMillis() + ".jpg");
					 * 
					 * 
					 * System.out.print("path="+destination); FileOutputStream
					 * fo; try { destination.createNewFile(); fo = new
					 * FileOutputStream(destination);
					 * fo.write(bytes.toByteArray()); fo.close(); } catch
					 * (FileNotFoundException e) { e.printStackTrace(); } catch
					 * (IOException e) { e.printStackTrace(); }
					 * 
					 * Bitmap b = getHexagonShape(thumbnail);
					 * capture_ImageView.setImageBitmap(b);
					 * capture_ImageView.setVisibility(View.VISIBLE);
					 * addimages(); setAdapter();
					 }
			} else {
				// Toast.makeText(getApplicationContext(), "if No pic selected",
				// 300).show();
			}
			if (data != null) {
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {
					Bitmap thumbnail = extras2.getParcelable("data");
				
			//Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
		    imageInByte = bytes.toByteArray();

			Log.e("output before conversion", imageInByte.toString());
			// Inserting Contacts
			Log.d("Insert: ", "Inserting ..");

			encodedImage = Base64.encodeToString(imageInByte,
					Base64.DEFAULT);
		    File destination = new File(Environment.getExternalStorageDirectory(),
		            System.currentTimeMillis() + ".jpg");

		    FileOutputStream fo;
		    try {
		        destination.createNewFile();
		        fo = new FileOutputStream(destination);
		        fo.write(bytes.toByteArray());
		        fo.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		 // capture_ImageView.setImageBitmap(yourImage);
			Bitmap b = getHexagonShape(thumbnail);
		    capture_ImageView.setImageBitmap(b);
		    capture_ImageView.setVisibility(View.VISIBLE);
		    
			}
			}else {
				// Toast.makeText(getApplicationContext(), "if No pic selected",
				// 300).show();
			}

			

			break;*/
	/*	case PICK_FROM_GALLERY:			
			System.out.println("data" + data);
		if (data != null) {
			Uri selectedImage = data.getData();

			// capture_ImageView.setImageURI(selectedImage);
			try {
				// Get the file path from the URI
				img_path = FileUtils.getPath(this, selectedImage);
				System.out.println("uri  datapath " + img_path);
				addimages();
				setAdapter();
			} catch (Exception e) {
				Log.e("FileSelectorTestActivity", "File select error", e);
			}

			
			 * try { Bitmap bitmapImage = decodeBitmap(selectedImage); //
			 * convert bitmap to byte ByteArrayOutputStream stream = new
			 * ByteArrayOutputStream(); bitmapImage
			 * .compress(Bitmap.CompressFormat.PNG, 100, stream);
			 * imageInByte = stream.toByteArray();
			 * 
			 * Log.e("output before conversion", imageInByte.toString());
			 * 
			 * encodedImage = Base64.encodeToString(imageInByte,
			 * Base64.DEFAULT); //
			 * capture_ImageView.setImageBitmap(yourImage); Bitmap b =
			 * getHexagonShape(bitmapImage);
			 * capture_ImageView.setImageBitmap(b);
			 * capture_ImageView.setVisibility(View.VISIBLE);
			 * System.out.print("path=" + selectedImage); addimages();
			 * setAdapter(); } catch (FileNotFoundException e) {
			 * e.printStackTrace(); }
			 

		}
			break;
			
			
		case (5) :
		      if (resultCode == Activity.RESULT_OK) {
		        Uri contactData = data.getData();
		        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
		        if (c.moveToFirst()) {
		          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		          // TODO Fetch other Contact details as you want to use
		          mContactListTextview.setText(name);
		        }
		      }
		      break;*/
		      
		      
		case TAKE_PICTURE:
			imageFilePath = getOutputMediaFileUri().getPath();
			//Remove output file
			deleteFile(resultUrl);
			System.out.println("=============resultUrl======"+imageFilePath);
			System.out.println("=============resultUrl======"+resultUrl);
			//if(imageFilePath!=null){
			// Starting recognition process
			new AsyncProcessTask(ImageCaptureActivity.this).execute(pictureFile.getAbsolutePath(), resultUrl);
			
			//}
			  // Intent results = new Intent( this, ResultsActivity.class);
		    	//results.putExtra("IMAGE_PATH", imageFilePath);
		    	//results.putExtra("RESULT_PATH", resultUrl);
		    	//startActivity(results);
			break;
			
		/* case SELECT_FILE: {
			 if(data!=null){
			Uri imageUri = data.getData();

			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cur = getContentResolver().query(imageUri, projection, null, null, null);
			cur.moveToFirst();
			imageFilePath = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
			//Remove output file
			deleteFile(resultUrl);
			// Starting recognition process
			new AsyncProcessTask(NewEtcActivity.this).execute(imageFilePath, resultUrl);
			   //Intent results1 = new Intent( this, ResultsActivity.class);
		    	//results1.putExtra("IMAGE_PATH", imageFilePath);
		    	//results1.putExtra("RESULT_PATH", resultUrl);
		    	//startActivity(results1);
			}
		 }
			break;*/
		}
	}
}

