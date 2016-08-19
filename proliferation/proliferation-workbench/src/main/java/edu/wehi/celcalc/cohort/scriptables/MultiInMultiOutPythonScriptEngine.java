package edu.wehi.celcalc.cohort.scriptables;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterModel;
import edu.wehi.graphplot.plot.GPXYSeries;

public class MultiInMultiOutPythonScriptEngine extends MultiMeasurementsInMultiOutBase
{
	private static final long serialVersionUID = 1L;
	
	String code;
	
	String fileName="";
	
	File file = null;
	
	public MultiInMultiOutPythonScriptEngine(String code, String fileName)
	{
		this.code = code;
		this.fileName = fileName;
	}
	
	public MultiInMultiOutPythonScriptEngine(File file)
	{
		
			this.file = file;
			fileName = file.getName();

	}
	
	public MultiInMultiOutPythonScriptEngine(File file, String name)
	{
		this.file = file;
		this.fileName = name;
	}
	
	@Override
	public List<GPXYSeries> scriptxy(List<Measurement> measurements)
	{
		
		
		ScripterModel interp =  new ScripterModel(measurements);
		try
		{
			List<GPXYSeries> result = null;
			
			if (file != null)
			{
				result = interp.executeIterCode(FileUtils.readFileToString(file));
			}
			else if (code!= null)
			{
				result = interp.executeIterCode(code);
			}
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public MultiInMultiOutPythonScriptEngine(String code)
	{
		this.code = code;
	}

	
	@Override
	public String getName()
	{
		return fileName;
	}
	

}
