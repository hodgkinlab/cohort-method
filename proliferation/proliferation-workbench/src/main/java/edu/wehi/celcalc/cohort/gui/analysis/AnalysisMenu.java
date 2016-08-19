package edu.wehi.celcalc.cohort.gui.analysis;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class AnalysisMenu
{
	
	JMenu mnuAnalysis = 				new JMenu("Analysis");
	JMenuItem mniNewAnalysis = 			new JMenuItem("New Analysis");
	JMenuItem mniSaveAnalysis = 		new JMenuItem("Save Analysis");
	JMenuItem mniOpenAnalysis = 		new JMenuItem("Open Analysis");
	JMenuItem mniCreateDefaultSuite = 	new JMenuItem("Create Default Suite");
	JMenuItem mniMultipleTrials = 		new JMenuItem("Create Trial Runs");
	
	public AnalysisMenu()
	{
		mnuAnalysis.add(mniNewAnalysis);
		mnuAnalysis.add(mniSaveAnalysis);
		mnuAnalysis.add(mniOpenAnalysis);
		mnuAnalysis.add(mniCreateDefaultSuite);
		mnuAnalysis.addSeparator();
	}

	public JMenu getAnalysisMenu()
	{
		return mnuAnalysis;
	}

	public void limitedMode()
	{
		mnuAnalysis.remove(mniSaveAnalysis);
	}
	
}
