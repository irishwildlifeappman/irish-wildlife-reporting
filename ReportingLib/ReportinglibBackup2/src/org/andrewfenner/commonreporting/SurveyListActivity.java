package org.andrewfenner.commonreporting;

import java.io.File;
import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


//adb -s emulator-5554 logcat
public class SurveyListActivity extends Activity implements LocationListener {
	private MonitorGpsTask mgt = null;
	private  ImageAdapter theImageAdapter ;
	
	private Button helpbutton;
	private Button submitbutton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.butterflylist);
   
        mgt = MonitorGpsTask.getInstance( (LocationManager) getSystemService(Context.LOCATION_SERVICE));
	        new Thread(
	        		mgt).start();
	        
	        init();
    
	        // Watch for button clicks.   
	        helpbutton = (Button) findViewById(R.id.helpButton);    
	        helpbutton.setOnClickListener(mHelpListener);
	        
	        submitbutton = (Button) findViewById(R.id.submitButton);    
	        submitbutton.setOnClickListener(mSubmitListener);
        
    
	}

    /* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		
		System.out.println(" got resume ");
		MonitorGpsTask mgt = MonitorGpsTask.getInstance( );
		mgt.subscribe(this);
		init();
		
	}
    	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		
		MonitorGpsTask mgt = MonitorGpsTask.getInstance( );
	    
		mgt.unsubscribe(true);
	}
    
	private void init()
	{
		
		 ResultFileHelper filex = new ResultFileHelper(
					getApplicationContext().getExternalFilesDir(null));
	       ArrayList<String> readData = filex.readData();
	        submitbutton = (Button) findViewById(R.id.submitButton);    
	       
	        submitbutton.setText("Submit " + readData.size() +" Records");
	        
		 Bundle extras = getIntent().getExtras(); 
	     String groupname = extras.getString("groupname");
		  if (groupname == null || groupname.equalsIgnoreCase("all")|| groupname.equalsIgnoreCase("null"))
	        {
	        	butteryflynames = Constants.speciesnameslist;
	        }
	        else
	        {
	        	ArrayList<String> templist = new ArrayList<String>();
	        	for(int i=0;i<Constants.speciesgroups.length;i++)
	        	{
	        		if (Constants.speciesgroups[i].equalsIgnoreCase(groupname))
	        		{
	        			templist.add(Constants.speciesnameslist[i]);
	        		}
	        	}
	        	butteryflynames = templist.toArray(new String[]{});
	        }
	        
	        
	        GridView g = (GridView) findViewById(R.id.butterflygridview);
	        theImageAdapter = new ImageAdapter(this);
	        configureYourUIHere();
	        
	        g.setAdapter(theImageAdapter);
	     
	}
	
	 private void removedata() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		 builder.setMessage("Are you sure you want to remove the saved records")
		 .setCancelable(false)
		 .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
		 	{           
			 	public void onClick(DialogInterface dialog, int id) 
			 	{     
			 		 ResultFileHelper filex1 = new ResultFileHelper(getApplicationContext().getExternalFilesDir(null));
		       		 filex1.removeData();
			 		dialog.cancel();  
			 		 submitbutton.setText("Submit 0 Records");
			 		  }       
			 	})
			.setNegativeButton("No", new DialogInterface.OnClickListener() 
		 	{           
			 	public void onClick(DialogInterface dialog, int id) 
			 	{              
			 		dialog.cancel();  
			 		 
			 		  }       
			 	}) 	;
		 AlertDialog alert = builder.create();
		 alert.show();
		 
			
		}
	 private void getsummarydata() {
		 new GetSummaryTask(SurveyListActivity.this)
			.execute();
	}
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {

	        // Inflate our menu which can gather user input for switching camera
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.main_menu, menu);
	        return true;
	    }
	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		  int id = item.getItemId();
		  if(id == R.id.itemsummary) {
			  getsummarydata();
		  }
		  else if(id == R.id.itemremove) {
			  removedata();
		  }
		  else if(id == R.id.itemhistory) {
			  showhistory();
		  }
		  else if(id == R.id.itemdeletehistory) {
			  deletehistory();
		  }
		  else
		  {
			  return super.onOptionsItemSelected(item);
		  }
		  return true;
		 
	       
	    	
	    }
    
	
		 private void showhistory() {
				// TODO Auto-generated method stub
				 Intent myIntent = new Intent(Intent.ACTION_VIEW); 
				 File locationFile = new File(getExternalFilesDir(null), Constants.SETTINGCONST + "history.csv");
					
				 //myIntent.setData(); 
							 
				 Uri uri = Uri.fromFile(locationFile);
				// Uri uri =  Uri.parse("http://www.google.com");
				// myIntent.setType("text/csv"); 	 
				 myIntent.setDataAndType(uri, "text/csv");
				 // file:///mnt/sdcard/Android/data/org.biologyie.roadkill/files/RoadkillApphistory.csv
				// 

				 Log.d(this.toString(), "intent " + myIntent +" \n " + Uri.fromFile(locationFile) + " "
						 + myIntent.getData() +" uri " + uri);
				// Intent j = Intent.createChooser(myIntent, "Choose an application to open with:"); 
				 startActivity(myIntent); 

			}
		 
		 private void deletehistory() {
				// TODO Auto-generated method stub
				 Intent myIntent = new Intent(Intent.ACTION_VIEW); 
				 File locationFile = new File(getExternalFilesDir(null), Constants.SETTINGCONST + "history.csv");
				 locationFile.delete();
				

				 Log.d(this.toString(), "Deleted file " +  Uri.fromFile(locationFile) + " "
						);
				
			}
	
	




	public class ImageAdapter extends BaseAdapter {
	        private int height;
			private int width;
			private int numbercolums;

			public ImageAdapter(Context c) {
	            mContext = c;
	        }

	        public void setSize(int height,int width,int numbercolums)
	        {
	        	this.height = height;
	        	this.width = width;
	        	this.numbercolums = numbercolums;
	        }
	        public int getCount() {
	            return butteryflynames.length;
	        }

	        public Object getItem(int position) {
	            return position;
	        }

	        public long getItemId(int position) {
	            return position;
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {
	        	
	        	 Log.d(this.toString(),"position " + position +" convertView " + convertView);
	        	
		           
		        	View ll = null;
		        	ImageView buttonView = null;
		        	 TextView textview = null;
//		        	if (convertView == null || position == 0) 
//		        	{
//		        	
		        	LayoutInflater inflater = (LayoutInflater)mContext.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		        	
		        	 ll = inflater.inflate(R.layout.griditem, null);
		        	 buttonView = (ImageView)ll.findViewById(R.id.butterflypic);
		        	  textview = (TextView)ll.findViewById(R.id.butterflyname);
		        	 
//		        }
//		        else
//		        {
//		        	ll = convertView;
//		        	 buttonView = (ImageView)ll.findViewById(R.id.butterflypic);
//		        	  textview = (TextView)ll.findViewById(R.id.butterflyname);
//		        }
		            
	        	
	        	
	           
	           
	            	
	            	

	           
                AfrButtonListener l = new AfrButtonListener();
                l.setPosition(position);
                
                buttonView.setOnClickListener(l);
          //      if (numbercolums==1)
            //    {
           //     	 ll.setLayoutParams(new GridView.LayoutParams(width, height/2));	
          //      }
            //    else{
             //   	ll.setLayoutParams(new GridView.LayoutParams(150, 150));
             //   }
             //   imageView.setAdjustViewBounds(false);
             //   imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           //     buttonView.setPadding(8, 8, 8, 16);
                // buttonView.setBackgroundResource(mThumbIds[position]);
	            try
	            {
	            	//InputStream is = null; 
	            //	try { 
	            	//	is = mContext.getResources().getAssets().open("brimstone.jpg");
	            //		}
	            	//catch (IOException e)
	            //	{ 
	            //		Log.w("EL", e); 
	            //		} 
	            	Resources resources = mContext.getResources();
	            	//Drawable d = Drawable.createFromPath("file:///android_assets/brimstone.jpg");
	            	
	            	String name = butteryflynames[position];
	            	textview.setText(name);
	            	textview.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
	            	String modifiedname = name.replace(" ", "").toLowerCase();
	            	 Log.d(this.toString(),"trying to open " +modifiedname + " resources.getAssets() " + resources.getAssets().toString());
	  	           
	  	         BitmapFactory.Options option = new BitmapFactory.Options();
					// System.out.println("Display OPtions "+option);
					
	  	         if (Constants.picturesize == true)
	  	         {
					option.inSampleSize =4;
	  	         }
	  	         else
	  	         {
	  	        	option.inSampleSize =1;
	  	         }
					option.inJustDecodeBounds = false;
	            	Bitmap d = null;
	            	try {
	            		AssetFileDescriptor openFd = resources.getAssets().openFd(modifiedname+".jpg");
	            		
	            		//d = Drawable.createFromStream( resources.getAssets().open(modifiedname+".jpg"), null) ;//
	            		d = BitmapFactory.decodeStream(resources.getAssets().open(modifiedname+".jpg"), null, option);
	            		//d= Drawable.createFromResourceStream(resources,new TypedValue(), resources.getAssets().open(modifiedname+".jpg"), null) ;//
							
	            	}
	            	catch (Exception e)
	            	{
	            		// will try without options
	            		AssetFileDescriptor openFd = resources.getAssets().openFd(modifiedname+".jpg");
	            		
	            		// d = Drawable.createFromStream( resources.getAssets().open(modifiedname+".jpg"), null) ;//
	            		d = BitmapFactory.decodeStream(resources.getAssets().open(modifiedname+".jpg"), null, null);
	            	}
					
	            	Log.d(this.toString(),"d "+ d);
	            	if (d != null){
	            		buttonView.setImageBitmap(d);
	            	}
	            }
	            catch(Exception e)
	            {
	            	e.printStackTrace();
	            }
	          
	         //   buttonView.setText(""+position);
	          //  imageView.setImageResource(R.drawable.one);

	         
	            return ll;
	        }

	        private Context mContext;

	    
	    }
	 
	    public class AfrButtonListener implements Button.OnClickListener
	    {
	    	private Integer position = 0;
			
			public void setPosition(Integer pos)
			{
				position = pos;
			}

			
			@Override
			public void onClick(View v) {
				Log.d(this.toString(), "Got a click of a button " + position + " v " + v.toString() );
				
			//	Button b = (Button)v;
			//	b.setText(""+number);
			//	b.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
			//	b.setTextColor(Color.RED);
				String name = butteryflynames[position];
				// createdialog(v,name);
				Intent xxx =   new Intent(SurveyListActivity.this,SendActivity.class);
				 
	    		
	    			
	    		//File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");   
	    		xxx.putExtra("butterflyname",  name);  
	    		// imageUri = Uri.fromFile(photo); 
	    		//startActivityForResult(camera, 1); 
	    		startActivity(xxx);
			} 
	    }
	 
	    private String[] butteryflynames;

	    public void onConfigurationChanged(Configuration newConfig)
	    {
	    	super.onConfigurationChanged(newConfig);     
	   
	    }

		private void configureYourUIHere() {
			Log.d(this.toString(), "changing orientation" + getResources().getConfiguration().orientation);
			GridView gv = (GridView) findViewById(R.id.butterflygridview);   
			Display display = getWindowManager().getDefaultDisplay(); 
			int width = display.getWidth(); 
			int height = display.getHeight();	
			
			//if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)       
			//		{
			//			gv.setNumColumns(4);  
			//			this.theImageAdapter.setSize(height, width, 4);
			//		}
			//	else       
			//		{
						gv.setNumColumns(2); 
						this.theImageAdapter.setSize(height, width, 2);
									
			//		}
			
			
		}

		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		} 
		

 
		
		
		 View.OnClickListener mHelpListener = new OnClickListener() 
		 {    
			 public void onClick(View v)
			 {     
				 Intent xxx =   new Intent(getApplicationContext(),FullScreenWebActivity.class);
				 	xxx.putExtra("butterflyname",  "helptext");  
	    		
				 	startActivity(xxx);
				 }  
			 };
			 
		View.OnClickListener mSubmitListener = new OnClickListener() 
			 {    
				 public void onClick(View v)
				 {     
					 SharedPreferences settings = getSharedPreferences(Constants.SETTINGCONST, 0);  
		        	 final Boolean UPLOADtype = settings.getBoolean("UPLOAD",true);   
		        	 Boolean EMAILtype = settings.getBoolean("EMAIL",false);   
			        
		        	 
		        	 
				if (UPLOADtype || EMAILtype) {
					ResultFileHelper filex = new ResultFileHelper(
							getApplicationContext().getExternalFilesDir(null));

					
					ArrayList<String> readData = filex.readData();
					if (readData.size() != 0)
					{
						
						Intent	xxx = new Intent(SurveyListActivity.this,SendDataActivity.class);
						
						//xxx.putExtra("animalname", (String)piclistadapter.getItem(arg2));
					
		        		startActivity(xxx);	
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Nothing to write ", Toast.LENGTH_SHORT).show();
       	            
					}

				
				}
					 }  
				 };
		
}