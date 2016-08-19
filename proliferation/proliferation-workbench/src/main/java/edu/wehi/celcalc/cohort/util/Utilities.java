package edu.wehi.celcalc.cohort.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wehi.celcalc.cohort.data.Groupable;

public class Utilities {
	
	
	@SuppressWarnings("unchecked")
	public static <T extends Groupable> Map<String, List<T>> toMap(List<T> groupableList)
	{
		Map<String,List<T>> measurementsSet = new HashMap<>();
		
		for (Groupable groupable : groupableList)
		{
			if (measurementsSet.get(groupable.getGroupKey()) == null)
			{
				measurementsSet.put(groupable.getGroupKey(), new ArrayList<>());
			}
			measurementsSet.get(groupable.getGroupKey()).add((T)groupable);
		}
		return measurementsSet;
	}
	
	public static <T> List<T> toList(Map<String, List<T>> map)
	{
		List<T> list = new ArrayList<>();
		for (String key : map.keySet())
		{
			list.addAll(map.get(key));
		}
		return list;
	}
	
	
	
}
