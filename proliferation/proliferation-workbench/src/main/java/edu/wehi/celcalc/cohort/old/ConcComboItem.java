package edu.wehi.celcalc.cohort.old;

public class ConcComboItem 
{
	private String label;

	public ConcComboItem(Double index)
	{
		this.label = index+"";
	}
	
	@Override
	public String toString()
	{
		return label;
	}
}
