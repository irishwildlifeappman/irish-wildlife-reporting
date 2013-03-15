package org.andrewfenner.commonreporting;

import java.io.IOException;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

public class SendActivity extends Activity {
	
	int siteId= -1;

	private Button playbutton = null;
	
	private ArrayList<String> pictureNames = new ArrayList<String>();
	private int picturenumber = 1;
	private GestureDetector gestureScanner;
	private GestureDetector gestureScanner1;
	private MediaPlayer mediaPlayer = null;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       requestWindowFeature(Window.FEATURE_NO_TITLE); 
	       setContentView(R.layout.sendpage);
	       gestureScanner = new GestureDetector(this,simpleOnGestureListener); 
	       gestureScanner1 = new GestureDetector(this,simpleOnGesturePictureListener); 
			  
	       Bundle extras = getIntent().getExtras(); 
	       String name = extras.getString("butterflyname");
	       setupSound(name);
	      // setTitle(name + " © DHardiman & ButterflyIreland ");
	       Resources resources = getResources();
        
	          
	       String modifiedname = name.replace(" ", "").toLowerCase();
      	   Log.d(this.toString(),"trying to open " +modifiedname + " resources.getAssets() " + resources.getAssets().toString());
           
      	 	BitmapFactory.Options option = new BitmapFactory.Options();
			// System.out.println("Display OPtions "+option);
			
			option.inSampleSize =4;
			try
			{
				String names[] = resources.getAssets().list("");
				pictureNames.add(modifiedname+".jpg");
				for (int i = 0;i < names.length;i++)
				{
					Log.d(this.toString(), "Matching " + names[i] + " against " + ".*["+modifiedname+"].*\\.jpg");
					if (names[i].matches(".*("+modifiedname+").*\\.jpg"))
					{
						pictureNames.add(names[i]);
					}
				}
			      
			Drawable d = null;//Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(0)), null,option) ;//
			try {
        		d= Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(pictureNames.size()-1)), null) ;//
					
        	}
        	catch (Exception e)
        	{
        		// will try without options
        		d = Drawable.createFromStream( resources.getAssets().open(pictureNames.get(pictureNames.size()-1)), null) ;//
        	}
        	picturenumber = pictureNames.size()-1;
        	
        	ImageView arrowview = (ImageView) findViewById(R.id.arrowview);
        	Log.d(this.toString(), "Number of pic s " + pictureNames.size());
			
 		   if (pictureNames.size() == 2)
 		   {
 			  arrowview.setVisibility(View.INVISIBLE); 			
 		   }
 		   else
 		   {
 			  arrowview.setOnClickListener(mArrowClickListener);
 			  
 		   }
 		   
 		   
			Log.d(this.toString(),"d "+ d);
			ImageView iv = (ImageView) findViewById(R.id.imageView1);
		     iv.setOnTouchListener(mPictureTouchListener);
		   //  iv.setOnClickListener(mPictureClickListener);
			iv.setImageDrawable(d);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	       // Watch for button clicks.   
	        playbutton = (Button) findViewById(R.id.senddetails);    
	     
		
	        playbutton.setOnClickListener(mPlayListener);
			
	       // TextView editText = (TextView) findViewById(R.id.helptext);
	       // SunriseSunset sut = new SunriseSunset();
	    	
	       // final MonitorGpsTask mgt = MonitorGpsTask.getInstance();
	      //  Location lastLocationCell = mgt.getLastLocationCell();

	        
	        WebView webView = (WebView) findViewById(R.id.webView1);
			
	        webView.loadUrl("file:///android_asset/"+ modifiedname+".html");
	       
	     //   webView.setOnClickListener(mWebClickListener);
	        webView.setOnTouchListener(mWebTouchListener);
	   	 }

	 
	 
	  void setupSound(String name)
	  {
		  
		// Watch for button clicks.   
	        playbutton = (Button) findViewById(R.id.button1);    
	     
	        try {
				AssetFileDescriptor afd = getAssets().openFd(name.toLowerCase()+".wav");
				afd.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				playbutton.setVisibility(View.INVISIBLE);
			} 
        	
	        playbutton.setOnClickListener(mPlaySoundListener);
			
//	        Log.d(this.toString(), "looking for " + name);
//	        try
//	        {
//	        	AssetFileDescriptor afd = getAssets().openFd(name.toLowerCase()+".wav"); 
//	        	mediaPlayer = new MediaPlayer(); 
//	        	mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength()); 
//	        }
//	        catch (Exception e)
//	        {
//	        	Log.d(this.toString(), "problem setting up sound", e);
//	        }
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

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			this.finish();
		}
		
		
		 View.OnClickListener mPlayListener = new OnClickListener() 
		 {    
			 public void onClick(View v)
			 {     
				 Intent xxx =   new Intent(SendActivity.this,SaveDetailsActivity.class);

				 Bundle extras = getIntent().getExtras(); 
			     String name = extras.getString("butterflyname");
			     
	    		xxx.putExtra("butterflyname",  name);  
	    		
	    		startActivityForResult(xxx,1);
				 }  
			 };
			 
			 View.OnClickListener mArrowClickListener = new OnClickListener() 
			 {    
				 public void onClick(View v)
				 {   
					 ImageView iv = (ImageView) findViewById(R.id.imageView1);
					 Resources resources = getResources();
				        
			          
				    
			      	 	BitmapFactory.Options option = new BitmapFactory.Options();
						// System.out.println("Display OPtions "+option);
						
						option.inSampleSize =4;
						picturenumber++;
						if (picturenumber > pictureNames.size()-1)
						{
							picturenumber=1;
						}
						Drawable d = null;//Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(0)), null,option) ;//
						try {
			        		d= Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(picturenumber)), null) ;//
								
			        	}
			        	catch (Exception e)
			        	{
			        		try {
			        		d = Drawable.createFromStream( resources.getAssets().open(pictureNames.get(picturenumber)), null) ;//
			        		}
				        	catch (Exception e1)
				        	{
				        		
				        	}
			        	}
			        	
			        	iv.setImageDrawable(d);
				 }
			};
		  View.OnClickListener mPictureClickListener = new OnClickListener() 
			 {    
				 public void onClick(View v)
				 {     
					 Intent xxx =   null;
					 if (android.os.Build.VERSION.SDK_INT  >= 14)
					 {
						 xxx =   new Intent(SendActivity.this,FullScreenPictureActivity.class);
					 }
					 else
					 {
						 xxx =   new Intent(SendActivity.this,FullScreenPictureWebActivity.class);
					 }
			    		
					 Bundle extras = getIntent().getExtras(); 
				     String name = extras.getString("butterflyname");
				     
		    		xxx.putExtra("butterflyname",  name);  
		    		
		    		startActivity(xxx);
					 }  
				 };
	
			View.OnClickListener mWebClickListener = new OnClickListener() 
					 {    
						 public void onClick(View v)
						 {     
								Intent xxx =   new Intent(SendActivity.this,FullScreenWebActivity.class);
								 
					    		
								 Bundle extras = getIntent().getExtras(); 
							     String name = extras.getString("butterflyname");
							     
					    		xxx.putExtra("butterflyname",  name);  
					    		
					    		startActivity(xxx);
							 }  
						 };
						 
						 
				private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener()
				{       
					
				
				//	public boolean onDown(MotionEvent event) 
				//	{            return true;        
				//	}        
				//	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) 
				//	{     
				//		Log.d(null,"Fling");       
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
//					        		d= Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(picturenumber)), null,option) ;//
//										
//					        	}
//					        	catch (Exception e)
//					        	{
//					        		// will try without options
//					        		d = Drawable.createFromStream( resources.getAssets().open(pictureNames.get(picturenumber)), null) ;//
//					        	}
//								ImageView iv = (ImageView) findViewById(R.id.imageView1);
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
					//	return false;
				//}
				
				public boolean onSingleTapConfirmed(MotionEvent e)  
				{
					Log.d(null,"onSingleTapConfirmed " + android.os.Build.VERSION.SDK_INT);       
					 Intent xxx =   null;
					 
					 xxx =   new Intent(SendActivity.this,FullScreenWebActivity.class);
					
					 Bundle extras = getIntent().getExtras(); 
				     String name = extras.getString("butterflyname");
				     
		    		xxx.putExtra("butterflyname",  name);  
		    		
		    		startActivity(xxx);
					return false;
				}
//				public boolean onSingleTapUp(MotionEvent e)  
//				{
//					Log.d(null,"onSingleTapUp");  
//					return false;
//				}
//				public void onLongPress(MotionEvent e)  
//				{
//					Log.d(null,"onLongPress");  
//				//	return false;
//				}
//				public void onScroll(MotionEvent e)  
//				{
//					Log.d(null,"onScroll");  
//				//	return false;
//				}
				};
				View.OnTouchListener mPictureTouchListener = new View.OnTouchListener() 
				{
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// Log.d(null,"onTouch " + arg1);    
					 
					boolean result =  gestureScanner1.onTouchEvent(arg1);
					 Log.d("mPictureTouchListener","onTouch " + result +" "+ arg1);    
					return true;
				} 

				};
				View.OnTouchListener mWebTouchListener = new View.OnTouchListener() 
				{
				public boolean onTouch(View arg0, MotionEvent arg1) {
					 Log.d(null,"onTouch " + arg1);    
					  gestureScanner.onTouchEvent(arg1);
					return false;
				} 
				};

				
				private GestureDetector.SimpleOnGestureListener simpleOnGesturePictureListener = new GestureDetector.SimpleOnGestureListener()
				{       
					
				
				//	public boolean onDown(MotionEvent event) 
				//	{            return true;        
				//	}        
					public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) 
					{     
						Log.d(null,"Fling");       
					int dx = (int) (event2.getX() - event1.getX());      
					// don't accept the fling if it's too short    
					// as it may conflict with a button push        
					if (Math.abs(dx) > 3 && Math.abs(velocityX) > Math.abs(velocityY))
					{            
						  Resources resources = getResources();
							BitmapFactory.Options option = new BitmapFactory.Options();
							// System.out.println("Display OPtions "+option);
							picturenumber++;
							if (picturenumber >= pictureNames.size())
							{
								picturenumber=0;
							}
						//	option.inSampleSize =2;
							try
							{
								//Drawable d = Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(picturenumber)), null,option) ;//
								Drawable d = null;//Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(0)), null,option) ;//
								try {
					        		d= Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(pictureNames.get(picturenumber)), null) ;//
										
					        	}
					        	catch (Exception e)
					        	{
					        		// will try without options
					        		d = Drawable.createFromStream( resources.getAssets().open(pictureNames.get(picturenumber)), null) ;//
					        	}
								ImageView iv = (ImageView) findViewById(R.id.imageView1);
								
								iv.setImageDrawable(d);
							}
							catch(Exception e)
							{
								e.printStackTrace();
								Log.e(this.toString(), "Problem with fling on " + picturenumber + " " + pictureNames);
							}
							
							
							//
						return true;     
					} 
					else 
					{          
						return false;          
					}       
					
				}
				
				public boolean onSingleTapConfirmed(MotionEvent e)  
				{
					Log.d(null,"onSingleTapConfirmed " + android.os.Build.VERSION.SDK_INT);       
					Intent xxx =   null;
					 if (android.os.Build.VERSION.SDK_INT  >= 14)
					 {
						 xxx =   new Intent(SendActivity.this,FullScreenPictureActivity.class);
					 }
					 else
					 {
						 xxx =   new Intent(SendActivity.this,FullScreenPictureWebActivity.class);
					 }
			    		
					 Bundle extras = getIntent().getExtras(); 
				     String name = extras.getString("butterflyname");
				     
		    		xxx.putExtra("butterflyname",  name);  
		    		xxx.putExtra("picturenumber",  picturenumber);  
		    		startActivity(xxx);
					return true;
				}
//				public boolean onSingleTapUp(MotionEvent e)  
//				{
//					Log.d(null,"onSingleTapUp");  
//					return false;
//				}
//				public void onLongPress(MotionEvent e)  
//				{
//					Log.d(null,"onLongPress");  
//				//	return false;
//				}
//				public void onScroll(MotionEvent e)  
//				{
//					Log.d(null,"onScroll");  
//				//	return false;
//				}
				};
		
			
				 View.OnClickListener mPlaySoundListener = new OnClickListener() 
				 {    
					 public void onClick(View v)
					 {     
						 try{
							 if (mediaPlayer !=null)
							 {
								 mediaPlayer.reset();
							 }
							   Bundle extras = getIntent().getExtras(); 
						       String name = extras.getString("butterflyname");
						       name = name.replace(" ", "");
						       Log.d(this.toString(), "looking for " + name);
						        try
						        {
						        	AssetFileDescriptor afd = getAssets().openFd(name.toLowerCase()+".wav"); 
						        	mediaPlayer = new MediaPlayer(); 
						        	mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength()); 
						        }
						        catch (Exception e)
						        {
						        	Log.d(this.toString(), "problem setting up sound", e);
						        }
							if (mediaPlayer != null)
							{						 
								mediaPlayer.prepare(); 

								mediaPlayer.start(); // no need to call prepare(); create() does that for you
							}
						 }
						 catch (Exception e)
						 {
							 Log.d(this.toString(), "Got an exception playing sound", e);
						 }
						 }  
					 };		
				
}
