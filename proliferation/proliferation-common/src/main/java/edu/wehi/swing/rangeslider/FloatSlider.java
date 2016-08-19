package edu.wehi.swing.rangeslider;

import javax.swing.JSlider;

public class FloatSlider extends JSlider
{
	private static final long serialVersionUID = 1L;
	
	final int n;
	
	public FloatSlider(double min, double max, int n)
	{
		super(0,n-1);
		this.n = n;
	}
	
	public double getValueAsFloat()
	{
		return ((double)this.getValue()*n) / 1000;
	}

}
