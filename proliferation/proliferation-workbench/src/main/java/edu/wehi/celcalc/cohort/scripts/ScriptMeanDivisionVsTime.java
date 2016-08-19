package edu.wehi.celcalc.cohort.scripts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;
import edu.wehi.celcalc.cohort.scriptables.MultiInMultiOutBaseIter;
import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPCoordinateWithNameImp;
import edu.wehi.graphplot.plot.GPXYSeries;

@Deprecated
public class ScriptMeanDivisionVsTime extends MultiInMultiOutBaseIter
{
	private static final long serialVersionUID = 1L;

	@Override
	public GPXYSeries scriptxyIter(List<Measurement> measurements, CellType type,	String treatment, Set<CellType> allTypes, Set<String> allTreatments, Set<Integer> allDivisions)
	{
		List<Double> times = MeasurementQuery.allTimes(measurements);
		List<GPCoordinateWithName> coords = new ArrayList<>();
		for (Double t : times)
		{
			List<Measurement> mes_t = new MeasurementQuery(measurements)
				.withTreatment(treatment)
				.withTime(t)
				.withType(type)
					.toSeries();
			
			if (mes_t.size() == 0)
			{
				continue;
			}
			
			double sum = mes_t.stream().mapToDouble(m -> m.getCells()*m.getDiv()).sum() / mes_t.size();
			double cells = mes_t.stream().mapToDouble(m -> m.getCells()).sum() / mes_t.size();
			double avg = sum/cells;
			GPCoordinateWithNameImp coord = new GPCoordinateWithNameImp(t, avg, 0, ""); // TODO - need to calculate the std error mean
			coords.add(coord);
		}
		GPXYSeries series = new GPXYSeries(type + "/" + treatment, coords);
		return series;
	}
	
	@Override
	public String getName()
	{
		return "Deprecated: MeanDivisionVsTime";
	}
}
