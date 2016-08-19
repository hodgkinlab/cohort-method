package edu.wehi.celcalc.cohort.scriptables;

import java.util.List;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptBase;
import edu.wehi.graphplot.plot.series.scriptseries.ScriptSeriesFunctions;
import edu.wehi.graphplot.plot.series.scriptseries.ScriptTypeDifference;

public abstract class MultiMeasurementsInMultiOutBase extends ScriptBase<List<Measurement>, List<GPDataNode<GPXYSeries>>>
implements MultiMeasurementsInMultiOut

,ScriptTypeDifference<
		List<Measurement>, 
		List<GPDataNode<GPXYSeries>>,
		List<Measurement>,
		List<GPXYSeries>>
{
	private static final long serialVersionUID = 1L;
	

	@Override
	public List<Measurement> convertInput(List<Measurement> input)
	{
		return input;
	}

	@Override
	public List<GPDataNode<GPXYSeries>> convertOutput(List<GPXYSeries> out)
	{
		return ScriptSeriesFunctions.convertToNodes(out);
	} 

}
