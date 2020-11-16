package org.andrewfenner.commonreporting;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class Constants {
	public static String HELLO = "HELLO";
	
	public static void init(Context mContext)
	{
		Resources resources = mContext.getResources();
		AssetManager assets = resources.getAssets();
		try {
			InputStream is = assets.open("appinfo.txt");
			DataInputStream in = new DataInputStream(is);
	          BufferedReader br = new BufferedReader(new InputStreamReader(in));

			
			
			String readline = br.readLine();
			ArrayList<String> tempspeciesnameslist = new ArrayList();
			ArrayList<String> tempspeciesgroups = new ArrayList();
			System.out.println( " readline " + readline);
			if (readline != null)
			{
				String[] firstline = readline.split(",");
				SETTINGCONST =firstline[0];
				if (firstline.length > 1)
				{
					picturesize = Boolean.parseBoolean(firstline[1]);
				}
				int counter=0;
				readline = br.readLine();
				while  (readline != null)
				{
					String[] split = readline.split(",");
				//	System.out.println("Adding " + readline);
					if (split != null && split.length == 2)
					{
				//	System.out.println("Adding " + counter  + split[0] + " " + split[1]);
					tempspeciesnameslist.add(split[0].trim().replace("\"", "").replace("\n", ""));
					tempspeciesgroups.add(split[1].trim().replace("\"", "").replace("\n", ""));
					counter++;
					}
					readline = br.readLine();
				}
				speciesnameslist = (String[])tempspeciesnameslist.toArray(speciesnameslist);
				speciesgroups = (String[])tempspeciesgroups.toArray(speciesgroups);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  static String SETTINGCONST = "DummyApp";
	public  static boolean picturesize = true;
	
	public  static  String[] speciesnameslist = {
		
		
};

public  static String[] speciesgroups = {
	
};
}
