package edu.wehi.celcalc.cohort.scriptables;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterModel;
import edu.wehi.graphplot.python.NameableMap;

public class MultiInMultiOutDictPythonScriptEngine extends MultiInMeasurementParamterScriptBase
{

	private static final long serialVersionUID = 1L;
	
	final String code;
	
	String fileName="";
	
	public MultiInMultiOutDictPythonScriptEngine(String code, String fileName)
	{
		this.code = code;
		this.fileName = fileName;
	}
	
	public MultiInMultiOutDictPythonScriptEngine(String code)
	{
		this.code = code;
	}
	
	public MultiInMultiOutDictPythonScriptEngine(File file)
	{
		try
		{
			this.code = FileUtils.readFileToString(file);
			fileName = file.getName();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("File contains bad code!");
		}
	}
	
	@Override
	public List<NameableMap> scriptxy(List<Measurement> measurements)
	{
		ScripterModel interp =  new ScripterModel(measurements);
		try
		{
			List<NameableMap> result = interp.executeIterCodeDic(code);
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getName()
	{
		return fileName;
	}

	
}
