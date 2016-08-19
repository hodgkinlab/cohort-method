package edu.wehi.swing.checkboxtable;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.wehi.GUI;
import edu.wehi.swing.checkboxtable.CheckBoxTableModel.Row;


/**
 * @author futschik.d
 * 
 * JTable implementation of a JList which allows for multiple options
 *
 */
public class CheckBoxTable extends JTable
{
	private static final long serialVersionUID = 1L;

	final CheckBoxTableModel checkModel;
	final AbstractAction space_action;
	final String col1Heading;
	final String col2Heading;
	
	public CheckBoxTable(String[] rows, String col1Heading, String col2Heading)
	{
		this(Arrays.asList(rows), col1Heading, col2Heading);
	}
	
	public CheckBoxTable(String[] rows)
	{
		this(Arrays.asList(rows), "tag", "on");
	}
	
	public CheckBoxTable(List<String> rows, String col1Heading, String col2Heading)
	{
		this(new CheckBoxTableModel(rows,col1Heading, col2Heading), col1Heading, col2Heading);
	}
	
	public CheckBoxTable(List<String> rows)
	{
		this(rows,"","");
	}
	
	public CheckBoxTable(CheckBoxTableModel model, String col1Heading, String col2Heading)
	{
		super(model);
		
		this.col1Heading = col1Heading;
		this.col2Heading = col2Heading;
		this.checkModel = model;
		space_action = new AbstractAction() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (!enabledSpace)
				{
					return;
				}
				
				int[] rows = getSelectedRows();

				boolean hasSelected = false;
				boolean allSelected = true;
				for (int r : rows)
				{
					hasSelected |= checkModel.rowList.get(r).isSelected;
					allSelected &= checkModel.rowList.get(r).isSelected;
				}

				if (!hasSelected || allSelected)
				{
					for (int r : rows)
					{
						checkModel.rowList.get(r).isSelected = !checkModel.rowList.get(r).isSelected;
					}
				} else
				{
					for (int r : rows)
					{
						checkModel.rowList.get(r).isSelected = true;
					}
				}
				getCheckBoxTableModel().fireTableDataChanged();
				updateUI();
			}
		};
		
		model.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent arg0) {
				listeners.forEach(l -> l.notified(model.getInputAsPair()));
			}
		});
		
		getColumnModel().getColumn(1).setMaxWidth(50);
		
		init();
	}
	
	boolean enabledSpace = true;
	@Override
	public void setEnabled(boolean enabled)
	{
		this.enabledSpace = enabled;
		checkModel.setButtonsEnabled(enabled);
	}
	

	public void init()
	{
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space");
		getActionMap().put("space", this.space_action);
	}
	
	@Override
	public String getColumnName(int col)
	{
		if (col == 0)
		{
			return col1Heading;
		}
		else
		{
			return col2Heading;
		}
	}
	
	public CheckBoxTableModel getCheckBoxTableModel()
	{
		return checkModel;
	}
	
	public void setSelected(boolean isSelected)
	{
		setSelectedNoFireChange(isSelected);
		checkModel.fireTableDataChanged();
	}
	
	public void setSelectedNoFireChange(boolean isSelected)
	{
		for (Row row : checkModel.rowList)
		{
			row.isSelected = isSelected;
		}
		revalidate();
		repaint();
	}
	
	public List<String> getSelectedRowNames()
	{
		return checkModel.getSelectedRowNames();
	}
	
	List<CheckboxTableListener> listeners = new ArrayList<>();
	public void addCheckboxTableListener(CheckboxTableListener l)
	{
		listeners.add(l);
	}
	
	public void removeCheckboxTableListener(CheckboxTableListener l)
	{
		listeners.remove(l);
	}
	
	public static void main(String[] args)
	{
		GUI.scroll(new CheckBoxTable(new String[]{"row1","row2","row3","row4","row5","row6","row7"}, "info", "selected"));
	}

	public String getHighlightedRow()
	{
		return (String) checkModel.getValueAt(getSelectedRow(), 0);
	}

	public void setSelectedNoFireChange(List<String> selections, boolean isSelected) {
		if (selections == null)
		{
			return;
		}
		for (Row row : checkModel.rowList)
		{
			if (selections.stream().anyMatch(s -> s.equals(row.name)))
			{
				row.isSelected = isSelected;
			}
		}
	}

	public void setSelectedRow(String name)
	{
		int r = checkModel.getIndexOf(name);
		setRowSelectionInterval(r, r);
	}
	
}