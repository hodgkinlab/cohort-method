package edu.wehi.swing;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class JSpinnerInteger extends JSpinner
{

	private static final long serialVersionUID = 1L;
	
	int max;
	
	public JSpinnerInteger(int min, int max, int value)
	{
		super(new SpinnerNumberModel(value, min, max, 1));
		this.max = max;
	}
	
	public void setMax(int max)
	{
		setModel(new SpinnerNumberModel(0, (int)getValue(), max, 1));
		this.max = max;
	}

	public Integer getMaximumValue()
	{
		return max;
	}
	

}
