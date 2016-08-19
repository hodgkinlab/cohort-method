package edu.wehi.graphplot.python;

import java.util.HashMap;
import java.util.Map;

public class NameableMapImp extends HashMap<String, Object> implements NameableMap
{
	private static final long serialVersionUID = 1L;
	
	String name;
	
	public NameableMapImp(String name, Map<String,Object> vals)
	{
		super(vals);
		this.name = name;
	}
	
	public NameableMapImp(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public int compareTo(NameableMap arg0)
	{
		return this.getName().compareTo(arg0.getName());
	}

}
