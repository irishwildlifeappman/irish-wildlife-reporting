package org.andrewfenner.commonreporting;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

public class UploadFileTask extends AsyncTask<ResultFileHelper, Integer, Integer>
{  
	private ProgressDialog progressdialog;
	private Context theContext;
	private Button submitbutton;
	private String savedresulttext=null;
	private boolean finish=false;
	private ArrayList<String> sentData = new ArrayList<String>();

	
	public UploadFileTask(Context aContext, Button asubmitButton,boolean finish)
	{
		// System.out.println("Creating UploadFileTask " + aContext);
		this.finish = finish;
	 theContext = aContext;
	 submitbutton = asubmitButton;
	
	 progressdialog =  new ProgressDialog(aContext);
	 progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	 progressdialog.setIndeterminate(true);
	 progressdialog.setTitle("Uploading");
	 progressdialog.setCancelable(true);
	 progressdialog.setMessage("Uploading records to server");;
	// System.out.println("created UploadFileTask " + dialog.getContext());
	
}
@Override
protected void onPreExecute()
{
	
//	 System.out.println("About to show " + theContext  +"  " +  dialog.getContext());
	Log.i(this.toString(),"onPreExecute " + progressdialog); 
	// dialog.show();
	// progressdialog = ProgressDialog.show(theContext, "aaaa", "bbbb");
	//progressdialog.dismiss();
	//  Log.i(this.toString(),"onPreExecute " + progressdialog +" " + progressdialog.isShowing() + " " + progressdialog.isIndeterminate()); 
		
	progressdialog.show();
	
	  Log.i(this.toString(),"onPreExecute " + progressdialog +" " + progressdialog.isShowing() + " " + progressdialog.isIndeterminate()); 
	// System.out.println("About to show " + dialog.isShowing());
	
 }
@Override
protected Integer doInBackground(ResultFileHelper... filex) 
{       
    Integer result = -1;	

	try
	{
	Log.i(this.toString(),"doInBackground " + progressdialog); 
	 SharedPreferences settings = theContext.getSharedPreferences(Constants.SETTINGCONST, 0);  
	 final Boolean UPLOADtype = settings.getBoolean("UPLOAD",true);    
   	
	 if(UPLOADtype )
	 {
   	 try {
   		 
   			ArrayList<String> readData = filex[0].readData();
   			 publishProgress(0);
   			 int sent = 0;
   			 sentData.clear();
   			Log.d(this.toString(), "List not to remove"  +ListHelper.getList());
			
			for(int i = 0 ; i < readData.size();i++)
			{ 
				Log.d(this.toString(), "List not to remove"  +ListHelper.getItem(i));
				if (ListHelper.getItem(i))
			
			 {
   	        HttpClient client = new DefaultHttpClient();  
   	        String postURL = "http://irishwildlifereporting.appspot.com/irishwildlifereporting/reportingServlet";
       	    HttpPost post = new HttpPost(postURL); 
   	            List<NameValuePair> params = new ArrayList<NameValuePair>();
   	            String[] data = readData.get(i).split(",");
   	            if (data.length == 8)
   	            {
   	            	params.add(new BasicNameValuePair("name", data[0]));
   	            	params.add(new BasicNameValuePair("date", data[1]));
   	            	params.add(new BasicNameValuePair("gridref", data[2]));
   	            	params.add(new BasicNameValuePair("location", data[3]));
   	            	params.add(new BasicNameValuePair("longitude", data[4]));
   	            	params.add(new BasicNameValuePair("latitude", data[5]));
   	            	params.add(new BasicNameValuePair("species", data[6]));
   	            	params.add(new BasicNameValuePair("comment", data[7]));
   	            	params.add(new BasicNameValuePair("appname", Constants.SETTINGCONST));
   	            }  
   	            else if (data.length == 14)
	            {
	            	params.add(new BasicNameValuePair("name", data[0]));
	            	params.add(new BasicNameValuePair("date", data[1]));
	            	params.add(new BasicNameValuePair("gridref", data[2]));
	            	params.add(new BasicNameValuePair("location", data[3]));
	            	params.add(new BasicNameValuePair("longitude", data[4]));
	            	params.add(new BasicNameValuePair("latitude", data[5]));
	            	params.add(new BasicNameValuePair("species", data[6]));
	            	params.add(new BasicNameValuePair("comment", data[7]));
	            	params.add(new BasicNameValuePair("number", data[8]));
	            	params.add(new BasicNameValuePair("lifestage", data[9]));
	            	params.add(new BasicNameValuePair("email", data[10]));
	            	params.add(new BasicNameValuePair("habitat1", data[11]));
	            	params.add(new BasicNameValuePair("habitat2", data[12]));
	            	params.add(new BasicNameValuePair("accuracy", data[13]));
	            	
	            	params.add(new BasicNameValuePair("appname", Constants.SETTINGCONST));
	            }  
   	            else
   	            {
   	            	params.add(new BasicNameValuePair("name", "unknown"));
   	            	params.add(new BasicNameValuePair("date", "100000"));
   	            	params.add(new BasicNameValuePair("gridref", "unknown"));
   	            	params.add(new BasicNameValuePair("location", "unknown"));
   	            	params.add(new BasicNameValuePair("longitude", "0"));
   	            	params.add(new BasicNameValuePair("latitude", "0"));
   	            	params.add(new BasicNameValuePair("species", "unknown"));
   	            	params.add(new BasicNameValuePair("comment", readData.get(i)));
   	            	params.add(new BasicNameValuePair("appname", Constants.SETTINGCONST));
   	            }
   	            
   	          //  Thread.sleep(2000);
   	            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
   	            post.setEntity(ent);
   	            Log.i("sentMessage","sentMessage " + i);
   	            HttpResponse responsePOST = client.execute(post);  
   	            HttpEntity resEntity = responsePOST.getEntity();  
   	            if (resEntity != null) {   
   	            	String resulttxt = EntityUtils.toString(resEntity);
   	                Log.i("RESPONSE",resulttxt);
   	                
   	                if (resulttxt != null)
   	                {
   	                	if(!resulttxt.contains("stored OK"))
   	                	{
   	                		throw new Exception("Failed to store data");
   	                	}
   	                	else
   	                	{
   	                		savedresulttext = resulttxt.replace("stored OK", ""); 
   	                		sentData.add(readData.get(i));
   	                		sent++;
   	                		}
   	                }
   	                
   	            }
			 }

   	            int progress = (i*100)/readData.size();
   	            publishProgress((int)progress);
   	            Log.i("Set Progress","Set Progress " + progress);
   	           // Toast.makeText(BumblebeeSurveyActivity.this, "Successfully wrote " + (i+1) + " records ", Toast.LENGTH_SHORT).show();
			
			}
			result = sent;
   	 }
   	 catch (Exception e) {
   	        e.printStackTrace();
   	      //  Toast.makeText(BumblebeeSurveyActivity.this, " Failed to send records ", Toast.LENGTH_LONG).show();
     		   
   	    }
	 }
			
       	 if(result != -1)
       	 {
       		 ResultFileHelper filex1 = new ResultFileHelper(theContext.getExternalFilesDir(null));
       		 filex1.removeData();
       		 
       		 HistoryFileHelper filex2 = new HistoryFileHelper(theContext.getExternalFilesDir(null));
       		 filex2.saveData(sentData);
       	 }
   	   
	}
	catch(Exception e)
	{
		Log.e(this.toString(), "Got an exception in do in background", e);
	}
	            
	            return result;
}
@Override
protected void onProgressUpdate(Integer... progress) 
{
	 if (progressdialog != null)
	 {
	 	if (progress[0] != 101)
	 	{
	 		Log.i(this.toString(),"Progress updated " + progress[0] + progressdialog.isShowing());
	 		
	 		progressdialog.show();
	 		progressdialog.setProgress(progress[0]);
	 	}
	 	else
	 	{
	 	//	dialog.dismiss();
	 	}
	 }
} 


private Integer sendemail(SharedPreferences settings,ResultFileHelper filex)
{
	 Boolean EMAILtype = settings.getBoolean("EMAIL",false);   
	 Integer result =-1;
   	 if(EMAILtype )
   	 {
   		 
   		 publishProgress(101);
   	Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE); 
		
		 
		//Uri uri = Uri.fromFile(new File(getExternalFilesDir(null),  Constants.SETTINGCONST + "results.csv"));  
		//ArrayList<Uri> uris = new ArrayList<Uri>();   
		//uris.add(uri);
		// uris.add(uri);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"afenner@eircom.net"});
		i.putExtra(Intent.EXTRA_SUBJECT, "results of survey for a bumblebee survey"  );
		ArrayList<String> readData = filex.readData();
		String surveyresult="";
		for(int y = 0 ; y < readData.size();y++)
		{
			surveyresult += readData.get(y) +" \n";
		}
		i.putExtra(Intent.EXTRA_TEXT   , " These are the results of the \n" + surveyresult);
	//	i.putExtra(Intent.EXTRA_STREAM   , uri);
		  
		// i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris); 
		
		try {    
			theContext.startActivity(Intent.createChooser(i, "Send mail..."));
			// finish();
			result = readData.size();
			}
		catch (android.content.ActivityNotFoundException ex)
		{    
			    }
   	 }
   	 return result;
}
protected void onPostExecute(Integer onPostExecute) 
{  
	Log.i(this.toString(),"onPostExecute " + onPostExecute); 
	
	 if (progressdialog != null){
		 progressdialog.dismiss();
	 }
	 if (onPostExecute != -1)
	 {
		 ResultFileHelper filex = new ResultFileHelper(
				 theContext.getExternalFilesDir(null));
	       ArrayList<String> readData = filex.readData();
	          
	      
	       if (submitbutton != null)
	       {
	    	   submitbutton.setText("Submit " + readData.size() +" Records");
	       }
		 AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
		 if (savedresulttext != null)
		 {
			 
		 }
		 else
		 {
			 savedresulttext = "";
		 }
		
		 builder.setMessage("Sucessfully sent all " + onPostExecute + " records \n" +
				 savedresulttext)
		 .setCancelable(false)
		 .setPositiveButton("OK", new DialogInterface.OnClickListener() 
		 	{           
			 	public void onClick(DialogInterface dialog, int id) 
			 	{              
			 		dialog.cancel();  
			 		  if (finish)
				       {
				    	   ((android.app.Activity)theContext).finish();
				    	   
				       }
			 		  else{
			 			  if (theContext instanceof SendDataActivity)
			 			  {
			 				  ((SendDataActivity)theContext).init(false,"");
			 			  }
			 		  }
			 		  }       });
		 AlertDialog alert = builder.create();
		 alert.show();
		 
	       
	     
	 }
	 else{
		 AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
		 builder.setMessage("Failed to sent all the data.\nPlease try again.\n")
		 .setCancelable(false)
		 .setPositiveButton("OK", 
				 new DialogInterface.OnClickListener() 
		 {           
			 	public void onClick(DialogInterface dialog, int id) 
			 	{              
			 		dialog.cancel();   
			 	 if (finish)
			       {
			    	   ((android.app.Activity)theContext).finish();
			       }}       });
		 AlertDialog alert = builder.create();
		 alert.show();
		 
	 }
}
}



