package edu.wehi.celcalc.cohort.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;

public class LegacyTools
{
	
	public static Map<Double, Map<Double, List<List<Double>>>> legacyFormat(List<Measurement> measurements)
	{
		Map<Double, Map<Double, List<List<Double>>>> cellData = new HashMap<>();
		for (String treatment : MeasurementQuery.allTreatments(measurements))
		{
			Map<Double, List<List<Double>>> byTime = new HashMap<>();
			for (Double time : MeasurementQuery.allTimes(measurements))
			{
				List<Integer> AllDivisions = new ArrayList<>(MeasurementQuery.allDivisions(measurements));
				Collections.sort(AllDivisions);
				
				List<List<Double>> repeatDiv = new ArrayList<>();
				
				
				for (Integer division : AllDivisions)
				{
					List<Measurement> messs = new ArrayList<>(new MeasurementQuery(measurements).withTreatment(treatment).withTime(time).withDivision(division).measurements);
					List<Double> messLis = messs.stream().map(m -> m.getCells()).collect(Collectors.toList());
					for (int i=0; i < messLis.size(); i++)
					{
						try
						{
							repeatDiv.get(i).add(messLis.get(i));
						}
						catch(Exception e)
						{
							repeatDiv.add(new ArrayList<>());
							repeatDiv.get(i).add(messLis.get(i));
							continue;
						}
					}
					
				}
				if (repeatDiv.size()!= 0)
				{
					byTime.put(time, repeatDiv);
				}
			}
			cellData.put(Double.parseDouble(treatment), byTime);	
		}
		return cellData;	
	}

}
