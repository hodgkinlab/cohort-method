package edu.wehi.celcalc.cohort.old;

import java.util.List;
import java.util.Map;

public class CohortModel {

	public List<Double> parseFile(String file)
	{
		CohortXLSReader reader = new CohortXLSReader();
		reader.parseFile(file);
		Map<Double, Map<Double, List<Double>>> meanLiveData = reader.getLiveMeans();
		CohortProcessor cohortProcessor = new CohortProcessor(reader);
		cohortProcessor.setSettings(5,false);
		List<Double> concsList = reader.getConcentrationList();
		return concsList;
	}
	
	double[] optimArray(List<List<Double>> cohortVals, Map<Double, Map<Double, double[]>> params)
	{
		double[] optimArray = new double[cohortVals.get(0).size()];	
		return optimArray;
	}
	
}
