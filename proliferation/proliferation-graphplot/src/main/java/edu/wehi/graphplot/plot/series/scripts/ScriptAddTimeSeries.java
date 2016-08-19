package edu.wehi.graphplot.plot.series.scripts;

import java.util.List;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptMultiInSingleOut;

public class ScriptAddTimeSeries extends ScriptMultiInSingleOut
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String newName;
	
	public ScriptAddTimeSeries(List<GPDataNode<GPXYSeries>> inputs) {
		super(inputs);
		newName = "";
	}
	
	public ScriptAddTimeSeries(String newName) {
		super();
		this.newName = newName;
	}

	@Override
	public GPDataNode<GPXYSeries> script(List<GPDataNode<GPXYSeries>> input) {
		
		if (input.size() == 0 )
		{
			throw new RuntimeException("No series were inputed to be added together");
		}

		if (input.size() == 1) {
			System.err.println("Adding less than 2 Series Together");
			System.err.println("Returning input[0]");
			return input.get(0);
		}

		GPXYSeries series1 = input.get(0).getData();

		for (int i = 1; i < input.size(); i++) {
			series1 = series1.add(newName, input.get(i).getData());
		}

		return new GPDataNode<GPXYSeries>(this, series1, newName);

	}

	@Override
	public String getDoc()
	{
		return "Addes 2 time series together: $$ (x1,y1)+(x1,y2)=(x1,y1+y2) $$";
	}

}
