package org.andrewfenner.commonreporting;

import java.util.ArrayList;
import java.util.List;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapPageActivity extends MapActivity {

		private GeoPoint lastpoint=null;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	      // requestWindowFeature(Window.FEATURE_NO_TITLE); 
	       setContentView(R.layout.mappagebutton);
	      
	
	       MapView mapView = (MapView) findViewById(R.id.mapview);  
	       mapView.setBuiltInZoomControls(true);
	       
	     
	       MonitorGpsTask mgt = MonitorGpsTask.getInstance();
	       Location lastLocation2 = mgt.getLastLocation();
	       if (lastLocation2 == null)
	       {
	    	   lastLocation2 = mgt.getLastLocationCell();
	       }
	       
	       double lat = 53.61858; 
	       double lng=-7.81128; 
	       if(lastLocation2 != null)
	       {
	    	   lat = lastLocation2.getLatitude();
	    	   lng = lastLocation2.getLongitude();
	       }
	       
	       GeoPoint point = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6)); 
	       
	       lastpoint = point;
	       //debug key
	       //97:EF:17:D3:98:98:79:F2:80:0F:C3:D8:A2:CD:44:74
	       //0wDJW2FTAQoL6KzRduwGe__X3CfXS0vIImEGkVA
	      // GeoPoint point = new GeoPoint(35410000, 139460000); 
		     
           //94:1F:FB:AC:F9:E4:E4:45:C5:5B:3C:E5:88:F8:64:55
	       //0wDJW2FTAQoJdnjFIDbn_teHGsQ5w8kVlXOPK_w
	       mapView.getController().setCenter(point);
	       
	       List<Overlay> mapOverlays = mapView.getOverlays();
	       Drawable drawable = this.getResources().getDrawable(R.drawable.pointer);
	       PointOverlay itemizedoverlay = new PointOverlay(drawable, this);
	       
	       OverlayItem overlayitem = new OverlayItem(point, "", "");
	       itemizedoverlay.addOverlay(overlayitem);
	       mapOverlays.add(itemizedoverlay);	
	       
	       
	       Button button = (Button) findViewById(R.id.button1);  
	       button.setOnClickListener(onbutton);
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
		protected boolean isRouteDisplayed() {
			// TODO Auto-generated method stub
			return false;
		}

		
		
		
		private class PointOverlay extends ItemizedOverlay
		{

			private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
			private Context mContext;
			public PointOverlay(Drawable defaultMarker)
			{
				super(boundCenterBottom(defaultMarker));
			}
			
			public void addOverlay(OverlayItem overlay) 
			{
				mOverlays.add(overlay);  
				populate();
			}
			@Override
			protected OverlayItem createItem(int i) {
				  return mOverlays.get(i);
			}

			@Override
			public int size() {
				return mOverlays.size();
			}
			
			public PointOverlay(Drawable defaultMarker, Context context)
			{
					super(boundCenterBottom(defaultMarker)); 
					mContext = context;
			}
			
			@Override
			protected boolean onTap(int index)
			{  
				OverlayItem item = mOverlays.get(index);  
				AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);  
				dialog.setTitle(item.getTitle());  
				dialog.setMessage(item.getSnippet());  
				dialog.show();  
				return true;
			}
			@Override
			public boolean onTap(GeoPoint p, MapView mapView)
			{
				Log.d(this.toString(), "got onclick " + mapView);
				OverlayItem item = mOverlays.remove(0);
				
				OverlayItem overlayitem = new OverlayItem(p, "", "");
				this.addOverlay(overlayitem);
				lastpoint = p;
				return true;
			}
		}
			 
	
		private OnClickListener onbutton = new View.OnClickListener(){
			
			public void onClick(View v)
			{
				Log.d(this.toString(), "Save button pressed");
				MapPageActivity.this.getIntent().putExtra("lat", lastpoint.getLatitudeE6());
				MapPageActivity.this.getIntent().putExtra("lng", lastpoint.getLongitudeE6());
				setResult(2,MapPageActivity.this.getIntent());
				finish();
				 	
				// finish();
			}
			};
	
			 
			
		
}
