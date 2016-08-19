package edu.wehi.celcalc.cohort.gui.browser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.MeasurementTableModel;
import edu.wehi.celcalc.cohort.gui.application.FilterListener;
import edu.wehi.celcalc.cohort.gui.application.FilterView;
import edu.wehi.celcalc.cohort.gui.application.MeasurementTable;
import edu.wehi.swing.checkboxtable.CheckBoxTable;

public class DataBrowserView extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	public DataBrowserView()
	{
		super(new BorderLayout());
		addAllcomponents();
	}
	
	public void addAllcomponents()
	{
		add(getMainSplitPane(), BorderLayout.CENTER);
	}

	JPanel selectionAndImportTableContainer = new JPanel(new BorderLayout());
	
	JPanel pnlImportExcel = null;
	private Component getTopLeft()
	{
		if (pnlImportExcel == null)
		{
			pnlImportExcel = new JPanel();
			pnlImportExcel.setLayout(new BorderLayout());
			pnlImportExcel.add(selectionAndImportTableContainer, BorderLayout.CENTER);
			pnlImportExcel.setMinimumSize(new Dimension(300,20));
		}
		return pnlImportExcel;
	}
	
	
	FilterView filterView = new FilterView();
	
	JSplitPane pnImportSelectionsAndQuery = null;
	JSplitPane getTopComponent()
	{
		if (pnImportSelectionsAndQuery == null)
		{
			pnImportSelectionsAndQuery = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			pnImportSelectionsAndQuery.setLeftComponent(getTopLeft());
			pnImportSelectionsAndQuery.setRightComponent(filterView);
			pnImportSelectionsAndQuery.setDividerLocation(0.5);
		}
		return pnImportSelectionsAndQuery; 
	}
	
	JPanel pnlSummaryStats = null;
	JPanel getSummaryStatsPanel()
	{
		if (pnlSummaryStats == null)
		{
			pnlSummaryStats = new JPanel(new BorderLayout());
			pnlSummaryStats.setMinimumSize(new Dimension(100, 80));
		}
		return pnlSummaryStats;
	}
	
	MeasurementTable tableMeasurementTable = new MeasurementTable();
	
	JSplitPane btmLeft = null;
	Component getBottomComponent()
	{
		if (btmLeft == null)
		{
			btmLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			btmLeft.setTopComponent(getSummaryStatsPanel());
			btmLeft.setBottomComponent(new JScrollPane(tableMeasurementTable));
		}
		
		return btmLeft;
	}
	
	
	JSplitPane pnImportFilterSelectionTable = null;
	Component getMainSplitPane()
	{
		if (pnImportFilterSelectionTable == null)
		{
			pnImportFilterSelectionTable = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			pnImportFilterSelectionTable.setTopComponent(getTopComponent());
			pnImportFilterSelectionTable.setBottomComponent(getBottomComponent());
			pnImportFilterSelectionTable.setDividerLocation(0.5);
		}
		return pnImportFilterSelectionTable;
	}
	
	public void setSelectedMeasurement(Measurement mes)
	{
		tableMeasurementTable.setSelectedMeasurement(mes);
		tableMeasurementTable.revalidate();
		tableMeasurementTable.repaint();
	}
	
	
	Set<FilterListener> filterListeners = new HashSet<>();
	
	public void addFilterRowSelectionListener(FilterListener l)
	{
		filterListeners.add(l);
	}
	
	public void setMeasurementTableModel(MeasurementTableModel model)
	{
		tableMeasurementTable.setMeasurementTableModel(model);
		revalidate();
		repaint();
	}
	
	TableModelListener selectionTableModelListener = null;
	public void addSelectionTableModelListener(TableModelListener listSelectionListener)
	{
		this.selectionTableModelListener = listSelectionListener;
	}

	public void addMeasurementTableMouseListener(MouseListener mouselistener)
	{
		tableMeasurementTable.addMouseListener(mouselistener);
	}
	
	public MeasurementTable getMeasurementTable()
	{
		return tableMeasurementTable;
	}
	
	public void setSelectedSelectionsRow(String name)
	{
		selectionAndImportTable.setSelectedRow(name);
	}
	
	
	
	public void setSelectedSelections(String name)
	{
		List<String> selections = new ArrayList<>();
		selections.add(name);
		setSelectedFilters(selections);
	}
	
	public void setFilteredText(String text)
	{
		filterView.setFilteredText(text);
	}
	
	public void clearAllFilters()
	{
		filterView.resetView();
	}

	
	public void addTreatmentsControlListiner(MouseListener actionListener)
	{
		filterView.selectTreatmentsListener(actionListener);
	}

	public void setTreatmentOptions(List<String> allTreatments)
	{
		filterView.syncTreatments(allTreatments);
	}
	
	public void setSelectedFilters(List<String> previousSelections)
	{
		this.selectionAndImportTable.getCheckBoxTableModel().setSelectedValues(previousSelections);
	}
	
	public void clearStatistics()
	{
		setStatisticsPanel(new JPanel());
	}

	public void addOptionsChangedListener(ActionListener al) 
	{
		this.filterView.addInputChangedActionListener(al);
	}
	
	CheckBoxTable selectionAndImportTable = null;
	
	
	
	public void setSelectionAndImportsTable(CheckBoxTable selectionTable)
	{
		this.selectionAndImportTable = selectionTable;
		selectionAndImportTableContainer.removeAll();
		selectionAndImportTableContainer.add(new JScrollPane(selectionTable), BorderLayout.CENTER);
		selectionTable.getCheckBoxTableModel().addTableModelListener(selectionTableModelListener);
		
		selectionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent arg0)
			{
				for (FilterListener l : filterListeners)
				{
					l.newSelectedFilter(selectionAndImportTable.getHighlightedRow());
				}
			}
		});
		revalidate();
		repaint();
	}

	
	public List<String> getSelectedFilters()
	{
		if (selectionAndImportTable == null)
		{
			return new ArrayList<>();
		}
		return selectionAndImportTable.getSelectedRowNames();
	}
	
	public void setStatisticsPanel(Component comp)
	{
		pnlSummaryStats.removeAll();
		pnlSummaryStats.add(comp, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	public static void main(String[] args)
	{
		GUI.gui(new DataBrowserView());
	}

	public void setAllFiltersSelected()
	{
		this.selectionAndImportTable.setSelected(true);
	}
	
}
