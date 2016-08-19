package edu.wehi.graphplot.plot.series.scripts;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scriptseries.ScriptSingleInSignleOutSeries;

public class ScriptAverageTimeSeriesSingle extends ScriptSingleInSignleOutSeries {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScriptAverageTimeSeriesSingle(GPDataNode<GPXYSeries> input) {
		super(input);
	}
	
	public ScriptAverageTimeSeriesSingle() {
		this(null);
	}

	@Override
	public GPXYSeries scriptxy(GPXYSeries in) {
		return in.computeAverageSeries();
	}

}
