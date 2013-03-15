package org.andrewfenner.commonreporting;

import java.io.IOException;
import java.util.ArrayList;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

public class FullScreenPictureActivity extends Activity {

	
	private ArrayList<String> pictureNames = new ArrayList<String>();
	private int picturenumber = 1;

	 
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	      // requestWindowFeature(Window.FEATURE_NO_TITLE); 
	       setContentView(R.layout.fullscreenpicture);
	     //  gestureScanner = new GestureDetector(this,simpleOnGestureListener); 
			  
	       Bundle extras = getIntent().getExtras(); 
	       String name = extras.getString("butterflyname");
	        picturenumber = extras.getInt("picturenumber");
	       
	       setTitle(name);
	       Resources resources = getResources();
     
	          
	       String modifiedname = name.replace(" ", "").toLowerCase();
	       String names[] = {};
	       try {
			names = resources.getAssets().list("");
	       } catch (IOException e) {
	    	   // TODO Auto-generated catch block
	    	   e.printStackTrace();
	       }
	       pictureNames.add(modifiedname+".jpg");
			for (int i = 0;i < names.length;i++)
			{
				Log.d(this.toString(), "Matching " + names[i] + " against " + ".*["+modifiedname+"].*\\.jpg");
				if (names[i].matches(".*("+modifiedname+").*\\.jpg"))
				{
					pictureNames.add(names[i]);
				}
			}
   	   //<html><body><img border="0" hspace="0"
   	// src="assets/brimstonedistributionmap.jpg" width=244 height=313></p>
   	//</body>
   	//</html>
   	   String webpic = "<html><body><img ";
   	 webpic+=" src=\"assets/"+pictureNames.get(picturenumber)+"\">";
   	 webpic+="</body></html>";
   	com.polites.android.GestureImageView webview = (com.polites.android.GestureImageView) findViewById(R.id.fullscreenbutterfly);
//		   
    BitmapFactory.Options option = new BitmapFactory.Options();
	// System.out.println("Display OPtions "+option);
	
	//option.inSampleSize =4;
	//option.inJustDecodeBounds = false;
	Bitmap d = null;
	try {
	//	AssetFileDescriptor openFd = resources.getAssets().openFd(pictureNames.get(picturenumber)+".jpg");
		
		//d = Drawable.createFromStream( resources.getAssets().open(modifiedname+".jpg"), null) ;//
		d = BitmapFactory.decodeStream(resources.getAssets().open(pictureNames.get(picturenumber)), null, option);
		//d= Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(modifiedname+".jpg"), null) ;//
			
	}
	catch (Exception e)
	{
		// will try without options
	//	AssetFileDescriptor openFd = resources.getAssets().openFd(modifiedname+".jpg");
		
		// d = Drawable.createFromStream( resources.getAssets().open(modifiedname+".jpg"), null) ;//
	//	d = BitmapFactory.decodeStream(resources.getAssets().open(modifiedname+".jpg"), null, null);
	}
   	webview.setImageBitmap(d);
 //  	webview.getSettings().setBuiltInZoomControls(true);
   	 Log.d(this.toString(),"trying to open updated " +webpic);
   //	 webview.loadUrl("file:///android_asset/"+ "brimstonepic"+".html");
  // 	webview.loadDataWithBaseURL("http://irishwildlifereporting.appspot.com", webpic, "text/html", "UTF-8", "about:blank");
  // 	webview.loadDataWithBaseURL("file:///android_asset/"+ "brimstonepic"+".html", webpic, "text/html", "UTF-8", "about:blank");

   	
   //	browser.loadDataWithBaseURL("http://www.example.com",  
   //          htmlContent,  
   //          "text/html",  
   //          "utf-8",  
   //          null);
  // 	webview.setOnTouchListener(this);
   	
  // 	 if (!webview.getSettings().supportZoom())
	//        {
	//        	setTitle(name +" : Sorry no zoom in this android version");
	//        }

   
	       
	    	
	 }
	 /* Request updates at startup */
		@Override
		protected void onResume() {
			super.onResume();
			
			
		}

		/* Remove the locationlistener updates when Activity is paused */
		@Override
		protected void onPause() {
			super.onPause();
			
			
		   
		
		}

		
		
			 
	
			 
				


		
}
