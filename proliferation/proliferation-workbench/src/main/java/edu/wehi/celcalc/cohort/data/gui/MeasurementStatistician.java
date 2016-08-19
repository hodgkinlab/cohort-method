package edu.wehi.celcalc.cohort.data.gui;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.wehi.celcalc.cohort.data.Measurement;

public class MeasurementStatistician implements Serializable 
{

	private static final long serialVersionUID = 1L;

	public static MeasurementStatistics stats(Collection<? extends Measurement> measurements)
	{
		Set<String> treatments = new HashSet<>();
		
		int noMeasurements = 0;
		Double lowestCellCount = null;
		Double highestCellCount = null;
		Double lowestTime = null;
		Double highestTime = null;
		Integer lowestDivision = null;
		Integer highestDivision = null;
		
		for (Measurement m : measurements)
		{
			
			noMeasurements++;
			if (lowestCellCount == null)
			{
				lowestCellCount = m.getCells();
			}
			if (highestCellCount == null)
			{
				highestCellCount = m.getCells();
			}
			if (lowestTime == null)
			{
				lowestTime = m.getTime();
			}
			if (highestTime == null)
			{
				highestTime = m.getTime();
			}
			if (lowestDivision == null)
			{
				lowestDivision = m.getDiv();
			}
			if (highestDivision == null)
			{
				highestDivision = m.getDiv();
			}
			
			treatments.add(m.getTreatment());
			
			lowestCellCount = 	Math.min(lowestCellCount, 	m.getCells());
			highestCellCount = 	Math.max(highestCellCount, 	m.getCells());
			lowestTime = 		Math.min(lowestTime, 		m.getTime());
			highestTime = 		Math.max(highestTime, 		m.getTime());
			lowestDivision =    Math.min(lowestDivision, 	m.getDiv());
			highestDivision = 	Math.max(highestDivision, 	m.getDiv());
			
		}
		
		int noTreatments = treatments.size();
		
		return new MeasurementStatistics(noMeasurements, noTreatments, lowestCellCount, highestCellCount, lowestTime, highestTime, lowestDivision, highestDivision);
	}
	
	public static Set<String> treatments(List<Measurement> measurements)
	{
		return measurements.stream().map(m -> m.getTreatment()).collect(Collectors.toSet());
	}
	
	public static Set<Double> times(List<Measurement> measurements)
	{
		return measurements.stream().map(m -> m.getTime()).collect(Collectors.toSet());
	}

	public static Set<Integer> divs(List<Measurement> measurements)
	{
		return measurements.stream().map(m -> m.getDiv()).collect(Collectors.toSet());
	}
	
}
