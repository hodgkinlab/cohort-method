package edu.wehi.celcalc.cohort.data;

import java.util.HashMap;
import java.util.Map;

public enum CellType {
	LIVE("live"),
	DEAD("dead"),
	DROP("drop");
	
	private static final Map<String, CellType> map = new HashMap<>();
	
	static
	{
		for (CellType type : CellType.values())
		{
			map.put(type.name, type);
		}
	}
	
	public final String name;
	CellType(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public static CellType cellType(String name)
	{
		return map.get(name);
	}
	
	public static void main(String[] args)
	{
		System.out.println(map.get("sdfd"));
	}
	
	public static boolean isIn(String name)
	{
		return map.containsKey(name);
	}
	
}
