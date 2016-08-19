package edu.wehi.celcalc.cohort.scriptables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;
import edu.wehi.graphplot.plot.NameableCollection;

public interface MeasurementIterHelperInterface<OUT extends NameableCollection>
{

	public default List<OUT> scriptxy(List<Measurement> measurements)
	{
		Set<String> allTreatments = MeasurementQuery.allTreatments(measurements);
		Set<CellType> allCellTypes = MeasurementQuery.allCellTypes(measurements);
		Set<Integer> allDivisions = MeasurementQuery.allDivisions(measurements);
	
		List<OUT> results = new ArrayList<>();
		for (CellType type : allCellTypes)
		{
			for (String treatment : allTreatments)
			{
				List<Measurement> selectedMeasurements = new ArrayList<>(new MeasurementQuery(measurements).withType(type).withTreatment(treatment).measurements);
				OUT result = scriptxyIter(selectedMeasurements, type, treatment, allCellTypes, allTreatments, allDivisions);
				if (result.size() != 0)
				{
					String resultName = result.getName();
					if (!(resultName.contains(type.toString()) && resultName.contains(treatment)))
					{
						String newName = result.getName();
						newName = type + "/" + treatment + "/" + newName;
						result.setName(newName);
					}
					results.add(result);
				}
			}
		}
		return results;
	}
	
	public OUT scriptxyIter(List<Measurement> measurements, CellType type, String treatment, Set<CellType> allTypes, Set<String> allTreatments, Set<Integer> allDivisions);

	
}
