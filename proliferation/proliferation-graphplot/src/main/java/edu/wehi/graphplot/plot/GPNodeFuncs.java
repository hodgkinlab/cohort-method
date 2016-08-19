package edu.wehi.graphplot.plot;

import java.util.ArrayList;
import java.util.List;

public class GPNodeFuncs {
	
	
	public static List<GPDataNode<GPXYSeries>> toListDataNode(List<GPXYSeries> seriess)
	{
		List<GPDataNode<GPXYSeries>> dataNodes = new ArrayList<>();
		
		for (GPXYSeries xy : seriess)
		{
			dataNodes.add(new GPDataNode<>(null, xy, xy.name));
		}
		
		return dataNodes;
	}
	
	public static List<String> getAllSeriesDataNodeNames(List<GPDataNode<GPXYSeries>> dataNodes)
	{
		List<String> names = new ArrayList<>();
		for (GPDataNode<GPXYSeries> dataNode : dataNodes)
		{
			names.add(dataNode.getData().name);
		}
		return names;
	}

}
