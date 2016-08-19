package edu.wehi.graphplot.plot.series.scriptseries;

import java.util.List;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptSingleInMultiOut;

public abstract class ScriptSingleInMultiOutSeries extends ScriptSingleInMultiOut

	implements ScriptTypeDifference<GPDataNode<GPXYSeries>,List<GPDataNode<GPXYSeries>>, GPXYSeries, List<GPXYSeries> >{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public GPXYSeries convertInput(GPDataNode<GPXYSeries> input) {
		return ScriptSeriesFunctions.convertToSingleSeires(input);
	}

	@Override
	public List<GPDataNode<GPXYSeries>> convertOutput(List<GPXYSeries> out) {
		return ScriptSeriesFunctions.convertToNodes(out);
	}

}
