package edu.wehi.celcalc.cohort;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wehi.celcalc.cohort.PythonScript;

public class PythonScriptTest
{

	
	@Test
	public void testGettingCodeCorrectly()
	{
		assertNotNull(PackageScripterUtilities.getAsString(PackageScripterUtilities.cohortSumExamplePY));
		assertNotNull(PackageScripterUtilities.getAsString(PackageScripterUtilities.cohortSumVsMeanDivisionExamplePY));
		assertNotNull(PackageScripterUtilities.getAsString(PackageScripterUtilities.meanDivisionExamplePY));
		assertNotNull(PackageScripterUtilities.getAsString(PackageScripterUtilities.testDicScriptPY));

	}
	
	
	@Test
	public void testAllScriptsNotNull()
	{
		for (PythonScript s : PythonScript.values())
		{
			assertNotNull("Script was null",	s.getScript());
			assertNotNull("Script was null",	s.getScript().getName());
			assertNotEquals("Script was null",	s.getScript().getName().equals(""));
		}
	}
	
}
