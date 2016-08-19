package edu.wehi.celcalc.cohort.gui.browser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import edu.wehi.celcalc.cohort.application.FilterContainter;
import edu.wehi.celcalc.cohort.application.MeasurementListWithNoDuplicates;
import edu.wehi.celcalc.cohort.application.Workspacefiles;
import edu.wehi.celcalc.cohort.data.CellCountXCELReader;
import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Dataset;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;
import edu.wehi.celcalc.cohort.data.gui.Filter;
import edu.wehi.celcalc.cohort.plotting.CohortPlottingFunctions;
import edu.wehi.celcalc.cohort.plotting.PlotOptions;
import edu.wehi.celcalc.cohort.util.Utilities;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPXYSeries;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class DataBrowserModel
{
	private Dataset dataset;
	
	private final MeasurementListWithNoDuplicates 	measurementModelData;
	private final FilterContainter 					filterContainer;
	private final List<String> 						imports = new ArrayList<>();

	public DataBrowserModel()
	{
		this(new MeasurementListWithNoDuplicates(), new FilterContainter());
	}
	
	public DataBrowserModel(
			MeasurementListWithNoDuplicates measurementModelData,
			FilterContainter filterContainer)
	{
		this.measurementModelData = measurementModelData;
		this.filterContainer = filterContainer;
	}
	
	
	public List<String> getAllTreatments()
	{
		return measurementModelData.getAllTreatments();
	}

	public void importExcelFile(File file) throws IOException, InvalidFormatException
	{
		if (file == null)
		{
			String errMessage = "empty file name";
			IOException e = new IOException(errMessage);
			throw e;
		}
		else
		{
			dataset = CellCountXCELReader.createDatasetFromFile(file);
			GPXYSeries.clearPreDefColors();

			// TBD Measurement will be removed
			List<Measurement> measurements 
				= Utilities.toList(CellCountXCELReader.processBookToMap(file));
			if (measurements != null)
			{
				if (measurements.size() == 0)
				{
					String errMessage = "data format error";
					InvalidFormatException e 
						= new InvalidFormatException(errMessage);
					throw e;
				}

				measurementModelData.addAll(measurements);
				imports.add(file.getName());
				addAndCreateAllFilters(measurements);
			}
		}
	}

	private void addAndCreateAllFilters(List<Measurement> measurements)
	{
		Set<String> treatments = MeasurementQuery.allTreatments(measurements);
		for (String treatement : treatments)
		{
			if (filterContainer.getFilter(treatement) == null)
			{
				Filter filter = new Filter(treatement, CellType.LIVE);
				filter.setTreatment(treatement);
				filterContainer.addFilter(filter);
			}
		}
	}

	public Collection<String> getSelectionNames()
	{
		return filterContainer.getFilterNames();
	}

	public Collection<? extends Measurement> getAllMeasurements()
	{
		return measurementModelData;
	}

	public Set<Filter> getAllFilters()
	{
		return filterContainer.getAllFilters();
	}

	public List<Measurement> getSelection(List<String> selectedNames)
	{
		Set<Filter> filters = filterContainer.allMatchingFiltersWithNames(selectedNames);
		Set<Measurement> measurementSet = new HashSet<Measurement>();
		for (Filter filter : filters)
		{
			measurementSet.addAll(filter.filter(measurementModelData));
		}
		return new ArrayList<>(measurementSet);
	}
	
	public int getTotalNumberOfMeasurements()
	{
		return measurementModelData.size();
	}

	public void deleteMeasurements(List<Measurement> measurements)
	{
		measurementModelData.removeAll(measurements);
	}

	public Filter getFilter(String filterName)
	{
		return filterContainer.getFilter(filterName);
	}

	public void addAllMeasurements(List<Measurement> measurements)
	{
		measurementModelData.addAll(measurements);
	}

	public void createSelection(Filter filter)
	{
		if (filterContainer.getFilterNames().contains(filter.getName()))
		{
			throw new RuntimeException("Contains that selection name");
		}
		filterContainer.addFilter(filter);
	}

	public JComponent plot(PlotOptions currentPlot, GPChartType scale, List<Measurement> selectedMeasurements)
	{
		return CohortPlottingFunctions.plot(selectedMeasurements, scale, currentPlot);
	}

	public boolean saveAllMeasurements(String workspacePath)
	{
		String path  = workspacePath + "\\" +Workspacefiles.MEASUREMENTS.getFileName();
		return DataBrowserDAO.saveMeasurements(path, measurementModelData);
	}

	public boolean saveAllFilters(String workspacePath)
	{
		 String path = workspacePath+"\\"+Workspacefiles.FILTER.getFileName();
		return DataBrowserDAO.saveFilters(path, filterContainer.getAllFilters());
	}

	public boolean openMeasurementsCSVFile(String absolutePath)
	{
		try
		{
			Collection<Measurement> measurements = DataBrowserDAO.loadMeasurements(absolutePath);
			if (measurements == null)
			{
				return false;
			}
			if (measurements.size() == 0)
			{
				return false;
			}
			
			return measurementModelData.addAll(measurements); 
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean openOpenFilters(File[] files)
	{
		if (files == null)
		{
			return false;
		}
		
		Collection<Filter> filters = new ArrayList<>();
		for (File file : files)
		{
			Filter filter = DataBrowserDAO.loadFilter(file.getAbsolutePath());
			if (filter == null)
			{
				return false;
			}
			filters.add(filter);
		}
		
		if (filters.stream().anyMatch(f -> filterContainer.getFilter(f.getName())!= null))
		{
			return false;
		}
		
		filterContainer.addAll(filters);
		
		return true;
	}

	public void initFromWorkspace(String workspacePath)
	{
		initMeasurements(workspacePath);
		initFilters(workspacePath);
	}

	private void initMeasurements(String workspacePath)
	{
		openMeasurementsCSVFile(workspacePath+"/"+Workspacefiles.MEASUREMENTS.getFileName());
	}

	private void initFilters(String workspacePath)
	{
		File folder = new File(workspacePath+"/"+Workspacefiles.FILTER.getFileName());
		File[] listOfFiles = folder.listFiles();
		openOpenFilters(listOfFiles);
	}

	public Dataset getDataset() {
		return dataset;
	}
}
