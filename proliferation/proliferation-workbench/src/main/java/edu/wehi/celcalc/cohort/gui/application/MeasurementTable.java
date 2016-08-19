package edu.wehi.celcalc.cohort.gui.application;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.MeasurementTableModel;

public class MeasurementTable extends JTable
{
	private static final long serialVersionUID = 1L;
	
	MeasurementTableModel mesModel = null;
	
	public MeasurementTable()
	{
		super();
		setAutoCreateRowSorter(true);
	}
	
	
	public MeasurementTableModel getMeasurementTableModel()
	{
		return mesModel;
	}
	
	public void setMeasurementTableModel(MeasurementTableModel model)
	{
		setModel(model);
		mesModel = model;
	}
	
	@Override
	public void setModel(TableModel dataModel)
	{
		this.removeEditor();
		super.setModel(dataModel);
		if (this.getColumnCount()>2)
		{
			TableColumn column = this.getColumnModel().getColumn(3);
			CellType[] values = CellType.values();
			column.setCellEditor(new MyComboBoxEditor(values));
			column.setCellRenderer(new MyComboBoxRenderer<CellType>(values));
		}
		updateRowHeights();
	};
	
	private void updateRowHeights()
	{
	    try
	    {
	        for (int row = 0; row < getRowCount(); row++)
	        {
	            int rowHeight = getRowHeight();

	            for (int column = 0; column < getColumnCount(); column++)
	            {
	                Component comp = prepareRenderer(getCellRenderer(row, column), row, column);
	                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
	            }

	            setRowHeight(row, rowHeight);
	        }
	    }
	    catch(ClassCastException e)
	    {}
	}
	
	
	// http://www.java2s.com/Tutorial/Java/0240__Swing/GetDefaultCellEditor.htm
	
	class MyComboBoxEditor extends DefaultCellEditor
	{
		private static final long serialVersionUID = 1L;

		public <T> MyComboBoxEditor(T[] items)
		{
		    super(new JComboBox<T>(items));
		}
	}
	
	
	
	class MyComboBoxRenderer<T> extends JComboBox<T> implements TableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public MyComboBoxRenderer(T[] items)
		{
		   super(items);
		   setOpaque(false);
			setUI(new MetalComboBoxUI()
			{
				@Override
				public void paintCurrentValueBackground(Graphics g,	Rectangle bounds, boolean hasFocus)
				{
					if (MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme)
					{
					}
					else if (g == null || bounds == null)
					{
						throw new NullPointerException("Must supply a non-null Graphics and Rectangle");
					}
				}
			});
		}

		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		  {
			  if (isSelected)
			  {
		            super.setBackground(table.getSelectionBackground());
		            super.setForeground(table.getForeground());
		        } else {
		            setForeground(table.getForeground());
		            setBackground(table.getBackground());
		        }
			  
		    setSelectedItem(value);
		    return this;
		  }
		}



	public void setSelectedMeasurement(Measurement mes)
	{
		int i = mesModel.getRowOf(mes);
		setRowSelectionInterval(i, i);
	}


	public List<Measurement> getSelectedMeasurements()
	{
		int[] rows = getSelectedRows();
		List<Measurement> measurements = new ArrayList<>();
		for (int r : rows)
		{
			measurements.add(getMeasurementTableModel().getMeasurements().get(r));
		}
		return measurements;
	}
	

}
