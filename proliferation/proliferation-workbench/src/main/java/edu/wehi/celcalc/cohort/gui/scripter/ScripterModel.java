package edu.wehi.celcalc.cohort.gui.scripter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.scriptables.SeriesAndTable;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.python.NameableMap;
import edu.wehi.graphplot.python.NameableMapAndSeries;

public class ScripterModel implements Serializable
{ 

	private static final long serialVersionUID = 1L;

	ScripterContainer scripterContainer = new ScripterContainer();

	CellCalcScriptEngine engine;
	
	public ScripterModel(List<Measurement> measurements)
	{
		engine = new CellCalcScriptEngine(measurements);
	}
	
	public ScripterModel()
	{
		this(new ArrayList<>());
	}

	public ScripterModel(ScripterContainer scripterContainer)
	{
		this();
		this.scripterContainer = scripterContainer;
	}

	public CellCalcScriptEngine getEngine()
	{
		return engine;
	}

	public void executeCode(String text)
	{
		engine.exec(text);
	}

	public void kill()
	{
		this.engine = null;
	}

	public List<GPXYSeries> executeIterCode(String currentCode)
	{
		return engine.execForEachTreatment(currentCode);
	}
	
	public List<NameableMap> executeIterCodeDic(String currentCode)
	{
		return engine.execForEachTreatmentMap(currentCode);
	}

	public void updateFromDir(String workspaceDir)
	{
		scripterContainer.updateFromDir(workspaceDir);
	}

	public List<SeriesAndTable> executeIterCodeTableAndDic(String code)
	{
		return engine.execForEachTreatmentMapAndCoords(code);
	}

	public List<NameableMapAndSeries> executeIterCodeMapAndSeries(String code)
	{
		return engine.execForEachTreatmentMapAndCoordsMapAndSeries(code);
	}
	
}
