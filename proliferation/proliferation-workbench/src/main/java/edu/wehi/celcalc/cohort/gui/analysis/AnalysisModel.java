package edu.wehi.celcalc.cohort.gui.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wehi.celcalc.cohort.PythonScript;
import edu.wehi.celcalc.cohort.application.FilterContainter;
import edu.wehi.celcalc.cohort.application.MeasurementListWithNoDuplicates;
import edu.wehi.celcalc.cohort.application.Workspacefiles;
import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.gui.Filter;
import edu.wehi.celcalc.cohort.gui.application.ApplicationData;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterContainer;
import edu.wehi.celcalc.cohort.plotting.properties.Plotories;
import edu.wehi.celcalc.cohort.scriptables.MapAndSeriesScript;
import edu.wehi.celcalc.cohort.scripts.ScriptCohortByDivision;
import edu.wehi.celcalc.cohort.scripts.ScriptCountByDivision;
import edu.wehi.celcalc.cohort.scripts.ScriptPrecursorCohortMethodDivisionTimes;
import edu.wehi.celcalc.cohort.scripts.ScriptPrecursorCohortMethodFittedCurve;

public class AnalysisModel
{

	private 					FilterContainter filterContainer;
	private 					PlotoriesContainer plotoriesContainer;
								ScripterContainer scriptsContainter;
								AnalysisContainer analysisContainer;
	private 					MeasurementListWithNoDuplicates measurementsContainer;
	
	public AnalysisModel(
								MeasurementListWithNoDuplicates measurementsContainer,
								FilterContainter filterContainer,
								PlotoriesContainer plotoriesContainer,
								ScripterContainer scriptsContainter,
								AnalysisContainer analysisContainer)
	{
								super();
								this.measurementsContainer = measurementsContainer;
								this.filterContainer = filterContainer;
								this.plotoriesContainer = plotoriesContainer;
								this.scriptsContainter = scriptsContainter;
								this.analysisContainer = analysisContainer;
	}
	
	public AnalysisModel(ApplicationData data)
	{
		this(data.getMeasurementListWithNoDuplicates(), data.getFilterContainter(), data.getPlotoriesContainer(), data.getScriptsContainter(), data.getAnalysisContainer());
	}
	
	public FilterContainter getFilterContainer()
	{
		return filterContainer;
	}
	
	public PlotoriesContainer getPlotoriesContainer()
	{
		return plotoriesContainer;
	}
	
	public ScripterContainer getScriptsContainter() 
	{
		return scriptsContainter;
	}

	public List<Filter> getFilters(Set<String> filterNames)
	{
		return filterContainer.getFilters(filterNames);
	}

	public Set<MapAndSeriesScript> getScripts(Set<String> scriptNames)
	{
		return scriptsContainter.getScripts(scriptNames);
	}

	public Plotories getPlotories(String plotoriesName)
	{
		return plotoriesContainer.getPlotories(plotoriesName);
	}

	public Analysis createNewAnalysis(String analysisName)
	{
		Analysis analysis = new Analysis(analysisName);
		if (analysisContainer.add(analysis))
		{
			return analysis; 
		}
		else
		{
			return null;
		}
	}

	public Collection<String> getSelectionNames()
	{
		return filterContainer.getFilterNames();
	}

	public List<String>  getScriptNames()
	{
		return scriptsContainter.getScriptNames();
	}
	
	public AnalysisContainer getAnalysisContainer()
	{
		return analysisContainer;
	}

	public boolean openAnalysis(File[] files)
	{
		if (files == null)
		{
			return false;
		}
		Collection<Analysis> analysis = new ArrayList<>();
		for (File f : files)
		{
			Analysis a = AnalysisDao.loadAnalysis(f.getAbsolutePath());
			if (a == null)
			{
				return false;
			}
			else
			{
				analysis.add(a);
			}
		}
		return analysisContainer.addAll(analysis);
	}

	public boolean saveAllAnalysis(String workspacePath)
	{
		return AnalysisDao.saveAnalysis(analysisContainer.getAllAnalysiss(), workspacePath+"/"+Workspacefiles.ANALYSIS.getFileName());
	}

	public void initFromWorkspace(String workspacePath)
	{
		File folder = new File(workspacePath+"/"+Workspacefiles.ANALYSIS.getFileName());
		File[] listOfAnalysis = folder.listFiles();
		openAnalysis(listOfAnalysis);
	}

	public void createcreateDefaultSuite()
	{
		createCellVsDiv();
		createCohort();
		createMeanDivision();
		createCohortSumVsMeanDivision();
		createPrecursorCohortModelFitting();
	}
	
	private void createPrecursorCohortModelFitting()
	{
		Collection<String> treatments = measurementsContainer.getAllTreatments();
		
		String name = "Precursor Cohort Model";
		for (String treatment : treatments)
		{
			Filter filter = filterContainer.createLiveFilterForTreatment(treatment);
			String analysisName = name + "("+filter.getName()+")";
			
			Set<String> scripts = new HashSet<>();
			scripts.add(new ScriptPrecursorCohortMethodDivisionTimes().getName());
			scripts.add(new ScriptPrecursorCohortMethodFittedCurve().getName() );
			
			Set<String> filters = new HashSet<String>();
			filters.add(filter.getName());
			
			Analysis analysis = new Analysis(analysisName, filters, scripts, "div", "cohort", analysisName, false);
			analysis.setGroup("Precursor Cohort");
			analysis.setShowing(false);
			analysisContainer.add(analysis);
		}
	}

	public void createCellVsDiv()
	{
		Collection<String> treatments = measurementsContainer.getAllTreatments();
		
		// Number of Cells Vs. Division
		String name = "Number of Cells Vs. Division";
		for (String treatment : treatments)
		{
			Filter filter = filterContainer.createLiveFilterForTreatment(treatment);
			String analysisName = name + "("+filter.getName()+")";
			Analysis analysis = new Analysis(analysisName, filter.getName(), new ScriptCountByDivision().getName(), "Division", "Count", analysisName, false, name);
			analysisContainer.add(analysis);
		}
	}
	
	public void createCohort()
	{
		Collection<String> treatments = measurementsContainer.getAllTreatments();
		
		// Number of Cells Vs. Division
		String name = "Cohort Number Vs. Division";
		for (String treatment : treatments)
		{
			String filterName = CellType.LIVE.name+" "+treatment;
			Filter filter = new Filter(
					filterName,
					CellType.LIVE,
					treatment);
			
			filterContainer.addFilter(filter);
			
			String analysisName = name + "("+filter.getName()+")";
			Analysis analysis = new Analysis(analysisName, filter.getName(), new ScriptCohortByDivision().getName(), "Cohort", "Div", analysisName, false, name);
			analysisContainer.add(analysis);
		}
	}
	
	public void createMeanDivision()
	{
		Filter filter = new Filter(
				"live",
				CellType.LIVE);
		filterContainer.addFilter(filter);
		
		String analysisName = "Mean Division";
		Analysis analysis = new Analysis(analysisName, filter.getName(), PythonScript.MEANDIV.getScript().getName(), "Mean Div", "Div", analysisName, false, "");
		analysisContainer.add(analysis);
	}
	
	public void createCohortSumVsMeanDivision()
	{
		Filter filter = new Filter(
				"live",
				CellType.LIVE);
		filterContainer.addFilter(filter);
		
		String analysisName = "CohortSum Vs. Mean Division";
		Analysis analysis = new Analysis(analysisName, filter.getName(), PythonScript.COHORTSUMVSMEANDIV.getScript().getName(), "CohortSum", "Mean Division", analysisName, false, "");
		analysisContainer.add(analysis);
	}

	public void showDoc(String scriptName)
	{
		MapAndSeriesScript script = scriptsContainter.getScript(scriptName);
		script.showDocComp();
	}

}
