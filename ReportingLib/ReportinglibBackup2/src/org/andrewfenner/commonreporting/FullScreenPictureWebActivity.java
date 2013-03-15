package org.andrewfenner.commonreporting;
import java.io.IOException;
import java.util.ArrayList;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class FullScreenPictureWebActivity extends Activity { //implements OnTouchListener
	

//	private LocationManager locationManager;
	

	
	
	
	
	
	private ArrayList<String> pictureNames = new ArrayList<String>();
	private int picturenumber = 1;
//	private GestureDetector gestureScanner;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	      // requestWindowFeature(Window.FEATURE_NO_TITLE); 
	       setContentView(R.layout.fullscreenwebpicture);
	  //     gestureScanner = new GestureDetector(this,simpleOnGestureListener); 
			  
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
      	WebView webview = (WebView) findViewById(R.id.fullscreenwebbutterfly);
//		   
      	webview.getSettings().setBuiltInZoomControls(true);
      	 Log.d(this.toString(),"trying to open " +webpic);
      //	 webview.loadUrl("file:///android_asset/"+ "brimstonepic"+".html");
      	webview.loadDataWithBaseURL("file:///android_asset/"+ "brimstonepic"+".html", webpic, "text/html", "UTF-8", "about:blank");

   //   	webview.setOnTouchListener(this);
      	// webview.loadData(webpic, "text/html", null);
      	 //	BitmapFactory.Options option = new BitmapFactory.Options();
			// System.out.println("Display OPtions "+option);
			
			//option.inSampleSize =2;
//			try
//			{
//				String names[] = resources.getAssets().list("");
//				pictureNames.add(modifiedname+".jpg");
//				for (int i = 0;i < names.length;i++)
//				{
//					Log.d(this.toString(), "Matching " + names[i] + " against " + ".*["+modifiedname+"].*\\.jpg");
//					if (names[i].matches(".*("+modifiedname+").*\\.jpg"))
//					{
//						pictureNames.add(names[i]);
//					}
//				}
//			      
//			Drawable d = null;//Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(0)), null,option) ;//
//			try {
//        		d= Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(picturenumber)), null,option) ;//
//					
//        	}
//        	catch (Exception e)
//        	{
//        		// will try without options
//        		d = Drawable.createFromStream( resources.getAssets().open(pictureNames.get(picturenumber)), null) ;//
//        	}
//        	picturenumber = pictureNames.size()-1;
//			Log.d(this.toString(),"d "+ d);
//			ImageView iv = (ImageView) findViewById(R.id.fullscreenbutterfly);
//		     iv.setOnTouchListener(this);
//			iv.setImageDrawable(d);
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
//	       
	       
	    	
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

		
		
			 
	
			 
//				private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener()
//				{       
//					public boolean onDown(MotionEvent event) 
//					{            return true;        
//					}        
//					public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) 
//					{     
//						Log.d(null,"Fling");       
//					int dx = (int) (event2.getX() - event1.getX());      
//					// don't accept the fling if it's too short    
//					// as it may conflict with a button push        
//					if (Math.abs(dx) > 3 && Math.abs(velocityX) > Math.abs(velocityY))
//					{            
//						  Resources resources = getResources();
//							BitmapFactory.Options option = new BitmapFactory.Options();
//							// System.out.println("Display OPtions "+option);
//							picturenumber++;
//							if (picturenumber >= pictureNames.size())
//							{
//								picturenumber=0;
//							}
//						//	option.inSampleSize =2;
//							try
//							{
//								//Drawable d = Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(picturenumber)), null,option) ;//
//								Drawable d = null;//Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(0)), null,option) ;//
//								try {
//					        		d= Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(picturenumber)), null) ;//
//					        		//d= Drawable.createFromResourceStream(res, value, is, srcName)(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(picturenumber)), null,option) ;//
//										
//					        	}
//					        	catch (Exception e)
//					        	{
//					        		// will try without options
//					        		d = Drawable.createFromStream( resources.getAssets().open(pictureNames.get(picturenumber)), null) ;//
//					        	}
//								ImageView iv = (ImageView) findViewById(R.id.fullscreenbutterfly);
//								
//								iv.setImageDrawable(d);
//							}
//							catch(Exception e)
//							{
//								e.printStackTrace();
//								Log.e(this.toString(), "Problem with fling on " + picturenumber + " " + pictureNames);
//							}
//							
//							
//							//
//						return true;     
//					} 
//					else 
//					{          
//						return false;          
//					}       
//					}    
//				};
//				@Override
//				public boolean onTouch(View arg0, MotionEvent arg1) {
//					// Log.d(null,"onTouch " + arg1);    
//					 
//					return gestureScanner.onTouchEvent(arg1);
//				} 


		
}