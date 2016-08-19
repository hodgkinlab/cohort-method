package edu.wehi.graphplot.plot;

public class GPCoordinateProxy implements GPCoordinateWithName {

	public final GPCoordinateWithName coord;
	
	public GPCoordinateProxy(GPCoordinateWithName coord)
	{
		this.coord = coord;
	}
	
	@Override
	public double getX() {
		return coord.getX();
	}

	@Override
	public double getY() {
		return coord.getY();
	}


	@Override
	public String getName()
	{
		return coord.getName();
	}
	
	double valLower = 0.0;
	double valUpper = 0.0;
	public void setUpperAndLower(double val)
	{
		this.valLower = val;
	}
	
	public double getYLower()
	{
		return getY() + valLower;
	}
	
	public double getYUpper()
	{
		return getY() + valUpper;
	}

	@Override
	public void setYLower(double valLower)
	{
		this.valLower = valLower;	
	}

	@Override
	public void setYUpper(double valUpper) {
		this.valUpper = valUpper;
	}


}
