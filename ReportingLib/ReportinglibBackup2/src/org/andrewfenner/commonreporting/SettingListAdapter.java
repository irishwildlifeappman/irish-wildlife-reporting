package org.andrewfenner.commonreporting;

import java.io.File;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SettingListAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[] settingname;
    private Boolean[] booleanvalues;
    private static LayoutInflater inflater=null;
    
    private File basefile;
    private String siteName;
    
    public SettingListAdapter(Activity a, String[] d,Boolean[] bv) {
        activity = a;
        settingname=d;
        booleanvalues=bv;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       
    }

    public int getCount() {
        return settingname.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.setting_list_item, null);

        TextView text=(TextView)vi.findViewById(R.id.listtext);;
        CheckBox checkbox=(CheckBox)vi.findViewById(R.id.checkBoxSetting);
        checkbox.setChecked(booleanvalues[position]);
        
       String name = settingname[position];
       SharedPreferences settings = activity.getSharedPreferences(Constants.SETTINGCONST, 0);  
       if (settings.contains(name))
       {
    	   Boolean settingvalue = settings.getBoolean(name,false);   
    	   checkbox.setChecked(settingvalue);
           
       }
        text.setText(settingname[position]);
      //  image.setImageResource(R.drawable.baticonsmall); 
        AfrCheckedChangeListener listener = new AfrCheckedChangeListener();
        listener.setPosition(position);
        checkbox.setOnCheckedChangeListener(listener);
       
        return vi;
    }
    
    public class AfrCheckedChangeListener implements CheckBox.OnCheckedChangeListener
    {
    	private Integer position = 0;
		
		public void setPosition(Integer pos)
		{
			position = pos;
		}

		
	


		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			Log.d(this.toString(), "Got a click of a button " + position + " v " + arg1 );
			booleanvalues[position]=arg1;

			 SharedPreferences settings = activity.getSharedPreferences(Constants.SETTINGCONST, 0);  
		      
			 SharedPreferences.Editor editor = settings.edit();    
			 editor.putBoolean(settingname[position], arg1);  
			 // Commit the edits!   
			 editor.commit();
		} 
    }
}