package edu.wehi.celcalc.cohort.gui.analyizer;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import edu.wehi.celcalc.cohort.gui.analysis.AnalysisModel;
import edu.wehi.celcalc.cohort.gui.application.ApplicationData;
import edu.wehi.celcalc.cohort.res.TestFileData;

public class TestAnalyizer
{

	ApplicationData data = new ApplicationData();
	
	AnalizerModel model = new AnalizerModel(data);

	AnalysisModel analysisModel = new AnalysisModel(data);
	
	@Before
	public void init()
	{
		data.getMeasurementListWithNoDuplicates().addAll(TestFileData.mesurements);
	}
	
	@Test
	public void testCreateCohort()
	{
		analysisModel.createCohort();
		model.createAnalizerView(new JFrame());
	}
	
	@Test
	public void testCreateCellVsDiv()
	{
		analysisModel.createCellVsDiv();
		model.createAnalizerView(new JFrame());
	}
	
	@Test
	public void testMeanDivision()
	{
		analysisModel.createMeanDivision();
		model.createAnalizerView(new JFrame());
	}
	
	@Test
	public void testCohortSumVsMeanDivision()
	{
		analysisModel.createCohortSumVsMeanDivision();
		model.createAnalizerView(new JFrame());
	}

}
