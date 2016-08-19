package edu.wehi.celcalc.cohort.scriptables;

import java.util.ArrayList;
import java.util.List;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.series.ScriptBase;
import edu.wehi.graphplot.plot.series.scriptseries.ScriptTypeDifference;
import edu.wehi.graphplot.python.NameableMap;


public abstract class MultiInMeasurementParamterScriptBase extends ScriptBase<
List<Measurement>, List<GPDataNode<NameableMap>>
> 

implements MeasurementParamaterScriptable

,ScriptTypeDifference<
List<Measurement>, 
List<GPDataNode<NameableMap>>,
List<Measurement>,
List<NameableMap>>
{
	private static final long serialVersionUID = 1L;

	@Override
	public List<Measurement> convertInput(List<Measurement> input)
	{
		return input;
	}

	@Override
	public List<GPDataNode<NameableMap>> convertOutput(
			List<NameableMap> out)
	{
		
		List<GPDataNode<NameableMap>> ouput = new ArrayList<>();
		for (NameableMap o : out)
		{
			ouput.add(new GPDataNode<NameableMap>(o));
		}
		return ouput;
	}

}
