package edu.wehi.celcalc.cohort.scripts;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.scriptables.MultiInMultiOutBaseIterTimeIter;
import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPXYSeries;

public class ScriptCountByDivision extends MultiInMultiOutBaseIterTimeIter
{
	private static final long serialVersionUID = 1L;


	@Override
	public GPXYSeries scriptxyIter(List<Measurement> measurements,	CellType type, String treatment, Double time,	Set<CellType> allTypes, Set<String> allTreatments,	List<Double> allTimes)
	{
		String name = type + "/" + treatment + "/" + time;
		GPXYSeries series = convertToCountByGenerationSeries(name, measurements);
		return series.computeAverageSeries();
	}

	private GPXYSeries convertToCountByGenerationSeries(String name, Collection<Measurement> measurementsInSeries)
	{
		List<GPCoordinateWithName> newCoords = measurementsInSeries.stream().map(m -> m.cellByGeneration()).collect(Collectors.toList());
		return new GPXYSeries(name, null, newCoords);
	}
	
	@Override
	public String getName()
	{
		return "builtin/"+"PopulationByDivision";
	}
	
	@Override
	public String getDoc()
	{
		return "\\text{Count by Division and Time} \\\\ x(k,t) \\\\ \\text{population of division} \\hspace{0.2 cm} k \\hspace{0.2 cm} \\text{at time } t";
	}

}
