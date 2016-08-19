package edu.wehi.graphplot.plot.series.scripts;

import java.util.ArrayList;
import java.util.List;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptMultiInMultiOut;
import edu.wehi.graphplot.plot.series.scriptables.ScriptableMultiInMultiOut;

public class ScriptMeanByTags extends ScriptMultiInMultiOut implements ScriptableMultiInMultiOut
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String[] tags; 
	public ScriptMeanByTags(String[] tags)
	{
		this.tags = tags;
	}
	
	public ScriptMeanByTags(List<String> tags)
	{
		this(tags.toArray(new String[tags.size()]));
	}
	
	@Override
	public List<GPDataNode<GPXYSeries>> script(List<GPDataNode<GPXYSeries>> input)
	{
		List<GPDataNode<GPXYSeries>> output = new ArrayList<>();
		
		for (String t : tags)
		{
			try 
			{
				List<GPDataNode<GPXYSeries>> filtered = 	new ScriptFilterTimeSeriesByTag(t).script(input);
				GPDataNode<GPXYSeries> sum = 				new ScriptAddTimeSeries(t).script(filtered);
				GPDataNode<GPXYSeries> average =			new ScriptAverageTimeSeriesSingle().script(sum);
				output.add(average);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}			
		}
		return output;
	}

}
