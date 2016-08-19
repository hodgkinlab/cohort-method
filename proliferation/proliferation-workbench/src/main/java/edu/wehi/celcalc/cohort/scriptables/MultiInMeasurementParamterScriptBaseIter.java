package edu.wehi.celcalc.cohort.scriptables;

import java.util.List;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.graphplot.python.NameableMap;


public abstract class MultiInMeasurementParamterScriptBaseIter extends MultiInMeasurementParamterScriptBase implements MeasurementIterHelperInterface<NameableMap>
{

	private static final long serialVersionUID = 1L;
	
	public List<NameableMap> scriptxy(List<Measurement> measurements)
	{
		return MeasurementIterHelperInterface.super.scriptxy(measurements);
	}


}
