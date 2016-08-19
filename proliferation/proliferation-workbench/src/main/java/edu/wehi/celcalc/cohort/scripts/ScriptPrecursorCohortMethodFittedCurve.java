package edu.wehi.celcalc.cohort.scripts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.legacy.LegacyTools;
import edu.wehi.celcalc.cohort.old.CohortGUI;
import edu.wehi.celcalc.cohort.old.CohortXLSReader;
import edu.wehi.celcalc.cohort.scriptables.MultiInMultiOutBaseIterTimeIter;
import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPCoordinateWithNameImp;
import edu.wehi.graphplot.plot.GPXYSeries;

public class ScriptPrecursorCohortMethodFittedCurve extends MultiInMultiOutBaseIterTimeIter
{

	private static final long serialVersionUID = 1L;
	
	Map<Double, Map<Double, List<List<Double>>>> liveCellData;	
	Map<Double, Map<Double, List<Double>>> meanLiveData;
	
	@Override
	public List<GPXYSeries> scriptxy(List<Measurement> measurements)
	{
		liveCellData = LegacyTools.legacyFormat(measurements);	
		meanLiveData = CohortXLSReader.getMeans(liveCellData);
		return super.scriptxy(measurements);
	}
	
	@Override
	public GPXYSeries scriptxyIter(
			List<Measurement> measurements,
			CellType type,
			String treatment,
			Double time,
			Set<CellType> allTypes,
			Set<String> allTreatments,
			List<Double> allTimes)
	{
		@SuppressWarnings("deprecation")
		double[] result = CohortGUI.getFittedPlots(meanLiveData, liveCellData, Double.parseDouble(treatment), time);
		
		List<GPCoordinateWithName> coords = new ArrayList<>();
		for (int i = 0; i< result.length; i++)
		{
			GPCoordinateWithNameImp coordWithName = new GPCoordinateWithNameImp(i, result[i], 0.0, "");
			coords.add(coordWithName);
		}
		return new GPXYSeries("", coords);
	}
	
	@Override
	public String getName()
	{
		return "builtin/model/"+"Precursor Cohort Method";
	}





}
