package edu.wehi.celcalc.cohort.cohort;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class CohortMenu extends JMenu
{

	private static final long serialVersionUID = 1L;
	
	JMenuItem miCohortAnalysis = new JMenuItem("Cohort Analysis");
	
	public CohortMenu()
	{
		super("Cohort Analysis");
		add(miCohortAnalysis);
	}

}
