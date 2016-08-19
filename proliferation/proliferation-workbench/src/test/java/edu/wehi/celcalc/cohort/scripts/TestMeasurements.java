package edu.wehi.celcalc.cohort.scripts;

import java.util.ArrayList;
import java.util.List;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.graphplot.plot.GPXYSeries;

public class TestMeasurements
{
	
	public final static List<Measurement> measurements = new ArrayList<Measurement>(){

		private static final long serialVersionUID = 1L;
		{
			add(new Measurement(0, 2, 1, CellType.LIVE, "test"));
			add(new Measurement(0, 3, 2, CellType.LIVE, "test"));
			add(new Measurement(2, 2, 3, CellType.LIVE, "test"));
			add(new Measurement(2, 4, 4, CellType.LIVE, "test"));
		}
	};
	
	public static GPXYSeries series = new GPXYSeries("LIVE/test", measurements);
	

}
