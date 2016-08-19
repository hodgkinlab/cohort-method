package edu.wehi.graphplot.plot.series;

import java.util.List;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scriptables.ScriptableMultiInSingleOut;

public abstract class ScriptMultiInSingleOut extends ScriptBase<List<GPDataNode<GPXYSeries>>, GPDataNode<GPXYSeries>> implements ScriptableMultiInSingleOut {

	
	private static final long serialVersionUID = 1L;

	public ScriptMultiInSingleOut() {
		super();
	}
	
	public ScriptMultiInSingleOut(List<GPDataNode<GPXYSeries>> input) {
		super(input);
	}
}
