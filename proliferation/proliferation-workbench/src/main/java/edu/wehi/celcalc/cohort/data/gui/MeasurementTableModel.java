package edu.wehi.celcalc.cohort.data.gui;

import java.io.Serializable;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;

public class MeasurementTableModel extends AbstractTableModel
implements Serializable 
{

	private static final long serialVersionUID = 1L;

	final List<Measurement> measurements;

	public MeasurementTableModel(List<Measurement> measurements)
	{
		this.measurements = measurements;
	}
	
	public List<Measurement> getMeasurements()
	{
		return measurements;
	}

	@Override
	public int getColumnCount()
	{
		return 6;
	}

	@Override
	public int getRowCount()
	{
		return measurements.size();
	}
	
	public int getRowOf(Measurement mes)
	{
		for (int i = 0; i < measurements.size(); i++)
		{
			if (measurements.get(i) == mes)
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		Measurement mes = measurements.get(row);
		switch (col) {
		case 0:
			return mes.getTime();
		case 1:
			return mes.getCells();
		case 2:
			return mes.getDiv();
		case 3:
			return mes.getType();
		case 4:
			return mes.getTreatment();
		case 5:
			return mes.isIncluded();
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int i)
	{
		switch (i) {
		case 0:
			return "Time";
		case 1:
			return "Cel";
		case 2:
			return "Div";
		case 3:
			return "Type";
		case 4:
			return "Treat";
		case 5:
			return "Inc";
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
		
		Measurement mes = measurements.get(row);
		
		switch(col)
		{
		case 0:
			mes.setTime((Double)value);
			break;
		case 1:
			mes.setCells((Double) value);
			break;
		case 2:
			mes.setDiv((Integer) value);
			break;
		case 3:
			mes.setType((CellType) value);
			break;
		case 4:
			mes.setTreatment(value.toString());
			break;
		case 5:
			mes.setIncluded((Boolean)value);
		default:
			return;
		}
		fireTableCellUpdated(row, col);
	}
	
	@Override
	public Class<?> getColumnClass(int col)
	{
		switch(col)
		{
		case 0:
			return Double.class;
		case 1:
			return Double.class;
		case 2:
			return Integer.class;
		case 3:
			return Enum.class;
		case 4:
			return String.class;
		case 5:
			return Boolean.class;
		default:
			return null;
		}
	}

}
