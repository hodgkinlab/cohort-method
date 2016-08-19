package edu.wehi.graphplot.plot.series.scripts;

import java.util.ArrayList;
import java.util.List;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptMultiInMultiOut;

public class ScriptFilterTimeSeriesByTag extends ScriptMultiInMultiOut
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String tag;
	
	public ScriptFilterTimeSeriesByTag(List<GPDataNode<GPXYSeries>> input, String tag)
	{
		super(input);
		this.tag = tag;
	}

	public ScriptFilterTimeSeriesByTag(String tag) {
		this(null, tag);
	}
	

	@Override
	public List<GPDataNode<GPXYSeries>> script(List<GPDataNode<GPXYSeries>> input)
	{
		List<GPDataNode<GPXYSeries>> list = new ArrayList<>();
		
		for (GPDataNode<GPXYSeries> d : input)
		{
			if (d.getData().name.contains(tag))
			{
				list.add(d);
			}
		}
		return list;
	}

}
