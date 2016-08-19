package edu.wehi.celcalc.cohort.gui.parameterer.type;

import java.util.HashMap;
import java.util.Map;

public class InputParameters
{
	
	String name;
	public InputParameters(String name)
	{
		this.name = name;
	}
	
	final Map<String, Parameterable> paramaters = new HashMap<>();
	
	public Map<String, Parameterable> getParameters()
	{
		return paramaters;
	}
	
	public boolean addParameter(Parameterable parameter)
	{
		if (paramaters.containsKey(parameter.getName()))
		{
			return false;
		}
		else
		{
			paramaters.put(parameter.getName(), parameter);
			return true;
		}
	}
		

}
