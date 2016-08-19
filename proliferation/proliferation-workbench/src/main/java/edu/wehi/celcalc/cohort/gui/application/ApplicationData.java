package edu.wehi.celcalc.cohort.gui.application;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import edu.wehi.celcalc.cohort.application.FilterContainter;
import edu.wehi.celcalc.cohort.application.MeasurementListWithNoDuplicates;
import edu.wehi.celcalc.cohort.data.Dataset;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.Filter;
import edu.wehi.celcalc.cohort.gui.analysis.AnalysisContainer;
import edu.wehi.celcalc.cohort.gui.analysis.PlotoriesContainer;
import edu.wehi.celcalc.cohort.gui.parameterer.type.InputParametersSet;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterContainer;

public class ApplicationData implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	private Dataset dataset = null;
	
	final private MeasurementListWithNoDuplicates 	measurementModelData 	= new MeasurementListWithNoDuplicates(); 
	final private FilterContainter 					filterContainer 		= new FilterContainter();
	final private PlotoriesContainer 				plotoriesContainer		= new PlotoriesContainer();
	final private ScripterContainer 				scripterContainer;
	final private transient AnalysisContainer 		analysisContainer 		= new AnalysisContainer();
	final private InputParametersSet				inputParameters 	= new InputParametersSet();  

	public ApplicationData()
	{
		this("");
	}
	
	public ApplicationData(String workspaceDir)
	{
		this.scripterContainer = new ScripterContainer(workspaceDir);
	}

	public MeasurementListWithNoDuplicates getMeasurementListWithNoDuplicates()
	{
		return measurementModelData;
	}
	
	public FilterContainter getFilterContainter()
	{
		return filterContainer;
	}

	public Collection<? extends Measurement> getAllMeasurements()
	{
		return measurementModelData;
	}
	
	public Dataset getDataset()
	{
		return dataset;
	}

	public Filter getFilter(String filterName)
	{
		return filterContainer.getFilter(filterName);
	}

	public Set<Filter> getAllFilters()
	{
		return filterContainer.getAllFilters();
	}

	public PlotoriesContainer getPlotoriesContainer()
	{
		return plotoriesContainer;
	}

	public ScripterContainer getScriptsContainter()
	{
		return scripterContainer;
	}

	public AnalysisContainer getAnalysisContainer()
	{
		return analysisContainer;
	}

	public void dipose()
	{
//		measurementModelData 	= null;
//		filterContainer 		= null;
//		plotoriesContainer 		= null;	
//		scripterContainer 		= null; 	
//		analysisContainer 		= null; 	
	}

	public InputParametersSet getInputParameters()
	{
		return inputParameters;
	}
}