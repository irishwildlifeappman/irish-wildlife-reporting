package org.andrewfenner.commonreporting;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class DataListAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<String> data;
    private static LayoutInflater inflater=null;
    
 //   private boolean[] checked;
 
	private Button thesendadata;
    
    public DataListAdapter(Activity a, ArrayList<String> d,Button sendadata) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
        
        thesendadata=sendadata;
       // checked =a.getIntent().getExtras().getBooleanArray("checkeddata");
        
        Log.d(this.toString(), "setting checked " + a.getIntent().getExtras());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
        	vi = inflater.inflate(R.layout.result_list_item, null);

        CheckBox cb=(CheckBox)vi.findViewById(R.id.senditem);;
        TextView speciesbox=(TextView)vi.findViewById(R.id.species);
        TextView datebox=(TextView)vi.findViewById(R.id.date);
    
        Button editbutton = (Button)vi.findViewById(R.id.editbutton);
        Datalistclick temppresser = new Datalistclick(position);
        //speciesbox.setOnClickListener(temppresser);
        editbutton.setOnClickListener(temppresser);
        //datebox.setOnClickListener(temppresser);
       
        Log.d("setting cb", "position " + position + " "+ ListHelper.getItem(position));
        
        cb.setChecked(ListHelper.getItem(position));
       
       // checked.add();
        cb.setOnCheckedChangeListener(new CheckListener(position));
        String[] line = data.get(position).split(",");
           if (line.length > 7)
           {
        	   speciesbox.setText(line[6]);
           //    locationbox.setText(line[3]);
               Date tempdate = new Date(Long.parseLong(line[1]));
               datebox.setText(tempdate.getDate()+"/"+(tempdate.getMonth()+1)+"/"+((tempdate.getYear()+1900)-2000));
         
           }  
        
        return vi;
    }
    
    
    
    public class  Datalistclick implements OnClickListener
    {
    	private int theposition = -1;
    	Datalistclick(int position)
    	{
    		theposition = position;
    	}
    	@Override
		public void onClick( View arg1) {
			
			Log.i("main", "got here ietem click 2 "  );
		
		//	Toast.makeText(arg1.getContext(), "Fill in the date " , Toast.LENGTH_LONG).show();
			
			
			Intent	xxx = new Intent(activity,SaveDetailsActivity.class);
			
			
		  
			xxx.putExtra("theposition", theposition);
		
			
			activity.startActivity(xxx);
		//	Log.i("main", "got here ietem click 3 " + arg0.getAdapter().getItem(arg2));
			return;
		}
		
    }
   
    
    public class  CheckListener implements OnCheckedChangeListener
    {
    	private int theposition = -1;
    	CheckListener(int position)
    	{
    		theposition = position;
    	}
    	
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			
			  //boolean[] tempArray = activity.getIntent().getBooleanArrayExtra("checkeddata");
		     
			
			
			ListHelper.setItem(theposition,arg1);
			
			ArrayList<Boolean> list = ListHelper.getList();
			int numbertosend = 0;
			for (int i=0;i<list.size();i++)
			{
				if (list.get(i)){
					numbertosend++;
				}
			}
			thesendadata.setText("Submit " + numbertosend +" Records");
			// activity.getIntent().putExtra("checkeddata",tempArray);
		//	Log.i("main", "got here ietem click 3 " + arg0.getAdapter().getItem(arg2));
			return;
			
		}
		
    }
	
}