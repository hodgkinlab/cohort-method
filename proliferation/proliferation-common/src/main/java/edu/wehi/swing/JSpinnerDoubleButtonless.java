package edu.wehi.swing;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicArrowButton;

public class JSpinnerDoubleButtonless extends JSpinner
{
	private static final long serialVersionUID = 1L;

	public JSpinnerDoubleButtonless(double min, double max, double value)
	{
		super(new SpinnerNumberModel(value, min, max, 0.0));

		// remove annoying arrow buttons 
        for (Component comp : getComponents())
        {
        	if (comp instanceof BasicArrowButton)
        	{
        		remove(comp);
        	}
        }
        
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor)getEditor();
        DecimalFormat format = editor.getFormat();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(100);
        
	}
	
	public void setMax(double max)
	{
		setModel(new SpinnerNumberModel(0.0, (double)getValue(), max, 0.0 ));
	}

}
