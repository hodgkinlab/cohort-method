package edu.wehi.swing.checkboxtable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

public class CheckBoxTableModel extends AbstractTableModel
{
	
	private static final long serialVersionUID = 1L;

	public final List<Row> rowList;
	

	
	public CheckBoxTableModel(String[] rows)
	{
		this(Arrays.asList(rows),"tag", "on");
	}
	
	final String col1;
	final String col2;
	public CheckBoxTableModel(List<String> rows, String col1, String col2)
	{
		rowList = new ArrayList<>();
		rows.forEach(r -> rowList.add(new Row(r)));
		this.col1 = col1;
		this.col2 = col2;
	}
	
	@Override
	public int getRowCount() {
		return rowList.size();
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public String getColumnName(int col)
	{
		switch(col)
		{
		case 0: return col1;
		case 1: return col2;
		}
		return "Column " + col;
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		if (col == 0) {
			return rowList.get(row).name;
		} else {
			return rowList.get(row).isSelected;
		}
	}

	@Override
	public Class<?> getColumnClass(int col)
	{
		return getValueAt(0, col).getClass();
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return col == 1 && isEdditingEnabled;
	}
	
	@Override
	public void setValueAt(Object aValue, int row, int col)
	{
		if (col == 1)
		{
			Boolean isSelected = (Boolean) aValue;
			rowList.get(row).isSelected = isSelected;
			fireTableRowsUpdated(row, row);
		}
		
	}
	
	public List<String> getSelectedRowNames()
	{
		return rowList.stream().filter(r -> r.isSelected).map(t -> t.name).collect(Collectors.toList());
	}
	
	public void setSelectedValues(List<String> rows)
	{
		for (Row row : rowList)
		{
			rows.stream().forEach(r -> {
				if (r.equals(row.name))
				{
					row.isSelected = true;
				}
			});
		}
		fireTableDataChanged();
	}
	
	public static class Row 
	{
		public final String name;
		public Boolean isSelected;
		
		public Row(String name, Boolean iSelected)
		{
			this.name = name;
			this.isSelected = iSelected;
		}
		
		public Row(String name)
		{
			this(name,false);
		}
	}

	public Map<String, Boolean> getInputAsPair()
	{
		Map<String,Boolean> input = new HashMap<>();
		rowList.forEach(r -> input.put(r.name, r.isSelected));
		return input;
	}
	
	public void setOptions(List<String> options)
	{
		Map<String, Boolean> oldInput = getInputAsPair();
		rowList.clear();
		options.forEach(o -> rowList.add(new Row(o)));
		
		
		List<String> toSelect = new ArrayList<>();
		for (String name : oldInput.keySet())
		{
			if (oldInput.get(name))
			{
				toSelect.add(name);
			}
		}
		
		setSelectedValues(toSelect);
	}
	
	public void clearAll()
	{
		rowList.clear();
	}

	public List<String> getAllRowNames() {
		return new ArrayList<>(getInputAsPair().keySet());
	}

	public int getIndexOf(String name) {
		for (int i = 0; i < rowList.size(); i++)
		{
			if (rowList.get(i).name.equals(name))
			{
				return i;
			}
		}
		return -1;
	}

	
	boolean isEdditingEnabled = true;
	public void setButtonsEnabled(boolean enabled)
	{
		isEdditingEnabled = enabled;
	}
	

	
}