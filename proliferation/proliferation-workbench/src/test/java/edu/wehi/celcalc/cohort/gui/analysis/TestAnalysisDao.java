package edu.wehi.celcalc.cohort.gui.analysis;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wehi.celcalc.cohort.runner.Runner;
import edu.wehi.celcalc.cohort.runner.RunnerOptions;

public class TestAnalysisDao
{
	
	@Test
	public void testSaveAndLoadAnalysis() throws IOException
	{
		String a1 = "a1";
		Analysis analysis = new Analysis(a1);
		File temp = File.createTempFile("somefile", "");
		List<Analysis> list = new ArrayList<>();
		list.add(analysis);
		
		assertTrue(AnalysisDao.saveAnalysis(list, temp.getParent()));
		
		String absolutePath = temp.getParent() +"\\"+ analysis.getName() +".xml";
		Analysis analysis_reloaded = AnalysisDao.loadAnalysis(absolutePath);
		
		assertNotNull("failed to reload analysis", analysis_reloaded);
		assertTrue(analysis_reloaded.getName().equals(a1));
	}
	
	
	@Test
	public void testSaveAndLoadAnalysisWithRunner() throws IOException
	{
		String analysisName = "a1";
		String group = "group";
		boolean log = false;
		boolean isShowing = false;
		String title = "title";
		
		Analysis analysis = new Analysis(analysisName);
		analysis.setGroup(group);
		analysis.setLog(log);
		analysis.setShowing(isShowing);
		analysis.setTitle(title);
		
		
		int seed = 2;
		int runs = 3;
		int sampleSize = 4;
		boolean showAllResults = false;
		
		Runner runner = new Runner(seed, runs, sampleSize, showAllResults, false, RunnerOptions.ALL);
		analysis.setRunner(runner);
		
		File temp = File.createTempFile("somefile", "");
		List<Analysis> list = new ArrayList<>();
		list.add(analysis);
		
		// save succeeded
		assertTrue(AnalysisDao.saveAnalysis(list, temp.getParent()));
		
		// reload analysis
		String absolutePath = temp.getParent() +"\\"+ analysis.getName() +".xml";
		Analysis analysis_reloaded = AnalysisDao.loadAnalysis(absolutePath);
		
		assertNotNull("failed to reload analysis", analysis_reloaded);
		assertEquals(analysisName, analysis_reloaded.getName());
		assertEquals(group, analysis_reloaded.getGroup());
		assertEquals(log, analysis_reloaded.isLog());
		assertEquals(title, analysis.getTitle());
		
		Runner runner_reloaded = analysis_reloaded.getRunner();
		assertNotNull("failed to reload runner", runner_reloaded);
		assertEquals(seed, runner_reloaded.getSeed());
		assertEquals(runs, runner_reloaded.getRuns());
		assertEquals(sampleSize, runner_reloaded.getSampleSize());
		assertEquals(showAllResults, runner_reloaded.isShowAllResults());
	}

}
