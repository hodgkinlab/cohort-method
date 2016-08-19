package edu.wehi.graphplot.plot.series.scripts;

import java.util.List;
import java.util.stream.Collectors;

import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scriptseries.ScriptMultiInMultiOutSeries;

public class ScriptAverageTimeSeriesMulti extends ScriptMultiInMultiOutSeries {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<GPXYSeries> scriptxy(List<GPXYSeries> in) {
		return in.stream().map(s -> s.computeAverageSeries()).collect(Collectors.toList());
	}

}
