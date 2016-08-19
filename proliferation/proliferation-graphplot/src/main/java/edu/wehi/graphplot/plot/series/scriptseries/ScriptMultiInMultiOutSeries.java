package edu.wehi.graphplot.plot.series.scriptseries;

import java.util.List;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptMultiInMultiOut;

public abstract class ScriptMultiInMultiOutSeries extends ScriptMultiInMultiOut
		implements ScriptTypeDifference<List<GPDataNode<GPXYSeries>>,
										List<GPDataNode<GPXYSeries>>,
										List<GPXYSeries>,
										List<GPXYSeries>>
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
	public List<GPDataNode<GPXYSeries>> convertOutput(List<GPXYSeries> out) {
		return ScriptSeriesFunctions.convertToNodes(out);
	} 

}
