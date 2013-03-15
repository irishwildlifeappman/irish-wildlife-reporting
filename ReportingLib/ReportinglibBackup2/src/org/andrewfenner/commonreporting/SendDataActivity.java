package org.andrewfenner.commonreporting;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
//adb -s emulator-5554 logcat
public class SendDataActivity extends Activity {
	
	private ListView list1;
	private DataListAdapter piclistadapter;
	private Button submitbutton;
	private boolean[] checkdata;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.senddata);
       
        Log.d(this.toString()," started ");

        Bundle extras = getIntent().getExtras(); 
	    String caller = "";
	    if (extras != null)
	    {
	    	caller = extras.getString("caller");
	    }
	    
	   
	   
	    init(false,caller);
	   
	}

    /* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		
		System.out.println(" got resume ");
		init(true,"");
	}
    	
	@Override
	protected void onPause() {
		super.onPause();
		
		System.out.println(" got Pause ");
	
	}
    	
	
	public void init(boolean pause,String caller)
	{
		
		   submitbutton = (Button) findViewById(R.id.submit1);    
	        submitbutton.setOnClickListener(mSubmitListener);
		
	        
	        ResultFileHelper filex = new ResultFileHelper(
					getApplicationContext().getExternalFilesDir(null));
	       ArrayList<String> readData = filex.readData();
	       Log.i(this.toString(), "init called " + pause +" "+caller+" " + readData.size());
			 
	       if (readData.size() == 0)
	       {
	    	   ListHelper.clear();
	    	
	       }
		   list1 = (ListView) findViewById(R.id.resultlist); 
		  // String[] sites = new String[11];
		   checkdata = new boolean[readData.size()];
		   
		   if (pause== false)
			   {ListHelper.clear();
			   for(int i = 0;i<readData.size();i++)
			   {
				   ListHelper.addItem(true);
			   }
			   }
	        piclistadapter =new  DataListAdapter(this,readData,submitbutton);
		    list1.setAdapter(piclistadapter);
		  //  list1.set(70);
			list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    		
	    		@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					
					Log.i("main", "got here ietem click 2 " + arg2 + " " + arg3 + " siteName "  );
				
					Toast.makeText(arg1.getContext(), "Fill in the date "+ arg2 + " " + arg3 , Toast.LENGTH_LONG).show();
					
					
					Intent	xxx = new Intent(SendDataActivity.this,SaveDetailsActivity.class);
					
					
				  
					xxx.putExtra("animalname", (String)piclistadapter.getItem(arg2));
				
					
	        		startActivity(xxx);
				//	Log.i("main", "got here ietem click 3 " + arg0.getAdapter().getItem(arg2));
					return;
				}
				});
			
			
			
			
			ArrayList<Boolean> list = ListHelper.getList();
			int numbertosend = 0;
			for (int i=0;i<list.size();i++)
			{
				if (list.get(i)){
					numbertosend++;
				}
			}
			submitbutton.setText("Press to Submit " + numbertosend +" Records");
			
			 if ("SaveDetails".equals(caller) && readData.size() == 1)
			    {			    	
			    	sendDatahelper(caller);
			    }
	    
	}
    
	
	View.OnClickListener mSubmitListener = new OnClickListener() 
	 {    
		 public void onClick(View v)
		 {     
			 sendDatahelper("");
			
			 }  
		 };
	
		 
		 private void sendDatahelper(String caller)
		 {
		     boolean finish = false;
			 if ("SaveDetails".equals(caller) )
			    {			    	
				 finish = true;
			    }
				ResultFileHelper filex = new ResultFileHelper(
							getApplicationContext().getExternalFilesDir(null));

					
					ArrayList<String> readData = filex.readData();
					if (readData.size() != 0)
					{
						AsyncTask<ResultFileHelper, Integer, Integer> execute = new UploadFileTask(SendDataActivity.this,submitbutton,finish);
						execute.execute(filex);
						
						
					
							 init(true,"");
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Nothing to write ", Toast.LENGTH_SHORT).show();
			            
					}

				
				
					 
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
		        // Handle item selection
			  int itemId = item.getItemId();
		        if (itemId == R.id.itemsummary)
		        {
		        		getsummarydata();
		        	return true;
		        }
		        else if (itemId == R.id.itemremove)
		        	{
	        		removedata();
	        	return true;   
		        	}
	        	else if (itemId ==R.id.itemhistory)
	        	{
	        		showhistory();
	        	return true;   
	        	}
	        	else if (itemId ==R.id.itemdeletehistory)
	        	{
	        		deletehistory();
	        	return true;
		  		}
	        	else {
		        	return super.onOptionsItemSelected(item);
		        }
		       
		    	
		    }
	    
		
		
		
		 private void getsummarydata() {
			 new GetSummaryTask(SendDataActivity.this)
				.execute();
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
				 		 init(false,"");
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


		 private void showhistory() {
				// TODO Auto-generated method stub
				 Intent myIntent = new Intent(Intent.ACTION_VIEW); 
				 File locationFile = new File(getApplicationContext().getFilesDir(), Constants.SETTINGCONST + "history.csv");
					
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
					 File locationFile = new File(getApplicationContext().getFilesDir(), Constants.SETTINGCONST + "history.csv");
					 locationFile.delete();
					

					 Log.d(this.toString(), "Deleted file " +  Uri.fromFile(locationFile) + " "
							);
					

					

				} 
	private static String animalnames[] = {"Badger",
		"Barn Owl",
		"Bat - Any",
		"Cat",
		"Crow Family",
		"Deer - Any",
		"Dog",
		"Fox",
		"Hare",
		"Hedgehog",
		"Kestrel",
		"Long eared owl",
		"Mink, American",
		"Otter",
		"Pheasant (cock)",
		"Pheasant (hen)",
		"Pine Marten",
		"Rabbit",
		"Short-Eared Owl",
		"Squirrel Grey",
		"Squirrel Red",
		"Stoat",
		"Swan - Any",
		"unidentified"};

	public boolean[] getChecked() {
		// TODO Auto-generated method stub
		return checkdata;
	}

	public void setChecked(int theposition, boolean arg1) {
		
		Log.i("setChecked","theposition "+ theposition + " " + arg1);
		checkdata[theposition]=arg1;
		
	}
}