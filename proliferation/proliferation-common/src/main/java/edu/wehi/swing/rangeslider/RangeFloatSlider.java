package edu.wehi.swing.rangeslider;

public class RangeFloatSlider extends RangeSlider
{

	private static final long serialVersionUID = 1L;
	
	final double diff;
	final double shift;
	final double maxVal;
	final double minVal;
	final int n;
	
	public RangeFloatSlider(double minVal, double maxVal, int n)
	{
		setMinimum(0);
		setMaximum(n);
		this.n = n;
		diff = maxVal - minVal; 
		shift = minVal;
		this.maxVal = maxVal;
		this.minVal = minVal;
		
        setValue(0);
        setUpperValue(n);
		
	}
	
	public double getValueAsDouble()
	{
		return minVal + this.getValue() * diff/n;
	}
	
	public double getUpperValueAsDouble()
	{
		return minVal + this.getUpperValue() * diff/n;
	}

}
