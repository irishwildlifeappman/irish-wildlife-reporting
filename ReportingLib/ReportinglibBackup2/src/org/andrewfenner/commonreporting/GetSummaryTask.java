package org.andrewfenner.commonreporting;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class GetSummaryTask extends AsyncTask<ResultFileHelper, Integer, Integer>
{  
	private ProgressDialog dialog;
	private Context theContext;
	private String savedresulttext=null;
	
	
	public GetSummaryTask(Context aContext)
	{
		// System.out.println("Creating UploadFileTask " + aContext);
		
	 theContext = aContext;
	 dialog = new ProgressDialog(theContext);
	 dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	 dialog.setIndeterminate(false);
	 dialog.setTitle("Reading from server");
	 dialog.setCancelable(true);
	 dialog.setMessage("fetching data");
	// System.out.println("created UploadFileTask " + dialog.getContext());
	
}
protected void onPreExecute()
{
	
//	 System.out.println("About to show " + theContext  +"  " +  dialog.getContext());
	 
	 dialog.show();
	// System.out.println("About to show " + dialog.isShowing());
	
 }
@Override
protected Integer doInBackground(ResultFileHelper... filex) 
{       
	 
    Integer result = -1;	
	   
   	
	
   		 
   		try
   		{	
			
   		  SharedPreferences settings = theContext.getSharedPreferences(Constants.SETTINGCONST, 0);  
	       String recodername = settings.getString("RecoderName","");   	
   	        HttpClient client = new DefaultHttpClient();  
   	        String postURL = "http://irishwildlifereporting.appspot.com/irishwildlifereporting/reportingServlet?requesttype=todaysinfo"+
   	        		"&appname="+Constants.SETTINGCONST
   	        		+"&requestername="+recodername;
   	        HttpGet post = new HttpGet(postURL); 
   	          //  List<NameValuePair> params = new ArrayList<NameValuePair>();
   	           
   	         //   
	        //	HttpParams params = new BasicHttpParams();  
	        //	params.setParameter("requesttype", "todaysinfo");
   	            
   	         //   params.setParameter("appname", Constants.SETTINGCONST);
   	         //   UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
   	         //   post.setParams(params);
   	        
   	            Log.i("sentMessage","sentMessage " );
   	            HttpResponse responsePOST = client.execute(post);  
   	            HttpEntity resEntity = responsePOST.getEntity();  
   	            if (resEntity != null) {   
   	            	String resulttxt = EntityUtils.toString(resEntity);
   	                Log.i("RESPONSE",resulttxt);
   	                
   	                if (resulttxt != null)
   	                {
   	                	if(!resulttxt.contains("Processed OK"))
   	                	{
   	                		throw new Exception("Failed to store data");
   	                	}
   	                	else
   	                	{
   	                		savedresulttext = resulttxt.replace("Processed OK", "");   	                	}
   	                }
   	                
   	            }
   	           
   	          
   	           // Toast.makeText(BumblebeeSurveyActivity.this, "Successfully wrote " + (i+1) + " records ", Toast.LENGTH_SHORT).show();
   	         result = 1; 
			}
			
   	
   	 catch (Exception e) {
   	        e.printStackTrace();
   	      //  Toast.makeText(BumblebeeSurveyActivity.this, " Failed to send records ", Toast.LENGTH_LONG).show();
     		   
   	    }
	
       	
   	
	            
	            return result;
}

protected void onPostExecute(Integer onPostExecute) 
{  
	 System.out.println("onPostExecute " + onPostExecute); 
	
	 if (dialog != null){
		 dialog.dismiss();
	 }
	 if (onPostExecute != -1)
	 {
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
		 if (savedresulttext != null)
		 {
			 
		 }
		 else
		 {
			 savedresulttext = "";
		 }
		
		 builder.setMessage("Todays response \n" +
				 savedresulttext)
		 .setCancelable(false)
		 .setPositiveButton("OK", new DialogInterface.OnClickListener() 
		 	{           
			 	public void onClick(DialogInterface dialog, int id) 
			 	{              
			 		dialog.cancel();  
			 		 
			 		  }       });
		 AlertDialog alert = builder.create();
		 alert.show();
		 
	       
	     
	 }
	 else{
		 AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
		 builder.setMessage("Couldn't read the information")
		 .setCancelable(false)
		 .setPositiveButton("OK", 
				 new DialogInterface.OnClickListener() 
		 {           
			 	public void onClick(DialogInterface dialog, int id) 
			 	{                dialog.cancel();   
			 	}       });
		 AlertDialog alert = builder.create();
		 alert.show();
		 
	 }
}
}



