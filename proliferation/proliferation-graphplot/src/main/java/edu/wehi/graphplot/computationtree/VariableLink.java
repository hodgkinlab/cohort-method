package edu.wehi.graphplot.computationtree;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scriptables.Scriptable;

/**
 * 
 * Because nodes can have multiple parent scripts, and the tree does not support having multiple edges,
 * this class is a wrapper.
 *
 */
public class VariableLink
{
	public final GPDataNode<GPXYSeries> inputNode;
	public final GPDataNode<GPXYSeries> outputNode;
	public final Scriptable<?,?> scriptable;
	
	public VariableLink(GPDataNode<GPXYSeries> inputNode,
			GPDataNode<GPXYSeries> outputNode, Scriptable<?, ?> scriptable) {
		super();
		this.inputNode = inputNode;
		this.outputNode = outputNode;
		this.scriptable = scriptable;
	}
}
