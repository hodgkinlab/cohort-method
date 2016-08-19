package edu.wehi.celcalc.cohort.scriptables;

import java.util.List;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptBase;
import edu.wehi.graphplot.plot.series.scriptseries.ScriptSeriesFunctions;
import edu.wehi.graphplot.plot.series.scriptseries.ScriptTypeDifference;

public abstract class MultiMeasurementsInSingleOutBase extends ScriptBase<List<Measurement>, GPDataNode<GPXYSeries>> 
implements MultiMeasurementsInSingleOut, ScriptTypeDifference<List<Measurement>, GPDataNode<GPXYSeries>,
List<Measurement>, GPXYSeries>
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public List<Measurement> convertInput(List<Measurement> input)
	{
		return input;
	}

	@Override
	public GPDataNode<GPXYSeries> convertOutput(GPXYSeries out)
	{
		return ScriptSeriesFunctions.convertToNode(out);
	}
}
