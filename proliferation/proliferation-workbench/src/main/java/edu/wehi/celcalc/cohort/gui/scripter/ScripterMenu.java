package edu.wehi.celcalc.cohort.gui.scripter;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import edu.wehi.GUITools;

public class ScripterMenu extends JMenu
{

	private static final long serialVersionUID = 1L;
	
	JMenu mnuScript =							new JMenu("Script");
	
	JMenu mniExamples =							new JMenu("Example Iter Scripts");
	JMenuItem mniExampleCohort =				new JMenuItem("Cohort");
	JMenuItem mniExampleCohortSum =				new JMenuItem("Cohort Sum");
	JMenuItem mniExampleCohortSumVsMeanDiv =	new JMenuItem("Cohort Sum Vs. Mean Division");
	JMenuItem mniMeanDiv =						new JMenuItem("Mean Division");
	
	JMenu mniExampleConsole = 					new JMenu("Exmple Console Scrips");
	JMenuItem mniExampleTemplate =				new JMenuItem("template");
	
	JMenuItem mniRunScript = 					new JMenuItem("Run Script (F4)");
	JMenuItem mniOpenScript = 					new JMenuItem("Open Script");
	JMenuItem mniCloseScript = 					new JMenuItem("Close Script");
	JMenuItem mniSaveScript = 					new JMenuItem("Save Script");
	JMenuItem mniNewScript = 					new JMenuItem("New Script");
	JMenuItem mniDeleteScript = 				new JMenuItem("Delete Script");
	JMenuItem mniRefresh = 						new JMenuItem("Refresh");
	
	public ScripterMenu()
	{
		super("Script");
		add(mniExamples);
		mniExamples.add(mniExampleCohort);
		mniExamples.add(mniExampleCohortSum);
		mniExamples.add(mniExampleCohortSumVsMeanDiv);
		mniExamples.add(mniMeanDiv);
		
		add(mniExampleConsole);
		mniExampleConsole.add(mniExampleTemplate);
		
		addSeparator();
		add(mniNewScript);
		add(mniOpenScript);	
		add(mniCloseScript);
		add(mniSaveScript);
		addSeparator();
		add(mniDeleteScript);
		add(mniRefresh);
		addSeparator();
		add(mniRunScript);
	}

	public void setMenuItemsWithNoActionListenersInactive()
	{
		GUITools.setMenuItemsWithNoActionListenersInactive(this);
	}

}
