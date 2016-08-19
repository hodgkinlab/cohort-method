package edu.wehi.demo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.data.MesurementGraphConverter;
import edu.wehi.celcalc.cohort.res.TestFileData;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPController;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPNodeFuncs;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scripts.ScriptAddTimeSeries;
import edu.wehi.graphplot.plot.series.scripts.ScriptAverageTimeSeriesSingle;
import edu.wehi.graphplot.plot.series.scripts.ScriptFilterTimeSeriesByTag;

public class ExamplePlottingScripts {
	
	@SuppressWarnings("rawtypes")
	private static final List measurementsList = TestFileData.mesurements;
	
	@SuppressWarnings("unchecked")
	private static final List<GPXYSeries> series = MesurementGraphConverter
			.timeSeriessByGroupable(measurementsList, "", "", "", "");
	
	private static final List<GPDataNode<GPXYSeries>> nodes = GPNodeFuncs.toListDataNode(series);

	public static void main(String[] args) {
		
		//showAll();
		showAveragePlotForTag("LIVE/5000.0");
		//showAveragePlotForTag("LIVE/1000.0");
		//showAveragePlotForTag("LIVE/0.0");
	}
	
	public static void showAll()
	{
		List<GPXYSeries> serieslist = new ArrayList<GPXYSeries>();
		for (GPDataNode<GPXYSeries> node : nodes)
		{
			serieslist.add(node.getData());
		}
		plotSeriesList(serieslist);
	}
	
	public static void showAveragePlotForTag(String tag)
	{
		// get all series whose names match the tag
		List<GPDataNode<GPXYSeries>> selection = new ScriptFilterTimeSeriesByTag(nodes, tag).getOutput();
		// combine them all together by adding them to one another
		GPDataNode<GPXYSeries> sum =  new ScriptAddTimeSeries(selection).getOutput();
		// find the average
		GPDataNode<GPXYSeries> averageSeries = new ScriptAverageTimeSeriesSingle(sum).getOutput();
		// plot the data
		plotSeriesList(averageSeries.getData());
	}
	
	private static void plotSeriesList(GPXYSeries data) {
		List<GPXYSeries> serieslist = new ArrayList<GPXYSeries>();
		serieslist.add(data);
		plotSeriesList(serieslist);
	}

	public static void plotSeriesList(List<GPXYSeries> series)
	{
		GPController controller = new GPController("test", "time", "cells", series, GPChartType.LINE);
		JComponent comp = controller.getView();
		GUI.gui(comp);
	}

}
