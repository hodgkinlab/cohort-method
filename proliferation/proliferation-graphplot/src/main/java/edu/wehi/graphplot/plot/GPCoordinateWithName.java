package edu.wehi.graphplot.plot;

public interface GPCoordinateWithName extends GPCoordinate {
	
	public String getName();
	
	public static GPCoordinateWithName add(GPCoordinateWithName coord1, GPCoordinateWithName coord2)
	{
		
		if (coord1.getX() != coord2.getX())
		{
			
			throw new RuntimeException("Error coordinates x component do not match "+
			"\n coord1.getX() = "+coord1.getX() +
			"\n coord2.getX() = "+coord2.getX());
		}
		
		return new GPCoordinateWithNameImp(
				coord1.getX(),
				coord1.getY() + coord2.getY(),
				0.0,
				"("+coord1.getName()
				+"+"+
				coord2.getName()+")");
	}
	
	public default void setUpperAndLower(double val){}

}
