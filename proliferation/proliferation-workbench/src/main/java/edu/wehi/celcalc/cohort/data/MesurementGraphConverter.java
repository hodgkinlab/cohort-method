package edu.wehi.celcalc.cohort.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wehi.celcalc.cohort.util.Utilities;
import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPXYSeries;



public class MesurementGraphConverter {
	
	public static <T extends GPCoordinateWithName & Groupable> List<GPXYSeries>
	timeSeriessByGroupable(List<T> measurementsList,
						   String title,
						   String xAxis, 
						   String yAxis,
						   String description)
	{
		List<GPXYSeries> plots = new ArrayList<>();
		Map<String, List<T>> map = Utilities.toMap(measurementsList);
		for (String key : map.keySet())
		{
			GPXYSeries plot = new GPXYSeries(key,
										null,
										null,
										map.get(key), 
										xAxis,
										yAxis);
			plots.add(plot);
		}
		return plots;
	}

}
