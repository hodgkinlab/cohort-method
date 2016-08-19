package edu.wehi.graphplot.plot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

public class GPPlotModel {
	
	private final List<GPXYSeries> seriess;
	private final String title;
	private final YIntervalSeriesCollection collection;
	
	final Map<GPXYSeries, YIntervalSeries> map = new HashMap<>();
	
	public YIntervalSeriesCollection getCollection()
	{
		return collection;
	}
	
	public List<GPXYSeries> getSeries()
	{
		return seriess;
	}
	
	public GPPlotModel(String title, List<GPXYSeries> seriess)
	{
		this.seriess = seriess;
		this.title = title;
		
		this.collection = new YIntervalSeriesCollection();
		for (GPXYSeries s : seriess)
		{
			YIntervalSeriesExtension series = GPPlotterFuncs.createIntervalXYDatasetSeries(s);
			collection.addSeries(series);
			map.put(s, series);
		}
		
		collection.setNotify(true);
	}
	
	public void hideSeries(long uuid)
	{
		GPXYSeries gp = getGPSeries(uuid);
		YIntervalSeries s = map.get(gp);
		if (contains(s))
		{
			collection.removeSeries(s);
		} else {
			System.err.println("series already hidden");
		}
	}
	
	public boolean contains(YIntervalSeries s)
	{
		for (int i = 0; i < collection.getSeriesCount(); i++)
		{
			if (s == collection.getSeries(i))
			{
				return true;
			}
		}
		return false;
	}
	
	public void hideAllSeries()
	{
		collection.removeAllSeries();
	}
	
	public void showSeries(long uuid)
	{
		GPXYSeries gp = getGPSeries(uuid);
		YIntervalSeries s = map.get(gp);
		if (contains(s))
		{
			collection.addSeries(s);
		} else {
			//System.err.println("series already shown"); // TODO this shouldn't happene except for the first correction!
		}
	}


	public GPXYSeries getGPSeries(long uuid) {
		for (GPXYSeries s : seriess)
		{
			if (s.id == uuid)
			{
				return s;
			}
		}
		return null;
	}
	
	public boolean isHidden(GPXYSeries series)
	{
		YIntervalSeries xyseries = map.get(series);
		return contains(xyseries);
	}
	
	public String getTitle() {	return title;}
	

}
