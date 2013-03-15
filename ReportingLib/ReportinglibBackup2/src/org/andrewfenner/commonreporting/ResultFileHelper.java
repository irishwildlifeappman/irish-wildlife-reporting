package org.andrewfenner.commonreporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class ResultFileHelper {
   private String name;
   private String date;
   private String gridref;
   private String location;
   private String longitude;
   private String latitude;
   private String species;
   private String comment;
   private HashMap<String,String> pairs = new HashMap<String,String>();
 //  private String picturelocation;
   private File extdir;
           
	
	public ResultFileHelper(File extdir)
	{
		
		this.extdir = extdir;
			
			
	}
	
	
	
	
	
	public ResultFileHelper(String name, String date, String gridref,
			String location, String longitude, String latitude, String species,
			String comment, File extdir) {
		
		this.name = name;
		this.date = date;
		this.gridref = gridref;
		this.location = location;
		this.longitude = longitude;
		this.latitude = latitude;
		this.species = species;
		this.comment = comment;
		this.extdir = extdir;
		
	}


	//public void setPictureLocation(String picturelocation) {
	//	this.picturelocation = picturelocation;
	//}


	public void setName(String name) {
		this.name = name;
	}





	public void setDate(String date) {
		this.date = date;
	}





	public void setGridref(String gridref) {
		this.gridref = gridref;
	}





	public void setLocation(String location) {
		this.location = location;
	}





	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}





	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}





	public void setSpecies(String species) {
		this.species = species;
	}





	public void setComment(String comment) {
		if (comment.equals(""))
		{
			this.comment="NoComment";
		}
		else{
			this.comment = comment;
	
		}
	}


	public void removeData()
	{
		
		Log.d(this.toString(), "about to delete file"  +"-Result" + ".csv");
		File locationFile = new File(extdir, Constants.SETTINGCONST + "results.csv");
		File newlocationFile = new File(extdir, Constants.SETTINGCONST + "results-bak.csv");
		
		ArrayList<String> readData = readData();
		
	    	
				try {
					if(newlocationFile.exists())
					{
						newlocationFile.delete();
					}
					locationFile.renameTo(newlocationFile);
					
					
					
					File locationFileUpdated = new File(extdir, Constants.SETTINGCONST + "results.csv");
					 FileWriter locationFileWriter = null;
					 locationFileWriter = new FileWriter(locationFileUpdated,true);
					
					ArrayList<Boolean> list = ListHelper.getList();
					Log.d(this.toString(), "List not to remove"  +list);
					
					for (int i = 0;i<list.size();i++)
					{
						
						if (list.get(i) == false)
						{
							locationFileWriter.append(readData.get(i) +"\n");
						}
					}
					
					locationFileWriter.flush();
					locationFileWriter.close();
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
		
	}


	public ArrayList<String> readData()
	{
		ArrayList<String> result = new ArrayList<String>(); ;
		
		Log.d(this.toString(), "about to write file from " + extdir.getAbsolutePath() +" -Result" + ".csv");
		File locationFile = new File(extdir, Constants.SETTINGCONST + "results.csv");
		 FileReader locationFileWriter = null;
	    	
				try {
					locationFileWriter = new FileReader(locationFile);
					BufferedReader locationFileReader = new BufferedReader(locationFileWriter);
					String line = locationFileReader.readLine();
					while (line != null)
					{
						result.add(line);
						line = locationFileReader.readLine();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
		return result;
	}

	public ArrayList<String> readData(int position )
	{
		ArrayList<String> result = new ArrayList<String>(); ;
		
		Log.d(this.toString(), "about to write file from " + extdir.getAbsolutePath() +" -Result" + ".csv");
		File locationFile = new File(extdir, Constants.SETTINGCONST + "results.csv");
		 FileReader locationFileWriter = null;
	    	
				try {
					locationFileWriter = new FileReader(locationFile);
					BufferedReader locationFileReader = new BufferedReader(locationFileWriter);
					String line = locationFileReader.readLine();
					while (line != null)
					{
						result.add(line);
						line = locationFileReader.readLine();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				String[] split = result.get(position).split(",");
				ArrayList temp = new ArrayList();
				for(int i = 0; i < split.length;i++)
				{
					temp.add(split[i]);
				}
		return temp;
	}
	
	public void saveData()
	{
		  
		Log.d(this.toString(), "about to write file"  +"-Result" + ".csv");
		File locationFile = new File(extdir, Constants.SETTINGCONST + "results.csv");
		 FileWriter locationFileWriter = null;
	    	
				try {
					locationFileWriter = new FileWriter(locationFile,true);
					
					String firstline =
						name+"," +date +"," +
					    gridref +"," +
					    location +"," +
					    longitude +"," +
					    latitude +"," +
					    species +"," +
					    comment.replace(",", " ") +"," +
					    pairs.get("number") +"," +
					    pairs.get("lifestage").replace(",", " ") +"," +
					    pairs.get("email") +"," +
					    pairs.get("habitat1") +"," +
					    pairs.get("habitat2")+ "," +
					    pairs.get("accuracy")
					    ;//+ "," +
					Log.d(this.getClass().getName(), "firstline  " + firstline + " to "
							+ locationFile.getAbsolutePath());
					firstline = firstline.replace("\n", " "); 
					locationFileWriter.write(firstline+"\n"); //$NON-NLS-1$

					
					locationFileWriter.flush();
					locationFileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	public void saveData(int position)
	{
		  
		ArrayList<String> readData = readData();
		Log.d(this.toString(), "about to write file"  +"-Result" + ".csv");
		File locationFile = new File(extdir, Constants.SETTINGCONST + "results.csv");
		 FileWriter locationFileWriter = null;
	    	
				try {
					locationFileWriter = new FileWriter(locationFile,false);
					
					String firstline =
						name+"," +date +"," +
					    gridref +"," +
					    location +"," +
					    longitude +"," +
					    latitude +"," +
					    species +"," +
					    comment.replace(",", " ") +"," +
					    pairs.get("number") +"," +
					    pairs.get("lifestage").replace(","," ") +"," +
					    pairs.get("email") +"," +
					    pairs.get("habitat1") +"," +
					    pairs.get("habitat2") + "," +
					    pairs.get("accuracy")
					    ;//+ "," +
					    //picturelocation;
					Log.d(this.getClass().getName(), "firstline  " + firstline + " to "
							+ locationFile.getAbsolutePath());
					firstline.replace("\n", " "); 
					
					readData.set(position, firstline);
					
					for (int i =0;i<readData.size();i++)
					{
						locationFileWriter.write(readData.get(i)+"\n"); 
					}

					
					locationFileWriter.flush();
					locationFileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}





	public void saveData(String name, String value) {
		pairs.put(name, value);
		
	}
	
	
}
