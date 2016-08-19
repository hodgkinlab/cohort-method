package edu.wehi.celcalc.cohort.scripts;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.legacy.LegacyTools;
import edu.wehi.celcalc.cohort.old.CohortGUI;
import edu.wehi.celcalc.cohort.old.CohortXLSReader;
import edu.wehi.celcalc.cohort.scriptables.MultiInMeasurementParamterScriptBaseIter;
import edu.wehi.graphplot.python.NameableMap;
import edu.wehi.graphplot.python.NameableMapImp;

public class ScriptPrecursorCohortMethodDivisionTimes extends MultiInMeasurementParamterScriptBaseIter
{

	private static final long serialVersionUID = 1L;

	Map<Double, Map<Double, List<List<Double>>>> liveCellData;	
	Map<Double, Map<Double, List<Double>>> meanLiveData;
	
	@Override
	public List<NameableMap> scriptxy(List<Measurement> measurements)
	{
		liveCellData = LegacyTools.legacyFormat(measurements);	
		meanLiveData = CohortXLSReader.getMeans(liveCellData);
		return super.scriptxy(measurements);
	}
	
	
	@Override
	public NameableMap scriptxyIter(List<Measurement> measurements, CellType type, String treatment, Set<CellType> allTypes, Set<String> allTreatments, Set<Integer> allDivisions)
	{
		double[] divTimes = CohortGUI.computeDivisionTimes(meanLiveData, liveCellData, Double.parseDouble(treatment));
		NameableMap map = new NameableMapImp("");
		
		for (int i=0; i<divTimes.length; i++)
		{
			map.put("div time: "+i, divTimes[i]);
		}
		return map;
	}
	
	@Override
	public String getName()
	{
		return "builtin/model/"+"Precursor Cohort Method Div Times";
	}
	
}
