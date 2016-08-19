package edu.wehi.celcalc.cohort.gui.application;

import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import edu.wehi.GUI;
import edu.wehi.GUITools;
import edu.wehi.celcalc.cohort.cohort.CohortMenu;
import edu.wehi.celcalc.cohort.gui.analyizer.AnalizerBar;
import edu.wehi.celcalc.cohort.gui.analysis.AnalysisMenu;
import edu.wehi.celcalc.cohort.gui.browser.DataBrowsingMenu;
import edu.wehi.celcalc.cohort.gui.parameterer.ParameterMenu;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterMenu;
import edu.wehi.swing.IconListRenderer;
import edu.wehi.swing.JComboBoxNoBorder;

public class ApplicationMenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L;
	
	JMenu mnuFile = 							new JMenu("File");
	JMenuItem miSwitchWorkspace =				new JMenuItem("Switch Workspace");
	JMenu muRecentWorkspace =					new JMenu("Recent Workspace");
	JMenuItem miSaveEntireWorkSpace	=			new JMenuItem("Save Entire Workspace");
	
	ScripterMenu mnuScript =					new ScripterMenu();

	AnalysisMenu mAnalysis = new AnalysisMenu(); 
	
	JMenu mnOptions = 							new JMenu("Options");
	JMenuItem miPreferences = 					new JMenuItem("Preferences");
	JMenuItem miHelp = 							new JMenuItem("Help");
	JMenuItem miAbout = 						new JMenuItem("About");
	JCheckBoxMenuItem michkUpdateAll = 			new JCheckBoxMenuItem("Live Updates");
	JRadioButtonMenuItem miStd =				new JRadioButtonMenuItem("Standard Deviation");
	JRadioButtonMenuItem miMeanError =			new JRadioButtonMenuItem("Standard Error of the Mean");
	JMenuItem miConfigureMuliPlots =			new JMenuItem("Configure MultiPlot Size");
	
	JMenuItem miFullMode = 						new JMenuItem("Full Mode");
	JMenuItem miLimitedMode =					new JMenuItem("Limited Mode");
	
	JMenuItem miOpenDefaultWorkspaceTrue = 		new JMenuItem("Set Open Default Workspace");
	JMenuItem miOpenDefaultWorkspaceFalse =		new JMenuItem("Always Promt for Workspace");
	
	ParameterMenu mnuParameter =				new ParameterMenu();
	
	CohortMenu mnuCohort = new CohortMenu();
	
	JComboBoxNoBorder<Perspectives> perspectiveBox = new JComboBoxNoBorder<>(
			Perspectives.values(),
			new IconListRenderer<>(Perspectives.getIconMap()));

	DataBrowsingMenu dataBrowserMenu = new DataBrowsingMenu();
	
	AnalizerBar analizerBar = new AnalizerBar();
	
	public ApplicationMenuBar()
	{
		add(mnuFile);
		mnuFile.add(dataBrowserMenu.miImportExcel);
		mnuFile.add(dataBrowserMenu.miRecentImportExcel);
		mnuFile.addSeparator();
		mnuFile.add(miSwitchWorkspace);
		mnuFile.add(miSaveEntireWorkSpace);
		
		add(dataBrowserMenu.mnuFilter);
		
		add(dataBrowserMenu.mnuPlotView);
		
		add(dataBrowserMenu.mnuMeasurement);
		
		add(mnuScript);

		JMenu analysisMenu = mAnalysis.getAnalysisMenu();
		add(analysisMenu);
		analysisMenu.addSeparator();
		analysisMenu.add(analizerBar.getUpdateMenu());
		add(analizerBar.getExportMenu());
		
		//add(mnuParameter); // TODO put this back in later
		add(mnOptions);
		mnOptions.add(miPreferences);
		mnOptions.add(miHelp);
		mnOptions.add(miAbout);
		mnOptions.add(michkUpdateAll);
		mnOptions.add(miConfigureMuliPlots);
		
		mnOptions.addSeparator();
		
		mnOptions.add(miFullMode);
		mnOptions.add(miLimitedMode);
		
		mnOptions.addSeparator();
		
		mnOptions.add(miOpenDefaultWorkspaceTrue);
		mnOptions.add(miOpenDefaultWorkspaceFalse);
		
		mnOptions.addSeparator();
		
		mnOptions.add(miMeanError);
		mnOptions.add(miStd);
		
		mnOptions.addSeparator();
		
		mnOptions.add(miConfigureMuliPlots);
		
		add(mnuCohort);
		
		add(Box.createHorizontalGlue());
		add(perspectiveBox);
		
	}
	
	public static void main(String[] args)
	{
		GUI.bar(new ApplicationMenuBar());
	}


	public void setMenuItemsWithNoActionListenersInactive()
	{
		GUITools.setMenuItemsWithNoActionListenersInactive(this);
	}

	public void limitedMode()
	{
		remove(mnuScript);
		remove(perspectiveBox);
		mnuFile.remove(miSwitchWorkspace);
		mnuFile.remove(miSaveEntireWorkSpace);
		mnOptions.remove(miLimitedMode);
	}


}
