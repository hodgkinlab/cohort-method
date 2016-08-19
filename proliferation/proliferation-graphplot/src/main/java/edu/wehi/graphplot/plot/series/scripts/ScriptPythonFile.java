package edu.wehi.graphplot.plot.series.scripts;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.python.util.PythonInterpreter;

import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptMultiInMultiOut;

public class ScriptPythonFile extends ScriptMultiInMultiOut
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String code;
	
	public ScriptPythonFile(List<GPDataNode<GPXYSeries>> inputs, File file)
	{
		super(inputs);
		try
		{
			this.code = FileUtils.readFileToString(file);
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("File contains bad code!");
		}
	}
	
	public ScriptPythonFile(List<GPDataNode<GPXYSeries>> inputs, String code)
	{
		super(inputs);
		this.code = code;
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GPDataNode<GPXYSeries>> script(List<GPDataNode<GPXYSeries>> data)
	{
		PythonInterpreter interp = new PythonInterpreter();
		
		for (GPDataNode<GPXYSeries> datanode : input)
		{
			interp.set(datanode.getName(), datanode.getData());
		}
		
		List<GPDataNode<GPXYSeries>> nodes = null;
		nodes = (List<GPDataNode<GPXYSeries>>) interp.eval(code).__tojava__(List.class);
		return nodes;
	}

}