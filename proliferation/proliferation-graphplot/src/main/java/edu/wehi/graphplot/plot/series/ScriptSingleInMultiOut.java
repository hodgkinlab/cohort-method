package edu.wehi.graphplot.plot.series;

import java.util.List;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scriptables.ScriptableSingleInMultiOut;

public abstract class ScriptSingleInMultiOut extends ScriptBase<GPDataNode<GPXYSeries>, List<GPDataNode<GPXYSeries>>> implements ScriptableSingleInMultiOut  {

	
	private static final long serialVersionUID = 1L;

	public ScriptSingleInMultiOut(GPDataNode<GPXYSeries> input) {
		super(input);
	}

	public ScriptSingleInMultiOut()
	{
		super();
	}

}
