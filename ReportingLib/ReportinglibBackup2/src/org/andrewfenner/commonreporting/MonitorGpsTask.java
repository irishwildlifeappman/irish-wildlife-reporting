package org.andrewfenner.commonreporting;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MonitorGpsTask extends Thread  implements LocationListener {

	private static MonitorGpsTask instance = new MonitorGpsTask();
	private LocationManager locationManager;
	private LocationListener subll = null;
	
	private Handler mHandler = null;
	private String savedprovider;
	private int savedstatus;
	private Bundle savedbundle;
	
	private Location updatedLocation =null;
	@Override
	public void run() {
		// Define the criteria how to select the locatioin provider -> use
		// default
		
		Looper.prepare();
		try{
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
		}
		catch (Exception e)
		{
			// buried
			Log.e(this.toString(), "Probably due to killed app", e);
		}
		
		 mHandler = new Handler() 
		 {          
			 public void handleMessage(Message msg) 
			 {                
				System.out.println("Got a message " + msg);            
				 }        
			 };
			
		
			
		 Looper.loop();      

	}

	private MonitorGpsTask()
	{
		
	}
	
	public String outputStatus()
	{
		updatedLocation.getExtras();
		
		System.out.println("status of MonitorGpsTask " + savedprovider + " " + savedstatus + " " + savedbundle);
		return "status " +  " "+ updatedLocation.getTime()
		+" " + updatedLocation.getExtras(); 
	}
	
	public Location getLastLocation()
	{
		
		return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	}
	
	public Location getLastLocationCell()
	{
		Location lastKnownLocation = null;//= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		//if (lastKnownLocation == null )
		//{
			lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		//}
		
		return lastKnownLocation;

	}
	public void subscribe(LocationListener ll)
	{
		try{
			
		
		subll =ll;
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1000, this);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void unsubscribe()
	{
		try{
			
		subll=null;
		unsubscribe(false);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
		
	}
	public void unsubscribe(boolean removeUpdates)
	{
		try{
			
		subll=null;
		if (removeUpdates)
		{
			locationManager.removeUpdates(this);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	public static MonitorGpsTask getInstance(LocationManager lm)
	{
		instance.locationManager = lm;
		return instance;
	}
	public static MonitorGpsTask getInstance()
	{
		return instance;
	}
	@Override
	public void onLocationChanged(Location location) {
		try{
			updatedLocation = location;	
		
		if (subll != null)
		{
			subll.onLocationChanged(location);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Got update " + location);
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
		System.out.println("provider "+  provider  + " status " + status +
				" extras " + extras);
		
		savedprovider = provider;
		savedstatus = status;
		savedbundle = extras;
	}

}
