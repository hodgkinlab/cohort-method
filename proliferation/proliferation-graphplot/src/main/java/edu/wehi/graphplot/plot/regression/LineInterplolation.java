package edu.wehi.graphplot.plot.regression;

import java.util.HashMap;
import java.util.Map;

public class LineInterplolation {
	
	class AB{ double a; double b; AB(double a, double b){this.a=a; this.b=b;}};
	
	Map<Double, AB> collection = new HashMap<>();
	
	final double[] xCoords, yCoords;
	final int sz;
	
	public LineInterplolation(double[] xCoords, double[] yCoords)
	{
		this.xCoords = xCoords;
		this.yCoords = yCoords;
		this.sz = xCoords.length;
	}
	
	public double interp(double xVal)
	{
		double y0 = 0, y1 = 0;
		double x0 = 0, x1 = 0;
		
		if (xVal <  xCoords[0])
		{
			x0 = xCoords[0];
			x1 = xCoords[1];
			y0 = yCoords[0];
			y1 = xCoords[1];
		}
		else if (xVal > xCoords[sz-1])
		{
			x0 = xCoords[sz-2];
			x1 = xCoords[sz-1];
			y0 = yCoords[sz-2];
			y1 = xCoords[sz-1];
		}
		else
		{
			for (int i = 0; i < sz; i++)
			{
				
			}
		}
		
		

		double m = (y1 - y0)/(x1 - x0);
		double c = y0 - x0*m;
		
		return m*xVal + c;
	}
	
	

}
