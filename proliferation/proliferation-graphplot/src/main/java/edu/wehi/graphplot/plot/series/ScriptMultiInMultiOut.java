package edu.wehi.graphplot.plot.series;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scriptables.ScriptableMultiInMultiOut;

public abstract class ScriptMultiInMultiOut extends ScriptBase<List<GPDataNode<GPXYSeries>>, List<GPDataNode<GPXYSeries>>> implements ScriptableMultiInMultiOut
{
	private static final long serialVersionUID = 1L;
	
	public ScriptMultiInMultiOut(List<GPDataNode<GPXYSeries>> input)
	{
		super(input);
	}
	
	public ScriptMultiInMultiOut()
	{
		super();
	}
	
	public List<GPXYSeries> scriptData(List<GPXYSeries> series)
	{
		List<GPDataNode<GPXYSeries>> nodes = new ArrayList<>();
		
		series.stream().forEach(s -> nodes.add(new GPDataNode<>(s)));
		
		List<GPDataNode<GPXYSeries>> result = this.script(nodes);
		
		List<GPXYSeries> resultSeries = result.stream().map(m -> m.data).collect(Collectors.toList());
		
		return resultSeries;
	}


}