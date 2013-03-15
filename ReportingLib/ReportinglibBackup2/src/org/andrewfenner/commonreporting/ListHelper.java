package org.andrewfenner.commonreporting;

import java.util.ArrayList;

public class ListHelper {
	
	private static ArrayList<Boolean> checkedlist= new ArrayList<Boolean>();
	
	public static void addItem(Boolean item)
	{
		checkedlist.add(item);
	}

	public static Boolean getItem(int position)
	{
		return checkedlist.get(position);
	}
	
	public static void setItem(int position,Boolean item)
	{
		 checkedlist.set(position,item);
	}
	
	
	public static void clear()
	{
		checkedlist.clear();
	}
	public static ArrayList<Boolean> getList()
	{
		return checkedlist;
	}

}
