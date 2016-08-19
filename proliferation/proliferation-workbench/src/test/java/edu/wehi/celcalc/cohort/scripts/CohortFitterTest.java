package edu.wehi.celcalc.cohort.scripts;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;

import edu.wehi.celcalc.cohort.data.CellCountXCELReader;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.legacy.LegacyTools;
import edu.wehi.celcalc.cohort.old.CohortGUI;
import edu.wehi.celcalc.cohort.old.CohortProcessor;
import edu.wehi.celcalc.cohort.old.CohortXLSReader;
import edu.wehi.celcalc.cohort.test.FILES;
import edu.wehi.celcalc.cohort.test.ResourceLoaderForTests;
import edu.wehi.celcalc.cohort.util.Utilities;

public class CohortFitterTest
{
	static final double conc = 5000.0;
	static final double currConc = conc;
	static final double time = 30.5;
	static final String path = ResourceLoaderForTests.getResource(FILES.TESTCOHORTINTEGRATION);
	
	static List<Measurement> measurements = null;
	
	static
	{
		try
		{
			measurements = Utilities.toList(CellCountXCELReader.processBookToMap(path));
		}
		catch (Exception e)
		{
			fail("Could not parse test file");
			assertNotNull(measurements);
		}
	}
	
	
	@Test
	public void testCohortFitterWorking()
	{
		
		Map<Double, Map<Double, List<List<Double>>>> liveCellDataLegacy = LegacyTools.legacyFormat(measurements);	
		Map<Double, Map<Double, List<Double>>> meanLiveDataLegacy = CohortXLSReader.getMeans(liveCellDataLegacy);
		@SuppressWarnings("deprecation")
		double[] optimArrayLegacy = CohortGUI.getFittedPlots(meanLiveDataLegacy, liveCellDataLegacy, conc, time);
		
		// Actual !!!!!

		CohortXLSReader reader = new CohortXLSReader();
		reader.parseFile(path);
		Map<Double, Map<Double, List<Double>>> meanLiveData = reader.getLiveMeans();
		
		
		assertEquals(meanLiveData.size(), meanLiveDataLegacy.size());
		
		CohortProcessor cohortProcessor = new CohortProcessor(reader);
		Map<Double, Map<Double, double[]>> params = cohortProcessor.fitter();
		List<List<Double>> cohortVals = cohortProcessor
				.getCohortNumbers()
				.get(currConc)
				.get(time);
		
		NormalDistribution dist = new NormalDistribution(
				params.get(currConc).get(time)[0],
				params.get(currConc).get(time)[1]+1e-5);

		double[] optimArray = new double[cohortVals.get(0).size()];
		for (int i=0; i<cohortVals.get(0).size()-1; i++)
		{
			optimArray[i] = params.get(currConc).get(time)[2]*(dist.cumulativeProbability(i+0.5) - dist.cumulativeProbability(i-0.5));
		}
		
		System.out.println();
		assertArrayEquals(optimArray, optimArrayLegacy, 0.01);
	}
	
	
}
