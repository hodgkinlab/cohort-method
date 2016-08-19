package edu.wehi.graphplot.plot.series.scriptseries;

import java.util.List;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptMultiInSingleOut;

public abstract class ScriptMultiInSingleOutSeries extends ScriptMultiInSingleOut
		implements  ScriptTypeDifference<List<GPDataNode<GPXYSeries>>,
										 GPDataNode<GPXYSeries>,
										 List<GPXYSeries>,
										 GPXYSeries>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<GPXYSeries> convertInput(List<GPDataNode<GPXYSeries>> input) {
		return ScriptSeriesFunctions.convertToSeriess(input);
	}

	@Override
	public GPDataNode<GPXYSeries> convertOutput(GPXYSeries out) {
		return ScriptSeriesFunctions.convertToNode(out);
	}

}
