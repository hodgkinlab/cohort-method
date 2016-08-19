package edu.wehi.celcalc.cohort.gui.analysis;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wehi.swing.checkboxtable.CheckBoxTable;
import edu.wehi.swing.checkboxtable.CheckboxTableListener;

public class AnalysisController
{
	private final AnalysisView view;
	private final AnalysisModel model;
	private final AnalysisMenu mnu;
	private final String workspacePath;
	
	public AnalysisView getView()
	{
		return view;
	}
	
	public AnalysisController(
			AnalysisView view,
			AnalysisMenu mnu,
			AnalysisModel model,
			String workspacePath)
	{
		super();
		this.view = view;
		this.model = model;
		this.workspacePath = workspacePath;
		this.mnu = mnu;
		
		model.initFromWorkspace(workspacePath);
	
		mnu.mniNewAnalysis.			addActionListener(e -> newAnalysisAction());
		mnu.mniSaveAnalysis.		addActionListener(e -> saveAnalysisAction());
		mnu.mniOpenAnalysis.		addActionListener(e -> openAnalysisAction());
		mnu.mniCreateDefaultSuite.	addActionListener(e -> createDefaultSuiteAction());
		
		view.tableAnalysis.getSelectionModel().addListSelectionListener(new SelectionChanged());
		view.setScriptMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent event)
			{
				if (event.getClickCount() == 2)
				{
					String script = view.chbtableScripts.getHighlightedRow();
					model.showDoc(script);
				}
			}
		});
		
		view.addAnalysisTableListeners(new AnalysisTableListeners() {
			
			@Override
			public void analysisTablesChanged() {
				analysisTableChanged();
			}
		});
		
	}
	
	public void analysisTableChanged()
	{
		// method to be overriden if needed
	}

	public void createDefaultSuiteAction()
	{
		model.createcreateDefaultSuite();
		syncView();
	}
	
	public void saveAnalysisAction()
	{
		boolean isSaved = saveAnalysis();
		if (!isSaved)
		{
			JOptionPane.showMessageDialog(view,
				    "Could not save Measurements.",
				    "Failed to Save.",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public boolean saveAnalysis()
	{
		return model.saveAllAnalysis(workspacePath);
	}
	
	public void openAnalysisAction()
	{
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.showOpenDialog(view);
		File[] files = fc.getSelectedFiles();
		boolean isSuccessfull = openAnalysis(files);
		if (!isSuccessfull)
		{
			JOptionPane.showMessageDialog(view,
				    "Could not open Analysis.",
				    "Failed to Analysis.",
				    JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			syncView();
		}
	}
	
	public boolean openAnalysis(File[] files)
	{
		return model.openAnalysis(files);
	}
	
	class UpdateAnalysisAction implements CheckboxTableListener
	{
		@Override
		public void notified(Map<String, Boolean> values)
		{
			Analysis analysis = model.getAnalysisContainer().get(view.tableAnalysis.getSelectedRow());
			if (analysis == null)
			{
				return;
			}	
		}	
	}
	
	public String selectionChanged()
	{
		int row = view.getSelectedAnalysisRow();
		Analysis analysis = model.getAnalysisContainer().get(row);
		view.sync(analysis);
		if (analysis == null)
		{
			return null;
		}
		return analysis.getGroup();
	}
	
	class SelectionChanged implements ListSelectionListener
	{

		@Override
		public void valueChanged(ListSelectionEvent ev)
		{
			selectionChanged();
		}
		
	}
	
	public void initGUI()
	{
		syncView();
		view.setAnalysisTableModel(model.getAnalysisContainer());
		view.setEnabledControls(false);
	}
	
	public void syncView()
	{
		syncFilterTableKeepingSelections();
		syncScriptsTableKeepingSelections();
	}
	
	private void syncScriptsTableKeepingSelections() 
	{
		List<String> selectedScripts = view.getSelectedScripts();
		syncScriptsTable();
		view.setSelectedScripts(selectedScripts);
	}


	private void syncScriptsTable()
	{
		List<String> selectionScripts = new ArrayList<>(model.getScriptNames());
		CheckBoxTable scriptTable = new CheckBoxTable(selectionScripts,"script", "on");
		view.setScriptTable(scriptTable);
	}


	public void syncFilterTableKeepingSelections()
	{
		List<String> selectedFilters = view.getSelectedFilters();
		syncFilterTable();
		view.setSelectedFilters(selectedFilters);
	}
	

	public void syncFilterTable()
	{
		List<String> selectionNames = new ArrayList<>(model.getSelectionNames());
		CheckBoxTable selectionTable = new CheckBoxTable(selectionNames,"filter", "on");
		view.setFilterTable(selectionTable);
	}


	public void newAnalysisAction()
	{
		String name = showNewAnalysisDailog();
		if (name != null)
		{
			createNewAnalysis(name);
		}
	}
	
	public Analysis createNewAnalysis(String name)
	{
		return model.createNewAnalysis(name);
	}
	
	String showNewAnalysisDailog() {return (String) JOptionPane.showInputDialog(view,  "Enter in unqiue name for analysis.", "Create new Analysis", JOptionPane.PLAIN_MESSAGE, null, null, "new_analysis");}

	public void addDoubleClickListener(MouseListener al)
	{
		view.chbtableFiltersaddMouseListener(al);
	}

	public String getSelectedRowName()
	{
		int selectedRow = view.chbtableFilters.getSelectedRow();
		return view.chbtableFilters.getCheckBoxTableModel().rowList.get(selectedRow).name;
	}

	public void limitedMode()
	{
		mnu.limitedMode();
	}
	
	public boolean isSettingValues()
	{
		return view.isSettingValues();
	}

	public void trySelectDefaultRow()
	{
		try
		{
			if (view.getAnalysisTable().getSelectionModel().getMinSelectionIndex() == -1)
			{
				view.getAnalysisTable().getSelectionModel().setSelectionInterval(0, 0);
			}
		}
		catch (Exception e){}
	}
	

}
