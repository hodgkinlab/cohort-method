package edu.wehi.celcalc.cohort.gui.analysis;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import edu.wehi.celcalc.cohort.gui.application.ApplicationData;
import edu.wehi.celcalc.cohort.res.TestFileData;


public class TestAnalysisModel
{
	
	public static int noTreatments = TestFileData.actual_treatments.length;
	
	static AnalysisModel createModel()
	{
		ApplicationData data = new ApplicationData();
		data.getMeasurementListWithNoDuplicates().addAll(TestFileData.mesurements);
		return new AnalysisModel(data);
	}
	
	public static void testAnalysis(Collection<Analysis> analysiss, Collection<String> scriptNames)
	{
		analysiss.forEach(a -> testAnalysis(a, scriptNames));
	}
	
	public static void testAnalysis(Analysis analysis, Collection<String> scriptNames)
	{
		
		// null errors
		// assertNotNull(analysis.getFilters());
		assertNotNull(analysis.getName());
		assertNotNull(analysis.getScripts());
		assertNotNull(analysis.getTitle());
		assertNotNull(analysis.getxAxis());
		assertNotNull(analysis.getyAxis());
		
		// logic errors
		assertNotEquals("", analysis.getName());
		assertNotEquals(0, analysis.getScripts().size());
		assertNotEquals("", analysis.getTitle());
		assertNotEquals("",analysis.getxAxis());
		assertNotEquals("",analysis.getyAxis());
		
		// script logical errors
		for (String script : analysis.getScripts())
		{
			assertTrue(scriptNames.contains(script));
		}
		
	}
	
	@Test
	public void testAnalysisAre0()
	{
		AnalysisModel model = createModel();
		assertEquals(0, model.analysisContainer.analysiss.size());
	}
	
	@Test
	public void testcreateCellVsDiv()
	{
		AnalysisModel model = createModel();
		model.createCellVsDiv();
		assertEquals(noTreatments,model.analysisContainer.analysiss.size());
		
		assertEquals(1,model.analysisContainer.analysiss.get(0).getScripts().size());
		testAnalysis(model.analysisContainer.analysiss, model.scriptsContainter.getScriptNames());
	}
	
	@Test
	public void testCreateCohort()
	{
		AnalysisModel model = createModel();
		model.createCohort();
		assertEquals(noTreatments,model.analysisContainer.analysiss.size());
		testAnalysis(model.analysisContainer.analysiss, model.scriptsContainter.getScriptNames());
	}
	
	@Test
	public void testCreateMeanDivision()
	{
		AnalysisModel model = createModel();
		model.createMeanDivision();
		assertEquals(1,model.analysisContainer.analysiss.size());
		testAnalysis(model.analysisContainer.analysiss, model.scriptsContainter.getScriptNames());
	}
	
	@Test
	public void testCreateCohortSumVsMeanDivision()
	{
		AnalysisModel model = createModel();
		model.createCohortSumVsMeanDivision();
		assertEquals(1,model.analysisContainer.analysiss.size());
		testAnalysis(model.analysisContainer.analysiss, model.scriptsContainter.getScriptNames());
	}
	
	@Test
	public void testCreatecreateDefaultSuite()
	{
		AnalysisModel model = createModel();
		int prevSize = model.analysisContainer.analysiss.size();
		assertEquals(0, prevSize);
		
		model.createCellVsDiv();
		assertTrue(prevSize < model.analysisContainer.analysiss.size());
		prevSize = model.analysisContainer.analysiss.size();
		
		model.createCohort();
		assertTrue(prevSize < model.analysisContainer.analysiss.size());
		prevSize = model.analysisContainer.analysiss.size();
		
		model.createMeanDivision();
		assertTrue(prevSize < model.analysisContainer.analysiss.size());
		prevSize = model.analysisContainer.analysiss.size();
		
		model.createCohortSumVsMeanDivision();
		assertTrue(prevSize < model.analysisContainer.analysiss.size());
		prevSize = model.analysisContainer.analysiss.size();
		
		testAnalysis(model.analysisContainer.analysiss, model.scriptsContainter.getScriptNames());
		
	}

}
