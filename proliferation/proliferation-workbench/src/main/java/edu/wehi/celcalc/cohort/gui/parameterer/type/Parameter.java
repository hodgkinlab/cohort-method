package edu.wehi.celcalc.cohort.gui.parameterer.type;

public enum Parameter
{
	
	BOOLEAN("Boolean"),
	DOUBLE("Double"),
	INTEGER("Integer"),
	STRING("String");
	
	final String name;
	Parameter(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

}
