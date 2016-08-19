package edu.wehi.celcalc.cohort.gui.analyizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.wehi.celcalc.cohort.application.FilterContainter;
import edu.wehi.celcalc.cohort.application.MeasurementListWithNoDuplicates;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.Filter;
import edu.wehi.celcalc.cohort.gui.analysis.Analysis;
import edu.wehi.celcalc.cohort.gui.analysis.PlotoriesContainer;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterContainer;
import edu.wehi.celcalc.cohort.runner.RunnerOptions;
import edu.wehi.celcalc.cohort.scriptables.MapAndSeriesScript;
import edu.wehi.celcalc.cohort.scriptables.MultiInMeasurementParamterScriptBase;
import edu.wehi.celcalc.cohort.scriptables.SeriesAndTable;
import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.python.NameableMap;
import edu.wehi.graphplot.python.NameableMapImp;

public class Analyizer
{		

	public static Map<Analysis, AnalyizerComponent> createAnalysisMap(
			Collection<Analysis> allAnalysissss,
			MeasurementListWithNoDuplicates measurementsListWithNoDuplicates,
			FilterContainter filterContainer,
			ScripterContainer scriptContainer,
			PlotoriesContainer plotoriesContainer
			)
	{
		Map<Analysis, AnalyizerComponent> mapFromAnalysisToComponent = new HashMap<>();
		
		
		for (Analysis analysis : allAnalysissss)
		{
			if (analysis.isShowing() == false || analysis.getScripts() == null)
			{
				continue;
			}
			
			performAnalysis(
					analysis, 
					measurementsListWithNoDuplicates,
					filterContainer.getFilters(analysis.getFilters()),
					scriptContainer.getScripts(analysis.getScripts()),
					mapFromAnalysisToComponent);
		}
		
		return mapFromAnalysisToComponent;
	}


	private static void performAnalysis(
			Analysis analysis,
			Collection<Measurement> measurements,
			Collection<Filter> filters,
			Collection<MapAndSeriesScript> scripts,
			Map<Analysis, AnalyizerComponent> mapFromAnalysisToComponent)
	{
		int 			seed = 					analysis.getRunner().getSeed();
		int		 		sampleSize = 			analysis.getRunner().getSampleSize();
		int 			noRuns = 				analysis.getRunner().getRuns();
		boolean 		isDisplayAllResults = 	analysis.getRunner().isShowAllResults();
		RunnerOptions	runnerOptions =	 		analysis.getRunner().getOptions();
		
		if (noRuns <= 0) noRuns = 1;
		if (sampleSize <= 0) sampleSize = 1000000000;
		
		
		Collection<Measurement> selectedMeasurements = Filter.filter(filters, measurements);
		if (selectedMeasurements.size() == 0)
		{
			return;
		}

		// lists of both the tables and the series
		List<GPDataNode<GPXYSeries>> seriesCollection = new ArrayList<>();
		List<NameableMap> allNameableMaps = new ArrayList<>();
		
		scripts.stream().filter(s -> s.getClass().isAssignableFrom(MultiInMeasurementParamterScriptBase.class)).collect(Collectors.toList()).size();
		
		for (MapAndSeriesScript script : scripts)
		{
			
			List<List<GPDataNode<SeriesAndTable>>> result = new ArrayList<>();
			
			switch(runnerOptions)
			{
			case ALL:
			{
				List<GPDataNode<SeriesAndTable>> r = script.script(new ArrayList<>(selectedMeasurements));
				result.add(r);
				
				break;
			}
			case PERMES:
			{	
				for (int i=0; i<noRuns; i++)
				{
					Collection<Measurement> mess = Measurement.subNoPerTimePoint(selectedMeasurements, seed*(i+1));
					List<GPDataNode<SeriesAndTable>> r = script.script(new ArrayList<>(mess));
					result.add(r);
				}
				
				break;
			}
			case SETTINGS:
			{	
				for (int i=0; i<noRuns; i++)
				{
					Collection<Measurement> mess = Measurement.subSet(selectedMeasurements, sampleSize, seed*(i+1));
					List<GPDataNode<SeriesAndTable>> r = script.script(new ArrayList<>(mess));
					result.add(r);
				}
				
				break;
			}
			default:
				break;
			
			}
			
				
			if (result.size() == 0)
			{
				continue;
			}

				
				List<GPDataNode<SeriesAndTable>> result_avg = avg(result,isDisplayAllResults);
				if (result_avg == null)
				{
					throw new RuntimeException("result_avg is NULL!!!!");
				}
				
				for (GPDataNode<SeriesAndTable> r : result_avg)
				{
					NameableMap dic = r.getData().getTable();
					GPXYSeries s = r.getData().getSeries();
					
					if (dic != null)
					{
						dic.setName(script.getName()+"/"+ dic.getName());
						allNameableMaps.add(dic);
					}
					
					if (s != null)
					{
						s.setName(script.getName()+"/"+ s.getName());
						seriesCollection.add(new GPDataNode<>(s.getName(),s));	
					}
				}

		}
		
		if (seriesCollection.size() == 0 && allNameableMaps.size() == 0)
		{
			return;
		}
		
		// add dictionary to map
		AnalyizerComponent component = new AnalyizerComponent(analysis,seriesCollection, allNameableMaps);
		mapFromAnalysisToComponent.put(analysis, component);
	}
	
	
	
	private static List<GPDataNode<SeriesAndTable>> avg(List<List<GPDataNode<SeriesAndTable>>> result, boolean isDisplayAllResults)
	{
		if (result.size() 		 == 0)	return null;
		if (result.get(0).size() == 0) 	return null;
		
		List<GPDataNode<SeriesAndTable>> averageResult = new ArrayList<>();
		
		int noTrials = result.size();
		int noOutputList = result.get(0).size();
			
		for (int o=0; o<noOutputList; o++)
		{
			List<SeriesAndTable> seriesAndTableList = new ArrayList<>();
			for (int i=0; i<noTrials; i++)
			{
				seriesAndTableList.add(result.get(i).get(o).getData());
			}
			SeriesAndTable mean = avgS(seriesAndTableList, isDisplayAllResults);
			averageResult.add(new GPDataNode<SeriesAndTable>(mean));
		}
		
		return averageResult;
	}


	private static SeriesAndTable avgS(List<SeriesAndTable> seriesAndTableList, boolean isDisplayAllResults)
	{
		if (seriesAndTableList.size() == 1)
		{
			return seriesAndTableList.get(0);
		}
		
		String name = seriesAndTableList.get(0).getName()+"_avg";
		List<GPCoordinateWithName> coords = new ArrayList<>();
		
		for (SeriesAndTable st : seriesAndTableList)
		{
			if (st.getSeries() == null)
			{
				continue;
			}
			coords.addAll(st.getSeries().coordinates);
		}
		GPXYSeries series = new GPXYSeries(name, null,coords);
		
		NameableMap map = new NameableMapImp(name);
		Map<String, List<Double>> toAvg = new HashMap<>();
		
		for (int i=0; i<seriesAndTableList.size(); i++)
		{
			NameableMap table = seriesAndTableList.get(i).getTable();
			if (table == null)
			{
				continue;
			}
			for (String key : table.keySet())
			{
				Object value = table.get(key);
				
				if (isDisplayAllResults)
				{
					map.put(key+"_r"+i, value);
				}
				
				if (value instanceof Integer)
				{
					value = ((Integer) value)+0.0;
				}
				if (value instanceof Double)
				{
					double val = (Double) value;
					if (!toAvg.containsKey(key))
					{
						toAvg.put(key, new ArrayList<>());
					}
					toAvg.get(key).add(val);
				}
			}
		}
		
		for (String key : toAvg.keySet())
		{
			
			List<Double> values = toAvg.get(key);
			
			double average = toAvg.get(key).stream().mapToDouble(d -> d).average().getAsDouble();
			double sum = 0;
			for (Double d : values)
			{
				sum += (d - average)*(d - average);
			}
			double var = sum / values.size();
			
			map.put(key+" avg:", average);
			map.put(key+" var: ", var);		
		}
		
		GPXYSeries avgSeries = series.computeAverageSeries();
		if (avgSeries.coordinates.size() == 0)
		{
			avgSeries = null;
		}
		return new SeriesAndTable(name, avgSeries, map);
	}
	

}
