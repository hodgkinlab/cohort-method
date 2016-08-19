package edu.wehi.celcalc.cohort.gui.analyizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;





import java.util.Set;

import javax.swing.table.AbstractTableModel;

import edu.wehi.graphplot.python.NameableMap;

public class KeyValueMapTable extends AbstractTableModel
{

	private static final long serialVersionUID = 1L;
	
	
	final List<NameableMap> maps;
	final List<String> keys = new ArrayList<>();
	
	public KeyValueMapTable(List<NameableMap> maps)
	{
		this.maps = maps;
		Collections.sort(maps);
		
		Set<String> keySet = new HashSet<>();
		for (NameableMap map : maps)
		{
			keySet.addAll(map.keySet());
		}
		keys.addAll(keySet);
		Collections.sort(keys);
	}
	

	@Override
	public int getColumnCount()
	{
		return maps.size()+1;
	}

	@Override
	public int getRowCount()
	{
		return keys.size();
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		if (col == 0)
		{
			return keys.get(row);
		}
		else
		{
			col -= 1;
			String key = keys.get(row);
			return maps.get(col).getOrDefault(key, "");
		}
	}
	
	@Override
	public String getColumnName(int i)
	{
		if (i ==0)
		{
			return "key";
		} 
		else
		{
			return maps.get(i-1).getName();
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

}
