package edu.wehi.celcalc.cohort.cohort;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class CohortAnalysisView extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	JPanel pnlPlots = new JPanel(new GridLayout(2, 2));
	JPanel pnlControls = new JPanel();
	
	JPanel pnlMeanDiv = new JPanel();
	JPanel pnlTotalPop = new JPanel();
	JPanel pnlCrashPlot = new JPanel();
	JPanel pnlCohortSum = new JPanel();
	
	public CohortAnalysisView()
	{
		setLayout(new BorderLayout());
		
		add(pnlPlots, BorderLayout.CENTER);
		add(pnlControls, BorderLayout.SOUTH);
		
		pnlPlots.add(pnlMeanDiv);
		pnlPlots.add(pnlTotalPop);
		pnlPlots.add(pnlCrashPlot);
		pnlPlots.add(pnlCohortSum);
	}
	

}
