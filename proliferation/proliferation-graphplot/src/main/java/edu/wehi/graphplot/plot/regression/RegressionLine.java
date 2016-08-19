package edu.wehi.graphplot.plot.regression;

import java.util.ArrayList;
import java.util.List;

import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPCoordinateWithNameImp;
import edu.wehi.graphplot.plot.GPXYSeries;

public class RegressionLine
{

	double a, b;
	
	public RegressionLine(double a, double b)
	{
		super();
		this.a = a;
		this.b = b;
	}

	public double getA()
	{
		return a;
	}

	public void setA(double a)
	{
		this.a = a;
	}

	public double getB()
	{
		return b;
	}

	public void setB(double b) 
	{
		this.b = b;
	}
	
	public static RegressionLine regression(double[] xcoords, double[] ycoords)
	{
		if (xcoords.length != ycoords.length)
		{
			return null;//throw new IllegalArgumentException("Input constraint:  x.length != y.length");
		}
		
		if (xcoords.length == 0)
		{
			return null; //throw new IllegalArgumentException("Input constraint:  length of elements must be greater than 0");
		}
		
		final int n = xcoords.length;
		
		double sumX = 0.0;
		double sumY = 0.0;
		double sumXY = 0.0;
		double sumX2 = 0.0;
		double sumY2 = 0.0;
		
		for (int i = 0; i < n; i++)
		{
			double x = xcoords[i];
			double y = ycoords[i];
			
			sumX += x;
			sumY += y;
			sumXY += x*y;
			sumX2 += x*x;
			sumY2 += y*y;
		}
		
		double a = (n*sumXY - sumX*sumY)/
				   (n*sumX2 - sumX*sumX);
		
		double b = (sumY - a*sumX)/n;
		
		double r =       (n * sumXY - sumX*sumY) /
				Math.sqrt((n*sumX2 - sumX*sumX) * (n*sumY2 - sumY*sumY));		
		RegressionLine line = new RegressionLine(a,b);
		line.setR2(r*r);
		
		return line;
	}

	public List<GPCoordinateWithName> series(double[] allXCoordinates) 
	{
		List<GPCoordinateWithName> coords = new ArrayList<>();
		
		for (int i = 0; i < allXCoordinates.length; i++)
		{
			coords.add(new GPCoordinateWithNameImp(allXCoordinates[i], allXCoordinates[i]*a + b, 0.0, ""));
		}
		
		return coords;
	}

	public static RegressionLine regression(List<? extends GPCoordinateWithName> coordinates)
	{
		int n = coordinates.size();
		
		double[] x = new double[n];
		double[] y = new double[n];
		
		for (int i = 0; i < n; i ++)
		{
			x[i] = coordinates.get(i).getX();
			y[i] = coordinates.get(i).getY();
		}
		return regression(x,y);
	}
	
	@Override
	public String toString()
	{
		return a + "x + " + b;
	}
	
	public List<? extends GPCoordinateWithName> series(List<? extends GPCoordinateWithName> filteredCoordsMiddle)
	{
		if (filteredCoordsMiddle == null) return null;
		int n = filteredCoordsMiddle.size();
		
		if (n == 0) return null;
		
		double[] x = new double[n];
		
		for (int i = 0; i < n; i++)
		{
			x[i] = filteredCoordsMiddle.get(i).getX();
		}
		
		return series(x);
	}
	
	public static void main(String[] args)
	{
		RegressionLine reg = RegressionLine.regression(
				new double[]{-2, 1, 3},
				new double[]{-1, 1 ,2});
		
		System.out.println(reg);
	}

	String name;
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

	double r2 = 0.0;
	
	public double getR2() {
		return r2;
	}
	
	public void setR2(double r2)
	{
		this.r2 = r2;
	}

	public double findR2(GPXYSeries gpxySeries)
	{
		double r2 = 0.0;
		
		for (GPCoordinateWithName coord : gpxySeries.coordinates)
		{
			r2 += Math.pow(coord.getY() - (coord.getX()*a + b), 2.0);
		}
		
		this.r2 = r2;
		return r2;
	}

}
