package edu.wehi.celcalc.cohort.plotting.properties;

import java.util.List;

import javax.swing.JPanel;

import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPPlotterFuncs;
import edu.wehi.graphplot.plot.GPXYSeries;

public class AnalysisPlotComponent extends JPanel
{

	private static final long serialVersionUID = 1L;

	public AnalysisPlotComponent(List<GPDataNode<GPXYSeries>> result)
	{
		add(GPPlotterFuncs.plotNodes(result, "Title", "x", "y", GPChartType.LINE));
	}

	public AnalysisPlotComponent(GPDataNode<GPXYSeries> result)
	{
		add(GPPlotterFuncs.plotNodes(result, "Title", "x", "y", GPChartType.LINE));
	}

}
