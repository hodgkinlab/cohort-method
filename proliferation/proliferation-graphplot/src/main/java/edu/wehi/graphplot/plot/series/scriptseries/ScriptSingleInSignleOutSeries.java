package edu.wehi.graphplot.plot.series.scriptseries;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptSingleInSingleOut;

public abstract class ScriptSingleInSignleOutSeries extends ScriptSingleInSingleOut

implements ScriptTypeDifference<GPDataNode<GPXYSeries>, GPDataNode<GPXYSeries>, GPXYSeries, GPXYSeries>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScriptSingleInSignleOutSeries(GPDataNode<GPXYSeries> input) {
		super(input);
	}

	@Override
	public GPXYSeries convertInput(GPDataNode<GPXYSeries> input) {
		return ScriptSeriesFunctions.convertToSingleSeires(input);
	}

	@Override
	public GPDataNode<GPXYSeries> convertOutput(GPXYSeries out) {
		return ScriptSeriesFunctions.convertToNode(out);
	}
}
