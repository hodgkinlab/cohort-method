package edu.wehi.celcalc.cohort.gui.analysis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.runner.RunnerView;
import edu.wehi.swing.MultiSplitPane;
import edu.wehi.swing.checkboxtable.CheckBoxTable;

public class AnalysisView extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	CheckBoxTable chbtableFilters = new CheckBoxTable(new String[]{});
	CheckBoxTable chbtableScripts = new CheckBoxTable(new String[]{});
	
	JTable tableAnalysis = new JTable();
	JScrollPane spTable = new JScrollPane(tableAnalysis);

	JPanel pnlScripts = new JPanel(new BorderLayout());
	
	public AnalysisView()
	{
		super(new BorderLayout());
		
		MultiSplitPane sp = new MultiSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		pnlFilterPanel.setMinimumSize(		new Dimension(30, 140));
		pnlScripts.setMinimumSize(			new Dimension(30, 203));
		pnlRunners.setMinimumSize(			new Dimension(30, 128));
		pnlRunners.setMinimumSize(			new Dimension(30, 128));
		spTable.setMinimumSize(				new Dimension(30, 444));

		sp.addComp(pnlFilterPanel);
		sp.addComp(pnlScripts);
		sp.addComp(pnlRunners);
		sp.addComp(spTable);
		add(sp, BorderLayout.CENTER);
	}
	
	JTable getAnalysisTable()
	{
		return tableAnalysis;
	}
	
	public CheckBoxTable getCheckboxTableFilters()
	{
		return chbtableFilters;
	}

	JPanel pnlFilterPanel = new JPanel(new BorderLayout()){
		
		private static final long serialVersionUID = 1L;
		
		{
			setMinimumSize(new Dimension(100, 80));
		}
	};
	
	RunnerView pnlRunners = new RunnerView(){

		private static final long serialVersionUID = 1L;
		
		public void inputValuesChange()
		{
			super.inputValuesChange();
			updateSyncListeners();
		}
	};

	public List<String> getSelectedFilters()
	{
		if (chbtableFilters == null)
		{
			return new ArrayList<>();
		}
		return chbtableFilters.getSelectedRowNames();
	}

	public void setSelectedFilters(List<String> previousSelections)
	{
		this.chbtableFilters.getCheckBoxTableModel().setSelectedValues(previousSelections);
	}

	MouseListener mouseListener;
	
	public void setFilterTable(CheckBoxTable selectionTable)
	{
		boolean isEnabled = chbtableFilters.isEnabled();
		chbtableFilters = selectionTable;
		chbtableFilters.setEnabled(isEnabled);
		pnlFilterPanel.removeAll();
		pnlFilterPanel.add(new JScrollPane(selectionTable), BorderLayout.CENTER);
		pnlFilterPanel.revalidate();
		pnlFilterPanel.repaint();
		chbtableFilters.addMouseListener(mouseListener);
		chbtableFilters.addCheckboxTableListener(e -> sync());
	}

	public void addSyncListiner(AnalysisViewSyncListiner l)
	{
		analysisViewSynclistiners.add(l);
	}
	
	Set<AnalysisViewSyncListiner> analysisViewSynclistiners = new HashSet<>(); 
	
	private void sync()
	{
		if (analysis == null)
		{
			return;
		}
		
		if (chbtableFilters != null)
		{
			this.analysis.setFilters(new HashSet<String>(chbtableFilters.getCheckBoxTableModel().getSelectedRowNames()));
		}
		
		if (chbtableScripts != null)
		{
			this.analysis.setScripts(new HashSet<String>(chbtableScripts.getCheckBoxTableModel().getSelectedRowNames()));
		}
		updateSyncListeners();
	}

	public List<String> getSelectedScripts()
	{
		if (chbtableScripts == null)
		{
			return new ArrayList<>();
		}
		return chbtableScripts.getSelectedRowNames();
	}
	
	public void setSelectedScripts(List<String> selectedScripts)
	{
		this.chbtableScripts.getCheckBoxTableModel().setSelectedValues(selectedScripts);
	}
	
	MouseListener scriptMouseListener = null;
	
	public void setScriptMouseListener(MouseListener scriptMouseListener)
	{
		this.scriptMouseListener = scriptMouseListener;
		if (chbtableScripts != null ) chbtableScripts.addMouseListener(scriptMouseListener);
	}

	public void setScriptTable(CheckBoxTable scriptTable)
	{
		chbtableScripts = scriptTable;
		pnlScripts.removeAll();
		pnlScripts.add(new JScrollPane(chbtableScripts), BorderLayout.CENTER);
		pnlScripts.revalidate();
		pnlScripts.repaint();
		chbtableScripts.addCheckboxTableListener(e ->  sync());
		if (scriptMouseListener != null) chbtableScripts.addMouseListener(scriptMouseListener);
	}

	public void setAnalysisTableModel(AbstractTableModel analysisContainer)
	{
		tableAnalysis.getModel().addTableModelListener(new TableModelListener()
		{
			@Override
			public void tableChanged(TableModelEvent arg0)
			{
				updateTableListeners();
			}
		});
		tableAnalysis.setModel(analysisContainer);
		tableAnalysis.revalidate();
		tableAnalysis.repaint();
	}
	
	public void addAnalysisTableListeners(AnalysisTableListeners listener)
	{
		analysisTableListeners.add(listener);
	}

	Set<AnalysisTableListeners> analysisTableListeners = new HashSet<>();
	public void updateTableListeners()
	{
		analysisTableListeners.forEach(a -> a.analysisTablesChanged());
	}
	
	boolean isSettingValues = false;
	
	public boolean isSettingValues()
	{
		return isSettingValues;
	}
	
	Analysis analysis = null;
	public void sync(Analysis analysis)
	{
		if (analysis == null)
		{
			setEnabledControls(false);
			return;
		}
		else
		{
			isSettingValues = true;
			this.analysis = analysis;
			setEnabledControls(true);
			setValues(analysis);
			isSettingValues = false;
		}
	}

	private void setValues(Analysis analysis2)
	{		
		if (chbtableScripts != null)
		{
			chbtableScripts.setSelectedNoFireChange(false);
			if (analysis2.getScripts() != null)
			{
				chbtableScripts.setSelectedNoFireChange(new ArrayList<String>(analysis2.getScripts()), true);
			}	
		}
		
		if (chbtableFilters != null)
		{
			chbtableFilters.setSelectedNoFireChange(false);
			if (analysis2.getFilters() != null)
			{
				chbtableFilters.setSelectedNoFireChange(new ArrayList<String>(analysis2.getFilters()), true);
			}
		}
		
		pnlRunners.sync(analysis2.getRunner());
	}
	
	public void updateSyncListeners()
	{
		analysisViewSynclistiners.forEach(l -> l.syncOccured());
	}

	public void setEnabledControls(boolean b)
	{
		chbtableFilters.setEnabled(b);
		chbtableScripts.setEnabled(b);
	}

	public int getSelectedAnalysisRow()
	{
		return tableAnalysis.getSelectedRow();
	}

	public void chbtableFiltersaddMouseListener(MouseListener al)
	{
		mouseListener = al;
		chbtableFilters.addMouseListener(al);
	}
	
	public static void main(String[] args)
	{
		GUI.gui(new AnalysisView());
	}

}
