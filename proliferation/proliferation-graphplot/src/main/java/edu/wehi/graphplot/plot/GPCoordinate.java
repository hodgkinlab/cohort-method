package edu.wehi.graphplot.plot;

public interface GPCoordinate extends Comparable<GPCoordinate>{
	
	public double getX();
	public double getY();
	
	public static GPCoordinate add(GPCoordinate coord1, GPCoordinate coord2)
	{
		if (coord1.getX() != coord2.getX())
		{
			throw new RuntimeException("Error coordinates x component do not match");
		}
		
		return new GPCoordinateWithNameImp(
				coord1.getX(),
				coord1.getY() + coord2.getY(),
				0.0,
				"null");
	}
	
	
	@Override
	public default int compareTo(GPCoordinate arg0) {
		double result = this.getX()- arg0.getX();
		
		if (result < 0.0 && result > -1.0)
		{
			return -1;
		}
		if (result > 0.0 && result < -1.0)
		{
			return 1;
		}
		
		return (int) (result);
	}
	
	public default double getYLower(){
		return getY();
	}
	
	public default double getYUpper(){
		return getY();
	}
	
	public void setYLower(double y);
	public void setYUpper(double y);
	
	public default String getKeyCode()
	{
		return getX() + "-" + getY();
	}

}
