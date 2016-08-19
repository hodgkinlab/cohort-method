package edu.wehi.celcalc.cohort;

import java.net.URL;

import edu.wehi.util.FileUtil;

public class PackageScripterUtilities
{
	public static final String cohortPY = "cohort.py";
	
	public static final String scriptDir 	= "python";
	public static final String templatePy 	= scriptDir + "/" + "template.console.py";
	public static final String treatmentPY 	= scriptDir + "/" +"treatment.py";
	
	public static final String scriptExt						= "py";
	public static final String builtInDir 						= "builtin/python";
	public static final String cohortSumExamplePY 				= builtInDir + "/" + "cohortSum."				+scriptExt;
	public static final String cohortSumVsMeanDivisionExamplePY = builtInDir + "/" + "cohortSumVsMeanDivision."	+scriptExt;
	public static final String meanDivisionExamplePY 			= builtInDir + "/" + "meanDivision."			+scriptExt;
	public static final String testDicScriptPY					= builtInDir + "/" + "testScript.dict."			+scriptExt;
	public static final String totalpopulationPY				= builtInDir + "/" + "totalpopulation."			+scriptExt;
	
	
	public static String getTemplate()
	{
		return  getAsString(templatePy);
	}
	
	public static String getTreatmentPy()
	{
		return getAsString(treatmentPY);
	}
	
	public static String getAsString(String py)
	{
		String result = FileUtil.convertResourceToString(PackageScripterUtilities.class.getResource(py));
		if (result == null)
		{
			throw new RuntimeException("Error could not find resource");
		}
		return result;
	}
	
	public URL getResource(String res)
	{
		return PackageScripterUtilities.class.getResource(res);
	}
	

}
