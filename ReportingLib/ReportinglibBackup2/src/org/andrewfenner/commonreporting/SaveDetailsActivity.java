package org.andrewfenner.commonreporting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class SaveDetailsActivity extends Activity implements LocationListener {
	

	private Location lastLocation = null;
	private String savedlocation = null;
	private String savedTextlocation = null;
	// private String pictureName = "notSet";
	private AsyncTask<Void, String, String> execute = null;
	
	private static final int  DATE_DIALOG_ID = 1;
	private static final int  TIME_DIALOG_ID = 2;
	
	private UploadFileTask fileTask;
	 boolean dialogresult = false;
	private int year;
	private int month;
	private int day;
	
	private int hour;
	private int min;
	
	private int lat =  0;
	private int lng  = 0 ;
	private int thePosition = -1;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
			
	       super.onCreate(savedInstanceState);
	      // requestWindowFeature(Window.FEATURE_NO_TITLE); 
	       setContentView(R.layout.recordedinfo);
	      
	       Bundle extras = getIntent().getExtras(); 
	       String name = extras.getString("animalname");
	        name = extras.getString("butterflyname");
		     
	   //    setupPictureButton();
	       if (name == null && extras.getInt("theposition",-1) != -1)
	       {
	    	   
	    	   thePosition  = extras.getInt("theposition");
	    	   name = "dummy " + extras.getInt("theposition");
	    	   setupPosition(extras.getInt("theposition") );
	       }
	       else
	       {
	    	   setupNormal(name);
	       }
	      
	    	
	 }
	 
	 private List<String> createList()
	 {
		   final List<String> list=new ArrayList<String>();
	       list.add("Not Set");
	       list.add("Cultivated land(BC)");
	       list.add("Arable crops(BC1)");
	       list.add("Horticultural land(BC2)");
	       list.add("Tilled land(BC3)");
	       list.add("Flower beds and borders(BC4)");
	       list.add("Built land(BL)");
	       list.add("Stone walls and other stonework(BL1)");
	       list.add("Earth banks(BL2)");
	       list.add("Buildings and artificial surfaces(BL3)");
	       list.add("Shingle and gravel banks(CB)");
	       list.add("Shingle and gravel banks(CB1)");
	       list.add("Coastal constructions(CC)");
	       list.add("Sea walls, piers and jetties(CC1)");
	       list.add("Sand dune systems(CD)");
	       list.add("Embryonic dunes(CD1)");
	       list.add("Marram dunes(CD2)");
	       list.add("Fixed dunes(CD3)");
	       list.add("Dune scrub and woodland(CD4)");
	       list.add("Dune slacks(CD5)");
	       list.add("Machair(CD6)");
	       list.add("Salt marshes(CM)");
	       list.add("Lower salt marsh(CM1)");
	       list.add("Upper salt marsh(CM2)");
	       list.add("Sea cliffs and islets(CS)");
	       list.add("Rocky sea cliffs(CS1)");
	       list.add("Sea stacks and islets(CS2)");
	       list.add("Sedimentary sea cliffs(CS3)");
	       list.add("Brackish waters(CW)");
	       list.add("Lagoons and saline lakes(CW1)");
	       list.add("Tidal rivers(CW2)");
	       list.add("Disturbed ground(ED)");
	       list.add("Exposed sand, gravel or till(ED1)");
	       list.add("Spoil and bare ground(ED2)");
	       list.add("Recolonising bare ground(ED3)");
	       list.add("Active quarries and mines(ED4)");
	       list.add("Refuse and other waste(ED5)");
	       list.add("Exposed rock(ER)");
	       list.add("Exposed siliceous rock(ER1)");
	       list.add("Exposed calcareous rock(ER2)");
	       list.add("Siliceous scree and loose rock(ER3)");
	       list.add("Calcareous scree and loose rock(ER4)");
	       list.add("Underground rock and caves(EU)");
	       list.add("Non-marine caves(EU1)");
	       list.add("Artificial underground habitats(EU2)");
	       list.add("Lakes and ponds(FL)");
	       list.add("Dystrophic lakes(FL1)");
	       list.add("Acid oligotrophic lakes(FL2)");
	       list.add("Limestone/marl lakes(FL3)");
	       list.add("Mesotrophic lakes(FL4)");
	       list.add("Eutrophic lakes(FL5)");
	       list.add("Turloughs(FL6)");
	       list.add("Reservoirs(FL7)");
	       list.add("Other artificial lakes and ponds(FL8)");
	       list.add("Springs(FP)");
	       list.add("Calcareous springs(FP1)");
	       list.add("Non-calcareous springs(FP2)");
	       list.add("Swamps(FS)");
	       list.add("Reed and large sedge swamps(FS1)");
	       list.add("Tall-herb swamps(FS2)");
	       list.add("Watercourses(FW)");
	       list.add("Eroding/upland rivers(FW1)");
	       list.add("Depositing/lowland rivers(FW2)");
	       list.add("Canals(FW3)");
	       list.add("Drainage ditches(FW4)");
	       list.add("Improved grassland(GA)");
	       list.add("Improved agricultural grassland(GA1)");
	       list.add("Amenity grassland (improved)(GA2)");
	       list.add("Freshwater marsh(GM)");
	       list.add("Marsh(GM1)");
	       list.add("Semi-natural grassland(GS)");
	       list.add("Dry calcareous and neutral grassland(GS1)");
	       list.add("Dry meadows and grassy verges(GS2)");
	       list.add("Dry-humid acid grassland(GS3)");
	       list.add("Wet grassland(GS4)");
	       list.add("Dense bracken(HD)");
	       list.add("Dense bracken(HD1)");
	       list.add("Heath(HH)");
	       list.add("Dry siliceous heath(HH1)");
	       list.add("Dry calcareous heath(HH2)");
	       list.add("Wet heath(HH3)");
	       list.add("Montane heath(HH4)");
	       list.add("Littoral rock(LR)");
	       list.add("Littoral sediment(LS)");
	       list.add("Bogs(PB)");
	       list.add("Raised bog(PB1)");
	       list.add("Upland blanket bog(PB2)");
	       list.add("Lowland blanket bog(PB3)");
	       list.add("Cutover bog(PB4)");
	       list.add("Eroding blanket bog(PB5)");
	       list.add("Fens and flushes(PF)");
	       list.add("Rich fen and flush(PF1)");
	       list.add("Poor fen and flush(PF2)");
	       list.add("Transition mire and quaking bog(PF3)");
	       list.add("Sublittoral rock(SR)");
	       list.add("Sublittoral sediment(SS)");
	       list.add("Highly modified/non-native woodland(WD)");
	       list.add("(Mixed) broadleaved woodland(WD1)");
	       list.add("(Mixed) broadleaved/conifer woodland(WD2)");
	       list.add("(Mixed) conifer woodland(WD3)");
	       list.add("Conifer plantation(WD4)");
	       list.add("Scattered trees and parkland(WD5)");
	       list.add("Linear woodland/scrub(WL)");
	       list.add("Hedgerows(WL1)");
	       list.add("Treelines(WL2)");
	       list.add("Semi-natural woodland(WN)");
	       list.add("Oak-birch-holly woodland(WN1)");
	       list.add("Oak-ash-hazel woodland(WN2)");
	       list.add("Yew woodland(WN3)");
	       list.add("Wet pedunculate oak-ash woodland(WN4)");
	       list.add("Riparian woodland(WN5)");
	       list.add("Wet willow-alder-ash woodland(WN6)");
	       list.add("Bog woodland(WN7)");
	       list.add("Scrub/transitional woodland(WS)");
	       list.add("Scrub(WS1)");
	       list.add("Immature woodland(WS2)");
	       list.add("Ornamental/non-native shrub(WS3)");
	       list.add("Short rotation coppice(WS4)");
	       list.add("Recently-felled woodland(WS5)");
	       
	       return list;
	 }
	 
//	 private void setupPictureButton()
//	 {
//		  final Button pictureb = (Button) findViewById(R.id.addpicturebutton);
//		    pictureb.setOnClickListener(new View.OnClickListener() 
//		    {             public void onClick(View v) {
//		    	
//		    	
//		    		ResultFileHelper filex = new ResultFileHelper(getApplicationContext().getFilesDir());
//		    		ArrayList<String> readData = filex.readData();
//		    		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   
//		    		Bundle extras = getIntent().getExtras(); 
//		  	        String name = extras.getString("animalname");
//		  	        name = name.replace(" ", "");
//		    		File photo = new File(getExternalFilesDir(null), name+"-" +readData.size() + ".jpg");
//		    			
//		    		//File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");   
//		    		camera.putExtra(MediaStore.EXTRA_OUTPUT,  Uri.fromFile(photo));  
//		    		// imageUri = Uri.fromFile(photo); 
//		    		//startActivityForResult(camera, 1); 
//		    		startActivityForResult(camera,3);
//		    		Log.i(this.toString(), "Finished with picture " + getExternalFilesDir(null) + "/"+ name+"-" +readData.size() + ".jpg"); 
//		    		
//		    	}
//		    });
//	 }
	 private void setupNormal(String name)
	 {
		  
	       
	       setTitle("Sending details of " + name );
        
	       // Restore preferences      
	       SharedPreferences settings = getSharedPreferences(Constants.SETTINGCONST, 0);  
	       String recodername = settings.getString("RecoderName","");   
	       String emailname = settings.getString("email","");   
		      
	       final EditText editTextname = (EditText) findViewById(R.id.editTextname);
	       if (recodername == null || recodername.equals("")|| recodername.equals(null))
	       {
	    	   editTextname.setText("");
	       }
	       else
	       {
	    	   editTextname.setText(recodername);
	       }
	       
	       final EditText emailnametxt = (EditText) findViewById(R.id.editTextemail);
		    
	       if (emailname == null || emailname.equals(null)|| emailname.equals(""))
	       {
	    	   emailnametxt.setText("");
	       }
	       else
	       {
	    	   emailnametxt.setText(emailname);
	       }
       	
	       MonitorGpsTask mgt = MonitorGpsTask.getInstance();
	        lastLocation = mgt.getLastLocation();
	      
	       final EditText editTextspecies = (EditText) findViewById(R.id.editTextspecies);
	       editTextspecies.setText(name);
	       
	       final Button mPickDate = (Button) findViewById(R.id.datebutton);
	      

	       // add a click listener to the button
	       mPickDate.setOnClickListener(new View.OnClickListener() {
	    	   public void onClick(View v) {
	    		   showDialog(DATE_DIALOG_ID);
	    	   }
	       });

	       Date now = new Date();
	       day = now.getDate();
	       month = now.getMonth()+1;
	       year = now.getYear()+1900;
	       hour = now.getHours();
	       min = now.getMinutes();
	       
	       mPickDate.setText(day+"/"+month+"/"+year);
	       
	       final Button mPickTime = (Button) findViewById(R.id.timebutton);
			 mPickTime.setText(hour+":"+min);
			 
			 // add a click listener to the button
			 mPickTime.setOnClickListener(new View.OnClickListener() {
		    	   public void onClick(View v) {
		    		   showDialog(TIME_DIALOG_ID);
		    	   }
		       });
	       final EditText editTextref = (EditText) findViewById(R.id.editTextref);
	       editTextref.setText("Couldn't read Grid Ref");
	       try
	       {
	       if(lastLocation == null){
	    	  
	    	   lastLocation = mgt.getLastLocationCell();
	    	   if(lastLocation != null){
	    		  // editTextref.setText("Couldn't read location");
	    		   WGS84Convertor convertor = new WGS84Convertor(lastLocation.getLongitude(),
		    			   lastLocation.getLatitude());
		    	   String location = convertor.getString();
		    	   editTextref.setText(location);
	    	   }
	       }
	       else
	       {
	    	   WGS84Convertor convertor = new WGS84Convertor(lastLocation.getLongitude(),
	    			   lastLocation.getLatitude());
	    	   String location = convertor.getString();
	    	   editTextref.setText(location);
	       }
	       }
	       catch(Exception e)
	       {
	    	   editTextref.setText("Couldn't get Grid Ref");
	       }
	       
	     //  final EditText editTextlocation = (EditText) findViewById(R.id.editTextlocation);
		 //  Log.d(this.toString(),"About to start dialog " + lastLocation); 
	      // if (lastLocation != null)
	     //  {
	    	 //  ProgressDialog dialog = ProgressDialog.show(this, "", "Trying to get location. Please wait...", true);
	    	  // dialog.show();
	    	  
	    	  // String locationText =locationText(lastLocation);
	    	 //  editTextlocation.setText(locationText);
	    	 //  dialog.dismiss();
	     //  }
	    //   Log.d(this.toString(),"finished  start dialog " ); 
		      
	       setupButtons("","");
	       final Object data = getLastNonConfigurationInstance();    
	       if (data != null)
	       {
	    	   TempStruct tempstuct = (TempStruct)data;
	    	   if (tempstuct.execute == null)
	    	   {
	    		   final EditText editTextlocation = (EditText) findViewById(R.id.editTextlocation);
	    		   editTextlocation.setText(tempstuct.savedlocation);
	    		   editTextref.setText(tempstuct.savedTextlocation);
	    		   savedlocation = tempstuct.savedlocation;  
	    		   savedTextlocation = tempstuct.savedTextlocation;
	    	   }
	    	   else
	    	   {
	    		   GetLocationTask tempexecute =(GetLocationTask)tempstuct.execute;
	    		   tempexecute.dialog = null;
	    	   }
	    		   
	    	   execute = tempstuct.execute;
	       }
	       else
	       {
	
	    	   execute = new GetLocationTask().execute();
	       }
	       
	 }
	 
	 
	 private void setupButtons(String habitat1,String habitat2)
	 {
		   final Button savebutton = (Button) findViewById(R.id.save); 
		     
	       savebutton.setOnClickListener(msavebutton) ;
	       
	       final Button usemap = (Button) findViewById(R.id.usemap); 
		     
	       usemap.setOnClickListener(musemap) ;
	       
	       final Button advancedbutton = (Button) findViewById(R.id.advancedbutton); 
		     
	       advancedbutton.setOnClickListener(madvancedbutton) ;
	       SharedPreferences settings = getSharedPreferences(Constants.SETTINGCONST, 0);  
	       Boolean advancevisable = settings.getBoolean("advancedvisable",false);  
	      
	       final LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout8);
			
	       if (advancevisable == false)
	       {
	    	   layout.setVisibility(View.GONE);
	       }
	       else
	       {
	    	   layout.setVisibility(View.VISIBLE);
	       }
	       
	       
	       
	       
	       
	    
	       List<String> createList = createList();

	       final Spinner sp=(Spinner) findViewById(R.id.habitatspinner);
	       ArrayAdapter<String> adp= new ArrayAdapter<String>(this,
	                                       android.R.layout.simple_list_item_1,createList);
	       adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	       sp.setAdapter(adp);
	       
	       if (habitat1.equals(""))
	       {
	    	   sp.setSelection(0);
	       }
	       else
	       {
	    	   int position = adp.getPosition(habitat1);
	    	   sp.setSelection(position);
	       
	       }
	       final Spinner sp2=(Spinner) findViewById(R.id.habitatspinner2);
	       ArrayAdapter<String> adp2= new ArrayAdapter<String>(this,
	                                       android.R.layout.simple_list_item_1,createList);
	       adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	       sp2.setAdapter(adp2);

	       if (habitat2.equals(""))
	       {
	    	   sp2.setSelection(0);
	       }
	       else
	       {
	    	   int position = adp2.getPosition(habitat2);
	    	   sp2.setSelection(position);
	       
	       }
	    
			 

	       
	      
	       String commentstr = settings.getString("comments","");  
		      
	       Log.i(this.toString(), "commentString " + commentstr);
	       String[] commentstrarr = commentstr.split(";");
	       ArrayList<String> comments = new ArrayList<String>();
	       for(int i=0 ; i<commentstrarr.length;i++)
	       {
	    	   comments.add(commentstrarr[i]);
	       }
	       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, comments);
	         AutoCompleteTextView textView = (AutoCompleteTextView)
	                 findViewById(R.id.editTextcomment);
	         textView.setThreshold(0);
	         textView.setAdapter(adapter);
	         
	         
	         
	         
	         
	         
		       String lifestagestr = settings.getString("lifestage","");  
			      
		       Log.i(this.toString(), "lifestagestr " + lifestagestr);
		       String[] lifestagestrarr = lifestagestr.split(";");
		       ArrayList<String> lifestage = new ArrayList<String>();
		       for(int i=0 ; i<lifestagestrarr.length;i++)
		       {
		    	   lifestage.add(lifestagestrarr[i]);
		       }
		       ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
		                 android.R.layout.simple_dropdown_item_1line, lifestage);
		         AutoCompleteTextView textView1 = (AutoCompleteTextView)
		                 findViewById(R.id.editTextlifestage);
		         textView1.setThreshold(0);
		         textView1.setAdapter(adapter1);
	       
	 }
	 private void setupPosition(int position)
	 {
		  
		 	final ResultFileHelper filex = new ResultFileHelper(getApplicationContext().getExternalFilesDir(null));
			
		 	ArrayList<String> readData = filex.readData();
		 	
		 	String[] splitData = readData.get(position).split(",");
	       setTitle("Sending details of " + splitData[6] );
    
//	  0     name+"," 
//	  1      date +"," +
//	  2	    gridref +"," +
//	  3	    location +"," +
//	  4	    longitude +"," +
//	  5	    latitude +"," +
//	  6	    species +"," +
//	  7	    comment +"," +
//	8	    pairs.get("number") +"," +
//	9	    pairs.get("lifestage") +"," +
//	10	    pairs.get("email") +"," +
//	11	    pairs.get("habitat1") +"," +
//	12	    pairs.get("habitat2");//+"," +;
//	13       pairs.get("accuracy");//+"," +;
//	       // Restore preferences      
	       SharedPreferences settings = getSharedPreferences(Constants.SETTINGCONST, 0);  
	       String recodername = splitData[0];   
	       
	       final EditText editTextname = (EditText) findViewById(R.id.editTextname);
	       if (recodername == null || recodername.equals("")|| recodername.equals(null))
	       {
	    	   editTextname.setText("");
	       }
	       else
	       {
	    	   editTextname.setText(recodername);
	       }
	       
	      
       	
       	
	   //    MonitorGpsTask mgt = MonitorGpsTask.getInstance();
	   //     lastLocation = mgt.getLastLocation();
	      
	       final EditText editTextspecies = (EditText) findViewById(R.id.editTextspecies);
	       editTextspecies.setText(splitData[6]);
	       
	       final Button mPickDate = (Button) findViewById(R.id.datebutton);
	      

	       // add a click listener to the button
	       mPickDate.setOnClickListener(new View.OnClickListener() {
	    	   public void onClick(View v) {
	    		   showDialog(DATE_DIALOG_ID);
	    	   }
	       });

	       String datestring = splitData[1];
	       Date now = new Date(Long.parseLong(datestring));
	       day = now.getDate();
	       month = now.getMonth()+1;
	       year = now.getYear()+1900;
	       hour = now.getHours();
	       min = now.getMinutes();
	       
	       mPickDate.setText(day+"/"+month+"/"+year);
	       
	       final Button mPickTime = (Button) findViewById(R.id.timebutton);
			 mPickTime.setText(hour+":"+min);
			 
			 // add a click listener to the button
			 mPickTime.setOnClickListener(new View.OnClickListener() {
		    	   public void onClick(View v) {
		    		   showDialog(TIME_DIALOG_ID);
		    	   }
		       });
	       final EditText editTextref = (EditText) findViewById(R.id.editTextref);
	       editTextref.setText( splitData[2]);
	      
	       
	       final EditText editTextlocation = (EditText) findViewById(R.id.editTextlocation);
		   Log.d(this.toString(),"About to start dialog " + lastLocation); 
	     
	       editTextlocation.setText(splitData[3]);

	        EditText editTexttemp = (EditText) findViewById(R.id.editTextemail);
	        editTexttemp.setText( splitData[10]);
	        
	        editTexttemp = (EditText) findViewById(R.id.editTextnumber);
	        editTexttemp.setText( splitData[8]);
	        
	        editTexttemp = (EditText) findViewById(R.id.editTextlifestage);
	        editTexttemp.setText( splitData[9]);
	         
	        if (splitData.length > 12)
	        {
	        	Log.i(this.toString(),"setting up location " + splitData[13] +" " + splitData[5] + " " + splitData[4]);
	        	if ("map".equalsIgnoreCase(splitData[13]))
	        	{
	        		Double x =Double.parseDouble(splitData[5]);
	        		
	        		lat = x.intValue();
	        		x =Double.parseDouble(splitData[4]);
	        		lng = x.intValue();
	        	}
	        }
	        
		      
	       setupButtons(splitData[11],splitData[12]);
	     
	       
	 }
	 
	 
	 
	 
	 @Override
	 protected Dialog onCreateDialog(int id) {
	 switch (id) {
	 case DATE_DIALOG_ID:
		 return new DatePickerDialog(this,
				 mDateSetListener,
				 year, month-1, day);
	 	
	 	
	 	case TIME_DIALOG_ID:
		 return new TimePickerDialog(this,
				 mTimeSetListener,
				 hour, min,true);
	 	}
	 	return null;
	 }

	// the callback received when the user "sets" the date in the dialog
	 private DatePickerDialog.OnDateSetListener mDateSetListener =
			 new DatePickerDialog.OnDateSetListener() {

		 public void onDateSet(DatePicker view, int yearin, 
				 int monthOfYear, int dayOfMonth) {
			 year = yearin;
			 month = monthOfYear+1;
			 day = dayOfMonth;
			 final Button mPickDate = (Button) findViewById(R.id.datebutton);
			 mPickDate.setText(day+"/"+month+"/"+year);
		        
	 }
	 };

	// the callback received when the user "sets" the date in the dialog
		 private TimePickerDialog.OnTimeSetListener mTimeSetListener =
				 new TimePickerDialog.OnTimeSetListener() {

			 public void onTimeSet(TimePicker view, int hourin, 
					 int minin) {
				 hour = hourin;
				 min = minin;
				
				 final Button mPickDate = (Button) findViewById(R.id.timebutton);
				 mPickDate.setText(hour+":"+min);
			        
		 }
		 };
		public void onBackPressed()
		{
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 builder.setMessage("Do you want to save updates?")
			 .setCancelable(false)
			 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int id) {
				 saveData();
				
		//	 SaveDetailsActivity.this.finish();

			 }
			 })
			 .setNegativeButton("No", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int id) {
			 dialog.cancel();
			 SaveDetailsActivity.this.finish();
			 }
			 });
			 AlertDialog alert = builder.create();
			 alert.show();
			 
		}
	 /* Request updates at startup */
		@Override
		protected void onResume() {
			super.onResume();
			MonitorGpsTask mgt = MonitorGpsTask.getInstance( );
			mgt.subscribe(this);
			
		}

		/* Remove the locationlistener updates when Activity is paused */
		@Override
		protected void onPause() {
			super.onPause();
			
			
		   
		
		}

		private boolean saveData()
		{
			final EditText editTextname = (EditText) findViewById(R.id.editTextname);
			String name = editTextname.getText().toString();
			name = name.replace("\n", ""); 
			 if (name == null || "null".equalsIgnoreCase(name) || "".equalsIgnoreCase(name))
			 {
				 AlertDialog.Builder builder = new AlertDialog.Builder(this);
				 builder.setMessage("Please enter some name in the Name field\n\nThis can be just initials or a nickname. This will not be released to the public but may be used to help verify the records.")
				 .setCancelable(false)
				 .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int id) {
					 dialog.cancel();

					
				 }});
				 AlertDialog alert = builder.create();
				 alert.show();
				 return false;
			 }
		
//			 if (! name.matches(".*@.*\\..*"))
//				{
//				 AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				 builder.setMessage("Please enter some e-mail address in the format id@xyz.com . This will not be released to the public but may be used to help verify the records.")
//				 .setCancelable(false)
//				 .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//				 public void onClick(DialogInterface dialog, int id) {
//					 dialog.cancel();
//
//					
//				 }});
//				 AlertDialog alert = builder.create();
//				 alert.show();
//				 return false;
//				}
			 SharedPreferences settings = getSharedPreferences(Constants.SETTINGCONST, 0);  
		       
			 SharedPreferences.Editor editor = settings.edit();    
			 editor.putString("RecoderName", name);  
			 // Commit the edits!   
			 
			 
			// ResultFileHelper filex = new ResultFileHelper(getApplicationContext().getExternalFilesDir(null));
			 final ResultFileHelper filex = new ResultFileHelper(getApplicationContext().getExternalFilesDir(null));
			  EditText editTexttemp = (EditText) findViewById(R.id.editTextcomment);
			  
			  String tempcomments = settings.getString("comments","");
			  String[] tempcommentssplit = tempcomments.split(";");
			  String thiscomment = editTexttemp.getText().toString();
			  tempcomments="";
			  for (int i=0;i<tempcommentssplit.length && i < 20 ;i++)
			  {
				  if (!thiscomment.equals(tempcommentssplit[i]))
				  {
					  tempcomments = tempcommentssplit[i] + ";" + tempcomments;
				  }
			  }
			  tempcomments = editTexttemp.getText().toString() +";"+tempcomments;
			  editor.putString("comments", tempcomments);  
			 
			  filex.setComment(editTexttemp.getText().toString());
			  
			  editTexttemp = (EditText) findViewById(R.id.editTextlifestage);
				 
			  filex.saveData("lifestage",editTexttemp.getText().toString());
			  
			  String templifestage = settings.getString("lifestage","");
			  String[] templifestagesplit = templifestage.split(";");
			  String thislifestage = editTexttemp.getText().toString();
			  templifestage="";
			  for (int i=0;i<templifestagesplit.length && i < 20 ;i++)
			  {
				  if (!thislifestage.equals(templifestagesplit[i]))
				  {
					  templifestage = templifestagesplit[i] + ";" + templifestage;
				  }
			  }
			  templifestage = editTexttemp.getText().toString() +";"+templifestage;
			  editor.putString("lifestage", templifestage);  
			  
			  editTexttemp = (EditText) findViewById(R.id.editTextemail);
			  filex.saveData("email",editTexttemp.getText().toString());
				 
			  editor.putString("email", editTexttemp.getText().toString());  

			  editor.commit();
			  
			 
			 Button dateButton = (Button) findViewById(R.id.datebutton);
			 String []datedata=dateButton.getText().toString().split("/");
			 Date tempdate = new Date();
			 tempdate.setDate(Integer.parseInt(datedata[0]));
			 tempdate.setMonth(Integer.parseInt(datedata[1])-1);
			 tempdate.setYear(Integer.parseInt(datedata[2])-1900);
			 
			 Button timeButton = (Button) findViewById(R.id.timebutton);
			 String []timedata=timeButton.getText().toString().split(":");
			 tempdate.setHours(Integer.parseInt(timedata[0]));
			 tempdate.setMinutes(Integer.parseInt(timedata[1]));
			 
			 filex.setDate(""+tempdate.getTime());
			 editTexttemp = (EditText) findViewById(R.id.editTextref);
			 filex.setGridref(editTexttemp.getText().toString());
			 editTexttemp = (EditText) findViewById(R.id.editTextlocation);
			 String accuracy = "unknown";
			 if (lastLocation != null)
			 {
				 accuracy=""+lastLocation.getAccuracy(); 
			 }
			 
			 
			Log.d(this.toString(), "lastLocation " + lastLocation + " accuracy " + accuracy + " lat lng" + lat +"," + lng);
			 if (lat != 0 && lng != 0 )
			 {
				 filex.setLatitude(""+lat/1E6);
				 filex.setLongitude(""+lng/1E6);
				 accuracy="map";
			 }
			 else if (lastLocation != null)
			 {
				 
				
				 
				 filex.setLatitude(""+lastLocation.getLatitude());
				 filex.setLongitude(""+lastLocation.getLongitude());
			 }
			 
			 filex.setLocation(editTexttemp.getText().toString().replace(",", ";") );
				
			 filex.saveData("accuracy",accuracy);
		//	 editTexttemp = (EditText) findViewById(R.id.editTextname);
			 filex.setName(name);
			 editTexttemp = (EditText) findViewById(R.id.editTextspecies);
			 filex.setSpecies(editTexttemp.getText().toString());
			 
			 editTexttemp = (EditText) findViewById(R.id.editTextnumber);
			 filex.saveData("number",editTexttemp.getText().toString());
			 
			 
			 Spinner sp1=(Spinner) findViewById(R.id.habitatspinner);
			 String temp =sp1.getSelectedItem().toString();
			 Log.d(this.toString(), "First Spinner " + sp1 + ", " + sp1.getSelectedItem() + ", " + temp);
			 filex.saveData("habitat1",temp);
			 
			 sp1=(Spinner) findViewById(R.id.habitatspinner2);
			  temp =sp1.getSelectedItem().toString();
			 filex.saveData("habitat2",temp);

			 if(thePosition != -1)
			 {
				 filex.saveData(thePosition);
			 }
			 else
			 {
				 filex.saveData();
			 }
			 
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 builder.setMessage("Do you want to submit updates?")
			 .setCancelable(false)
			 .setPositiveButton("Now", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int id) {
				//  fileTask =  new UploadFileTask(SaveDetailsActivity.this,null,true);
				// fileTask.execute(filex);
				 dialog.dismiss();
			//	 if (filex.readData().size() != 1)
			//	 {
					 Intent	xxx = new Intent(SaveDetailsActivity.this,SendDataActivity.class);
					 xxx.putExtra("caller", "SaveDetails");				
					 startActivityForResult(xxx,2);
			//	 }
			//	 else
			//	 {
//					 Log.d(this.toString(), "about to sent 1 record "  );
//					 AsyncTask<ResultFileHelper, Integer, Integer> execute = new UploadFileTask(SaveDetailsActivity.this,null,true)
//						.execute(filex);
//					 Integer integer;
//					try {
//						integer = execute.get();
//						 
//						 Log.d(this.toString(), "got result " + integer + " " + execute.getStatus() );
//						 
//						// init(false,"");
//						 
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ExecutionException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				 }
			 }
			 })
			 .setNegativeButton("Later", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int id) {
			 dialog.dismiss();
			 	SaveDetailsActivity.this.finish();
			 }
			 });
			 AlertDialog alert = builder.create();
			 alert.show();
			 
			 return true;
			 
			// Toast.makeText(SaveDetailsActivity.this, "Successfully saved details" 
			//		, Toast.LENGTH_LONG).show();
		}
		
		
		private Location getLocation()
		{
			MonitorGpsTask mgt = MonitorGpsTask.getInstance();
		     
			Location location = mgt.getLastLocation();
			if (location == null)
			{
				location = mgt.getLastLocationCell();
			}
			return location;
			
		}
		private OnClickListener msavebutton = new View.OnClickListener(){
		
		public void onClick(View v)
		{
			
			Log.d(this.toString(), "got onclick of save");
			
		     lastLocation = getLocation();
		     
			 if (lastLocation == null || lastLocation.getAccuracy() > 200)
			 {
				 
				 dialogresult = false;
				 String accuracytxt =" Unknown ";
				 if (lastLocation != null)
				 {
					 accuracytxt = lastLocation.getAccuracy() +" Meters ";
				 }
				 AlertDialog.Builder builder = new AlertDialog.Builder(SaveDetailsActivity.this);
				 builder.setMessage("Accuracy is " + accuracytxt + ". So you want to wait for a more accurate reading?")
				 .setCancelable(false)
				 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int id) {
					 dialogresult = true;
					 dialog.cancel();
					
					 ;
					
				 }})
				 .setNegativeButton("No", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int id) {
					// dialogresult = false;
					 dialog.cancel();
					 saveData();
					
				 }});
				 AlertDialog alert = builder.create();
				 alert.show();
				 
				 Log.d(this.toString(), "dialogresult " + dialogresult);
					
				
				
			 }
			 else
			 {
				 saveData();
			 }
			 
			// finish();
		}
		};
			 
		private OnClickListener madvancedbutton = new View.OnClickListener(){
			
			public void onClick(View v)
			{
				
				Log.d(this.toString(), "got onclick madvancedbutton");
				
				final LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout8);
				
				
				 SharedPreferences settings = getSharedPreferences(Constants.SETTINGCONST, 0);  
			       Boolean advancevisable = settings.getBoolean("advancedvisable",false);  
			       SharedPreferences.Editor editor = settings.edit();    
					 
					
			       if (advancevisable == true)
			       {
			    	   layout.setVisibility(View.GONE);
			    	   editor.putBoolean("advancedvisable",false);  
			       }
			       else
			       {
			    	   layout.setVisibility(View.VISIBLE);
			    	   editor.putBoolean("advancedvisable",true); 
			       }
			       // Commit the edits!   
					 editor.commit();
				
			}
			};
		
			 
			private OnClickListener musemap = new View.OnClickListener(){
				
				public void onClick(View v)
				{
					
					Log.d(this.toString(), "got onclick musemap");
					 Intent xxx =   new Intent(getApplicationContext(),MapPageActivity.class);
					 	xxx.putExtra("butterflyname",  "helptext");  
						
					 	if (thePosition != -1)
					 	{	
					 		 final ResultFileHelper filex = new ResultFileHelper(getApplicationContext().getExternalFilesDir(null));
					 		ArrayList<String> readData = filex.readData(thePosition);	
					 		Log.d(this.toString(), "Setting lat/long " + thePosition +" "+ readData.size() + " " + readData);
					    	  
					 		if (readData.size() > 5 )
					 		{
					 			xxx.putExtra("long",  readData.get(4));  
					 			xxx.putExtra("lat",  readData.get(5));
					 		}
					 	}
					 	startActivityForResult(xxx,1);
					// finish();
				}
				};
		
			
			@Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data)
			{
				
				Log.d(this.toString(),"Got result " + resultCode + " data "+ data +" requestCode " + requestCode);
				if (requestCode == 2)
				{
					 SaveDetailsActivity.this.finish();
				}
				if (requestCode == 3)
				{
					if (data == null)
					{
						
					}
					else
					{
						Log.d(this.toString(),"finished getting a picture " + resultCode + " " + data.getExtras());
							
					}
					
				}
				if (requestCode == 1 && resultCode == 2 && data != null)
				{
					Log.d(this.toString(),"Got result " + resultCode + " data "+ data.getIntExtra("lat", 0));
					
					 lat = data.getIntExtra("lat", 0);
					 lng = data.getIntExtra("lng", 0);
					
					String locationtxt = locationText(lat/1E6,lng/1E6);
					
					 String location =null;
			    	   try
			    	   {
			    		   WGS84Convertor convertor = new WGS84Convertor(lng/1E6,
			    				   lat/1E6);
			    	   	   location = convertor.getString();
			    	   }
			    	   catch (Exception e)
			    	   {
			    		   location ="Couldn't get Grid Ref";
			    	   }
			    	   
			    	   final EditText editTextlocation = (EditText) findViewById(R.id.editTextlocation);
						editTextlocation.setText(locationtxt);
						
						  final EditText editTextref = (EditText) findViewById(R.id.editTextref);
						  editTextref.setText(location);
					
				}
				
			}
			
		public String locationText(double lat,double lng) { 
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());    
            String result = ""; 
            try { 
                List<Address> list = geocoder.getFromLocation( 
                		lat, lng, 1); 
                if (list != null && list.size() > 0) { 
                    for (int i = 0 ; i<list.size();i++)
                    {
                	Address address = list.get(i); 
                	for (int j = 0 ; j<address.getMaxAddressLineIndex();j++)
                    {
                		if (address.getAddressLine(j) == null || "null".equalsIgnoreCase(address.getAddressLine(j)))
                		{
                			
                		}else{
                			result += address.getAddressLine(j) + ", ";
                		}
                    }
                	
                    // sending back first address line and locality 
                    }
                } 
            } catch (IOException e) { 
                Log.d(this.toString(), "Impossible to connect to Geocoder"); 
            } finally {
            	
            	//Date now = new Date();
            	
            	// result +=  "Accuracy " + location.getAccuracy() + " age of reading " + (now.getTime()-location.getTime())/1000; 
                
            } 
            
            return result;
        }
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), "Got location update " + location, Toast.LENGTH_SHORT).show();
			Log.d(this.toString(), "Got location update " + location);
			
		}
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		} 

		 @Override
			public Object onRetainNonConfigurationInstance() 
			{   
			 	TempStruct list = null;
			    if (savedlocation != null || this.execute != null)
			    {
			        list = new TempStruct(); 
			    	list.savedlocation = this.savedlocation;
			    	list.savedTextlocation = this.savedTextlocation;
			    	list.execute = this.execute;
			    }
				return list;
			}
			 class TempStruct{
				String savedlocation;
				String savedTextlocation;
				AsyncTask<Void, String, String> execute;
			 };
		
		 private class GetLocationTask extends AsyncTask<Void, String, String>
		 {   
			 private ProgressDialog dialog;
			 private int numbersec = 10;
			 private int counter = 0;
			 public GetLocationTask()
			 {
				 dialog = new ProgressDialog(SaveDetailsActivity.this);
				 dialog.setButton(DialogInterface.BUTTON_POSITIVE ,"skip", mSkipbutton);
				 dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				 dialog.setIndeterminate(false);
				 dialog.setTitle("Fetching Location Data");
				 dialog.setMessage("Fetching Location Data Max 10 Seconds");
				 dialog.setCancelable(true);
				 
			 }
			 
			 private DialogInterface.OnClickListener mSkipbutton = new DialogInterface.OnClickListener(){
					
					public void onClick(DialogInterface v,int button)
					{
						
						Log.d(this.toString(), "got onclick");
						counter = 11;
						// finish();
					}
					};
			 protected void onPreExecute()
			 {
				
				 Log.d(this.toString(),"About to show");
				 dialog.show();
				 Log.d(this.toString(),"About to show " + dialog.isShowing());
				
			  }
			 @Override
			 protected String doInBackground(Void... filex) 
			 {       
			     Log.d(this.toString(),"About to start dialog " + lastLocation); 
			     String locationText = "";
			     try
			     {
				
			    //  if (lastLocation != null)
			     //  {
			    	 //  ProgressDialog dialog = ProgressDialog.show(this, "", "Trying to get location. Please wait...", true);
			    	  // dialog.show();
			    	   
			    	    
			    	    
			    	    MonitorGpsTask mgt = MonitorGpsTask.getInstance();
			    	    Location lastLocation2 = mgt.getLastLocation();
			    	    Date now = new Date();
			    	   
			    	    long locationtime = 0;
			    	    float accuracy = 1000;
			    	    if (lastLocation2 != null)
			    	    {
			    	    	locationtime = lastLocation2.getTime();
			    	    	accuracy = lastLocation2.getAccuracy();
			    	    }
			    	    //((now.getTime() -locationtime) > 10*60*1000 )
			    	   
			    	    while ((counter < 10) && (accuracy > 200))
			    	    {
			    	    	 Log.d(this.toString(),"counter " + counter +" accuracy " + accuracy + " " + lastLocation2);
			    	    	 Date timeofread = new Date(locationtime);
			    	    	 publishProgress( (10 - counter) + " seconds left before using less accurate reading " );
			    	    	 counter++;
			    	    	 try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							lastLocation2 = mgt.getLastLocation();
							
							
							locationtime = 0;
							if (lastLocation2 != null)
				    	    {
				    	    	locationtime = lastLocation2.getTime();
				    	    	accuracy = lastLocation2.getAccuracy();
				    	    }
							else
							{
								 Location lastLocationCell = mgt.getLastLocationCell();
								 if (lastLocationCell != null){
									 accuracy = lastLocationCell.getAccuracy();
								 }
							}
			    	    }
			    	    if (lastLocation2 == null || (now.getTime() -locationtime) > 10*60*1000 ) 
				        {
			    	    	lastLocation = mgt.getLastLocationCell();
				        }
			    	    else{
			    	    	lastLocation = lastLocation2;
			    	    }
			    	    
			    	    if (lastLocation != null)
			    	    {
			    	    	locationText =locationText(lastLocation.getLatitude(),lastLocation.getLongitude());
			    	    	
			    	    }
			    	    else
			    	    {
			    	    	locationText ="Couldn't read location!";
			    	    }
			    	  // editTextlocation.setText(locationText);
			    	 //  dialog.dismiss();
			      // }
			       Log.d(this.toString(),"finished  start dialog " + lastLocation);
			     }
			     catch (Exception e)
			     {
			       Log.e(this.toString(),"finished  start dialog with exception",e ); 
			     }
			    		
		        	
				            
				            return locationText;
			 }
			 protected void onProgressUpdate(String... progress) 
			 {
				 if (dialog!= null)
				 {
					 dialog.setMessage("Progress " + progress[0]);
				 }
			 } 
			 
			
			 protected void onPostExecute(String onPostExecute) 
			 {  
				// System.out.println("onPostExecute " + onPostExecute);  
				 final EditText editTextlocation = (EditText) findViewById(R.id.editTextlocation);
				 editTextlocation.setText(onPostExecute);
				 savedlocation = onPostExecute;
				  final EditText editTextref = (EditText) findViewById(R.id.editTextref);
			      
			       if(lastLocation == null){
			    	   editTextref.setText("Couldn't read Grid Ref");	
			    	   savedTextlocation = "Couldn't read Grid Ref";
			       }
			       else
			       {
			    	   String location =null;
			    	   try
			    	   {
			    		   WGS84Convertor convertor = new WGS84Convertor(lastLocation.getLongitude(),
			    			   lastLocation.getLatitude());
			    	   	   location = convertor.getString();
			    	   }
			    	   catch (Exception e)
			    	   {
			    		   location ="Couldn't get Grid Ref";
			    	   }
			    	   editTextref.setText(location);
			    	   savedTextlocation = location;
			    	   if (lastLocation.getAccuracy() < 500)
			    	   {
			    		   
			    		   Date timeofread = new Date(lastLocation.getTime());
			    		   Toast.makeText(SaveDetailsActivity.this, "Got accurate location at " + timeofread, Toast.LENGTH_SHORT).show();			  			 
			    	   }
			    	   else
			    	   {
			    		   Date timeofread = new Date(lastLocation.getTime());
			    		   Toast.makeText(SaveDetailsActivity.this, "Didn't get accurate location at " + timeofread, Toast.LENGTH_SHORT).show();			  			 
					    	 
			    	   }
			       }
			     if (dialog!= null)
				 {
			    	 dialog.dismiss();
			     }
				 execute = null;
			 }
			
			
		} 
}
