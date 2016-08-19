package edu.wehi.swing;

import javax.swing.JTable;


public class JTableSortable extends JTable {

	private static final long serialVersionUID = 1L;
	
	public JTableSortable()
	{
		setAutoCreateRowSorter(true);
	}
	
}
