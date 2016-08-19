package edu.wehi.celcalc.cohort.gui.scripter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.wehi.celcalc.cohort.PythonScript;
import edu.wehi.celcalc.cohort.application.Workspacefiles;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.scriptables.MapAndSeriesScript;
import edu.wehi.celcalc.cohort.scriptables.MeasurementParamaterScriptable;
import edu.wehi.celcalc.cohort.scriptables.MultiMeasurementsInMultiOut;
import edu.wehi.celcalc.cohort.scriptables.MultiMeasurementsInSingleOut;
import edu.wehi.celcalc.cohort.scriptables.MultiPurposePythonScriptBase;
import edu.wehi.celcalc.cohort.scriptables.SeriesAndTable;
import edu.wehi.celcalc.cohort.scripts.ScriptCohortByDivision;
import edu.wehi.celcalc.cohort.scripts.ScriptCountByDivision;
import edu.wehi.celcalc.cohort.scripts.ScriptPrecursorCohortMethodDivisionTimes;
import edu.wehi.celcalc.cohort.scripts.ScriptPrecursorCohortMethodFittedCurve;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scriptables.Scriptable;
import edu.wehi.graphplot.plot.series.scriptables.ScriptableMultiInMultiOut;
import edu.wehi.graphplot.plot.series.scriptables.ScriptableMultiInSingleOut;
import edu.wehi.graphplot.plot.series.scriptables.ScriptableSingleInSingleOut;
import edu.wehi.graphplot.python.NameableMap;

public class ScripterContainer extends ArrayList<MapAndSeriesScript> implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	
	
	public ScripterContainer()
	{
		this(".");
	}
	
	@Override
	public boolean add(MapAndSeriesScript script)
	{
		if (stream().anyMatch(s -> s.getName().equals(script.getName())))
		{
			return false;
		}
		
		return super.add(script);
	}
	
	public void remove(String scriptName)
	{
		for (MapAndSeriesScript script : this)
		{
			if (script.getName().equals(scriptName))
			{
				remove(script);
				return;
			}
		}
	}
	
	public ScripterContainer(String workspaceDir)
	{
		super();
		
		// internal scripts
		addScript(new ScriptCohortByDivision());
		addScript(new ScriptCountByDivision());
		
		// legacy code
		addScript(new ScriptPrecursorCohortMethodDivisionTimes());
		addScript(new ScriptPrecursorCohortMethodFittedCurve());
		
		// Python scripts
		for (PythonScript s : PythonScript.values())
		{
			add(s.getScript());
		}

		Collections.sort(this);
		
	}
	
	public static List<GPDataNode<SeriesAndTable>> converter(
			List<Measurement> input, Scriptable<?, ?> script)
	{
		
		String name = input.get(0).getGroupKey();
		List<GPDataNode<SeriesAndTable>> seriesAndTable = new ArrayList<>();
		
		if (script instanceof ScriptableMultiInMultiOut)
		{
			ScriptableMultiInMultiOut smmio = (ScriptableMultiInMultiOut) script;
			List<GPDataNode<GPXYSeries>> nodes = new ArrayList<>();
			nodes.add(new GPDataNode<GPXYSeries>(name,new GPXYSeries(name,input)));
			List<GPDataNode<GPXYSeries>> output = smmio.script(nodes);
			
			for (GPDataNode<GPXYSeries> o : output)
			{
				GPXYSeries series = o.getData();
				seriesAndTable.add(new GPDataNode<SeriesAndTable>(new SeriesAndTable(name, series, null)));
			}
		}
		else if (script instanceof ScriptableMultiInSingleOut)
		{
			ScriptableMultiInSingleOut smso = (ScriptableMultiInSingleOut) script;
			List<GPDataNode<GPXYSeries>> nodes = new ArrayList<>();
			nodes.add(new GPDataNode<GPXYSeries>(new GPXYSeries(input)));
			GPDataNode<GPXYSeries> output = smso.script(nodes);
			seriesAndTable.add(new GPDataNode<SeriesAndTable>(new SeriesAndTable(output.getName(), output.getData(), null)));
		}
		else if (script instanceof ScriptableSingleInSingleOut)
		{
			ScriptableSingleInSingleOut ssso = (ScriptableSingleInSingleOut) script;
			GPDataNode<GPXYSeries> output = ssso.script(new GPDataNode<GPXYSeries>(new GPXYSeries(input)));
			seriesAndTable.add(new GPDataNode<SeriesAndTable>(new SeriesAndTable(output.getName(), output.getData(), null)));
		}
		else if (script instanceof ScriptableMultiInSingleOut)
		{
			ScriptableMultiInSingleOut smso = (ScriptableMultiInSingleOut) script;
			List<GPDataNode<GPXYSeries>> nodes = new ArrayList<>();
			nodes.add(new GPDataNode<GPXYSeries>(new GPXYSeries(input)));
			GPDataNode<GPXYSeries> output = smso.script(nodes);
			seriesAndTable.add(new GPDataNode<SeriesAndTable>(new SeriesAndTable(output.getName(), output.getData(), null)));
		}
		else if (script instanceof MultiMeasurementsInMultiOut)
		{
			MultiMeasurementsInMultiOut mmo = (MultiMeasurementsInMultiOut) script;
			List<GPDataNode<GPXYSeries>> output = mmo.script(input);
			for (GPDataNode<GPXYSeries> o : output)
			{
				GPXYSeries series = o.getData();
				seriesAndTable.add(new GPDataNode<SeriesAndTable>(new SeriesAndTable(name, series, null)));
			}
		}
		else if (script instanceof MultiMeasurementsInSingleOut)
		{
			MultiMeasurementsInSingleOut mms = (MultiMeasurementsInSingleOut) script;
			GPDataNode<GPXYSeries> output = mms.script(input);
			seriesAndTable.add(new GPDataNode<SeriesAndTable>(new SeriesAndTable(output.getName(), output.getData(), null)));
		}
		else if (script instanceof MeasurementParamaterScriptable)
		{
			MeasurementParamaterScriptable mapScript = (MeasurementParamaterScriptable) script;
			List<GPDataNode<NameableMap>> output = mapScript.script(input);
			for (GPDataNode<NameableMap> o : output)
			{
				NameableMap map = o.getData();
				seriesAndTable.add(new GPDataNode<SeriesAndTable>(new SeriesAndTable(name, null, map)));
			}
			
		}
		else
		{
			throw new RuntimeException("Cannot handle script type: "+script.getClass());
		}
		
		return seriesAndTable;
	}

	private void addScript(Scriptable<?,?> script) {
		add(new MapAndSeriesScript(){

			private static final long serialVersionUID = 1L;

			@Override
			public List<GPDataNode<SeriesAndTable>> script(List<Measurement> input)
			{
				return converter(input, script);
			}

			@Override
			public String getName() {
				return script.getName();
			}

			@Override
			public void showDocComp() {
				script.showDocComp();
			}

			@Override
			public int compareTo(Scriptable<?, ?> o) {
				return script.compareTo(o);
			}});
		
	}

	public MapAndSeriesScript getScript(String scriptName)
	{
		for (MapAndSeriesScript scriptBase : this)
		{
			if (scriptBase.getName().equals(scriptName))
			{
				return scriptBase;
			}
		}
		return null;
	}

	public Set<MapAndSeriesScript> getScripts(Set<String> scriptNames)
	{
		if (scriptNames == null)
		{
			return null;
		}
		return stream().filter(script -> scriptNames.contains(script.getName())).collect(Collectors.toSet());
	}

	public List<String> getScriptNames()
	{
		return stream().map(script -> script.getName()).collect(Collectors.toList());
	}
	
	public void updateFromDir(String workspaceDir)
	{
		File[] files = new File(workspaceDir+"/"+Workspacefiles.SCRIPT.getFileName()).listFiles();
		
		if (files == null)
		{
			return;
		}
		
		
		for (File file : files)
		{
			remove(file.getName());
		}
		
		for (File file : files)
		{
			add(new MultiPurposePythonScriptBase(file));
		}
		
		Collections.sort(this);
		
	}


}
