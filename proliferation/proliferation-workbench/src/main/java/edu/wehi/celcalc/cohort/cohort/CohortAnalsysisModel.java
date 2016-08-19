package edu.wehi.celcalc.cohort.cohort;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wehi.celcalc.cohort.PythonScript;
import edu.wehi.celcalc.cohort.bell.BellController;
import edu.wehi.celcalc.cohort.bell.Bellable;
import edu.wehi.celcalc.cohort.data.Condition;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;
import edu.wehi.celcalc.cohort.gui.application.ApplicationController;
import edu.wehi.celcalc.cohort.scriptables.SeriesAndTable;
import edu.wehi.celcalc.cohort.scripts.ScriptCountByDivision;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPCoordinateWithNameImp;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPPlotterFuncs;
import edu.wehi.celcalc.cohort.data.*;

import static edu.wehi.graphplot.plot.GPPlotterFuncs.adjustSeriesColor;
import static edu.wehi.graphplot.plot.GPPlotterFuncs.createPlot;
import static edu.wehi.graphplot.plot.GPPlotterFuncs.createRenderer;
import static edu.wehi.graphplot.plot.GPPlotterFuncs.createXYChart;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.regression.RegressionController;
import edu.wehi.graphplot.plot.regression.RegressionModel;
import edu.wehi.graphplot.plot.regression.RegressionView;
import edu.wehi.swing.GBHelper;
import edu.wehi.swing.VerticalLabelUI;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.ejml.ops.CommonOps;

import org.ejml.simple.SimpleMatrix;

public class CohortAnalsysisModel implements Bellable
{

	List<JFrame> frames = new ArrayList<>();
	
	public List<JPanel> showCohortAnalysis(
			Dataset dataset,
			List<Measurement> mess,
			JPanel pnlCohortPlots, JPanel pnlCellNumVsDiv, JPanel pnlCohortVsDiv)
	{
		ArrayList<JPanel> pnls = new ArrayList<>();
		
		pnlCohortPlots.removeAll();
		pnlCellNumVsDiv.removeAll();
		pnlCohortVsDiv.removeAll();
		
		pnlCohortPlots.add(showTrio(dataset, mess));
		pnlCellNumVsDiv.add(showCellNumberVsDivision(mess));
		pnlCohortVsDiv.add(showCohortNumberVsDivision(mess, this));
		
		pnls.add(pnlCohortPlots);
		pnls.add(pnlCellNumVsDiv);
		pnls.add(pnlCohortVsDiv);
		
		pnls.forEach(p -> {p.revalidate(); p.repaint();});
		
		return pnls;
	}
	
	JPanel plotCohort = new JPanel(new BorderLayout());
	
	public Component showTrio(Dataset dataset, List<Measurement> mess)
	{
		this.cachedMess = mess;
		
		doPlots(dataset);
		return (plotCohort);
	}
	
	
	private Component doCohortSumVsTime(Dataset dataset)
	{
		List<Condition> conditions = dataset.getConditions();
		List<GPXYSeries> series = new ArrayList();

		for (Condition currCond : conditions) {
			String name = currCond.getName();
			List<TimePoint> tpList = currCond.getTimePoints();
			int numTP = tpList.size();

			SimpleMatrix x = new SimpleMatrix(numTP, 1);
			SimpleMatrix y = new SimpleMatrix(numTP, 1);

			double m1 = 1;
			for (int itp = 0; itp < numTP; itp++) {
				TimePoint currTP = tpList.get(itp);
				double time = currTP.getTime();
				SimpleMatrix vals = currTP.getValues();

				int numDivs = vals.numRows();
				int numReps = vals.numCols();
				SimpleMatrix scales = new SimpleMatrix(numDivs, numReps);
				
				for (int i = 0; i < numDivs; i++) {
					for (int j = 0; j < numReps; j++) {
						scales.set(i, j, Math.pow(2, i));
					}
				}
				vals = vals.elementDiv(scales);
				SimpleMatrix s = SimpleMatrix.wrap(
						CommonOps.sumCols(vals.getMatrix(), null));
				double m = s.elementSum() / s.getNumElements();
				
				if (itp == 0) {
					m1 = m;
					m = 1;
				}
				else {
					m = m/m1;
				}

				x.set(itp, 0, time);
				y.set(itp, 0, m);
			}

			List<GPCoordinateWithName> cList
					= GPCoordinateWithNameImp.listFromVectors(x, y);

			GPXYSeries currData = new GPXYSeries(name, cList);
			series.add(currData);
		}

		return GPPlotterFuncs.plot(
				series,
				"survival",
				"time",
				"% cohort sum",
				GPChartType.LINE);
	}

	List<Measurement> cachedMess = new ArrayList<>();
	public void refreshTrioPlots(Dataset dataset)
	{
		if (cachedMess == null) return;
		doPlots(dataset);
	}
	
	public void doPlots(Dataset dataset)
	{
		String dataName = dataset.getName();
		plotCohort.removeAll();
		
		JPanel pnlPlots = new JPanel(new GridLayout(2,2));
		
		ChartPanel cp1 = (ChartPanel) doTotalPopulationPlot(dataset);
		pnlPlots.add(cp1);		
		GPPlotterFuncs.exportPDF(cp1, "plot_population-" + dataName + ".pdf");
		
		ChartPanel cp2 = (ChartPanel) doCohortSumVsMeanDiv(dataset);
		pnlPlots.add(cp2);
		GPPlotterFuncs.exportPDF(cp2, "plot_tot-cohort-vs-mdn-" + dataName + ".pdf");
		
		RegressionView regressionView = doMeanDivRegressionPlot(dataset);
		pnlPlots.add(regressionView.getPlot());
		ChartPanel cp3 = regressionView.getChartPanel();
		GPPlotterFuncs.exportPDF(cp3, "plot_mdn-" + dataName + ".pdf");
		
		ChartPanel cp4 = (ChartPanel) doCohortSumVsTime(dataset);
		pnlPlots.add(cp4);
		GPPlotterFuncs.exportPDF(cp4, "plot_tot-cohort-" + dataName + ".pdf");
		
		plotCohort.add(pnlPlots, BorderLayout.CENTER);
		//plotCohort.add(regressionView.getControllerPanel(), BorderLayout.SOUTH);
		
		plotCohort.revalidate();
		plotCohort.repaint();
	}
	
	interface ToComponent
	{
		public Component script(List<Measurement> mess);
	}
	
	public Component showGridPlot(List<Measurement> mess, ToComponent toComponent, String xAxis, String yAxis, String title)
	{
		List<Double> times = MeasurementQuery.allTimes(mess);
		Set<String> treatments = MeasurementQuery.allTreatments(mess);
		
		JPanel pnlPlots = new JPanel(new GridBagLayout());
		pnlPlots.setBackground(Color.WHITE);
		GBHelper gb = new GBHelper();
		
		final Font font = new Font("Serif", Font.PLAIN, 18);
		
		JLabel labelCorner = new JLabel("Treatment\\Time");
		labelCorner.setFont(font);
		pnlPlots.add(labelCorner, gb);
		
		for (Double time : times)
		{
			JLabel label = new JLabel(time+"");
			label.setFont(font);
			pnlPlots.add(label, gb.nextCol().expandW().align(GBHelper.CENTER));
		}
		
		for (String treatment : treatments)
		{
			gb.nextRow();
			JLabel label = new JLabel(treatment);
			label.setFont(font);
			pnlPlots.add(label, gb);
			for (Double time : times)
			{
				Collection<Measurement> filteredMess = new MeasurementQuery(mess).withTreatment(treatment).withTime(time).measurements;
				gb.nextCol();
				if (filteredMess.size() != 0)
				{
					Component comp = toComponent.script(new ArrayList<>(filteredMess));
					comp.setPreferredSize(new Dimension(ApplicationController.getMultiWidth(),ApplicationController.getMultiHeight()));
					pnlPlots.add(comp,gb);
				}
				else
				{
					pnlPlots.add(new JPanel(){

						
						private static final long serialVersionUID = 1L;

					{
						setBackground(Color.WHITE);
					}},gb);
				}
				
			}
		}
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		
		pnlMain.add(pnlPlots, BorderLayout.CENTER);
		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(font);
		
		pnlMain.add(lblTitle, BorderLayout.NORTH);
		
		
		JLabel lblXAxis = new JLabel("xAxis \t\t\t\t"+xAxis+"\t\t\t\t");
		lblXAxis.setFont(font);
		pnlMain.add(lblXAxis, BorderLayout.SOUTH);
		
		JLabel lblYAxis = new JLabel("\t\t\t\t"+yAxis+"\t\t\t\t");
		lblYAxis.setFont(font);
		lblYAxis.setUI(new VerticalLabelUI());
		pnlMain.add(lblYAxis, BorderLayout.WEST);
		
		return new JScrollPane(pnlMain);
	}
	
	public Component showCellNumberVsDivision(List<Measurement> mess)
	{
		return showGridPlot(mess, new ToComponent()
		{
			@Override
			public Component script(List<Measurement> mess)
			{
				List<GPXYSeries> data = new ScriptCountByDivision().scriptxy(new ArrayList<>(mess));
				data.forEach(d -> d.setVisibleInLegend(false));
				return GPPlotterFuncs.plot(data, "", "", "", GPChartType.LINE);
			}
			
		}, 
		"Division", "Populaiton", "");
	}
	
	Map<String, Double> shartedTimeAndMeanDivDict = new HashMap<>();
	
	public Component showCohortNumberVsDivision(List<Measurement> mess, Bellable bellable)
	{
		return showGridPlot(mess, new ToComponent()
		{
			@Override
			public Component script(List<Measurement> mess)
			{
				if(mess.size() == 0) return new JPanel();
				Measurement mes = mess.get(0);
				
				
				BellController bell = new BellController(mes.getTreatment(), mes.getDiv(), mes.getTime());
				bell.inputData(mess);
				if (bellable != null) bell.addBellable(bellable);
				return bell.getView();
			}
			
		},
		"Time", "Cohort No.", "");
	}

	private Component doTotalPopulationPlot(Dataset dataset) {

		List<Condition> conditions = dataset.getConditions();
		List<GPXYSeries> series = new ArrayList();
		
		for (Condition currCond : conditions) {
			String name = currCond.getName();
			List<TimePoint> tpList = currCond.getTimePoints();
			int numTP = tpList.size();

			SimpleMatrix x = new SimpleMatrix(numTP, 1);
			SimpleMatrix y = new SimpleMatrix(numTP, 1);
			
			for (int i = 0; i < numTP; i++) {
				TimePoint currTP = tpList.get(i);
				double time = currTP.getTime();
				SimpleMatrix vals = currTP.getValues();
				
				SimpleMatrix s = SimpleMatrix.wrap(
						CommonOps.sumCols(vals.getMatrix(), null));
				double m = s.elementSum()/s.getNumElements();
				
				x.set(i, 0, time);
				y.set(i, 0, m);
			}

			List<GPCoordinateWithName> cList
					= GPCoordinateWithNameImp.listFromVectors(x, y);

			GPXYSeries currData = new GPXYSeries(name, cList);
			series.add(currData);
		}

		return GPPlotterFuncs.plot(
				series,
				"total live cells",
				"time",
				"cell number",
				GPChartType.LINE);
	}

	public RegressionView doMeanDivRegressionPlot(Dataset dataset) {
		List<Condition> conditions = dataset.getConditions();
		List<GPXYSeries> series = new ArrayList();

		for (Condition currCond : conditions) {
			String name = currCond.getName();
			List<TimePoint> tpList = currCond.getTimePoints();
			int numTP = tpList.size();

			SimpleMatrix x = new SimpleMatrix(numTP, 1);
			SimpleMatrix y = new SimpleMatrix(numTP, 1);

			for (int itp = 0; itp < numTP; itp++) {
				TimePoint currTP = tpList.get(itp);
				double time = currTP.getTime();
				SimpleMatrix cellCounts = currTP.getValues();

				int numDivs = cellCounts.numRows();
				int numReps = cellCounts.numCols();
				SimpleMatrix scales = new SimpleMatrix(numDivs, numReps);
				SimpleMatrix divNums = new SimpleMatrix(numDivs, numReps);

				// TBD take this out of the loop
				for (int i = 0; i < numDivs; i++) {
					for (int j = 0; j < numReps; j++) {
						scales.set(i, j, Math.pow(2, i));
					}
				}
				SimpleMatrix cohortNums = cellCounts.elementDiv(scales);
				SimpleMatrix cohortSum = SimpleMatrix.wrap(
						CommonOps.sumCols(cohortNums.getMatrix(), null));

				// TBD take this out of the loop
				for (int i = 0; i < numDivs; i++) {
					for (int j = 0; j < numReps; j++) {
						divNums.set(i, j, i);
					}
				}
				SimpleMatrix weightedCohort = cohortNums.elementMult(divNums);
				SimpleMatrix weightedSum = SimpleMatrix.wrap(
						CommonOps.sumCols(weightedCohort.getMatrix(), null));

				SimpleMatrix mdn = weightedSum.elementDiv(cohortSum);
				double mdnMean = mdn.elementSum() / mdn.getNumElements();

				x.set(itp, 0, time);
				y.set(itp, 0, mdnMean);
			}

			List<GPCoordinateWithName> cList
					= GPCoordinateWithNameImp.listFromVectors(x, y);

			GPXYSeries currData = new GPXYSeries(name, cList);
			series.add(currData);
		}

		RegressionController regressionController
				= new RegressionController(
						new RegressionModel(series, "mean division number", "time", "MDN"),
						new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								refreshTrioPlots(dataset);
							}
						});

		ChartPanel mdnPlot = regressionController.refreshPlot();	
		RegressionView rv = regressionController.getView();
		rv.setChartPanel(mdnPlot);
		
		return rv;
	}
	
	private Component doCohortSumVsMeanDiv(Dataset dataset) {
		List<Condition> conditions = dataset.getConditions();
		List<GPXYSeries> series = new ArrayList();

		for (Condition currCond : conditions) {
			String name = currCond.getName();
			List<TimePoint> tpList = currCond.getTimePoints();
			int numTP = tpList.size();

			SimpleMatrix x = new SimpleMatrix(numTP, 1);
			SimpleMatrix y = new SimpleMatrix(numTP, 1);

			for (int itp = 0; itp < numTP; itp++) {
				TimePoint currTP = tpList.get(itp);
				SimpleMatrix cellCounts = currTP.getValues();

				int numDivs = cellCounts.numRows();
				int numReps = cellCounts.numCols();
				SimpleMatrix scales = new SimpleMatrix(numDivs, numReps);
				SimpleMatrix divNums = new SimpleMatrix(numDivs, numReps);

				// TBD take this out of the loop
				for (int i = 0; i < numDivs; i++) {
					for (int j = 0; j < numReps; j++) {
						scales.set(i, j, Math.pow(2, i));
					}
				}
				SimpleMatrix cohortNums = cellCounts.elementDiv(scales);
				SimpleMatrix cohortSum = SimpleMatrix.wrap(
						CommonOps.sumCols(cohortNums.getMatrix(), null));

				// TBD take this out of the loop
				for (int i = 0; i < numDivs; i++) {
					for (int j = 0; j < numReps; j++) {
						divNums.set(i, j, i);
					}
				}
				SimpleMatrix weightedCohort = cohortNums.elementMult(divNums);
				SimpleMatrix weightedSum = SimpleMatrix.wrap(
						CommonOps.sumCols(weightedCohort.getMatrix(), null));

				SimpleMatrix mdn = weightedSum.elementDiv(cohortSum);
				double xMean = mdn.elementSum() / mdn.getNumElements();
				double yMean
						= cohortSum.elementSum() / cohortSum.getNumElements();

				x.set(itp, 0, xMean);
				y.set(itp, 0, yMean);
			}

			List<GPCoordinateWithName> cList
					= GPCoordinateWithNameImp.listFromVectors(x, y);

			GPXYSeries currData = new GPXYSeries(name, cList);
			series.add(currData);
		}

		return GPPlotterFuncs.plot(
				series,
				"crash plots",
				"MDN",
				"cohort sum",
				GPChartType.LINE);
	}

	@Override
	public void bellChange(String treatment, int div, double time, String mu, String sigma, String scaleFactor)
	{
		// TODO Auto-generated method stub
		
		System.out.println("treatment = "+treatment);
		System.out.println("div = "+div);
		System.out.println("time = "+time);
		System.out.println("mu = "+mu);
		System.out.println("sigma = "+sigma);
		System.out.println("scaleFactor = "+scaleFactor);
		System.out.println("\n\n\n");
	}
	

}
