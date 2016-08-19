package edu.wehi.celcalc.cohort.runner;

public enum RunnerOptions
{	
	ALL("All Data"),
	PERMES("Default Sampling"),
	SETTINGS("Custom Settings");
	
	String txt;
	RunnerOptions(String txt)
	{
		this.txt = txt;
	}

	@Override
	public String toString()
	{
		return txt;
	}
}
