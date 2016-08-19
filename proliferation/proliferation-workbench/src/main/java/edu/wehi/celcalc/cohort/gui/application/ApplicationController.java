package edu.wehi.celcalc.cohort.gui.application;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.wehi.celcalc.cohort.ApplicationPreferences;
import edu.wehi.celcalc.cohort.cohort.CohortAnalsysiController;
import edu.wehi.celcalc.cohort.data.Dataset;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.Filter;
import edu.wehi.celcalc.cohort.gui.analyizer.AnalizerController;
import edu.wehi.celcalc.cohort.gui.analysis.Analysis;
import edu.wehi.celcalc.cohort.gui.analysis.AnalysisController;
import edu.wehi.celcalc.cohort.gui.analysis.AnalysisModel;
import edu.wehi.celcalc.cohort.gui.analysis.AnalysisViewSyncListiner;
import edu.wehi.celcalc.cohort.gui.browser.DataBrowserController;
import edu.wehi.celcalc.cohort.gui.browser.DataBrowserModel;
import edu.wehi.celcalc.cohort.gui.parameterer.ParametererControler;
import edu.wehi.celcalc.cohort.gui.parameterer.ParametererModel;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterController;
import edu.wehi.celcalc.cohort.main.CohortMain;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.swing.FileDrop;

public class ApplicationController implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	public final static int defaultMultiWidth = 250;
	public final static int defaultMultiHeight = 200;
	public final static boolean defaultIsSTD = false;
	public final static String isSTD = "isSTD";
	
	
	final static String				txtName = "Cell Modeller";
	
	public static final transient Preferences prefs = Preferences.userRoot().node(ApplicationController.class.getName());

	ApplicationData 				model = new ApplicationData();
	ApplicationView 				view = new ApplicationView();

	final ScripterController 		scripterController;
	final DataBrowserController 	dataBrowserController;
	final AnalysisController		analysisController;
	final AnalizerController		analyizerController;
	final ParametererControler		parameterController;
	final CohortAnalsysiController	cohortAnalysisController;
	
	final ApplicationMenuBar		bar;
	
	final String workspaceDir;

	public ApplicationController(String workspaceDir)
	{
		this(workspaceDir, new ApplicationData(workspaceDir));
	}

	public void updateAnalizerDecorated()
	{
		if (isLiveUpdate() && !analysisController.isSettingValues()) analyizerController.resetView();
	}
	
	public static boolean isSTD()
	{
		return prefs.getBoolean(isSTD, defaultIsSTD);
	}
	
	public static void setIsSTD(boolean std)
	{
		prefs.putBoolean(isSTD, std);
	}
	
	public ApplicationController(String workspaceDir, ApplicationData model)
	{
		this.workspaceDir = workspaceDir;
		this.model = model;
		this.bar = view.getApplicationMenuBar();

		/**
		 * Initializing all the sub-controllers and integrate them with one another
		 * using the java anonymous class syntax
		 */
		
		scripterController = new ScripterController(view.scripterView, view.bar.mnuScript, workspaceDir, model.getScriptsContainter())
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void runScript()
			{
				setMeasurements(dataBrowserController.getSelectedMeasurements());
				super.runScript();
			}
			
			@Override
			public boolean saveScript(String scriptAbsPath, String scriptCode)
			{
				boolean result = super.saveScript(scriptAbsPath, scriptCode);
				analyizerController.resetView();
				analysisController.initGUI();
				return result;
			}
			
			@Override
			public JEditorPane addNewScriptPane(String scriptName, ActionListener al)
			{
				changePerspective(Perspectives.SCRIPTING);
				return super.addNewScriptPane(scriptName, al);
			}
			
			public boolean newScriptAction()
			{
				boolean result = super.newScriptAction();
				if (result == true)
				{
					changePerspective(Perspectives.SCRIPTING);
				}
				return result;
			}

		};		
		
		analysisController = new AnalysisController(view.getAnalysisPnl(), view.bar.mAnalysis, new AnalysisModel(
				model.getMeasurementListWithNoDuplicates(),
				model.getFilterContainter(),
				model.getPlotoriesContainer(),
				model.getScriptsContainter(), 
				model.getAnalysisContainer()),
				workspaceDir)
		{
			
			@Override
			public Analysis createNewAnalysis(String name)
			{
				Analysis result = super.createNewAnalysis(name);
				if (result != null)
				{
					changePerspective(Perspectives.ANALYSIS);
				}
				return result;
			}
			
			@Override
			public void createDefaultSuiteAction()
			{
				super.createDefaultSuiteAction();
				dataBrowserController.syncFilterTableKeepingSelections();
				changePerspective(Perspectives.ANALYSIS);
			}
			
			@Override
			public void analysisTableChanged()
			{
				updateAnalizerDecorated();
			}
			
			public String selectionChanged()
			{
				String result = super.selectionChanged();
				analyizerController.setSelectedPane(result);
				return result;
			}
			
		};
		
		MouseListener ml = new MouseAdapter()
		{
			
			public void mouseReleased(MouseEvent e)
			{
			    if (e.getClickCount() == 2)
			    {
			    	if (!analysisController.getView().getCheckboxTableFilters().isEnabled())
			    	{
			    		JOptionPane.showMessageDialog(
			    				view,
			    			    "This table is not editable until an analysis is selected.",
			    			    "Error",
			    			    JOptionPane.ERROR_MESSAGE);
			    		return;
			    	}
			    	JOptionPane.showMessageDialog(view,getFilterEditorView(analysisController.getSelectedRowName()),"Filter",JOptionPane.PLAIN_MESSAGE);
			    	dataBrowserController.syncView();
			    }
			}
		};
		
		analysisController.addDoubleClickListener(ml);
		
		
		analysisController.getView().addSyncListiner(new AnalysisViewSyncListiner()
		{
			@Override
			public void syncOccured()
			{
				if (isLiveUpdate())
				{
					updateAnalizerDecorated();
				}
			}});
		
		dataBrowserController = new DataBrowserController(
				new DataBrowserModel(
				model.getMeasurementListWithNoDuplicates(),
				model.getFilterContainter()),
				view.dataBrowsingView,
				view.bar.dataBrowserMenu)
		{
			@Override
			public void syncFilterTable()
			{
				super.syncFilterTable();
				analysisController.syncFilterTableKeepingSelections();
			}
			
			@Override
			public Filter createNewSelection(String name)
			{
				Filter result = super.createNewSelection(name);
				if (result != null)
				{
					changePerspective(Perspectives.DATABROWSING);
				}
				return result;
			}
			
			@Override
			public void controlerNotifiedOfNewMeasurements(List<Measurement> measurements)
			{
				if (measurements == null)
				{
					return;
				}
				if (measurements.size() == 0)
				{
					return;
				}
				changePerspective(Perspectives.DATABROWSING);
				super.controlerNotifiedOfNewMeasurements(measurements);
			}
			
			@Override
			public boolean importFile(File file)
			{
				boolean result = super.importFile(file);
				if (result == true && isLimitedMode)
				{
					analysisController.createDefaultSuiteAction();
					analyizerController.resetView();
				}
				return result;
			}			
			
		};
		
		analyizerController = new AnalizerController(view.analizerPanel, view.bar.analizerBar, model)
		{
			@Override
			public JFrame getJFrame()
			{
				return view;
			}
			
			@Override
			public void resetView()
			{
				String title = view.getTitle();
				view.setTitle("!!!!!!!!!!!!!!!!!!!!!! ----BUSY----- !!!!!!!!!!!!!!!!!!!!");
				view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				view.revalidate();
				view.repaint();
				
				view.setExtendedState(JFrame.MAXIMIZED_BOTH);
				
				view.revalidate();
				view.repaint();
				
				super.resetView();
				view.setTitle(title);
				view.setCursor(Cursor.getDefaultCursor());
				
				view.revalidate();
				view.repaint();
			}
			
		};
		
		cohortAnalysisController = new CohortAnalsysiController(bar.mnuCohort)
		{
			
			@Override
			public void showCohortAnalysis(JPanel pnlTrio, JPanel pnlCellNumVsDiv, JPanel pnlCohortVsDiv)
			{
				
				javax.swing.SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						
						if (!frameTrio.isVisible()) 			frameTrio.setExtendedState(JFrame.MAXIMIZED_BOTH);
						if (!frameCellNumVsDiv.isVisible()) 	frameCellNumVsDiv.setExtendedState(JFrame.MAXIMIZED_BOTH);
						if (!framepnlCohortVsDiv.isVisible()) 	framepnlCohortVsDiv.setExtendedState(JFrame.MAXIMIZED_BOTH);
						
						frameTrio.setVisible(true);
						frameCellNumVsDiv.setVisible(true);
						framepnlCohortVsDiv.setVisible(true);
					}});
				super.showCohortAnalysis(pnlTrio, pnlCellNumVsDiv, pnlCohortVsDiv);
			}
			
			public List<Measurement> getMeasurements()
			{
				return model.getMeasurementListWithNoDuplicates();
			}

			public Dataset getDataset()
			{
				return model.getDataset();
			}
			
			@Override
			public List<String> getTreatments()
			{
				return dataBrowserController.getAllTreatments();
			}

			@Override
			public JPanel getPnlTrio()
			{
				return pnlTrio;
			}

			@Override
			public JPanel getPnlCellNumVsDiv()
			{
				return pnlCellNumVsDiv;
			}

			@Override
			public JPanel getPnlCohortVsDiv() 
			{
				return pnlCohortVsDiv;
			}
		};
		
		parameterController = new ParametererControler(new ParametererModel(model.getInputParameters()), view.getParameterView(), bar.mnuParameter)
		{
			// TODO - handle integration with broader application here
		};

		// http://stackoverflow.com/questions/286727/java-keylistener-for-jframe-is-being-unresponsive
		InputMap inputMap  = view.getMainPnl().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = view.getMainPnl().getActionMap();

		AbstractAction action    = new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				scripterController.saveAllScripts();
				scripterController.saveCurrentScript();
				analyizerController.resetView();
			}
		};

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "dispose");
		actionMap.put("dispose", action);
			
		view							.addWindowListener(new ClosingAdapter());
		bar.perspectiveBox				.addActionListener(e -> changePerspective((Perspectives) view.bar.perspectiveBox.getSelectedItem()));
		bar.miSaveEntireWorkSpace		.addActionListener(e -> saveEntireWorkspace());
		bar.miFullMode					.addActionListener(e -> fullModeAction());
		bar.miLimitedMode				.addActionListener(e -> limitedActionMode());
		bar.miOpenDefaultWorkspaceFalse	.addActionListener(e -> defaultWorkspaceFalse());
		bar.miOpenDefaultWorkspaceTrue	.addActionListener(e -> defaultWorkspaceFalse());
		bar.michkUpdateAll				.addActionListener(e -> setLiveUpdates(bar.michkUpdateAll.isSelected()));
		bar.miAbout						.addActionListener(e -> showAboutDialog());
		bar.miConfigureMuliPlots		.addActionListener(e -> doMultiSizeConfig());
		bar.miMeanError					.addActionListener(e -> doErrorAction());
		bar.miStd						.addActionListener(e -> doErrorAction());
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(bar.miMeanError);
		bg.add(bar.miStd);

		initGUI();
	}
	
	JPanel pnlTrio = new JPanel();
	JPanel pnlCellNumVsDiv = new JPanel();
	JPanel pnlCohortVsDiv = new JPanel();
	
	JFrame frameTrio = new JFrame("Cohort Analysis"){

		private static final long serialVersionUID = 1L;

		{
			add(pnlTrio);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}
		
	};
	
	JFrame frameCellNumVsDiv = new JFrame("Population Vs. Division") {
		
		private static final long serialVersionUID = 1L;
		
		{
			add(pnlCellNumVsDiv);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}
	};
	
	JFrame framepnlCohortVsDiv = new JFrame("Cohort Vs. Division") {
		
		private static final long serialVersionUID = 1L;
		
		{
			add(pnlCohortVsDiv);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}
	};
	
	final static String isLiveUpdatePref = "isLiveUpdate";
	final static String widthMulti = "widthMulti";
	final static String hiehgtMulti = "hiehgtMulti";
	
	public void setLiveUpdates(boolean isLiveUpdate)
	{
		prefs.putBoolean(isLiveUpdatePref, isLiveUpdate);
		isLiveUpdate = isLiveUpdate();
		updateAnalizerDecorated();
	}
	
	public void doErrorAction()
	{
		prefs.putBoolean(isSTD, bar.miStd.isSelected());
		GPXYSeries.isSTD = isSTD();
	}
	
	public boolean isLiveUpdate()
	{
		return prefs.getBoolean(isLiveUpdatePref, true);
	}
	
	public void defaultWorkspaceFalse()
	{
		ApplicationPreferences.prefs.putBoolean(ApplicationPreferences.openDefaultPref, false);
	}
	
	public void defaultWorkspaceTrue()
	{
		ApplicationPreferences.prefs.putBoolean(ApplicationPreferences.openDefaultPref, true);
	}
	
	private void limitedActionMode()
	{
		if (isLimitedMode)
		{
			JOptionPane.showMessageDialog(view, "Already in limited mode");
			return;
		}
		
		if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(view, "Do you want to change to limited Mode? (Note: you may want to save the workspace before pressing ok)", "Closing", JOptionPane.YES_NO_OPTION))
		{
			ApplicationPreferences.prefs.putBoolean(ApplicationPreferences.isLimitedMode, true);
			dispose();
			CohortMain.main(new String[]{});
			return;
		}
	}

	private void fullModeAction()
	{
		if (!isLimitedMode)
		{
			JOptionPane.showMessageDialog(view, "Already in full mode");
			return;
		}
		
		if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(view, "Do you want to change to full Mode?", "Closing", JOptionPane.YES_NO_OPTION))
		{
			ApplicationPreferences.prefs.putBoolean(ApplicationPreferences.isLimitedMode, false);
			dispose();
			CohortMain.main(new String[]{});
			return;
		}
	}

	private boolean saveEntireWorkspace()
	{
		boolean isSuccessfull = true;
		isSuccessfull &= dataBrowserController.saveMeasurements();
		isSuccessfull &= dataBrowserController.saveFilters();
		isSuccessfull &= analysisController.saveAnalysis();
		isSuccessfull &= scripterController.saveAllScripts(); 
		System.out.println("Workspace saved successfully: "+isSuccessfull);
		return isSuccessfull;
	}

	public Component getFilterEditorView(String filterName)
	{
		FilterView filterView = new FilterView();
		filterView.syncTreatments(dataBrowserController.getAllTreatments());
		filterView.sync(dataBrowserController.getFilter(filterName));
		filterView.setEnabledControls(true);
		return filterView;
	}

	private void changePerspective(Perspectives perspective)
	{
		if (perspective == Perspectives.SCRIPTING)
		{
			scripterController.setMeasurements(model.getMeasurementListWithNoDuplicates());
		}
		if (perspective == Perspectives.ANALYSIS)
		{
			analysisController.trySelectDefaultRow();
		}
		view.setPerspective(perspective);
	}

	public void initGUI()
	{
		view.bar.setMenuItemsWithNoActionListenersInactive();
		view.bar.michkUpdateAll.setSelected(isLiveUpdate());
		view.revalidate();
		view.repaint();
		new FileDrop(view, new FileDrop.Listener()
		{
			public void filesDropped(java.io.File[] files)
			{
				for (File f : files) 
				{
					dataBrowserController.importFile(f);
				}
			}
		});
		dataBrowserController.initGUI();
		analysisController.initGUI();
		
		view.setTitle(txtName+":\t"+workspaceDir);
		setMode();
		view.requestFocus();
		view.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		bar.miMeanError.setSelected(!isSTD());
		bar.miStd.setSelected(isSTD());
		GPXYSeries.isSTD = isSTD();
	}
	
	private void setMode()
	{
		if (ApplicationPreferences.prefs.getBoolean(ApplicationPreferences.isLimitedMode, true))
		{
			limitedMode();
		}
		
	}

	boolean isLimitedMode = false;
	public void limitedMode()
	{
		isLimitedMode = true;
		view.limitedMode();
		dataBrowserController.limitedMode();
		analysisController.limitedMode();
		view.setTitle(txtName+":\t"+"LIMITED MODE - Note: not all features available: to change go to options and select full mode!");
	}

	public void exitingAction() 
	{
		if (isLimitedMode)
		{
			view.dispose();
			System.exit(0);
		}
		
		int response = showSaveBeforeExitDialog();
		if (response == JOptionPane.NO_OPTION) 
		{
			view.dispose();
			System.exit(0);
		}
		else if (response == JOptionPane.CANCEL_OPTION ||
				 response == JOptionPane.CLOSED_OPTION)
		{
			return;
		}
		else if (response == JOptionPane.YES_OPTION)
		{
			saveEntireWorkspace();
			view.dispose();
			System.exit(0);
		}
	}

	protected void showExitingDialog() 
	{
		int confirmed = showConfirmClosingDialogPlain();
		if (confirmed == JOptionPane.YES_OPTION) 
		{
			view.dispose();
			System.exit(0);
		}
		else if (confirmed == JOptionPane.NO_OPTION)
		{
			return;
		}
	}

	public void dispose()
	{
		this.view.dispose();
		this.view = null;
		this.model.dipose();
		this.model = null;
	}

	void showOpenError()		{		JOptionPane.showMessageDialog(view, "Could not open.", 				"Open Error",		JOptionPane.ERROR_MESSAGE);	}
	void showSaveError()		{		JOptionPane.showMessageDialog(view, "Could not save.", 				"Save Error",		JOptionPane.ERROR_MESSAGE);	}
	void showFileImportError() 	{		JOptionPane.showMessageDialog(view, "Could not import the file", 	"Input Error", 		JOptionPane.ERROR_MESSAGE);	}
	void showCreateError() 		{		JOptionPane.showMessageDialog(view, "Could not create filter",		"Creation Error", 	JOptionPane.ERROR_MESSAGE); }

	int showSaveBeforeExitDialog() {		return JOptionPane.showConfirmDialog(view, "Do you want to save the workspace before closing?",					"Closing",					JOptionPane.YES_NO_OPTION);	}
	int showConfirmClosingDialogPlain() {	return JOptionPane.showConfirmDialog(view, "Are you sure you want to exit the program?",						"Exit Program Message Box", JOptionPane.YES_NO_OPTION);}	

	private class ClosingAdapter extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			exitingAction();
		}
	}

	public JFrame getView()
	{
		return view;
	}

	public void setVisible(boolean isVisible) 
	{
		view.setVisible(isVisible);
	}

	public static void main(String[] args)
	{
		final ApplicationController controller = new ApplicationController(null);
		final JFrame frame = controller.getView();
		frame.setSize(600, 600);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				frame.setVisible(true);
			}
		});
	}

	public void evaluateAnalysis()
	{
		analyizerController.resetView();
	}
	
	public void showAboutDialog()
	{
		JOptionPane.showMessageDialog(view,"Version: "+ getClass().getPackage().getImplementationVersion());
	}
	
	public static int getMultiWidth()
	{
		return prefs.getInt(widthMulti, defaultMultiWidth);
	}
	
	public static int getMultiHeight()
	{
		return prefs.getInt(hiehgtMulti, defaultMultiHeight);
	}
	
	public void doMultiSizeConfig()
	{
		bar.miConfigureMuliPlots.isSelected();
		
		SizeConfigurationPanel pnl = new SizeConfigurationPanel();
		
		pnl.setWidthInput(getMultiWidth()+"");
		pnl.setHeightInput(getMultiHeight()+"");
		
		
		int option = JOptionPane.showConfirmDialog(view, pnl, "Set Size of Multi Plots",JOptionPane.PLAIN_MESSAGE);
		
		if (option == JOptionPane.OK_OPTION)
		{
			String width = pnl.getWidthInput();
			String height = pnl.getHeightInput();
			
			try
			{
				int w = Integer.parseInt(width);
				int h = Integer.parseInt(height);
				
				prefs.putInt(widthMulti, w);
				prefs.putInt(hiehgtMulti, h);
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(view, "Error, Integer values needed.");
			}
		}
		
		
		
	}

}
