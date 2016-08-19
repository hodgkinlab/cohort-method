package edu.wehi.graphplot.python;

import edu.wehi.graphplot.plot.GPXYSeries;

public class NameableMapAndSeries
{
	NameableMap map;
	GPXYSeries series;
	String name;
	
	public NameableMapAndSeries(String name, NameableMap map, GPXYSeries series)
	{
		super();
		this.map = map;
		this.series = series;
		this.name = name;
	}
	
	public NameableMap getMap()
	{
		return map;
	}
	
	public void setMap(NameableMap map)
	{
		this.map = map;
	}
	
	public GPXYSeries getSeries()
	{
		return series;
	}
	
	public void setSeries(GPXYSeries series)
	{
		this.series = series;
	}

	public String getName() {
		return name;
	}	

}
