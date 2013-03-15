package org.andrewfenner.commonreporting;

import java.util.ArrayList;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.ListView;

public class SettingListActivity extends Activity {
	

//	private LocationManager locationManager;
	

	
	
	
	
	
	private ArrayList<String> pictureNames = new ArrayList<String>();
	private int picturenumber = 1;
	private GestureDetector gestureScanner;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	      // requestWindowFeature(Window.FEATURE_NO_TITLE); 
	       setContentView(R.layout.settinglist);
	      
	       ListView list1 = (ListView) findViewById(R.id.listView1); 
	    
	       String []settingName = {"GROUPING","UPLOAD"};
	       Boolean []settingbool = {true,true};
	       SettingListAdapter settinglistadapter =new  SettingListAdapter(this,settingName,settingbool);
	    	list1.setAdapter(settinglistadapter);
	      
	       
	       
	    	
	 }
	 /* Request updates at startup */
		@Override
		protected void onResume() {
			super.onResume();
			Log.d(this.toString(), "Setting activity resuiume");
			
		}

		/* Remove the locationlistener updates when Activity is paused */
		@Override
		protected void onPause() {
			super.onPause();
			Log.d(this.toString(), "Setting activity pause");
			
			
		   
		
		}

		
		
			 
	
			 
			
		
}
