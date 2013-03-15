package org.andrewfenner.commonreporting;

import java.util.ArrayList;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.webkit.WebView;

public class FullScreenWebActivity extends Activity {
	

//	private LocationManager locationManager;
	

	
	
	
	
	
	private ArrayList<String> pictureNames = new ArrayList<String>();
	private int picturenumber = 1;
	private GestureDetector gestureScanner;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	      // requestWindowFeature(Window.FEATURE_NO_TITLE); 
	       setContentView(R.layout.fullscreenweb);
	      
	       Bundle extras = getIntent().getExtras(); 
	       String name = extras.getString("butterflyname");
	     
	       setTitle(name);
	     //  setTitle(name + " © DHardiman & ButterflyIreland ");
	       Resources resources = getResources();
        
	          
	       String modifiedname = name.replace(" ", "").toLowerCase();
      	   Log.d(this.toString(),"trying to open " +modifiedname + " resources.getAssets() " + resources.getAssets().toString());
      	   WebView webView = (WebView) findViewById(R.id.webView1);
			
	        webView.loadUrl("file:///android_asset/"+ modifiedname+".html");
	        
	        webView.getSettings().setBuiltInZoomControls(true);
	        if (!webView.getSettings().supportZoom())
	        {
	        	setTitle(name +" : Sorry no zoom in this android version");
	        }
	      //  wv.getSettings().setDisplayZoomControls(false); 
	       
	    	
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
