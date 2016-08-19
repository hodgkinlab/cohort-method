package edu.wehi.celcalc.cohort.plotting;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.wehi.celcalc.cohort.PythonScript;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MesurementGraphConverter;
import edu.wehi.celcalc.cohort.scriptables.SeriesAndTable;
import edu.wehi.celcalc.cohort.scripts.ScriptCohortByDivision;
import edu.wehi.celcalc.cohort.scripts.ScriptCountByDivision;
import edu.wehi.celcalc.cohort.scripts.ScriptPrecursorCohortMethodFittedCurve;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPPlotterFuncs;
import edu.wehi.graphplot.plot.GPXYSeries;

public class CohortPlottingFunctions
{

	public static JComponent plotCountByTreatmentByDivision(List<Measurement> selectedMeasurements, GPChartType chartType)
	{
		List<GPDataNode<GPXYSeries>> results = new ScriptCountByDivision().script(selectedMeasurements);
		return GPPlotterFuncs.plotNodes(results, "Population by each Division Vs. Time", "Time", "Cells", chartType);
	}
	
	public static JComponent plotMeanByTreatment(List<Measurement> selectedMeasurements, GPChartType chartType)
	{
		List<GPDataNode<SeriesAndTable>> results = PythonScript.POPULATION.getScript().script(selectedMeasurements);
		List<GPXYSeries> plots = results.stream().map(st -> st.getData().getSeries()).collect(Collectors.toList());
		return GPPlotterFuncs.plot(plots, "Total Population Vs Time", "Time", "Cells", chartType);
	}
	

	
	public static JComponent plotMeanDivisionByTime(List<Measurement> selectedMeasurements, GPChartType chartType)
	{
		List<GPDataNode<SeriesAndTable>> results = PythonScript.MEANDIV.getScript().script(selectedMeasurements);
		List<GPXYSeries> plots = results.stream().map(st -> st.getData().getSeries()).collect(Collectors.toList());
		return GPPlotterFuncs.plot(plots, "Mean Division Vs. Time", "Time", "Cells", chartType);
	}
	
	public static JComponent plotCohort(List<Measurement> selectedMeasurements, GPChartType chartType)
	{
		List<GPDataNode<GPXYSeries>> result = new ScriptCohortByDivision().script(selectedMeasurements);
		return GPPlotterFuncs.plotNodes(result, "Cohort Vs Time", "Time", "Cohort No.", chartType);
	}
	

	public static JComponent plotCohortFitter(List<Measurement> selectedMeasurements,	GPChartType chartType)
	{
		List<GPDataNode<GPXYSeries>> results = new ScriptPrecursorCohortMethodFittedCurve().script(selectedMeasurements);
		return GPPlotterFuncs.plotNodes(results, "Cohort Fitter", "", "", chartType);
	}
	
	public static JComponent plot(List<Measurement> selectedMeasurements, GPChartType scale, PlotOptions options)
	{
		switch(options)
		{
		case MEANTREATMENTDIVISION:
			return plotCountByTreatmentByDivision(selectedMeasurements, 	scale);
		case MEANTREATMENT:
			return plotMeanByTreatment(selectedMeasurements, 			scale);
		case MEANDIVISIONBYTIME:
			return plotMeanDivisionByTime(selectedMeasurements, 			scale);
		case RAWDATA:
			List<GPXYSeries> xySeries = MesurementGraphConverter.timeSeriessByGroupable(selectedMeasurements, "", "", "", "");
			return GPPlotterFuncs.plot(xySeries, "Raw Data", "Time", "No. Cells", scale);
		case COHORT:
			return plotCohort(selectedMeasurements, 			scale);
		case COHORTFITTER:
			return plotCohortFitter(selectedMeasurements, 			scale);
		default:
			return new JPanel();
		}
	}
	
	public static JComponent plot(Measurement measurement, GPChartType scale, PlotOptions options)
	{
		List<Measurement> mess = new ArrayList<>();
		mess.add(measurement);
		return plot(mess, scale, options);
	}
	
	public static void plotShow(List<Measurement> selectedMeasurements, GPChartType scale, PlotOptions options)
	{
		JFrame frame = new JFrame();
		JPanel pnl = new JPanel(new BorderLayout());
		frame.add(pnl);
		pnl.add(plot(selectedMeasurements, scale, options));
		frame.setSize(400,400);
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			
			@Override
			public void run()
			{
				frame.setVisible(true);
			}
			
		});
	}
	
	public static void plotShow(Measurement measurement, GPChartType scale, PlotOptions options)
	{
		List<Measurement> measurements = new ArrayList<>();
		measurements.add(measurement);
		plotShow(measurements, scale, options);
	}


	
	
	
	
}
