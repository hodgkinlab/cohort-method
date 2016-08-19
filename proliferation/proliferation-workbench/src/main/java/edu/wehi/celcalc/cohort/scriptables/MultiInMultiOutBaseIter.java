package edu.wehi.celcalc.cohort.scriptables;

import java.util.List;
import edu.wehi.celcalc.cohort.data.Measurement;

import edu.wehi.graphplot.plot.GPXYSeries;

public abstract class MultiInMultiOutBaseIter extends MultiMeasurementsInMultiOutBase implements MeasurementIterHelperInterface<GPXYSeries>
{
	private static final long serialVersionUID = 1L;
	
	
	public List<GPXYSeries> scriptxy(List<Measurement> measurements)
	{
		return MeasurementIterHelperInterface.super.scriptxy(measurements);
	}
	
}
