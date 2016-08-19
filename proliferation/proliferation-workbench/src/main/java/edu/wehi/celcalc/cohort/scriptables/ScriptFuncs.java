package edu.wehi.celcalc.cohort.scriptables;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;

public class ScriptFuncs {

	public static String getTreatment(GPDataNode<GPXYSeries> node)
	{
		return getTreatment(node.getData());
	}
	
	public static String getTreatment(GPXYSeries series)
	{
		return series.name.split("/")[0];
	}
	
	
}
