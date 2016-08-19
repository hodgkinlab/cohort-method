package edu.wehi.demo;

import java.util.List;

import javax.swing.JComponent;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.data.MesurementGraphConverter;
import edu.wehi.celcalc.cohort.res.TestFileData;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPController;
import edu.wehi.graphplot.plot.GPXYSeries;

public class ExamplePlotController {
	
	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		List measurementsList = TestFileData.mesurements;
		@SuppressWarnings("unchecked")
		List<GPXYSeries> plots = MesurementGraphConverter
				.timeSeriessByGroupable(measurementsList, "", "", "", "");

		GPController controller = new GPController("test", "time", "cells", plots, GPChartType.LINE);
		JComponent comp = controller.getView();

		GUI.gui(comp);
	}
	
	
}
