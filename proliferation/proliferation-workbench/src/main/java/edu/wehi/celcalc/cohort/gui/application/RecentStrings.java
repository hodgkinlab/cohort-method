package edu.wehi.celcalc.cohort.gui.application;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class RecentStrings
{
	private final Preferences prefs;
	private final int noRecent;
	private static final int defaultNo = 10;
	private final String indexingKey;
	
	public RecentStrings()
	{
		this(Preferences.userRoot(), defaultNo, "r");
	}
	
	public RecentStrings(Preferences prefs, int noRecent, String indexingKey)
	{
		this.noRecent = noRecent;
		this.prefs = prefs;
		this.indexingKey = indexingKey;
	}
	
	public void setMostRecentString(String file)
	{
		if (!getAllRecentStrings().contains(file))
		{
			for (int i = noRecent-1; i >= 0; i--)
			{
				prefs.put(indexingKey+(i+1), prefs.get(indexingKey+i, ""));
			}
			if (prefs == null | indexingKey == null || file == null)
			{
				return;
			}
			prefs.put(indexingKey+0, file);
		}
		else
		{
			removeRecentString(file);
			setMostRecentString(file);
		}
	}
	
	public void removeRecentString(String file)
	{
		List<String> recents = getAllRecentStrings();
		recents.remove(file);
		for (int i=0; i < noRecent; i++)
		{
			String value;
			if (i < recents.size())
			{
				value = recents.get(i);
			}
			else
			{
				value = "";
			}
			prefs.put(indexingKey+i,value);
		}
	}
	
	public String getMostRecentString()
	{
		return prefs.get(indexingKey+0, "");
	}

	public List<String> getAllRecentStrings()
	{
		List<String> mostRecent = new ArrayList<>();
		for (int i = 0; i < 10; i++)
		{
			String value = prefs.get(indexingKey+i, "");
			if (!value.equals(""))
			{
				mostRecent.add(value);
			}
		}
		return mostRecent;
	}
}
