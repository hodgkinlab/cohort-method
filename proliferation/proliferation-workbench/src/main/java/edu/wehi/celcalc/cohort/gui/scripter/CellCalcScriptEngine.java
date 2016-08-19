package edu.wehi.celcalc.cohort.gui.scripter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.util.InteractiveInterpreter;

import edu.wehi.celcalc.cohort.PackageScripterUtilities;
import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;
import edu.wehi.celcalc.cohort.scriptables.SeriesAndTable;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.python.NameableMap;
import edu.wehi.graphplot.python.NameableMapAndSeries;
import edu.wehi.graphplot.python.PyPlottingFunctions;

public class CellCalcScriptEngine extends InteractiveInterpreter implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	
	// variables used in the interpreter
	public static String resultMap 			= "result_map";
	public static String resultCoords 		= "result_coords";
	public static String error 				= "error";
	public static String all_times 			= "all_times";
	public static String all_divisions 		= "all_divisions";
	public static String all_treatments 	= "all_treatments";
	
	List<Measurement> measurements = new ArrayList<>();
	
	public CellCalcScriptEngine()
	{
		this (new ArrayList<>());
	}
	
	public CellCalcScriptEngine(List<Measurement> measurements)
	{
		super();
		this.measurements = measurements;
		initEngineVariablesAndFunctions(measurements);
	}
	
	
	public void exec(String s) 
	{
//		System.err.println("Python Code");
//		System.err.println(s);
		super.exec(s);
	}
	
	public List<Measurement> getMeasurements()
	{
		return measurements;
	}
	
	
	public List<NameableMap> execForEachTreatmentMap(String code)
	{
		List<NameableMap> maps = new ArrayList<>();
		List<Measurement> oldMeasurements = measurements;
		Set<String> allTreatments = MeasurementQuery.allTreatments(measurements);
		
		for (CellType type : CellType.values())
		{
		for (String treatment : allTreatments)
		{
		try
		{
			// Code which will be performed for each cell type and each treatment  
			List<Measurement> mess =  oldMeasurements.stream().filter(m -> m.getType() == type && m.getTreatment().equals(treatment)).collect(Collectors.toList());
			
			if (mess.size() == 0)
			{
				continue;
			}
			
			initEngineVariablesAndFunctions(mess);
			exec(code);
			NameableMap map = getMapResult();
			if (map != null)
			{
				if (map.size() != 0)
				{
					map.setName(type + "/"+treatment);
					maps.add(map);
				}
			}
			
		}catch (Exception e)
		{
			 e.printStackTrace();
		}
		}
		}
		
		setMeasurements(oldMeasurements);
		return maps;
	}
	
	
	private NameableMap getMapResult()
	{
		try
		{
			PyDictionary result = (PyDictionary) get(resultMap);
			NameableMap map = PyPlottingFunctions.convertToMap(result);
			return map;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public List<GPXYSeries> execForEachTreatment(String code)
	{
		List<GPXYSeries> seriess = new ArrayList<>();
		List<Measurement> oldMeasurements = measurements;
		Set<String> allTreatments = MeasurementQuery.allTreatments(measurements);
		
		for (CellType type : CellType.values())
		{
		for (String treatment : allTreatments)
		{
		try
		{
			
			
			// Code which will be performed for each cell type and each treatment  
			List<Measurement> mess =  oldMeasurements.stream().filter(m -> m.getType() == type && m.getTreatment().equals(treatment)).collect(Collectors.toList());
			
			if (mess.size() == 0)
			{
				continue;
			}
			
			initEngineVariablesAndFunctions(mess);
			exec(code);
			GPXYSeries series = getGPXYSeriesResult();
			if (series != null)
			{
				if (series.size() != 0)
				{
					series.setName(type + "/"+treatment);
					seriess.add(series);
				}
			}
			
			
		}catch (Exception e)
		{
			 e.printStackTrace();
		}
		}
		}
		
		setMeasurements(oldMeasurements);
		return seriess;
	}
	
	private GPXYSeries getGPXYSeriesResult()
	{
		PyObject result = get(resultCoords);
		PyObject err = get(error);
		
		GPXYSeries series =   PyPlottingFunctions.convertToSeries((PyList)result);
		double[] errorArray = null;
		List<double[]> errorBounds= null;
		
		if (err != null)
		{
			try
			{
				errorArray = PyPlottingFunctions.convertToDoulbeArray((PyList)err);
				if (errorArray == null) throw new RuntimeException("");
			}
			catch(Exception e)
			{
				errorBounds = PyPlottingFunctions.convertToListOfPoints((PyList)err);
			}
		}
		
		
			
				int sz = 0;
				
				if (errorArray != null)
				{
					sz = errorArray.length;
				}
				else if (errorBounds != null)
				{
					sz = errorBounds.size();
				}
				
				for (int i = 0; i < sz; i++)
				{
					if (errorArray != null)
					{
						series.coordinates.get(i).setUpperAndLower(errorArray[i]/2.0);
					}
					else if(errorBounds != null)
					{
						series.coordinates.get(i).setYLower(errorBounds.get(i)[0]);
						series.coordinates.get(i).setYUpper(errorBounds.get(i)[1]);
					}
				}
			
		
		return series;
	}

	public void initEngineVariablesAndFunctions(List<Measurement> measurements)
	{
		setMeasurements(measurements);
		setUpCohortFunctions();
	}
	
	public void setUpCohortFunctions()
	{
		exec(PackageScripterUtilities.getTreatmentPy());
	}
	
	public void setMeasurements(List<Measurement> measurements)
	{
		this.measurements = measurements;
		set("measurements", measurements);
		
		List<Double> allTimes = MeasurementQuery.allTimes(measurements);
		Set<Integer> allDivisions = MeasurementQuery.allDivisions(measurements);
		Set<String> allTreatments = MeasurementQuery.allTreatments(measurements);
		
		set(all_times, 		allTimes);
		set(all_divisions, 	allDivisions);
		set(all_treatments, allTreatments);
		
		setXKMap(measurements, new HashSet<>(allTimes),allDivisions);
		
	}
	
	DefaultMap x;
	
	public void setXKMap(List<Measurement> measurements, Set<Double> allTimes, Set<Integer> allDivisions)
	{
		x = new DefaultMap();
		
		for (Double t : allTimes)
		{
			for (Integer d : allDivisions)
			{
				try
				{
					double avg = measurements.stream().filter(m -> m.getTime() == t && m.getDiv() == d).mapToDouble(m -> m.getCells()).average().getAsDouble();
					x.put(d, t, avg);
				}
				catch (Exception e)
				{

				}
			}
		}
		
		set("x_java",x);
	}
	
	public static class DefaultMap extends HashMap<Integer, Map<Double,Double>>
	{
		private static final long serialVersionUID = 1L;
		
		public Double get(Integer div, Double time)
		{
			Map<Double,Double> mapinner = get(div);
			if (mapinner == null)
			{
				return 0.0;
			}
			else
			{
				Double x = mapinner.get(time);
				if (x == null)
				{
					return 0.0;
				}
				else
				{
					return x;
				}
			}
		}
		
		public void put(Integer div, Double time, Double count)
		{
			if (get(div) == null)
			{
				put(div, new HashMap<Double,Double>());
			}
			get(div).put(time, count);
		}
	}

	public List<SeriesAndTable> execForEachTreatmentMapAndCoords(String code)
	{
		List<Measurement> oldMeasurements = measurements;
		Set<String> allTreatments = MeasurementQuery.allTreatments(measurements);
		List<SeriesAndTable> seriesAndTable = new ArrayList<>();
		
		for (CellType type : CellType.values())
		{
		for (String treatment : allTreatments)
		{
		try
		{
			// Code which will be performed for each cell type and each treatment  
			List<Measurement> mess =  oldMeasurements.stream().filter(m -> m.getType() == type && m.getTreatment().equals(treatment)).collect(Collectors.toList());
			
			if (mess.size() == 0)
			{
				continue;
			}
			
			initEngineVariablesAndFunctions(mess);
			exec(code);
			
			NameableMap map = null;
			try {map = getMapResult();} catch(Exception e){e.printStackTrace();} 
			
			GPXYSeries series = null;
			try {series = getGPXYSeriesResult();} catch(Exception e){e.printStackTrace();} 
			
			if (series == null && map == null)
			{
				continue;
			}
			
			if (map != null)
			{
				if (map.size() != 0)
				{
					map.setName(type + "/" + treatment);
				}
			}
			
			if (series != null)
			{
				if (series.size() != 0)
				{
					series.setName(type + "/" + treatment);
				}
			}
			
			seriesAndTable.add(new SeriesAndTable(series.getName(), series, map));
			
			
		}catch (Exception e)
		{
			 e.printStackTrace();
		}
		}
		}
		
		setMeasurements(oldMeasurements);
		
		return seriesAndTable;
	}

	public List<NameableMapAndSeries> execForEachTreatmentMapAndCoordsMapAndSeries(String code)
	{
		List<Measurement> oldMeasurements = measurements;
		Set<String> allTreatments = MeasurementQuery.allTreatments(measurements);
		List<NameableMapAndSeries> seriesAndTable = new ArrayList<>();
		
		for (CellType type : CellType.values())
		{
		for (String treatment : allTreatments)
		{
		try
		{
			
			String iterName = type + "/" + treatment;
			
			// Code which will be performed for each cell type and each treatment  
			List<Measurement> mess =  oldMeasurements.stream().filter(m -> m.getType() == type && m.getTreatment().equals(treatment)).collect(Collectors.toList());
			
			if (mess.size() == 0)
			{
				continue;
			}
			
			initEngineVariablesAndFunctions(mess);
			exec(code);
			
			
			NameableMap map = getMapResult();
			GPXYSeries series = getGPXYSeriesResult();
			
			if (series == null || map == null)
			{
				continue;
			}
			
			if (map != null)
			{
				if (map.size() != 0)
				{
					map.setName(iterName);
				}
			}
			
			if (series != null)
			{
				if (series.size() != 0)
				{
					series.setName(iterName);
				}
			}
			
			seriesAndTable.add(new NameableMapAndSeries(iterName, map, series));
			
			
		}catch (Exception e)
		{
			 e.printStackTrace();
		}
		}
		}
		
		setMeasurements(oldMeasurements);
		
		return seriesAndTable;
	}

}
