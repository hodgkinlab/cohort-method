package edu.wehi.celcalc.cohort.gui.browser;

import java.io.Serializable;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class DataBrowsingMenu implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	public JMenu miRecentImportExcel = 					new JMenu("Recent Import Excel File");
	public JMenuItem miImportExcel = 					new JMenuItem("Import Excel");
	
	public JMenu mnuFilter = new JMenu("Filter");
	public JMenuItem miCreateNewFilter = 				new JMenuItem("Create New Filter");
	public JMenuItem miSaveFilters = 					new JMenuItem("Save Filters");
	public JMenuItem miOpenFilters = 					new JMenuItem("Open Filters.xml");

	public JMenu mnuPlotView = 							new JMenu("Plot");
	public JMenuItem miPlotMeanTreatmentLinear =		new JMenuItem("Population by Treatment (Linear Scale)");
	public JMenuItem miPlotMeanTreatmentLogarythmic = 	new JMenuItem("Population by Treatment (Log Scale)");
	public JMenuItem miPlotMeanDivisionTreatmentLinear= new JMenuItem("Population by Treatment by Division (Linear Scale");
	public JMenuItem miPlotMeanDivisionTreatmentLog =	new JMenuItem("Population by Treatment by Division (Log Scale");
	public JMenuItem miPlotRawData = 					new JMenuItem("Raw Data");
	public JMenuItem miPlotCohort =						new JMenuItem("Cohort");
	public JMenuItem mimiPlotCohortFitter = 			new JMenuItem("Cohort Fitter");

	public JMenu mnuMeasurement = new JMenu("Measurement");
	public JMenuItem miSaveMeasurements = new JMenuItem("Save Measurements");
	public JMenuItem miOpenMeasurementsCSV = new JMenuItem("Open Measurements.cvs");
	public JMenuItem miNewMeasurements = new JMenuItem("Enter in New Measurements");
	
	public DataBrowsingMenu()
	{
		mnuFilter.add(miCreateNewFilter);
		mnuMeasurement.add(miNewMeasurements);
		mnuMeasurement.add(miSaveMeasurements);
		mnuMeasurement.add(miOpenMeasurementsCSV);
		mnuFilter.add(miCreateNewFilter);
		mnuFilter.add(miSaveFilters);
		mnuFilter.add(miOpenFilters);
		
		
		mnuPlotView.add(miPlotMeanTreatmentLinear);
		mnuPlotView.add(miPlotMeanTreatmentLogarythmic);
		
		mnuPlotView.addSeparator();
		
		mnuPlotView.add(miPlotMeanDivisionTreatmentLinear);
		mnuPlotView.add(miPlotMeanDivisionTreatmentLog);
		
		mnuPlotView.addSeparator();
		
		mnuPlotView.add(miPlotRawData);

		mnuPlotView.addSeparator();
		
		mnuPlotView.add(miPlotCohort);
		mnuPlotView.add(mimiPlotCohortFitter);
		
		mnuPlotView.addSeparator();
	}
	
	
	public JMenuItem getImportExcelJMenuItem()
	{
		return miImportExcel;
	}
	
	public JMenu getRecentExcelImport()
	{
		return miRecentImportExcel;
	}
	
	public JMenu getPlotView()
	{
		return mnuPlotView;
	}
	
	public JMenu getMeasurements()
	{
		return mnuMeasurement;
	}
	
	public void setRecentImportFiles(List<JMenuItem> items) 
	{
		miRecentImportExcel.removeAll();
		items.forEach(itm -> miRecentImportExcel.add(itm));
		miRecentImportExcel.revalidate();
		miRecentImportExcel.repaint();
	}

	public void limitedMode()
	{
		mnuMeasurement.remove(miSaveMeasurements);
		mnuMeasurement.remove(miNewMeasurements);
		mnuFilter.remove(miSaveFilters);
	}
	
}
