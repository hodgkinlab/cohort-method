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

public class ScriptCohortByDivision extends MultiInMultiOutBaseIterTimeIter
{
	private static final long serialVersionUID = 1L;
	
	public static final String sName = "CohortNumber";

	@Override
	public GPXYSeries scriptxyIter(List<Measurement> measurements,	CellType type, String treatment, Double time,	Set<CellType> allTypes, Set<String> allTreatments,	List<Double> allTimes)
	{
		String name = type + "/" + treatment + "/" + time;
		GPXYSeries series = convertToCohortByGenerationSeries(name, measurements);
		return series.computeAverageSeries();
	}

	private GPXYSeries convertToCohortByGenerationSeries(String name, Collection<Measurement> measurementsInSeries)
	{
		List<GPCoordinateWithName> newCoords = measurementsInSeries.stream().map(m -> m.cellByCohort()).collect(Collectors.toList());
		return new GPXYSeries(name, null, newCoords);
	}

	@Override
	public String getName()
	{
		return "builtin/"+sName;
	}
	
	@Override
	public String getDoc()
	{
		return "\\text{Cohort Number} \\\\ c(k,t) = \\frac{x(k,t)}{2^k}";
	}
	
}
