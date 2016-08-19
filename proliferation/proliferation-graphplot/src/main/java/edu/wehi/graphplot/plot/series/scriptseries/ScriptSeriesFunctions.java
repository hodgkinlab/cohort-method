package edu.wehi.graphplot.plot.series.scriptseries;

import java.util.List;
import java.util.stream.Collectors;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;

public interface ScriptSeriesFunctions {
	
	public static List<GPDataNode<GPXYSeries>> convertToNodes(List<GPXYSeries> series)
	{
		return series.stream().map(m -> convertToNode(m)).collect(Collectors.toList());
	}
	
	public static GPDataNode<GPXYSeries> convertToNode(GPXYSeries series)
	{
		return new GPDataNode<GPXYSeries>(null,series, series.name);
	}
	
	public static List<GPXYSeries> convertToSeriess(List<GPDataNode<GPXYSeries>> series)
	{
		return series.stream().map(m -> m.data).collect(Collectors.toList());
	}
	
	public static GPXYSeries convertToSingleSeires(GPDataNode<GPXYSeries> s)
	{
		return s.data;
	}
	
}
