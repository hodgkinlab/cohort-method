package edu.wehi.graphplot.python;



import java.util.ArrayList;
import java.util.List;

import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyTuple;

import edu.wehi.graphplot.plot.GPCoordinateWithNameImp;
import edu.wehi.graphplot.plot.GPPlotterFuncs;
import edu.wehi.graphplot.plot.GPXYSeries;

public class PyPlottingFunctions
{

	public static void plotPyList(PyList pyList, String name)
	{
		GPPlotterFuncs.plotSeriesList(convertToSeries(pyList, name));
	}
	
	public static GPXYSeries convertToSeries(PyList pyList, String name)
	{
		@SuppressWarnings("rawtypes")
		List coords = (List) pyList.__tojava__(List.class);
		
		List<GPCoordinateWithNameImp> gpCoords = new ArrayList<>();
		
		for (int i = 0; i < coords.size(); i++)
		{
			PyTuple tuple = (PyTuple) coords.get(i);
			
			double x = toDouble(tuple.get(0));
			double y = toDouble(tuple.get(1));
			
			GPCoordinateWithNameImp coord = new GPCoordinateWithNameImp(x, y, 0.0, "");
			gpCoords.add(coord);
		}
		
		return new GPXYSeries(name, gpCoords);
	}
	
	public static GPXYSeries convertToSeries(PyList pyList)
	{
		return convertToSeries(pyList, "");
	}
	
	public static void plotPyList(PyList pyList)
	{
		plotPyList(pyList,"");
	}
	
	static double toDouble(Object object)
	{
		if (object instanceof Double)
		{
			return (Double) object;
		}
		else if (object instanceof Integer)
		{
			return ((Integer) object).doubleValue();
		}
		throw new RuntimeException("Error converting coordinate from python to java");
	}
	
	public static NameableMap convertToMap(PyDictionary result)
	{
		NameableMap map = new NameableMapImp("");
		
		for (Object key : result.keySet())
		{
			map.put(key.toString(), result.get(key));
		}
		
		return map;
	}

	public static double[] convertToDoulbeArray(PyList result)
	{
		if (result == null)
		{
			return null;
		}
		
		int sz = result.size();
		
		if (sz == 0)
		{
			return null;
		}
		Object firstElement = result.get(0);
		if (firstElement instanceof PyTuple)
		{
			return null;
		}
		
		double[] errors = new double[sz];
		for (int i=0; i<sz; i++)
		{
			try{
			errors[i] = (double) result.get(i);
			}catch(Exception e)
			{
				errors[i] = (double) (int) result.get(i);
			}
		}
		return errors;
	}

	public static List<double[]> convertToListOfPoints(PyList pyList)
	{
		
		List<double[]> bounds = new ArrayList<>();
		
		
		@SuppressWarnings("rawtypes")
		List coords = (List) pyList.__tojava__(List.class);
		
		for (int i = 0; i < coords.size(); i++)
		{
			PyTuple tuple = (PyTuple) coords.get(i);
			
			double lower = toDouble(tuple.get(0));
			double upper = toDouble(tuple.get(1));
			
			bounds.add(new double[]{lower,upper});
		}
		
		return bounds;
	}


}
