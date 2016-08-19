package edu.wehi.celcalc.cohort.scripts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterModel;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.series.ScriptBase;
import edu.wehi.graphplot.python.NameableMapAndSeries;

public class ScriptNameableMapAndSeries extends ScriptBase<
List<Measurement>, List<GPDataNode<NameableMapAndSeries>>>
{

	private static final long serialVersionUID = 1L;
	
	String code;
	
	String fileName="";
	
	File file = null;
	
	public ScriptNameableMapAndSeries(String code, String fileName)
	{
		this.code = code;
		this.fileName = fileName;
	}
	
	public ScriptNameableMapAndSeries(File file)
	{
		
			this.file = file;
			fileName = file.getName();

	}
	
	public ScriptNameableMapAndSeries(File file, String name)
	{
		this.file = file;
		this.fileName = name;
	}

	@Override
	public List<GPDataNode<NameableMapAndSeries>> script(List<Measurement> measurements)
	{
		
		ScripterModel interp =  new ScripterModel(measurements);
		try
		{
			List<NameableMapAndSeries> result = null;
			
			if (file != null)
			{
				result = interp.executeIterCodeMapAndSeries(FileUtils.readFileToString(file));
			}
			else if (code!= null)
			{
				result = interp.executeIterCodeMapAndSeries(code);
			}
			
			List<GPDataNode<NameableMapAndSeries>> nodes = new ArrayList<>();
			
			for (NameableMapAndSeries r : result)
			{
				nodes.add(new GPDataNode<NameableMapAndSeries>(null,r,r.getName()));
			}
			
			return nodes;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
