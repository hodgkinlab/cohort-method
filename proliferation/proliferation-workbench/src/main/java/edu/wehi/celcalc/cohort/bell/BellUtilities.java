package edu.wehi.celcalc.cohort.bell;

import java.util.ArrayList;
import java.util.List;

import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPCoordinateWithNameImp;
import edu.wehi.graphplot.plot.GPXYSeries;

public class BellUtilities
{

	/**
	 * Returns the estimated mean and standard deviation
	 * of a series representing a histogram.
	 * @param series
	 * @return
	 */
	public static double[] bell(GPXYSeries series)
	{
		double[] x = series.getAllXCoordinates();
		double[] y = series.getAllYCoordinates();
		
		if (x.length != y.length) throw new RuntimeException("x and y must be of the same length");
		
		double n = 0;
		for (int i = 0; i < y.length; i++) n+=y[i];
		
		int nel = x.length;
		
		double sumXY = 0;
		for (int i = 0; i < nel; i++) sumXY += x[i] * y[i];
		double mu = sumXY / n;
		
		double var = 0;
		for (int i = 0; i < nel; i ++)
		{
			var += y[i]*Math.pow(x[i] - mu, 2);
		}
		var = var/(n - 1);
		
		double sigma = Math.sqrt(var);
		
		return new double[]{mu, sigma, n};
	}
	
	public static double norm(double x, double mu, double sigma)
	{
		return Math.exp(- 0.5 * Math.pow(((x - mu) / sigma),2)) / (sigma * Math.sqrt(2 * Math.PI));
	}
	
	public static GPXYSeries normFit(String name, double mu, double sigma, double start, double end, double sFactor, int n)
	{
		List<GPCoordinateWithName> coords = new ArrayList<>();
		double diff = (end - start)/n;
		for (int i = 0; i <= n; i++)
		{
			double x = start + i * diff;
			double y = norm(x, mu, sigma)*sFactor;
			coords.add(new GPCoordinateWithNameImp(x, y, 0, ""));
		}
		return new GPXYSeries(name, coords);
	}
	
}
