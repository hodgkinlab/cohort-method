package edu.wehi.graphplot.plot;

import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;

public class GPCoordinateWithNameImp implements GPCoordinateWithName
{

	final double x;
	final double y;
	final String name;
	double std;
	
	public GPCoordinateWithNameImp(double x, double y, double std, String name)
	{
		super();
		this.x = x;
		this.y = y;
		this.std = std;
		this.name = name;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public double getYUpper()
	{
		return this.getY() + std + upper;
	}
	
	@Override
	public double getYLower()
	{
		return this.getY() - std - lower;
	}

	@Override
	public void setUpperAndLower(double val)
	{
		this.std = val;
	}

	double lower = 0.0;
	@Override
	public void setYLower(double lower) {
		this.lower = lower;
	}

	double upper = 0.0;
	@Override
	public void setYUpper(double upper) {
		this.upper = upper;
	}

	// TBD get rid of using List<GPCoordinateWithName> altogether
	public static List<GPCoordinateWithName> listFromVectors(
			SimpleMatrix x, SimpleMatrix y) {

		List<GPCoordinateWithName> coordList = new ArrayList<>();
		int n = x.getNumElements();
		for (int i = 0; i < n; i++) {
			double xCoord = x.get(i, 0);
			double yCoord = y.get(i, 0);
			GPCoordinateWithNameImp point
					= new GPCoordinateWithNameImp(xCoord, yCoord, 0.0, "");
			coordList.add(point);
		}
		return coordList;
	}
}
