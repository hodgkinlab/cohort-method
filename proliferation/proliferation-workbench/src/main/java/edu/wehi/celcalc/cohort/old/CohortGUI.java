/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wehi.celcalc.cohort.old;

/**
 *
 * @author pavlyshyn.d
 */

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
//import Share.*;
import java.util.List;

import org.math.plot.*;
import org.apache.commons.math3.distribution.NormalDistribution;

public class CohortGUI extends JFrame
{
	
	// Computation classes
	private CohortProcessor cohortProcessor;
	private CohortXLSReader reader;
	private Map<Double, Map<Double, double[]>> params;
	private Map<Double, Map<Double, List<Double>>> meanLiveData;

	private final double[] titleCoords = {0.5, 1.1};

	// Frame variables
	private double currConc = -1;
	private String currConcLabel = "N/A";
	private double timeToFirstDivision;
	private double timeToSubsequentDivision;

	// Panels
	private final JPanel mainPanel = new JPanel();

	private final JPanel controlPanel = new JPanel(new FlowLayout());
	private final JPanel infoPanel = new JPanel();

	private final JTabbedPane displayTabs = new JTabbedPane();

	private final JPanel timePlotDisplay = new JPanel();
	private final JPanel summaryPlotDisplay = new JPanel();

	private final JPanel dataSummary = new JPanel();
	private final JPanel fittedTimeDisplay = new JPanel();

	private final JPanel fullTimeDisplay = new JPanel();

	private final JScrollPane timePlotDisplayScroll = new JScrollPane(timePlotDisplay);

	// controlPanel elements
	private final JButton loadButton = new JButton("Load Data");
	private final JButton plotButton = new JButton("Plot Cohort Numbers");
	private final JButton summaryButton = new JButton("Plot Parameters");
	private final JButton closeButton = new JButton("Close tab");
	private final JComboBox<ConcComboItem> concSelect = new JComboBox<ConcComboItem>();
	private final JFileChooser loadDialogue = new JFileChooser("C:\\Users\\futschik.d\\Downloads");



	public CohortGUI() {
		super("Cohort Fitter");
		setSize(700,900);
		//createMenuBar();
		createPanelContents();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		//pack();
		
		
	}

	public static void main(String[] args) {
		
		new CohortGUI().setVisible(true);
		
	}

	private void createPanelContents() {
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		createControlPanel();
		createDisplayTabs();

		//Display
		add(mainPanel);
	}

	private void removeIndex(int i)
	{
		displayTabs.remove(i);
	}
	
	private void removeSelectedIndex()
	{
		removeIndex(displayTabs.getSelectedIndex());
	}
	
	private void createControlPanel() {

		controlPanel.setMaximumSize(new Dimension(700, 0));
		
		
		
		closeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				removeSelectedIndex();
			}
		});

		loadButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int returnVal = loadDialogue.showOpenDialog(mainPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = loadDialogue.getSelectedFile();

					// Load appropriate data
					reader = new CohortXLSReader();
					System.out.println(file.getPath());
					reader.parseFile(file.getPath());

					meanLiveData = reader.getLiveMeans();

					cohortProcessor = new CohortProcessor(reader);


					// Get new concentrations
					List<Double> concsList = reader.getConcentrationList();

					// Update concentration combobox
					concSelect.removeAllItems();
					//ArrayList<String> concsStringList = new ArrayList<String>();
					for (Double conc : concsList) {
						concSelect.addItem(new ConcComboItem(conc));
					}

					displayTabs.removeAll();

					params = null;
				}
			}
		});

		
		plotButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (currConc < 0) {
					//
				} else {
					createTimePlots();
					displayTabs.setSelectedIndex(displayTabs.getTabCount()-1);
				}

			}
		});

		
		
		summaryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currConc < 0) {
					//);
				} else {
					createSummaryPlots();
					displayTabs.setSelectedIndex(displayTabs.getTabCount()-1);
				}
			}
		});

		// Actions for setting concentration based on loaded data
		concSelect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				currConc = ((ConcComboItem) e.getItem()).getIndex();
				currConcLabel = ((ConcComboItem) e.getItem()).getLabel();
				//statusLabel.setText("Concentration index:" + currConc);

				
			}
		});


		controlPanel.add(loadButton);
		controlPanel.add(concSelect);
		controlPanel.add(plotButton);
		controlPanel.add(summaryButton);
		controlPanel.add(closeButton);

		mainPanel.add(controlPanel);
	}

	private void createDisplayTabs()
	{
		mainPanel.add(displayTabs);
	}
	
	
	public static double[] computeDivisionTimes(
			Map<Double, Map<Double, List<Double>>> meanLiveData,
			Map<Double, Map<Double, List<List<Double>>>> liveCellData,
			double currConc)
	{
		
		CohortProcessor cohortProcessor = new CohortProcessor(meanLiveData, liveCellData);
		Map<Double, Map<Double, double[]>> paramss = cohortProcessor.fitter();
		
		double[] meanTimes = cohortProcessor.getFirstTimes(currConc);
		return meanTimes;
	}
	
	
	@Deprecated
	public static double[]  getFittedPlots(
			Map<Double, Map<Double, List<Double>>> meanLiveData,
			Map<Double, Map<Double, List<List<Double>>>> liveCellData,
			double currConc,
			double time)
	{
		CohortProcessor cohortProcessor = new CohortProcessor(meanLiveData, liveCellData);
		Map<Double, Map<Double, double[]>> paramss = cohortProcessor.fitter();

		List<Double> timeList = new ArrayList<Double>(meanLiveData.get(currConc).keySet());
		Collections.sort(timeList);
		
		
			List<List<Double>> cohortVals = cohortProcessor
					.getCohortNumbers()
					.get(currConc)
					.get(time);

			// Make fitted line plots
			double[] optimArray = new double[cohortVals.get(0).size()];

			NormalDistribution dist = new NormalDistribution(
					paramss.get(currConc).get(time)[0],
					paramss.get(currConc).get(time)[1]+1e-5);

			for (int i=0; i<cohortVals.get(0).size()-1; i++)
			{
				optimArray[i] = paramss.get(currConc).get(time)[2]*(dist.cumulativeProbability(i+0.5)
						- dist.cumulativeProbability(i-0.5));
			}
			
			optimArray[cohortVals.get(0).size()-1] = paramss.get(currConc).get(time)[2]*(1-dist.cumulativeProbability(cohortVals.get(0).size()-1.5));
			

		return optimArray;
	}
	

	private void createTimePlots()
	{
		
		timePlotDisplay.setLayout(new BoxLayout(timePlotDisplay, BoxLayout.Y_AXIS));
		
		double[] meanTimes = cohortProcessor.getFirstTimes(currConc);
		if (params == null) 
		{
			params = cohortProcessor.fitter();
		}
		timeToFirstDivision = meanTimes[0];
		timeToSubsequentDivision = meanTimes[1];

		ArrayList<Plot2DPanel> timePlots = new ArrayList<Plot2DPanel>();
		int plotInd = 0;
		List<Double> timeList = new ArrayList<Double>(meanLiveData.get(currConc).keySet());
		Collections.sort(timeList);
		for(Double time : timeList)
		
		{
			timePlots.add(new Plot2DPanel());
			List<List<Double>> cohortVals = cohortProcessor
					.getCohortNumbers()
					.get(currConc)
					.get(time);
			// Make data scatter plots
			for (int i=0; i<cohortVals.size(); i++) 
			{

				double[] cohortArray = new double[cohortVals.get(i).size()];

				for (int k=0; k<cohortVals.get(i).size(); k++) {
					cohortArray[k] = cohortVals.get(i).get(k).doubleValue();
					
				}

				timePlots.get(plotInd).addScatterPlot("Cohort sizes", cohortArray);
			}

			
			// Make fitted line plots
			double[] optimArray = new double[cohortVals.get(0).size()];

			NormalDistribution dist = new NormalDistribution(
					params.get(currConc).get(time)[0],
					params.get(currConc).get(time)[1]+1e-5);

			for (int i=0; i<cohortVals.get(0).size()-1; i++)
			{
				optimArray[i] = params.get(currConc).get(time)[2]*
							(dist.cumulativeProbability(i+0.5) - dist.cumulativeProbability(i-0.5));
			}

			optimArray[cohortVals.get(0).size()-1] = params.get(currConc).get(time)[2]*(1-dist.cumulativeProbability(cohortVals.get(0).size()-1.5));
			
			
			// plot settings
			timePlots.get(plotInd).addLinePlot("Approximation", Color.BLACK, optimArray);
			timePlots.get(plotInd).setAxisLabels("Division", "Cohort Size");
			timePlots.get(plotInd).setPreferredSize(new Dimension(100, 200));
			timePlots.get(plotInd).addBaseLabel("Time = " + time.toString(), Color.red, titleCoords);
			timePlotDisplay.add(timePlots.get(plotInd));
			plotInd++;
		}

		timePlotDisplayScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		timePlotDisplayScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		createInfoPanel();


		fullTimeDisplay.setLayout(new BoxLayout(fullTimeDisplay, BoxLayout.Y_AXIS));

		fullTimeDisplay.add(timePlotDisplayScroll);
		fullTimeDisplay.add(infoPanel);

		displayTabs.addTab("Cohort plots (c = " + currConcLabel + ")", fullTimeDisplay);
	}

	/*
	 * Creates plots summariseing the relationships between fitted parameters
	 */
	private void createSummaryPlots() {
		
		summaryPlotDisplay.setLayout(new BoxLayout(summaryPlotDisplay, BoxLayout.Y_AXIS));

		if (params == null) {
			params = cohortProcessor.fitter();
		}

		// Division number vs. cohort size

		// Harvest time vs. mean division number
		Plot2DPanel timeVsDivNum = new Plot2DPanel();

		//params = cohortProcessor.fitter();

		Map<Double, double []> currConcParams = params.get(currConc);
		double[] meanDivArray = new double[currConcParams.size()];

		//List<Double> timePoints = cohortProcessor.getTimePoints();
		double[] timePointArray = new double[currConcParams.size()];

		double[] sdDivArray = new double[currConcParams.size()];
		double[] gammaArray = new double[currConcParams.size()];
		//assert timePoints.size() == currConcParams.size();

		int i=0;
		for (Double time : currConcParams.keySet()) {
			meanDivArray[i] = currConcParams.get(time)[0];
			timePointArray[i] = time;
			sdDivArray[i] = currConcParams.get(time)[1];
			gammaArray[i] = currConcParams.get(time)[2];
			i++;
		}


		timeVsDivNum.addScatterPlot("Time vs div num",
				timePointArray,
				meanDivArray);
		timeVsDivNum.setAxisLabels("Time", "Mean division");
		timeVsDivNum.addBaseLabel("Time vs Mean division number", Color.red, titleCoords);

		summaryPlotDisplay.add(timeVsDivNum);

		// Mean division number vs. cohort size
		Plot2DPanel divNumVsGamma = new Plot2DPanel();

		divNumVsGamma.addScatterPlot("Div num mean vs cohort size",
				meanDivArray,
				gammaArray);
		divNumVsGamma.setAxisLabels("Mean division", "Cohort size");
		divNumVsGamma.addBaseLabel("Mean division number vs Cohort size", Color.red, titleCoords);

		summaryPlotDisplay.add(divNumVsGamma);

		// Mean division number vs. standard deviation
		
		Plot2DPanel divNumVsSD = new Plot2DPanel();

		divNumVsSD.addScatterPlot("Div num mean vs standard deviation",
				meanDivArray,
				sdDivArray);
		divNumVsSD.setAxisLabels("Mean division", "Division standard deviation");
		divNumVsSD.addBaseLabel("Mean division number vs Standard deviation of division number", Color.red, titleCoords);

		summaryPlotDisplay.add(divNumVsSD);

		//>>>>>>>>>>

		displayTabs.addTab("Parameter plots (c = " + currConcLabel + ")", summaryPlotDisplay);

	}


	private void createInfoPanel() {
		
		infoPanel.setLayout(new GridLayout(0, 2));
		infoPanel.setMaximumSize(new Dimension(700, 200));

		dataSummary.setLayout(new BoxLayout(dataSummary, BoxLayout.Y_AXIS));

		TitledBorder fittedSummaryBorder = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Data Summary");

		dataSummary.setBorder(fittedSummaryBorder);

		dataSummary.add(new JLabel("Concentration: " + currConc));

		fittedTimeDisplay.setLayout(new FlowLayout(FlowLayout.LEFT));

		TitledBorder fittedTimeBorder = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Mean Division Times");

		fittedTimeDisplay.setBorder(fittedTimeBorder);

		JPanel divisionLabelsPanel = new JPanel(new GridLayout(2, 1));

		divisionLabelsPanel.add(new JLabel("Mean time to first division: "));
		divisionLabelsPanel.add(new JLabel("Mean time to Subsequent division: "));


		JPanel divisionValuesPanel = new JPanel(new GridLayout(2, 1));
		
		divisionValuesPanel.add(new JLabel(String.format("%.2f", timeToFirstDivision)));
		divisionValuesPanel.add(new JLabel(String.format("%.2f", timeToSubsequentDivision)));

		fittedTimeDisplay.add(divisionLabelsPanel);
		fittedTimeDisplay.add(divisionValuesPanel);
		

		infoPanel.add(dataSummary);
		infoPanel.add(fittedTimeDisplay);

	}
	
	
	// Supplementary classes
	private class ConcComboItem {
		private double index;
		private String label;

		public ConcComboItem(Double index, String label) {
			this.index = index;
			this.label = label;
		}

		public ConcComboItem(Double index) {
			this.index = index;
			this.label = index.toString();
		}

		public double getIndex() {
			return this.index;
		}

		public String getLabel() {
			return this.label;
		}

		@Override
		public String toString() {
			return label;
		}
	}
}
