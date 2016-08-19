package edu.wehi.celcalc.cohort.scriptables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;
import edu.wehi.graphplot.plot.GPXYSeries;

public abstract class MultiInMultiOutBaseIterTimeIter extends MultiMeasurementsInMultiOutBase
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public List<GPXYSeries> scriptxy(List<Measurement> measurements)
	{
		
		if (measurements.size() == 0)
		{
			return new ArrayList<GPXYSeries>();
		}
		
		Set<String> allTreatments = MeasurementQuery.allTreatments(measurements);
		Set<CellType> allCellTypes = MeasurementQuery.allCellTypes(measurements);
		List<Double> allTimes = MeasurementQuery.allTimes(measurements);
	
		List<GPXYSeries> results = new ArrayList<>();
		for (CellType type : allCellTypes)
		{
			for (String treatment : allTreatments)
			{
				for (Double time : allTimes)
				{
					List<Measurement> selectedMeasurements = new ArrayList<>(new MeasurementQuery(measurements).withType(type).withTreatment(treatment).withTime(time).measurements);
					if (selectedMeasurements.size() == 0)
					{
						continue;
					}
					GPXYSeries result = null;
					try
					{
						result = scriptxyIter(selectedMeasurements, type, treatment, time, allCellTypes, allTreatments, allTimes);
					}
					catch (Exception e)
					{
						System.err.println("Iteration: "+ type + "/" + treatment + "/" + time + ":\t Failed");
						continue;
					}
					if (result.size() != 0)
					{
						String resultName = result.getName();
						if (!(resultName.contains(type.toString()) && resultName.contains(treatment) && resultName.contains(time.toString())))
						{
							String newName = result.getName();
							newName = type + "/" + treatment + "/" + time + (newName.equals("")? "":newName);
							result.setName(newName);
						}
						results.add(result);
					}
				}
			}
		}
		return results;
	}
	
	public abstract GPXYSeries scriptxyIter(List<Measurement> measurements, CellType type, String treatment, Double time, Set<CellType> allTypes, Set<String> allTreatments, List<Double> allTimes);

}
