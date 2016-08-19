package edu.wehi.celcalc.cohort.old;

import java.util.List;
import java.util.Map;

public class CohortProcessorInputData
{
	
	private final  Map<Double, Map<Double, List<Double>>> startParams;
	private final  Map<Double, Map<Double, List<Double>>> liveMeans;
	private final  Map<Double, Map<Double, List<List<Double>>>> liveCellData;

	
	public CohortProcessorInputData(
			Map<Double, Map<Double, List<Double>>> startParams,
			Map<Double, Map<Double, List<Double>>> liveMeans,
			Map<Double, Map<Double, List<List<Double>>>> liveCellData)
	{

		this.liveMeans = liveMeans;
		this.startParams = startParams;
		this.liveCellData = liveCellData;
	}

	public Map<Double, Map<Double, List<Double>>> getStartParams()
	{
		return startParams;
	}

	public Map<Double, Map<Double, List<Double>>> getLiveMeans()
	{
		return liveMeans;
	}

	public Map<Double, Map<Double, List<List<Double>>>> getLiveCellData()
	{
		return liveCellData;
	}
	
}
