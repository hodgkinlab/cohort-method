package edu.wehi.celcalc.cohort;

import edu.wehi.celcalc.cohort.scriptables.MultiPurposePythonScriptBase;

public enum PythonScript
{

	COHORTSUM(			PackageScripterUtilities.cohortSumExamplePY),
	COHORTSUMVSMEANDIV(	PackageScripterUtilities.cohortSumVsMeanDivisionExamplePY),
	MEANDIV(			PackageScripterUtilities.meanDivisionExamplePY),
	POPULATION(			PackageScripterUtilities.totalpopulationPY);	
	
	public final String path;
	PythonScript(String path)
	{
		this.path = path;
	}
	
	public MultiPurposePythonScriptBase getScript()
	{
		return new MultiPurposePythonScriptBase(PackageScripterUtilities.getAsString(path), path);
	}
}
