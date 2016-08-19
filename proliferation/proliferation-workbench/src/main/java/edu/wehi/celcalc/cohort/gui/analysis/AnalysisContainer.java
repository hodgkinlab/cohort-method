package edu.wehi.celcalc.cohort.gui.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class AnalysisContainer extends AbstractTableModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public AnalysisContainer()
	{
		
	}
	
	
	
	List<Analysis> analysiss = new ArrayList<>();
	
	public Collection<Analysis> getAllAnalysiss()
	{
		return analysiss;
	}
	
	public boolean add(Analysis analysis)
	{
		if (analysiss.stream().anyMatch(a -> a.getName().equals(analysis.getName())))
		{
			return false;
		}
		boolean result = analysiss.add(analysis);
		fireTableDataChanged();
		return result;
	}
	
	@Override
	public int getColumnCount()
	{
		return 7;
	}

	@Override
	public int getRowCount() 
	{
		return analysiss.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Analysis analysis = analysiss.get(rowIndex);
		
		switch(columnIndex)
		{
		case 0:
			return analysis.getName();
		case 1:
			return analysis.getxAxis();
		case 2:
			return analysis.getyAxis();
		case 3:
			return analysis.getTitle();
		case 4:
			return analysis.isLog();
		case 5:
			return analysis.getGroup();
		case 6:
			return analysis.isShowing();
		default:
			return null;
		}
	}
	
	@Override
	public String getColumnName(int i)
	{
		switch(i)
		{
		case 0:
			return "Name";
		case 1:
			return "x-axis";
		case 2:
			return "y-axis";
		case 3:
			return "title";
		case 4:
			return "log";
		case 5:
			return "group";
		case 6:
			return "on";
		default:
			return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col)
	{
		Analysis analysis = analysiss.get(row);
		
		switch(col)
		{
		case 0:
			analysis.setName((String)value);
			break;
		case 1:
			analysis.setxAxis((String)value);
			break;
		case 2:
			analysis.setyAxis((String)value);
			break;
		case 3:
			analysis.setTitle((String)value);
			break;
		case 4:
			analysis.setLog((Boolean)value);
			break;
		case 5:
			analysis.setGroup((String)value);
			break;
		case 6:
			analysis.setShowing((Boolean)value);
			break;
		default:
			break;
		}
		fireTableCellUpdated(row, col);
	}
	
	@Override
	public Class<?> getColumnClass(int col)
	{
		switch(col)
		{
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return Boolean.class;
		case 5:
			return String.class;
		case 6:
			return Boolean.class;
		default:
			return null;
		}
	}

	public Analysis get(int row)
	{
		if (row < 0)
		{
			return null;
		}
		return analysiss.get(row);
	}

	public boolean addAll(Collection<Analysis> analysis) 
	{
		for (Analysis a : analysis)
		{
			for (Analysis a2 : analysiss)
			{
				if (a.getName().equals(a2.getName()))
				{
					return false;
				}
			}
		}
		return analysiss.addAll(analysis);
	}

}
