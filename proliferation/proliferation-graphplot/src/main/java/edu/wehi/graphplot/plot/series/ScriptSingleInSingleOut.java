package edu.wehi.graphplot.plot.series;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scriptables.ScriptableSingleInSingleOut;

public abstract class ScriptSingleInSingleOut extends ScriptBase<GPDataNode<GPXYSeries>, GPDataNode<GPXYSeries>> implements ScriptableSingleInSingleOut {

	
	private static final long serialVersionUID = 1L;

	public ScriptSingleInSingleOut(GPDataNode<GPXYSeries> input)
	{
		super(input);
	}

	public ScriptSingleInSingleOut()
	{
		super();
	}


}
